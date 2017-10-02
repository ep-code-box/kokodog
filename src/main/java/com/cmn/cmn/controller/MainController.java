package com.cmn.cmn.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.stereotype.Controller;

@Controller
public class MainController {
  private static Logger logger = LogManager.getLogger(MainController.class);
  
  @RequestMapping(value="/**", method=RequestMethod.GET)
  public String main(HttpServletRequest request, HttpServletResponse response) throws Exception {
    logger.debug("Start of controller com.cmn.cmn.controller.MainController[/**]");
    return (String)request.getAttribute("_VIEW_URL");
  }
}