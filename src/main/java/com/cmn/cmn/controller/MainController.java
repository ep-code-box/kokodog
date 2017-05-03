package com.cmn.cmn.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.stereotype.Controller;

@Controller
public class MainController {
  private static Logger logger = Logger.getLogger(MainController.class);
  
  @RequestMapping(value="/**", method=RequestMethod.GET)
  public String main(HttpServletRequest request, HttpServletResponse response) throws Exception {
    return (String)request.getAttribute("_VIEW_URL");
  }
}