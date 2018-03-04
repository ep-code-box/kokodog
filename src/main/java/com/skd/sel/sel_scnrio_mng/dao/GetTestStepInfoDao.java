package com.skd.sel.sel_scnrio_mng.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface GetTestStepInfoDao {
  public List<Map<String, Object>> getTestStepInfo(Map<String, Object> inputMap) throws SQLException;
}