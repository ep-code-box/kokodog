package com.skd.sel.module.service;

import java.util.List;
import java.util.Map;

/**
 * 이 클래스는 Selenium 테스트 자동화 도구 개발 프로젝트 내에서
 * 파라미터로 지정되어 있는 소스코드를 입력 파라미터와 함께 메서드 호출 시
 * 전달받아 소스코드 내 입력 변수를 치환하여 완벽한 테스트 소스 코드를 구현하는 데에 있다.<br/>
 * 치환 파라미터는 소스 내에 다음 형식으로 구성되어 있을 경우 치환된다.<br/>
 * ${ + 입력변수 명 + }
 * 예) a와 b가 변수이며 이를 소스에서 치환할 변수로 지정하기 위해서는 다음과 같이 구성한다.<br/>
 *     sum(Integer.parseInt("${a}") + Integer.parseInt("${b}"))
 *
 * @author  Minseok Lee
 * @since   2018.02.03
 * @version 1.0
 */
public interface MakeSrcCdWithInputParamSvc {
  /**
   * 입력 파라미터가 조합된 소스코드를 돌려준다.
   *
   * @param baseSrcCd 조합되기 전 입력 파라미터를 변수로 갖고 있는 소스
   * @param input 치환될 입력 값
   * @return 조합된 소스 코드소스 코드
   * @exception Exception 예상하지 못한 Exception으로 정의한다.
   */
  public String makeSrcCd(String baseSrcCd, List<Map<String, Object>> input) throws Exception;
}