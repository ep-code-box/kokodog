/*
 * Title : GetImgOcrInfoByFileKeyService
 *
 * @Version : 1.0
 *
 * @Date : 2016-04-17
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
public interface GetImgOcrInfoByFileKeyService {
  /**
   *  기존에 등록하였던 파일을 재조회하여 다시 처리하기 위한 서비스이다.
   *  DB에 담겨 있는 전체 파일 업로드 파일 리스트를 file_key와 파일명, 등록일시를 기준으로
   *  전체 데이터를 가져온다.
   *  @return List 타입으로 내부 요소는 Map<String, Object> 형태를 가지며 file_key, 등록일시, 파일명을 결과로 전달한다.
   *  @throws 기타 Exception
   */
  public List<Map<String, Object>> getImgOcrByFileKey(String fileKey) throws Exception;
}