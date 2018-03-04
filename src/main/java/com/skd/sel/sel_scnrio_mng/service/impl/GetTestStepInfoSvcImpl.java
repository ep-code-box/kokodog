package com.skd.sel.sel_scnrio_mng.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skd.sel.sel_scnrio_mng.service.GetTestStepInfoSvc;
import com.skd.sel.sel_scnrio_mng.dao.GetTestStepInfoDao;

@Service("getTestStepInfoSvc")
public class GetTestStepInfoSvcImpl implements GetTestStepInfoSvc {
  private static Logger logger = LogManager.getLogger(GetTestStepInfoSvcImpl.class);
  
  @Autowired
  private GetTestStepInfoDao getTestStepInfoDao;
  
  public List<Map<String, Object>> getTestStepInfo(Map<String, Object> inputMap) throws Exception {
    return getTestStepInfoDao.getTestStepInfo(inputMap);
  }
}