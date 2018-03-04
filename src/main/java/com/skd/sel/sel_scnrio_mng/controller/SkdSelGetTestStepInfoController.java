package com.skd.sel.sel_scnrio_mng.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.stereotype.Controller;

import com.skd.sel.sel_scnrio_mng.service.GetTestStepInfoSvc;
import com.cmn.err.SystemException;

@Controller
public class SkdSelGetTestStepInfoController {
  @Autowired
  private GetTestStepInfoSvc getTestStepInfoSvc;
  
  @Autowired
  private SystemException systemException;
  
  private static Logger logger = LogManager.getLogger(SkdSelGetTestCaseInfoByScnrioNumController.class);
  
  @RequestMapping(value="/skd/sel/sel_scnrio_mng/GetTestStepInfo", method=RequestMethod.POST)
  @ResponseBody
  public List<Map<String, Object>> main(HttpServletRequest request, HttpServletResponse response) throws Exception {
    logger.debug("Start method of SkdSelGetTestCaseInfoByScnrioNumController.main[/skd/sel/sel_scnrio_mng/GetTestStepInfo]");
    validationCheck(request, response);
    Map<String, Object> inputMap = new HashMap<String, Object>();
    inputMap.put("scnrio_num", Integer.parseInt(request.getParameter("scnrio_num")));
    if (request.getParameter("case_num") != null) {
      inputMap.put("case_num", Integer.parseInt(request.getParameter("case_num")));
    }
    return getTestStepInfoSvc.getTestStepInfo(inputMap);
  }

  private void validationCheck(HttpServletRequest request, HttpServletResponse response) throws Exception {
    int tempNum = 0;
    if (request.getParameter("scnrio_num") == null) {
      throw systemException.systemException(3, "scnrio_num");
    }
    try {
      tempNum = Integer.parseInt(request.getParameter("scnrio_num"));
    } catch (NumberFormatException e) {
      throw systemException.systemException(9, "scnrio_num", request.getParameter("scnrio_num"));
    }
  }
}