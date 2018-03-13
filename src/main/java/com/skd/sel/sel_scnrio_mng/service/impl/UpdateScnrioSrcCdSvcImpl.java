package com.skd.sel.sel_scnrio_mng.service;

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

import com.skd.sel.sel_scnrio_mng.service.UpdateScnrioSrcCdSvc;
import com.skd.sel.module.service.MakeSrcCdWithInputParamSvc;
import com.skd.sel.sel_scnrio_mng.dao.UpdateScnrioSrcCdDao;
/**
 * 이 클래스는 Selenium 테스트 자동화 도구 개발 프로젝트 내에서
 * 소스코드를 수정할 때
 * 수정된 소스코드를 DB에 넣는 역할을 수행한다.
 * 소스코드를 넣을 때, 소스 코드 내에 input 값을 자동으로 찾아
 * input 값으로 활용할 수 있도록 DB를 가공해야 하며
 * input값의 순서 변경 시에 각 case에 대한 정보까지 흐트러지지 않도록 보정이 필요하다.
 *
 * @author  Minseok Lee
 * @since   2018.02.03
 * @version 1.0
 */
@Service("updateScnrioSrcCdSvc")
public class UpdateScnrioSrcCdSvcImpl implements UpdateScnrioSrcCdSvc {
  
  @Autowired
  private UpdateScnrioSrcCdDao updateScnrioSrcCdDao;
  
  @Autowired
  private MakeSrcCdWithInputParamSvc makeSrcCdWithInputParamSvc;
  /**
   * 해당 시나리오 번호에 해당하는 소스코드를 수정한다.
   *
   * @param inputMap Map 형태의 아래 정보를 포함하고 있는 정보
   *        scnrio_num - 시나리오 번호(필수)
   *        src_cd - 소스코드(필수)
   * @exception Exception 예상하지 못한 Exception으로 정의한다.
   */
  public void updateScnrioSrcCd(Map<String, Object> inputMap) throws Exception {
    List<Map<String, Object>> inputValList = null;
    Map<String, Object> daoInputMap = new HashMap<String, Object>();
    Map<String, Object> daoOutputMap = null;
    ServletRequestAttributes sra = null;
    HttpServletRequest request = null;
    long systemCallDtm = 0L;
    int userNum = 0;
    int i = 0;
    int j = 0;
    int seq = 0;
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
    inputValList = makeSrcCdWithInputParamSvc.getSrcCdInputNm((String)inputMap.get("src_cd"));
    for (i = 0; i < inputValList.size(); i++) {
      daoInputMap.clear();
      daoInputMap.put("scnrio_num", inputMap.get("scnrio_num"));
      daoInputMap.put("input_nm", inputValList.get(i).get("input_nm"));
      daoInputMap.put("system_call_dtm", new Date(systemCallDtm));
      daoOutputMap = updateScnrioSrcCdDao.getTestInputInfoByScnrioNumAndInputNm(daoInputMap);
      if (daoOutputMap != null && daoOutputMap.get("input_num") != null && ((Long)daoOutputMap.get("input_num")).intValue() == seq + 1) {
        seq++;
      } else {
        for (j = 0; j < i; j++) {
          if (inputValList.get(i).get("input_nm").equals(inputValList.get(j).get("input_nm")) == true) {
            break;
          }
        }
        if (j == i) {
          daoInputMap.clear();
          daoInputMap.put("scnrio_num", inputMap.get("scnrio_num"));
          daoInputMap.put("input_num",  seq + 1);
          daoInputMap.put("system_call_dtm", new Date(systemCallDtm));
          daoInputMap.put("user_num", userNum);
          updateScnrioSrcCdDao.delTestCaseInputWithScnrioNumAndInputNum(daoInputMap);
          daoInputMap.clear();
          daoInputMap.put("scnrio_num", inputMap.get("scnrio_num"));
          daoInputMap.put("input_num", seq + 1);
          daoInputMap.put("input_nm", inputValList.get(i).get("input_nm"));
          daoInputMap.put("system_call_dtm", new Date(systemCallDtm));
          daoInputMap.put("user_num", userNum);
          updateScnrioSrcCdDao.insertChangedInputNumWithScnrioNumAndCaseNumAndInputNm(daoInputMap);
          daoInputMap.clear();
          daoInputMap.put("scnrio_num", inputMap.get("scnrio_num"));
          daoInputMap.put("input_num", seq + 1);
          daoInputMap.put("system_call_dtm", new Date(systemCallDtm));
          daoInputMap.put("user_num", userNum);
          updateScnrioSrcCdDao.delTestInputWithScnrioNumAndInputNm(daoInputMap);
          daoInputMap.put("input_nm", inputValList.get(i).get("input_nm"));
          daoInputMap.clear();
          daoInputMap.put("scnrio_num", inputMap.get("scnrio_num"));
          daoInputMap.put("input_num", seq + 1);
          daoInputMap.put("input_nm", inputValList.get(i).get("input_nm"));
          daoInputMap.put("input_desc", daoOutputMap == null ? null : daoOutputMap.get("input_desc"));
          daoInputMap.put("system_call_dtm", new Date(systemCallDtm));
          daoInputMap.put("user_num", userNum);
          updateScnrioSrcCdDao.insertNewTestInput(daoInputMap);
          seq++;
        }
      }
    }
    daoInputMap.clear();
    daoInputMap.put("scnrio_num", inputMap.get("scnrio_num"));
    daoInputMap.put("system_call_dtm", new Date(systemCallDtm));
    daoInputMap.put("user_num", userNum);
    updateScnrioSrcCdDao.delTestScnrio(daoInputMap);
    daoInputMap.clear();
    daoInputMap.put("scnrio_num", inputMap.get("scnrio_num"));
    daoInputMap.put("src_cd", inputMap.get("src_cd"));
    daoInputMap.put("system_call_dtm", new Date(systemCallDtm));
    daoInputMap.put("user_num", userNum);
    updateScnrioSrcCdDao.insertNewSrcCdWithScnrioNum(daoInputMap);
    daoInputMap.clear();
    daoInputMap.put("scnrio_num", inputMap.get("scnrio_num"));
    daoInputMap.put("system_call_dtm", new Date(systemCallDtm));
    daoInputMap.put("user_num", userNum);
    daoInputMap.put("input_num", seq + 1);
    updateScnrioSrcCdDao.delTestInputBiggerThanInputNum(daoInputMap);
    daoInputMap.clear();
    daoInputMap.put("scnrio_num", inputMap.get("scnrio_num"));
    daoInputMap.put("system_call_dtm", new Date(systemCallDtm));
    daoInputMap.put("user_num", userNum);
    daoInputMap.put("input_num", seq + 1);
    updateScnrioSrcCdDao.delTestCaseInputBiggerThanInputNum(daoInputMap);
  }
}