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
    String dir = null;
    int apNum = Integer.parseInt(System.getProperty("apnum"));
    int containerNum = Integer.parseInt(System.getProperty("containernum"));
    Map<String, Object> inputMap = new HashMap<String, Object>();
    Map<String, Object> outputMap = null;
    inputMap.put("ap_num", apNum);
    inputMap.put("container_num", containerNum);
    logger.debug("Input map of SQL getCmnNextCatalinaDir - " + inputMap);
    outputMap = sqlSession.selectOne("getCmnNextCatalinaDir", inputMap);
    logger.debug("Output map of SQL getCmnNextCatalinaDir - " + outputMap);
    dir = (String)outputMap.get("dir");
    Process process = null;
    process = new ProcessBuilder("ps", "-ef").start();
    BufferedReader stdOut = new BufferedReader(new InputStreamReader(process.getInputStream()));
    String line = null;
    boolean isExistProcess = false;;
    while ((line = stdOut.readLine()) != null) {
      if (line.indexOf("java") >= 0 && line.indexOf("-Dcontainernum=" + ((BigDecimal)outputMap.get("container_num")).intValue()) >= 0) {
        isExistProcess = true;
        break;
      }
    }
    if (isExistProcess == false) {
      process = new ProcessBuilder(dir, "stop").start();
      process.waitFor();
      process = new ProcessBuilder(dir, "start").start();
      process.waitFor();
      inputMap.clear();
      inputMap.put("ap_num", ((BigDecimal)outputMap.get("ap_num")).intValue());
      inputMap.put("container_num", ((BigDecimal)outputMap.get("container_num")).intValue());
      inputMap.put("datetime", new Date(currentTime));
      inputMap.put("user_num", 0);
      logger.debug("Input map of insertCmnAutoRecoveryHst - " + inputMap);
      sqlSession.insert("insertCmnAutoRecoveryHst", inputMap);
    }
  }
}