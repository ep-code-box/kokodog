package com.skd.sel.sel_scnrio_mng.dao.impl;

import java.util.List;
import java.util.Map;
import java.sql.SQLException;

import org.apache.ibatis.session.SqlSession;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.skd.sel.sel_scnrio_mng.dao.UpdateScnrioSrcCdDao;

@Repository("updateScnrioSrcCdDao")
public class UpdateScnrioSrcCdDaoImpl implements UpdateScnrioSrcCdDao {
  private static Logger logger = LogManager.getLogger(UpdateScnrioSrcCdDaoImpl.class);

  @Autowired
  private SqlSession sqlSession;

  public Map<String, Object> getTestInputInfoByScnrioNumAndInputNm(Map<String, Object> inputMap) throws SQLException {
    logger.debug("============   Start method of UpdateScnrioSrcCdDaoImpl.getTestInputInfoByScnrioNumAndInputNm   ============");
    logger.debug("Parameter inputMap[" + inputMap.toString() + "]");
    Map<String, Object> outputMap = null;
    outputMap = sqlSession.selectOne("com.skd.sel.sel_scnrio_mng.getTestInputInfoByScnrioNumAndInputNm", inputMap);
    logger.debug("Output Map[" + outputMap.toString() + "]");
    logger.debug("============   End method of UpdateScnrioSrcCdDaoImpl.getTestInputInfoByScnrioNumAndInputNm   ============");
    return outputMap;
  }

  public void delTestInputWithScnrioNumAndInputNm(Map<String, Object> inputMap) throws SQLException {
    logger.debug("============   Start method of UpdateScnrioSrcCdDaoImpl.delTestInputWithScnrioNumAndInputNm   ============");
    logger.debug("Parameter inputMap[" + inputMap.toString() + "]");
    sqlSession.update("com.skd.sel.sel_scnrio_mng.delTestInputWithScnrioNumAndInputNm", inputMap);
    logger.debug("============   End method of UpdateScnrioSrcCdDaoImpl.delTestInputWithScnrioNumAndInputNm   ============");
  }
  
  public void insertNewTestInput(Map<String, Object> inputMap) throws SQLException {
    logger.debug("============   Start method of UpdateScnrioSrcCdDaoImpl.insertNewTestInput   ============");
    logger.debug("Parameter inputMap[" + inputMap.toString() + "]");
    sqlSession.insert("com.skd.sel.sel_scnrio_mng.insertNewTestInput", inputMap);
    logger.debug("============   End method of UpdateScnrioSrcCdDaoImpl.insertNewTestInput   ============");
  }
  
  public void delTestCaseInputWithScnrioNumAndInputNum(Map<String, Object> inputMap) throws SQLException {
    logger.debug("============   Start method of UpdateScnrioSrcCdDaoImpl.delTestCaseInputWithScnrioNumAndInputNum   ============");
    logger.debug("Parameter inputMap[" + inputMap.toString() + "]");
    sqlSession.update("com.skd.sel.sel_scnrio_mng.delTestCaseInputWithScnrioNumAndInputNum", inputMap);
    logger.debug("============   End method of UpdateScnrioSrcCdDaoImpl.delTestCaseInputWithScnrioNumAndInputNum   ============");    
  }
  
  public void insertChangedInputNumWithScnrioNumAndCaseNumAndInputNm(Map<String, Object> inputMap) throws SQLException {
    logger.debug("============   Start method of UpdateScnrioSrcCdDaoImpl.insertChangedInputNumWithScnrioNumAndCaseNumAndInputNm   ============");
    logger.debug("Parameter inputMap[" + inputMap.toString() + "]");
    sqlSession.insert("com.skd.sel.sel_scnrio_mng.insertChangedInputNumWithScnrioNumAndCaseNumAndInputNm", inputMap);
    logger.debug("============   End method of UpdateScnrioSrcCdDaoImpl.insertChangedInputNumWithScnrioNumAndCaseNumAndInputNm   ============");
  }
  
  public void delTestScnrio(Map<String, Object> inputMap) throws SQLException {
    logger.debug("============   Start method of UpdateScnrioSrcCdDaoImpl.delTestScnrio   ============");
    logger.debug("Parameter inputMap[" + inputMap.toString() + "]");
    sqlSession.update("com.skd.sel.sel_scnrio_mng.delTestScnrio", inputMap);
    logger.debug("============   End method of UpdateScnrioSrcCdDaoImpl.delTestScnrio   ============");
  }

  public void insertNewSrcCdWithScnrioNum(Map<String, Object> inputMap) throws SQLException {
    logger.debug("============   Start method of UpdateScnrioSrcCdDaoImpl.insertNewSrcCdWithScnrioNum   ============");
    logger.debug("Parameter inputMap[" + inputMap.toString() + "]");
    sqlSession.insert("com.skd.sel.sel_scnrio_mng.insertNewSrcCdWithScnrioNum", inputMap);
    logger.debug("============   End method of UpdateScnrioSrcCdDaoImpl.insertNewSrcCdWithScnrioNum   ============");
  }
  
  public void delTestInputBiggerThanInputNum(Map<String, Object> inputMap) throws SQLException {
    logger.debug("============   Start method of UpdateScnrioSrcCdDaoImpl.delTestInputBiggerThanInputNum   ============");
    logger.debug("Parameter inputMap[" + inputMap.toString() + "]");
    sqlSession.update("com.skd.sel.sel_scnrio_mng.delTestInputBiggerThanInputNum", inputMap);
    logger.debug("============   End method of UpdateScnrioSrcCdDaoImpl.delTestInputBiggerThanInputNum   ============");    
  }

  public void delTestCaseInputBiggerThanInputNum(Map<String, Object> inputMap) throws SQLException {
    logger.debug("============   Start method of UpdateScnrioSrcCdDaoImpl.delTestCaseInputBiggerThanInputNum   ============");
    logger.debug("Parameter inputMap[" + inputMap.toString() + "]");
    sqlSession.update("com.skd.sel.sel_scnrio_mng.delTestCaseInputBiggerThanInputNum", inputMap);
    logger.debug("============   End method of UpdateScnrioSrcCdDaoImpl.delTestCaseInputBiggerThanInputNum   ============");    
  }
}