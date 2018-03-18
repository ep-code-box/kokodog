package com.opr.inf.batch;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;
import java.lang.Process;
import java.text.SimpleDateFormat;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.cmn.cmn.batch.Batch;

public class AnotherServiceStateMonitorBatch extends Batch {
  private static Logger logger = LogManager.getLogger(AnotherServiceStateMonitorBatch.class);
  
  public void run(long batchRunTime, String param) throws Exception {
    addLog("============   Start method of MemoryMonitorBatch.run   ============");
    addLog(" Parameter - batchRunTime[" + batchRunTime + "], param[" + param + "]");
    SimpleDateFormat format = null;
    format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    long currentTime = (System.currentTimeMillis() / 1000L / 10L) * 1000L * 10L;
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
    if (getIsPrevContainerMonitorInfo(batchRunTime, Integer.parseInt(System.getProperty("apnum")), Integer.parseInt(System.getProperty("containernum"))) == true) {
      deleteBatchHist(batchRunTime);
      return;
    }
    while (currentTimeCal.get(Calendar.DAY_OF_YEAR) == startTimeCal.get(Calendar.DAY_OF_YEAR) && currentTimeCal.get(Calendar.YEAR) == startTimeCal.get(Calendar.YEAR)) {
      checkAnotherServerState(batchRunTime);
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
        addLog("    Start memory check for time - " + format.format(new Date(currentTime)));
      }
    }
  }
  
  private void checkAnotherServerState(long currentTime) throws Exception {
    int apNum = Integer.parseInt(System.getProperty("apnum"));
    int containerNum = Integer.parseInt(System.getProperty("containernum"));
    boolean chkAnotherStateProcess = false;
    Map<String, Object> anotherState = null;
    anotherState = getAnotherCheckObjContainer(apNum, containerNum);
    if (apNum == ((BigDecimal)anotherState.get("ap_num")).intValue()) {
      chkAnotherStateProcess = checkAnotherServerStateByProcess(((BigDecimal)anotherState.get("container_num")).intValue(), (String)anotherState.get("container"));
    } else {
      chkAnotherStateProcess = checkAnotherServerStateByProcessWithSsh(((BigDecimal)anotherState.get("ap_num")).intValue()
                                                                       , ((BigDecimal)anotherState.get("container_num")).intValue()
                                                                       , (String)anotherState.get("container_dir"));      
    }
    if (chkAnotherStateProcess == false) {
      addRecoveryHist(currentTime, apNum, containerNum);
      if (apNum == ((BigDecimal)anotherState.get("ap_num")).intValue()) {
        recoverAnotherProcess((String)anotherState.get("exe"));
      } else {
        recoverAnotherProcessWithSsh((String)anotherState.get("exe"), ((BigDecimal)anotherState.get("ap_num")).intValue());
      }
    } else {
      chkAnotherStateProcess = chkAnotherStateByBatchProcess(currentTime, apNum, containerNum);
      if (chkAnotherStateProcess == false) {
        addRecoveryHist(currentTime, apNum, containerNum);
        if (apNum == ((BigDecimal)anotherState.get("ap_num")).intValue()) {
          recoverAnotherProcess((String)anotherState.get("exe"));
        } else {
          recoverAnotherProcessWithSsh((String)anotherState.get("exe"), ((BigDecimal)anotherState.get("ap_num")).intValue());
        }
      } else {
        chkAnotherStateProcess = checkAnotherServerRebootReq(((BigDecimal)anotherState.get("ap_num")).intValue(), ((BigDecimal)anotherState.get("container_num")).intValue());
      }
    }
  }
  
  private Map<String, Object> getAnotherCheckObjContainer(int apNum, int containerNum) throws Exception {
    Map<String, Object> inputMap = new HashMap<String, Object>();
    Map<String, Object> outputMap = null;
    inputMap.put("ap_num", apNum);
    inputMap.put("container_num", containerNum);
    logger.debug("Input map of SQL getOprInfNextCatalinaDir - " + inputMap);
    outputMap = sqlSession.selectOne("com.opr.inf.batch.getOprInfNextCatalinaDir", inputMap);
    logger.debug("Output map of SQL getOprInfNextCatalinaDir - " + outputMap);
    return outputMap;
  }

  private boolean checkAnotherServerStateByProcess(int containerNum, String chkStr) throws Exception {
    Process process = null;
    process = new ProcessBuilder("ps", "-ef").start();
    BufferedReader stdOut = new BufferedReader(new InputStreamReader(process.getInputStream()));
    String line = null;
    boolean isExistProcess = false;;
    while ((line = stdOut.readLine()) != null) {
      if (line.indexOf("java") >= 0 && line.indexOf(chkStr) >= 0 && line.indexOf("-Dcontainernum=" + containerNum) >= 0) {
        isExistProcess = true;
        break;
      }
    }
    return isExistProcess;
  }
  
  private boolean checkAnotherServerStateByProcessWithSsh(int apNum, int containerNum, String chkStr) throws Exception {
    Process process = null;
    process = new ProcessBuilder("ps", "-ef").start();
    BufferedReader stdOut = new BufferedReader(new InputStreamReader(process.getInputStream()));
    String line = null;
    boolean isExistProcess = false;;
    while ((line = stdOut.readLine()) != null) {
      if (line.indexOf("java") >= 0 && line.indexOf(chkStr) >= 0 && line.indexOf("-Dcontainernum=" + containerNum) >= 0) {
        isExistProcess = true;
        break;
      }
    }
    return isExistProcess;
  }

  private boolean chkAnotherStateByBatchProcess(long currentTime, int apNum, int containerNum) throws Exception {
    Map<String, Object> inputMap = new HashMap<String, Object>();
    Map<String, Object> outputMap = null;
    inputMap.put("current_time", new Date(currentTime));
    inputMap.put("ap_num", apNum);
    inputMap.put("container_num", containerNum);
    outputMap = sqlSession.selectOne("com.opr.inf.batch.getOprInfBatchProcessYn", inputMap);
    if (outputMap != null && "Y".equals(outputMap.get("process_yn")) == true) {
      return true;
    } else {
      return false;
    }
  }

  private void addRecoveryHist(long currentTime, int apNum, int containerNum) throws Exception {
    Map<String, Object> inputMap = new HashMap<String, Object>();
    inputMap.clear();
    inputMap.put("ap_num", apNum);
    inputMap.put("container_num", containerNum);
    inputMap.put("datetime", new Date(currentTime));
    inputMap.put("user_num", 0);
    logger.debug("Input map of insertCmnAutoRecoveryHst - " + inputMap);
    sqlSession.insert("com.opr.inf.batch.insertOprInfAutoRecoveryHst", inputMap);
  }
  
  private void recoverAnotherProcess(String dir) throws Exception {
    Process process = null;
    process = new ProcessBuilder(dir, "stop").start();
    process.waitFor();
    process = new ProcessBuilder(dir, "start").start();
    process.waitFor();    
  }  

  private void recoverAnotherProcessWithSsh(String dir, int apNum) throws Exception {
    Process process = null;
    process = new ProcessBuilder(dir, "stop").start();
    process.waitFor();
    process = new ProcessBuilder(dir, "start").start();
    process.waitFor();    
  }
  
  private boolean checkAnotherServerRebootReq(int apNum, int containerNum) throws Exception {
    Map<String, Object> inputMap = new HashMap<String, Object>();
    Map<String, Object> outputMap = null;
    inputMap.put("ap_num", apNum);
    inputMap.put("container_num", containerNum);
    outputMap = sqlSession.selectOne("com.opr.inf.batch.getOprInfIsRebootReq", inputMap);
    if (outputMap != null && "Y".equals(outputMap.get("req_yn"))) {
      return true;
    } else {
      return false;
    }
  }
  
  private boolean getIsPrevContainerMonitorInfo(long batchRunTime, int apNum, int containerNum) throws Exception {
    Map<String, Object> inputMap = new HashMap<String, Object>();
    Map<String, Object> outputMap = null;
    inputMap.put("exe_dtm", new Date(batchRunTime));
    inputMap.put("ap_num", apNum);
    inputMap.put("container_num", containerNum);
    inputMap.put("batch_num", getBatchNum());
    outputMap = sqlSession.selectOne("com.opr.inf.batch.getIsPrevContainerMonitorInfo", inputMap);
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
    sqlSession.getConnection().commit();
  }
}