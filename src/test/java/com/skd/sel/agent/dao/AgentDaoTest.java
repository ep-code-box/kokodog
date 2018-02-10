package com.skd.sel.agent.dao;

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

import com.skd.sel.agent.dao.AgentDao;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:/conf/root-context.xml", "classpath:/conf/kokodog-servlet.xml"})
public class AgentDaoTest {
  @Autowired
  private AgentDao agentDao;
  
  @Autowired
  private SqlSession sqlSession;

  @Test(timeout=1000)
  @Transactional
  @Rollback(true)
  public void getAllPlannedScnrioTest() throws Exception {
    getAllPlannedScnrioWithStaDtEndDtExecCyclTest();
    getAllPlannedScnrioWithStaDtEndDtTest();
    getAllPlannedScnrioWithEndDtExecCyclTest();
  }
  
  @Test(timeout=100)
  @Transactional
  @Rollback(true)
  public void getScnrioInfoTest() throws Exception {
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
    insertTestAgentData(testInputMap);
    scnrioNum = ((Long)getInsertedTestAgentData().get("scnrio_num")).intValue();
    testInputMap.clear();
    testInputMap.put("scnrio_num", scnrioNum);
    outputMap = agentDao.getScnrioInfo(testInputMap);
    Assert.assertTrue("python source code example".equals(outputMap.get("src_cd")));
  }
  
  @Test(timeout=100)
  @Transactional
  @Rollback(true)
  public void getCaseWithScnrioTest() throws Exception {
    int scnrioNum = 0;
    Map<String, Object> testInputMap = new HashMap<String, Object>();
    List<Map<String, Object>> outputList = null;
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
    testInputMap.put("case_nm", "테스트 케이스");
    testInputMap.put("case_desc", "테스트 케이스");
    testInputMap.put("eff_sta_dtm", new Date(staDtm.getTimeInMillis()));
    insertTestCaseData(testInputMap);
    testInputMap.clear();
    testInputMap.put("scnrio_num", scnrioNum);
    outputList = agentDao.getCaseWithScnrio(testInputMap);
    Assert.assertEquals(outputList.size(), 1);
    Assert.assertEquals(((Long)outputList.get(0).get("case_num")).intValue(), 1);
  }

  @Test(timeout=500)
  @Transactional
  @Rollback(true)
  public void getInputWithScnrioAndCaseTest() throws Exception {
    int scnrioNum = 0;
    Map<String, Object> testInputMap = new HashMap<String, Object>();
    List<Map<String, Object>> outputList = null;
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
    testInputMap.put("scnrio_num", scnrioNum);
    testInputMap.put("case_num", 1);
    outputList = agentDao.getInputWithScnrioAndCase(testInputMap);
    Assert.assertEquals(outputList.size(), 1);
    Assert.assertEquals(outputList.get(0).get("input_nm"), "테스트 입력");
    Assert.assertEquals(outputList.get(0).get("input_val"), "테스트 입력 값");
  }

  private void getAllPlannedScnrioWithStaDtEndDtExecCyclTest() throws Exception {
    int scnrioNum = 0;
    Map<String, Object> testInputMap = new HashMap<String, Object>();
    List<Map<String, Object>> outputList = null;
    Calendar staDtm = GregorianCalendar.getInstance();
    Calendar endDtm = GregorianCalendar.getInstance();
    staDtm.setTimeInMillis(staDtm.getTimeInMillis() - (10L * 60L * 1000L));
    endDtm.setTimeInMillis(endDtm.getTimeInMillis() + (10L * 60L * 1000L));
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
    outputList = agentDao.getAllPlannedScnrio();
    Date[] retDate = new Date[2];
    int dateNum = 0;
    for (int i = 0; i < outputList.size(); i++) {
      if (((Long)outputList.get(i).get("scnrio_num")).intValue() == scnrioNum) {
        if (dateNum == 2) {
          Assert.fail("2개만 생성되어야 할 데이터가 3개 이상 생성되었습니다.");
          return;
        }
        retDate[dateNum] = (Date)outputList.get(i).get("exec_expt_dtm");
        dateNum++;
      }
    }
    if (dateNum != 2) {
      Assert.fail("2개 생성되어야 할 데이터가 맞게 생성되지 않았습니다.[" + dateNum + "]");
      return;
    }
    if ((retDate[0].getTime() / 1000L) != (staDtm.getTimeInMillis() / 1000L) && (retDate[1].getTime() / 1000L) != (staDtm.getTimeInMillis() / 1000L)) {
      Assert.fail("정상적인 시간 예상값이 리턴되지 않았습니다.[" + retDate[0].getTime() + "][" + retDate[1].getTime() + "][" + staDtm.getTimeInMillis() + "]");
      return;
    }
    if ((retDate[0].getTime() / 1000L) != ((staDtm.getTimeInMillis() + (7L * 60L * 1000L)) / 1000L) && (retDate[1].getTime() / 1000L) != ((staDtm.getTimeInMillis() + (7L * 60L * 1000L)) / 1000L)) {
      Assert.fail("정상적인 시간 예상값이 리턴되지 않았습니다.[" + retDate[0].getTime() + "][" + retDate[1].getTime() + "][" + (staDtm.getTimeInMillis() + (4L * 60L * 1000L)) + "]");
      return;
    }
    Assert.assertTrue(true);
  }
  
