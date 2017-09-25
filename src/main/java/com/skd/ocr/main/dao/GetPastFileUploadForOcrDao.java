/*
 * Title : GetPastFileUploadForOcrDao
 *
 * @Version : 1.0
 *
 * @Date : 2017-09-13
 *
 * @Copyright by 이민석
 */
package com.skd.ocr.main.dao;

import java.util.List;
import java.util.Map;
import java.sql.SQLException;

/**
 *  기존에 등록하였던 Img OCR 변환 파일을 재조회하기 위함이 이 클래스의 주 사용 목적이다.
 */
public interface GetPastFileUploadForOcrDao {
  /**
   *  기존에 등록하였던 파일을 재조회하여 다시 처리하기 위한 서비스이다.
   *  DB에 담겨 있는 전체 파일 업로드 파일 리스트를 file_key와 파일명, 등록일시를 기준으로
   *  @return file_key, 등록시각, 파일명을 output으로 갖는 List Map
   *  @throws 기타 익셉션
   */
  public List<Map<String, Object>> getPastFileUpload() throws SQLException;
  
  /**
   *  기존에 등록하였던 파일을 재조회 할 때, 더 이상 필요없는 파일을 삭제하고자 함이다.
   *  @param fileKey 삭제를 진행하고자 하는 파일의 file_key
   *  @throws 기타 Exception
   */
  public void deletePastFileUpload(Map<String, Object> inputMap) throws SQLException;
}