package com.res.sto.batch;

import java.util.Date;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import org.apache.ibatis.session.SqlSession;

public class Dummy {
  
  private static Logger logger = LogManager.getLogger(GetStoProgressHstInfo.class);
  private String report = "";

  public String main(SqlSession sqlSession, Date exeDateTime) throws Exception {
    logger.debug("Dummy Start");
    init(sqlSession, exeDateTime);
    process(sqlSession, exeDateTime);
    return report;
  }
  
  private void init(SqlSession sqlSession, Date exeDateTime) throws Exception {
    this.report = null;
  }
  
  private void process(SqlSession sqlSession, Date exeDateTime) throws Exception {
    this.report = "Test finished";
  }
}