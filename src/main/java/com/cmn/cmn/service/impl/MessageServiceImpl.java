package com.cmn.cmn.service.impl;

import java.util.Map;
import java.util.HashMap;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmn.cmn.service.MessageService;
import com.cmn.cmn.dao.MessageDao;

@Service("messageService")
public class MessageServiceImpl implements MessageService {
  @Autowired
  private MessageDao messageDao;
  
  private static Logger logger = LogManager.getLogger(MessageServiceImpl.class);
  
  public Map<String, Object> getErrMessageByMessageNum(int n) throws Exception {
    logger.debug("============   Start method of MessageServiceImpl.getErrMessageByMessageNum   ============");
    Map<String, Object> inputMap = new HashMap<String, Object>();
    Map<String, Object> outputMap = null;
    inputMap.put("msg_num", n);
    outputMap = messageDao.getErrMessageByMessageNum(inputMap);
    return outputMap;
  }
}