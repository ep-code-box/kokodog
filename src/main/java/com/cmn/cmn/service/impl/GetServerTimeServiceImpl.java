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

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.cmn.cmn.dao.GetServerTimeDao;
import com.cmn.cmn.service.GetServerTimeService;

/**
  *  이 객체는 현재 서버 시간을 리턴을 하는 역할을 수행한다.
  */
@Repository("getServerTimeService")
public class GetServerTimeServiceImpl implements GetServerTimeService {
  private static Logger logger = LogManager.getLogger(GetServerTimeServiceImpl.class);

  @Autowired
  private GetServerTimeDao getServerTimeDao;
  
  public long getServerTime() throws SQLException {
    logger.debug("============   Start method of GetServerTimeServiceImpl.getServerTime   ============");
    return ((Date)(((Map<String, Object>)getServerTimeDao.getServerTime()).get("datetime"))).getTime();
  }  
}