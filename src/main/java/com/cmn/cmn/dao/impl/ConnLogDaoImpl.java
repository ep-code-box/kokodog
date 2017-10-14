package com.cmn.cmn.dao.impl;

import java.util.Map;
import java.sql.SQLException;

import org.apache.ibatis.session.SqlSession;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import org.apache.ibatis.session.SqlSessionFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.stereotype.Repository;

import com.cmn.cmn.dao.ConnLogDao;

@Repository("connLogDao")
public class ConnLogDaoImpl implements ConnLogDao {
  @Autowired
  private SqlSessionFactory sqlSessionFactory;
  
  private SqlSession sqlSession = null;
  
  private static Logger logger = LogManager.getLogger(ConnLogDaoImpl.class);

  public void insertConnLog(Map<String, Object> inputMap) throws SQLException {
    logger.debug("============   Start method of ConnLogDaoImpl.insertConnLog   ============");
    logger.debug(" Parameter - inputMap[" + inputMap + "]");
    if (sqlSession != null && sqlSession.getConnection() != null && sqlSession.getConnection().isClosed() == false) {
      try {
        sqlSession.insert("com.cmn.cmn.insertConnLog", inputMap);
      } catch (DataAccessResourceFailureException e) {
        sqlSession.close();
        sqlSession = sqlSessionFactory.openSession();
        sqlSession.insert("com.cmn.cmn.insertConnLog", inputMap);
      }
    } else {
      if (sqlSession != null) {
        sqlSession.close();
      }
      sqlSession = sqlSessionFactory.openSession();
      sqlSession.insert("com.cmn.cmn.insertConnLog", inputMap);
    }
  }
  
  public Map<String, Object> getLastSeqConnLog(Map<String, Object> inputMap) throws SQLException {
    logger.debug("============   Start method of ConnLogDaoImpl.getLastSeqConnLog   ============");
    logger.debug(" Parameter - inputMap[" + inputMap + "]");    
    Map<String, Object> outputMap = null;
    if (sqlSession != null && sqlSession.getConnection() != null && sqlSession.getConnection().isClosed() == false) {
      try {
        outputMap = sqlSession.selectOne("com.cmn.cmn.getLastSeqConnLog", inputMap);
      } catch (DataAccessResourceFailureException e) {
        sqlSession.close();
        sqlSession = sqlSessionFactory.openSession();
        outputMap = sqlSession.selectOne("com.cmn.cmn.getLastSeqConnLog", inputMap);
      }
    } else {
      if (sqlSession != null) {
        sqlSession.close();
      }
      sqlSession = sqlSessionFactory.openSession();
      outputMap = sqlSession.selectOne("com.cmn.cmn.getLastSeqConnLog", inputMap);
    }
    logger.debug(" return - outputMap[" + outputMap + "]");
    return outputMap;
  }
  
  public void updateConnEndLog(Map<String, Object> inputMap) throws SQLException {
    logger.debug("============   Start method of ConnLogDaoImpl.updateConnEndLog   ============");
    logger.debug(" Parameter - inputMap[" + inputMap + "]");
    if (sqlSession != null && sqlSession.getConnection() != null && sqlSession.getConnection().isClosed() == false) {
      try {
        sqlSession.update("com.cmn.cmn.updateConnEndLog", inputMap);
      } catch (DataAccessResourceFailureException e) {
        sqlSession.close();
        sqlSession = sqlSessionFactory.openSession();
        sqlSession.update("com.cmn.cmn.updateConnEndLog", inputMap);
      }
    } else {
      if (sqlSession != null) {
        sqlSession.close();
      }
      sqlSession = sqlSessionFactory.openSession();
      sqlSession.update("com.cmn.cmn.updateConnEndLog", inputMap);
    }
  }
}