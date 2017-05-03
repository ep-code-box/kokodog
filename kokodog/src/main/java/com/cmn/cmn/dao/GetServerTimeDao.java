/*
 * Title : GetServerTimeDao
 *
 * @Version : 1.0
 *
 * @Date : 2016-03-08
 *
 * @Copyright by 이민석
 */

package com.cmn.cmn.dao;

import java.util.Map;
import java.sql.SQLException;

/**
  *  이 객체는 현재 서버 시간을 리턴을 하는 역할을 수행한다.
  */
public interface GetServerTimeDao {
  public Map<String, Object> getServerTime() throws SQLException;
}