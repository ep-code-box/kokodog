package com.skd.sel.sel_scnrio_mng.controller;

import java.util.HashMap;
import java.util.List;
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

import com.skd.sel.sel_scnrio_mng.service.GetTestCaseInfoByScnrioNumSvc;
import com.cmn.err.SystemException;
/**
 *  이 클래스는 SK 주식회사 C&C DT 프로젝트 일환으로
 *  진행하고 있는 셀레니움을 통한 테스트 자동화를 목적으로 수행한다.<br/>
 *  케이스 정보를 되돌려주는 메서드를 포함한 컨트롤러이다.
 */
@Controller
public class SkdSelGetTestCaseInfoByScnrioNumController {
  @Autowired
  private GetTestCaseInfoByScnrioNumSvc getTestCaseInfoByScnrioNumSvc;
  
  @Autowired
  private SystemException systemException;
  
  private static Logger logger = LogManager.getLogger(SkdSelGetTestCaseInfoByScnrioNumController.class);
  
  /**
   *  해당 메서드는 /skd/sel/sel_scnrio_mng/GetTestCaseInfoByScnrioNum URL을 통해 호출된다.<br/>
   *  입력값으로 전달받은 시나리오 번호를 기반으로 케이스 정보를 되돌려준다..<br/>
   *  케이스는 여러 건이 될 수 있으며, 케이스에 관련 정보는 케이스번호, 케이스명, 케이스 상세 정보가 있다.<br/>
   *  리턴하는 대상은 다음과 같다.<br/>
   *  <ul>
   *  <li> 형식 : JSON 형식(List) </li>
   *  <li>데이터 리스트</li>
   *  <ul>
   *  <li>case_num : 케이스 번호</li>
   *  <li>case_nm : 케이스명</li>
   *  <li>case_desc : 케이스 설명</li>
   *  </ul>
   *  </ul>
   *
   *  @param request 서블릿 Request
   *          필수 파라미터 - 없음
   *  @param response 서블릿 response
   *  @return List 타입의 케이스정보 전체 리스트
   *  @throws 기타 Exception
   */
  @RequestMapping(value="/skd/sel/sel_scnrio_mng/GetTestCaseInfoByScnrioNum", method=RequestMethod.POST)
  @ResponseBody
  public List<Map<String, Object>> main(HttpServletRequest request, HttpServletResponse response) throws Exception {
    logger.debug("Start method of SkdSelGetTestCaseInfoByScnrioNumController.main[/skd/sel/sel_scnrio_mng/GetTestCaseInfoByScnrioNum]");
    validationCheck(request, response);
    Map<String, Object> inputMap = new HashMap<String, Object>();
    inputMap.put("scnrio_num", Integer.parseInt(request.getParameter("scnrio_num")));
    return getTestCaseInfoByScnrioNumSvc.getTestCaseInfoByScnrioNum(inputMap);
  }

  private void validationCheck(HttpServletRequest request, HttpServletResponse response) throws Exception {
    int tempNum = 0;
    if (request.getParameter("scnrio_num") == null) {
      throw systemException.systemException(3, "scnrio_num");
    }
    try {
      tempNum = Integer.parseInt(request.getParameter("scnrio_num"));
    } catch (NumberFormatException e) {
      throw systemException.systemException(9, "scnrio_num", request.getParameter("scnrio_num"));
    }
    return;
  }
}