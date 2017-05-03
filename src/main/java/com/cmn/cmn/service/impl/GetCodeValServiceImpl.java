package com.cmn.cmn.service.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.apache.log4j.Logger;

import com.cmn.cmn.service.GetCodeValService;
import com.cmn.cmn.dao.GetCodeValDao;

@Service("getCodeValService")
public class GetCodeValServiceImpl implements GetCodeValService {
  @Autowired
  private GetCodeValDao getCodeValDao;
  
  private static Logger logger = Logger.getLogger(GetCodeValServiceImpl.class);

  public Map<Integer, String> getCodeVal(int cdNum) throws Exception {
    int i = 0;
    Map<String, Object> inputMap = new HashMap<String, Object>();
    List<Map<String, Object>> outputList = null;
    inputMap.put("cd_num", cdNum);
    outputList = getCodeValDao.getAllCodeListValFromCodeNum(inputMap);
    Map<Integer, String> returnMap = new HashMap<Integer, String>();
    for (i = 0; i < outputList.size(); i++) {
      returnMap.put(new Integer(((Long)outputList.get(i).get("cd_seq")).intValue()), (String)outputList.get(i).get("cd_seq_name"));
    }
    return returnMap;    
  }
}