package com.skd.sel.sel_scnrio_mng.dao.impl;

import java.util.List;
import java.util.Map;
import java.sql.SQLException;

import org.apache.ibatis.session.SqlSession;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.skd.sel.sel_scnrio_mng.dao.UpdateTestExptRsltDao;

@Repository("updateTestExptRsltDao")
public class UpdateTestExptRsltDaoImpl implements UpdateTestExptRsltDao {
  private static Logger logger = LogManager.getLogger(UpdateTestExptRsltDaoImpl.class);

  @Autowired
  private SqlSession sqlSession;

  public void delTestExptRsltByStepNum(Map<String, Object> inputMap) throws SQLException {
    logger.debug("============   Start method of UpdateTestExptRsltDaoImpl.delTestExptRsltByStepNum   ============");
    logger.debug("Parameter inputMap[" + inputMap.toString() + "]");
    sqlSession.update("com.skd.sel.sel_scnrio_mng.UpdateTestExptRslt.delTestExptRsltByStepNum", inputMap);
    logger.debug("============   End method of UpdateTestExptRsltDaoImpl.delTestExptRsltByStepNum   ============");
  }
  
  public void insertTestExptRslt(Map<String, Object> inputMap) throws SQLException {
    logger.debug("============   Start method of UpdateTestExptRsltDaoImpl.insertTestExptRslt   ============");
    logger.debug("Parameter inputMap[" + inputMap.toString() + "]");
    sqlSession.insert("com.skd.sel.sel_scnrio_mng.UpdateTestExptRslt.insertTestExptRslt", inputMap);
    logger.debug("============   End method of UpdateTestExptRsltDaoImpl.insertTestExptRslt   ============");
  }
}