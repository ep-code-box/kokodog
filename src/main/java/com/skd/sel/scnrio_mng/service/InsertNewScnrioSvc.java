package com.skd.sel.sel_scnrio_mng.service;

import java.util.Map;
/**
 * 이 클래스는 Selenium 테스트 자동화 도구 개발 프로젝트 내에서
 * 시스템 관리를 위해 신규 시나리오 등록 요청 발생 시
 * 시나리오번호를 채번하여 신규 시나리오번호를 등록해주는 역할을 한다.
 * input은 시나리오명, 시나리오설명을 받아
 * 최종 결과값으로 시나리오번호를 되돌려주는 역할을 수행한다.
 *
 * @author  Minseok Lee
 * @since   2018.02.03
 * @version 1.0
 */
public interface InsertNewScnrioSvc {
  /**
   * 신규 시나리오명과 시나리오설명을 받아 채번된 시나리오번호를 리턴한다.
   *
   * @param inputMap Map 형태의 아래 정보를 포함하고 있는 정보
   *        scnrio_nm - 시나리오 명(필수)
   *        scnior_desc - 시나리오 설명(선택)
   * @return 시나리오 번호
   * @exception Exception 예상하지 못한 Exception으로 정의한다.
   */
  public int insertNewScnrio(Map<String, Object> inputMap) throws Exception;
}