package com.cmn.cmn.service.impl;

import java.util.List;
import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmn.cmn.service.GetDataFromURLService;
import com.cmn.cmn.service.GetGoogleLoginOAuthRedirectUriService;
import com.cmn.cmn.service.OAuthLoginService;
import com.cmn.cmn.dao.OAuthLoginDao;

@Service("oAuthGoogleLoginService")
public class OAuthLoginServiceImpl implements OAuthLoginService {
  @Autowired
  private GetDataFromURLService getDataFromURLService;
  
  @Autowired
  private OAuthLoginDao oAuthLoginDao;
  
  @Autowired
  private GetGoogleLoginOAuthRedirectUriService getGoogleLoginOAuthRedirectUriService;
  
  private static Logger logger = Logger.getLogger(OAuthLoginServiceImpl.class);
  
  public Map<String, Object> getAccessTokenByCode(String code, String requestUrl) throws Exception {
    List<Map<String, Object>> outputList = oAuthLoginDao.getGoogleLoginOAuthTokenParameter();
    List<Map<String, String>> inputList = new LinkedList<Map<String, String>>();
    Map<String, String> inputTempMap = null;
    for (int i = 0; i < outputList.size(); i++) {
      inputTempMap = new HashMap<String, String>();
      inputTempMap.put("key", (String)(outputList.get(i).get("oauth_login_token_key")));
      inputTempMap.put("value", (String)(outputList.get(i).get("oauth_login_token_value")));
      inputList.add(inputTempMap);
    }
    inputTempMap = new HashMap<String, String>();
    String redirectUrl = requestUrl + "/";
    redirectUrl = redirectUrl + getGoogleLoginOAuthRedirectUriService.getGoogleLoginOAuthRedirectUri();
    inputTempMap.put("key", "redirect_uri");
    inputTempMap.put("value", redirectUrl);
    inputList.add(inputTempMap);
    inputTempMap = new HashMap<String, String>();
    inputTempMap.put("key", "code");
    inputTempMap.put("value", code);
    inputList.add(inputTempMap);
    JSONObject obj = (JSONObject)getDataFromURLService.getDataFromURL("https://www.googleapis.com/oauth2/v4/token", inputList, "POST", "UTF-8", GetDataFromURLService.TYPE_JSON);
    String accessToken = obj.getString("access_token");
    int expiresIn = obj.getInt("expires_in");
    String refreshToken = obj.containsKey("refresh_token") == true ? obj.getString("refresh_token") : null;
    Map<String, Object> returnMap = new HashMap<String, Object>();
    returnMap.put("access_token", accessToken);
    returnMap.put("expires_in", expiresIn);
    returnMap.put("refresh_token", refreshToken);
    return returnMap;
  }
  
  public String getOAuthIdByAccessToken(String accessToken) throws Exception {
    Map<String, String> inputTempMap = new HashMap<String, String>();
    List<Map<String, String>> inputList = new LinkedList<Map<String, String>>();
    inputTempMap.clear();
    inputTempMap.put("key", "access_token");
    inputTempMap.put("value", accessToken);
    inputList.add(inputTempMap);
    JSONObject obj = (JSONObject)getDataFromURLService.getDataFromURL("https://www.googleapis.com/plus/v1/people/me", inputList, "GET", "UTF-8", GetDataFromURLService.TYPE_JSON);
    String id = obj.getString("id");
    return id;
  }
  
  public int getUserNumByOAuthId(String id, String nowDtm) throws Exception {
    Map<String, Object> inputMap = new HashMap<String, Object>();
    inputMap.put("google_id", id);
    logger.debug("Input Map of SQL Map getUserNumByOAuthLoginId - " + inputMap);
    Map<String, Object> outputMap = oAuthLoginDao.getUserNumByOAuthLoginId(inputMap);
    logger.debug("Output Map of SQL Map getUserNumByOAuthLoginId - " + outputMap);
    boolean isNewUser = false;
    if (outputMap == null || outputMap.get("user_num") == null) {
      isNewUser = true;
      inputMap.clear();
      inputMap.put("google_id", id);
      inputMap.put("now_dtm", nowDtm);
      logger.debug("Input Map of SQL Map insertCmnUser - " + inputMap);
      oAuthLoginDao.insertUser(inputMap);
      inputMap.clear();
      inputMap.put("google_id", id);
      outputMap = oAuthLoginDao.getUserNumByOAuthLoginId(inputMap);
    }
    int userNum = ((Long)(outputMap.get("user_num"))).intValue();
    if (isNewUser == true) {
      inputMap.clear();
      inputMap.put("user_num", userNum);
      inputMap.put("now_dtm", nowDtm);
      oAuthLoginDao.insertAllInitAuth(inputMap);      
    }
    return userNum;
  }
  
