package com.skd.sel.sel_scnrio_mng.service;

import java.util.List;
import java.util.Map;
/**
 * 이 클래스는 Selenium 테스트 자동화 도구 개발 프로젝트 내에서
 * 시나리오 혹은 시나리오번호 + 케이스번호 조합에 해당하는 소스코드를 전부 되돌려주는 역할을 수행한다.
 *
 * @author  Minseok Lee
 * @since   2018.02.03
 * @version 1.0
 */
public interface GetImportedSrcCdSvc {
  /**
   * 시나리오번호 혹은 시나리오번호 및 케이스번호에 해당하는 소스코드를 되돌려준다.
   *
   * @param inputMap 아래 값을 갖는 Map으로 정의한다.
   *        scnrio_num - 시나리오번호(필수)
   *        case_num - 케이스번호(선택)
   * @return List에 아래 내용을 담긴 Map을 순차적으로 담아 소스코드를 제공한다.
   *         case_num : 케이스 번호 (Integer 타입)
   *         src_cd : 소스코드(String 타입)
   * @exception Exception 예상하지 못한 Exception으로 정의한다.
   */
  public List<Map<String, Object>> getImportedSrcCd(Map<String, Object> inputMap) throws Exception;
}