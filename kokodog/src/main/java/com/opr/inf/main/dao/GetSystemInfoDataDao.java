package com.opr.inf.main.dao;

import java.util.Map;
import java.util.List;
import java.sql.SQLException;

public interface GetSystemInfoDataDao {
  public List<Map<String, Object>> getMemoryUsed(Map<String, Object> inputMap) throws SQLException;
  public List<Map<String, Object>> getCpuUsed(Map<String, Object> inputMap) throws SQLException;
}