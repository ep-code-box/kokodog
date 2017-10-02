package com.cmn.cmn.service;

import java.io.InputStream;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

public interface FileControlService {
  public Map<String, Object> insertFile(HttpServletRequest request) throws Exception;
  public Map<String, Object> insertFile(InputStream is, String fileName, int userNum, Long systemCallDtm) throws Exception;
  public void deleteFile(int fileNum) throws Exception;
  public Map<String, Object> getFileInfo(String fileKey) throws Exception;
  public byte[] getFileContent(int fileNum, int seq) throws Exception;
  public byte[] getFileContent(String fileKey) throws Exception;
}