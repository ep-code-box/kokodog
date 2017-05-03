/*
 * Title : GetServerTimeServiceImpl
 *
 * @Version : 1.0
 *
 * @Date : 2016-03-08
 *
 * @Copyright by 이민석
 */

package com.cmn.cmn.service.impl;

import java.util.Date;
import java.util.Map;
import java.sql.SQLException;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.cmn.cmn.dao.GetServerTimeDao;
import com.cmn.cmn.service.GetServerTimeService;
import com.cmn.err.SystemException;

/**
  *  이 객체는 현재 서버 시간을 리턴을 하는 역할을 수행한다.
  */
@Repository("getServerTimeService")
public class GetServerTimeServiceImpl implements GetServerTimeService {
  @Autowired
  private GetServerTimeDao getServerTimeDao;
  
  @Autowired
  private SystemException systemException;
  
  public long getServerTime() throws SQLException {
    return ((Date)(((Map<String, Object>)getServerTimeDao.getServerTime()).get("datetime"))).getTime();
  }  
}