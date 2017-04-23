/*
 * Title : KokodogExceptionResolver
 *
 * @Version : 1.0
 *
 * @Date : 2016-03-08
 *
 * @Copyright by 이민석
 */

package com.cmn.err;

import java.util.Map;
import java.util.HashMap;
import java.util.Date;
import java.util.Enumeration;
import java.lang.StackTraceElement;
import java.text.SimpleDateFormat;
import java.net.URLEncoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.http.ResponseEntity;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;
import com.cmn.err.UserException;
import com.cmn.err.SystemException;
import com.cmn.err.KokodogException;
import com.cmn.cmn.component.AddoptInfoComponent;

/**
 *  이 객체는 서블릿 수행 과정 중에 Exception이 발생하였을 때
 *  오류에 대한 로그를 출력하고
 *  오류 결과를 리턴하는 역할을 한다.
 */
@ControllerAdvice
public class KokodogExceptionResolver {
  @Autowired
  private SqlSession sqlSession;
  
  private static Logger logger = Logger.getLogger(KokodogExceptionResolver.class);

  /**
    *  사용자 선택에 따른 오류 출력
    *  @param request - 서블릿 Request
    *  @param response - 서블릿 응답이 정의된 response
    *  @param ex - Exception 이 발생한 클래스
    *  @return - 결과를 표현할 모델
    */
  @ExceptionHandler(KokodogException.class)
  public Object kokodogException(HttpServletRequest request, HttpServletResponse response, KokodogException ex) throws Exception {
    if (ex instanceof UserException) {
      printLogForUserException(request, (UserException)ex);
    } else {
      printLogForSystemException(request, ex);
    }
    AddoptInfoComponent t = (AddoptInfoComponent)request.getAttribute("_ADDOPT_INFO");
    t.setEndValue(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date()), ex);
    if (request.getMethod().equals("GET") == false) {
      Map<String, Object> returnMap = new HashMap<String, Object>();
      returnMap.put("error_num", ex.getMessageNum());
      returnMap.put("error_nm", ex.getMessage());
      if (ex.getErrTyp() != 0) {
        return (Object)(new ResponseEntity<Map>(returnMap, HttpStatus.valueOf(ex.getErrTyp())));
      } else {
        return (Object)(new ResponseEntity<Map>(returnMap, HttpStatus.INTERNAL_SERVER_ERROR));
      }
    } else {
      ModelAndView mav = new ModelAndView();
      if (ex.getErrTyp() != 0) {
        mav.setViewName("cmn/err/err_" + ex.getErrTyp());
      } else {
        mav.setViewName("cmn/err/err_500");
      }
      response.setStatus(ex.getErrTyp());
      return mav;
    }
  }
  
  /**
    *  사용자 오류로 인한 디버그 오류를 출력한다.
    *  @param request - 서블릿 Request
    *  @param ex - Exception 이 발생한 클래스
    */
  private void printLogForUserException(HttpServletRequest request, UserException ex) throws Exception {
    Enumeration param = request.getParameterNames();
    String queryParam = "";
	  while (param.hasMoreElements()) {
      String key = param.nextElement() + "";
      queryParam = queryParam + URLEncoder.encode(key, "UTF-8") + "=" + URLEncoder.encode(request.getParameter(key), "UTF-8") + "&";
	  }
    if (queryParam.equals("") == false) {
      queryParam = queryParam.substring(0, queryParam.length() - 1);
    }
    logger.debug("=================     User Exception Start    ==================");
    logger.debug("Exception Start Dtm[" + request.getAttribute("now_dtm") + "]");
    logger.debug("Request URI[" + request.getRequestURL().toString() + "]");
    logger.debug("Query String[" + queryParam + "]");
    logger.debug("UserNum[" + request.getSession().getAttribute("user_num") + "]");
    logger.debug("Remote Address[" + request.getRemoteAddr() + "]");
    logger.debug("Error Type[" + ex.getErrTyp() + "]");
    logger.debug("Exception End Dtm[" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "]");
    logger.debug("Error Trace!!");
    logger.debug(ex.getClass().getName() + ": " + ex.getMessage());
    StackTraceElement[] ste = ex.getStackTrace();
    for (int i = 0; i < ste.length; i++) {
      logger.debug("       at " + ste[i].toString());
    }
    logger.debug("=================      User Exception End     ==================");    
  }
  
  /**
    *  사용자 오류로 인한 디버그 오류를 출력한다.
    *  @param request - 서블릿 Request
    *  @param ex - Exception 이 발생한 클래스
    */
  private void printLogForSystemException(HttpServletRequest request, KokodogException ex) throws Exception {
    Enumeration param = request.getParameterNames();
    String queryParam = "";
	  while (param.hasMoreElements()){
      String key = param.nextElement() + "";
      queryParam = queryParam + URLEncoder.encode(key, "UTF-8") + "=" + URLEncoder.encode(request.getParameter(key), "UTF-8") + "&";
	  }
    if (queryParam.equals("") == false) {
      queryParam = queryParam.substring(0, queryParam.length() - 1);
    }
    logger.warn("=================     User Exception Start    ==================");
    logger.warn("Exception Start Dtm[" + request.getAttribute("now_dtm") + "]");
    logger.warn("Request URI[" + request.getRequestURL().toString() + "]");
    logger.warn("Query String[" + queryParam + "]");
    logger.warn("UserNum[" + request.getSession().getAttribute("user_num") + "]");
    logger.warn("Remote Address[" + request.getRemoteAddr() + "]");
    logger.warn("Error Type[" + ex.getErrTyp() + "]");
    logger.warn("Exception End Dtm[" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "]");
    logger.warn("Error Trace!!");
    logger.warn(ex.getClass().getName() + ": " + ex.getMessage());
    StackTraceElement[] ste = ex.getStackTrace();
    for (int i = 0; i < ste.length; i++) {
      logger.warn("       at " + ste[i].toString());
    }
    logger.warn("=================      User Exception End     ==================");    
  }
  
  /**
    *  시스템에 정의되어 있지 않은 예기치 못한 오류에 대하여 출력한다.
    *  @param request - 서블릿 Request
    *  @param response - 서블릿 응답이 정의된 response
    *  @param ex - Exception 이 발생한 클래스
    *  @return - 결과를 표현할 모델
    */
  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public Object exception(HttpServletRequest request, HttpServletResponse response, Exception ex) throws Exception {
    Enumeration param = request.getParameterNames();
    String queryParam = "";
	  while (param.hasMoreElements()){
      String key = param.nextElement() + "";
      queryParam = queryParam + URLEncoder.encode(key, "UTF-8") + "=" + URLEncoder.encode(request.getParameter(key), "UTF-8") + "&";
	  }
    if (queryParam.equals("") == false) {
      queryParam = queryParam.substring(0, queryParam.length() - 1);
    }
    logger.error("=================     Internal Exception Start    ==================");
    logger.error("Exception Start Dtm[" + request.getAttribute("now_dtm") + "]");
    logger.error("Request URI[" + request.getRequestURL().toString() + "]");
    logger.error("Query String[" + queryParam + "]");
    logger.error("UserNum[" + request.getSession().getAttribute("user_num") + "]");
    logger.error("Remote Address[" + request.getRemoteAddr() + "]");
    logger.error("Exception End Dtm[" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "]");
    logger.error("Error Trace!!");
    logger.error(ex.getClass().getName() + ": " + ex.getMessage());
    StackTraceElement[] ste = ex.getStackTrace();
    for (int i = 0; i < ste.length; i++) {
      logger.error("       at " + ste[i].toString());
    }
    logger.error("=================      Internal Exception End     ==================");
    AddoptInfoComponent t = (AddoptInfoComponent)request.getAttribute("_ADDOPT_INFO");
    t.setEndValue(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date()), ex);
    Map<String, Object> inputMap = new HashMap<String, Object>();
    inputMap.put("msg_num", 8);
    logger.debug("Input Map of getCmnErrorMessage SQL Map - " + inputMap);
    Map<String, Object> outputMap = sqlSession.selectOne("getCmnErrorMessage", inputMap);
    logger.debug("Output Map of getCmnErrorMessage SQL Map - " + outputMap);
    if (request.getMethod().equals("GET") == false) {
      Map<String, Object> returnMap = new HashMap<String, Object>();
      returnMap.put("error_nm", outputMap.get("message"));
      returnMap.put("error_sub_nm", ex.getMessage());
      return (Object)(new ResponseEntity<Map>(returnMap, HttpStatus.INTERNAL_SERVER_ERROR));
    } else {
      ModelAndView mav = new ModelAndView();
      mav.setViewName("cmn/err/err_500");
      response.setStatus(500);
      return mav;
    }
  }
}