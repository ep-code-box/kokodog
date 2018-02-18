package com.skd.sel.sel_scnrio_mng.dao;

import java.util.Map;
import java.sql.SQLException;

public interface DelScnrioDao {
  public void delScnrio(Map<String, Object> inputMap) throws SQLException;
}