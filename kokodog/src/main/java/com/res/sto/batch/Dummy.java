package com.res.sto.batch;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.text.SimpleDateFormat;

import org.apache.log4j.Logger;
import org.apache.ibatis.session.SqlSession;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;

public class Dummy {
  
  private static Logger logger = Logger.getLogger(GetStoProgressHstInfo.class);
  private String report = "";
  private SqlSession sqlSession;
  private Date exeDateTime;

  public String main(SqlSession sqlSession, Date exeDateTime) throws Exception {
    logger.debug("Dummy Start");
    init(sqlSession, exeDateTime);
    process(sqlSession, exeDateTime);
    return report;
  }
  
  private void init(SqlSession sqlSession, Date exeDateTime) throws Exception {
    this.sqlSession = sqlSession;
    this.exeDateTime = exeDateTime;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
  }
  
  private void process(SqlSession sqlSession, Date exeDateTime) throws Exception {
    this.report = "Test finished";
  }
}