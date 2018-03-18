package com.skd.sel.sel_scnrio_mng.controller;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.stereotype.Controller;

import com.skd.sel.sel_scnrio_mng.service.GetTestInputAndExptRsltByCaseNumSvc;
import com.cmn.err.SystemException;
/**
 *  이 클래스는 SK 주식회사 C&C DT 프로젝트 일환으로
 *  진행하고 있는 셀레니움을 통한 테스트 자동화를 목적으로 수행한다.<br/>
 *  해당 테스트 케이스에 해당하는 입력값 정보들과 기대 결과 리스트를 되돌려주는
 *  역할을 수행한다.
 */
@Controller
public class SkdSelGetTestInputAndExptRsltByCaseNumController {
  @Autowired
  private GetTestInputAndExptRsltByCaseNumSvc getTestInputAndExptRsltByCaseNumSvc;
  
  private static Logger logger = LogManager.getLogger(SkdSelGetTestInputAndExptRsltByCaseNumController.class);
  
  /**
   *  해당 메서드는 /skd/sel/sel_scnrio_mng/GetTestInputAndExptRsltByCaseNum URL을 통해 호출된다.<br/>
   *  입력값으로 전달받은 시나리오 번호 및 케이스 번호를 기반으로 저장된 테스트를 위한 입력값 정보와
   *  기대하는 결과 리스트를 되돌려주는 역할을 수행한다.<br/>
   *  리턴하는 대상은 다음과 같다.<br/>
   *  <ul>
   *  <li> 형식 : JSON 형식(List) </li>
   *  <li>데이터 리스트</li>
   *  <ul>
   *  <li>
   *  input : 입력값 정보
   *  <ul>
   *  <li>input_num : 입력번호</li>
   *  <li>input_nm : 입력명</li>
   *  <li>input_val : 입력값</li>
   *  </ul>
   *  </li>
   *  <li>
   *  expr_rslt : 기대결과 정보
   *  <ul>
   *  <li>test_step_num : 테스트 스텝 번호</li>
   *  <li>rslt_strd : 결과 문구</li>
   *  <li>judg_typ_num : 판단유형번호</li>
   *  <li>judg_typ_nm : 판단유형명</li>
   *  </ul>
   *  </li>
   *  </ul>
   *  </ul>
   *
   *  @param request 서블릿 Request
   *          필수 파라미터 - 없음
   *  @param response 서블릿 response
   *  @return List 타입의 케이스정보 전체 리스트
   *  @throws 기타 Exception
   */
  @RequestMapping(value="/skd/sel/sel_scnrio_mng/GetTestInputAndExptRsltByCaseNum", method=RequestMethod.POST)
  @ResponseBody
  public Map<String, Object> main(HttpServletRequest request, HttpServletResponse response) throws Exception {
    logger.debug("Start method of SkdSelGetTestCaseInfoByScnrioNumController.main[/skd/sel/sel_scnrio_mng/GetTestInputAndExptRsltByCaseNum]");
    validationCheck(request, response);
    Map<String, Object> inputMap = new HashMap<String, Object>();
    inputMap.put("scnrio_num", Integer.parseInt(request.getParameter("scnrio_num")));
    inputMap.put("case_num", Integer.parseInt(request.getParameter("case_num")));
    return getTestInputAndExptRsltByCaseNumSvc.getTestInputAndExptRsltByCaseNum(inputMap);
  }

  private void validationCheck(HttpServletRequest request, HttpServletResponse response) throws Exception {
    int tempNum = 0;
    if (request.getParameter("scnrio_num") == null) {
      throw new SystemException(3, "scnrio_num");
    }
    try {
      tempNum = Integer.parseInt(request.getParameter("scnrio_num"));
    } catch (NumberFormatException e) {
      throw new SystemException(9, "scnrio_num", request.getParameter("scnrio_num"));
    }
    if (request.getParameter("scnrio_num") == null) {
      throw new SystemException(3, "scnrio_num");
    }
    try {
      tempNum = Integer.parseInt(request.getParameter("case_num"));
    } catch (NumberFormatException e) {
      throw new SystemException(9, "case_num", request.getParameter("case_num"));
    }
    return;
  }
}