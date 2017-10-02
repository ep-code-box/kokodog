package com.opr.inf.main.service.impl;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.sql.Timestamp;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmn.err.SystemException;
import com.cmn.err.UserException;
import com.opr.inf.main.service.GetAppLogListService;
import com.opr.inf.main.dao.GetAppLogListDao;
import com.cmn.cmn.service.GetServerTimeService;

@Service("getAppLogListService")
public class GetAppLogListServiceImpl implements GetAppLogListService {
  @Autowired
  private SystemException systemException;
  
  @Autowired
  private UserException userException;
  
  @Autowired
  private GetAppLogListDao getAppLogListDao;
  
  @Autowired
  private GetServerTimeService getServerTimeService;
  
  private static Logger logger = LogManager.getLogger(GetAppLogListServiceImpl.class);
  
  public Map<String, Object> getAppLogList(long fromDatetime, long toDatetime, int logTypCd, String filterTxt, long startSeq, List<String> logLevelList) throws Exception {
    logger.debug("============   Start method of GetAppLogListService.getAppLogList   ============");
    logger.debug(" Parameter - fromDatetime[" + fromDatetime + "], toDatetime[" + toDatetime + "], logTypCd[" + logTypCd + "], filterText[" + filterTxt + "], startSeq["
                 + startSeq + "], logLevelList[" + logLevelList + "]");
    Map<String, Object> inputMap = new HashMap<String, Object>();
    List<Map<String, Object>> outputList = new ArrayList<Map<String, Object>>();
    List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();
    Map<String, Object> returnMainMap = new HashMap<String, Object>();
    int i = 0;
    inputMap.put("from_datetime", new Date(fromDatetime));
    inputMap.put("to_datetime", new Date(toDatetime));
    if (logTypCd != 0) {
      inputMap.put("log_typ_cd", (long)logTypCd);
    } else {
      inputMap.put("log_typ_cd", null);
    }
    inputMap.put("filter_txt", filterTxt);
    inputMap.put("start_seq", startSeq);
    if (logLevelList.contains("FATAL") == false) {
      inputMap.put("log_level_fatal", false);
    } else {
      inputMap.put("log_level_fatal", true);
    }
    if (logLevelList.contains("ERROR") == false) {
      inputMap.put("log_level_error", false);
    } else {
      inputMap.put("log_level_error", true);
    }
    if (logLevelList.contains("WARN") == false) {
      inputMap.put("log_level_warn", false);
    } else {
      inputMap.put("log_level_warn", true);
    }
    if (logLevelList.contains("INFO") == false) {
      inputMap.put("log_level_info", false);
    } else {
      inputMap.put("log_level_info", true);
    }
    if (logLevelList.contains("DEBUG") == false) {
      inputMap.put("log_level_debug", false);
    } else {
      inputMap.put("log_level_debug", true);
    }
    if (logLevelList.contains("TRACE") == false) {
      inputMap.put("log_level_trace", false);
    } else {
      inputMap.put("log_level_trace", true);
    }
    outputList = getAppLogListDao.getAppLogList(inputMap);
    for (i = 0; i < outputList.size(); i++) {
      Map<String, Object> tempMap = outputList.get(i);
      Map<String, Object> returnMap = new HashMap<String, Object>();
      returnMap.put("datetime", ((Timestamp)tempMap.get("datetime")).getTime());
      returnMap.put("millisec", tempMap.get("millisec"));
      returnMap.put("log_typ", tempMap.get("log_typ"));
      returnMap.put("class_nm", tempMap.get("class_nm"));
      returnMap.put("method_nm", tempMap.get("method_nm"));
      returnMap.put("log_msg", tempMap.get("log_msg"));
      returnMap.put("seq", tempMap.get("seq"));
      returnList.add(returnMap);
    }
    returnMainMap.put("result", returnList);
    returnMainMap.put("datetime", getServerTimeService.getServerTime());
    return returnMainMap;
  }
  
  public long getAppLogListCnt(Map<String, Object> inputMap) throws Exception {
    Map<String, Object> outputMap = null;
    outputMap = getAppLogListDao.getAppLogListCnt(inputMap);
    if (outputMap == null || outputMap.get("cnt") == null) {
      return 0L;
    } else {
      return ((Long)outputMap.get("cnt")).longValue();
    }
  }
}