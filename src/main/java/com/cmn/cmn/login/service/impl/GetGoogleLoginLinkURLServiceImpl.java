/*
 * Title : GetGoogleLoginLinkURLServiceImpl
 *
 * @Version : 1.0
 *
 * @Date : 2016-03-08
 *
 * @Copyright by 이민석
 */

package com.cmn.cmn.login.service.impl;

import java.util.Map;
import java.util.List;
import java.net.URLEncoder;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmn.cmn.login.service.GetGoogleLoginLinkURLService;
import com.cmn.cmn.service.GetGoogleLoginOAuthRedirectUriService;
import com.cmn.cmn.login.dao.GetGoogleLoginLinkURLDao;

/**
 *  이 객체는 Google OAuth 2.0을 이용한 로그인 시도를 진행할 때
 *  Google 서버로부터 코드 등의 각종 정보를 가져오기 위한 파라미터를
 *  포함한 Link를 가져오기 위하여 호출한다.
 */
@Service("getGoogleLoginLinkURLService")
public class GetGoogleLoginLinkURLServiceImpl implements GetGoogleLoginLinkURLService {
  @Autowired
  private GetGoogleLoginLinkURLDao getGoogleLoginLinkURLDao;
  
  @Autowired
  private GetGoogleLoginOAuthRedirectUriService getGoogleLoginOAuthRedirectUriService;

  private static Logger logger = LogManager.getLogger(GetGoogleLoginLinkURLServiceImpl.class);

  public String getGoogleLoginLinkURL(String scheme, String serverName, int serverPort, String redirectUrlParam) throws Exception {
    logger.debug("============   Start method of GetGoogleLoginLinkURLServiceImpl.getGoogleLoginLinkURL   ============");
    List<Map<String, Object>> outputList = getGoogleLoginLinkURLDao.getGoogleLoginOAuthParameter();
    int i = 0;
    String parameter = "";
    for (i = 0; i < outputList.size(); i++) {
      parameter = parameter + (URLEncoder.encode((String)(outputList.get(i).get("oauth_login_key")), "UTF-8") + "=" + URLEncoder.encode((String)(outputList.get(i).get("oauth_login_value")), "UTF-8"));
      parameter = parameter + "&";
    }
    parameter = parameter + "state=" + URLEncoder.encode(redirectUrlParam, "UTF-8") + "&";
    String redirectUrl = scheme + "://" + serverName;
    if (("http".equals(scheme) == false || serverPort != 80) && ("https".equals(scheme) == false || serverPort != 443)) {
      redirectUrl = redirectUrl + ":" + serverPort;
    }
    redirectUrl = redirectUrl + "/" + getGoogleLoginOAuthRedirectUriService.getGoogleLoginOAuthRedirectUri();
    parameter = parameter + "redirect_uri=" + URLEncoder.encode(redirectUrl, "UTF-8");
    return parameter;
  }
}