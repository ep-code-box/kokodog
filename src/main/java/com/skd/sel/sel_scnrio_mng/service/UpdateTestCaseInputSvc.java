package com.skd.sel.sel_scnrio_mng.service;

import java.util.List;
import java.util.Map;
/**
 * 이 클래스는 Selenium 테스트 자동화 도구 개발 프로젝트 내에서
 * 테스트 케이스를 지정하기 위한 입력 데이터를 수정할 때
 * 수정된 입력데이터를 DB에 변경하여 넣는 역할을 수행한다.
 * 다른 사람이 동시 수행하는 것을 가정하여 입력 기준값은
 * 입력명(input_nm)이며 만약 수정하는 동안 다른 사람이 이 입력명을
 * 삭제했을 경우 삭제코드를 List에 담아 돌려준다.
 *
 * @author  Minseok Lee
 * @since   2018.02.03
 * @version 1.0
 */
public interface UpdateTestCaseInputSvc {
  /**
   * 해당 시나리오 번호, 케이스번호 및 입력명에 대한 입력값을 수정한다.
   *
   * @param inputMap Map 형태의 아래 정보를 포함하고 있는 정보
   *        scnrio_num - 시나리오 번호(필수)
   *        case_num - 케이스 번호(필수)
   *        input_nm - 입력명(필수)
   *        input_val - 입력값(필수)
   * @return 다음 형식의 리스트
   *        del_yn - 삭제여부(Y/N)
   * @exception Exception 예상하지 못한 Exception으로 정의한다.
   */
  public List<Boolean> updateTestCaseInput(Map<String, Object> inputMap) throws Exception;
}