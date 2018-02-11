package com.skd.sel.sel_scnrio_mng.dao;

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

import com.skd.sel.sel_scnrio_mng.dao.InsertNewCaseDao;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:/conf/root-context.xml", "classpath:/conf/kokodog-servlet.xml"})
public class InsertNewCaseDaoTest {
  @Autowired
  private InsertNewCaseDao insertNewCaseDao;
  
  @Autowired
  private SqlSession sqlSession;

  @Test(timeout=1000)
  @Transactional
  @Rollback(true)
  public void testInsertNewCase() throws Exception {
    int opTypNum = 0;
    Map<String, Object> testInputMap = new HashMap<String, Object>();
    Map<String, Object> outputMap = null;
    Calendar staDtm = GregorianCalendar.getInstance();
    Calendar endDtm = GregorianCalendar.getInstance();
    testInputMap.put("user_num", 0);
    testInputMap.put("scnrio_nm", "테스트 시나리오");
    testInputMap.put("op_typ_num", 1);
    testInputMap.put("scnrio_desc", "테스트 시나리오");
    testInputMap.put("seq_num", 1);
    testInputMap.put("src_cd", "python temp src cd");
    testInputMap.put("eff_sta_dtm", new Date(staDtm.getTimeInMillis()));
    insertTempScnrioData(testInputMap);
    outputMap = getInsertedTempScnrioData();
    testInputMap.clear();
    testInputMap.put("user_num", 0);
    testInputMap.put("scnrio_num", ((Long)outputMap.get("scnrio_num")).intValue());
    testInputMap.put("case_nm", "테스트 케이스");
    testInputMap.put("case_desc", "테스트 케이스");
    testInputMap.put("system_call_dtm", new Date(staDtm.getTimeInMillis()));
    insertNewCaseDao.insertNewCase(testInputMap);
    outputMap = getInsertedCaseData(testInputMap);
    if (outputMap == null) {
      Assert.fail("1개의 데이터만 조회되어야 하나 데이터가 조회되지 않았습니다.");
    }
    Assert.assertEquals(outputMap.get("case_nm"), "테스트 케이스");
    Assert.assertEquals(outputMap.get("case_desc"), "테스트 케이스");
  }
  
  @Test(timeout=1000)
  @Transactional
  @Rollback(true)
  public void testChkSameCaseNm() throws Exception {
    testChkSameCaseNmExistY();
    testChkSameCaseNmExistN();
  }
  
  @Test(timeout=1000)
  @Transactional
  @Rollback(true)
  public void testGetNewCaseNum() throws Exception {
    Map<String, Object> testInputMap = new HashMap<String, Object>();
    Map<String, Object> outputMap = null;
    Calendar staDtm = GregorianCalendar.getInstance();
    Calendar endDtm = GregorianCalendar.getInstance();
    int caseNum = 0;
    int scnrioNum = 0;
    testInputMap.put("user_num", 0);
    testInputMap.put("scnrio_nm", "테스트 시나리오");
    testInputMap.put("op_typ_num", 1);
    testInputMap.put("scnrio_desc", "테스트 시나리오");
    testInputMap.put("seq_num", 1);
    testInputMap.put("src_cd", "python temp src cd");
    testInputMap.put("eff_sta_dtm", new Date(staDtm.getTimeInMillis()));
    insertTempScnrioData(testInputMap);
    scnrioNum = ((Long)getInsertedTempScnrioData().get("scnrio_num")).intValue();
    testInputMap.clear();
    testInputMap.put("user_num", 0);
    testInputMap.put("scnrio_num", scnrioNum);
    testInputMap.put("case_nm", "테스트 케이스");
    testInputMap.put("case_desc", "테스트 케이스");
    testInputMap.put("eff_sta_dtm", new Date(staDtm.getTimeInMillis()));
    insertTempCaseData(testInputMap);
    testInputMap.clear();
    testInputMap.put("scnrio_num", scnrioNum);
    caseNum = ((Long)getInsertedCaseData(testInputMap).get("case_num")).intValue();
    testInputMap.clear();
    testInputMap.put("scnrio_num", scnrioNum);
    testInputMap.put("case_nm", "테스트 케이스");
    outputMap = insertNewCaseDao.getNewCaseNum(testInputMap);
    Assert.assertEquals(((Long)outputMap.get("case_num")).intValue(), caseNum);
  }
  
