package com.opr.inf.main;

import java.util.Map;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import org.apache.log4j.Logger;

import com.cmn.err.SystemException;
import com.cmn.err.UserException;

@Controller
public class SetChangeToMobileController {
  @Autowired
  private SystemException systemException;
  
  @Autowired
  private UserException userException;
  
  private static Logger logger = Logger.getLogger(SetChangeToMobileController.class);
  
  @RequestMapping(value="/opr/inf/main/SetChangeToMobile", method=RequestMethod.POST)
  @ResponseBody
  public Map main(HttpServletRequest request, HttpServletResponse response) throws Exception {
    request.getSession().setAttribute("is_mobile", "Y");
    return new HashMap<String, Object>();
  }
}