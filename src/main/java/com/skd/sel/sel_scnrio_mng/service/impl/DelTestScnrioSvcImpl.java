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

import com.skd.sel.sel_scnrio_mng.service.DelTestScnrioSvc;
import com.skd.sel.sel_scnrio_mng.dao.DelTestScnrioDao;

@Service("delTestScnrioSvc")
public class DelTestScnrioSvcImpl implements DelTestScnrioSvc {
  @Autowired
  private DelTestScnrioDao delTestScnrioDao;
  public void delTestScnrio(int scnrioNum) throws Exception {
    Map<String, Object> inputMap = new HashMap<String, Object>();
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
    inputMap.put("system_call_dtm", new Date(systemCallDtm));
    inputMap.put("user_num", userNum);
    delTestScnrioDao.delTestScnrio(inputMap);
    delTestScnrioDao.delAllTestCaseWithScnrioNum(inputMap);
    delTestScnrioDao.delAllTestInputWithScnrioNum(inputMap);
  }
}