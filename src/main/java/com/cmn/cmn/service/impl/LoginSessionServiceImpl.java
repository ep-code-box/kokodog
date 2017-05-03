package com.cmn.cmn.service;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmn.cmn.service.LoginSessionService;
import com.cmn.cmn.service.OAuthLoginService;

@Service("loginSessionService")
public class LoginSessionServiceImpl implements LoginSessionService {
  @Autowired
  private OAuthLoginService oAuthLoginService;
  
  public void loginSessionLogin(HttpServletRequest request, int userNum) throws Exception {
    request.getSession().setAttribute("user_num", new Integer(userNum));
  }
  
  public void loginSessionLogout(HttpServletRequest request) throws Exception {
    if (request.getSession().getAttribute("user_num") != null) {
      oAuthLoginService.deleteOAuthInfo(((Integer)request.getSession().getAttribute("user_num")).intValue(), (String)request.getSession().getAttribute("now_dtm"));
      request.getSession().setAttribute("user_num", null);
    }    
  }
}