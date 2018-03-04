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

import com.skd.sel.sel_scnrio_mng.dao.GetTestStepInfoDao;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:/conf/root-context.xml", "classpath:/conf/kokodog-servlet.xml"})
public class GetTestStepInfoDaoTest {
  @Autowired
  private GetTestStepInfoDao getTestStepInfoDao;
  
  @Autowired
  private SqlSession sqlSession;

  @Test(timeout=1000)
  @Transactional
  @Rollback(false)
  public void testDelTestExptRsltByStepNum() throws Exception {
    testDelTestExptRsltByStepNumWithCaseNum();
    testDelTestExptRsltByStepNumWithoutCaseNum();
  }
  
  private void testDelTestExptRsltByStepNumWithCaseNum() throws Exception {
    int scnrioNum = 0;
    int caseNum = 0;
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
    testInputMap.put("scnrio_num", scnrioNum);
    caseNum = ((Long)getInsertedCaseData(testInputMap).get("case_num")).intValue();
    testInputMap.clear();
    testInputMap.put("user_num", 0);
    testInputMap.put("scnrio_num", scnrioNum);
    testInputMap.put("case_num", caseNum);
    testInputMap.put("test_step_num", 1);
    testInputMap.put("rslt_strd", "테스트1");
    testInputMap.put("judg_typ_cd", 1);
    testInputMap.put("eff_sta_dtm", new Date(staDtm.getTimeInMillis()));
    insertTempTestExptRslt(testInputMap);
    testInputMap.clear();
    testInputMap.put("user_num", 0);
    testInputMap.put("scnrio_num", scnrioNum);
    testInputMap.put("case_num", caseNum);
    testInputMap.put("test_step_num", 2);
    testInputMap.put("rslt_strd", "테스트2");
    testInputMap.put("judg_typ_cd", 2);
    testInputMap.put("eff_sta_dtm", new Date(staDtm.getTimeInMillis()));
    insertTempTestExptRslt(testInputMap);
    testInputMap.clear();
    testInputMap.put("scnrio_num", scnrioNum);
    testInputMap.put("case_num", caseNum);
    List<Map<String, Object>> outputList = getTestStepInfoDao.getTestStepInfo(testInputMap);
    Assert.assertEquals(2, outputList.size());
    for (int i = 0; i < outputList.size(); i++) {
      Assert.assertEquals("테스트 시나리오", outputList.get(i).get("scnrio_nm"));
      Assert.assertEquals("테스트 케이스", outputList.get(i).get("case_nm"));
      if (((Long)outputList.get(i).get("test_step_num")).intValue() == 1) {
        Assert.assertEquals("테스트1", outputList.get(i).get("rslt_strd"));
        Assert.assertEquals(1, outputList.get(i).get("judg_typ_cd"));
      } else if (((Long)outputList.get(i).get("test_step_num")).intValue() == 2) {
        Assert.assertEquals("테스트2", outputList.get(i).get("rslt_strd"));
        Assert.assertEquals(2, outputList.get(i).get("judg_typ_cd"));
      } else {
        Assert.fail("정상적인 test_step_num이 추출되지 않았습니다.");
      }
    }
  }
  
