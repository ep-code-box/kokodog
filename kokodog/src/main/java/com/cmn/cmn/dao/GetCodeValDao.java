package com.cmn.cmn.dao;

import java.util.List;
import java.util.Map;
import java.sql.SQLException;

public interface GetCodeValDao {
  public List<Map<String, Object>> getAllCodeListValFromCodeNum(Map<String, Object> inputMap) throws SQLException;
}