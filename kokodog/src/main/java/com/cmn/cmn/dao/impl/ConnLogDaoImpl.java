package com.cmn.cmn.dao.impl;

import java.util.List;
import java.util.Map;
import java.sql.SQLException;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.cmn.cmn.dao.ConnLogDao;

@Repository("connLogDao")
public class ConnLogDaoImpl implements ConnLogDao {
  @Autowired
  private SqlSession sqlSession;
  
  private static Logger logger = Logger.getLogger(ConnLogDaoImpl.class);

  public void insertConnLog(Map<String, Object> inputMap) throws SQLException {
    logger.debug("============   Start method of ConnLogDaoImpl.insertConnLog   ============");
    logger.debug(" Parameter - inputMap[" + inputMap + "]");
    sqlSession.insert("com.cmn.cmn.insertConnLog", inputMap);
  }
  
  public Map<String, Object> getLastSeqConnLog(Map<String, Object> inputMap) throws SQLException {
    logger.debug("============   Start method of ConnLogDaoImpl.getLastSeqConnLog   ============");
    logger.debug(" Parameter - inputMap[" + inputMap + "]");    
    Map<String, Object> outputMap = null;
    outputMap = sqlSession.selectOne("com.cmn.cmn.getLastSeqConnLog", inputMap);
    logger.debug(" return - outputMap[" + outputMap + "]");
    return outputMap;
  }
  
  public void updateConnEndLog(Map<String, Object> inputMap) throws SQLException {
    logger.debug("============   Start method of ConnLogDaoImpl.updateConnEndLog   ============");
    logger.debug(" Parameter - inputMap[" + inputMap + "]");    
    sqlSession.insert("com.cmn.cmn.updateConnEndLog", inputMap);
  }
}