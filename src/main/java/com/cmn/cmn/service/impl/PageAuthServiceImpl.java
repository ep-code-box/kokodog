package com.cmn.cmn.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.cmn.cmn.service.PageAuthService;
import com.cmn.cmn.dao.PageAuthDao;

@Service("pageAuthService")
public class PageAuthServiceImpl implements PageAuthService {
  @Autowired
  private PageAuthDao pageAuthDao;
  
  private static Logger logger = LogManager.getLogger(PageAuthServiceImpl.class);

  public boolean getIsLoginAuth(String pgmAbb, String taskAbb, String pageAbb, int userNum) throws Exception {
    logger.debug("============   Start method of PageAuthServiceImpl.getErrMessageByMessageNum   ============");
    Map<String, Object> inputMap = new HashMap<String, Object>();
    Map<String, Object> outputMap = null;
    inputMap.put("pgm_abb", pgmAbb);
    inputMap.put("task_abb", taskAbb);
    inputMap.put("page_abb", pageAbb);
    inputMap.put("user_num", userNum == 0 ? null : userNum);
    outputMap = pageAuthDao.getIsLoginAuth(inputMap);
    if (outputMap == null || outputMap.get("is_auth") == null || outputMap.get("is_auth").equals("Y") == false) {
      return false;
    } else {
      return true;
    }
  }
  
  public boolean getIsMobilePageExist(String pgmAbb, String taskAbb, String pageAbb) throws Exception {
    Map<String, Object> inputMap = new HashMap<String, Object>();
    Map<String, Object> outputMap = null;
    inputMap.put("pgm_abb", pgmAbb);
    inputMap.put("task_abb", taskAbb);
    inputMap.put("page_abb", pageAbb);
    outputMap = pageAuthDao.getIsMobilePageExist(inputMap);
    if (outputMap == null || outputMap.get("is_exist") == null || outputMap.get("is_exist").equals("Y") == false) {
      return false;
    } else {
      return true;
    }
  }
  
  public boolean getIsPageExist(String pgmAbb, String taskAbb, String pageAbb) throws Exception {
    Map<String, Object> inputMap = new HashMap<String, Object>();
    Map<String, Object> outputMap = null;
    inputMap.put("pgm_abb", pgmAbb);
    inputMap.put("task_abb", taskAbb);
    inputMap.put("page_abb", pageAbb);
    outputMap = pageAuthDao.getIsPageExist(inputMap);
    if (outputMap == null || outputMap.get("is_page_exists") == null || outputMap.get("is_page_exists").equals("Y") == false) {
      return false;
    } else {
      return true;
    }    
  }
}