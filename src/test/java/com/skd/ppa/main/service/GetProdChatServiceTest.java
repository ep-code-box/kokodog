package com.skd.ppa.main.service;

import java.io.File;
import java.io.FileInputStream;
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

import com.skd.ppa.main.service.GetProdChatService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:/conf/root-context.xml"})
public class GetProdChatServiceTest {
  @Autowired
  private GetProdChatService getProdChatService;
  
  @Test(timeout=50000)
  @Transactional
  @Rollback(true)
  public void testRefresh() throws Exception {
    getProdChatService.refresh(1);
    Assert.assertTrue(true);
  }

  @Test(timeout=50000)
  @Transactional
  @Rollback(true)
  public void testGetProdChat() throws Exception {
    getProdChatService.refresh(1);
    String convResult = getProdChatService.getProdChat("", 1);
    Assert.assertTrue(convResult.equals("안녕하세요, 상품Bot 입니다. 상품/할인이 PLM에 등록되어 있나요?"));
  }

  @Test(timeout=50000)
  @Transactional
  @Rollback(true)
  public void testGetProdChatDeeply() throws Exception {
    getProdChatService.refresh(1);
    String convResult = getProdChatService.getProdChat("", 1);
    Assert.assertTrue(convResult.equals("안녕하세요, 상품Bot 입니다. 상품/할인이 PLM에 등록되어 있나요?"));
    convResult = getProdChatService.getProdChat("아무말 대잔치!!", 1);
    Assert.assertTrue(convResult.equals("알아듣기 쉽게 다시 말씀해주시면 좋겠어요."));
  }
  
  @Test(timeout=5000)
  @Transactional
  @Rollback(true)
  public void testGetProdInitChat() throws Exception {
    getProdChatService.refresh(1);
    String convResult = getProdChatService.getProdChat("", 1);
    Assert.assertTrue(convResult.equals("안녕하세요, 상품Bot 입니다. 상품/할인이 PLM에 등록되어 있나요?"));
    convResult = getProdChatService.getProdChat("응", 1);
    Assert.assertTrue(convResult.equals("PLM에 상품/할인이 등록되어 있군요, 상품/할인명을 말씀해주시면 정보를 I/F하겠습니다.(미구현)"));
    List<Map<String, Object>> outputList = getProdChatService.getProdInitChat(1);
    Assert.assertEquals(outputList.size(), 3);
  }
}