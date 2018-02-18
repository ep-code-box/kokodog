package com.skd.sel.sel_scnrio_mng.service;

import java.util.Map;
/**
 * 이 클래스는 Selenium 테스트 자동화 도구 개발 프로젝트 내에서
 * 소스코드를 수정할 때
 * 수정된 소스코드를 DB에 넣는 역할을 수행한다.
 * 소스코드를 넣을 때, 소스 코드 내에 input 값을 자동으로 찾아
 * input 값으로 활용할 수 있도록 DB를 가공해야 하며
 * input값의 순서 변경 시에 각 case에 대한 정보까지 흐트러지지 않도록 보정이 필요하다.
 *
 * @author  Minseok Lee
 * @since   2018.02.03
 * @version 1.0
 */
public interface UpdateScnrioSrcCdSvc {
  /**
   * 해당 시나리오 번호에 해당하는 소스코드를 수정한다.
   *
   * @param inputMap Map 형태의 아래 정보를 포함하고 있는 정보
   *        scnrio_num - 시나리오 번호(필수)
   *        src_cd - 소스코드(필수)
   * @exception Exception 예상하지 못한 Exception으로 정의한다.
   */
  public void updateScnrioSrcCd(Map<String, Object> inputMap) throws Exception;
}