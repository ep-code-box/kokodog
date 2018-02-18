package com.skd.sel.sel_scnrio_mng.dao.impl;

import java.util.Map;
import java.sql.SQLException;

import org.apache.ibatis.session.SqlSession;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.skd.sel.sel_scnrio_mng.dao.DelScnrioDao;

@Repository("delScnrioDao")
public class DelScnrioDaoImpl implements DelScnrioDao {
  private static Logger logger = LogManager.getLogger(DelScnrioDaoImpl.class);

  @Autowired
  private SqlSession sqlSession;
  
  public void delScnrio(Map<String, Object> inputMap) throws SQLException {
    logger.debug("============   Start method of DelScnrioDaoImpl.delScnrio   ============");
    logger.debug("Parameter inputMap[" + inputMap.toString() + "]");
    sqlSession.update("com.skd.sel.sel_scnrio_mng.delScnrio", inputMap);
    logger.debug("============   End method of DelScnrioDaoImpl.delScnrio   ============");
  }
}