package com.opr.inf.main.controller;

import java.util.Map;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.cmn.err.SystemException;
import com.cmn.err.UserException;

@Controller
public class SetChangeToPCController {
  @Autowired
  private SystemException systemException;
  
  @Autowired
  private UserException userException;
  
  private static Logger logger = LogManager.getLogger(SetChangeToPCController.class);
  
  @RequestMapping(value="/opr/inf/main/SetChangeToPC", method=RequestMethod.POST)
  @ResponseBody
  public Map<String, Object> main(HttpServletRequest request, HttpServletResponse response) throws Exception {
    logger.debug("Start method of SetChangeToPCController.main[/opr/inf/main/SetChangeToPC]");
    request.getSession().setAttribute("is_mobile", "N");
    return new HashMap<String, Object>();
  }
}