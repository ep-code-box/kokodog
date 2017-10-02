package com.cmn.cmn.service.impl;

import org.springframework.stereotype.Service;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.cmn.cmn.service.GetRandomStringArrayService;

@Service("getRandomStringArrayService")
public class GetRandomStringArrayServiceImpl implements GetRandomStringArrayService {
  private static Logger logger = LogManager.getLogger(GetRandomStringArrayServiceImpl.class);

  public String getRandomStringArray(int n) throws Exception {
    logger.debug("============   Start method of GetRandomStringArrayServiceImpl.getRandomStringArray   ============");
    String returnStr = "";
    for (int i = 0; i < n; i++) {
      int randomValue = (int)(Math.random() * (10 + 26 + 26));
      if (randomValue < 10) {
        returnStr = (returnStr + (char)((int)'0' + randomValue));
      } else if (randomValue < 10 + 26) {
        returnStr = (returnStr + (char)((int)'a' + (randomValue - 10)));        
      } else {
        returnStr = (returnStr + (char)((int)'A' + (randomValue - (10 + 26))));        
      }
    }
    return returnStr;    
  }
}