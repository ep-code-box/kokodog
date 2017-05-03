/*
 * Title : BatchExeManageComponent
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
import java.util.List;
import java.util.Date;
import java.util.Random;
import java.text.SimpleDateFormat;
import java.security.SecureRandom;

import javax.annotation.PreDestroy;

import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import com.cmn.cmn.component.BatchExeProcessComponent;
import com.cmn.cmn.component.AddoptInfoComponent;
import com.cmn.cmn.dao.BatchExeManageDao;
import com.cmn.cmn.service.GetServerTimeService;

/**
 *  이 객체는 Spring 기동 시에 배치 관리를 할 수 있는 구동 관리 객체이다.<br/>
 *  tomcat 기동시에 해당 객체 내 backgroundProcess 메서드를 수행하면 테이블 내에 cmn_batch 테이블에 등록되어있는 배치 수행 대상이
 *  수행 관리된다.<br/>
 *  이 객체를 위해서는 DB Table에 하기 스크립트를 갖는 테이블이 미리 생성되어 있어야 한다.
 *  1번 : 테이블명 - cmn_batch, 설명 - 수행해야 할 배치 리스트를 정의한다. <br/>
 *  CREATE TABLE `cmn_batch` (<br/>
 *   `batch_num` int(5) NOT NULL,<br/>
 *   `eff_end_dtm` datetime NOT NULL,<br/>
 *   `audit_id` int(10) NOT NULL,<br/>
 *   `audit_dtm` datetime NOT NULL,<br/>
 *   `batch_nm` varchar(80) NOT NULL,<br/>
 *   `batch_exe_nm` varchar(200) NOT NULL,<br/>
 *   `start_time` time DEFAULT NULL,<br/>
 *   `end_time` time DEFAULT NULL,<br/>
 *   `period` int(5) DEFAULT NULL,<br/>
 *   `week_yn` varchar(1) NOT NULL,<br/>
 *   `holy_yn` varchar(1) NOT NULL,<br/>
 *   `auto_rerun` varchar(1) NOT NULL,<br/>
 *   `exe_param` varchar(255) DEFAULT NULL,<br/>
 *   `eff_sta_dtm` datetime NOT NULL<br/>
 * ) ENGINE=InnoDB DEFAULT CHARSET=utf8;<br/>
 *  <br/>
 *  2번 : 테이블명 - cmn_batch_exe_hst, 설명 - 배치 수행 리포트를 관리한다. <br/>
 *  CREATE TABLE `cmn_batch_exe_hst` (<br/>
 *   `exe_dtm` datetime NOT NULL,<br/>
 *   `batch_num` int(5) NOT NULL,<br/>
 *   `seq` int(5) NOT NULL,<br/>
 *   `audit_id` int(10) NOT NULL,<br/>
 *   `audit_dtm` datetime NOT NULL,<br/>
 *   `real_exe_dtm` datetime NOT NULL,<br/>
 *   `real_end_dtm` datetime DEFAULT NULL,<br/>
 *   `batch_exe_state` int(2) NOT NULL,<br/>
 *   `batch_exe_err_num` int(11) DEFAULT NULL,<br/>
 *   `batch_result_report` mediumtext,<br/>
 *   `batch_proc_log_report` mediumtext,<br/>
 *   `batch_err_report` mediumtext,<br/>
 *   `ap_num` int(2) UNSIGNED NOT NULL,<br/>
 *   `container_num` int(2) UNSIGNED NOT NULL<br/>
 *  ) ENGINE=MyISAM DEFAULT CHARSET=utf8;<br/>
 *  모든 수행되는 배치는 com.cmn.cmn.batch.Batch를 상속받아 작성되어야 하며
 *  만약 해당 Class를 상속받지 않고 배치가 작성될 경우<br/>
 *  "수행하고자 하는 Batch 프로그램이 \"com.cmn.cmn.batch.Batch\"를 상속받아 구현되지 않음."
 *  메시지와 함께 익셉션 로그에 기록된다.</br>
 *  참고
 *  {@link com.cmn.cmn.batch.Batch com.cmn.cmn.batch.Batch}
 *  {@link com.cmn.cmn.component.BatchExeMangeComponentMain com.cmn.cmn.component.BatchExeMangeComponentMain}
 *  {@link com.cmn.cmn.component.BatchExeProcessComponent com.cmn.cmn.component.BatchExeProcessComponent}
 */
@Component
@EnableAsync
public class BatchExeManageComponent {
  @Autowired
  private BatchExeManageDao batchExeManageDao;
  
  @Autowired
  private GetServerTimeService getServerTimeService;
  
  @Autowired
  private BatchExeProcessComponent batchExeProcessComponent;
  
