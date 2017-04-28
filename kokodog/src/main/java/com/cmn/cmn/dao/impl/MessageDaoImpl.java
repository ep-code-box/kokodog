package com.cmn.cmn.dao.impl;

import java.util.List;
import java.util.Map;
import java.sql.SQLException;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.cmn.cmn.dao.MessageDao;

@Repository("messageDao")
public class MessageDaoImpl implements MessageDao {
  @Autowired
  private SqlSession sqlSession;
  
  private static Logger logger = Logger.getLogger(MessageDaoImpl.class);
  
  public Map<String, Object> getErrMessageByMessageNum(Map<String, Object> inputMap) throws SQLException {
    logger.debug("============   Start method of MessageDaoImpl.getErrMessageByMessageNum   ============");
    Map<String, Object> outputMap = sqlSession.selectOne("com.cmn.cmn.getErrMessageByMessageNum");
    logger.debug(" return - outputList[" + outputMap + "]");
    return outputMap;
  }
}