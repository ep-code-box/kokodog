package com.skd.ppa.main.service;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.Map;

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
/*    File file = new File(fileDir);
    FileInputStream fis = new FileInputStream(file);
    JSONArray jsonArray = docNlpService.getNounList(fis);
    Assert.assertTrue(jsonArray.getString(0).equals("작성자"));
    Assert.assertTrue(jsonArray.getString(1).equals("소속"));
    jsonArray = docNlpService.getNounList("UPEVSSA1SjTfiYJy1rtNOHUT0Wv40BnXUpXG0b8A");
    Assert.assertTrue(jsonArray.getString(0).equals("작성자"));
    Assert.assertTrue(jsonArray.getString(1).equals("소속"));*/
  }

  @Test(timeout=50000)
  public void testGetMorpheme() throws Exception {
/*    File file = new File(fileDir);
    FileInputStream fis = new FileInputStream(file);
    JSONArray jsonArray = docNlpService.getMorpheme(fis);
    Assert.assertTrue(jsonArray.size() >= 1);*/
  }

  @Test(timeout=50000)
  public void testGetMorphemeDetail() throws Exception {
/*    JSONArray jsonArray = null;
    jsonArray = docNlpService.getMorphemeDetail("UPEVSSA1SjTfiYJy1rtNOHUT0Wv40BnXUpXG0b8A");
    Assert.assertTrue(jsonArray.getJSONObject(0).getString("voca").equals("<?"));*/
  }

  @Test(timeout=50000)
  public void testGetProdChkLstDetail() throws Exception {
/*    Map<String, Object> outputMap = null;
    outputMap = docNlpService.getProdChkLstDetail("UPEVSSA1SjTfiYJy1rtNOHUT0Wv40BnXUpXG0b8A");
    Assert.assertTrue(((String)((List<Map<String, Object>>)outputMap.get("morpheme_detail_list")).get(0).get("voca")).equals("<?"));*/
  }
}