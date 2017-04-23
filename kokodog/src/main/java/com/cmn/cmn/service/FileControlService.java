package com.cmn.cmn.service;

import java.io.InputStream;
import java.util.Map;

public interface FileControlService {
  public Map<String, Object> insertFile(InputStream is, String fileName, int userNum, String nowDtm) throws Exception;
  public void deleteFile(int fileNum) throws Exception;
}