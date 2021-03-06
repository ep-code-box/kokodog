/*
 * Title : GetPageAuthDao
 *
 * @Version : 1.0
 *
 * @Date : 2017-09-24
 *
 * @Copyright by 이민석
 */
package com.cmn.err.err_401.dao;

import java.util.List;
import java.util.Map;
import java.sql.SQLException;

/**
 *  기존에 등록하였던 Img OCR 변환 파일을 재조회하기 위함이 이 클래스의 주 사용 목적이다.
 */
public interface GetPageAuthDao {
  /**
   *  해당 경로에 해당하는 권한을 모두 가져온다.
   *  @param inputMap - 프로그램(pgm), 업무(task), 페이지(page)를 담고 있는 맵
   *  @return List 타입으로 내부 요소는 Map<String, Object> 형태를 가지며 file_key, 등록일시, 파일명을 결과로 전달한다.
   *  @throws 기타 Exception
   */
  public List<Map<String, Object>> getAuthListByPath(Map<String, Object> inputMap) throws SQLException;
  
  /**
   *  사용자 권한 등록을 요청한다.
   *  @param inputMap - 프로그램(pgm), 업무(task), 페이지(page)를 담고 있는 맵
   *  @return List 타입으로 내부 요소는 Map<String, Object> 형태를 가지며 file_key, 등록일시, 파일명을 결과로 전달한다.
   *  @throws 기타 Exception
   */
  public int requestAuth(Map<String, Object> inputMap) throws SQLException;
}