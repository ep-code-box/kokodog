package com.cmn.cmn.dao;

import java.util.Map;
import java.sql.SQLException;

public interface FileControlDao {
  public Map<String, Object> getIsExistFileKeyByKey(Map<String, Object> inputMap) throws SQLException;
  public void insertFile(Map<String, Object> inputMap) throws SQLException;
  public Map<String, Object> getLastFileNum() throws SQLException;
  public void insertFileContent(Map<String, Object> inputMap) throws SQLException;
  public void deleteFileDeleteByFileNum(Map<String, Object> inputMap) throws SQLException;
  public Map<String, Object> getFileInfo(Map<String, Object> inputMap) throws SQLException;
  public Map<String, Object> getFileContent(Map<String, Object> inputMap) throws Exception;
}