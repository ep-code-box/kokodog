package com.opr.inf.main.service.impl;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.sql.Timestamp;

import org.apache.log4j.Logger;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.cmn.err.SystemException;
import com.cmn.err.UserException;
import com.opr.inf.main.service.GetBatchExeHstService;
import com.opr.inf.main.dao.GetBatchExeHstDao;

@Service("getBatchExeHstService")
public class GetBatchExeHstServiceImpl implements GetBatchExeHstService {
  @Autowired
  private SystemException systemException;
  
  @Autowired
  private UserException userException;
  
  @Autowired
  private GetBatchExeHstDao getBatchExeHstDao;
  
  private static Logger logger = Logger.getLogger(GetBatchExeHstServiceImpl.class);
  
  public List<Map<String, Object>> getBatchExeHst(long fromDatetime, long toDatetime) throws Exception {
    logger.debug("============   Start method of GetBatchExeHstServiceImpl.getBatchExeHst   ============");
    logger.debug(" Parameter - fromDatetime[" + fromDatetime + "], toDatetime[" + toDatetime + "]");
    Map<String, Object> inputMap = new HashMap<String, Object>();
    List<Map<String, Object>> outputList = new ArrayList<Map<String, Object>>();
    List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();
    int i = 0;
    inputMap.put("from_datetime", new Date(fromDatetime));
    inputMap.put("to_datetime", new Date(toDatetime));
    outputList = getBatchExeHstDao.getBatchExecList(inputMap);
    for (i = 0; i < outputList.size(); i++) {
      Map<String, Object> tempMap = outputList.get(i);
      Map<String, Object> returnMap = new HashMap<String, Object>();
      returnMap.put("batch_num", tempMap.get("batch_num"));
      returnMap.put("batch_nm", tempMap.get("batch_nm"));
      returnMap.put("exe_dtm", ((Timestamp)tempMap.get("exe_dtm")).getTime());
      returnMap.put("real_exe_dtm", ((Timestamp)tempMap.get("real_exe_dtm")).getTime());
      if (tempMap.get("real_end_dtm") == null) {
        returnMap.put("real_end_dtm", "");
      } else {
        returnMap.put("real_end_dtm", ((Timestamp)tempMap.get("real_end_dtm")).getTime());
      }
      returnMap.put("batch_exe_state", tempMap.get("batch_exe_state"));
      returnMap.put("batch_exe_state_nm", tempMap.get("batch_exe_state_nm"));
      returnList.add(returnMap);
    }
    logger.debug(" return - returnList[" + returnList + "]");
    return returnList;
  }
}