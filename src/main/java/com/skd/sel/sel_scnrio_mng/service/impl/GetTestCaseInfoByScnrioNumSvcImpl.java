package com.skd.sel.sel_scnrio_mng.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skd.sel.sel_scnrio_mng.service.GetTestCaseInfoByScnrioNumSvc;
import com.skd.sel.sel_scnrio_mng.dao.GetTestCaseInfoByScnrioNumDao;

/**
 * 이 클래스는 Selenium 테스트 자동화 도구 개발 프로젝트 내에서
 * 시스템 관리를 위해 시나리오 번호에 해당하는 케이스의 정보 리스트를 돌려주는 역할을 수행한다.
 * 케이스 정보는 케이스번호, 케이스명, 케이스설명을 포함하고 있다.
 * 입력값은 시나리오 번호이며 필수값이다.<br/>
 *
 * @author  Minseok Lee
 * @since   2018.02.03
 * @version 1.0
 */
@Service("getTestCaseInfoByScnrioNumSvcImpl")
public class GetTestCaseInfoByScnrioNumSvcImpl implements GetTestCaseInfoByScnrioNumSvc {
  private static Logger logger = LogManager.getLogger(GetTestCaseInfoByScnrioNumSvcImpl.class);
  
  @Autowired
  private GetTestCaseInfoByScnrioNumDao getTestCaseInfoByScnrioNumDao;

  /**
   * 시나리오 번호에 해당하는 케이스의 전체 리스트를 되돌려준다.
   *
   * @param scnrio_num 시나리오번호(필수))
   * @return 아래 정보를 Map으로 갖고 있는 List
   *         case_num : 케이스 번호
   *         case_nm : 케이스명
   *         case_desc : 케이스 설명
   * @exception Exception 예상하지 못한 Exception으로 정의한다.
   */
  public List<Map<String, Object>> getTestCaseInfoByScnrioNum(Map<String, Object> inputMap) throws Exception {
    return getTestCaseInfoByScnrioNumDao.getTestCaseInfoByScnrioNum(inputMap);
  }
}