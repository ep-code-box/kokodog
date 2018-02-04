package com.skd.ppa.module.service;

import net.sf.json.JSONArray;
import net.sf.json.JSONSerializer;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.skd.ppa.module.service.NlpByKonlpyService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:/conf/root-context.xml"})
public class NlpByKonlpyServiceTest {
  @Autowired
  private NlpByKonlpyService nlpByKonlpyService;

  @Test(timeout=50000)
  public void testGetNounList() throws Exception {
/*    JSONArray jsonArray = nlpByKonlpyService.getNounList("이것은 테스트 입니다.");
    Assert.assertTrue(jsonArray.getString(0).equals("이것"));
    Assert.assertTrue(jsonArray.getString(1).equals("테스트"));*/
  }
  
  @Test(timeout=50000)
  public void testGetMorphemeList() throws Exception {
/*    JSONArray jsonArray = nlpByKonlpyService.getMorpheme("이것은 테스트 입니다.");
    Assert.assertTrue(jsonArray.getJSONObject(0).getString("voca").equals("이것"));*/
  }
}