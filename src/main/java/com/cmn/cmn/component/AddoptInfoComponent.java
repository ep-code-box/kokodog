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
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.annotation.PreDestroy;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.cmn.cmn.service.ConnLogService;
import com.cmn.cmn.service.GetServerTimeService;
import com.cmn.cmn.service.MessageService;

/**
 *  이 객체는 서블릿이 http 혹은 https 프로토콜을 통해 호출되었을 때 각종 정보를 분석하고
 *  삽입하기 위하여 새로운 스레드를 생성하기 위한 클래스이다.<br/>
 *  전달받은 request는 해당 객체 내의 newConnList 메서드를 통해 request에 담긴 정보가 임시적으로
 *  저장되고 종료시에는 endConnUpdate 메서드를 통해 서블릿 종료 정보가 저장된다.<br/>
 *  해당 클래스 내부에서 수행되는 데몬(메서드명 : run)을 통해 저장된 connection 정보 및 connection
 *  종료 정보를 지속적으로 분석하여 DB에 삽입해야 할 내용을 결정하고 Connection 로그를 DB에 저장한다.<br/>
 *  주의 - 해당 클래스를 사용하기 위해서는 사전에 하기 스크립트로 구성된 DB Table이 먼저 생성되어 있어야 한다.<br/>
 *  CREATE TABLE `cmn_conn_log` (<br/>
 *    `time` datetime NOT NULL,<br/>
 *    `millisecond` int(3) UNSIGNED NOT NULL,<br/>
 *    `seq` int(10) UNSIGNED NOT NULL,<br/>
 *    `audit_id` int(10) UNSIGNED DEFAULT NULL,<br/>
 *    `audit_dtm` datetime NOT NULL,<br/>
 *    `ip` varchar(15) NOT NULL,<br/>
 *    `url` mediumtext NOT NULL,<br/>
 *    `query_string` mediumtext,<br/>
 *    `user_num` int(10) UNSIGNED DEFAULT NULL,<br/>
 *    `method` varchar(10) NOT NULL,<br/>
 *    `end_dtm` datetime DEFAULT NULL,<br/>
 *    `end_millisec` int(3) DEFAULT NULL,<br/>
 *    `response_num` int(3) DEFAULT NULL,<br/>
 *    `error_msg` mediumtext,<br/>
 *    `ap_num` int(2) NOT NULL,<br/>
 *    `container_num` int(2) NOT NULL<br/>
 *  ) ENGINE=MyISAM DEFAULT CHARSET=utf8;<br/>
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
  private long setDayConnCnt = 0L;
  private List<Map<String, Object>> connList = null;
  private boolean isClosed = false;
  private long prevDay = 0L;
    
  private static Logger logger = LogManager.getLogger(AddoptInfoComponent.class);

  /**
   *  이 메서드는 종료 신호 전까지 Connection 정보를 모니터링 하여 DB에 적재해준다.
   *  주의 - 관리자 외 절대 실행하지 말 것
   */
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
  
  /**
   *  이 메서드는 전달받은 Connection 정보를 임시 저장소에 저장하여 DB에 적재할 준비를 한다.
   *  @param request : Request 내용
   *  @param response : response 내용
   *  @param systemCallDtm : 실제 Request가 발생한 서버 시각
   *  @param userNum : 세션에 저장되어 있는 사용자 번호(user_num). 로그인을 안한 사용자가 request를 보냈다면 null이 전달된다.
   *  @param remoteAddr : request를 보낸 곳의 IP 정보
   *  @param requestUrl : Request 호출시에 전달한 URL 정보
   *  @param queryString : Request에 포함된 parameter내용(양식 : key1=value1&key2=value2&...)
   *  @param method : Request 방식(POST, GET 등)
   *  @return 해당 request 날짜 별 request를 구분짓는 Sequence 번호
   *  @throws 기타 익셉션
   */
  public long newConnList(HttpServletRequest request, HttpServletResponse response, long systemCallDtm, Integer userNum, String remoteAddr, String requestUrl, String queryString, String method) throws Exception {
    logger.debug("============   Start method of AddoptInfoComponent.newConnList   ============");
    Map<String, Object> inputListMap = new HashMap<String, Object>();
    Calendar tmpCal = null;
    Calendar systemCallCal = null;
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
  }
  
  /**
   *  이 메서드는 전달받은 Connection 종료 정보를 임시 저장소에 저장하여 DB에 적재할 준비를 한다.
   *  @param request : Request 내용
   *  @param response : response 내용
   *  @param endCallDtm : 실제 Request가 처리 완료된 시각
   *  @param responseNum : response에 담겨진 status 번호
   *  @param errMsg : 오류 메시지. 오류가 아닐 경우는 null 이다.
   *  @param connSeq : newConnList 메서드에서 전달받은 커넥션 번호(일자별 Sequence 번호))
   *  @param systemCallDtm : Connection이 맺어진 시각. newConnList 에서 호출했던 시각과 동일해야 한다.
   *  @throws 기타 익셉션
   */
  @Async
  public void endConnUpdate(HttpServletRequest request, HttpServletResponse response, long endCallDtm, int responseNum, String errMsg, long connSeq, long systemCallDtm) {
    logger.debug("============   Start method of AddoptInfoComponent.endConnUpdate   ============");
    int i = 0;
    try {
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
  
  /**
   *  DB에 삽입하는 thread를 종료하기 위한 메서드
   *  주의 - 관리자 외 절대 실행하지 말 것
   */
  @PreDestroy
  public void setClose() {
    this.isClosed = true;
  }
}
