package com.skd.ocr.module.service;

import java.io.RandomAccessFile;

import net.sf.json.JSONArray;
import net.sf.json.JSONSerializer;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.skd.ocr.module.service.ImgOcrService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:/conf/root-context.xml"})
public class ImgOcrServiceTest {
  @Autowired
  private ImgOcrService imgOcrService;

  @Test(timeout=100000)
  public void testGetImgOcrInfoByImgWithStr() throws Exception {
    RandomAccessFile raf = new RandomAccessFile("/home/leems83/workspace/kokodog/src/test/resources/python/2.jpg", "r");
    byte[] b = new byte[(int)raf.length()];
    raf.readFully(b);
    raf.close();
    JSONArray returnJson = imgOcrService.getImgOcrInfoByImgWithStr(b);
    Assert.assertEquals(returnJson.getJSONObject(0).getJSONArray("cord").getInt(0), 4);
  }
}