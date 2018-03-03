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

import com.skd.sel.sel_scnrio_mng.service.UpdateTestExptRsltSvc;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:/conf/root-context.xml", "classpath:/conf/kokodog-servlet.xml"})
public class UpdateTestExptRsltSvcTest {
  @Autowired
  private UpdateTestExptRsltSvc updateTestExptRsltSvc;
  
  @Autowired
  private SqlSession sqlSession;

  @Test(timeout=1000)
  @Transactional
  @Rollback(true)
  public void testUpdateTestExptRslt() throws Exception {
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
    testInputMap.put("rslt_strd", "테스트1");
    testInputMap.put("judg_typ_cd", 1);
    testInputMap.put("eff_sta_dtm", new Date(staDtm.getTimeInMillis()));
    insertTempTestExptRslt(testInputMap);
    testInputMap.clear();
    testInputMap.put("user_num", 0);
    testInputMap.put("scnrio_num", scnrioNum);
    testInputMap.put("case_num", 1);
    testInputMap.put("test_step_num", 2);
    testInputMap.put("rslt_strd", "테스트2");
    testInputMap.put("judg_typ_cd", 2);
    testInputMap.put("eff_sta_dtm", new Date(staDtm.getTimeInMillis()));
    insertTempTestExptRslt(testInputMap);
    testInputMap.clear();
    testInputMap.put("user_num", 0);
    testInputMap.put("scnrio_num", scnrioNum);
    testInputMap.put("case_num", 1);
    testInputMap.put("test_step_num", 3);
    testInputMap.put("rslt_strd", "테스트3");
    testInputMap.put("judg_typ_cd", 3);
    testInputMap.put("eff_sta_dtm", new Date(staDtm.getTimeInMillis()));
    insertTempTestExptRslt(testInputMap);
    testInputMap.clear();
    testInputMap.put("user_num", 0);
    testInputMap.put("scnrio_num", scnrioNum);
    testInputMap.put("case_num", 1);
    testInputMap.put("test_step_num", 4);
    testInputMap.put("rslt_strd", "테스트4");
    testInputMap.put("judg_typ_cd", 4);
    testInputMap.put("eff_sta_dtm", new Date(staDtm.getTimeInMillis()));
    insertTempTestExptRslt(testInputMap);
    List<Map<String, Object>> inputList = new ArrayList<Map<String, Object>>();
    Map<String, Object> tempInputMap = null;
    tempInputMap = new HashMap<String, Object>();
    tempInputMap.put("test_step_num", 2);
    tempInputMap.put("modify_typ", 3);
    inputList.add(tempInputMap);
    tempInputMap = new HashMap<String, Object>();
    tempInputMap.put("old_test_step_num", 4);
    tempInputMap.put("test_step_num", 5);
    tempInputMap.put("rslt_strd", "테스트5");
    tempInputMap.put("judg_typ_cd", 5);
    tempInputMap.put("modify_typ", 2);
    inputList.add(tempInputMap);
    tempInputMap = new HashMap<String, Object>();
    tempInputMap.put("test_step_num", 6);
    tempInputMap.put("rslt_strd", "테스트6");
    tempInputMap.put("judg_typ_cd", 6);
    tempInputMap.put("modify_typ", 1);
    inputList.add(tempInputMap);
    updateTestExptRsltSvc.updateTestExptRslt(scnrioNum, 1, inputList);
    testInputMap.clear();
    testInputMap.put("scnrio_num", scnrioNum);
    testInputMap.put("case_num", 1);
    List<Map<String, Object>> allInsertedTestExptRslt = getAllInsertedTestExptRslt(testInputMap);
    Assert.assertEquals(4, allInsertedTestExptRslt.size());
    for (int i = 0; i < allInsertedTestExptRslt.size(); i++) {
      if (((Long)allInsertedTestExptRslt.get(i).get("test_step_num")).intValue() == 1) {
        Assert.assertEquals("테스트1", allInsertedTestExptRslt.get(i).get("rslt_strd"));
        Assert.assertEquals(1, allInsertedTestExptRslt.get(i).get("judg_typ_cd"));
      } else if (((Long)allInsertedTestExptRslt.get(i).get("test_step_num")).intValue() == 3) {
        Assert.assertEquals("테스트3", allInsertedTestExptRslt.get(i).get("rslt_strd"));
        Assert.assertEquals(3, allInsertedTestExptRslt.get(i).get("judg_typ_cd"));
      } else if (((Long)allInsertedTestExptRslt.get(i).get("test_step_num")).intValue() == 5) {
        Assert.assertEquals("테스트5", allInsertedTestExptRslt.get(i).get("rslt_strd"));
        Assert.assertEquals(5, allInsertedTestExptRslt.get(i).get("judg_typ_cd"));
      } else if (((Long)allInsertedTestExptRslt.get(i).get("test_step_num")).intValue() == 6) {
        Assert.assertEquals("테스트6", allInsertedTestExptRslt.get(i).get("rslt_strd"));
        Assert.assertEquals(6, allInsertedTestExptRslt.get(i).get("judg_typ_cd"));
      } else {
        Assert.fail("정상적인 데이터가 검출되지 않았습니다. [" + allInsertedTestExptRslt.get(i) + "]");
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

  private List<Map<String, Object>> getAllInsertedTestExptRslt(Map<String, Object> inputMap) throws Exception {
    return sqlSession.selectList("com.skd.sel.SelScnrioMngTest.getAllInsertedTestExptRslt", inputMap);
  }
}