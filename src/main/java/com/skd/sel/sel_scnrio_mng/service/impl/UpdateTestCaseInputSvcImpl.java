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
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import com.skd.sel.sel_scnrio_mng.service.UpdateTestCaseInputSvc;
import com.skd.sel.sel_scnrio_mng.dao.UpdateTestCaseInputDao;
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
@Service("updateTestCaseInputSvc")
public class UpdateTestCaseInputSvcImpl implements UpdateTestCaseInputSvc {
  
  @Autowired
  private UpdateTestCaseInputDao updateTestCaseInputDao;
  /**
   * 해당 시나리오 번호, 케이스번호 및 입력명에 대한 입력값을 수정한다.
   *
   * @param inputMap Map 형태로 아래 정보를 포함하고 있다.
   *        scnrio_num - 시나리오 번호(필수)
   *        case_num - 케이스 번호(필수)
   *        input - 리스트 형태로 아래 Map 정보를 저장한다.
   *                input_nm - 입력명(필수)
   *                input_val - 입력값(필수)
   * @return 다음 형식의 리스트
   *        del_yn - 삭제여부(Y/N)
   * @exception Exception 예상하지 못한 Exception으로 정의한다.
   */
  public List<Boolean> updateTestCaseInput(Map<String, Object> inputMap) throws Exception {
    Map<String, Object> outputMap = null;
    Map<String, Object> daoInputMap = new HashMap<String, Object>();
    List<Boolean> outputList = new ArrayList<Boolean>();
    ServletRequestAttributes sra = null;
    HttpServletRequest request = null;
    long systemCallDtm = 0L;
    int userNum = 0;
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
    for (int i = 0; i < ((List<Map<String, Object>>)inputMap.get("input")).size(); i++) {
      outputMap = updateTestCaseInputDao.getTestCaseInputByInputNm(((List<Map<String, Object>>)inputMap.get("input")).get(i));
      if (outputMap != null && outputMap.get("input_num") != null) {
        daoInputMap.clear();
        daoInputMap.put("scnrio_num", inputMap.get("scnrio_num"));
        daoInputMap.put("case_num", inputMap.get("case_num"));
        daoInputMap.put("input_num", outputMap.get("input_num"));
        daoInputMap.put("system_call_dtm", new Date(systemCallDtm));
        daoInputMap.put("user_num", userNum);
        updateTestCaseInputDao.delTestCaseInputByInputNm(daoInputMap);
        daoInputMap.clear();
        daoInputMap.put("scnrio_num", inputMap.get("scnrio_num"));
        daoInputMap.put("case_num", inputMap.get("case_num"));
        daoInputMap.put("input_num", outputMap.get("input_num"));
        daoInputMap.put("input_val", ((List<Map<String, Object>>)inputMap.get("input")).get(i).get("input_val"));
        daoInputMap.put("system_call_dtm", new Date(systemCallDtm));
        daoInputMap.put("user_num", userNum);
        updateTestCaseInputDao.insertNewTestCaseInput(daoInputMap);
        outputList.add(true);
      } else {
        outputList.add(false);
      }
    }
    return outputList;
  }
}