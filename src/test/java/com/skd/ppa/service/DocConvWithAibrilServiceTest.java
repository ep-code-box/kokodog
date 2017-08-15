package com.skd.ppa.service;

import java.io.File;
import java.io.FileInputStream;

import net.sf.json.JSONArray;
import net.sf.json.JSONSerializer;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.skd.ppa.service.DocConvWithAibrilService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:/conf/root-context.xml"})
public class DocConvWithAibrilServiceTest {
  private static final String fileDir = "/home/leems83/data/proposal_for_skcc_dt/[전산요건] T끼리 순액 맞춤형 요금제.docx";
  @Autowired
  private DocConvWithAibrilService docConvWithAibrilService;
  @Test(timeout=5000)
  public void testGetNounList() throws Exception {
    FileInputStream fis = new FileInputStream(new File(fileDir));
    String htmlStr = docConvWithAibrilService.convToHtml(fis);
    Assert.assertEquals("<?xml", htmlStr.substring(0, 5));
  }
}