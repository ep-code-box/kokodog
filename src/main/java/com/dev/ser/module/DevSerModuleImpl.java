package com.dev.ser.module;

import java.lang.Process;
import java.lang.ProcessBuilder;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileWriter;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import com.cmn.err.SystemException;
import com.cmn.err.UserException;
import com.dev.ser.module.DevSerModule;
import com.dev.cmn.module.DevCmnModule;

@Service("devSerModule")
public class DevSerModuleImpl implements DevSerModule {
  @Autowired
  private SqlSession sqlSession;
  
  @Autowired
  private UserException userException;
  
  @Autowired
  private DevCmnModule devCmnModule;
  
  private static Logger logger = Logger.getLogger(DevSerModuleImpl.class);
  
  public void serDist(int serviceNum, int repVer, int instance, HttpServletRequest request) throws Exception {
    Map<String, Object> inputMap = new HashMap<String, Object>();
    inputMap.put("cd_num", 16);
    logger.debug("Input map of SQL getCmnCdList - " + inputMap);
    List<Map<String, Object>> outputList = sqlSession.selectList("getCmnCdList", inputMap);
    logger.debug("Output list of SQL getCmnCdList - " + outputList);
    String devDir = new String((String)(outputList.get(0).get("cd_seq_name")));
    String url = new String((String)(outputList.get(0).get("cd_seq_name"))) + "/WEB-INF/classes";
    inputMap.clear();
    inputMap.put("service_num", serviceNum);
    inputMap.put("dist_instance", instance);
    logger.debug("Input map of SQL getDevSerLastDistServiceName - " + inputMap);
    Map<String, Object> outputMap = sqlSession.selectOne("getDevSerLastDistServiceName", inputMap);
    logger.debug("Output map of SQL getDevSerLastDistServiceName - " + outputMap);
    String pastServiceFile = null;
    if (outputMap != null && outputMap.get("service_name") != null) {
      pastServiceFile = outputMap.get("pgm_abb") + "/" + outputMap.get("task_abb") + "/" + outputMap.get("page_abb")
        + "/" + outputMap.get("service_name") + ".class";
    }
    inputMap.clear();
    inputMap.put("service_num", serviceNum);
    if (repVer == 0) {
      inputMap.put("rep_ver", null);
    } else {
      inputMap.put("rep_ver", repVer);
    }
    logger.debug("Input map of SQL getDevSerDBDistInfo - " + inputMap);
    outputMap = sqlSession.selectOne("getDevSerDBDistInfo", inputMap);
    logger.debug("Output map of SQL getDevSerDBDistInfo - " + outputMap);
    String newServiceFile = outputMap.get("service_name") + ".java";
    BufferedWriter bw = new BufferedWriter(new FileWriter(devDir + "/" + newServiceFile));
    bw.write((String)outputMap.get("source"));
    bw.close();
    String[] strCommand = new String[6];
    strCommand[0] = "javac";
    strCommand[1] = "-cp";
    strCommand[2] = devDir + "/WEB-INF/classes/:";
    String libPath = devDir + "/WEB-INF/lib/";
    File libDir = new File(libPath);
    File[] libFileList = libDir.listFiles();
    for (int i = 0; i < libFileList.length; i++) {
      if(libFileList[i].isFile()) {
        strCommand[2] = strCommand[2] + libPath + libFileList[i].getName() + ":";
      }
    }
    strCommand[3] = "-d";
    strCommand[4] = url;
    strCommand[5] = devDir + "/" + newServiceFile;
    if (pastServiceFile != null) {
      File pastOriginalFile = new File(url + "/" + pastServiceFile);
      File pastBackupFile = new File(url + "/" + pastServiceFile + ".bak");
      pastOriginalFile.renameTo(pastBackupFile);
    }
    try {
      Process process = new ProcessBuilder(strCommand).start();
      BufferedReader stdOut = new BufferedReader(new InputStreamReader(process.getInputStream()));
      BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));
      String errStr = "";
      String processStr = "";
      String tempStr = "";
      while ((tempStr = stdOut.readLine()) != null) {
        processStr = processStr + tempStr + "\n";
      }
      while ((tempStr = stdError.readLine()) != null) {
        errStr = errStr + tempStr + "\n";
      }
      if (errStr.equals("") == false) {
        throw userException.userException(12, errStr);
      }
      File file = new File(url + "/" + pastServiceFile + ".bak");
      if (file.exists() == true) {
        file.delete();
      }
      file = new File(devDir + "/" + newServiceFile);
      if (file.exists() == true) {
        file.delete();
      }
      devCmnModule.distResultInsert(serviceNum, ((Long)outputMap.get("rep_ver")).intValue(), instance, 2, request);
    } catch (Exception e) {
      File file = new File(url + "/" + pastServiceFile + ".bak");
      if (file.exists() == true) {
        File fileToMove = new File(url + "/" + pastServiceFile);
        file.renameTo(fileToMove);
      }
      throw e;
    }
  }
}