/*
 * Title : BatchExeProcessComponent
 *
 * @Version : 1.0
 *
 * @Date : 2016-03-08
 *
 * @Copyright by 이민석
 */
package com.cmn.cmn.component;

import java.util.Map;
import java.util.HashMap;
import java.util.Date;
import java.text.SimpleDateFormat;

import java.lang.Class;
import java.lang.reflect.Constructor;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.scheduling.annotation.Async;

import com.cmn.cmn.batch.Batch;
import com.cmn.cmn.dao.BatchExeProcessDao;

/** 
 *  이 객체는 실제로 할당받은 배치를 실행하는 객체이다.<br/>
 *  batchExeProcess 메서드를 통해 실행해야 할 배치를 파라메터로 전달받는다.<br/>
 *  해당 메서드는 Async Annotation을 사용하여 비동기로 수행할 수도 있다.<br/>
 *  다수의 배치를 동시에 수행하여 관리받아야 하면 비동기로 수행할 수 있도록 한다.<br/>
 *  해당 메서드에서 발생하는 Exception은 Application Log에 작성되지만
 *  배치 내에서 발생하는 Exception은 배치 수행 이력 DB에 오류 로그가 작성된다.<br/>
 *  Batch를 상속받아 작성된 batch를 수행하며
 *  만약 Batch를 상속받지 않은 batch가 수행요청을 받으면<br/>
 *  이 객체는 Exception 로그를 작성하고 종료하므로 반드시 com.cmn.cmn.batch.Batch 클래스를 상속받아 작성하도록 주의한다.<br/>
 *  만약 오등록되어 배치명을 찾을 수가 없어도 Exception 로그를 작성하고 종료한다.
 *  참조
 *  {@link com.cmn.cmn.batch.Batch com.cmn.cmn.batch.Batch}
 *  {@link com.cmn.cmn.batch.BatchExeManageComponent com.cmn.cmn.batch.BatchExeManageComponent}
 */
@Component
public class BatchExeProcessComponent {
  @Autowired
  @Qualifier("sqlSessionFactoryForBatch")
  private SqlSessionFactory sqlSessionFactoryForBatch;
  
  @Autowired
  private BatchExeProcessDao batchExeProcessDao;
  
  private static Logger logger = LogManager.getLogger(BatchExeProcessDao.class);

