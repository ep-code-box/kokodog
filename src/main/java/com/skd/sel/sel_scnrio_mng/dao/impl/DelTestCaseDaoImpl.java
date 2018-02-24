package com.skd.sel.sel_scnrio_mng.dao.impl;

import java.util.Map;
import java.sql.SQLException;

import org.apache.ibatis.session.SqlSession;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.skd.sel.sel_scnrio_mng.dao.DelTestCaseDao;

@Repository("delTestCaseDao")
public class DelTestCaseDaoImpl implements DelTestCaseDao {
  private static Logger logger = LogManager.getLogger(DelTestCaseDaoImpl.class);

  @Autowired
  private SqlSession sqlSession;
  
  public Map<String, Object> getCanBeDeletedCase(Map<String, Object> inputMap) throws SQLException {
    logger.debug("============   Start method of DelTestCaseDaoImpl.getCanBeDeletedCase   ============");
    logger.debug("Parameter inputMap[" + inputMap.toString() + "]");
    Map<String, Object> outputMap = null;
    outputMap = sqlSession.selectOne("com.skd.sel.sel_scnrio_mng.DelTestCase.getCanBeDeletedCase", inputMap);
    logger.debug("Parameter outputMap[" + outputMap + "]");
    logger.debug("============   End method of DelTestCaseDaoImpl.getCanBeDeletedCase   ============");
    return outputMap;
  }

  public void delTestCaseWithScnrioNumAndCaseNum(Map<String, Object> inputMap) throws SQLException {
    logger.debug("============   Start method of DelTestCaseDaoImpl.delTestCaseWithScnrioNumAndCaseNum   ============");
    logger.debug("Parameter inputMap[" + inputMap.toString() + "]");
    sqlSession.update("com.skd.sel.sel_scnrio_mng.DelTestCase.delTestCaseWithScnrioNumAndCaseNum", inputMap);
    logger.debug("============   End method of DelTestCaseDaoImpl.delTestCaseWithScnrioNumAndCaseNum   ============");
  }
  
  public void delAllTestCaseInputWithScnrioNumAndCaseNum(Map<String, Object> inputMap) throws SQLException {
    logger.debug("============   Start method of DelTestCaseDaoImpl.delAllTestCaseInputWithScnrioNumAndCaseNum   ============");
    logger.debug("Parameter inputMap[" + inputMap.toString() + "]");
    sqlSession.update("com.skd.sel.sel_scnrio_mng.DelTestCase.delAllTestCaseInputWithScnrioNumAndCaseNum", inputMap);
    logger.debug("============   End method of DelTestCaseDaoImpl.delAllTestCaseInputWithScnrioNumAndCaseNum   ============");
  }
}