/*
 * Title : GetPageAuthService
 *
 * @Version : 1.0
 *
 * @Date : 2017-09-24
 *
 * @Copyright by 이민석
 */
package com.cmn.err.err_401.service;

import java.util.List;
import java.util.Map;

/**
 *  이 객체는 사용자가 Page접근 권한이 없을 때
 *  각종 권한을 관리하고 요청하는 역할을 정의하는
 *  서비스로 이루어진다.
 */
public interface GetPageAuthService {
  /**
   *  요청 request 별로 갖고 있는 권한 리스트를 불러온다.
   *  @param path - 패스 리스트로 정의된다.
   *  @return List 타입으로 내부 요소는 Map<String, Object> 형태를 가지며 file_key, 등록일시, 파일명을 결과로 전달한다.
   *  @throws 기타 Exception
   */
  public List<Map<String, Object>> getAuthListByPath(String path, int userNum) throws Exception;
  
  /**
   *  권한을 요청한다.
   *  @param authNum - 요청하고자 하는 권한 번호
   *  @param userNum - 요청자의 요청번호
   *  @throws 기타 Exceptioni
   */
  public void requestAuth(int authNum, int userNum) throws Exception;
}