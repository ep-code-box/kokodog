package com.opr.inf.batch;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;
import java.text.SimpleDateFormat;

import org.apache.log4j.Logger;

import com.cmn.cmn.batch.Batch;

public class MemoryMonitorBatch extends Batch {
  private static Logger logger = Logger.getLogger(MemoryMonitorBatch.class);

  public void run(long batchRunTime, String[] param) throws Exception {
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
    sqlSession.insert("com.cmn.cmn.batch.insertMemoryInfo", inputMap);
    inputMap.clear();
  }
}