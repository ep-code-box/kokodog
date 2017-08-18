/*
 * Title : DocNlpService
 *
 * @Version : 1.0
 *
 * @Date : 2017-08-15
 *
 * @Copyright by 이민석
 */
package com.skd.ppa.main.service;

import java.io.FileInputStream;

import net.sf.json.JSONArray;

/**
 *  이 객체는 Python 내 Konlpy를 사용하여 각종
 *  주어진 문서를 기준으로 Natural Langauge Processing을 수행하는 기능을 제공한다.
 *  사용할 수 있는 문서 타입은 MS Word(doc, docx), Excel(xls, xlsx), PPT(ppt, pptx)이다.
 *  모든 return은 JSONArray를 상속받은 JSON 객체들로 리턴된다.
 *  형태소 분리는 미리 작성된 Python 로직에 의해 수행된다.
 *  상세 내용을 수정하기 위헤서는 Python 로직 수정이 필요하다.
 */
public interface DocNlpService {
  /**
   *  이 메서드는 Konlpy 내 사전에 등록된 명사 리스트를 JSON으로 리턴해주는 역할을 수행한다.
   *  @param str : 명사 리스트를 추출하기 위한 기본 텍스트
   *  @return 명사 리스트
   *  @throws 기타 익셉션
   */
  public JSONArray getNounList(FileInputStream fis) throws Exception;
  /**
   *  이 메서드는 Konlpy 내 사전에 등록된 전체 형태소 리스트를 분류별로 JSON으로 리턴해주는 역할을 수행한다.
   *  @param str : 형태소를 추출하기 위한 기본 글
   *  @return 형태소 리스트
   *  @throws 기타 익셉션
   */
  public JSONArray getMorpheme(FileInputStream fis) throws Exception;

  /**
   *  이 메서드는 Konlpy 내 사전에 등록된 전체 명사 리스트를 분류별로 JSON으로 리턴해주는 역할을 수행한다.
   *  @param fileKey : 분석하고자 하는 HTML의 파일 Key
   *  @return 형태소 리스트
   *  @throws 기타 익셉션
   */
  public JSONArray getNounList(String fileKey) throws Exception;
}