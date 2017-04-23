/*
 * Title : GetGoogleLoginOAuthParameterServiceImpl
 *
 * @Version : 1.0
 *
 * @Date : 2016-03-08
 *
 * @Copyright by 이민석
 */

package com.cmn.cmn.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmn.cmn.service.GetGoogleLoginOAuthRedirectUriService;
import com.cmn.cmn.dao.GetGoogleLoginOAuthRedirectUriDao;

@Service("getGoogleLoginOAuthRedirectUriService")
public class GetGoogleLoginOAuthRedirectUriServiceImpl implements GetGoogleLoginOAuthRedirectUriService {
  @Autowired
  private GetGoogleLoginOAuthRedirectUriDao getGoogleLoginOAuthRedirectUriDao;
  
  public String getGoogleLoginOAuthRedirectUri() throws Exception {
    Map<String, Object> outputMap = getGoogleLoginOAuthRedirectUriDao.getGoogleLoginOAuthRedirectUri();
    String redirectUri = (String)outputMap.get("redirect_uri");
    return redirectUri;
  }
}