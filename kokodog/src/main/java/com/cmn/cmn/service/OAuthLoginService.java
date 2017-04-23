package com.cmn.cmn.service;

import java.util.Map;

public interface OAuthLoginService {
  public Map<String, Object> getAccessTokenByCode(String code, String requestUrl) throws Exception;
  public String getOAuthIdByAccessToken(String accessToken) throws Exception;
  public int getUserNumByOAuthId(String id, String nowDtm) throws Exception;
  public void insertToken(String accessToken, String refreshToken, int expiresIn, String nowDtm, int userNum) throws Exception;
  public void insertAccessToken(String accessToken, int expiresIn, String nowDtm, int userNum) throws Exception;
  public void insertRefreshToken(String refreshToken, String nowDtm, int userNum) throws Exception;
  public Map<String, Object> getAccessTokenByRefreshToken(String refreshToken) throws Exception;
  public void deleteOAuthInfo(int userNum, String nowDtm) throws Exception;
  public void revokeToken(String accessToken) throws Exception;
}
