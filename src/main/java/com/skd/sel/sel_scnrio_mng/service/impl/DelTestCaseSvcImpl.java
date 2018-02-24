package com.skd.sel.sel_scnrio_mng.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import com.skd.sel.sel_scnrio_mng.service.DelTestCaseSvc;
import com.skd.sel.sel_scnrio_mng.dao.DelTestCaseDao;
import com.cmn.err.UserException;

@Service("delTestCaseSvc")
public class DelTestCaseSvcImpl implements DelTestCaseSvc {
  @Autowired
  private DelTestCaseDao delTestCaseDao;
  
  @Autowired
  private UserException userException;
  
  public void delTestCase(int scnrioNum, int caseNum) throws Exception {
    Map<String, Object> inputMap = new HashMap<String, Object>();
    Map<String, Object> outputMap = null;
    ServletRequestAttributes sra = null;
    HttpServletRequest request = null;
    long systemCallDtm = 0L;
    int userNum = 0;
    try {
      sra = (ServletRequestAttributes)RequestContextHolder.currentRequestAttributes();
      request = sra.getRequest();
      if (request == null || request.getSession() == null || request.getSession().getAttribute("user_num") == null) {
        systemCallDtm = new Date().getTime();
        userNum = 0;
      } else {
        systemCallDtm = ((Long)request.getAttribute("system_call_dtm")).longValue();
        userNum = ((Integer)request.getSession().getAttribute("user_num")).intValue();
      }
    } catch (Exception e) {
      systemCallDtm = new Date().getTime();
      userNum = 0;
    }
    inputMap.put("scnrio_num", scnrioNum);
    inputMap.put("case_num", caseNum);
    outputMap = delTestCaseDao.getCanBeDeletedCase(inputMap);
    if (outputMap != null && outputMap.get("can_be_deleted") != null && "Y".equals(outputMap.get("can_be_deleted")) == false) {
      throw userException.userException(23, "1", "케이스");
    }
    inputMap.put("system_call_dtm", new Date(systemCallDtm));
    inputMap.put("user_num", userNum);
    delTestCaseDao.delTestCaseWithScnrioNumAndCaseNum(inputMap);
    delTestCaseDao.delAllTestCaseInputWithScnrioNumAndCaseNum(inputMap);
  }
}