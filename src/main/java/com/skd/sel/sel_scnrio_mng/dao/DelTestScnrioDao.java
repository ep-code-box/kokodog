package com.skd.sel.sel_scnrio_mng.dao;

import java.util.Map;
import java.sql.SQLException;

public interface DelTestScnrioDao {
  public void delTestScnrio(Map<String, Object> inputMap) throws SQLException;
  public void delAllTestCaseWithScnrioNum(Map<String, Object> inputMap) throws SQLException;
}