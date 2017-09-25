package com.cmn.err.err_401.service;

import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.cmn.err.err_401.service.GetPageAuthService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:/conf/root-context.xml"})
public class GetPageAuthServiceTest {
  @Autowired
  private GetPageAuthService getPageAuthService;

  @Test(timeout=10000)
  public void testGetAuthListByPath() throws Exception {
    List<Map<String, Object>> outputList = getPageAuthService.getAuthListByPath("/skd/ocr", 1);
    Assert.assertEquals(outputList.size(), 0);
  }
}