/*
 * Title : GetConvHtmlService
 *
 * @Version : 1.0
 *
 * @Date : 2017-08-15
 *
 * @Copyright by 이민석
 */
package com.skd.ppa.main.service;

/**
 *  이 객체는 문서 변환 프로세스를 통해 변환되어 저장된 HTML 정보를
 *  돌려주는 역할을 수행한다.
 */
public interface GetConvHtmlService {
  /**
   *  이 메서드는 문서 변환 프로세스를 통해 변환되어 저장된 HTML 정보를
   *  돌려주는 역할을 수행한다.
   *  @param fileKey : HTML로 되돌려줄 파일의 키
   *  @return 결과 HTML
   *  @throws 기타 익셉션
   */
  public String getHtml(String fileKey) throws Exception;
}