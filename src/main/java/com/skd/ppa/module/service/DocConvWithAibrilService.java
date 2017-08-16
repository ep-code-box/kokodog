/*
 * Title : DocConvWithAibrilService
 *
 * @Version : 1.0
 *
 * @Date : 2017-08-10
 *
 * @Copyright by 이민석
 */
package com.skd.ppa.module.service;

import java.io.InputStream;

/**
 *  이 객체는 Aibril 에서 제공하는 Document Converting 기능을 수행한다.
 *  MS Word, Presentation, Excel의 문서를 HTML형식으로 변환하는 기능을
 *  갖고 있다.
 */
public interface DocConvWithAibrilService {
  /**
   *  이 메쏘드는 MS word, Presentation, Excel의 문서를
   *  HTML 형식으로 리턴해주는 기능을 갖는다.
   *  @param is - InputStream 형식의 변환 전 파일 위치
   *  @return HTML 형식의 문자열
   *  @throws 기타 모든 예외
   */
  public String convToHtml(InputStream is) throws Exception;
  
  /**
   *  이 메쏘드는 MS word, Presentation, Excel의 문서를
   *  HTML 형식으로 리턴해주는 기능을 갖는다.
   *  @param fileName - 전달할 파일이 위치하는 파일 명
   *  @return HTML 형식의 문자열
   *  @throws 기타 모든 예외
   */
  public String convToHtml(String fileName) throws Exception;
}