  @Autowired
  private AddoptInfoComponent addoptInfoComponent;
  private boolean isClosed;
  private static Logger logger = Logger.getLogger(BatchExeManageComponent.class);
  private static boolean isFirstRunning = true;

  /** 
   *  컨테이너 종료히세 해당 메서드를 호출한다.
   *  호출하지 않으면 컨테이너가 종료되지 않는다.
   */
  @PreDestroy
  public void setClose() {
    Map<String, Object> inputMap = new HashMap<String, Object>();
    int i = 0;
    try {
      inputMap.put("ap_num", System.getProperty("apnum"));
      inputMap.put("container_num", System.getProperty("containernum"));
      batchExeManageDao.updateAllBatchExeError(inputMap);
    } catch (Exception e) {
      StackTraceElement[] ste = e.getStackTrace();
      logger.error("=================     Internal Exception Start    ==================");
      logger.error("Current Time[" + new SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date()) + "]");
      logger.error("Error Trace!!");
      logger.error("Error Main Message : " + e.getMessage());
      logger.error(e.getClass().getName() + ": " + e.getMessage());
      for (i = 0; i < ste.length; i++) {
        logger.error("       at " + ste[i].toString());
      }
      logger.error("=================      Internal Exception End     ==================");
    }
    this.isClosed = true;
  }
  
  /** 
   *  백그라운드로 수행되면서 배치 잡을 시간순서대로 관리하는 역할을 수행한다.
   *  Configuration이 Async로 되어있으면 비동기로 수행되어 다른 업무와 함께 배치 잡 관리가 가능하다.
   */
  @Async
  public void backgroundProcess() {
    logger.debug("============   Start method of BatchExeManageComponent.run   ============");
    Map<String, Object> inputMap = new HashMap<String, Object>();
    isClosed = false;
    long lastMin = 0L;
    int i = 0;
    long lastSecondTemp = 0L;
    if (isFirstRunning == false) {
      return;
    }
    isFirstRunning = true;
    inputMap.clear();
    inputMap = new HashMap<String, Object>();
    try {
      inputMap.put("datetime", new Date(new Date().getTime() / 1000L / 60L * 1000L * 60L));
      inputMap.put("user_id", 0);
      inputMap.put("ap_num", System.getProperty("apnum"));
      inputMap.put("container_num", System.getProperty("containernum"));
      batchExeManageDao.updateBatchLockDateTimeToN(inputMap);
    } catch (Exception e) {
      StackTraceElement[] ste = e.getStackTrace();
      logger.error("=================     Internal Exception Start    ==================");
      logger.error("Current Time[" + new SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date()) + "]");
      logger.error("Error Trace!!");
      logger.error("Error Main Message : " + e.getMessage());
      logger.error(e.getClass().getName() + ": " + e.getMessage());
      for (i = 0; i < ste.length; i++) {
        logger.error("       at " + ste[i].toString());
      }
      logger.error("=================      Internal Exception End     ==================");
    }
    addoptInfoComponent.run();
    while (isClosed == false) {
      try {
        lastSecondTemp = getServerTimeService.getServerTime() / 1000L;
        if (lastSecondTemp / 60L > lastMin) {
          batchProcess(lastSecondTemp / 60L * 60L, Integer.parseInt(System.getProperty("apnum")), Integer.parseInt(System.getProperty("containernum")));
          lastMin = lastSecondTemp / 60L;
        }
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        inputMap.put("ap_num", System.getProperty("apnum"));
        inputMap.put("container_num", System.getProperty("containernum"));
        batchExeManageDao.updateAllBatchExeError(inputMap);
      } catch (Exception e) {
        StackTraceElement[] ste = e.getStackTrace();
        logger.error("=================     Internal Exception Start    ==================");
        logger.error("Current Time[" + new SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date()) + "]");
        logger.error("Error Trace!!");
        logger.error("Error Main Message : " + e.getMessage());
        logger.error(e.getClass().getName() + ": " + e.getMessage());
        for (i = 0; i < ste.length; i++) {
          logger.error("       at " + ste[i].toString());
        }
        logger.error("=================      Internal Exception End     ==================");
      } finally {
        continue;
      }
    }    
  }
  
  private void batchProcess(long currentMin, int apNum, int containerNum) throws Exception {
    logger.debug("============   Start method of BatchExeManageComponent.batchProcess   ============");
    logger.debug(" Parameter - currentMin[" + currentMin + "], apNum[" + apNum + "], containerNum[" + containerNum + "]");
    int i = 0;
    List<Map<String, Object>> outputList = null;
    Map<String, Object> inputMap = new HashMap<String, Object>();
    Map<String, Object> outputMap = null;
    int batchProcessListCnt = 0;
    double batchProcessPercent = 0.0;
    int preExeCnt = 0;
    int containerCnt = 0;
    Random random = null;
    int tempResult = 0;
    boolean allBatchProcess = false;
    inputMap.put("datetime", new Date(currentMin * 1000L));
    inputMap.put("user_id", 0);
    inputMap.put("ap_num", System.getProperty("apnum"));
    inputMap.put("container_num", System.getProperty("containernum"));
    long tmpServerTime = 0L;
    long batchCheckCurTime = getServerTimeService.getServerTime();
    do {
      tempResult = batchExeManageDao.insertBatchLockDateTime(inputMap);
      if (tempResult == 0) {
        tempResult = batchExeManageDao.updateBatchLockDateTimeToY(inputMap);
      }
      Thread.sleep(100);
      tmpServerTime = getServerTimeService.getServerTime();
    } while (tempResult == 0 && (tmpServerTime - batchCheckCurTime) < 1000L * 60L);
    if (tempResult == 0) {
      outputMap = batchExeManageDao.getLastBatchLockInfo();
      if (tmpServerTime - ((Date)outputMap.get("datetime")).getTime() > 1000L * 60L * 2L) {
        inputMap.put("datetime", new Date(currentMin * 1000L));
        inputMap.put("user_id", 0);
        inputMap.put("ap_num", System.getProperty("apnum"));
        inputMap.put("container_num", System.getProperty("containernum"));
        batchExeManageDao.updateBatchLockDateTimeToYWithY(inputMap);
      } else {
        return;
      }
    }
    try {
      inputMap.clear();
      inputMap.put("ap_num", apNum);
      inputMap.put("container_num", containerNum);
      inputMap.put("datetime", new Date(currentMin * 1000L));
      outputMap = batchExeManageDao.getIsExistPreBatchProcess(inputMap);
      if (outputMap == null || outputMap.get("is_exist") == null || outputMap.get("is_exist").equals("Y") == true) {
        return;
      }
      inputMap.put("ap_num", apNum);
      inputMap.put("container_num", containerNum);
      inputMap.put("user_num", 0);
      inputMap.put("datetime", new Date(currentMin * 1000L));
      batchProcessListCnt = batchExeManageDao.insertBatchProcessHstWithoutLast(inputMap);
      if (batchProcessListCnt == 0) {
        allBatchProcess = true;
        inputMap.put("ap_num", apNum);
        inputMap.put("container_num", containerNum);
        inputMap.put("user_num", 0);
        inputMap.put("datetime", new Date(currentMin * 1000L));
        batchExeManageDao.insertBatchProcessHst(inputMap);
      } else {
        allBatchProcess = false;
        inputMap.clear();
        inputMap.put("datetime", new Date(currentMin * 1000L));
        outputMap = batchExeManageDao.getCurrentBatchProcessInfo(inputMap);
        preExeCnt = ((Long)outputMap.get("pre_exe_cnt")).intValue();
        containerCnt = ((Long)outputMap.get("container_cnt")).intValue();
        batchProcessPercent = (double)preExeCnt / (double)containerCnt;
      }
      outputList = batchExeManageDao.getBatchNoExeList();
      for (i = 0; i < outputList.size(); i++) {
        if (allBatchProcess == true || i < outputList.size() / (containerCnt - preExeCnt + 1)) {
          batchExeProcessComponent.batchExeProcess(((Date)outputList.get(i).get("date_time")).getTime(), (String)outputList.get(i).get("batch_exe_nm")
                                                   , ((Integer)outputList.get(i).get("batch_num")).intValue(), (String)outputList.get(i).get("exe_param"));
        } else if (outputList.size() / (containerCnt - preExeCnt + 1) == i){
          try {
            random = SecureRandom.getInstance("SHA1PRNG");
          } catch (Exception e) {
            logger.error(e.getMessage());
            return;
          }
          if (random.nextDouble() <= (double)(outputList.size() % (containerCnt - preExeCnt + 1)) / (double)outputList.size()) {
            batchExeProcessComponent.batchExeProcess(((Date)outputList.get(i).get("date_time")).getTime(), (String)outputList.get(i).get("batch_exe_nm")
                                                     , ((Integer)outputList.get(i).get("batch_num")).intValue(), (String)outputList.get(i).get("exe_param"));
          }
        }
      }
      outputList.clear();
      outputList = null;
    } catch (Exception e) {
      throw e;
    } finally {
      inputMap.clear();
      inputMap.put("datetime", new Date(currentMin * 1000L));
      inputMap.put("user_id", 0);
      inputMap.put("ap_num", System.getProperty("apnum"));
      inputMap.put("container_num", System.getProperty("containernum"));
      batchExeManageDao.updateBatchLockDateTimeToN(inputMap);
    }
  }
}