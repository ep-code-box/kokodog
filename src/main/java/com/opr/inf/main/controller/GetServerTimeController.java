package com.opr.inf.main.controller;

import java.util.Map;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cmn.err.SystemException;
import com.cmn.err.UserException;
import com.cmn.cmn.service.GetServerTimeService;

@Controller
public class GetServerTimeController {
  @Autowired
  private SystemException systemException;
  
  @Autowired
  private UserException userException;
  
  @Autowired
  private GetServerTimeService getServerTimeService;
  
  private static Logger logger = Logger.getLogger(GetServerTimeController.class);
  
  @RequestMapping(value="/opr/inf/main/GetServerTime", method=RequestMethod.POST)
  @ResponseBody
  public Map main(HttpServletRequest request, HttpServletResponse response) throws Exception {
    validation(request, response);
    Map<String, Object> returnMap = new HashMap<String, Object>();
    returnMap.put("datetime", getServerTimeService.getServerTime());
    return returnMap;
  }
  
  private void validation(HttpServletRequest request, HttpServletResponse response) throws Exception {
    return;
  }
}