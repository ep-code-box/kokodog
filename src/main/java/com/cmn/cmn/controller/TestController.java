package com.cmn.cmn.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.stereotype.Controller;

import com.cmn.err.UserException;
import com.cmn.err.SystemException;

@Controller
public class TestController {
  private static Logger logger = LogManager.getLogger(MainController.class);
  
  @Autowired
  private UserException userException;
  
  @Autowired
  private SystemException systemException;

  @RequestMapping(value="/cmn/cmn/test", method = {RequestMethod.GET, RequestMethod.POST})
  public String test(HttpServletRequest request, HttpServletResponse response) throws Exception {
    logger.debug("Start method of com.cmn.cmn.controller.test");
    if ("user".equals(request.getParameter("param")) == true) {
      throw userException.userException(2);
    } else if ("system".equals(request.getParameter("param")) == true) {
      throw systemException.systemException(2);
    } else if ("jsp".equals(request.getParameter("param")) == true) {
      return "cmn/cmn/test";
    } else {
      throw new Exception("test");
    }
  }
}
