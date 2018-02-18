package com.skd.sel.sel_scnrio_mng.service;

import java.util.Map;
/**
 * 이 클래스는 Selenium 테스트 자동화 도구 개발 프로젝트 내에서
 * 시스템 관리를 위해 신규 케이스 등록 요청 발생 시
 * 시나리오번호를 채번하여 신규 시나리오번호를 등록해주는 역할을 한다.
 * input은 케이스명, 케이스설명, 시나리오번호를 받아
 * 최종 결과값으로 케이스번호를 되돌려주는 역할을 수행한다.
 *
 * @author  Minseok Lee
 * @since   2018.02.03
 * @version 1.0
 */
public interface InsertNewCaseSvc {
  /**
   * 신규 케이스명과 케이스설명, 시나리오번호를 받아 채번된 케이스번호를 리턴한다.
   *
   * @param inputMap Map 형태의 아래 정보를 포함하고 있는 정보
   *        scnrio_num - 시나리오번호(필수)
   *        case_nm - 케이스 명(필수)
   *        case_desc - 케이스 설명(선택)
   * @return 케이스 번호
   * @exception Exception 예상하지 못한 Exception으로 정의한다.
   */
  public int insertNewCase(Map<String, Object> inputMap) throws Exception;
}