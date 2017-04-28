/*
 * Title : AddoptInfoComponent
 *
 * @Version : 1.0
 *
 * @Date : 2016-03-08
 *
 * @Copyright by 이민석
 */

package com.cmn.cmn.component;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.annotation.PreDestroy;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.cmn.err.UserException;
import com.cmn.err.SystemException;
import com.cmn.cmn.service.ConnLogService;
import com.cmn.cmn.service.GetServerTimeService;
import com.cmn.cmn.service.MessageService;

/**
 *  이 객체는 서블릿이 호출되었을 때 각종 정보를 분석하고
 *  삽입하기 위하여 새로운 스레드를 생성하기 위한 클래스이다.
 */
@Component
public class AddoptInfoComponent {
  @Autowired
  private ConnLogService connLogService;
  
  @Autowired
  private MessageService messageService;
  
  @Autowired
  private GetServerTimeService getServerTimeService;
  
  private static int TIME_OUT = 30;
  private boolean setStart = false;
  private long setDayConnCnt = 0L;
  private List<Map<String, Object>> connList = null;
  private boolean isClosed = false;
  private long prevDay = 0L;
    
  private static Logger logger = Logger.getLogger(AddoptInfoComponent.class);

  @Async
  public void run() {
    logger.debug("============   Start method of AddoptInfoComponent.run   ============");
    connList = new ArrayList<Map<String, Object>>();
    isClosed = false;
    int i = 0;
    while (isClosed == false) {
      try {
        logger.debug("AddoptInfoComponnet.run running...");
        logger.debug("conList : " + connList);
        for (i = 0; i < connList.size(); i++) {
          if (connList.get(i).get("db_update").equals("N") == true) {
            long seq = connLogService.insertConnLog(((Long)connList.get(i).get("system_call_dtm")).longValue()
                                                   , connList.get(i).get("user_num") == null ? 0 : ((Integer)connList.get(i).get("user_num")).intValue()
                                                   , (String)connList.get(i).get("remote_addr")
                                                   , (String)connList.get(i).get("request_url")
                                                   , (String)connList.get(i).get("query_string")
                                                   , (String)connList.get(i).get("method"));
            connList.get(i).put("db_update", "Y");
            connList.get(i).put("seq", seq);
          }
          if (connList.get(i).get("end_call_dtm") != null) {
            connLogService.updateConnEndLog(((Long)connList.get(i).get("system_call_dtm")).longValue()
                                            , ((Long)connList.get(i).get("end_call_dtm")).longValue()
                                            , connList.get(i).get("response_num") == null ? null : ((Integer)connList.get(i).get("response_num")).intValue()
                                            , (String)connList.get(i).get("err_msg")
                                            , ((Long)connList.get(i).get("seq")).longValue()
                                            , connList.get(i).get("user_num") == null ? 0 : ((Integer)connList.get(i).get("user_num")).intValue());
            connList.remove(i);
          } else if (((Long)connList.get(i).get("system_call_dtm")).longValue() < getServerTimeService.getServerTime() - 1000L * (long)TIME_OUT) {
            connLogService.updateTimeoutConnEndLog(((Long)connList.get(i).get("system_call_dtm")).longValue()
                                                   , getServerTimeService.getServerTime()
                                                   , ((Long)connList.get(i).get("seq")).longValue()
                                                   , connList.get(i).get("user_num") == null ? 0 : ((Integer)connList.get(i).get("user_num")).intValue());
//            ((HttpServletResponse)(connList.get(i).get("response"))).setStatus(408);
//            ((HttpServletResponse)(connList.get(i).get("response"))).getOutputStream().write(((String)messageService.getErrMessageByMessageNum(7).get("msg")).getBytes("UTF-8"));
//            ((HttpServletResponse)(connList.get(i).get("response"))).getOutputStream().flush();
            
            connList.remove(i);
          }
        }
        Thread.sleep(1000);
      } catch (Exception e) {
        StackTraceElement[] ste = e.getStackTrace();
        logger.error("=================     Internal Exception Start    ==================");
        logger.error("Current Time[" + new SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date()) + "]");
        logger.error("Error Trace!!");
        logger.error("Error Main Message : " + e.getMessage());
        logger.error(e.getClass().getName() + ": " + e.getMessage());
        for (i = 0; i < ste.length; i++) {
          logger.error("       at " + ste[i].toString());
        }
        logger.error("=================      Internal Exception End     ==================");
        try {
          Thread.sleep(1000);
        } catch (Exception e2) {
          return;
        }
      }
    }
  }
  
  public long newConnList(HttpServletRequest request, HttpServletResponse response, long systemCallDtm, Integer userNum, String remoteAddr, String requestUrl, String queryString, String method) {
    logger.debug("============   Start method of AddoptInfoComponent.newConnList   ============");
    Map<String, Object> inputListMap = new HashMap<String, Object>();
    Calendar tmpCal = null;
    Calendar systemCallCal = null;
    int i = 0;
    try {
      if (prevDay == 0L) {
        tmpCal = (Calendar)GregorianCalendar.getInstance();
        tmpCal.setTimeInMillis(systemCallDtm);
        setDayConnCnt = 0L;
        tmpCal.set(Calendar.HOUR, 0);
        tmpCal.set(Calendar.MINUTE, 0);
        tmpCal.set(Calendar.SECOND, 0);
        tmpCal.set(Calendar.MILLISECOND, 0);
        prevDay = tmpCal.getTimeInMillis();
      }
      systemCallCal = (Calendar)GregorianCalendar.getInstance();
      systemCallCal.setTimeInMillis(systemCallDtm);
      systemCallCal.set(Calendar.HOUR, 0);
      systemCallCal.set(Calendar.MINUTE, 0);
      systemCallCal.set(Calendar.SECOND, 0);
      systemCallCal.set(Calendar.MILLISECOND, 0);
      if (prevDay != systemCallCal.getTimeInMillis()) {
        prevDay = systemCallCal.getTimeInMillis();
        setDayConnCnt = 0L;
      }
      setDayConnCnt++;
      inputListMap.put("request", request);
      inputListMap.put("response", response);
      inputListMap.put("system_call_dtm", systemCallDtm);
      inputListMap.put("user_num", userNum);
      inputListMap.put("remote_addr", remoteAddr);
      inputListMap.put("request_url", requestUrl);
      inputListMap.put("query_string", queryString);
      inputListMap.put("method", method);
      inputListMap.put("db_update", "N");
      inputListMap.put("set_day_conn_cnt", setDayConnCnt);
      connList.add(inputListMap);
      return setDayConnCnt;
    } catch (Exception e) {
      StackTraceElement[] ste = e.getStackTrace();
      logger.error("=================     Internal Exception Start    ==================");
      logger.error("Current Time[" + new SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date()) + "]");
      logger.error("Error Trace!!");
      logger.error("Error Main Message : " + e.getMessage());
      logger.error(e.getClass().getName() + ": " + e.getMessage());
      for (i = 0; i < ste.length; i++) {
        logger.error("       at " + ste[i].toString());
      }
      logger.error("=================      Internal Exception End     ==================");
      return 0L;
    }
  }
  
  @Async
  public void endConnUpdate(HttpServletRequest request, HttpServletResponse response, long endCallDtm, int responseNum, String errMsg, long connSeq, long systemCallDtm) {
    logger.debug("============   Start method of AddoptInfoComponent.endConnUpdate   ============");
    int cnt = 0;
    int i = 0;
    try {
      logger.debug("testttt : " + connList + "connSeq : " + connSeq);
      for (i = 0; i < connList.size(); i++) {
        if (request == (HttpServletRequest)connList.get(i).get("request") && ((Long)connList.get(i).get("set_day_conn_cnt")).longValue() == connSeq && ((Long)connList.get(i).get("system_call_dtm")).longValue() == systemCallDtm) {
          connList.get(i).put("end_call_dtm", endCallDtm);
          connList.get(i).put("response_num", responseNum == 0 ? 200 : responseNum);
          connList.get(i).put("err_msg", errMsg);
          break;
        }
      }
    } catch (Exception e) {
      StackTraceElement[] ste = e.getStackTrace();
      logger.error("=================     Internal Exception Start    ==================");
      logger.error("Current Time[" + new SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date()) + "]");
      logger.error("Error Trace!!");
      logger.error("Error Main Message : " + e.getMessage());
      logger.error(e.getClass().getName() + ": " + e.getMessage());
      for (i = 0; i < ste.length; i++) {
        logger.error("       at " + ste[i].toString());
      }
      logger.error("=================      Internal Exception End     ==================");
    }
  }
  
  @PreDestroy
  public void setClose() {
    this.isClosed = true;
  }
}