  public void insertToken(String accessToken, String refreshToken, int expiresIn, String nowDtm, int userNum) throws Exception {
    insertAccessToken(accessToken, expiresIn, nowDtm, userNum);
    if (refreshToken != null) {
      insertRefreshToken(refreshToken, nowDtm, userNum);
    }
  }
  
  public void insertAccessToken(String accessToken, int expiresIn, String nowDtm, int userNum) throws Exception {
    Map<String, Object> inputMap = new HashMap<String, Object>();
    inputMap.put("user_num", userNum);
    inputMap.put("now_dtm", nowDtm);
    inputMap.put("expires_in", expiresIn);
    inputMap.put("access_token", accessToken);
    oAuthLoginDao.insertAccessToken(inputMap);    
  }

  public void insertRefreshToken(String refreshToken, String nowDtm, int userNum) throws Exception {
    Map<String, Object> inputMap = new HashMap<String, Object>();
    inputMap.put("user_num", userNum);
    inputMap.put("now_dtm", nowDtm);
    oAuthLoginDao.updatePrevRefreshTokenAsDelete(inputMap);        
    inputMap.clear();
    inputMap.put("user_num", userNum);
    inputMap.put("now_dtm", nowDtm);
    inputMap.put("refresh_token", refreshToken);
    logger.debug("Input Map of SQL Map insertRefreshToken - " + inputMap);
    oAuthLoginDao.insertRefreshToken(inputMap);        
  }
  
  public Map<String, Object> getAccessTokenByRefreshToken(String refreshToken) throws Exception {
    List<Map<String, Object>> outputList = oAuthLoginDao.getGoogleLoginOAuthTokenParameterByRefreshToken();
    List<Map<String, String>> inputList = new LinkedList<Map<String, String>>();
    Map<String, String> inputTempMap = null;
    for (int i = 0; i < outputList.size(); i++) {
      inputTempMap = new HashMap<String, String>();
      inputTempMap.put("key", (String)(outputList.get(i).get("oauth_login_token_key")));
      inputTempMap.put("value", (String)(outputList.get(i).get("oauth_login_token_value")));
      inputList.add(inputTempMap);
    }
    inputTempMap = new HashMap<String, String>();
    inputTempMap.put("key", "refresh_token");
    inputTempMap.put("value", refreshToken);
    inputList.add(inputTempMap);
    JSONObject obj = (JSONObject)getDataFromURLService.getDataFromURL("https://www.googleapis.com/oauth2/v4/token", inputList, "POST", "UTF-8", GetDataFromURLService.TYPE_JSON);
    String accessToken = obj.getString("access_token");
    int expiresIn = obj.getInt("expires_in");
    Map<String, Object> returnMap = new HashMap<String, Object>();
    returnMap.put("access_token", accessToken);
    returnMap.put("expires_in", expiresIn);
    return returnMap;    
  }
  
  public void deleteOAuthInfo(int userNum, String nowDtm) throws Exception {
    Map<String, Object> inputMap = new HashMap<String, Object>();
    inputMap.put("user_num", userNum);
    Map<String, Object> outputMap = null;
    String accessToken = null;
    outputMap = oAuthLoginDao.getAccessTokenByUserNum(inputMap);
    accessToken = (String)outputMap.get("access_token");
    inputMap.clear();
    inputMap.put("user_num", userNum);
    inputMap.put("now_dtm", nowDtm);
    oAuthLoginDao.updatePrevRefreshTokenAsDelete(inputMap);
    inputMap.clear();
    inputMap.put("user_num", userNum);
    inputMap.put("now_dtm", nowDtm);
    oAuthLoginDao.updateAccessTokenAsDelete(inputMap);
  }
  
  public void revokeToken(String accessToken) throws Exception {
    List<Map<String, String>> paramList = new LinkedList<Map<String, String>>();
    Map<String, String> inputMap = new HashMap<String, String>();
    inputMap.put("token", accessToken);
    paramList.add(inputMap);
    getDataFromURLService.getDataFromURL("https://accounts.google.com/o/oauth2/revoke", paramList, "GET", "UTF-8", GetDataFromURLService.TYPE_JSON);
  }
}
