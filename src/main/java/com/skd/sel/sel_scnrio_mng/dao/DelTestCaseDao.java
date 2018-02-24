package com.skd.sel.sel_scnrio_mng.dao;

import java.util.Map;
import java.sql.SQLException;

public interface DelTestCaseDao {
  public Map<String, Object> getCanBeDeletedCase(Map<String, Object> inputMap) throws SQLException;
  public void delTestCaseWithScnrioNumAndCaseNum(Map<String, Object> inputMap) throws SQLException;
  public void delAllTestCaseInputWithScnrioNumAndCaseNum(Map<String, Object> inputMap) throws SQLException;
}