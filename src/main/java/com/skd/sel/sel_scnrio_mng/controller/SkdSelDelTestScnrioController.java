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

import com.skd.sel.sel_scnrio_mng.service.DelTestScnrioSvc;
import com.cmn.err.SystemException;

@Controller
public class SkdSelDelTestScnrioController {
  @Autowired
  private DelTestScnrioSvc delTestScnrioSvc;
  
  private static Logger logger = LogManager.getLogger(SkdSelDelTestScnrioController.class);
  
  @RequestMapping(value="/skd/sel/sel_scnrio_mng/DelTestScnrio", method=RequestMethod.POST)
  @ResponseBody
  public Map<String, Object> main(HttpServletRequest request, HttpServletResponse response) throws Exception {
    logger.debug("Start method of SkdSelDelTestScnrioController.main[/skd/sel/sel_scnrio_mng/DelTestScnrio]");
    validationCheck(request, response);
    delTestScnrioSvc.delTestScnrio(Integer.parseInt(request.getParameter("scnrio_num")));
    return new HashMap<String, Object>();
  }

  private void validationCheck(HttpServletRequest request, HttpServletResponse response) throws Exception {
    int tempNum = 0;
    if (request.getParameter("scnrio_num") == null) {
      throw new SystemException(3, "scnrio_num");
    }
    try {
      tempNum = Integer.parseInt(request.getParameter("scnrio_num"));
    } catch (NumberFormatException e) {
      throw new SystemException(9, "scnrio_num", request.getParameter("scnrio_num"));
    }
    return;
  }
}