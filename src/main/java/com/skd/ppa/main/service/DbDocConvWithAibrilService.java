/*
 * Title : DbDocConvWithAibrilService
 *
 * @Version : 1.0
 *
 * @Date : 2017-08-10
 *
 * @Copyright by 이민석
 */
package com.skd.ppa.main.service;

/**
 *  이 객체는 Aibril 에서 제공하는 Document Converting 기능을 수행한다.
 *  MS Word, Presentation, Excel의 문서를 HTML형식으로 변환하는 기능을
 *  갖고 있다.
 *  단, DocConvWithAibrilService와는 다르게 DB에 저장되어 있는 파일 데이터를 변환한다.
 */
public interface DbDocConvWithAibrilService {
  /**
   *  이 메쏘드는 MS word, Presentation, Excel의 문서를
   *  HTML 형식으로 리턴해주는 기능을 갖는다.
   *  @param file - File 형식의 변환 전 파일 위치
   *  @return HTML 형식의 문자열
   *  @throws 기타 모든 예외
   */
  public void convToHtml(String fileKey) throws Exception;
}