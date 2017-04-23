package com.cmn.cmn.component;

import java.util.Map;
import java.util.HashMap;
import java.util.Date;
import java.text.SimpleDateFormat;

import java.lang.Class;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

import org.apache.log4j.Logger;
import org.apache.ibatis.session.SqlSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.scheduling.annotation.Async;

import com.cmn.cmn.batch.Batch;
import com.cmn.cmn.dao.BatchExeProcessDao;

@Component
public class BatchExeProcessComponent {
  @Autowired
  private SqlSession sqlSession;
  
  @Autowired
  private BatchExeProcessDao batchExeProcessDao;
  
  private static Logger logger = Logger.getLogger(BatchExeProcessDao.class);

  @Async
  public void batchExeProcess(long exeDateTime, String className, int batchNum) {
    logger.debug("============   Start method of BatchExeManageComponent.batchProcess   ============");
    logger.debug(" Parameter - exeDateTime[" + exeDateTime + "], className[" + className + "], batchNum[" + batchNum + "]");
    SimpleDateFormat format = null;
    Map<String, Object> inputMap = null;
    String report = null;
    Batch batch = null;
    Class<Batch> clas;
    Constructor<Batch> constructor = null;
    Object object = null;
    Method method = null;
    try {
      format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
      inputMap = new HashMap<String, Object>();
      inputMap.put("batch_num", batchNum);
      inputMap.put("exe_dtm", new Date(exeDateTime));
      inputMap.put("ap_num", System.getProperty("apnum"));
      inputMap.put("container_num", System.getProperty("containernum"));
      batchExeProcessDao.insertBatchExeProcess(inputMap);
      inputMap.clear();
      batch = (Batch)(((Class<Batch>)Class.forName(className)).getConstructor()).newInstance();
      batch.setBatchNum(batchNum);
      batch.setSqlSession(sqlSession);
      batch.setExeDtm(exeDateTime);
    } catch (Throwable e) {
      inputMap.clear();
      inputMap.put("batch_num", batchNum);
      if (e instanceof InvocationTargetException) {
        e = ((InvocationTargetException)e).getTargetException();
      }
      try {
        logger.error("=================     Internal Exception Start    ==================");
        logger.error("Batch Number[" + batchNum + "]");
        logger.error("Date[" + format.format(new Date(exeDateTime)) + "]");
        logger.error("Current Time[" + format.format(new Date()) + "]");
        logger.error("Error Trace!!");
        logger.error("" + e.getClass().getName() == null ? "" : e.getClass().getName() + ": " + e.getMessage() == null ? "" : e.getMessage());
        StackTraceElement[] ste = e.getStackTrace();
        for (int i = 0; i < ste.length; i++) {
          logger.error("       at " + ste[i].toString());
        }
        logger.error("=================      Internal Exception End     ==================");
        return;
      } catch (Exception e2) {
        logger.error(e2.toString());
        return;
      }
    }
    try {
      batch.run(exeDateTime, new String[1]);
    } catch (Exception e) {
      String errReport = "";
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
      try {
        inputMap.put("err_num", batch.getErrNum());
      } catch (Exception e2) {
        logger.error("=================     Internal Exception Start    ==================");
        logger.error("Batch Number[" + batchNum + "]");
        logger.error("Date[" + format.format(new Date(exeDateTime)) + "]");
        logger.error("Current Time[" + format.format(new Date()) + "]");
        logger.error("Error Trace!!");
        logger.error(e2.toString());
        StackTraceElement[] ste2 = e2.getStackTrace();
        for (int i = 0; i < ste2.length; i++) {
          logger.error("       at " + ste2[i].toString());
        }
        logger.error("=================      Internal Exception End     ==================");
      }
      try {
        batchExeProcessDao.updateErrReport(inputMap);
      } catch (Exception e2) {
        logger.error("=================     Internal Exception Start    ==================");
        logger.error("Batch Number[" + batchNum + "]");
        logger.error("Date[" + format.format(new Date(exeDateTime)) + "]");
        logger.error("Current Time[" + format.format(new Date()) + "]");
        logger.error("Error Trace!!");
        logger.error(e2.toString());
        StackTraceElement[] ste2 = e2.getStackTrace();
        for (int i = 0; i < ste2.length; i++) {
          logger.error("       at " + ste2[i].toString());
        }
        logger.error("=================      Internal Exception End     ==================");
        return;
      }
      return;
    }
    try {
      report = batch.getReport();
      inputMap.clear();
      inputMap.put("batch_num", batchNum);
      inputMap.put("exe_dtm", new Date(exeDateTime));
      inputMap.put("batch_result_report", report);
      batchExeProcessDao.updateBatchFinishResult(inputMap);
      inputMap.clear();
    } catch (Exception e) {
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
  }
}