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
import com.cmn.cmn.service.GetServerTimeService;
import com.cmn.cmn.dao.BatchExeManageDao;

@Component
@EnableAsync
public class BatchExeManageComponent {
  @Autowired
  private BatchExeManageDao batchExeManageDao;
  
  @Autowired
  private GetServerTimeService getServerTimeService;
  
  @Autowired
  private BatchExeProcessComponent batchExeProcessComponent;
  private boolean isClosed;
  private static Logger logger = Logger.getLogger(BatchExeManageComponent.class);
  private boolean isFirstRunning = true;

  @PreDestroy
  public void setClose() {
    Map<String, Object> inputMap = new HashMap<String, Object>();
    try {
      inputMap.put("ap_num", System.getProperty("apnum"));
      inputMap.put("container_num", System.getProperty("containernum"));
      batchExeManageDao.updateAllBatchExeError(inputMap);
    } catch (Exception e) {
      // sleep 오류 무시
    }
    this.isClosed = true;
  }
  
  @Async
  public void backgroundProcess() {
    logger.debug("============   Start method of BatchExeManageComponent.run   ============");
    Map<String, Object> inputMap = new HashMap<String, Object>();
    boolean isClosed = false;
    long lastMin = 0L;
    int i = 0;
    long lastSecondTemp = 0L;
    if (isFirstRunning == false) {
      return;
    }
    isFirstRunning = true;
    while (isClosed == false) {
      try {
        lastSecondTemp = getServerTimeService.getServerTime() / 1000L;
        if (lastSecondTemp / 60L > lastMin) {
          batchProcess(lastSecondTemp / 60L * 60L, Integer.parseInt(System.getProperty("apnum")), Integer.parseInt(System.getProperty("containernum")));
          lastMin = lastSecondTemp / 60L;
        }
        Thread.sleep(1000);
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
    do {
      tempResult = batchExeManageDao.insertBatchLockDateTime(inputMap);
      if (tempResult == 0) {
        tempResult = batchExeManageDao.updateBatchLockDateTimeToY(inputMap);
      }
      try {
        Thread.sleep(10);
      } catch (Exception e) {
        // sleep 오류 시 무시
      }
    } while (tempResult == 0);
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
    logger.debug("Output list of SQL getCmnBatchNoExeList - " + outputList);
    for (i = 0; i < outputList.size(); i++) {
      if (allBatchProcess == true || i < outputList.size() / (containerCnt - preExeCnt + 1)) {
        batchExeProcessComponent.batchExeProcess(((Date)outputList.get(i).get("date_time")).getTime(), (String)outputList.get(i).get("batch_exe_nm"), ((Integer)outputList.get(i).get("batch_num")).intValue());
      } else if (outputList.size() / (containerCnt - preExeCnt + 1) == i){
        try {
          random = SecureRandom.getInstance("SHA1PRNG");
        } catch (Exception e) {
          logger.error(e.getMessage());
          return;
        }
        if (random.nextDouble() <= (double)(outputList.size() % (containerCnt - preExeCnt + 1)) / (double)outputList.size()) {
          batchExeProcessComponent.batchExeProcess(((Date)outputList.get(i).get("date_time")).getTime(), (String)outputList.get(i).get("batch_exe_nm"), ((Integer)outputList.get(i).get("batch_num")).intValue());
        }
      }
    }
    outputList.clear();
    outputList = null;
    try {
      Thread.sleep(10);
    } catch (Exception e) {
      // sleep 오류 무시
    }
    inputMap.put("datetime", new Date(currentMin * 1000L));
    inputMap.put("user_id", 0);
    tempResult = batchExeManageDao.updateBatchLockDateTimeToN(inputMap);
  }
}