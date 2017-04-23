package com.opr.inf.batch;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;
import java.text.SimpleDateFormat;

import org.apache.log4j.Logger;
import org.apache.ibatis.session.SqlSession;

import com.cmn.cmn.batch.Batch;

public class CpuMonitorBatch extends Batch {
  private static Logger logger = Logger.getLogger(CpuMonitorBatch.class);

  public void run(long batchRunTime, String[] param) throws Exception {
    addLog("============   Start method of CpuMonitorBatch.run   ============");
    addLog(" Parameter - batchRunTime[" + batchRunTime + "], param[" + param + "]");
    Map<String, Object> outputMap = new HashMap<String, Object>();
    SimpleDateFormat format = null;
    format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    outputMap = sqlSession.selectOne("com.cmn.cmn.getServerTime");
    long currentTime = (((Date)outputMap.get("datetime")).getTime() / 1000L / 10L) * 1000L * 10L;
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
      checkCPU(currentTime);
      lastInsertBatchTime = currentTime;
      while (lastInsertBatchTime / 1000L / 10L >= currentTime / 1000L / 10L) {
        outputMap = sqlSession.selectOne("com.cmn.cmn.getServerTime");
        currentTime = (((Date)outputMap.get("datetime")).getTime() / 1000L / 10L) * 1000L * 10L;
        currentTimeCal.setTimeInMillis(currentTime);
        Thread.sleep(1000);
      }
      if (currentTime % (1000L * 60L * 60L) == 0) {
        addLog(" Start cpu check for time - " + format.format(new Date(currentTime)));
      }
    }
  }
  
  private void checkCPU(long batchRunTime) throws Exception {
    Map<String, Object> inputMap = new HashMap<String, Object>();
    Map<String, Object> outputMap = null;
    double[] cpuUsePercent = null;
    int i = 0;
    int cpuCnt = 0;
    String line = null;
    BufferedReader stdOut = null;
    Process process = null;
    outputMap = sqlSession.selectOne("com.cmn.cmn.batch.getCPUCnt");
    logger.debug("Output map of SQL getCPUCnt - " + outputMap);
    cpuCnt = ((Long)outputMap.get("cpu_cnt")).intValue();
    cpuUsePercent = new double[cpuCnt];
    process = new ProcessBuilder("/bin/sh", "-c", "/home/leems83/services/sysstat-11.5.5/bin/mpstat -P ALL 10 1 | tail -" + cpuCnt + " | awk '{print 100-$12}'").start();
    stdOut = new BufferedReader(new InputStreamReader(process.getInputStream()));
    for (i = 0; i < cpuCnt; i++) {
      line = stdOut.readLine();
      if (line == null) {
        cpuUsePercent[i] = 0.0;
      } else {
        cpuUsePercent[i] = Double.parseDouble(line);
      }
      inputMap.clear();
      inputMap.put("datetime", new Date(batchRunTime));
      inputMap.put("user_num", 0);
      inputMap.put("ap_num", Integer.parseInt(System.getProperty("apnum")));
      inputMap.put("core_num", i + 1);
      inputMap.put("cpu_share", cpuUsePercent[i]);
      sqlSession.insert("com.cmn.cmn.batch.insertCPUShareInfo", inputMap);
    }
  }
}