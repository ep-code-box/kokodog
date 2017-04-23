/*
 * Title : GetGoogleLoginLinkURL
 *
 * @Version : 1.0
 *
 * @Date : 2016-03-08
 *
 * @Copyright by 이민석
 */

package com.cmn.cmn.login.controller;

import java.util.Map;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cmn.err.SystemException;
import com.cmn.cmn.login.service.GetGoogleLoginLinkURLService;

/**
 *  이 객체는 Google OAuth 2.0을 이용한 로그인 시도를 진행할 때
 *  Google 서버로부터 코드 등의 각종 정보를 가져오기 위한 파라미터를
 *  포함한 Link를 가져오기 위하여 호출한다.
 */
@Controller
public class GetGoogleLoginLinkURLController {
  @Autowired
  private GetGoogleLoginLinkURLService getGoogleLoginLinkURLService;
  
  @Autowired
  private SystemException systemException;
  
  private static Logger logger = Logger.getLogger(GetGoogleLoginLinkURLController.class);

  /**
    *  Google OAuth 로그인을 위하여 링크 정보를 가져오는 method
    *  @param request - 서블릿 Request    
    *  @param response - 서블릿 응답이 정의된 response
    *  @return - 링크 정보를 포함한 모델   
    */
  @RequestMapping(value="/cmn/cmn/login/GetGoogleLoginLinkURL", method=RequestMethod.POST)
  @ResponseBody
  public Map getGoogleLoginLinkURL(HttpServletRequest request, HttpServletResponse response) throws Exception {
    validation(request, response);
    Map<String, Object> returnMap = new HashMap<String, Object>();
    String parameter = getGoogleLoginLinkURLService.getGoogleLoginLinkURL(request.getScheme(), request.getServerName(), request.getServerPort(), request.getParameter("redirect_url"));
    returnMap.put("url", "https://accounts.google.com/o/oauth2/v2/auth?" + parameter);
    return returnMap;
  }
  
  /**
    *  Input parameter 등의 유효성 체크
    *  @param request - 서블릿 Request    
    *  @param response - 서블릿 응답이 정의된 response
    *  @return - 없음  
    */
  private void validation(HttpServletRequest request, HttpServletResponse response) throws Exception {
    if (request.getParameter("redirect_url") == null) {
      throw systemException.systemException(3, "redirect_url");
    }
  }
}