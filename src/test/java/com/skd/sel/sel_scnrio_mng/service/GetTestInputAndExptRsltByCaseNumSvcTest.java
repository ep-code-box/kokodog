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

import com.skd.sel.sel_scnrio_mng.service.GetTestInputAndExptRsltByCaseNumSvc;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:/conf/root-context.xml", "classpath:/conf/kokodog-servlet.xml"})
public class GetTestInputAndExptRsltByCaseNumSvcTest {
  @Autowired
  private GetTestInputAndExptRsltByCaseNumSvc getTestInputAndExptRsltByCaseNumSvc;
  
  @Autowired
  private SqlSession sqlSession;

  @Test(timeout=1000)
  @Transactional
  @Rollback(true)
  public void testGetTestCaseInfoByScrnioNum() throws Exception {
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
    testInputMap.put("src_cd", "python source code example");
    testInputMap.put("eff_sta_dtm", new Date(staDtm.getTimeInMillis()));
    insertTempScnrioData(testInputMap);
    scnrioNum = ((Long)getInsertedTempScnrioData().get("scnrio_num")).intValue();
    testInputMap.clear();
    testInputMap.put("scnrio_num", scnrioNum);
    testInputMap.put("user_num", 0);
    testInputMap.put("case_nm", "테스트 케이스");
    testInputMap.put("case_desc", "테스트 케이스");
    testInputMap.put("eff_sta_dtm", new Date(staDtm.getTimeInMillis()));
    insertTestCaseData(testInputMap);
    testInputMap.clear();
    testInputMap.put("scnrio_num", scnrioNum);
    testInputMap.put("input_num", 1);
    testInputMap.put("user_num", 0);
    testInputMap.put("input_nm", "테스트 입력");
    testInputMap.put("input_desc", "테스트 입력");
    testInputMap.put("eff_sta_dtm", new Date(staDtm.getTimeInMillis()));
    insertTestInputData(testInputMap);
    testInputMap.clear();
    testInputMap.put("scnrio_num", scnrioNum);
    testInputMap.put("case_num", 1);
    testInputMap.put("input_num", 1);
    testInputMap.put("user_num", 0);
    testInputMap.put("input_val", "테스트 입력 값");
    testInputMap.put("eff_sta_dtm", new Date(staDtm.getTimeInMillis()));
    insertTestCaseInputData(testInputMap);
    testInputMap.clear();
    testInputMap.put("scnrio_num", scnrioNum);
    testInputMap.put("case_num", 1);
    testInputMap.put("test_step_num", 1);
    testInputMap.put("user_num", 0);
    testInputMap.put("rslt_strd", "테스트 결과 기준");
    testInputMap.put("judg_typ_cd", 2);
    testInputMap.put("eff_sta_dtm", new Date(staDtm.getTimeInMillis()));
    insertTestExptRsltData(testInputMap);
    testInputMap.clear();
    testInputMap.put("scnrio_num", scnrioNum);
    testInputMap.put("case_num", 1);
    outputMap = getTestInputAndExptRsltByCaseNumSvc.getTestInputAndExptRsltByCaseNum(testInputMap);
    if (outputMap.get("input") == null) {
      Assert.fail("input 데이터가 존재하지 않습니다.");
    }
    if (((List<Map<String, Object>>)outputMap.get("input")).size() != 1) {
      Assert.fail("Map 내 input 값의 size가 예상했던 1이 아닙니다.[" + ((List<Map<String, Object>>)outputMap.get("input")).size() + "]");
    }
    Assert.assertEquals(((List<Map<String, Object>>)outputMap.get("input")).get(0).get("input_num"), 1L);
    Assert.assertEquals(((List<Map<String, Object>>)outputMap.get("input")).get(0).get("input_nm"), "테스트 입력");
    Assert.assertEquals(((List<Map<String, Object>>)outputMap.get("input")).get(0).get("input_val"), "테스트 입력 값");
    if (outputMap.get("expt_rslt") == null) {
      Assert.fail("expt_rslt 데이터가 존재하지 않습니다.");
    }
    if (((List<Map<String, Object>>)outputMap.get("expt_rslt")).size() != 1) {
      Assert.fail("Map 내 expt_rslt 값의 size가 예상했던 1이 아닙니다.[" + ((List<Map<String, Object>>)outputMap.get("expt_rslt")).size() + "]");
    }
    Assert.assertEquals(((List<Map<String, Object>>)outputMap.get("expt_rslt")).get(0).get("test_step_num"), 1L);
    Assert.assertEquals(((List<Map<String, Object>>)outputMap.get("expt_rslt")).get(0).get("rslt_strd"), "테스트 결과 기준");
    Assert.assertEquals(((List<Map<String, Object>>)outputMap.get("expt_rslt")).get(0).get("judg_typ_cd"), 2);
    Assert.assertEquals(((List<Map<String, Object>>)outputMap.get("expt_rslt")).get(0).get("judg_typ_nm"), "비정상 리턴");
  }
  
  private void insertTempScnrioData(Map<String, Object> testInputMap) throws Exception {
    sqlSession.insert("com.skd.sel.SelScnrioMngTest.insertTempScnrioData", testInputMap);
  }

  private Map<String, Object> getInsertedTempScnrioData() throws Exception {
    return sqlSession.selectOne("com.skd.sel.SelScnrioMngTest.getInsertedTempScnrioData");
  }

  private void insertTestCaseData(Map<String, Object> inputMap) throws Exception {
    sqlSession.selectOne("com.skd.sel.SelScnrioMngTest.insertTestCaseData", inputMap);
  }

  private void insertTestInputData(Map<String, Object> inputMap) throws Exception {
    sqlSession.selectOne("com.skd.sel.SelScnrioMngTest.insertTestInputData", inputMap);
  }

  private void insertTestCaseInputData(Map<String, Object> inputMap) throws Exception {
    sqlSession.selectOne("com.skd.sel.SelScnrioMngTest.insertTestCaseInputData", inputMap);
  }

  private void insertTestExptRsltData(Map<String, Object> inputMap) throws Exception {
    sqlSession.selectOne("com.skd.sel.SelScnrioMngTest.insertTestExptRsltData", inputMap);
  }
}