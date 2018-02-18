package com.skd.sel.sel_scnrio_mng.dao.impl;

import java.util.List;
import java.util.Map;
import java.sql.SQLException;

import org.apache.ibatis.session.SqlSession;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.skd.sel.sel_scnrio_mng.dao.UpdateTestCaseInputDao;

@Repository("updateTestCaseInputDao")
public class UpdateTestCaseInputDaoImpl implements UpdateTestCaseInputDao {
  private static Logger logger = LogManager.getLogger(UpdateTestCaseInputDaoImpl.class);

  @Autowired
  private SqlSession sqlSession;

  public Map<String, Object> getTestCaseInputByInputNm(Map<String, Object> inputMap) throws SQLException {
    logger.debug("============   Start method of UpdateTestCaseInputDaoImpl.getTestCaseInputByInputNm   ============");
    logger.debug("Parameter inputMap[" + inputMap.toString() + "]");
    Map<String, Object> outputMap = null;
    outputMap = sqlSession.selectOne("com.skd.sel.sel_scnrio_mng.getTestCaseInputByInputNm", inputMap);
    logger.debug("Parameter outputMap[" + outputMap + "]");
    logger.debug("============   End method of UpdateTestCaseInputDaoImpl.getTestCaseInputByInputNm   ============");
    return outputMap;
  }
  
  public void delTestCaseInputByInputNm(Map<String, Object> inputMap) throws SQLException {
    logger.debug("============   Start method of UpdateTestCaseInputDaoImpl.delTestCaseInput   ============");
    logger.debug("Parameter inputMap[" + inputMap.toString() + "]");
    sqlSession.update("com.skd.sel.sel_scnrio_mng.delTestCaseInput", inputMap);
    logger.debug("============   End method of UpdateTestCaseInputDaoImpl.delTestCaseInput   ============");
  }
  
  public void insertNewTestCaseInput(Map<String, Object> inputMap) throws SQLException {
    logger.debug("============   Start method of UpdateTestCaseInputDaoImpl.insertNewTestCaseInput   ============");
    logger.debug("Parameter inputMap[" + inputMap.toString() + "]");
    sqlSession.insert("com.skd.sel.sel_scnrio_mng.insertNewTestCaseInput", inputMap);
    logger.debug("============   End method of UpdateTestCaseInputDaoImpl.insertNewTestInputByInputNm   ============");
  }  
}