  private void getAllPlannedScnrioWithStaDtEndDtTest() throws Exception {
    int scnrioNum = 0;
    Map<String, Object> testInputMap = new HashMap<String, Object>();
    List<Map<String, Object>> outputList = null;
    Calendar staDtm = GregorianCalendar.getInstance();
    Calendar endDtm = GregorianCalendar.getInstance();
    staDtm.setTimeInMillis(staDtm.getTimeInMillis() - (10L * 60L * 1000L));
    endDtm.setTimeInMillis(endDtm.getTimeInMillis() + (10L * 60L * 1000L));
    testInputMap.put("user_num", 0);
    testInputMap.put("scnrio_nm", "테스트 시타리오");
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
    testInputMap.put("exec_cycl", null);
    testInputMap.put("wkday_exec_yn", "Y");
    testInputMap.put("hkday_exec_yn", "Y");
    testInputMap.put("eff_sta_dtm", new Date(staDtm.getTimeInMillis()));
    insertTestPlanData(testInputMap);
    outputList = agentDao.getAllPlannedScnrio();
    Date[] retDate = new Date[1];
    int dateNum = 0;
    for (int i = 0; i < outputList.size(); i++) {
      if (((Long)outputList.get(i).get("scnrio_num")).intValue() == scnrioNum) {
        if (dateNum == 1) {
          Assert.fail("1개만 생성되어야 할 데이터가 2개 이상 생성되었습니다.");
          return;
        }
        retDate[dateNum] = (Date)outputList.get(i).get("exec_expt_dtm");
        dateNum++;
      }
    }
    if (dateNum != 1) {
      Assert.fail("1개 생성되어야 할 데이터가 맞게 생성되지 않았습니다.[" + dateNum + "]");
      return;
    }
    if ((retDate[0].getTime() / 1000L) != (staDtm.getTimeInMillis() / 1000L)) {
      Assert.fail("정상적인 시간 예상값이 리턴되지 않았습니다.[" + retDate[0].getTime() + "][" + retDate[1].getTime() + "][" + staDtm.getTimeInMillis() + "]");
      return;
    }
  }
  
  private void getAllPlannedScnrioWithEndDtExecCyclTest() throws Exception {
    int scnrioNum = 0;
    Map<String, Object> testInputMap = new HashMap<String, Object>();
    List<Map<String, Object>> outputList = null;
    Calendar staDtm = GregorianCalendar.getInstance();
    Calendar endDtm = GregorianCalendar.getInstance();
    staDtm.set(staDtm.get(Calendar.YEAR), staDtm.get(Calendar.MONTH), staDtm.get(Calendar.DATE), 0, 0, 0);
    staDtm.set(Calendar.MILLISECOND, 0);
    endDtm.setTimeInMillis(endDtm.getTimeInMillis() + (10L * 60L * 1000L));
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
    testInputMap.put("sta_dt", new Date(staDtm.getTimeInMillis()));
    testInputMap.put("end_dt", new Date(GregorianCalendar.getInstance().getTimeInMillis()));
    testInputMap.put("sta_tm", null);
    testInputMap.put("end_tm", new Date(endDtm.getTimeInMillis()));
    testInputMap.put("exec_cycl", null);
    testInputMap.put("wkday_exec_yn", "Y");
    testInputMap.put("hkday_exec_yn", "Y");
    testInputMap.put("eff_sta_dtm", new Date(staDtm.getTimeInMillis()));
    insertTestPlanData(testInputMap);
    outputList = agentDao.getAllPlannedScnrio();
    Date[] retDate = new Date[1];
    int dateNum = 0;
    for (int i = 0; i < outputList.size(); i++) {
      if (((Long)outputList.get(i).get("scnrio_num")).intValue() == scnrioNum) {
        if (dateNum == 1) {
          Assert.fail("1개만 생성되어야 할 데이터가 2개 이상 생성되었습니다.");
          return;
        }
        retDate[dateNum] = (Date)outputList.get(i).get("exec_expt_dtm");
        dateNum++;
      }
    }
    if (dateNum != 1) {
      Assert.fail("1개 생성되어야 할 데이터가 맞게 생성되지 않았습니다.[" + dateNum + "]");
      return;
    }
    if ((retDate[0].getTime() / 1000L) != (staDtm.getTimeInMillis() / 1000L)) {
      Assert.fail("정상적인 시간 예상값이 리턴되지 않았습니다.[" + retDate[0] + "][" + staDtm + "]");
      return;
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