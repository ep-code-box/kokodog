package com.skd.sel.sel_scnrio_mng.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import com.skd.sel.sel_scnrio_mng.service.UpdateTestExptRsltSvc;
import com.skd.sel.sel_scnrio_mng.dao.UpdateTestExptRsltDao;
import com.cmn.err.UserException;
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
@Service("updateTestExptRsltSvc")
public class UpdateTestExptRsltSvcImpl implements UpdateTestExptRsltSvc {
  @Autowired
  private UpdateTestExptRsltDao updateTestExptRsltDao;
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
  public void updateTestExptRslt(int scnrioNum, int caseNum, List<Map<String, Object>> inputList) throws Exception {
    Map<String, Object> outputMap = null;
    Map<String, Object> daoInputMap = new HashMap<String, Object>();
    ServletRequestAttributes sra = null;
    HttpServletRequest request = null;
    long systemCallDtm = 0L;
    int userNum = 0;
    int i = 0;
    try {
      sra = (ServletRequestAttributes)RequestContextHolder.currentRequestAttributes();
      request = sra.getRequest();
      if (request == null || request.getSession() == null || request.getSession().getAttribute("user_num") == null) {
        systemCallDtm = new Date().getTime();
        userNum = 0;
      } else {
        systemCallDtm = ((Long)request.getAttribute("system_call_dtm")).longValue();
        userNum = ((Integer)request.getSession().getAttribute("user_num")).intValue();
      }
    } catch (Exception e) {
      systemCallDtm = new Date().getTime();
      userNum = 0;
    }
    for (i = 0; i < inputList.size(); i++) {
      if (((Integer)inputList.get(i).get("modify_typ")).intValue() == 2 || ((Integer)inputList.get(i).get("modify_typ")).intValue() == 3) {
        daoInputMap.clear();
        daoInputMap.put("system_call_dtm", new Date(systemCallDtm));
        daoInputMap.put("user_num", userNum);
        daoInputMap.put("scnrio_num", scnrioNum);
        daoInputMap.put("case_num", caseNum);
        if (((Integer)inputList.get(i).get("modify_typ")).intValue() == 2) {
          daoInputMap.put("test_step_num", inputList.get(i).get("old_test_step_num"));
        } else {
          daoInputMap.put("test_step_num", inputList.get(i).get("test_step_num"));
        }
        updateTestExptRsltDao.delTestExptRsltByStepNum(daoInputMap);
      }
    }
    for (i = 0; i < inputList.size(); i++) {
      if (((Integer)inputList.get(i).get("modify_typ")).intValue() == 1 || ((Integer)inputList.get(i).get("modify_typ")).intValue() == 2) {
        daoInputMap.clear();
        daoInputMap.put("system_call_dtm", new Date(systemCallDtm));
        daoInputMap.put("user_num", userNum);
        daoInputMap.put("scnrio_num", scnrioNum);
        daoInputMap.put("case_num", caseNum);
        daoInputMap.put("test_step_num", inputList.get(i).get("test_step_num"));
        daoInputMap.put("rslt_strd", inputList.get(i).get("rslt_strd"));
        daoInputMap.put("judg_typ_cd", inputList.get(i).get("judg_typ_cd"));
        try {
          updateTestExptRsltDao.insertTestExptRslt(daoInputMap);
        } catch (Exception e) {
          if (e instanceof DuplicateKeyException == true) {
            throw new UserException(24, "테스트스텝번호", "테스트스텝번호 : " + inputList.get(i).get("test_step_num"));
          }
        }
      }
    }
  }
}