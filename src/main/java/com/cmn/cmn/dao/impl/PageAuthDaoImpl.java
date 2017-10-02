package com.cmn.cmn.dao.impl;

import java.util.Map;
import java.sql.SQLException;

import org.apache.ibatis.session.SqlSession;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.cmn.cmn.dao.PageAuthDao;

@Repository("PpgeAuthDao")
public class PageAuthDaoImpl implements PageAuthDao {
  @Autowired
  private SqlSession sqlSession;
  
  private static Logger logger = LogManager.getLogger(PageAuthDaoImpl.class);

  public Map<String, Object> getIsLoginAuth(Map<String, Object> inputMap) throws SQLException {
    logger.debug("============   Start method of PageAuthDaoImpl.getIsLoginAuth   ============");
    logger.debug(" Parameter - inputMap[" + inputMap + "]");
    Map<String, Object> outputMap = null;
    outputMap = sqlSession.selectOne("com.cmn.cmn.getIsLoginAuth", inputMap);
    logger.debug(" return - outputMap[" + outputMap + "]");
    return outputMap;
  }
  
  public Map<String, Object> getIsMobilePageExist(Map<String, Object> inputMap) throws SQLException {
    logger.debug("============   Start method of PageAuthDaoImpl.getIsMobilePageExist   ============");
    logger.debug(" Parameter - inputMap[" + inputMap + "]");
    Map<String, Object> outputMap = null;
    outputMap = sqlSession.selectOne("com.cmn.cmn.getIsMobilePageExist", inputMap);
    logger.debug(" return - outputMap[" + outputMap + "]");
    return outputMap;
  }

  public Map<String, Object> getIsPageExist(Map<String, Object> inputMap) throws SQLException {
    logger.debug("============   Start method of PageAuthDaoImpl.getIsPageExist   ============");
    logger.debug(" Parameter - inputMap[" + inputMap + "]");
    Map<String, Object> outputMap = null;
    outputMap = sqlSession.selectOne("com.cmn.cmn.getIsPageExist", inputMap);
    logger.debug(" return - outputMap[" + outputMap + "]");
    return outputMap;
  }
}