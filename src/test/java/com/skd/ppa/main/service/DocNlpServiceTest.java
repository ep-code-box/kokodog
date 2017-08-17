package com.skd.ppa.main.service;

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

import com.skd.ppa.main.service.DocNlpService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:/conf/root-context.xml"})
public class DocNlpServiceTest {
  private static final String fileDir = "/home/leems83/data/proposal_for_skcc_dt/[전산요건] T끼리 순액 맞춤형 요금제.docx";

  @Autowired
  private DocNlpService docNlpService;
  
  @Test(timeout=50000)
  public void testGetNounList() throws Exception {
    File file = new File(fileDir);
    FileInputStream fis = new FileInputStream(file);
    JSONArray jsonArray = docNlpService.getNounList(fis);
    Assert.assertTrue(jsonArray.getString(0).equals("작성자"));
    Assert.assertTrue(jsonArray.getString(1).equals("소속"));
  }

  @Test(timeout=50000)
  public void getMorpheme() throws Exception {
    File file = new File(fileDir);
    FileInputStream fis = new FileInputStream(file);
    JSONArray jsonArray = docNlpService.getMorpheme(fis);
    Assert.assertTrue(jsonArray.size() >= 1);
  }
}