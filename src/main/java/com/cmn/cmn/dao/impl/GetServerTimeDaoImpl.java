/*
 * Title : GetServerTimeDaoImpl
 *
 * @Version : 1.0
 *
 * @Date : 2016-03-08
 *
 * @Copyright by 이민석
 */

package com.cmn.cmn.dao.impl;

import java.util.Map;
import java.sql.SQLException;

import org.apache.ibatis.session.SqlSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.cmn.cmn.dao.GetServerTimeDao;

/**
  *  이 객체는 현재 서버 시간을 리턴을 하는 역할을 수행한다.
  */
@Repository("getServerTimeDao")
public class GetServerTimeDaoImpl implements GetServerTimeDao {
  @Autowired
  private SqlSession sqlSession;
  
  public Map<String, Object> getServerTime() throws SQLException {
    return sqlSession.selectOne("com.cmn.cmn.getServerTime");
  }  
}