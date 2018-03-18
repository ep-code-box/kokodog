package com.opr.inf.main.controller;

import java.util.Map;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

@Controller
public class SetChangeToMobileController {
  private static Logger logger = LogManager.getLogger(SetChangeToMobileController.class);
  
  @RequestMapping(value="/opr/inf/main/SetChangeToMobile", method=RequestMethod.POST)
  @ResponseBody
  public Map<String, Object> main(HttpServletRequest request, HttpServletResponse response) throws Exception {
    logger.debug("Start method of SetChangeToMobileController.main[/opr/inf/main/SetChangeToMobile");
    request.getSession().setAttribute("is_mobile", "Y");
    return new HashMap<String, Object>();
  }
}