/*
 * Title : GetGoogleLoginLinkURLDaoImpl
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

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.cmn.cmn.dao.GetGoogleLoginOAuthRedirectUriDao;

/**
  *  이 객체는 Google OAuth Login 후 지정된 사이트로 리다이렉트 URL 가져오는 서비스로 정의된다.
  *  대상 데이터는 모두 DB 공통 테이블에서 관리되고 공통 테이블에 저장되어 있는 값을
  *  리턴하는 메서드가 있다.
  */
@Repository("getGoogleLoginOAuthRedirectUriDao")
public class GetGoogleLoginOAuthRedirectUriDaoImpl implements GetGoogleLoginOAuthRedirectUriDao {
  @Autowired
  private SqlSession sqlSession;
  
  private static Logger logger = LogManager.getLogger(GetGoogleLoginOAuthRedirectUriDaoImpl.class);

  public Map<String, Object> getGoogleLoginOAuthRedirectUri() throws SQLException {
    logger.debug("============   Start method of GetGoogleLoginOAuthRedirectUriDaoImpl.getGoogleLoginOAuthRedirectUri   ============");
    Map<String, Object> outputMap = sqlSession.selectOne("com.cmn.cmn.getGoogleLoginOAuthRedirectUri");
    logger.debug(" return - outputMap[" + outputMap + "]");
    return outputMap;
  }  
}