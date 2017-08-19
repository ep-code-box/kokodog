package com.cmn.cmn.service;

import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.cmn.cmn.service.GetCodeValService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:/conf/root-context.xml", "classpath:/conf/kokodog-servlet.xml"})
public class GetCodeValServiceTest {
  @Autowired
  private GetCodeValService getCodeValService;
  
  @Test(timeout=50000)
  public void testGetCodeVal() throws Exception {
    Map<Integer, String> resultMap = getCodeValService.getCodeVal(31);
    Assert.assertEquals(3, resultMap.size());
  }
}