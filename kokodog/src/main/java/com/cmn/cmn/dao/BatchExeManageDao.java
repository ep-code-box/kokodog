package com.cmn.cmn.dao;

import java.util.List;
import java.util.Map;
import java.sql.SQLException;

public interface BatchExeManageDao {
  public void updateAllBatchExeError(Map<String, Object> inputMap) throws SQLException;
  public int insertBatchLockDateTime(Map<String, Object> inputMap) throws SQLException;
  public int updateBatchLockDateTimeToY(Map<String, Object> inputMap) throws SQLException;
  public Map<String, Object> getIsExistPreBatchProcess(Map<String, Object> inputMap) throws SQLException;
  public int insertBatchProcessHstWithoutLast(Map<String, Object> inputMap) throws SQLException;
  public int insertBatchProcessHst(Map<String, Object> inputMap) throws SQLException;
  public Map<String, Object> getCurrentBatchProcessInfo(Map<String, Object> inputMap) throws SQLException;
  public List<Map<String, Object>> getBatchNoExeList() throws SQLException;
  public int updateBatchLockDateTimeToN(Map<String, Object> inputMap) throws SQLException;
}