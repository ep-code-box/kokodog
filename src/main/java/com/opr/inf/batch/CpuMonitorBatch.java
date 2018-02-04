package com.opr.inf.batch;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;
import java.text.SimpleDateFormat;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import org.apache.ibatis.session.SqlSession;

import com.cmn.cmn.batch.Batch;

/**
  *  CREATE TABLE `opr_inf_cpu_share_info` (<br/>
  *    `datetime` datetime NOT NULL,<br/>
  *    `ap_num` int(2) NOT NULL,<br/>
  *    `core_num` int(2) NOT NULL,<br/>
  *    `audit_id` int(10) NOT NULL,<br/>
  *    `audit_dtm` datetime NOT NULL,<br/>
  *    `cpu_share` double NOT NULL<br/>
  *  ) ENGINE=MyISAM DEFAULT CHARSET=utf8;<br/>
  */
public class CpuMonitorBatch extends Batch {
  public void run(long batchRunTime, String param) throws Exception {
    addLog("============   Start method of CpuMonitorBatch.run   ============");
    addLog(" Parameter - batchRunTime[" + batchRunTime + "], param[" + param + "]");
    //Map<String, Object> outputMap = new HashMap<String, Object>();
    SimpleDateFormat format = null;
    format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    long currentTime = (System.currentTimeMillis() / 1000L / 10L) * 1000L * 10L;
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
        currentTime = (System.currentTimeMillis() / 1000L / 10L) * 1000L * 10L;
        currentTimeCal.setTimeInMillis(currentTime);
        Thread.sleep(1000);
        if (checkProcessEnd == true) {
          break;
        }
      }
      if (checkProcessEnd == true) {
        break;
      }
      if (currentTime % (1000L * 60L * 60L) == 0) {
        addLog(" Start cpu check for time - " + format.format(new Date(currentTime)));
      }
    }
  }
  
  private void checkCPU(long batchRunTime) throws Exception {
    CPUCheckProcessThread cpuCheckProcessThread = null;
    cpuCheckProcessThread = new CPUCheckProcessThread(sqlSession, batchRunTime);
    cpuCheckProcessThread.start();
  }
}

class CPUCheckProcessThread extends Thread {
  private static Logger logger = LogManager.getLogger(CPUCheckProcessThread.class);

  private long batchRunTime = 0L;
  private SqlSession sqlSession;
  
  public CPUCheckProcessThread(SqlSession sqlSession, long batchRunTime) {
    this.sqlSession = sqlSession;
    this.batchRunTime = batchRunTime;
  }
  
  public void run() {
    Map<String, Object> inputMap = new HashMap<String, Object>();
    Map<String, Object> outputMap = null;
    double[] cpuUsePercent = null;
    int i = 0;
    int cpuCnt = 0;
    String line = null;
    BufferedReader stdOut = null;
    Process process = null;
    SimpleDateFormat format = null;
    format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    try {
      outputMap = sqlSession.selectOne("com.opr.inf.batch.getCPUCnt");
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
        sqlSession.insert("com.opr.inf.batch.insertCPUShareInfo", inputMap);
        sqlSession.getConnection().commit();
      }
    } catch (Exception e) {
      logger.error("=================     CPU Check Exception Start    ==================");
      logger.error("Call Time[" + format.format(new Date(batchRunTime)) + "]");
      logger.error("Current Time[" + format.format(new Date()) + "]");
      logger.error("Error Trace!!");
      logger.error("" + e.getClass().getName() == null ? "" : e.getClass().getName() + ": " + e.getMessage() == null ? "" : e.getMessage());
      StackTraceElement[] ste = e.getStackTrace();
      for (i = 0; i < ste.length; i++) {
        logger.error("       at " + ste[i].toString());
      }
      logger.error("=================      CPU Check Exception End     ==================");
    }
  }
}