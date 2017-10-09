package com.cmn.cmn.service;

import java.net.MalformedURLException;
import java.util.Map;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.cmn.cmn.service.GetCodeValService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:/conf/root-context.xml", "classpath:/conf/kokodog-servlet.xml"})
public class GetCodeValServiceTest {
  @Autowired
  private GetCodeValService getCodeValService;
  
  private static final Logger logger = LogManager.getLogger(GetCodeValServiceTest.class);
     
  @BeforeClass
  public static void setLogger() throws MalformedURLException {
    System.setProperty("log4j.configuration", "/WEB-INF/classes/conf/log4j.xml");
  }
  
  @Test(timeout=50000)
  public void testGetCodeVal() throws Exception {
    Map<Integer, String> resultMap = getCodeValService.getCodeVal(31);
    Assert.assertEquals(3, resultMap.size());
  }
}