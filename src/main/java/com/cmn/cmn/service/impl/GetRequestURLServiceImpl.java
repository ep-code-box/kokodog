package com.cmn.cmn.service.impl;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import org.apache.log4j.Logger;

import com.cmn.cmn.service.GetRequestURLService;

@Service("getRequestURLService")
public class GetRequestURLServiceImpl implements GetRequestURLService {
  private static Logger logger = Logger.getLogger(GetRequestURLServiceImpl.class);
  
  public String getRequestURL(HttpServletRequest request) throws Exception {
    logger.debug("============   Start method of GetRequestURLServiceImpl.getRequestURL   ============");
    String requestUrl = request.getScheme() + "://" + request.getServerName();
    if ((request.getScheme().equals("http") == false || request.getServerPort() != 80) && (request.getScheme().equals("https") == false || request.getServerPort() != 443)) {
      requestUrl = requestUrl + ":" + request.getServerPort();
    }
    logger.debug(" return - requestUrl[" + requestUrl + "]");
    return requestUrl;
  }
}