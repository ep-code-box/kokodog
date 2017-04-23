package com.cmn.cmn.service;

import javax.servlet.http.HttpServletRequest;

public interface GetRequestURLService {
  public String getRequestURL(HttpServletRequest request) throws Exception;
}