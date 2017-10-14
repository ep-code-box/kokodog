package com.cmn.cmn.dao;

import java.util.Map;
import java.sql.SQLException;

public interface ConnLogDao {
  public void insertConnLog(Map<String, Object> inputMap) throws SQLException;
  public Map<String, Object> getLastSeqConnLog(Map<String, Object> inputMap) throws SQLException;
  public void updateConnEndLog(Map<String, Object> inputMap) throws SQLException;
  public void checkConnectValid() throws SQLException;
}