package com.opr.inf.main.controller;

import java.util.Date;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cmn.err.SystemException;
import com.cmn.err.UserException;
import com.opr.inf.main.service.GetAppLogListService;

@Controller
public class GetAppLogListController {
  @Autowired
  private GetAppLogListService getAppLogListService;
  
  private static Logger logger = LogManager.getLogger(GetAppLogListController.class);
  
  private static final long MAX_LOG_CNT = 1000L;

  @RequestMapping(value="/opr/inf/main/GetAppLogList", method=RequestMethod.POST)
  @ResponseBody
  public Map<String, Object> main(HttpServletRequest request, HttpServletResponse response) throws Exception {
    logger.debug("Start method of GetAppLogListController.main[/opr/inf/main/GetAppLogList]");
    validation(request, response);
    List<String> logLevelList = new ArrayList<String>();
    Map<String, Object> returnMap = null;
    if (request.getParameter("log_level_fatal") != null && request.getParameter("log_level_fatal").equals("true")) {
      logLevelList.add("FATAL");
    }
    if (request.getParameter("log_level_error") != null && request.getParameter("log_level_error").equals("true")) {
      logLevelList.add("ERROR");
    }
    if (request.getParameter("log_level_warn") != null && request.getParameter("log_level_warn").equals("true")) {
      logLevelList.add("WARN");
    }
    if (request.getParameter("log_level_info") != null && request.getParameter("log_level_info").equals("true")) {
      logLevelList.add("INFO");
    }
    if (request.getParameter("log_level_debug") != null && request.getParameter("log_level_debug").equals("true")) {
      logLevelList.add("DEBUG");
    }
    if (request.getParameter("log_level_trace") != null && request.getParameter("log_level_trace").equals("true")) {
      logLevelList.add("TRACE");
    }
    returnMap = getAppLogListService.getAppLogList(Long.parseLong(request.getParameter("from_datetime")), Long.parseLong(request.getParameter("to_datetime"))
                                                  , request.getParameter("log_typ_cd") == null ? 0 : Integer.parseInt(request.getParameter("log_typ_cd"))
                                                  , request.getParameter("filter_txt")
                                                  , request.getParameter("start_seq") == null ? 0L : Long.parseLong(request.getParameter("start_seq"))
                                                  , logLevelList);
    return returnMap;
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
    if (request.getParameter("seq") != null) {
      try {
        Long.parseLong(request.getParameter("seq"));
      } catch (NumberFormatException e) {
        throw new SystemException(9, "seq", request.getParameter("seq"));
      }
    }
    if (request.getParameter("log_level_fatal") != null) {
      if (request.getParameter("log_level_fatal").equals("true") == false && request.getParameter("log_level_fatal").equals("false") == false) {
        throw new SystemException(9, "log_level_fatal", request.getParameter("log_level_fatal"));
      }
    }
    if (request.getParameter("log_level_error") != null) {
      if (request.getParameter("log_level_error").equals("true") == false && request.getParameter("log_level_error").equals("false") == false) {
        throw new SystemException(9, "log_level_error", request.getParameter("log_level_error"));
      }
    }
    if (request.getParameter("log_level_warn") != null) {
      if (request.getParameter("log_level_warn").equals("true") == false && request.getParameter("log_level_warn").equals("false") == false) {
        throw new SystemException(9, "log_level_warn", request.getParameter("log_level_warn"));
      }
    }
    if (request.getParameter("log_level_info") != null) {
      if (request.getParameter("log_level_info").equals("true") == false && request.getParameter("log_level_info").equals("false") == false) {
        throw new SystemException(9, "log_level_info", request.getParameter("log_level_info"));
      }
    }
    if (request.getParameter("log_level_debug") != null) {
      if (request.getParameter("log_level_debug").equals("true") == false && request.getParameter("log_level_debug").equals("false") == false) {
        throw new SystemException(9, "log_level_debug", request.getParameter("log_level_debug"));
      }
    }
    if (request.getParameter("log_level_trace") != null) {
      if (request.getParameter("log_level_trace").equals("true") == false && request.getParameter("log_level_trace").equals("false") == false) {
        throw new SystemException(9, "log_level_trace", request.getParameter("log_level_trace"));
      }
    }
    Map<String, Object> inputMap = new HashMap<String, Object>();
    inputMap.put("from_datetime", new Date(Long.parseLong(request.getParameter("from_datetime"))));
    inputMap.put("to_datetime", new Date(Long.parseLong(request.getParameter("to_datetime"))));
    if (request.getParameter("log_typ_cd") != null) {
      inputMap.put("log_typ_cd", Long.parseLong(request.getParameter("log_typ_cd")));
    } else {
      inputMap.put("log_typ_cd", null);
    }
    inputMap.put("filter_txt", request.getParameter("filter_txt"));
    if (request.getParameter("start_seq") == null) {
      inputMap.put("start_seq", 0L);
    } else {
      inputMap.put("start_seq", Long.parseLong(request.getParameter("start_seq")));
    }
    if (request.getParameter("log_level_fatal") == null) {
      inputMap.put("log_level_fatal", false);
    } else if (request.getParameter("log_level_fatal").equals("true")) {
      inputMap.put("log_level_fatal", true);
    }
    if (request.getParameter("log_level_error") == null) {
      inputMap.put("log_level_error", false);
    } else if (request.getParameter("log_level_error").equals("true")) {
      inputMap.put("log_level_error", true);
    }
    if (request.getParameter("log_level_warn") == null) {
      inputMap.put("log_level_warn", false);
    } else if (request.getParameter("log_level_warn").equals("true")) {
      inputMap.put("log_level_warn", true);
    }
    if (request.getParameter("log_level_info") == null) {
      inputMap.put("log_level_info", false);
    } else if (request.getParameter("log_level_info").equals("true")) {
      inputMap.put("log_level_info", true);
    }
    if (request.getParameter("log_level_debug") == null) {
      inputMap.put("log_level_debug", false);
    } else if (request.getParameter("log_level_debug").equals("true")) {
      inputMap.put("log_level_debug", true);
    }
    if (request.getParameter("log_level_trace") == null) {
      inputMap.put("log_level_trace", false);
    } else if (request.getParameter("log_level_trace").equals("true")) {
      inputMap.put("log_level_trace", true);
    }
    long cnt = getAppLogListService.getAppLogListCnt(inputMap);
    if (cnt > MAX_LOG_CNT) {
      throw new UserException(15, "출력 갯수 : " + cnt);
    }
  }
}