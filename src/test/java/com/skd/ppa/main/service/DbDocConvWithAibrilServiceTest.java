package com.skd.ppa.main.service;

import java.net.MalformedURLException;
import java.io.File;

import org.apache.ibatis.session.SqlSession;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.skd.ppa.main.service.DbDocConvWithAibrilServiceTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:/conf/root-context.xml"})
public class DbDocConvWithAibrilServiceTest {
  @Autowired
  private DbDocConvWithAibrilService dbDocConvWithAibrilService;
  
  @Autowired
  private SqlSession sqlSession;

  private static final Logger logger = LogManager.getLogger(DbDocConvWithAibrilServiceTest.class);

  @BeforeClass
  public static void setLogger() throws MalformedURLException {
    System.setProperty("log4j.configurationFile", "src/main/resources/conf/log4j.xml");
  }

  @Test(timeout=50000)
  @Transactional
  @Rollback(true)
  public void convToHtml() throws Exception {
//    dbDocConvWithAibrilService.convToHtml("XGIBimKc0tJWksI8uCNpdaIzrzhQOQuUNqV0EXf4");
  }
}