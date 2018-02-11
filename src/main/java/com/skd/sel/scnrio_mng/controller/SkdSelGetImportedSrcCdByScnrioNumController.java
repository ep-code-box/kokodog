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

import com.skd.sel.sel_scnrio_mng.service.GetImportedSrcCdSvc;
import com.cmn.err.SystemException;
/**
 *  이 클래스는 SK 주식회사 C&C DT 프로젝트 일환으로
 *  진행하고 있는 셀레니움을 통한 테스트 자동화를 목적으로 수행한다.<br/>
 *  시나리오 번호 및 케이스번호를 기준으로 특정 케이스에 대한 변환된 소스코드를 되돌려주는 역할을 수행한다.
 *  시나리오번호는 필수값이며 케이스번호는 선택값이다.
 *  만약 케이스번호가 존재하지 않는다면 해당 시나리오번호에 해당하는 모든 케이스번호의 변환된 소스코드를 되돌려준다.
 */
@Controller
public class SkdSelGetImportedSrcCdByScnrioNumController {
  @Autowired
  private GetImportedSrcCdSvc getImportedSrcCdSvc;
  
  @Autowired
  private SystemException systemException;
  
  private static Logger logger = LogManager.getLogger(SkdSelGetImportedSrcCdByScnrioNumController.class);
  
  /**
   *  해당 메서드는 /skd/sel/sel_scnrio_mng/GetImportedSrcCdByScnrioNum URL을 통해 호출된다.<br/>
   *  입력받은 시나리오 번호/케이스번호를 기반으로 파이썬으로 작성되며 입력값이 변환된 소스코드를 되돌려준다..<br/>
   *  케이스번호가 존재하지 않으면 해당 시나리오의 전체 케이스에 해당하는 소스코드를 되돌려준다.
   *  리턴하는 대상은 다음과 같다.<br/>
   *  <ul>
   *  <li> 형식 : JSON 형식(List) </li>
   *  <li>데이터 리스트</li>
   *  <ul>
   *  <li>case_num: 케이스번호</li>
   *  <li>src_cd : 소스코드</li>
   *  </ul>
   *  </ul>
   *
   *  @param request 서블릿 Request
   *          필수 파라미터 - 없음
   *  @param response 서블릿 response
   *  @return List 타입의 시나리오 번호 전체 리스트
   *  @throws 기타 Exception
   */
  @RequestMapping(value="/skd/sel/sel_scnrio_mng/GetImportedSrcCdByScnrioNum", method=RequestMethod.POST)
  @ResponseBody
  public List<Map<String, Object>> main(HttpServletRequest request, HttpServletResponse response) throws Exception {
    logger.debug("Start method of SkdSelGetImportedSrcCdByScnrioNumController.main[/skd/sel/sel_scnrio_mng/GetImportedSrcCdByScnrioNum]");
    validationCheck(request, response);
    Map<String, Object> inputMap = new HashMap<String, Object>();
    inputMap.put("scnrio_num", Integer.parseInt(request.getParameter("scnrio_num")));
    if (request.getParameter("case_num") != null) {
      inputMap.put("case_num", Integer.parseInt(request.getParameter("input_num")));
    }
    return getImportedSrcCdSvc.getImportedSrcCd(inputMap);
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
    if (request.getParameter("case_num") != null) {
      try {
        tempNum = Integer.parseInt(request.getParameter("case_num"));
      } catch (NumberFormatException e) {
        throw systemException.systemException(9, "case_num", request.getParameter("case_num"));
      }
    }
    return;
  }
}