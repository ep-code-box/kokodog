/*
 * Title : GetGoogleLoginOAuthRedirectUriDao
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
  *  이 객체는 Google OAuth Login 후 지정된 사이트로 리다이렉트 URL 가져오는 서비스로 정의된다.
  *  대상 데이터는 모두 DB 공통 테이블에서 관리되고 공통 테이블에 저장되어 있는 값을
  *  리턴하는 메서드가 있다.
  */
public interface GetGoogleLoginOAuthRedirectUriDao {
  public Map<String, Object> getGoogleLoginOAuthRedirectUri() throws SQLException;
}