package com.cmn.cmn.dao.impl;

import java.util.List;
import java.util.Map;
import java.sql.SQLException;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import org.apache.ibatis.session.SqlSession;

import com.cmn.cmn.dao.BatchExeManageDao;

@Repository("batchExeManageDao")
public class BatchExeManageDaoImpl implements BatchExeManageDao {
  @Autowired
  private SqlSession sqlSession;

  private static Logger logger = LogManager.getLogger(BatchExeManageDaoImpl.class);

  public void updateAllBatchExeError(Map<String, Object> inputMap) throws SQLException {
    logger.debug("============   Start method of BatchExeManageDaoImpl.updateAllBatchExeError   ============");
    logger.debug(" Parameter - inputMap[" + inputMap + "]");
    sqlSession.update("com.cmn.cmn.updateAllBatchExeError", inputMap);
  }
  
  public int insertBatchLockDateTime(Map<String, Object> inputMap) throws SQLException {
    logger.debug("============   Start method of BatchExeManageDaoImpl.insertBatchLockDateTime   ============");
    logger.debug(" Parameter - inputMap[" + inputMap + "]");
    int returnNum = sqlSession.insert("com.cmn.cmn.insertBatchLockDateTime", inputMap);
    logger.debug(" return[" + returnNum + "]");
    return returnNum;
  }
  
  public int updateBatchLockDateTimeToY(Map<String, Object> inputMap) throws SQLException {
    logger.debug("============   Start method of BatchExeManageDaoImpl.updateBatchLockDateTimeToY   ============");
    logger.debug(" Parameter - inputMap[" + inputMap + "]");
    int returnNum = sqlSession.update("com.cmn.cmn.updateBatchLockDateTimeToY", inputMap);
    logger.debug(" return[" + returnNum + "]");
    return returnNum;
  }
  public Map<String, Object> getIsExistPreBatchProcess(Map<String, Object> inputMap) throws SQLException {
    logger.debug("============   Start method of BatchExeManageDaoImpl.getIsExistPreBatchProcess   ============");
    logger.debug(" Parameter - inputMap[" + inputMap + "]");
    Map<String, Object> outputMap = sqlSession.selectOne("com.cmn.cmn.getIsExistPreBatchProcess", inputMap);
    logger.debug(" return[" + outputMap + "]");
    return outputMap;
  }
  public int insertBatchProcessHstWithoutLast(Map<String, Object> inputMap) throws SQLException {
    logger.debug("============   Start method of BatchExeManageDaoImpl.insertBatchProcessHstWithoutLast   ============");
    logger.debug(" Parameter - inputMap[" + inputMap + "]");
    int returnNum = sqlSession.insert("com.cmn.cmn.insertBatchProcessHstWithoutLast", inputMap);
    logger.debug(" return[" + returnNum + "]");
    return returnNum;
  }
  
  public int insertBatchProcessHst(Map<String, Object> inputMap) throws SQLException {
    logger.debug("============   Start method of BatchExeManageDaoImpl.insertBatchProcessHst   ============");
    logger.debug(" Parameter - inputMap[" + inputMap + "]");
    int returnNum = sqlSession.insert("com.cmn.cmn.insertBatchProcessHst", inputMap);
    logger.debug(" return[" + returnNum + "]");
    return returnNum;
  }
  
  public Map<String, Object> getCurrentBatchProcessInfo(Map<String, Object> inputMap) throws SQLException {
    logger.debug("============   Start method of BatchExeManageDaoImpl.getCurrentBatchProcessInfo   ============");
    logger.debug(" Parameter - inputMap[" + inputMap + "]");
    Map<String, Object> outputMap = sqlSession.selectOne("com.cmn.cmn.getCurrentBatchProcessInfo", inputMap);
    logger.debug(" return[" + outputMap + "]");
    return outputMap;
  }
  
  public List<Map<String, Object>> getBatchNoExeList() throws SQLException {
    logger.debug("============   Start method of BatchExeManageDaoImpl.getBatchNoExeList   ============");
    List<Map<String, Object>> outputList = sqlSession.selectList("com.cmn.cmn.getBatchNoExeList");
    logger.debug(" return[" + outputList + "]");
    return outputList;
  }
  
  public int updateBatchLockDateTimeToN(Map<String, Object> inputMap) throws SQLException {
    logger.debug("============   Start method of BatchExeManageDaoImpl.updateBatchLockDateTimeToN   ============");
    logger.debug(" Parameter - inputMap[" + inputMap + "]");
    int returnNum = sqlSession.update("com.cmn.cmn.updateBatchLockDateTimeToN", inputMap);
    logger.debug(" return[" + returnNum + "]");
    return returnNum;
  }

  public Map<String, Object> getLastBatchLockInfo() throws SQLException {
    logger.debug("============   Start method of BatchExeManageDaoImpl.getLastBatchLockInfo   ============");
    Map<String, Object> outputMap = sqlSession.selectOne("com.cmn.cmn.getLastBatchLockInfo");
    logger.debug(" return[" + outputMap + "]");
    return outputMap;
  }

  public int updateBatchLockDateTimeToYWithY(Map<String, Object> inputMap) throws SQLException {
    logger.debug("============   Start method of BatchExeManageDaoImpl.updateBatchLockDateTimeToYWithY   ============");
    logger.debug(" Parameter - inputMap[" + inputMap + "]");
    int returnNum = sqlSession.update("com.cmn.cmn.updateBatchLockDateTimeToYWithY", inputMap);
    logger.debug(" return[" + returnNum + "]");
    return returnNum;
  }
}