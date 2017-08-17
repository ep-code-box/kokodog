package com.skd.ppa.main.service;

import org.apache.ibatis.session.SqlSession;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.skd.ppa.main.service.GetConvHtmlService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:/conf/root-context.xml", "classpath:/conf/kokodog-servlet.xml"})
public class GetConvHtmlServiceTest {
  @Autowired
  private GetConvHtmlService getConvHtmlService;
  
  @Test(timeout=50000)
  @Transactional
  @Rollback(true)
  public void getHtml() throws Exception {
    String result = getConvHtmlService.getHtml("0gSsH0lVWwlDzP2vfADKkjplb1TGkYVSrjBVHWOo");
    Assert.assertEquals("<?xml", result.substring(0, 5));
  }
}