  private void testChkSameCaseNmExistY() throws Exception {
    Map<String, Object> testInputMap = new HashMap<String, Object>();
    Map<String, Object> outputMap = null;
    Calendar staDtm = GregorianCalendar.getInstance();
    Calendar endDtm = GregorianCalendar.getInstance();
    testInputMap.put("user_num", 0);
    testInputMap.put("scnrio_nm", "테스트 시나리오");
    testInputMap.put("op_typ_num", 1);
    testInputMap.put("scnrio_desc", "테스트 시나리오");
    testInputMap.put("seq_num", 1);
    testInputMap.put("src_cd", "python temp src cd");
    testInputMap.put("eff_sta_dtm", new Date(staDtm.getTimeInMillis()));
    insertTempScnrioData(testInputMap);
    outputMap = getInsertedTempScnrioData();
    testInputMap.clear();
    testInputMap.put("user_num", 0);
    testInputMap.put("scnrio_num", ((Long)outputMap.get("scnrio_num")).intValue());
    testInputMap.put("case_nm", "테스트 케이스");
    testInputMap.put("case_desc", "테스트 케이스");
    testInputMap.put("eff_sta_dtm", new Date(staDtm.getTimeInMillis()));
    insertTempCaseData(testInputMap);
    testInputMap.clear();
    testInputMap.put("user_num", 0);
    testInputMap.put("scnrio_num", ((Long)outputMap.get("scnrio_num")).intValue());
    testInputMap.put("case_nm", "테스트 케이스");
    testInputMap.put("case_desc", "테스트 케이스");
    testInputMap.put("system_call_dtm", new Date(staDtm.getTimeInMillis()));
    outputMap = insertNewCaseDao.chkSameCaseNm(testInputMap);
    Assert.assertEquals(outputMap.get("is_exist_yn"), "Y");
  }
  
  private void testChkSameCaseNmExistN() throws Exception {
    Map<String, Object> testInputMap = new HashMap<String, Object>();
    Map<String, Object> outputMap = null;
    Calendar staDtm = GregorianCalendar.getInstance();
    Calendar endDtm = GregorianCalendar.getInstance();
    testInputMap.put("user_num", 0);
    testInputMap.put("scnrio_nm", "테스트 시나리오");
    testInputMap.put("op_typ_num", 1);
    testInputMap.put("scnrio_desc", "테스트 시나리오");
    testInputMap.put("seq_num", 1);
    testInputMap.put("src_cd", "python temp src cd");
    testInputMap.put("eff_sta_dtm", new Date(staDtm.getTimeInMillis()));
    insertTempScnrioData(testInputMap);
    outputMap = getInsertedTempScnrioData();
    testInputMap.clear();
    testInputMap.put("user_num", 0);
    testInputMap.put("scnrio_num", ((Long)outputMap.get("scnrio_num")).intValue());
    testInputMap.put("case_nm", "테스트 케이스");
    deleteTempCaseData(testInputMap);
    testInputMap.clear();
    testInputMap.put("user_num", 0);
    testInputMap.put("scnrio_num", ((Long)outputMap.get("scnrio_num")).intValue());
    testInputMap.put("case_nm", "테스트 케이스");
    testInputMap.put("case_desc", "테스트 케이스");
    testInputMap.put("system_call_dtm", new Date(staDtm.getTimeInMillis()));
    outputMap = insertNewCaseDao.chkSameCaseNm(testInputMap);
    Assert.assertEquals(outputMap.get("is_exist_yn"), "N");
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
  
  private Map<String, Object> getInsertedCaseData(Map<String, Object> inputMap) throws Exception {
    return sqlSession.selectOne("com.skd.sel.SelScnrioMngTest.getInsertedCaseData", inputMap);
  }
  
  private void insertTempScnrioData(Map<String, Object> inputMap) throws Exception {
    sqlSession.insert("com.skd.sel.SelScnrioMngTest.insertTempScnrioData", inputMap);
  }

  private void insertTempCaseData(Map<String, Object> inputMap) throws Exception {
    sqlSession.insert("com.skd.sel.SelScnrioMngTest.insertTempCaseData", inputMap);
  }

  private void deleteTempCaseData(Map<String, Object> inputMap) throws Exception {
    sqlSession.update("com.skd.sel.SelScnrioMngTest.deleteTempCaseData", inputMap);    
  }
  
  private Map<String, Object> getInsertedTempScnrioData() throws Exception {
    return sqlSession.selectOne("com.skd.sel.SelScnrioMngTest.getInsertedTempScnrioData");    
  }
}