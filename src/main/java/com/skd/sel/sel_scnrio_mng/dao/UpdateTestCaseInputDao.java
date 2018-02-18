package com.skd.sel.sel_scnrio_mng.dao;

import java.util.Map;
import java.sql.SQLException;

public interface UpdateTestCaseInputDao {
  public Map<String, Object> getTestCaseInputByInputNm(Map<String, Object> inputMap) throws SQLException;
  
  public void delTestCaseInputByInputNm(Map<String, Object> inputMap) throws SQLException;
  
  public void insertNewTestCaseInput(Map<String, Object> inputMap) throws SQLException;
}