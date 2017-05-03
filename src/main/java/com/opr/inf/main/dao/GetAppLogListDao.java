package com.opr.inf.main.dao;

import java.util.Map;
import java.util.List;
import java.sql.SQLException;

public interface GetAppLogListDao {
  public List<Map<String, Object>> getAppLogList(Map<String, Object> inputMap) throws SQLException;
  public Map<String, Object> getAppLogListCnt(Map<String, Object> inputMap) throws SQLException;
}