/*
 * Title : GetPastFileUploadService
 *
 * @Version : 1.0
 *
 * @Date : 2016-04-17
 *
 * @Copyright by 이민석
 */
package com.skd.ppa.main.service;

import java.util.List;
import java.util.Map;

/**
 *  이 클래스는 SK 주식회사 C&C DT 프로젝트 일환으로
 *  진행하고 있는 상품 요구명세서 분석 프로젝트의
 *  개발 프로젝트 중 일부에 포함된다.<br/>
 *  챗봇의 대화를 통해 상품 요건서를 정형화된 포맷에 만족할 수 있도록
 *  하는 기능을 제공한다.
 */
public interface GetProdChatService {
  /**
   *  이 메서드는 대화의 output을 리턴으로 받아주는 역할을 수행한다.
   *  @param text - 전달하고자 하는 메시지
   *  @param userNum - 사용자 번호
   *  @return 챗봇이 대답하는 데이터
   *  @throws 기타 예외
   */
  public String getProdChat(String text, int userNum) throws Exception;
  
  /**
   *  이 메서드는 기존 대화를 완전히 삭제하는 역할을 수행한다.
   *  @param userNum - 대화 결과를 삭제하고자 하는 사용자 번호
   *  @throws 기타 예외
   */
  public void refresh(int userNum) throws Exception;

  /**
   *  이 메서드는 기존 대화를 기반으로 최초 30개의 데이터를 화면으로 전달한다.
   *  만약 기존 대화가 없을 경우에는 init 대화를 Aibril 서버로부터 가져온다.
   *  @param userNum - 대화 결과를 조회하고자 하는 사용자 번호
   *  @return 대화 결과 리스트, Map에 담긴 Key는 text이다.
   *  @throws 기타 예외
   */
  public List<Map<String, Object>> getProdInitChat(int userNum) throws Exception;
}