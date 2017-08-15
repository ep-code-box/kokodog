package com.skd.ppa.service;

import net.sf.json.JSONArray;
import net.sf.json.JSONSerializer;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.skd.ppa.service.DocNlpService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:/conf/root-context.xml"})
public class DocNlpService {
  @Autowired
  private DocNlpService docNlpService;
  
  @Test(timeout=5000)
  public void testGetNounList() throws Exception {
    JSONArray jsonArray = docNlpService.getNounList("이것은 테스트 입니다.");
    Assert.assertTrue(jsonArray.getString(0).equals("이것"));
    Assert.assertTrue(jsonArray.getString(1).equals("테스트"));
  }
  
  @Test(timeout=5000)
  public void testGetMorphemeList() throws Exception {
    JSONArray jsonArray = docNlpService.getMorpheme("이것은 테스트 입니다.");
    Assert.assertTrue(jsonArray.getJSONObject(0).getString("voca").equals("이것"));
  }
}