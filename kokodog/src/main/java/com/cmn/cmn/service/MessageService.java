package com.cmn.cmn.service;

import java.util.Map;

public interface MessageService {
  public Map<String, Object> getErrMessageByMessageNum(int n) throws Exception;
}