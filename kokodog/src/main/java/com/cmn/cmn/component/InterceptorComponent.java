/*
 * Title : InterceptorComponent
 *
 * @Version : 1.0
 *
 * @Date : 2016-03-08
 *
 * @Copyright by 이민석
 */

package com.cmn.cmn.component;

import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;
import java.lang.Thread;
import java.net.URLEncoder;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.TimeZone;
import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.RequestDispatcher;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.ibatis.session.SqlSession;

import com.cmn.err.UserException;
import com.cmn.err.SystemException;
import com.cmn.cmn.component.AddoptInfoComponent;
import com.cmn.cmn.service.GetServerTimeService;
import com.cmn.cmn.service.OAuthLoginService;

/**
 *  이 객체는 서블릿 수행 전 혹은 후에 기능을 적용하기 위하여
 *  생성한다.
 */
public class InterceptorComponent extends HandlerInterceptorAdapter {
  @Autowired
  private SqlSession sqlSession;
  
  @Autowired
  private UserException userException;
  
  @Autowired
  private SystemException systemException;
  
  @Autowired
  private GetServerTimeService getServerTimeService;
  
  @Autowired
  private OAuthLoginService oAuthLoginService;
  
  private static Logger logger = Logger.getLogger(InterceptorComponent.class);
  
