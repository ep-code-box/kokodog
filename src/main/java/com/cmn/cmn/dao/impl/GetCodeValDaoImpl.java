package com.cmn.cmn.dao.impl;

import java.util.List;
import java.util.Map;
import java.sql.SQLException;

import org.apache.ibatis.session.SqlSession;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.cmn.cmn.dao.GetCodeValDao;

@Repository("getCodeValDao")
public class GetCodeValDaoImpl implements GetCodeValDao {
  @Autowired
  private SqlSession sqlSession;
  
  private static Logger logger = LogManager.getLogger(GetCodeValDaoImpl.class);

  public List<Map<String, Object>> getAllCodeListValFromCodeNum(Map<String, Object> inputMap) throws SQLException {
    logger.debug("============   Start method of GetCodeValDaoImpl.getAllCodeListValFromCodeNum   ============");
    logger.debug(" Parameter - inputMap[" + inputMap + "]");
    List<Map<String, Object>> outputList = null;
    outputList = sqlSession.selectList("com.cmn.cmn.getAllCodeListValFromCodeNum", inputMap);
    logger.debug(" return - outputList[" + outputList + "]");
    return outputList;
  }
}