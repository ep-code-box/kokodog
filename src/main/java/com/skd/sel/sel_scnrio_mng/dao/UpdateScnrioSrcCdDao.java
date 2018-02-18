package com.skd.sel.sel_scnrio_mng.dao;

import java.util.Map;
import java.sql.SQLException;

public interface UpdateScnrioSrcCdDao {
  public Map<String, Object> getTestInputInfoByScnrioNumAndInputNm(Map<String, Object> inputMap) throws SQLException;
  public void delTestInputWithScnrioNumAndInputNm(Map<String, Object> inputMap) throws SQLException;
  public void insertNewTestInput(Map<String, Object> inputMap) throws SQLException;
  public void delTestCaseInputWithScnrioNumAndInputNum(Map<String, Object> inputMap) throws SQLException;
  public void insertChangedInputNumWithScnrioNumAndCaseNumAndInputNm(Map<String, Object> inputMap) throws SQLException;
  public void delTestScnrio(Map<String, Object> inputMap) throws SQLException;
  public void delTestInputBiggerThanInputNum(Map<String, Object> inputMap) throws SQLException;
  public void delTestCaseInputBiggerThanInputNum(Map<String, Object> inputMap) throws SQLException;
  public void insertNewSrcCdWithScnrioNum(Map<String, Object> inputMap) throws SQLException;
}