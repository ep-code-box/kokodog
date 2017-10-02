/*
 * Title : GetGoogleLoginLinkURLDaoImpl
 *
 * @Version : 1.0
 *
 * @Date : 2016-03-08
 *
 * @Copyright by 이민석
 */

package com.cmn.cmn.login.dao.impl;

import java.util.List;
import java.util.Map;
import java.sql.SQLException;

import org.apache.ibatis.session.SqlSession;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.cmn.cmn.login.dao.GetGoogleLoginLinkURLDao;
import com.cmn.err.SystemException;

@Repository("getGoogleLoginLinkURLDao")
public class GetGoogleLoginLinkURLDaoImpl implements GetGoogleLoginLinkURLDao {
  private static Logger logger = LogManager.getLogger(GetGoogleLoginLinkURLDaoImpl.class);

  @Autowired
  private SqlSession sqlSession;
  
  @Autowired
  private SystemException systemException;
  
  /**
    *  Google OAuth 로그인을 위하여 링크 정보를 가져오는 method
    *  @return - key : value 의 Map 형태의 전체 정보를 리스트에 담아 리턴되는 값이다.
    *  @throws SQLException - DB 조회 시 발생하는 SQLException으로 정의된다.
    */
  public List<Map<String, Object>> getGoogleLoginOAuthParameter() throws SQLException {
    logger.debug("============   Start method of GetGoogleLoginLinkURLDaoImpl.getGoogleLoginOAuthParameter   ============");
    return sqlSession.selectList("com.cmn.cmn.login.getGoogleLoginOAuthParameter");
  }
}