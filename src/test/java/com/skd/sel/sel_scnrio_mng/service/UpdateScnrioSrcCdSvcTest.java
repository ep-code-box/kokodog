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

import com.skd.sel.sel_scnrio_mng.service.UpdateScnrioSrcCdSvc;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:/conf/root-context.xml", "classpath:/conf/kokodog-servlet.xml"})
public class UpdateScnrioSrcCdSvcTest {
  @Autowired
  private UpdateScnrioSrcCdSvc updateScnrioSrcCdSvc;
  
  @Autowired
  private SqlSession sqlSession;

  @Test(timeout=1000)
  @Transactional
  @Rollback(true)
  public void testUpdateScnrioSrcCd() throws Exception {
    int scnrioNum = 0;
    Map<String, Object> testInputMap = new HashMap<String, Object>();
    Map<String, Object> outputMap = null;
    List<Map<String, Object>> outputList = null;
    Calendar staDtm = GregorianCalendar.getInstance();
    Calendar endDtm = GregorianCalendar.getInstance();
    staDtm.add(Calendar.DATE, -1);
    testInputMap.put("scnrio_nm", "테스트 시나리오");
    testInputMap.put("op_typ_num", 1);
    testInputMap.put("scnrio_desc", "테스트 시나리오");
    testInputMap.put("user_num", 1);
    testInputMap.put("seq_num", 1);
    testInputMap.put("src_cd", "test python source code ${svc_num}, ${temp}, ${login_id}, ${add_test}");
    testInputMap.put("eff_sta_dtm", new Date(staDtm.getTimeInMillis()));
    insertTempScnrioData(testInputMap);
    scnrioNum = ((Long)getInsertedTempScnrioData().get("scnrio_num")).intValue();
    testInputMap.clear();
    testInputMap.put("scnrio_num", scnrioNum);
    testInputMap.put("case_num", 1);
    testInputMap.put("user_num", 1);
    testInputMap.put("case_nm", "테스트 케이스");
    testInputMap.put("case_desc", "테스트 케이스");
    testInputMap.put("eff_sta_dtm", new Date(staDtm.getTimeInMillis()));
    insertTestCaseData(testInputMap);
    testInputMap.clear();
    testInputMap.put("scnrio_num", scnrioNum);
    testInputMap.put("input_num", 1);
    testInputMap.put("input_nm", "svc_num");
    testInputMap.put("user_num", 1);
    testInputMap.put("input_desc", "서비스번호");
    testInputMap.put("eff_sta_dtm", new Date(staDtm.getTimeInMillis()));
    insertTestInputData(testInputMap);
    testInputMap.clear();
    testInputMap.put("scnrio_num", scnrioNum);
    testInputMap.put("input_num", 2);
    testInputMap.put("input_nm", "temp");
    testInputMap.put("user_num", 1);
    testInputMap.put("input_desc", "임시");
    testInputMap.put("eff_sta_dtm", new Date(staDtm.getTimeInMillis()));
    insertTestInputData(testInputMap);
    testInputMap.clear();
    testInputMap.put("scnrio_num", scnrioNum);
    testInputMap.put("input_num", 3);
    testInputMap.put("input_nm", "login_id");
    testInputMap.put("user_num", 1);
    testInputMap.put("input_desc", "로그인ID");
    testInputMap.put("eff_sta_dtm", new Date(staDtm.getTimeInMillis()));
    insertTestInputData(testInputMap);
    testInputMap.clear();
    testInputMap.put("scnrio_num", scnrioNum);
    testInputMap.put("input_num", 4);
    testInputMap.put("input_nm", "add_test");
    testInputMap.put("user_num", 1);
    testInputMap.put("input_desc", "추가테스트");
    testInputMap.put("eff_sta_dtm", new Date(staDtm.getTimeInMillis()));
    insertTestInputData(testInputMap);
    testInputMap.clear();
    testInputMap.put("scnrio_num", scnrioNum);
    testInputMap.put("case_num", 1);
    testInputMap.put("input_num", 1);
    testInputMap.put("user_num", 1);
    testInputMap.put("input_val", "01026189758");
    testInputMap.put("eff_sta_dtm", new Date(staDtm.getTimeInMillis()));
    insertTestCaseInputData(testInputMap);
    testInputMap.clear();
    testInputMap.put("scnrio_num", scnrioNum);
    testInputMap.put("case_num", 1);
    testInputMap.put("input_num", 2);
    testInputMap.put("user_num", 1);
    testInputMap.put("input_val", "임시");
    testInputMap.put("eff_sta_dtm", new Date(staDtm.getTimeInMillis()));
    insertTestCaseInputData(testInputMap);
    testInputMap.clear();
    testInputMap.put("scnrio_num", scnrioNum);
    testInputMap.put("case_num", 1);
    testInputMap.put("input_num", 3);
    testInputMap.put("user_num", 1);
    testInputMap.put("input_val", "UK076");
    testInputMap.put("eff_sta_dtm", new Date(staDtm.getTimeInMillis()));
    insertTestCaseInputData(testInputMap);
    testInputMap.clear();
    testInputMap.put("scnrio_num", scnrioNum);
    testInputMap.put("case_num", 1);
    testInputMap.put("input_num", 4);
    testInputMap.put("user_num", 1);
    testInputMap.put("input_val", "추가변수");
    testInputMap.put("eff_sta_dtm", new Date(staDtm.getTimeInMillis()));
    insertTestCaseInputData(testInputMap);
    testInputMap.clear();
    testInputMap.put("scnrio_num", scnrioNum);
    testInputMap.put("src_cd", "test python source code ${login_id}, ${temp}, ${svc_num}");
    updateScnrioSrcCdSvc.updateScnrioSrcCd(testInputMap);
    testInputMap.clear();
    testInputMap.put("scnrio_num", scnrioNum);
    outputMap = getSrcCdWithSrcCd(testInputMap);
    Assert.assertEquals("test python source code ${login_id}, ${temp}, ${svc_num}", outputMap.get("src_cd"));
    testInputMap.clear();
    testInputMap.put("scnrio_num", scnrioNum);
    outputList = getTestInputData(testInputMap);
    Assert.assertEquals(3, outputList.size());
    for (int i = 0; i < outputList.size(); i++) {
      if (((Long)outputList.get(i).get("input_num")).intValue() == 1) {
        Assert.assertEquals(outputList.get(i).get("input_nm"), "login_id");
        Assert.assertEquals(outputList.get(i).get("input_desc"), "로그인ID");
      } else if (((Long)outputList.get(i).get("input_num")).intValue() == 2) {
        Assert.assertEquals(outputList.get(i).get("input_nm"), "temp");
        Assert.assertEquals(outputList.get(i).get("input_desc"), "임시");
        Assert.assertEquals(((Date)outputList.get(i).get("eff_sta_dtm")).getTime() / 1000L, (new Date(staDtm.getTimeInMillis())).getTime() / 1000L);
      } else if (((Long)outputList.get(i).get("input_num")).intValue() == 3) {
        Assert.assertEquals(outputList.get(i).get("input_nm"), "svc_num");
        Assert.assertEquals(outputList.get(i).get("input_desc"), "서비스번호");
      } else {
        Assert.fail("예상치 못한 input_num 값이 들어가 있습니다.[" + ((Long)outputList.get(i).get("input_num")).intValue() + "]");
      }
    }
    testInputMap.clear();
    testInputMap.put("scnrio_num", scnrioNum);
    testInputMap.put("case_num", 1);
    outputList = getTestCaseInputData(testInputMap);
    Assert.assertEquals(3, outputList.size());
    for (int i = 0; i < outputList.size(); i++) {
      if (((Long)outputList.get(i).get("input_num")).intValue() == 1) {
        Assert.assertEquals(outputList.get(i).get("input_val"), "UK076");
      } else if (((Long)outputList.get(i).get("input_num")).intValue() == 2) {
        Assert.assertEquals(outputList.get(i).get("input_val"), "임시");
        Assert.assertEquals(((Date)outputList.get(i).get("eff_sta_dtm")).getTime() / 1000L, (new Date(staDtm.getTimeInMillis())).getTime() / 1000L);
      } else if (((Long)outputList.get(i).get("input_num")).intValue() == 3) {
        Assert.assertEquals(outputList.get(i).get("input_val"), "01026189758");
      } else {
        Assert.fail("예상치 못한 input_num 값이 들어가 있습니다.[" + ((Long)outputList.get(i).get("input_num")).intValue() + "]");
      }
    }
    testInputMap.clear();
    testInputMap.put("scnrio_num", 3);
    testInputMap.put("src_cd", "test source code by python\n\n1st variable : ${svc_num}\n2nd variable : ${tmp}\n3rd variable : ${login_id}");
    updateScnrioSrcCdSvc.updateScnrioSrcCd(testInputMap);    
  }
  
