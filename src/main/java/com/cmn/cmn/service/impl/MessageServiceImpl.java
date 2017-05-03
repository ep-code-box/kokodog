package com.cmn.cmn.service.impl;

import java.io.InputStream;
import java.util.Map;
import java.util.HashMap;

import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmn.cmn.service.MessageService;
import com.cmn.cmn.service.GetRandomStringArrayService;
import com.cmn.cmn.dao.MessageDao;

@Service("messageService")
public class MessageServiceImpl implements MessageService {
  @Autowired
  private MessageDao messageDao;
  
  private static Logger logger = Logger.getLogger(MessageServiceImpl.class);
  
  public Map<String, Object> getErrMessageByMessageNum(int n) throws Exception {
    Map<String, Object> inputMap = new HashMap<String, Object>();
    Map<String, Object> outputMap = null;
    inputMap.put("msg_num", n);
    outputMap = messageDao.getErrMessageByMessageNum(inputMap);
    return outputMap;
  }
}