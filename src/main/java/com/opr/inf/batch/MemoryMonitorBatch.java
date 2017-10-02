package com.opr.inf.batch;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;
import java.text.SimpleDateFormat;

import com.cmn.cmn.batch.Batch;

/**
  *  CREATE TABLE `opr_inf_memory` (<br/>
  *  `datetime` datetime NOT NULL,<br/>
  *  `ap_num` int(2) NOT NULL,<br/>
  *  `container_num` int(2) NOT NULL,<br/>
  *  `audit_id` int(10) NOT NULL,<br/>
  *  `audit_dtm` datetime NOT NULL,<br/>
  *  `free_memory` bigint(12) NOT NULL,<br/>
  *  `total_memory` bigint(12) NOT NULL<br/>
  *  ) ENGINE=MyISAM DEFAULT CHARSET=utf8;<br/>
  */
public class MemoryMonitorBatch extends Batch {
  public void run(long batchRunTime, String param) throws Exception {
    addLog("============   Start method of MemoryMonitorBatch.run   ============");
    addLog(" Parameter - batchRunTime[" + batchRunTime + "], param[" + param + "]");
    Map<String, Object> outputMap = new HashMap<String, Object>();
    SimpleDateFormat format = null;
    format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    outputMap = sqlSession.selectOne("com.cmn.cmn.getServerTime");
    long currentTime = (((Date)outputMap.get("datetime")).getTime() / 1000L / 10L) * 1000L * 10L;
    addLog("Real Start time - " + currentTime);
    long lastInsertBatchTime = 0L;
    Calendar batchRunTimeCal = Calendar.getInstance();
    batchRunTimeCal.setTimeInMillis(batchRunTime);
    Calendar currentTimeCal = Calendar.getInstance();
    currentTimeCal.setTimeInMillis(currentTime);
    Calendar startTimeCal = Calendar.getInstance();
    startTimeCal.setTimeInMillis(currentTime);
    if (batchRunTimeCal.get(Calendar.DAY_OF_YEAR) != currentTimeCal.get(Calendar.DAY_OF_YEAR) || batchRunTimeCal.get(Calendar.YEAR) != currentTimeCal.get(Calendar.YEAR)) {
      setReport("[Stop report] Not proper start because of different between current time and request time");
      return;
    }
    if (getIsPrevMemoryInfo(batchRunTime, Integer.parseInt(System.getProperty("apnum")), Integer.parseInt(System.getProperty("containernum"))) == true) {
      deleteBatchHist(batchRunTime);
      return;
    }
    while (currentTimeCal.get(Calendar.DAY_OF_YEAR) == startTimeCal.get(Calendar.DAY_OF_YEAR) && currentTimeCal.get(Calendar.YEAR) == startTimeCal.get(Calendar.YEAR)) {
      insertMemoryValue(currentTime, Integer.parseInt(System.getProperty("apnum")), Integer.parseInt(System.getProperty("containernum")));
      lastInsertBatchTime = currentTime;
      while (lastInsertBatchTime / 1000L / 10L >= currentTime / 1000L / 10L) {
        outputMap = sqlSession.selectOne("com.cmn.cmn.getServerTime");
        currentTime = (((Date)outputMap.get("datetime")).getTime() / 1000L / 10L) * 1000L * 10L;
        currentTimeCal.setTimeInMillis(currentTime);
        Thread.sleep(1000);
      }
      if (currentTime % (1000L * 60L * 60L) == 0) {
        addLog("    Start memory check for time - " + format.format(new Date(currentTime)));
      }
    }
  }
  
  private void insertMemoryValue(long currentTime, int apNum, int containerNum) throws Exception {
    Map<String, Object> inputMap = new HashMap<String, Object>();
    inputMap.put("datetime", new Date(currentTime));
    inputMap.put("user_num", 0);
    inputMap.put("ap_num", apNum);
    inputMap.put("container_num", containerNum);
    inputMap.put("free_memory", Runtime.getRuntime().freeMemory());
    inputMap.put("total_memory", Runtime.getRuntime().totalMemory());
    sqlSession.insert("com.opr.inf.batch.insertMemoryInfo", inputMap);
    inputMap.clear();
  }
  
  private boolean getIsPrevMemoryInfo(long batchRunTime, int apNum, int containerNum) throws Exception {
    Map<String, Object> inputMap = new HashMap<String, Object>();
    Map<String, Object> outputMap = null;
    inputMap.put("exe_dtm", new Date(batchRunTime));
    inputMap.put("ap_num", apNum);
    inputMap.put("container_num", containerNum);
    inputMap.put("batch_num", getBatchNum());
    outputMap = sqlSession.selectOne("com.opr.inf.batch.getIsPrevMemoryInfoBatchExist", inputMap);
    if (outputMap == null || outputMap.get("is_exist") == null || outputMap.get("is_exist").equals("N") == true) {
      return false;
    } else {
      return true;
    }
  }
  
  private void deleteBatchHist(long batchRunTime) throws Exception {
    Map<String, Object> inputMap = new HashMap<String, Object>();
    inputMap.put("batch_num", getBatchNum());
    inputMap.put("exe_dtm", new Date(batchRunTime));
    sqlSession.delete("com.opr.inf.batch.deleteBatchHist", inputMap);
  }
}