  private void insertTempScnrioData(Map<String, Object> inputMap) throws Exception {
    sqlSession.insert("com.skd.sel.SelScnrioMngTest.insertTempScnrioData", inputMap);
  }
  
  private Map<String, Object> getInsertedTempScnrioData() throws Exception {
    return sqlSession.selectOne("com.skd.sel.SelScnrioMngTest.getInsertedTempScnrioData");    
  }

  private void insertTestCaseData(Map<String, Object> inputMap) throws Exception {
    sqlSession.insert("com.skd.sel.SelScnrioMngTest.insertTestCaseData", inputMap);
  }

  private void insertTestInputData(Map<String, Object> inputMap) throws Exception {
    sqlSession.insert("com.skd.sel.SelScnrioMngTest.insertTestInputData", inputMap);
  }

  private void insertTestCaseInputData(Map<String, Object> inputMap) throws Exception {
    sqlSession.insert("com.skd.sel.SelScnrioMngTest.insertTestCaseInputData", inputMap);
  }
  
  private Map<String, Object> getSrcCdWithSrcCd(Map<String, Object> inputMap) throws Exception {
    return sqlSession.selectOne("com.skd.sel.SelScnrioMngTest.getSrcCdWithSrcCd", inputMap);
  }
  
  private List<Map<String, Object>> getTestInputData(Map<String, Object> inputMap) throws Exception {
    return sqlSession.selectList("com.skd.sel.SelScnrioMngTest.getTestInputData", inputMap);
  }
  
  private List<Map<String, Object>> getTestCaseInputData(Map<String, Object> inputMap) throws Exception {
    return sqlSession.selectList("com.skd.sel.SelScnrioMngTest.getTestCaseInputData", inputMap);    
  }
}