  private void testDelTestExptRsltByStepNumWithoutCaseNum() throws Exception {
    int scnrioNum = 0;
    int caseNum1 = 0;
    int caseNum2 = 0;
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
    testInputMap.put("case_nm", "테스트 케이스 1");
    testInputMap.put("case_desc", "테스트 케이스 1");
    testInputMap.put("eff_sta_dtm", new Date(staDtm.getTimeInMillis()));
    insertTempCaseData(testInputMap);
    testInputMap.clear();
    testInputMap.put("scnrio_num", scnrioNum);
    caseNum1 = ((Long)getInsertedCaseData(testInputMap).get("case_num")).intValue();
    testInputMap.clear();
    testInputMap.put("user_num", 0);
    testInputMap.put("scnrio_num", scnrioNum);
    testInputMap.put("case_nm", "테스트 케이스 2");
    testInputMap.put("case_desc", "테스트 케이스 2");
    testInputMap.put("eff_sta_dtm", new Date(staDtm.getTimeInMillis()));
    insertTempCaseData(testInputMap);
    testInputMap.clear();
    testInputMap.put("scnrio_num", scnrioNum);
    caseNum2 = ((Long)getInsertedCaseData(testInputMap).get("case_num")).intValue();
    testInputMap.clear();
    testInputMap.put("user_num", 0);
    testInputMap.put("scnrio_num", scnrioNum);
    testInputMap.put("case_num", caseNum1);
    testInputMap.put("test_step_num", 1);
    testInputMap.put("rslt_strd", "테스트1");
    testInputMap.put("judg_typ_cd", 1);
    testInputMap.put("eff_sta_dtm", new Date(staDtm.getTimeInMillis()));
    insertTempTestExptRslt(testInputMap);
    testInputMap.clear();
    testInputMap.put("user_num", 0);
    testInputMap.put("scnrio_num", scnrioNum);
    testInputMap.put("case_num", caseNum1);
    testInputMap.put("test_step_num", 2);
    testInputMap.put("rslt_strd", "테스트2");
    testInputMap.put("judg_typ_cd", 2);
    testInputMap.put("eff_sta_dtm", new Date(staDtm.getTimeInMillis()));
    insertTempTestExptRslt(testInputMap);
    testInputMap.clear();
    testInputMap.put("user_num", 0);
    testInputMap.put("scnrio_num", scnrioNum);
    testInputMap.put("case_num", caseNum2);
    testInputMap.put("test_step_num", 1);
    testInputMap.put("rslt_strd", "테스트3");
    testInputMap.put("judg_typ_cd", 3);
    testInputMap.put("eff_sta_dtm", new Date(staDtm.getTimeInMillis()));
    insertTempTestExptRslt(testInputMap);
    testInputMap.clear();
    testInputMap.put("user_num", 0);
    testInputMap.put("scnrio_num", scnrioNum);
    testInputMap.put("case_num", caseNum2);
    testInputMap.put("test_step_num", 2);
    testInputMap.put("rslt_strd", "테스트4");
    testInputMap.put("judg_typ_cd", 4);
    testInputMap.put("eff_sta_dtm", new Date(staDtm.getTimeInMillis()));
    insertTempTestExptRslt(testInputMap);
    testInputMap.clear();
    testInputMap.put("scnrio_num", scnrioNum);
    List<Map<String, Object>> outputList = getTestStepInfoDao.getTestStepInfo(testInputMap);
    Assert.assertEquals(4, outputList.size());
    for (int i = 0; i < outputList.size(); i++) {
      Assert.assertEquals("테스트 시나리오", outputList.get(i).get("scnrio_nm"));
      if (((Long)outputList.get(i).get("case_num")).intValue() == caseNum1) {
        Assert.assertEquals("테스트 케이스 1", outputList.get(i).get("case_nm"));
        if (((Long)outputList.get(i).get("test_step_num")).intValue() == 1) {
          Assert.assertEquals("테스트1", outputList.get(i).get("rslt_strd"));
          Assert.assertEquals(1, outputList.get(i).get("judg_typ_cd"));
        } else if (((Long)outputList.get(i).get("test_step_num")).intValue() == 2) {
          Assert.assertEquals("테스트2", outputList.get(i).get("rslt_strd"));
          Assert.assertEquals(2, outputList.get(i).get("judg_typ_cd"));
        } else {
          Assert.fail("정상적인 test_step_num이 추출되지 않았습니다.");
        }
      } else if (((Long)outputList.get(i).get("case_num")).intValue() == caseNum2) {
        Assert.assertEquals("테스트 케이스 2", outputList.get(i).get("case_nm"));
        if (((Long)outputList.get(i).get("test_step_num")).intValue() == 1) {
          Assert.assertEquals("테스트3", outputList.get(i).get("rslt_strd"));
          Assert.assertEquals(3, outputList.get(i).get("judg_typ_cd"));
        } else if (((Long)outputList.get(i).get("test_step_num")).intValue() == 2) {
          Assert.assertEquals("테스트4", outputList.get(i).get("rslt_strd"));
          Assert.assertEquals(4, outputList.get(i).get("judg_typ_cd"));
        } else {
          Assert.fail("정상적인 test_step_num이 추출되지 않았습니다.");
        }
      } else {
        Assert.fail("정상적인 case_num이 추출되지 않았습니다.");
      }
    }    
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
}