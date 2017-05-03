package com.cmn.cmn.dao;

import java.util.Map;
import java.sql.SQLException;

public interface PageAuthDao {
  public Map<String, Object> getIsLoginAuth(Map<String, Object> inputMap) throws SQLException;
  public Map<String, Object> getIsMobilePageExist(Map<String, Object> inputMap) throws SQLException;
  public Map<String, Object> getIsPageExist(Map<String, Object> inputMap) throws SQLException;
}