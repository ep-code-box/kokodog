package com.opr.inf.main.dao.impl;

import java.util.Map;
import java.util.List;
import java.sql.SQLException;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import org.apache.ibatis.session.SqlSession;

import com.opr.inf.main.dao.GetBatchExeHstDao;

@Repository("getBatchExeHstDao")
public class GetBatchExeHstDaoImpl implements GetBatchExeHstDao {
  @Autowired
  private SqlSession sqlSession;
  
  private static Logger logger = LogManager.getLogger(GetBatchExeHstDaoImpl.class);
  
  public List<Map<String, Object>> getBatchExecList(Map<String, Object> inputMap) throws SQLException {
    logger.debug("============   Start method of GetBatchExeHstDaoImpl.getBatchExecList   ============");
    logger.debug(" Parameter - inputMap[" + inputMap + "]");
    List<Map<String, Object>> outputList = null;
    outputList = sqlSession.selectList("com.opr.inf.main.getBatchExecList", inputMap);
    logger.debug(" return - outputList[" + outputList + "]");
    return outputList;
  }
}