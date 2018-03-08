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

import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import com.cmn.err.KokodogException;
import com.cmn.err.UserException;
import com.cmn.err.SystemException;
import com.cmn.cmn.component.AddoptInfoComponent;
import com.cmn.cmn.service.GetServerTimeService;
import com.cmn.cmn.service.OAuthLoginService;
import com.cmn.cmn.service.MessageService;
import com.cmn.cmn.service.PageAuthService;

/**
 *  이 객체는 서블릿 수행 전 혹은 후에 모든 커넥션에 적용되어야 할 기능을 정의하기 위하여
 *  생성한다.<br/>
 *  역할은 다음과 같다.
 *  <ul>
 *  <li>서블릿 호출 시 사전 작업</li>
 *  <ul>
 *  <li>A. 페이지 존재 확인</li>
 *  <li>B. 페이지 권한 확인</li>
 *  <li>C. 모바일 페이지 존재 여부 확인</li>
 *  <li>D. 메서드별 호출 내용 정의 - GET메서드는 페이지 리턴, POST메서드는 JSON 형식의 응답 리턴</li>
 *  <li>E. 커넥션 로그 DB에 기록</li>
 *  </ul>
 *  <li> 서블릿 종료 시 사후 작업</li>
 *  <ul>
 *  <li> 커넥션 종료 시 종료 로그 DB에 기록</li>
 *  </ul>
 *  </ul>
 */
@Component
@EnableAsync
public class InterceptorComponent extends HandlerInterceptorAdapter {
  @Autowired
  private UserException userException;
  
  @Autowired
  private SystemException systemException;
  
  @Autowired
  private GetServerTimeService getServerTimeService;
  
  @Autowired
  private OAuthLoginService oAuthLoginService;
  
  @Autowired
  private MessageService messageService;
  
  @Autowired
  private PageAuthService pageAuthService;
  
  @Autowired
  private AddoptInfoComponent addoptInfoComponent;
  
  private static Logger logger = LogManager.getLogger(InterceptorComponent.class);
  
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
    response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
    response.setHeader("Access-Control-Max-Age", "3600");
    response.setHeader("Access-Control-Allow-Headers", "x-requested-with");
    response.addHeader("Access-Control-Allow-Origin", "*");
    request.setAttribute("system_call_dtm", getServerTimeService.getServerTime());
    String ip = null;
    Calendar calendar = new GregorianCalendar(TimeZone.getDefault());
    request.setAttribute("now_dtm", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(calendar.getTime()));
    ip = request.getHeader("X-Forwarded-For");
    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getHeader("Proxy-Client-IP");
    }
    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getHeader("WL-Proxy-Client-IP");
    }
    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getHeader("HTTP_CLIENT_IP");
    }
    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getHeader("HTTP_X_FORWARDED_FOR");
    }
    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getRemoteAddr();
    }
    long connSeq = addoptInfoComponent.newConnList(request, response, ((Long)request.getAttribute("system_call_dtm")).longValue(), (Integer)request.getSession().getAttribute("user_num"), ip, request.getRequestURL().toString(), request.getQueryString(), request.getMethod());
    request.setAttribute("_REQUEST_CONN_SEQ", connSeq);
    checkLogin(request, response);
    Map<String, String> pgmInfo = getPgmTaskPageName(request.getRequestURI(), request.getMethod());
    if ("GET".equals(request.getMethod()) == true && pgmInfo.get("pgm") != null && pgmInfo.get("pgm").equals("FileDown") == true) {
      return true;
    } else if ("favicon.ico".equals(pgmInfo.get("pgm")) == true) {
      return true;
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
          afterCompletion(request, response, null, null);
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
    logger.debug("============   Start method of InterceptorComponent.afterCompletion   ============");
    int responseNum = 0;
    String errMsg = null;
    if (ex instanceof KokodogException == true) {
      responseNum = ((KokodogException)ex).getErrTyp();
      errMsg = ((KokodogException)ex).getMessage();
    } else {
      if (ex != null) {
        responseNum = 500;
        errMsg = ex.getMessage();
      } else {
        if (request.getAttribute("_REQUEST_RESPONSE_STATUS") == null) {
          responseNum = 200;          
        } else {
          responseNum = ((Integer)request.getAttribute("_REQUEST_RESPONSE_STATUS")).intValue();
          errMsg = (String)request.getAttribute("_REQUEST_ERR_MESSGE");
        }
      }
    }
    addoptInfoComponent.endConnUpdate(request, response, getServerTimeService.getServerTime(), responseNum, errMsg, ((Long)request.getAttribute("_REQUEST_CONN_SEQ")).longValue()
                                      , ((Long)request.getAttribute("system_call_dtm")).longValue());
  }

  /**
    *  로그인 세션 처리를 Google OAuth 토큰 만료를 기준으로 판단하여 진행.
    *  @param request - 서블릿 Request
    *  @param response - 서블릿 응답이 정의된 response
    */
  private void checkLogin(HttpServletRequest request, HttpServletResponse response) throws Exception {
    logger.debug("============   Start method of InterceptorComponent.checkLogin   ============");
    Map<String, Object> outputMap = null;
    boolean isAccessTokenExist = false;
    String refreshToken = null;
    if (request.getSession().getAttribute("user_num") != null) {
      isAccessTokenExist = oAuthLoginService.getIsAccessTokenExist(((Integer)request.getSession().getAttribute("user_num")).intValue());
      if (isAccessTokenExist == false) {
        refreshToken = oAuthLoginService.getRequestTokenByUserNum(((Integer)request.getSession().getAttribute("user_num")).intValue());
        if (refreshToken != null) {
          outputMap = oAuthLoginService.getAccessTokenByRefreshToken(refreshToken);
          oAuthLoginService.insertAccessToken((String)outputMap.get("access_token"), ((Integer)outputMap.get("expires_in")).intValue(), ((Integer)request.getSession().getAttribute("user_num")).intValue());
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
    logger.debug("============   Start method of InterceptorComponent.getPgmTaskPageName   ============");
    logger.debug(" Parameter - uri[" + uri + "], method[" + method + "]");
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
    logger.debug(" return - returnMap[" + returnMap + "]");
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
    ret = pageAuthService.getIsPageExist(pgm, task, page);
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
    ret = pageAuthService.getIsLoginAuth(pgm, task, page, userNum);
    return ret;
  }
  
  private String returnView(HttpServletRequest request, Map<String, String> pgmInfo) throws Exception {
    String userAgent = null;
    boolean isMobile = false;
    String returnUrl = null;
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
      if (pageAuthService.getIsMobilePageExist(pgmInfo.get("pgm"), pgmInfo.get("task"), pgmInfo.get("page")) == true) {
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