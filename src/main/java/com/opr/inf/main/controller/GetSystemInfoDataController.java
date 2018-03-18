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
import com.opr.inf.main.service.GetSystemInfoDataService;

@Controller
public class GetSystemInfoDataController {
  @Autowired
  private GetSystemInfoDataService getSystemInfoDataService;
  
  private static Logger logger = LogManager.getLogger(GetSystemInfoDataController.class);
  
  private static final int MAX_DIFF_TIME = 2 * 24 * 60 * 60;
  
  @RequestMapping(value="/opr/inf/main/GetSystemInfoData", method=RequestMethod.POST)
  @ResponseBody
  public List<Map<String, Object>> main(HttpServletRequest request, HttpServletResponse response) throws Exception {
    logger.debug("Start method of GetSystemInfoDataController.main[/opr/inf/main/GetSystemInfoData]");
    validation(request, response);
    List<Map<String, Object>> outputList = null;
    outputList = getSystemInfoDataService.getSystemInfoData(Long.parseLong(request.getParameter("from_datetime"))
                                                                         , Long.parseLong(request.getParameter("to_datetime"))
                                                                         , request.getParameter("cnt") == null ? MAX_DIFF_TIME : Integer.parseInt(request.getParameter("cnt")));
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
    if (toDatetime - fromDatetime > (long)(MAX_DIFF_TIME * 1000)) {
      throw new SystemException(14, "최대 조회 허용 시각인 " + MAX_DIFF_TIME + "초 차이보다 큰 결과를 조회하려고 시도하였습니다.");
    }
  }
}