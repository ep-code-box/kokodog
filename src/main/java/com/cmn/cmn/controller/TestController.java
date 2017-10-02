package com.cmn.cmn.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.stereotype.Controller;

import com.cmn.err.SystemException;

@Controller
public class TestController {
  private static Logger logger = Logger.getLogger(TestController.class);
  
  @Autowired
  private SystemException systemException;
  
  @RequestMapping(value="/cmn/cmn/test")
  public String main(HttpServletRequest request, HttpServletResponse response) throws Exception {
    return request.getHeader("Accept");
  }
}