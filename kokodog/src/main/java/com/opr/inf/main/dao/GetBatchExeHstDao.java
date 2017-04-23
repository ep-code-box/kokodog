package com.opr.inf.main.dao;

import java.util.Map;
import java.util.List;
import java.sql.SQLException;

public interface GetBatchExeHstDao {
  public List<Map<String, Object>> getBatchExecList(Map<String, Object> inputMap) throws SQLException;
}