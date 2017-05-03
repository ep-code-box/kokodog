package com.cmn.cmn.service;

import java.util.Map;

public interface GetCodeValService {
  public Map<Integer, String> getCodeVal(int cdNum) throws Exception;
}