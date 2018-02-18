package com.skd.sel.sel_scnrio_mng.service;

import java.util.ArrayList;
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

import com.skd.sel.sel_scnrio_mng.service.UpdateTestCaseInputSvc;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:/conf/root-context.xml", "classpath:/conf/kokodog-servlet.xml"})
public class UpdateTestCaseInputSvcTest {
  @Autowired
  private UpdateTestCaseInputSvc updateTestCaseInputSvc;
  
  @Autowired
  private SqlSession sqlSession;

  @Test(timeout=1000)
  @Transactional
  @Rollback(true)
  public void testUpdateScnrioSrcCd() throws Exception {
    int scnrioNum = 0;
    Map<String, Object> testInputMap = new HashMap<String, Object>();
    Map<String, Object> outputMap = null;
    List<Map<String, Object>> inputList = new ArrayList<Map<String, Object>>();
    Calendar staDtm = GregorianCalendar.getInstance();
    Calendar endDtm = GregorianCalendar.getInstance();
    testInputMap.put("scnrio_num", 1);
    testInputMap.put("case_num", 2);
    Map<String, Object> testInputMapSub = new HashMap<String, Object>();
    testInputMapSub.put("input_num", 1);
    testInputMapSub.put("input_nm", "svc_num");
    testInputMapSub.put("input_val", "01026189758");
    inputList.add(testInputMapSub);
    testInputMapSub = new HashMap<String, Object>();
    testInputMapSub.put("input_num", 2);
    testInputMapSub.put("input_nm", "login_id");
    testInputMapSub.put("input_val", "UK076");
    inputList.add(testInputMapSub);
    testInputMapSub = new HashMap<String, Object>();
    testInputMapSub.put("input_num", 1);
    testInputMapSub.put("input_nm", "ctz_corp_num");
    testInputMapSub.put("input_val", "8306271178512");
    inputList.add(testInputMapSub);
    testInputMap.put("input", inputList);
    updateTestCaseInputSvc.updateTestCaseInput(testInputMap);    
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