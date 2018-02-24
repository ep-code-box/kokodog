package com.skd.sel.sel_scnrio_mng.dao.impl;

import java.util.Map;
import java.sql.SQLException;

import org.apache.ibatis.session.SqlSession;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.skd.sel.sel_scnrio_mng.dao.DelTestScnrioDao;

@Repository("delTestScnrioDao")
public class DelTestScnrioDaoImpl implements DelTestScnrioDao {
  private static Logger logger = LogManager.getLogger(DelTestScnrioDaoImpl.class);

  @Autowired
  private SqlSession sqlSession;
  
  public void delTestScnrio(Map<String, Object> inputMap) throws SQLException {
    logger.debug("============   Start method of DelTestScnrioDaoImpl.delTestScnrio   ============");
    logger.debug("Parameter inputMap[" + inputMap.toString() + "]");
    sqlSession.update("com.skd.sel.sel_scnrio_mng.DelTestScnrio.delTestScnrio", inputMap);
    logger.debug("============   End method of DelTestScnrioDaoImpl.delTestScnrio   ============");
  }

  public void delAllTestCaseWithScnrioNum(Map<String, Object> inputMap) throws SQLException {
    logger.debug("============   Start method of DelTestScnrioDaoImpl.delAllTestCaseWithScnrioNum   ============");
    logger.debug("Parameter inputMap[" + inputMap.toString() + "]");
    sqlSession.update("com.skd.sel.sel_scnrio_mng.DelTestScnrio.delAllTestCaseWithScnrioNum", inputMap);
    logger.debug("============   End method of DelTestScnrioDaoImpl.delAllTestCaseWithScnrioNum   ============");
  }
  public void delAllTestInputWithScnrioNum(Map<String, Object> inputMap) throws SQLException {
    logger.debug("============   Start method of DelTestScnrioDaoImpl.delAllTestInputWithScnrioNum   ============");
    logger.debug("Parameter inputMap[" + inputMap.toString() + "]");
    sqlSession.update("com.skd.sel.sel_scnrio_mng.DelTestScnrio.delAllTestInputWithScnrioNum", inputMap);
    logger.debug("============   End method of DelTestScnrioDaoImpl.delAllTestInputWithScnrioNum   ============");
  }
  
  public void delAllTestCaseInputWithScnrioNum(Map<String, Object> inputMap) throws SQLException {
    logger.debug("============   Start method of DelTestScnrioDaoImpl.delAllTestCaseInputWithScnrioNum   ============");
    logger.debug("Parameter inputMap[" + inputMap.toString() + "]");
    sqlSession.update("com.skd.sel.sel_scnrio_mng.DelTestScnrio.delAllTestCaseInputWithScnrioNum", inputMap);
    logger.debug("============   End method of DelTestScnrioDaoImpl.delAllTestCaseInputWithScnrioNum   ============");
  }
}