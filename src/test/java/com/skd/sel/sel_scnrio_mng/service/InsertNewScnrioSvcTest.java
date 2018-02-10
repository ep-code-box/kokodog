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

import com.skd.sel.sel_scnrio_mng.service.InsertNewScnrioSvc;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:/conf/root-context.xml", "classpath:/conf/kokodog-servlet.xml"})
public class InsertNewScnrioSvcTest {
  @Autowired
  private InsertNewScnrioSvc insertNewScnrioSvc;
  
  @Autowired
  private SqlSession sqlSession;

  @Test(timeout=1000)
  @Transactional
  @Rollback(true)
  public void testInsertNewScnrio() throws Exception {
    int scnrioNum = 0;
    Map<String, Object> testInputMap = new HashMap<String, Object>();
    Map<String, Object> outputMap = null;
    int opTypNum = 0;
    Calendar staDtm = GregorianCalendar.getInstance();
    Calendar endDtm = GregorianCalendar.getInstance();
    testInputMap.put("user_num", 0);
    testInputMap.put("op_typ_nm", "TST");
    testInputMap.put("op_typ_desc", "테스트 설명");
    testInputMap.put("eff_sta_dtm", new Date(staDtm.getTimeInMillis()));
    insertTempOpTyp(testInputMap);
    opTypNum = ((Long)getInsertedTempOpTyp().get("op_typ_num")).intValue();
    testInputMap.clear();
    testInputMap.put("user_num", 0);
    testInputMap.put("op_typ_num", opTypNum);
    testInputMap.put("eff_sta_dtm", new Date(staDtm.getTimeInMillis()));
    if (insertTempUserOpTyp(testInputMap) != 1) {
      Assert.fail("테스트 데이터가 정상적으로 삽입되지 않았습니다.");
    }
    testInsertNewScnrioWithPrevDataIneserted(opTypNum);
    testInsertNewScnrioWithPrevDataNotIneserted(opTypNum);
  }
  
  public void testInsertNewScnrioWithPrevDataIneserted(int opTypNum) throws Exception {
    int scnrioNum = 0;
    Map<String, Object> testInputMap = new HashMap<String, Object>();
    Map<String, Object> outputMap = null;
    Calendar staDtm = GregorianCalendar.getInstance();
    Calendar endDtm = GregorianCalendar.getInstance();
    testInputMap.clear();
    testInputMap.put("scnrio_nm", "테스트 시나리오");
    deleteTempScnrioData(testInputMap);
    testInputMap.clear();
    testInputMap.put("user_num", 0);
    testInputMap.put("scnrio_nm", "테스트 시나리오");
    testInputMap.put("op_typ_num", opTypNum);
    testInputMap.put("scnrio_desc", "테스트 시나리오");
    testInputMap.put("seq_num", 1);
    testInputMap.put("src_cd", "python temp source code");
    testInputMap.put("eff_sta_dtm", new Date(staDtm.getTimeInMillis()));
    insertTempScnrioData(testInputMap);
    testInputMap.clear();
    testInputMap.put("user_num", 0);
    testInputMap.put("scnrio_nm", "테스트 시나리오");
    testInputMap.put("op_typ_num", opTypNum);
    testInputMap.put("scnrio_desc", "테스트 시나리오");
    testInputMap.put("eff_sta_dtm", new Date(staDtm.getTimeInMillis()));
    try {
      insertNewScnrioSvc.insertNewScnrio(testInputMap);
    } catch (Exception e) {
      return;
    }
    Assert.fail("익셉션이 발생해야 하나 익셉션이 발생하지 않았습니다.");
  }
  
  public void testInsertNewScnrioWithPrevDataNotIneserted(int opTypNum) throws Exception {
    int scnrioNum = 0;
    Map<String, Object> testInputMap = new HashMap<String, Object>();
    Map<String, Object> outputMap = null;
    Calendar staDtm = GregorianCalendar.getInstance();
    Calendar endDtm = GregorianCalendar.getInstance();
    testInputMap.clear();
    testInputMap.put("scnrio_nm", "테스트 시나리오");
    testInputMap.put("user_num", 0);
    testInputMap.put("eff_sta_dtm", new Date(staDtm.getTimeInMillis()));
    deleteTempScnrioData(testInputMap);
    testInputMap.clear();
    testInputMap.put("user_num", 0);
    testInputMap.put("scnrio_nm", "테스트 시나리오");
    testInputMap.put("op_typ_num", opTypNum);
    testInputMap.put("scnrio_desc", "테스트 시나리오");
    testInputMap.put("system_call_dtm", new Date(staDtm.getTimeInMillis()));
    insertNewScnrioSvc.insertNewScnrio(testInputMap);
    outputMap = getInsertedScnrioData(testInputMap);
    if (outputMap == null) {
      Assert.fail("1개의 데이터만 조회되어야 하나 데이터가 조회되지 않았습니다.");
    }
    Assert.assertEquals(outputMap.get("scnrio_nm"), "테스트 시나리오");
    Assert.assertEquals(outputMap.get("scnrio_desc"), "테스트 시나리오");
    Assert.assertEquals(((Long)outputMap.get("op_typ_num")).intValue(), opTypNum);
  }
  
  private void insertTempOpTyp(Map<String, Object> inputMap) throws Exception {
    sqlSession.insert("com.skd.sel.SelScnrioMngTest.insertTempOpTyp", inputMap);
  }

  private Map<String, Object> getInsertedTempOpTyp() throws Exception {
    return sqlSession.selectOne("com.skd.sel.SelScnrioMngTest.getInsertedTempOpTyp");
  }

  private int insertTempUserOpTyp(Map<String, Object> inputMap) throws Exception {
    return sqlSession.insert("com.skd.sel.SelScnrioMngTest.insertTempUserOpTyp", inputMap);    
  }
  
  private Map<String, Object> getInsertedScnrioData(Map<String, Object> inputMap) throws Exception {
    return sqlSession.selectOne("com.skd.sel.SelScnrioMngTest.getInsertedScnrioData", inputMap);
  }
  
  private void insertTempScnrioData(Map<String, Object> inputMap) throws Exception {
    sqlSession.insert("com.skd.sel.SelScnrioMngTest.insertTempScnrioData", inputMap);
  }

  private void deleteTempScnrioData(Map<String, Object> inputMap) throws Exception {
    sqlSession.update("com.skd.sel.SelScnrioMngTest.deleteTempScnrioData", inputMap);    
  }
  
  private Map<String, Object> getInsertedTempScnrioData() throws Exception {
    return sqlSession.selectOne("com.skd.sel.SelScnrioMngTest.getInsertedTempScnrioData");    
  }
}