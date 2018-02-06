package com.skd.sel.sel_scnrio_mng.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skd.sel.sel_scnrio_mng.service.GetTestInputAndExptRsltByCaseNumSvc;
import com.skd.sel.sel_scnrio_mng.dao.GetTestInputByCaseNumDao;

/**
 * 이 클래스는 Selenium 테스트 자동화 도구 개발 프로젝트 내에서
 * 시스템 관리를 위해 시나리오 번호와 케이스 번호에 해당하는 데이터에 저장된
 * 각 입력값 및 기대 결과값을 되돌려주는 역할을 수행한다.
 * 메인 메서드는 getTestCaseInfoByScnrioNum 메서드인데
 * 최종 결과 값은 Map 내에 key가 input에 input값에 해당하는 정보를 삽입하며
 * expt_rslt 에는 기대 결과값을 되돌려준다.
 * input내에 다시 Map으로 구성되어 입력번호(input_num), 입력명(input_nm), 입력값(input_val) 정보를 포함하고 있으며
 * expt_rslt에는 테스트스텝번호(test_step_num), 결과기준(rslt_strd), 판단유형번호(judg_typ_num) 정보를 포함한다.
 *
 * @author  Minseok Lee
 * @since   2018.02.03
 * @version 1.0
 */
@Service("getTestInputAndExptRsltByCaseNumSvc")
public class GetTestInputAndExptRsltByCaseNumSvcImpl implements GetTestInputAndExptRsltByCaseNumSvc {
  private static Logger logger = LogManager.getLogger(GetTestCaseInfoByScnrioNumSvcImpl.class);
  
  @Autowired
  private GetTestInputByCaseNumDao getTestInputByCaseNumDao;

  /**
   * 시나리오 번호에 해당하는 입력정보 및 기대결과 정보를 포함한다.
   *
   * @param inputMap Map 형태의 아래 정보를 포함하고 있는 정보
   *        scnrio_num - 시나리오 번호(필수)
   *        case_num - 케이스 번호(필수)
   * @return 아래 정보를 Map으로 갖고 있는 정보
   *         input - 입력 정보
   *               input_num - 입력번호
   *               input_nm - 입력명
   *               input_val - 입력값
   *         expt_rslt : 기대결과
   *               test_step_num - 테스트 스텝 번호
   *               rslt_strd : 결과기준
   *               judg_typ_num : 판단유형번호
   * @exception Exception 예상하지 못한 Exception으로 정의한다.
   */
  public Map<String, Object> getTestInputAndExptRsltByCaseNum(Map<String, Object> inputMap) throws Exception {
    Map<String, Object> outputMap = new HashMap<String, Object>();
    outputMap.put("input", getTestInputByCaseNumDao.getTestInputByCaseNum(inputMap));
    return outputMap;
  }
}