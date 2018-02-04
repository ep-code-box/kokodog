package com.opr.inf.batch;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;
import java.lang.Process;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.cmn.cmn.batch.Batch;

public class AnotherServiceStateMonitorBatch extends Batch {
  private static Logger logger = LogManager.getLogger(AnotherServiceStateMonitorBatch.class);
  
  public void run(long batchRunTime, String param) throws Exception {
    checkAnotherServerState(batchRunTime);
  }
  
  private void checkAnotherServerState(long currentTime) throws Exception {
    int apNum = Integer.parseInt(System.getProperty("apnum"));
    int containerNum = Integer.parseInt(System.getProperty("containernum"));
    boolean chkAnotherStateProcess = false;
    Map<String, Object> anotherState = null;
    anotherState = getAnotherCheckObjContainer(apNum, containerNum);
    if (apNum == ((BigDecimal)anotherState.get("ap_num")).intValue()) {
      chkAnotherStateProcess = checkAnotherServerStateByProcess(((BigDecimal)anotherState.get("container_num")).intValue());
    } else {
      chkAnotherStateProcess = checkAnotherServerStateByProcessWithSsh(((BigDecimal)anotherState.get("ap_num")).intValue(), ((BigDecimal)anotherState.get("container_num")).intValue());      
    }
    if (chkAnotherStateProcess == false) {
      addRecoveryHist(currentTime, apNum, containerNum);
      if (apNum == ((BigDecimal)anotherState.get("ap_num")).intValue()) {
        recoverAnotherProcess((String)anotherState.get("dir") + ((BigDecimal)anotherState.get("container_num")).intValue());
      } else {
        recoverAnotherProcessWithSsh((String)anotherState.get("dir") + ((BigDecimal)anotherState.get("container_num")).intValue()
                              , ((BigDecimal)anotherState.get("ap_num")).intValue());
      }
    } else {
      chkAnotherStateProcess = chkAnotherStateByBatchProcess(currentTime, apNum, containerNum);
      if (chkAnotherStateProcess == false) {
        addRecoveryHist(currentTime, apNum, containerNum);
        if (apNum == ((BigDecimal)anotherState.get("ap_num")).intValue()) {
          recoverAnotherProcess((String)anotherState.get("dir") + ((BigDecimal)anotherState.get("container_num")).intValue());
        } else {
          recoverAnotherProcessWithSsh((String)anotherState.get("dir") + ((BigDecimal)anotherState.get("container_num")).intValue()
                                , ((BigDecimal)anotherState.get("ap_num")).intValue());
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
    logger.debug("Input map of SQL getCmnNextCatalinaDir - " + inputMap);
    outputMap = sqlSession.selectOne("com.opr.inf.batch.getOprInfNextCatalinaDir", inputMap);
    logger.debug("Output map of SQL getOprInfNextCatalinaDir - " + outputMap);
    return outputMap;
  }

  private boolean checkAnotherServerStateByProcess(int containerNum) throws Exception {
    Process process = null;
    process = new ProcessBuilder("ps", "-ef").start();
    BufferedReader stdOut = new BufferedReader(new InputStreamReader(process.getInputStream()));
    String line = null;
    boolean isExistProcess = false;;
    while ((line = stdOut.readLine()) != null) {
      if (line.indexOf("java") >= 0 && line.indexOf("-Dcontainernum=" + containerNum) >= 0) {
        isExistProcess = true;
        break;
      }
    }
    return isExistProcess;
  }
  
  private boolean checkAnotherServerStateByProcessWithSsh(int apNum, int containerNum) throws Exception {
    Process process = null;
    process = new ProcessBuilder("ps", "-ef").start();
    BufferedReader stdOut = new BufferedReader(new InputStreamReader(process.getInputStream()));
    String line = null;
    boolean isExistProcess = false;;
    while ((line = stdOut.readLine()) != null) {
      if (line.indexOf("java") >= 0 && line.indexOf("-Dcontainernum=" + containerNum) >= 0) {
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
}