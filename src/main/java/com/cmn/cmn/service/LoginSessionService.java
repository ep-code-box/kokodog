package com.cmn.cmn.service;

import javax.servlet.http.HttpServletRequest;

public interface LoginSessionService {
  public void loginSessionLogin(HttpServletRequest request, int userNum) throws Exception;
  public void loginSessionLogout(HttpServletRequest request) throws Exception;
}