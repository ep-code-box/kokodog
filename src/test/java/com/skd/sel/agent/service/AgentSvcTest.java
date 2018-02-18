package com.skd.sel.agent.service;

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

import com.skd.sel.agent.service.AgentSvc;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:/conf/root-context.xml", "classpath:/conf/kokodog-servlet.xml"})
public class AgentSvcTest {
  @Autowired
  private AgentSvc agentSvc;
  
  @Autowired
  private SqlSession sqlSession;

  @Test(timeout=1000)
  @Transactional
  @Rollback(true)
  public void testGetCurTestLst() throws Exception {
    testGetCurTestLstWithoutVariable();
    testGetCurTestLstWithVariable();
  }
  
  private void testGetCurTestLstWithoutVariable() throws Exception {
    int scnrioNum = 0;
    Map<String, Object> testInputMap = new HashMap<String, Object>();
    Map<String, Object> outputMap = null;
    List<Object> outputList = null;
    int dataNum = 0;
    String testReturnString = null;
    Calendar staDtm = GregorianCalendar.getInstance();
    Calendar endDtm = GregorianCalendar.getInstance();
    testInputMap.put("user_num", 0);
    testInputMap.put("scnrio_nm", "테스트 시나리오");
    testInputMap.put("op_typ_num", 1);
    testInputMap.put("scnrio_desc", "테스트 시나리오");
    testInputMap.put("seq_num", 1);
    testInputMap.put("src_cd", "python source code example");
    testInputMap.put("eff_sta_dtm", new Date(staDtm.getTimeInMillis()));
    insertTestAgentData(testInputMap);
    scnrioNum = ((Long)getInsertedTestAgentData().get("scnrio_num")).intValue();
    testInputMap.clear();
    testInputMap.put("scnrio_num", scnrioNum);
    testInputMap.put("user_num", 0);
    testInputMap.put("sta_dt", new Date(GregorianCalendar.getInstance().getTimeInMillis()));
    testInputMap.put("end_dt", new Date(GregorianCalendar.getInstance().getTimeInMillis()));
    testInputMap.put("sta_tm", new Date(staDtm.getTimeInMillis()));
    testInputMap.put("end_tm", new Date(endDtm.getTimeInMillis()));
    testInputMap.put("exec_cycl", 7);
    testInputMap.put("wkday_exec_yn", "Y");
    testInputMap.put("hkday_exec_yn", "Y");
    testInputMap.put("eff_sta_dtm", new Date(staDtm.getTimeInMillis()));
    insertTestPlanData(testInputMap);
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
    testInputMap.put("input_nm", "svc_num");
    testInputMap.put("input_desc", "서비스번호");
    testInputMap.put("eff_sta_dtm", new Date(staDtm.getTimeInMillis()));
    insertTestInputData(testInputMap);
    testInputMap.clear();
    testInputMap.put("scnrio_num", scnrioNum);
    testInputMap.put("case_num", 1);
    testInputMap.put("input_num", 1);
    testInputMap.put("user_num", 0);
    testInputMap.put("input_val", "01026189758");
    testInputMap.put("eff_sta_dtm", new Date(staDtm.getTimeInMillis()));
    insertTestCaseInputData(testInputMap);
    outputList = agentSvc.getCurTestLst();
    for (int i = 0; i < outputList.size(); i++) {
      if (((Integer)((Map<String, Object>)(outputList.get(i))).get("scnrio_num")).intValue() == scnrioNum) {
        if (dataNum == 1) {
          Assert.fail("해당 case는 1개만 생성되어야 하나 2개 이상의 데이터가 발생했습니다.");
          return;
        }
        dataNum++;
        testReturnString = (String)((((Map<String, Object>)((Map<String, Object>)outputList.get(i)).get("test_case")).get("src_cd")));
      }
    }
    if (dataNum != 1) {
      Assert.fail("정상적인 데이터가 추출되지 않았습니다.");
      return;
    } else {
      Assert.assertEquals(testReturnString, "python source code example");      
    }
  }
  
