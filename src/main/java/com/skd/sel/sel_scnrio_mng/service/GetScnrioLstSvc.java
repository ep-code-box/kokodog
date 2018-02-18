package com.skd.sel.sel_scnrio_mng.service;

import java.util.List;
import java.util.Map;
/**
 * 이 클래스는 Selenium 테스트 자동화 도구 개발 프로젝트 내에서
 * 시스템 관리를 위해 시나리오 리스트를 출력해주는 역할을 수행한다.
 * 입력값은 검색어이며 선택값이고, 페이징을 위한 현재 페이지를 입력값으로 활용한다..<br/>
 * 기본적으로 1페이지에 40개의 데이터를 보여주도록 구성한다.
 *
 * @author  Minseok Lee
 * @since   2018.02.03
 * @version 1.0
 */
public interface GetScnrioLstSvc {
  /**
   * 검색어에 해당하는 전체 시나리오 리스트 정보를 제공한다.
   *
   * @param inputMap 아래 값을 갖는 Map으로 정의한다.
   *        sch_txt - 검색어(선택)
   *        page_num - 페이지번호(선택)
   * @return List에 아래 내용을 담긴 Map을 순차적으로 담아 현재 테스트에 필요한 정보를 제공한다.
   *         scnrio_num : 시나리오 번호 (Integer 타입)
   *         scnrio_nm : 시나리오 명(String 타입)
   *         scnrio_desc : 시나리오 설명(String 타입)
   * @exception Exception 예상하지 못한 Exception으로 정의한다.
   */
  public List<Map<String, Object>> getScnrioLst(Map<String, Object> inputMap) throws Exception;
}