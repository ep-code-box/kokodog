package com.skd.sel.sel_scnrio_mng.dao;

import java.util.Map;
import java.sql.SQLException;

public interface UpdateTestExptRsltDao {
  public void delTestExptRsltByStepNum(Map<String, Object> inputMap) throws SQLException;
  
  public void insertTestExptRslt(Map<String, Object> inputMap) throws SQLException;
}