  private void testGetCurTestLstWithVariable() throws Exception {
    int scnrioNum = 0;
    Map<String, Object> testInputMap = new HashMap<String, Object>();
    Map<String, Object> outputMap = null;
    List<Object> outputList = null;
    int dataNum = 0;
    String testReturnString = null;
    Calendar staDtm = GregorianCalendar.getInstance();
    Calendar endDtm = GregorianCalendar.getInstance();
    testInputMap.put("user_num", 0);
    testInputMap.put("scnrio_nm", "테스트 시나리오");
    testInputMap.put("op_typ_num", 1);
    testInputMap.put("scnrio_desc", "테스트 시나리오");
    testInputMap.put("seq_num", 1);
    testInputMap.put("src_cd", "python source code example ${svc_num}, ${temp_num} \\${svc_num}");
    testInputMap.put("eff_sta_dtm", new Date(staDtm.getTimeInMillis()));
    insertTestAgentData(testInputMap);
    scnrioNum = ((Long)getInsertedTestAgentData().get("scnrio_num")).intValue();
    testInputMap.clear();
    testInputMap.put("scnrio_num", scnrioNum);
    testInputMap.put("user_num", 0);
    testInputMap.put("sta_dt", new Date(GregorianCalendar.getInstance().getTimeInMillis()));
    testInputMap.put("end_dt", new Date(GregorianCalendar.getInstance().getTimeInMillis()));
    testInputMap.put("sta_tm", new Date(staDtm.getTimeInMillis()));
    testInputMap.put("end_tm", new Date(endDtm.getTimeInMillis()));
    testInputMap.put("exec_cycl", 7);
    testInputMap.put("wkday_exec_yn", "Y");
    testInputMap.put("hkday_exec_yn", "Y");
    testInputMap.put("eff_sta_dtm", new Date(staDtm.getTimeInMillis()));
    insertTestPlanData(testInputMap);
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
    testInputMap.put("input_nm", "svc_num");
    testInputMap.put("input_desc", "서비스번호");
    testInputMap.put("eff_sta_dtm", new Date(staDtm.getTimeInMillis()));
    insertTestInputData(testInputMap);
    testInputMap.clear();
    testInputMap.put("scnrio_num", scnrioNum);
    testInputMap.put("case_num", 1);
    testInputMap.put("input_num", 1);
    testInputMap.put("user_num", 0);
    testInputMap.put("input_val", "01026189758");
    testInputMap.put("eff_sta_dtm", new Date(staDtm.getTimeInMillis()));
    insertTestCaseInputData(testInputMap);
    outputList = agentSvc.getCurTestLst();
    for (int i = 0; i < outputList.size(); i++) {
      if (((Integer)((Map<String, Object>)(outputList.get(i))).get("scnrio_num")).intValue() == scnrioNum) {
        if (dataNum == 1) {
          Assert.fail("해당 case는 1개만 생성되어야 하나 2개 이상의 데이터가 발생했습니다.");
          return;
        }
        dataNum++;
        testReturnString = (String)((((Map<String, Object>)((Map<String, Object>)outputList.get(i)).get("test_case")).get("src_cd")));
      }
    }
    if (dataNum != 1) {
      Assert.fail("정상적인 데이터가 추출되지 않았습니다.");
      return;
    } else {
      Assert.assertEquals(testReturnString, "python source code example 01026189758,  \\${svc_num}");      
    }
  }
  
  private void insertTestAgentData(Map<String, Object> testInputMap) throws Exception {
    sqlSession.insert("com.skd.sel.agentTest.insertTestScnrioData", testInputMap);
  }

  private Map<String, Object> getInsertedTestAgentData() throws Exception {
    return sqlSession.selectOne("com.skd.sel.agentTest.getInsertedTestAgentData");
  }

  private void insertTestPlanData(Map<String, Object> inputMap) throws Exception {
    sqlSession.selectOne("com.skd.sel.agentTest.insertTestPlanData", inputMap);
  }

  private void insertTestCaseData(Map<String, Object> inputMap) throws Exception {
    sqlSession.selectOne("com.skd.sel.agentTest.insertTestCaseData", inputMap);
  }

  private void insertTestInputData(Map<String, Object> inputMap) throws Exception {
    sqlSession.selectOne("com.skd.sel.agentTest.insertTestInputData", inputMap);
  }

  private void insertTestCaseInputData(Map<String, Object> inputMap) throws Exception {
    sqlSession.selectOne("com.skd.sel.agentTest.insertTestCaseInputData", inputMap);
  }
}