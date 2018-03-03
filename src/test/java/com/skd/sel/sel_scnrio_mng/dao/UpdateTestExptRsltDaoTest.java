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

import com.skd.sel.sel_scnrio_mng.dao.UpdateTestExptRsltDao;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:/conf/root-context.xml", "classpath:/conf/kokodog-servlet.xml"})
public class UpdateTestExptRsltDaoTest {
  @Autowired
  private UpdateTestExptRsltDao updateTestExptRsltDao;
  
  @Autowired
  private SqlSession sqlSession;

  @Test(timeout=1000)
  @Transactional
  @Rollback(true)
  public void testDelTestExptRsltByStepNum() throws Exception {
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
    testInputMap.put("src_cd", "Temp python source code");
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
    testInputMap.put("user_num", 0);
    testInputMap.put("scnrio_num", scnrioNum);
    testInputMap.put("case_num", 1);
    testInputMap.put("test_step_num", 1);
    testInputMap.put("rslt_strd", "테스트");
    testInputMap.put("judg_typ_cd", 1);
    testInputMap.put("eff_sta_dtm", new Date(staDtm.getTimeInMillis()));
    insertTempTestExptRslt(testInputMap);
    testInputMap.clear();
    testInputMap.put("scnrio_num", scnrioNum);
    testInputMap.put("case_num", 1);
    testInputMap.put("test_step_num", 1);
    testInputMap.put("user_num", 0);
    testInputMap.put("system_call_dtm", new Date(staDtm.getTimeInMillis()));
    updateTestExptRsltDao.delTestExptRsltByStepNum(testInputMap);
    testInputMap.clear();
    testInputMap.put("scnrio_num", scnrioNum);
    testInputMap.put("case_num", 1);
    testInputMap.put("test_step_num", 1);
    outputMap = getInsertedTempTestExptRslt(testInputMap);
    if (outputMap == null || outputMap.get("eff_end_dtm") == null) {
      Assert.fail("Test setting failed..");
    }
    Assert.assertEquals(new Long(staDtm.getTimeInMillis() / 1000L - 1L), new Long(((Date)outputMap.get("eff_end_dtm")).getTime() / 1000L));
  }
  
  @Test(timeout=1000)
  @Transactional
  @Rollback(true)
  public void testInsertTestExptRslt() throws Exception {
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
    testInputMap.put("src_cd", "Temp python source code");
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
    testInputMap.put("user_num", 0);
    testInputMap.put("scnrio_num", scnrioNum);
    testInputMap.put("case_num", 1);
    testInputMap.put("test_step_num", 1);
    testInputMap.put("rslt_strd", "테스트");
    testInputMap.put("judg_typ_cd", 1);
    testInputMap.put("system_call_dtm", new Date(staDtm.getTimeInMillis()));
    updateTestExptRsltDao.insertTestExptRslt(testInputMap);
    testInputMap.clear();
    testInputMap.put("scnrio_num", scnrioNum);
    testInputMap.put("case_num", 1);
    testInputMap.put("test_step_num", 1);
    outputMap = getInsertedTestExptRslt(testInputMap);
    if (outputMap == null) {
      Assert.fail("정상적으로 삽입되어야 할 데이터가 삽입되지 않았습니다.");
    }
    Assert.assertEquals(1, outputMap.get("judg_typ_cd"));
    Assert.assertEquals("테스트", outputMap.get("rslt_strd"));
  }
  
  private void insertTempTestExptRslt(Map<String, Object> inputMap) throws Exception {
    sqlSession.insert("com.skd.sel.SelScnrioMngTest.insertTempTestExptRslt", inputMap);
  }

  private void insertTempScnrioData(Map<String, Object> inputMap) throws Exception {
    sqlSession.insert("com.skd.sel.SelScnrioMngTest.insertTempScnrioData", inputMap);
  }

  private Map<String, Object> getInsertedTempScnrioData() throws Exception {
    return sqlSession.selectOne("com.skd.sel.SelScnrioMngTest.getInsertedTempScnrioData");    
  }
  
  private void insertTempCaseData(Map<String, Object> inputMap) throws Exception {
    sqlSession.insert("com.skd.sel.SelScnrioMngTest.insertTempCaseData", inputMap);
  }

  private Map<String, Object> getInsertedCaseData(Map<String, Object> inputMap) throws Exception {
    return sqlSession.selectOne("com.skd.sel.SelScnrioMngTest.getInsertedCaseData", inputMap);
  }

  private Map<String, Object> getInsertedTempTestExptRslt(Map<String, Object> inputMap) throws Exception {
    return sqlSession.selectOne("com.skd.sel.SelScnrioMngTest.getInsertedTempTestExptRslt", inputMap);
  }

  private Map<String, Object> getInsertedTestExptRslt(Map<String, Object> inputMap) throws Exception {
    return sqlSession.selectOne("com.skd.sel.SelScnrioMngTest.getInsertedTestExptRslt", inputMap);
  }
}