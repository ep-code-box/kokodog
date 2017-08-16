package com.skd.ppa.main.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.skd.ppa.main.service.DbDocConvWithAibrilServiceTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:/conf/root-context.xml"})
public class DbDocConvWithAibrilServiceTest {
  @Autowired
  private DbDocConvWithAibrilService dbDocConvWithAibrilService;
  
  @Test(timeout=50000)
  @Transactional
  @Rollback(true)
  public void convToHtml() throws Exception {
    dbDocConvWithAibrilService.convToHtml("XGIBimKc0tJWksI8uCNpdaIzrzhQOQuUNqV0EXf4");
  }
}