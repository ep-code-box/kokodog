package com.cmn.cmn.dao.impl;

import java.util.List;
import java.util.Map;
import java.sql.SQLException;

import org.apache.ibatis.session.SqlSession;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.cmn.cmn.dao.OAuthLoginDao;

@Repository("oAuthLoginDaoImpl")
public class OAuthLoginDaoImpl implements OAuthLoginDao {
  @Autowired
  private SqlSession sqlSession;
  
  private static Logger logger = LogManager.getLogger(OAuthLoginDaoImpl.class);
  
  public List<Map<String, Object>> getGoogleLoginOAuthTokenParameter() throws SQLException {
    logger.debug("============   Start method of GetGoogleLoginDaoImpl.getGoogleLoginOAuthTokenParameter   ============");
    List<Map<String, Object>> outputList = sqlSession.selectList("com.cmn.cmn.getGoogleLoginOAuthTokenParameter");
    logger.debug(" return - outputList[" + outputList + "]");
    return outputList;
  }
  
  public Map<String, Object> getUserNumByOAuthLoginId(Map<String, Object> inputMap) throws SQLException {
    logger.debug("============   Start method of GetGoogleLoginDaoImpl.getUserNumByOAuthLoginId   ============");    
    logger.debug(" Parameter - inputMap[" + inputMap + "]");
    Map<String, Object> outputMap = sqlSession.selectOne("com.cmn.cmn.getUserNumByOAuthLoginId", inputMap);
    logger.debug(" return - outputMap[" + outputMap + "]");
    return outputMap;
  }
  
  public void insertUser(Map<String, Object> inputMap) throws SQLException {
    logger.debug("============   Start method of GetGoogleLoginDaoImpl.insertUser   ============");    
    logger.debug(" Parameter - inputMap[" + inputMap + "]");
    sqlSession.insert("com.cmn.cmn.insertUser", inputMap);
  }
  
  public void insertAllInitAuth(Map<String, Object> inputMap) throws SQLException {
    logger.debug("============   Start method of GetGoogleLoginDaoImpl.insertAllInitAuth   ============");    
    logger.debug(" Parameter - inputMap[" + inputMap + "]");
    sqlSession.insert("com.cmn.cmn.insertAllInitAuth", inputMap);
  }

  public void insertAccessToken(Map<String, Object> inputMap) throws SQLException {
    logger.debug("============   Start method of GetGoogleLoginDaoImpl.insertAccessToken   ============");    
    logger.debug(" Parameter - inputMap[" + inputMap + "]");
    sqlSession.insert("com.cmn.cmn.insertAccessToken", inputMap);
  }

  public void updatePrevRefreshTokenAsDelete(Map<String, Object> inputMap) throws SQLException {
    logger.debug("============   Start method of GetGoogleLoginDaoImpl.updatePrevRefreshTokenAsDelete   ============");    
    logger.debug(" Parameter - inputMap[" + inputMap + "]");
    sqlSession.update("com.cmn.cmn.updatePrevRefreshTokenAsDelete", inputMap);
  }
  
  public void insertRefreshToken(Map<String, Object> inputMap) throws SQLException {
    logger.debug("============   Start method of GetGoogleLoginDaoImpl.insertRefreshToken   ============");    
    logger.debug(" Parameter - inputMap[" + inputMap + "]");
    sqlSession.insert("com.cmn.cmn.insertRefreshToken", inputMap);
  }

  public Map<String, Object> getAccessTokenByUserNum(Map<String, Object> inputMap) throws SQLException {
    logger.debug("============   Start method of GetGoogleLoginDaoImpl.getAccessTokenByUserNum   ============");    
    logger.debug(" Parameter - inputMap[" + inputMap + "]");
    Map<String, Object> outputMap = sqlSession.selectOne("com.cmn.cmn.getAccessTokenByUserNum", inputMap);
    logger.debug(" return - outputMap[" + outputMap + "]");
    return outputMap;
  }

  public void updateAccessTokenAsDelete(Map<String, Object> inputMap) throws SQLException {
    logger.debug("============   Start method of GetGoogleLoginDaoImpl.updateAccessTokenAsDelete   ============");    
    logger.debug(" Parameter - inputMap[" + inputMap + "]");
    sqlSession.update("com.cmn.cmn.updateAccessTokenAsDelete", inputMap);
  }
  
  public List<Map<String, Object>> getGoogleLoginOAuthTokenParameterByRefreshToken() throws SQLException {
    logger.debug("============   Start method of GetGoogleLoginDaoImpl.getGoogleLoginOAuthTokenParameterByRefreshToken   ============");    
    List<Map<String, Object>> outputList = sqlSession.selectList("com.cmn.cmn.getGoogleLoginOAuthTokenParameterByRefreshToken");    
    logger.debug(" return - outputList[" + outputList + "]");
    return outputList;
  }
  
  public Map<String, Object> getIsAccessTokenExist(Map<String, Object> inputMap) throws SQLException {
    logger.debug("============   Start method of GetGoogleLoginDaoImpl.getIsAccessTokenExist   ============");    
    logger.debug(" Parameter - inputMap[" + inputMap + "]");
    Map<String, Object> outputMap = null;
    outputMap = sqlSession.selectOne("com.cmn.cmn.getIsAccessTokenExist", inputMap);    
    logger.debug(" return - outputMap[" + outputMap + "]");
    return outputMap;
  }
  
  public Map<String, Object> getRequestTokenByUserNum(Map<String, Object> inputMap) throws SQLException {
    logger.debug("============   Start method of GetGoogleLoginDaoImpl.getRequestTokenByUserNum   ============");    
    logger.debug(" Parameter - inputMap[" + inputMap + "]");
    Map<String, Object> outputMap = null;
    outputMap = sqlSession.selectOne("com.cmn.cmn.getRequestTokenByUserNum", inputMap);    
    logger.debug(" return - outputMap[" + outputMap + "]");
    return outputMap;    
  }
}