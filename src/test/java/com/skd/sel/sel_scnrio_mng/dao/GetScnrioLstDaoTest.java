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

import com.skd.sel.sel_scnrio_mng.dao.GetScnrioLstDao;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:/conf/root-context.xml", "classpath:/conf/kokodog-servlet.xml"})
public class GetScnrioLstDaoTest {
  @Autowired
  private GetScnrioLstDao getScnrioLstDao;
  
  @Autowired
  private SqlSession sqlSession;

  @Test(timeout=1000)
  @Transactional
  @Rollback(true)
  public void testGetScnrioLst() throws Exception {
    testGetScnrioLstWithTxt();
    testGetScnrioLstWithoutTxt();
  }
  
  public void testGetScnrioLstWithTxt() throws Exception {
    int scnrioNum = 0;
    int i = 0;
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
    insertTempScnrioData(testInputMap);
    scnrioNum = ((Long)getInsertedTempScnrioData().get("scnrio_num")).intValue();
    testInputMap.clear();
    testInputMap.put("sch_txt", "시나리오");
    outputList = getScnrioLstDao.getScnrioLst(testInputMap);
    for (i = 0; i < outputList.size(); i++) {
      if (((Long)outputList.get(i).get("scnrio_num")).intValue() == scnrioNum) {
        Assert.assertEquals((String)outputList.get(i).get("scnrio_nm"), "테스트 시나리오");
        Assert.assertEquals((String)outputList.get(i).get("scnrio_desc"), "테스트 시나리오");
        break;
      }
    }
    if (i == outputList.size()) {
      Assert.fail("데이터가 정상적으로 조회되지 않았습니다.");
    }
  }
  
  public void testGetScnrioLstWithoutTxt() throws Exception {
    int scnrioNum = 0;
    int i = 0;
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
    insertTempScnrioData(testInputMap);
    scnrioNum = ((Long)getInsertedTempScnrioData().get("scnrio_num")).intValue();
    testInputMap.clear();
    testInputMap.put("sch_txt", null);
    outputList = getScnrioLstDao.getScnrioLst(testInputMap);
    for (i = 0; i < outputList.size(); i++) {
      if (((Long)outputList.get(i).get("scnrio_num")).intValue() == scnrioNum) {
        Assert.assertEquals((String)outputList.get(i).get("scnrio_nm"), "테스트 시나리오");
        Assert.assertEquals((String)outputList.get(i).get("scnrio_desc"), "테스트 시나리오");
        break;
      }
    }
    if (i == outputList.size()) {
      Assert.fail("데이터가 정상적으로 조회되지 않았습니다.");
    }
  }
  
  private void insertTempScnrioData(Map<String, Object> testInputMap) throws Exception {
    sqlSession.insert("com.skd.sel.SelScnrioMngTest.insertTempScnrioData", testInputMap);
  }

  private Map<String, Object> getInsertedTempScnrioData() throws Exception {
    return sqlSession.selectOne("com.skd.sel.SelScnrioMngTest.getInsertedTempScnrioData");
  }
}