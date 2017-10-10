package com.cmn.cmn.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.cmn.cmn.service.ConnLogService;
import com.cmn.cmn.dao.ConnLogDao;
import com.cmn.cmn.service.GetServerTimeService;

@Service("connLogService")
public class ConnLogServiceImpl implements ConnLogService {
  @Autowired
  private ConnLogDao connLogDao;
  
  @Autowired
  private GetServerTimeService getServerTimeService;
  
  private static Logger logger = LogManager.getLogger(ConnLogServiceImpl.class);

  public long insertConnLog(long systemCallDtm, int userNum, String remoteAddr, String requesrURL, String queryString, String method) throws Exception {
    logger.debug("============   Start method of InterceptorComponent.preHandle   ============");
    Map<String, Object> inputMap = new HashMap<String, Object>();
    Map<String, Object> outputMap = null;
    long seq = 0L;
    inputMap.put("now_dtm", new Date(systemCallDtm / 1000L * 1000L));
    inputMap.put("millisec", (int)(systemCallDtm % 1000L));
    inputMap.put("user_num", userNum);
    inputMap.put("ip", remoteAddr);
    inputMap.put("url", requesrURL);
    inputMap.put("query_string", queryString);
    inputMap.put("method", method);
    inputMap.put("ap_num", Integer.parseInt(System.getProperty("apnum")));
    inputMap.put("container_num", Integer.parseInt(System.getProperty("containernum")));
    connLogDao.insertConnLog(inputMap);
    inputMap.clear();
    inputMap.put("now_dtm", new Date(systemCallDtm / 1000L * 1000L));
    inputMap.put("millisec", (int)(systemCallDtm % 1000L));
    outputMap = connLogDao.getLastSeqConnLog(inputMap);
    seq = ((Long)outputMap.get("seq")).intValue();
    return seq;
  }
  
  public void updateConnEndLog(long systemCallDtm, long systemEndDtm, int responseNum, String errMsg, long seq, int userNum) throws Exception {
    Map<String, Object> inputMap = new HashMap<String, Object>();
    inputMap.put("end_dtm", new Date(systemEndDtm / 1000L * 1000L));
    inputMap.put("end_millisec", (int)(systemEndDtm % 1000L));
    inputMap.put("response_num", responseNum == 0 ? 200 : responseNum);
    inputMap.put("error_msg", errMsg);
    inputMap.put("now_dtm", new Date(systemCallDtm / 1000L * 1000L));
    inputMap.put("millisec", (int)(systemCallDtm % 1000L));
    inputMap.put("seq", seq);
    inputMap.put("user_num", userNum);
    connLogDao.updateConnEndLog(inputMap);
  }
  
  public void updateTimeoutConnEndLog(long systemCallDtm, long systemEndDtm, long seq, int userNum) throws Exception {
    updateConnEndLog(systemCallDtm, systemEndDtm, 408, "Request time out.", seq, userNum);
  }
  
  public void checkConnectValid() throws Exception {
    connLogDao.checkConnectValid();
  }
}