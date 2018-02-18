package com.skd.sel.sel_scnrio_mng.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skd.sel.sel_scnrio_mng.service.GetSrcCdByScnrioNumSvc;
import com.skd.sel.sel_scnrio_mng.dao.GetSrcCdByScnrioNumDao;

/**
 * 이 클래스는 Selenium 테스트 자동화 도구 개발 프로젝트 내에서
 * 시스템 관리를 위해 시나리오 번호에 해당하는 소스코드를 리턴해주는 역할을 수행한다.
 * 입력값은 시나리오 번호이며 필수값이다..<br/>
 *
 * @author  Minseok Lee
 * @since   2018.02.03
 * @version 1.0
 */
@Service("getSrcCdByScnrioNumSvc")
public class GetSrcCdByScnrioNumSvcImpl implements GetSrcCdByScnrioNumSvc {
  private static Logger logger = LogManager.getLogger(GetSrcCdByScnrioNumSvcImpl.class);
  
  @Autowired
  private GetSrcCdByScnrioNumDao getSrcCdByScnrioNumDao;

  /**
   * 시나리오 번호에 해당하는 소스코드를 돌려준다.
   *
   * @param scnrio_num 시나리오번호
   * @return 소스코드
   * @exception Exception 예상하지 못한 Exception으로 정의한다.
   */
  public String getSrcCdByScnrioNum(int scnrioNum) throws Exception {
    Map<String, Object> inputMap = new HashMap<String, Object>();
    inputMap.put("scnrio_num", scnrioNum);
    return (String)getSrcCdByScnrioNumDao.getSrcCdByScnrioNum(inputMap).get("src_cd");
  }
}