  /**
    *  서블릿이 호출되기 전에 수행
    *  @param request - 서블릿 Request
    *  @param response - 서블릿 응답이 정의된 response
    *  @param handler - 핸들러
    *  @return - true : 요청한 서블릿 정상 호출, false : 요청한 서블릿 호출하지 않고 리턴
    */
  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    logger.debug("============   Start method of InterceptorComponent.preHandle   ============");
    request.setAttribute("system_call_dtm", getServerTimeService.getServerTime());
    RequestDispatcher rd = null;
    Calendar calendar = new GregorianCalendar(TimeZone.getDefault());
    request.setAttribute("now_dtm", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(calendar.getTime()));
    String startTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(calendar.getTime());
    AddoptInfoComponent addoptInfoComponent = new AddoptInfoComponent(request, response, sqlSession, startTime);
    addoptInfoComponent.start();
    request.setAttribute("_ADDOPT_INFO", addoptInfoComponent);
    checkLogin(request, response);
    Map<String, String> pgmInfo = getPgmTaskPageName(request.getRequestURI(), request.getMethod());
    if ("GET".equals(request.getMethod()) == true && pgmInfo.get("pgm") != null && pgmInfo.get("pgm").equals("FileDown") == true) {
      fileDownMain(request, response);
      return false;
    }
    if (isPageExists(pgmInfo.get("pgm"), pgmInfo.get("task"), pgmInfo.get("page")) == false) {
      throw userException.userException(1);
    }
    if (isAuthExists((request.getSession().getAttribute("user_num") != null) ? ((Integer)(request.getSession().getAttribute("user_num"))).intValue() : 0, pgmInfo.get("pgm"), pgmInfo.get("task"), pgmInfo.get("page")) == false) {
      if (request.getSession().getAttribute("user_num") == null) {
        if (request.getMethod().equals("GET") == true ) {
          String url = request.getRequestURL().toString();
          if (request.getQueryString() != null) {
            url = url + "?" + request.getQueryString();
          }
          response.sendRedirect("/cmn/cmn/login?redirect_url=" + URLEncoder.encode(url, "UTF-8"));
          addoptInfoComponent.setEndValue(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date()), null);
          return false;
        } else {
          throw userException.userException(4);
        }
      } else {
        throw userException.userException(2);
      }
    } else if ("GET".equals(request.getMethod()) == true) {
      request.setAttribute("_VIEW_URL", returnView(request, pgmInfo));
      return true;
    } else {
      return true;
    }
  }
  
  /**
    *  서블릿이 수행 완료되고 호출됨. 오류시에는 미호출.
    *  @param request - 서블릿 Request
    *  @param response - 서블릿 응답이 정의된 response
    *  @param handler - 핸들러
    *  @param handler - 서블릿 요청을 수행한 최종 결과
    */
  @Override
  public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView mav) throws Exception {
    if (request.getAttribute("_TIME_OUT") != null && ((Boolean)(request.getAttribute("_TIME_OUT"))).booleanValue() == true) {
      Map<String, Object> inputMap = new HashMap<String, Object>();
      inputMap.put("msg_num", 7);
      logger.debug("Input map of SQL getCmnMessageByMsgNum - " + inputMap);
      Map<String, Object> outputMap = sqlSession.selectOne("getCmnMessageByMsgNum", inputMap);
      logger.debug("Output map of SQL getCmnMessageByMsgNum  " + outputMap);
      String msg = (String)outputMap.get("msg");
      int errTyp = ((Integer)outputMap.get("err_typ")).intValue();
      if (mav != null) {
        mav = new ModelAndView();
        Map<String, Object> returnMap = new HashMap<String, Object>();
        returnMap.put("error_num", 7);
        returnMap.put("error_nm", msg);
        if (request.getMethod().equals("GET") == true) {
          mav.setViewName("err_" + errTyp);
        } else {
          mav.setViewName("jsonView");
        }
      }
      response.setStatus(errTyp);
    }    
  }
  
  /**
    *  서블릿이 수행 완료되고 호출됨. 오류시에는 호출.
    *  @param request - 서블릿 Request
    *  @param response - 서블릿 응답이 정의된 response
    *  @param handler - 핸들러
    *  @param ex - 서블릿 요청을 수행했을 때 오류 전달
    */
  @Override
  public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    if (request.getAttribute("_TIME_OUT") != null && ((Boolean)(request.getAttribute("_TIME_OUT"))).booleanValue() == true) {
      return;
    }
    AddoptInfoComponent t = (AddoptInfoComponent)request.getAttribute("_ADDOPT_INFO");
    t.setEndValue(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date()), ex);
  }

  /**
    *  로그인 세션 처리를 Google OAuth 토큰 만료를 기준으로 판단하여 진행.
    *  @param request - 서블릿 Request
    *  @param response - 서블릿 응답이 정의된 response
    */
  private void checkLogin(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Map<String, Object> inputMap = new HashMap<String, Object>();
    Map<String, Object> outputMap = null;
    if (request.getSession().getAttribute("user_num") != null) {
      inputMap.put("user_num", request.getSession().getAttribute("user_num"));
      logger.debug("Input Map of SQL getIsAccessTokenExist - " + inputMap.toString());
      outputMap = sqlSession.selectOne("getIsAccessTokenExist", inputMap);
      logger.debug("Ouput Map of SQL getIsAccessTokenExist - " + outputMap);
      if (outputMap == null || outputMap.get("is_access_token_exist") == null || ((String)(outputMap.get("is_access_token_exist"))).equals("Y") == false) {
        inputMap.clear();
        inputMap.put("user_num", request.getSession().getAttribute("user_num"));
        logger.debug("Input Map of SQL getCmnRequestTokenByUserNum - " + inputMap);
        outputMap = sqlSession.selectOne("getCmnRequestTokenByUserNum", inputMap);
        logger.debug("Ouput Map of SQL getCmnRequestTokenByUserNum - " + outputMap);
        if (outputMap != null && outputMap.get("request_token") != null) {
          outputMap = oAuthLoginService.getAccessTokenByRefreshToken((String)outputMap.get("request_token"));
          oAuthLoginService.insertAccessToken((String)outputMap.get("access_token"), ((Integer)outputMap.get("expires_in")).intValue(), (String)request.getAttribute("now_dtm"), ((Integer)request.getSession().getAttribute("user_num")).intValue());
          request.getSession().setAttribute("user_num", request.getSession().getAttribute("user_num"));
        } else {
          request.getSession().setAttribute("user_num", null);
        }
      }
    } else {
      request.getSession().setAttribute("user_num", request.getSession().getAttribute("user_num"));
    }    
  }
  
  /**
    *  URL을 기준으로 호출한 프로그램, 업무, 페이지 분석 후 리턴
    *  @param uri - 호출 URL
    *  @return - 분석한 프로그램, 업무, 페이지명을 포함한 map
    */
  private Map<String, String> getPgmTaskPageName(String uri, String method) throws Exception {
    Map<String, String> returnMap = new HashMap<String, String>();
    String[] seperatedRequestUriTemp = uri.split("/");
    String[] seperatedRequestUri = null;
    if (seperatedRequestUriTemp.length > 1) {
      seperatedRequestUri = Arrays.copyOfRange(seperatedRequestUriTemp, 1, seperatedRequestUriTemp.length);
      if (method.equals("GET") == true && seperatedRequestUri.length > 3) {
        throw userException.userException(1);
      } else if (seperatedRequestUri.length > 4) {
          throw userException.userException(1);
      }
      String pgm = null;
      if (seperatedRequestUri[0].equals("")) {
        pgm = "cmn";
      } else {
        pgm = seperatedRequestUri[0];
      }
      String task = null;
      String page = null;
      if (seperatedRequestUri.length > 1) {
         task = seperatedRequestUri[1];
        if (seperatedRequestUri.length > 2) {
          page = seperatedRequestUri[2];
        } else {
          page = "main";
        }
      } else {
        task = "cmn";
      }
      returnMap.put("pgm", pgm);
      returnMap.put("task", task);
      returnMap.put("page", page);
    } else {
      returnMap.put("pgm", "cmn");
      returnMap.put("task", "cmn");
      returnMap.put("page", "main");
    }
    return returnMap;
  }
  
  /**
    *  호출한 페이지가 실제 존재하는지 여부 판단. 미존재 시 오류를 제출하기 위함.
    *  @param pgm - 프로그램
    *  @param task - 업무
    *  @param page - 페이지
    *  @return - true : 페이지 존재, false : 페이지 미존재
    */
  private boolean isPageExists(String pgm, String task, String page) throws Exception {
    logger.debug("============   Start method of InterceptorComponent.isPageExists   ============");
    logger.debug(" Parameter - pgm[" + pgm + "], task[" + task + "], page[" + page + "]");
    boolean ret = false;
    Map<String, Object> inputMap = new HashMap<String, Object>();
    inputMap.put("pgm_abb", pgm);
    inputMap.put("task_abb", task);
    inputMap.put("page_abb", page);
    Map<String, Object> outputMap = sqlSession.selectOne("getCmnIsPageExist", inputMap);
    if (outputMap == null || outputMap.get("is_page_exists") == null || outputMap.get("is_page_exists").equals("Y") == false) {
      ret = false;
    } else {
      ret = true;
    }
    logger.debug(" return[" + ret + "]");
    return ret;
  }
  
  /**
    *  호출한 페이지가 현재 로그인한 권한으로 접근 가능한지 확인. 접근 불가 시 오류를 제출하기 위함.
    *  @param pgm - 프로그램
    *  @param task - 업무
    *  @param page - 페이지
    *  @return - true : 접근 가능, false : 접근 불가
    */
  private boolean isAuthExists(int userNum, String pgm, String task, String page) throws Exception {
    logger.debug("============   Start method of InterceptorComponent.isAuthExists   ============");
    logger.debug(" Parameter - userNum[" + userNum + "], pgm[" + pgm + "], task[" + task + "], page[" + page + "]");
    boolean ret = false;
    Map<String, Object> inputMap = new HashMap<String, Object>();
    inputMap.put("pgm_abb", pgm);
    inputMap.put("task_abb", task);
    inputMap.put("page_abb", page);
    inputMap.put("user_num", userNum);
    Map<String, Object> outputMap = sqlSession.selectOne("getCmnIsLoginAuth", inputMap);
    if (outputMap == null || outputMap.get("is_auth") == null || outputMap.get("is_auth").equals("Y") == false) {
      ret = false;
    } else {
      ret = true;
    }
    logger.debug(" return[" + ret + "]");
    return ret;
  }
  
  private void fileDownMain(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Map<String, Object> inputMap = new HashMap<String, Object>();
    inputMap.put("file_key", request.getParameter("file_key"));
    logger.debug("Input map of SQL getCmnFileInfo - " + inputMap);
    Map<String, Object> outputMap = sqlSession.selectOne("getCmnFileInfo", inputMap);
    logger.debug("Output map of SQL getCmnFileInfo - " + outputMap);
    int contentCnt = ((Long)outputMap.get("content_cnt")).intValue();
    int fileNum = ((Long)outputMap.get("file_num")).intValue();
    String fileName = new String((String)outputMap.get("file_nm"));
    long fileLength = ((BigDecimal)outputMap.get("content_length")).longValue();
    response.setContentType("application/octet-stream");
    response.setHeader("Content-Disposition", "attachment; fileName=\"" + URLEncoder.encode(fileName, "UTF-8") + "\";");
    response.setHeader("Content-Transfer-Encoding", "binary");
    response.setContentType("application/octet-stream");
    response.setContentLength((int)fileLength);
    byte[] fileContent = null;
    for (int i = 0; i < contentCnt; i++) {
      inputMap.clear();
      inputMap.put("file_num", fileNum);
      inputMap.put("seq", i + 1);
      logger.debug("Input map of SQL getCmnFileContent - " + inputMap);
      outputMap = sqlSession.selectOne("getCmnFileContent", inputMap);
      fileContent = (byte[])outputMap.get("content");
      response.getOutputStream().write(fileContent);
      response.getOutputStream().flush();
      outputMap.clear();
    }   
    response.getOutputStream().close();
  }

  private String returnView(HttpServletRequest request, Map<String, String> pgmInfo) throws Exception {
    String userAgent = null;
    boolean isMobile = false;
    String returnUrl = null;
    Map<String, Object> inputMap = new HashMap<String, Object>();
    Map<String, Object> outputMap = null;
    if (request.getSession().getAttribute("is_mobile") == null) {
      userAgent = request.getHeader("User-Agent").toLowerCase();
      if (userAgent.indexOf("mobile") >= 0) {
        isMobile = true;
      } else {
        isMobile = false;
      }
    } else if ("Y".equals((String)request.getSession().getAttribute("is_mobile")) == true) {
      isMobile = true;
    } else {
      isMobile = false;
    }
    if (isMobile == true) {
      inputMap.put("pgm_abb", pgmInfo.get("pgm"));
      inputMap.put("task_abb", pgmInfo.get("task"));
      inputMap.put("page_abb", pgmInfo.get("page"));
      logger.debug("Input map of SQL getCmnIsMobilePageExist - " + inputMap);
      outputMap = sqlSession.selectOne("getCmnIsMobilePageExist", inputMap);
      logger.debug("Output map of SQL getCmnIsMobilePageExist - " + outputMap);
      if (outputMap != null && outputMap.get("is_exist") != null && outputMap.get("is_exist").equals("Y") == true) {
        returnUrl = "m/";
      } else {
        returnUrl = "";
      }
    } else {
      returnUrl = "";
    }
    returnUrl = returnUrl + pgmInfo.get("pgm") + "/" + pgmInfo.get("task") + "/" + pgmInfo.get("page");
    return returnUrl;
  }
}