  /**
   *  실제로 비동기로 수행되며 배치를 수행하는 역할을 한다.
   *  @param exeDateTime - 배치 수행 시각, 실제 수행시각과는 다르며 요청받은 시각을 의미하지만 정확히 요청받은 시각에 수행되지 않을 수 있다.
   *  @param className - 배치를 수행하고자 하는 클래스 명
   *  @param batchNum - 배치 수행 번호
   *  @param param - 요청 받은 배치에 전달받을 파라미터
   */
  @Async
  @SuppressWarnings("unchecked")
  public void batchExeProcess(long exeDateTime, String className, int batchNum, String param) {
    logger.debug("============   Start method of BatchExeManageComponent.batchProcess   ============");
    logger.debug(" Parameter - exeDateTime[" + exeDateTime + "], className[" + className + "], batchNum[" + batchNum + "]");
    Map<String, Object> inputMap = null;
    String report = null;
    Batch batch = null;
    Class<Batch> clas = null;
    SqlSession sqlSessionForBatch = null;
    SqlSession sqlSessionForLog = null;
    try {
      sqlSessionForLog = sqlSessionFactoryForBatch.openSession();
      sqlSessionForLog.getConnection().setAutoCommit(false);
      inputMap = new HashMap<String, Object>();
      inputMap.clear();
      inputMap.put("batch_num", batchNum);
      inputMap.put("exe_dtm", new Date(exeDateTime));
      inputMap.put("ap_num", System.getProperty("apnum"));
      inputMap.put("container_num", System.getProperty("containernum"));
      sqlSessionForLog.insert("com.cmn.cmn.insertBatchExeProcess", inputMap);
      sqlSessionForLog.getConnection().commit();
    } catch (Exception e) {
      errorLogForInternalLogic(batchNum, exeDateTime, e);
      if (sqlSessionForLog != null) {
        sqlSessionForLog.close();
      }
      return;
    }
    try {
      clas = (Class<Batch>)Class.forName(className);
    } catch (ClassNotFoundException e) {
      errorLogForBatch(batchNum, exeDateTime, new Exception("수행하고자 하는 Batch 프로그램이 없습니다."), null, sqlSessionForLog);
      if (sqlSessionForLog != null) {
        sqlSessionForLog.close();
      }
      return;
    }
    try {
      batch = (Batch)((Constructor<Batch>)(clas.getConstructor())).newInstance();
    } catch (ClassCastException e) {
      errorLogForBatch(batchNum, exeDateTime, new Exception("수행하고자 하는 Batch 프로그램이 \"com.cmn.cmn.batch.Batch\"를 상속받아 구현되지 않음."), null, sqlSessionForLog);
      if (sqlSessionForLog != null) {
        sqlSessionForLog.close();
      }
      return;
    } catch (Exception e) {
      errorLogForInternalLogic(batchNum, exeDateTime, e);      
      if (sqlSessionForLog != null) {
        sqlSessionForLog.close();
      }
      return;
    }
    try {
      batch.setBatchNum(batchNum);
      sqlSessionForBatch = sqlSessionFactoryForBatch.openSession();
      sqlSessionForBatch.getConnection().setAutoCommit(false);
      batch.setSqlSession(sqlSessionForBatch, sqlSessionForLog);
      batch.setExeDtm(exeDateTime);
    } catch (Exception e) {
      errorLogForInternalLogic(batchNum, exeDateTime, e);
      if (sqlSessionForBatch != null) {
        sqlSessionForBatch.close();
      }
      if (sqlSessionForLog != null) {
        sqlSessionForLog.close();
      }
      return;
    }
    try {
      batch.run(exeDateTime, param);
    } catch (Exception e) {
      try {
        sqlSessionForBatch.getConnection().rollback();
      } catch (Exception e2) {
        errorLogForInternalLogic(batchNum, exeDateTime, e2);
      }
      errorLogForBatch(batchNum, exeDateTime, e, batch, sqlSessionForLog);
      if (sqlSessionForBatch != null) {
        sqlSessionForBatch.close();
      }
      if (sqlSessionForLog != null) {
        sqlSessionForLog.close();
      }
      return;
    }
    try {
      sqlSessionForBatch.getConnection().commit();
      sqlSessionForLog.getConnection().commit();
      report = batch.getReport();
      inputMap.clear();
      inputMap.put("batch_num", batchNum);
      inputMap.put("exe_dtm", new Date(exeDateTime));
      inputMap.put("batch_result_report", report);
      sqlSessionForLog.update("com.cmn.cmn.updateBatchFinishResult", inputMap);
      sqlSessionForLog.getConnection().commit();
      inputMap.clear();
    } catch (Exception e) {
      errorLogForInternalLogic(batchNum, exeDateTime, e);
    } finally {
      if (sqlSessionForBatch != null) {
        sqlSessionForBatch.close();
      }
      if (sqlSessionForLog != null) {
        sqlSessionForLog.close();
      }
    }
  }
  
  private void errorLogForInternalLogic(int batchNum, long exeDateTime, Exception e) {
    SimpleDateFormat format = null;
    format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    logger.error("=================     Internal Exception Start    ==================");
    logger.error("Batch Number[" + batchNum + "]");
    logger.error("Date[" + format.format(new Date(exeDateTime)) + "]");
    logger.error("Current Time[" + format.format(new Date()) + "]");
    logger.error("Error Trace!!");
    logger.error(e.toString());
    StackTraceElement[] ste = e.getStackTrace();
    for (int i = 0; i < ste.length; i++) {
      logger.error("       at " + ste[i].toString());
    }
    logger.error("=================      Internal Exception End     ==================");    
  }

  private void errorLogForBatch(int batchNum, long exeDateTime, Exception e, Batch batch, SqlSession sqlSessionForLog) {
    Map<String, Object> inputMap = null;
    inputMap = new HashMap<String, Object>();
    String errReport = "";
    SimpleDateFormat format = null;
    format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    errReport = "=================     Internal Exception Start    ==================\n";
    errReport += "Batch Number[" + batchNum + "]\n";
    errReport += "Date[" + format.format(new Date(exeDateTime)) + "]\n";
    errReport += "Current Time[" + format.format(new Date()) + "]\n";
    errReport += "Error Trace!!\n";
    errReport += e.toString() + "\n";
    StackTraceElement[] ste = e.getStackTrace();
    for (int i = 0; i < ste.length; i++) {
      errReport += "       at " + ste[i].toString() + "\n";
    }
    errReport += "=================      Internal Exception End     ==================\n";
    inputMap.clear();
    inputMap.put("batch_num", batchNum);
    inputMap.put("exe_dtm", new Date(exeDateTime));
    inputMap.put("err_report", errReport);
    if (batch == null) {
      inputMap.put("err_num", 0);
    } else {
      inputMap.put("err_num", batch.getErrNum());
    }
    try {
      sqlSessionForLog.update("com.cmn.cmn.updateErrReport", inputMap);
      sqlSessionForLog.getConnection().commit();
    } catch (Exception e2) {
      errorLogForInternalLogic(batchNum, exeDateTime, e2);
    }
  }
}