package com.cmn.cmn.dao;

import java.util.List;
import java.util.Map;
import java.sql.SQLException;

public interface MessageDao {
  public Map<String, Object> getErrMessageByMessageNum(Map<String, Object> inputMap) throws SQLException;
}