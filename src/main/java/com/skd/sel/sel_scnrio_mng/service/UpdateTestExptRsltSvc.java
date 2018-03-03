package com.skd.sel.sel_scnrio_mng.service;

import java.util.List;
import java.util.Map;
/**
 * 이 클래스는 Selenium 테스트 자동화 도구 개발 프로젝트 내에서
 * 테스트 케이스 내 테스트가 정상적으로 수행되었는지 기준정보를 판단하기 위한
 * 기대결과 리스트를 관리하는 서비스로 정의된다.
 * 신규 서비스를 저장/삭제/수정 하는 기능을 제공한다.
 *
 * @author  Minseok Lee
 * @since   2018.02.03
 * @version 1.0
 */
public interface UpdateTestExptRsltSvc {
  /**
   * 해당 시나리오 번호, 케이스번호 및 테스트스텝번호에 따른 값을 추가/삭제/수정한다.
   *
   * @param scnrioNum 시나리오번호
   * @param caseNum 케이스번호
   * @param inputList Map 형태의 아래 정보를 포함하고 있는 정보를 갖는 List
   *        test_step_num - 테스트 스텝 번호(필수)
   *        modify_typ - 변경 유형(필수, 1 : add, 2 : update, 3 : delete)
   *        rslt_strd - 기준 문구(선택)
   *        judg_typ_cd - 판단 유형 코드(선택)
   *        old_test_step_num - 기존 테스트 스텝 번호(선택)
   * @exception Exception 예상하지 못한 Exception으로 정의한다.
   */
  public void updateTestExptRslt(int scnrioNum, int caseNum, List<Map<String, Object>> inputList) throws Exception;
}