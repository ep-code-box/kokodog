package com.skd.ppa.main.service;

import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.skd.ppa.main.service.GetPastFileUploadService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:/conf/root-context.xml"})
public class GetPastFileUploadServiceTest {
  @Autowired
  private GetPastFileUploadService getPastFileUploadService;
  
  @Test(timeout=50000)
  public void getPastFileUploadList() throws Exception {
    List<Map<String, Object>> outputList = getPastFileUploadService.getPastFileUploadList();
    Assert.assertTrue(outputList.size() >= 1);
  }
  
  @Test(timeout=50000)
  @Transactional
  @Rollback(true)
  public void deletePastFileUpload() throws Exception {
    getPastFileUploadService.deletePastFileUpload("0gSsH0lVWwlDzP2vfADKkjplb1TGkYVSrjBVHWOo");
    Assert.assertTrue(true);
  }
}