package com.skd.sel.sel_scnrio_mng.dao.impl;

import java.util.List;
import java.util.Map;
import java.sql.SQLException;

import org.apache.ibatis.session.SqlSession;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.skd.sel.sel_scnrio_mng.dao.GetTestStepInfoDao;

@Repository("getTestStepInfoDao")
public class GetTestStepInfoDaoImpl implements GetTestStepInfoDao {
  private static Logger logger = LogManager.getLogger(GetTestStepInfoDaoImpl.class);

  @Autowired
  private SqlSession sqlSession;

  public List<Map<String, Object>> getTestStepInfo(Map<String, Object> inputMap) throws SQLException {
    logger.debug("============   Start method of GetTestStepInfoDaoImpl.getTestStepInfo   ============");
    logger.debug("Parameter inputMap[" + inputMap.toString() + "]");
    List<Map<String, Object>> outputList = null;
    outputList = sqlSession.selectList("com.skd.sel.sel_scnrio_mng.GetTestStepInfo.getTestStepInfo", inputMap);
    logger.debug("Output List[" + outputList.toString() + "]");
    logger.debug("============   End method of GetTestStepInfoDaoImpl.getTestStepInfo   ============");
    return outputList;    
  }
}