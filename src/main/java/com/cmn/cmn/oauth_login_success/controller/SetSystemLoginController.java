package com.cmn.cmn.oauth_login_success.controller;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.cmn.cmn.service.OAuthLoginService;
import com.cmn.cmn.service.GetRequestURLService;
import com.cmn.cmn.service.LoginSessionService;

@Controller
public class SetSystemLoginController {
  @Autowired
  private OAuthLoginService oAuthLoginService;
  
  @Autowired
  private GetRequestURLService getRequestURLService;
  
  @Autowired
  private LoginSessionService loginSessionService;
  
  private static Logger logger = LogManager.getLogger(SetSystemLoginController.class);

  @RequestMapping(value="/cmn/cmn/oauth_login_success", method=RequestMethod.GET)
  public ModelAndView oauthLoginSuccess(HttpServletRequest request, HttpServletResponse response) throws Exception {
    logger.debug("============   Start method of SetSystemLoginController.oauthLoginSuccess   ============");
    validation(request, response);
    Map<String, Object> outputMap = null;
    String requestUrl = null;
    String id = null;
    int userNum = 0;
    ModelAndView mav = null;
    requestUrl = getRequestURLService.getRequestURL(request);
    outputMap = oAuthLoginService.getAccessTokenByCode(request.getParameter("code"), requestUrl);
    id = oAuthLoginService.getOAuthIdByAccessToken((String)(outputMap.get("access_token")));
    userNum = oAuthLoginService.getUserNumByOAuthId(id);
    oAuthLoginService.insertToken((String)outputMap.get("access_token"), outputMap.get("refresh_token") != null ? (String)outputMap.get("refresh_token") : null, ((Integer)outputMap.get("expires_in")).intValue(), userNum);
    loginSessionService.loginSessionLogin(request, userNum);
    mav = new ModelAndView("redirect:" + request.getParameter("state"));
    return mav;
  }
  
  private void validation(HttpServletRequest request, HttpServletResponse response) throws Exception {
    return;
  }
}