package com.skd.ocr.main.service;

import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.skd.ocr.main.service.GetImgOcrInfoByFileKeyService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:/conf/root-context.xml"})
public class GetImgOcrInfoByFileKeyServiceTest {
  @Autowired
  private GetImgOcrInfoByFileKeyService getImgOcrInfoByFileKeyService;

  @Test(timeout=100000)
  public void testGetImgOcrInfoByImgWithStr() throws Exception {
 //   List<Map<String, Object>> outputList = getImgOcrInfoByFileKeyService.getImgOcrByFileKey("NAPv84JGzXLXe7CNLk16kvxEppKvaaeoHK8iYcSo");
 //   Assert.assertEquals(((int[])(outputList.get(0).get("cord")))[0], 4);
  }
}