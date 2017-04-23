/*
 * Title : BatchExeProcessDao
 *
 * @Version : 1.0
 *
 * @Date : 2017-04-18
 *
 * @Copyright by 이민석
 */

package com.cmn.cmn.dao;

import java.util.Map;
import java.sql.SQLException;

/**
  *  이 객체는 Batch를 수행하는 class에서 배치 리포트를 저장하는 역할을 수행하는
  *  Dao들로 정의된다.
  */
public interface BatchExeProcessDao {
  public void updateErrReport(Map<String, Object> inputMap) throws SQLException;
  public void updateBatchFinishResult(Map<String, Object> inputMap) throws SQLException;
  public void insertBatchExeProcess(Map<String, Object> inputMap) throws SQLException;
}