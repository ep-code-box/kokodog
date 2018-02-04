package com.skd.ppa.module.service;

import java.io.File;
import java.io.FileInputStream;

import net.sf.json.JSONArray;
import net.sf.json.JSONSerializer;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.skd.ppa.module.service.DocConvWithAibrilService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:/conf/root-context.xml", "classpath:/conf/kokodog-servlet.xml"})
public class DocConvWithAibrilServiceTest {
  private static final String fileDir = "/home/leems83/data/proposal_for_skcc_dt/[전산요건] T끼리 순액 맞춤형 요금제.docx";

  @Autowired
  private DocConvWithAibrilService docConvWithAibrilService;
  
  @Test(timeout=50000)
  public void testGetNounList() throws Exception {
/*    FileInputStream fis = new FileInputStream(new File(fileDir));
    String htmlStr = docConvWithAibrilService.convToHtml(fis);
    Assert.assertEquals("<?xml", htmlStr.substring(0, 5));*/
  }
}