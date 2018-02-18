package com.skd.sel.sel_scnrio_mng.service;

import java.util.List;
import java.util.Map;
/**
 * 이 클래스는 Selenium 테스트 자동화 도구 개발 프로젝트 내에서
 * 시스템 관리를 위해 시나리오 번호에 해당하는 소스코드를 리턴해주는 역할을 수행한다.
 * 입력값은 시나리오 번호이며 필수값이다..<br/>
 *
 * @author  Minseok Lee
 * @since   2018.02.03
 * @version 1.0
 */
public interface GetSrcCdByScnrioNumSvc {
  /**
   * 시나리오 번호에 해당하는 소스코드를 돌려준다.
   *
   * @param scnrio_num 시나리오번호
   * @return 소스코드
   * @exception Exception 예상하지 못한 Exception으로 정의한다.
   */
  public String getSrcCdByScnrioNum(int scnrioNum) throws Exception;
}