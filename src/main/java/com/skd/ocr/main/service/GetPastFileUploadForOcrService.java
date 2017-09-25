/*
 * Title : GetPastFileUploadForOcrService
 *
 * @Version : 1.0
 *
 * @Date : 2017-08-10
 *
 * @Copyright by 이민석
 */
package com.skd.ocr.main.service;

import java.util.List;
import java.util.Map;

/**
 *  이 객체는 SK 주식회사 C&C DT 프로젝트 일환으로
 *  진행하고 있는  분석 프로젝트의
 *  개발 프로젝트 중 일부에 포함된다.<br/>
 *  기존에 미리 업로드된 파일 리스트를 가져오는 역할을 수행한다.
 */
public interface GetPastFileUploadForOcrService {
  /**
   *  이 객체는 기존에 미리 업로드된 파일 리스트를 가져오는 역할을 수행한다.
   *  @return JSONArray 타입의 전체 업로드된 파일 리스트
   *  @throws 기타 예외
   */
  public List<Map<String, Object>> getPastFileUploadList() throws Exception;
  
  /**
   *  기존에 등록하였던 파일을 재조회 할 때, 더 이상 필요없는 파일을 삭제하고자 함이다.
   *  @param fileKey 삭제를 진행하고자 하는 파일의 file_key
   *  @throws 기타 Exception
   */
  public void deletePastFileUpload(String fileKey) throws Exception;
}