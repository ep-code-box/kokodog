/*
 * Title : GetGoogleLoginLinkURLService
 *
 * @Version : 1.0
 *
 * @Date : 2016-03-08
 *
 * @Copyright by 이민석
 */

package com.cmn.cmn.login.service;

/**
 *  이 객체는 Google OAuth 2.0을 이용한 로그인 시도를 진행할 때
 *  Google 서버로부터 코드 등의 각종 정보를 가져오기 위한 파라미터를
 *  포함한 Link를 가져오기 위하여 호출한다.
 */
public interface GetGoogleLoginLinkURLService {
  public String getGoogleLoginLinkURL(String scheme, String serverName, int serverPort, String redirectUrlParam) throws Exception;
}