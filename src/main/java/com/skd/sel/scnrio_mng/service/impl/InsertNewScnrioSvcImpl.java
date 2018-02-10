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

import com.skd.sel.sel_scnrio_mng.service.InsertNewScnrioSvc;
import com.skd.sel.sel_scnrio_mng.dao.InsertNewScnrioDao;
import com.cmn.err.UserException;

/**
 * 이 클래스는 Selenium 테스트 자동화 도구 개발 프로젝트 내에서
 * 시스템 관리를 위해 신규 시나리오 등록 요청 발생 시
 * 시나리오번호를 채번하여 신규 시나리오번호를 등록해주는 역할을 한다.
 * input은 시나리오명, 시나리오설명을 받아
 * 최종 결과값으로 시나리오번호를 되돌려주는 역할을 수행한다.
 *
 * @author  Minseok Lee
 * @since   2018.02.03
 * @version 1.0
 */
@Service("insertNewScnrioSvc")
public class InsertNewScnrioSvcImpl implements InsertNewScnrioSvc {
  private static Logger logger = LogManager.getLogger(InsertNewScnrioSvcImpl.class);
  
  @Autowired
  private InsertNewScnrioDao insertNewScnrioDao;
  
  @Autowired
  private UserException userException;

  /**
   * 신규 시나리오명과 시나리오설명을 받아 채번된 시나리오번호를 리턴한다.
   *
   * @param inputMap Map 형태의 아래 정보를 포함하고 있는 정보
   *        scnrio_nm - 시나리오 명(필수)
   *        scnior_desc - 시나리오 설명(선택)
   * @return 시나리오 번호
   * @exception Exception 예상하지 못한 Exception으로 정의한다.
   */
  public int insertNewScnrio(Map<String, Object> inputMap) throws Exception {
    Map<String, Object> outputMap = null;
    Map<String, Object> methodInputMap = new HashMap<String, Object>();
    ServletRequestAttributes sra = null;
    HttpServletRequest request = null;
    methodInputMap.put("scnrio_nm", inputMap.get("scnrio_nm"));
    outputMap = insertNewScnrioDao.chkSameScnrioNm(methodInputMap);
    if (outputMap.get("is_exist_yn") != null && "Y".equals(outputMap.get("is_exist_yn")) == true) {
      throw userException.userException(22, "시나리오명", (String)inputMap.get("scnrio_nm"));
    }
    methodInputMap.clear();
/*    try {
      sra = (ServletRequestAttributes)RequestContextHolder.currentRequestAttributes();
      request = sra.getRequest();
      if (request == null || request.getSession() == null || request.getSession().getAttribute("user_num") == null) {
        methodInputMap.put("user_num", 0);
        methodInputMap.put("system_call_dtm", new Date());
      } else {
        methodInputMap.put("user_num", ((Integer)request.getSession().getAttribute("user_num")).intValue());
        methodInputMap.put("system_call_dtm", new Date(((Long)request.getSession().getAttribute("system_call_dtm")).longValue()));
      }
    } catch (Exception e) {
      throw e;
    }
*/
    methodInputMap.put("user_num", inputMap.get("user_num"));
    methodInputMap.put("system_call_dtm", intpuMap.get("system_call_dtm"));
    methodInputMap.put("scnrio_nm", inputMap.get("scnrio_nm"));
    methodInputMap.put("scnrio_desc", inputMap.get("scnrio_desc"));
    insertNewScnrioDao.insertNewScnrio(methodInputMap);
    methodInputMap.clear();
    methodInputMap.put("scnrio_nm", inputMap.get("scnrio_nm"));
    outputMap = insertNewScnrioDao.getNewScnrioNum(methodInputMap);
    return ((Long)outputMap.get("scnrio_num")).intValue();
  }
}