package com.opr.inf.main.controller;

import java.util.Map;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cmn.err.SystemException;
import com.cmn.err.UserException;
import com.opr.inf.main.service.GetBatchExeHstService;

@Controller
public class GetBatchExeHstController {
  @Autowired
  private GetBatchExeHstService getBatchExeHstService;
  
  private static Logger logger = LogManager.getLogger(GetBatchExeHstController.class);
  
  @RequestMapping(value="/opr/inf/main/GetBatchExeHst", method=RequestMethod.POST)
  @ResponseBody
  public List<Map<String, Object>> main(HttpServletRequest request, HttpServletResponse response) throws Exception {
    logger.debug("Start method of GetBatchExeHstController.main[/opr/inf/main/GetBatchExeHst]");
    validation(request, response);
    List<Map<String, Object>> outputList = null;
    outputList = getBatchExeHstService.getBatchExeHst(Long.parseLong(request.getParameter("from_datetime")), Long.parseLong(request.getParameter("to_datetime")));
    return outputList;
  }
  
  private void validation(HttpServletRequest request, HttpServletResponse response) throws Exception {
    long fromDatetime = 0L;
    long toDatetime = 0L;
    if (request.getParameter("from_datetime") == null) {
      throw new SystemException(3, "from_datetime");
    }
    try {
      fromDatetime = Long.parseLong(request.getParameter("from_datetime"));
    } catch (NumberFormatException e) {
      throw new SystemException(9, "from_datetime", request.getParameter("from_datetime"));
    }
    if (fromDatetime > ((Long)request.getAttribute("system_call_dtm")).longValue()) {
      throw new SystemException(9, "from_datetime", request.getParameter("from_datetime"));
    }
    if (request.getParameter("to_datetime") == null) {
      throw new SystemException(3, "to_datetime");
    }
    try {
      toDatetime = Long.parseLong(request.getParameter("to_datetime"));
    } catch (NumberFormatException e) {
      throw new SystemException(9, "to_datetime", request.getParameter("to_datetime"));
    }
    if (toDatetime > ((Long)request.getAttribute("system_call_dtm")).longValue()) {
      throw new SystemException(9, "to_datetime", request.getParameter("to_datetime"));
    }
    if (toDatetime < fromDatetime) {
      throw new SystemException(14, "종료시각이 시작시각보다 작습니다.");
    }
  }
}