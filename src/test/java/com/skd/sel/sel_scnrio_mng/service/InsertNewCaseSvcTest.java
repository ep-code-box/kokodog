package com.skd.sel.sel_scnrio_mng.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.skd.sel.sel_scnrio_mng.service.InsertNewCaseSvc;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:/conf/root-context.xml", "classpath:/conf/kokodog-servlet.xml"})
public class InsertNewCaseSvcTest {
  @Autowired
  private InsertNewCaseSvc insertNewCaseSvc;
  
  @Autowired
  private SqlSession sqlSession;

  @Test(timeout=1000)
  @Transactional
  @Rollback(true)
  public void testInsertNewCase() throws Exception {
    int scnrioNum = 0;
    Map<String, Object> testInputMap = new HashMap<String, Object>();
    Map<String, Object> outputMap = null;
    Calendar staDtm = GregorianCalendar.getInstance();
    Calendar endDtm = GregorianCalendar.getInstance();
    testInputMap.clear();
    testInputMap.put("user_num", 0);
    testInputMap.put("scnrio_nm", "테스트 시나리오");
    testInputMap.put("op_typ_num", 1);
    testInputMap.put("scnrio_desc", "테스트 시나리오");
    testInputMap.put("seq_num", 1);
    testInputMap.put("src_cd", "python temp source code");
    testInputMap.put("eff_sta_dtm", new Date(staDtm.getTimeInMillis()));
    insertTempScnrioData(testInputMap);
    scnrioNum = ((Long)getInsertedTempScnrioData().get("scnrio_num")).intValue();
    testInsertNewCaseWithPrevDataIneserted(scnrioNum);
    testInsertNewCaseWithPrevDataNotIneserted(scnrioNum);
  }
  
  public void testInsertNewCaseWithPrevDataIneserted(int scnrioNum) throws Exception {
    Map<String, Object> testInputMap = new HashMap<String, Object>();
    Map<String, Object> outputMap = null;
    Calendar staDtm = GregorianCalendar.getInstance();
    Calendar endDtm = GregorianCalendar.getInstance();
    testInputMap.clear();
    testInputMap.put("user_num", 0);
    testInputMap.put("scnrio_num", scnrioNum);
    testInputMap.put("case_nm", "테스트 케이스");
    testInputMap.put("case_desc", "테스트 케이스");
    testInputMap.put("seq_num", 1);
    testInputMap.put("eff_sta_dtm", new Date(staDtm.getTimeInMillis()));
    insertTempCaseData(testInputMap);
    testInputMap.clear();
    testInputMap.put("user_num", 0);
    testInputMap.put("scnrio_num", scnrioNum);
    testInputMap.put("case_nm", "테스트 케이스");
    testInputMap.put("case_desc", "테스트 케이스");
    testInputMap.put("eff_sta_dtm", new Date(staDtm.getTimeInMillis()));
    try {
      insertNewCaseSvc.insertNewCase(testInputMap);
    } catch (Exception e) {
      return;
    }
    Assert.fail("익셉션이 발생해야 하나 익셉션이 발생하지 않았습니다.");
  }
  
  public void testInsertNewCaseWithPrevDataNotIneserted(int scnrioNum) throws Exception {
    Map<String, Object> testInputMap = new HashMap<String, Object>();
    Map<String, Object> outputMap = null;
    Calendar staDtm = GregorianCalendar.getInstance();
    Calendar endDtm = GregorianCalendar.getInstance();
    testInputMap.clear();
    testInputMap.put("scnrio_num", scnrioNum);
    testInputMap.put("case_nm", "테스트 케이스");
    testInputMap.put("user_num", 0);
    testInputMap.put("eff_sta_dtm", new Date(staDtm.getTimeInMillis()));
    deleteTempCaseData(testInputMap);
    testInputMap.clear();
    testInputMap.put("user_num", 0);
    testInputMap.put("scnrio_num", scnrioNum);
    testInputMap.put("case_nm", "테스트 케이스");
    testInputMap.put("case_desc", "테스트 케이스");
    testInputMap.put("eff_sta_dtm", new Date(staDtm.getTimeInMillis()));
    insertNewCaseSvc.insertNewCase(testInputMap);
    outputMap = getInsertedCaseData(testInputMap);
    if (outputMap == null) {
      Assert.fail("1개의 데이터만 조회되어야 하나 데이터가 조회되지 않았습니다.");
    }
    Assert.assertEquals(outputMap.get("case_nm"), "테스트 케이스");
    Assert.assertEquals(outputMap.get("case_desc"), "테스트 케이스");
  }
  
  private Map<String, Object> getInsertedCaseData(Map<String, Object> inputMap) throws Exception {
    return sqlSession.selectOne("com.skd.sel.SelScnrioMngTest.getInsertedCaseData", inputMap);
  }
  
  private Map<String, Object> getInsertedScnrioData(Map<String, Object> inputMap) throws Exception {
    return sqlSession.selectOne("com.skd.sel.SelScnrioMngTest.getInsertedScnrioData", inputMap);
  }
  
  private void insertTempScnrioData(Map<String, Object> inputMap) throws Exception {
    sqlSession.insert("com.skd.sel.SelScnrioMngTest.insertTempScnrioData", inputMap);
  }
  
  private void insertTempCaseData(Map<String, Object> inputMap) throws Exception {
    sqlSession.insert("com.skd.sel.SelScnrioMngTest.insertTempCaseData", inputMap);
  }
  
  private Map<String, Object> getInsertedTempScnrioData() throws Exception {
    return sqlSession.selectOne("com.skd.sel.SelScnrioMngTest.getInsertedTempScnrioData");    
  }
  
  private void deleteTempCaseData(Map<String, Object> inputMap) throws Exception {
    sqlSession.update("com.skd.sel.SelScnrioMngTest.deleteTempCaseData", inputMap);    
  }
}