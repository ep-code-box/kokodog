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

import java.util.Map;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import com.cmn.err.UserException;
import com.cmn.err.SystemException;

/**
 *  이 객체는 서블릿이 호출되었을 때 각종 정보를 분석하고
 *  삽입하기 위하여 새로운 스레드를 생성하기 위한 클래스이다.
 */
public class AddoptInfoComponent extends Thread {
  private static int TIME_OUT = 30;
  private int seq;
  private HttpServletRequest request;
  private HttpServletResponse response;
  private SqlSession sqlSession;
  private static Logger logger = Logger.getLogger(AddoptInfoComponent.class);
  private String startTime;
  private String endTime;
  private int userNum;
  private String exMessage;
  private boolean isCallEnd;
  private int status;
    
  /**
    *  생성자
    *  @param request - 서블릿 Request
    *  @param response - 서블릿 응답이 정의된 response
    *  @param sqlSession - DB 세션이 담긴 Pool
    */    
  public AddoptInfoComponent(HttpServletRequest request, HttpServletResponse response, SqlSession sqlSession, String startTime) {
    this.request = request;
    this.response = response;
    this.sqlSession = sqlSession;
    this.startTime = startTime;
    this.userNum = ((request.getSession().getAttribute("user_num") != null) ? ((Integer)(request.getSession().getAttribute("user_num"))).intValue() : 0);
    this.isCallEnd = false;
    this.exMessage = null;
    this.endTime = null;
    this.status = 0;
  }
  /**
    *  쓰레드 수행
    */
  @Override
  public void run() {
    logger.debug("ConnInsert Log Thread Start");
    insertConnLog(sqlSession);
    int intTime = 0;
    while (this.isCallEnd == false && intTime < TIME_OUT) {
      try {
        Thread.sleep(1000);
      } catch (Exception e) {
        return;
      }
      intTime++;
    }
    if (intTime != TIME_OUT) {
      updateConnEndLog(sqlSession);
    } else {
      updateTimeoutConnEndLog(sqlSession);      
    }
  }
    
  /**
    *  커넥션 로그 삽입
    *  @param sqlSession - DB 커넥션 풀
    */
  private void insertConnLog(SqlSession sqlSession) {
    Map<String, Object> inputMap = new HashMap<String, Object>();
    inputMap.put("now_dtm", startTime.substring(0, startTime.indexOf(".")));
    inputMap.put("millisec", startTime.substring(startTime.indexOf(".") + 1));
    inputMap.put("user_num", request.getSession().getAttribute("user_num") != null ? ((Integer)(request.getSession().getAttribute("user_num"))).intValue() : 0);
    inputMap.put("ip", request.getRemoteAddr());
    inputMap.put("url", request.getRequestURL().toString());
    inputMap.put("query_string", request.getQueryString());
    inputMap.put("method", request.getMethod());
    inputMap.put("ap_num", System.getProperty("apnum"));
    inputMap.put("container_num", System.getProperty("containernum"));
    logger.debug("Input Map of SQL Map insertCmnConnLog - " + inputMap);
    sqlSession.insert("insertCmnConnLog", inputMap);
    inputMap.clear();
    inputMap.put("now_dtm", startTime);
    inputMap.put("millisec", startTime.substring(startTime.indexOf(".") + 1));
    logger.debug("Input Map of SQL Map getCmnLastSeqConnLog - " + inputMap);
    Map<String, Object> outputMap = sqlSession.selectOne("getCmnLastSeqConnLog", inputMap);
    logger.debug("Output Map of SQL Map getCmnLastSeqConnLog - " + outputMap);
    seq = ((Long)outputMap.get("seq")).intValue();
  }

  /**
    *  커넥션 종료 로그 삽입
    *  @param sqlSession - DB 커넥션 풀
    */
  private void updateConnEndLog(SqlSession sqlSession) {
    Map<String, Object> inputMap = new HashMap<String, Object>();
    inputMap.put("end_dtm", endTime.substring(0, endTime.indexOf(".")));
    inputMap.put("end_millisec", endTime.substring(endTime.indexOf(".") + 1));
    inputMap.put("response_num", status);
    inputMap.put("error_msg", exMessage);
    inputMap.put("now_dtm", startTime.substring(0, startTime.indexOf(".")));
    inputMap.put("millisec", startTime.substring(startTime.indexOf(".") + 1));
    inputMap.put("seq", seq);
    inputMap.put("user_num", userNum);
    logger.debug("Input Map of SQL Map updateCmnConnEndLog - " + inputMap);
    sqlSession.insert("updateCmnConnEndLog", inputMap);
  }
  
  private void updateTimeoutConnEndLog(SqlSession sqlSession) {
    Map<String, Object> inputMap = new HashMap<String, Object>();
    inputMap.put("end_dtm", endTime.substring(0, endTime.indexOf(".")));
    inputMap.put("end_millisec", endTime.substring(endTime.indexOf(".") + 1));
    inputMap.put("response_num", 408);
    inputMap.put("error_msg", "Server request timeout");
    inputMap.put("now_dtm", startTime.substring(0, startTime.indexOf(".")));
    inputMap.put("millisec", startTime.substring(startTime.indexOf(".") + 1));
    inputMap.put("seq", seq);
    inputMap.put("user_num", userNum);
    logger.debug("Input Map of SQL Map updateCmnConnEndLog - " + inputMap);
    sqlSession.insert("updateCmnConnEndLog", inputMap);
    request.setAttribute("_TIME_OUT", true);
  }
  
  public void setEndValue(String endTime, Exception ex) {
    this.endTime = new String(endTime);
    if (ex != null) {
      this.exMessage = new String(ex.getMessage());
      if (ex instanceof SystemException == true) {
        this.status = (((SystemException)ex).getErrTyp() == 0) ? 500 : ((SystemException)ex).getErrTyp();
      } else if (ex instanceof UserException == true) {
        this.status = (((UserException)ex).getErrTyp() == 0) ? 500 : ((UserException)ex).getErrTyp();        
      } else {
        this.status = 500;
      }
    } else {
      this.status = 200;
    }
    this.isCallEnd = true;
  }
}
