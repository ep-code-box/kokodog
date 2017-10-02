package com.opr.inf.main.dao.impl;

import java.util.Map;
import java.util.List;
import java.sql.SQLException;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import org.apache.ibatis.session.SqlSession;

import com.cmn.err.SystemException;
import com.cmn.err.UserException;
import com.opr.inf.main.dao.GetSystemInfoDataDao;

@Repository("getSystemInfoDataDao")
public class GetSystemInfoDataDaoImpl implements GetSystemInfoDataDao {
  @Autowired
  private SqlSession sqlSession;
  
  @Autowired
  private SystemException systemException;
  
  @Autowired
  private UserException userException;
  
  private static Logger logger = LogManager.getLogger(GetSystemInfoDataDaoImpl.class);
  
  public List<Map<String, Object>> getMemoryUsed(Map<String, Object> inputMap) throws SQLException {
    logger.debug("============   Start method of GetSystemInfoDataDaoImpl.getMemoryUsed   ============");
    logger.debug(" Parameter - inputMap[" + inputMap + "]");
    List<Map<String, Object>> outputList = null;
    outputList = sqlSession.selectList("com.opr.inf.main.getMemoryUsed", inputMap);
    logger.debug(" return - outputList[" + outputList + "]");
    return outputList;
  }

  public List<Map<String, Object>> getCpuUsed(Map<String, Object> inputMap) throws SQLException {
    logger.debug("============   Start method of GetSystemInfoDataDaoImpl.getCpuUsed   ============");
    logger.debug(" Parameter - inputMap[" + inputMap + "]");
    List<Map<String, Object>> outputList = null;
    outputList = sqlSession.selectList("com.opr.inf.main.getCpuUsed", inputMap);
    logger.debug(" return - outputList[" + outputList + "]");
    return outputList;
  }
}