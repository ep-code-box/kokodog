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

import com.skd.sel.sel_scnrio_mng.dao.DelTestScnrioDao;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:/conf/root-context.xml", "classpath:/conf/kokodog-servlet.xml"})
public class DelTestScnrioDaoTest {
  @Autowired
  private DelTestScnrioDao delTestScnrioDao;
  
  @Autowired
  private SqlSession sqlSession;

  @Test(timeout=1000)
  @Transactional
  @Rollback(true)
  public void testDelTestScnrio() throws Exception {
    int scnrioNum = 0;
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
    scnrioNum = ((Long)outputMap.get("scnrio_num")).intValue();
    testInputMap.clear();
    testInputMap.put("scnrio_num", scnrioNum);
    testInputMap.put("user_num", 1);
    testInputMap.put("system_call_dtm", new Date(staDtm.getTimeInMillis()));
    delTestScnrioDao.delTestScnrio(testInputMap);
    testInputMap.clear();
    testInputMap.put("scnrio_num", scnrioNum);
    outputMap = getDeletedTestScnrioData(testInputMap);
    Assert.assertEquals(new Long(staDtm.getTimeInMillis() / 1000L - 1L), new Long(((Date)outputMap.get("eff_end_dtm")).getTime() / 1000L));
  }
  
  @Test(timeout=1000)
  @Transactional
  @Rollback(true)
  public void testDelAllTestCaseWithScnrioNum() throws Exception {
    int scnrioNum = 0;
    Map<String, Object> testInputMap = new HashMap<String, Object>();
    Map<String, Object> outputMap = null;
    List<Map<String, Object>> outputList = null;
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
    scnrioNum = ((Long)outputMap.get("scnrio_num")).intValue();
    testInputMap.clear();
    testInputMap.put("user_num", 0);
    testInputMap.put("scnrio_num", scnrioNum);
    testInputMap.put("case_nm", "테스트 케이스");
    testInputMap.put("case_desc", "테스트 케이스");
    testInputMap.put("eff_sta_dtm", new Date(staDtm.getTimeInMillis()));
    insertTempCaseData(testInputMap);
    outputMap = getInsertedCaseData(testInputMap);
    testInputMap.clear();
    testInputMap.put("scnrio_num", scnrioNum);
    testInputMap.put("user_num", 1);
    testInputMap.put("system_call_dtm", new Date(staDtm.getTimeInMillis()));
    delTestScnrioDao.delAllTestCaseWithScnrioNum(testInputMap);
    testInputMap.clear();
    testInputMap.put("scnrio_num", scnrioNum);
    outputList = getDeletedTestCaseData(testInputMap);
    for (int i = 0; i < outputList.size(); i++) {
      Assert.assertEquals(new Long(staDtm.getTimeInMillis() / 1000L - 1L), new Long(((Date)outputList.get(i).get("eff_end_dtm")).getTime() / 1000L));      
    }
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
  
  private Map<String, Object> getInsertedTempScnrioData() throws Exception {
    return sqlSession.selectOne("com.skd.sel.SelScnrioMngTest.getInsertedTempScnrioData");    
  }
  
  private Map<String, Object> getDeletedTestScnrioData(Map<String, Object> inputMap) throws Exception {
    return sqlSession.selectOne("com.skd.sel.SelScnrioMngTest.getDeletedTestScnrioData", inputMap);
  }
  
  private List<Map<String, Object>> getDeletedTestCaseData(Map<String, Object> inputMap) throws Exception {
    return sqlSession.selectList("com.skd.sel.SelScnrioMngTest.getDeletedTestCaseData", inputMap);
  }
}