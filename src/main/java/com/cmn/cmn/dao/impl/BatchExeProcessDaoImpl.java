/*
 * Title : BatchExeProcessDao
 *
 * @Version : 1.0
 *
 * @Date : 2017-04-18
 *
 * @Copyright by 이민석
 */

package com.cmn.cmn.dao;

import java.util.Map;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import org.apache.ibatis.session.SqlSession;

import com.cmn.cmn.dao.BatchExeProcessDao;

/**
  *  이 객체는 Batch를 수행하는 class에서 배치 리포트를 저장하는 역할을 수행하는
  *  Dao들로 정의된다.
  */
@Repository("batchExeProcessDao")
public class BatchExeProcessDaoImpl implements BatchExeProcessDao {
  @Autowired
  private SqlSession sqlSession;
  
  private static Logger logger = Logger.getLogger(BatchExeProcessDaoImpl.class);

  public void updateErrReport(Map<String, Object> inputMap) throws SQLException {
    logger.debug("============   Start method of BatchExeProcessDaoImpl.updateErrReport   ============");
    logger.debug(" Parameter - inputMap[" + inputMap + "]");
    sqlSession.update("com.cmn.cmn.updateErrReport", inputMap);
  }
  
  public void updateBatchFinishResult(Map<String, Object> inputMap) throws SQLException {
    logger.debug("============   Start method of BatchExeProcessDaoImpl.updateBatchFinishResult   ============");
    logger.debug(" Parameter - inputMap[" + inputMap + "]");
    sqlSession.update("com.cmn.cmn.updateBatchFinishResult", inputMap);
  }
  
  public void insertBatchExeProcess(Map<String, Object> inputMap) throws SQLException {
    logger.debug("============   Start method of BatchExeProcessDaoImpl.insertBatchExeProcess   ============");
    logger.debug(" Parameter - inputMap[" + inputMap + "]");
    sqlSession.update("com.cmn.cmn.insertBatchExeProcess", inputMap);
  }
}