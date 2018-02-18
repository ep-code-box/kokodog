package com.skd.sel.sel_scnrio_mng.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import com.skd.sel.sel_scnrio_mng.service.InsertNewCaseSvc;
import com.skd.sel.sel_scnrio_mng.dao.InsertNewCaseDao;
import com.cmn.err.UserException;

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
@Service("insertNewCaseSvc")
public class InsertNewCaseSvcImpl implements InsertNewCaseSvc {
  private static Logger logger = LogManager.getLogger(InsertNewCaseSvcImpl.class);
  
  @Autowired
  private InsertNewCaseDao insertNewCaseDao;
  
  @Autowired
  private UserException userException;

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
  public int insertNewCase(Map<String, Object> inputMap) throws Exception {
    Map<String, Object> outputMap = null;
    Map<String, Object> methodInputMap = new HashMap<String, Object>();
    ServletRequestAttributes sra = null;
    HttpServletRequest request = null;
    methodInputMap.put("case_nm", inputMap.get("case_nm"));
    methodInputMap.put("scnrio_num", inputMap.get("scnrio_num"));
    outputMap = insertNewCaseDao.chkSameCaseNm(methodInputMap);
    if (outputMap.get("is_exist_yn") != null && "Y".equals(outputMap.get("is_exist_yn")) == true) {
      throw userException.userException(22, "케이스명", (String)inputMap.get("case_nm"));
    }
    methodInputMap.clear();
    try {
      sra = (ServletRequestAttributes)RequestContextHolder.currentRequestAttributes();
      request = sra.getRequest();
      if (request == null || request.getSession() == null || request.getSession().getAttribute("user_num") == null) {
        methodInputMap.put("user_num", 0);
        methodInputMap.put("system_call_dtm", new Date());
      } else {
        methodInputMap.put("user_num", ((Integer)request.getSession().getAttribute("user_num")).intValue());
        methodInputMap.put("system_call_dtm", new Date(((Long)request.getAttribute("system_call_dtm")).longValue()));
      }
    } catch (Exception e) {
      methodInputMap.put("user_num", 0);
      methodInputMap.put("system_call_dtm", new Date());
    }
    methodInputMap.put("case_nm", inputMap.get("case_nm"));
    methodInputMap.put("case_desc", inputMap.get("case_desc"));
    methodInputMap.put("scnrio_num", inputMap.get("scnrio_num"));
    insertNewCaseDao.insertNewCase(methodInputMap);
    methodInputMap.clear();
    methodInputMap.put("case_nm", inputMap.get("case_nm"));
    methodInputMap.put("scnrio_num", inputMap.get("scnrio_num"));
    outputMap = insertNewCaseDao.getNewCaseNum(methodInputMap);
    return ((Long)outputMap.get("case_num")).intValue();
  }
}