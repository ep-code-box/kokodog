package com.cmn.cmn.dao;

import java.util.List;
import java.util.Map;
import java.sql.SQLException;

public interface OAuthLoginDao {
  public List<Map<String, Object>> getGoogleLoginOAuthTokenParameter() throws SQLException;
  public Map<String, Object> getUserNumByOAuthLoginId(Map<String, Object> inputMap) throws SQLException;
  public void insertUser(Map<String, Object> inputMap) throws SQLException;
  public void insertAllInitAuth(Map<String, Object> inputMap) throws SQLException;
  public void insertAccessToken(Map<String, Object> inputMap) throws SQLException;
  public void updatePrevRefreshTokenAsDelete(Map<String, Object> inputMap) throws SQLException;
  public void insertRefreshToken(Map<String, Object> inputMap) throws SQLException;
  public Map<String, Object> getAccessTokenByUserNum(Map<String, Object> inputMap) throws SQLException;
  public void updateAccessTokenAsDelete(Map<String, Object> inputMap) throws SQLException;
  public List<Map<String, Object>> getGoogleLoginOAuthTokenParameterByRefreshToken() throws SQLException;
  public Map<String, Object> getIsAccessTokenExist(Map<String, Object> inputMap) throws SQLException;
  public Map<String, Object> getRequestTokenByUserNum(Map<String, Object> inputMap) throws SQLException;
}