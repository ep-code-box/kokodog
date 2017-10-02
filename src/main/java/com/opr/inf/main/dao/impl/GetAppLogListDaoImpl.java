package com.opr.inf.main.dao.impl;

import java.util.Map;
import java.util.List;
import java.sql.SQLException;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import org.apache.ibatis.session.SqlSession;

import com.opr.inf.main.dao.GetAppLogListDao;

@Repository("getAppLogListDao")
public class GetAppLogListDaoImpl implements GetAppLogListDao {
  @Autowired
  private SqlSession sqlSession;
  
  private static Logger logger = LogManager.getLogger(GetAppLogListDaoImpl.class);
  
  public List<Map<String, Object>> getAppLogList(Map<String, Object> inputMap) throws SQLException {
    logger.debug("============   Start method of GetAppLogListService.getAppLogList   ============");
    logger.debug(" Parameter - inputMap[" + inputMap + "]");
    List<Map<String, Object>> outputList = null;
    outputList = sqlSession.selectList("com.opr.inf.main.getAppLogList", inputMap);
    logger.debug(" return display skipped");
    return outputList;
  }
  
  public Map<String, Object> getAppLogListCnt(Map<String, Object> inputMap) throws SQLException {
    logger.debug("============   Start method of GetAppLogListService.getAppLogListCnt   ============");
    logger.debug(" Parameter - inputMap[" + inputMap + "]");
    Map<String, Object> outputMap = null;
    outputMap = sqlSession.selectOne("com.opr.inf.main.getAppLogListCnt", inputMap);
    logger.debug(" return - outputMap[" + outputMap + "]");
    return outputMap;    
  }
}