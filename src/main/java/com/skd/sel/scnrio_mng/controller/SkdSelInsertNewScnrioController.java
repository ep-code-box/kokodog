package com.skd.sel.sel_scnrio_mng.controller;

import java.util.Date;
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

import com.skd.sel.sel_scnrio_mng.service.InsertNewScnrioSvc;
import com.cmn.err.SystemException;
/**
 *  이 클래스는 SK 주식회사 C&C DT 프로젝트 일환으로
 *  진행하고 있는 셀레니움을 통한 테스트 자동화를 목적으로 수행한다.<br/>
 *  새로운 시나리오를 등록해주는 역할을 수행한다.
 */
@Controller
public class SkdSelInsertNewScnrioController {
  @Autowired
  private InsertNewScnrioSvc insertNewScnrioSvc;
  
  @Autowired
  private SystemException systemException;
  
  private static Logger logger = LogManager.getLogger(SkdSelInsertNewScnrioController.class);
  
  /**
   *  해당 메서드는 /skd/sel/sel_scnrio_mng/InsertNewScnrio URL을 통해 호출된다.<br/>
   *  입력값으로 전달받은 시나리오 명 및 시나리오 설명을 토대로 신규 시나리오번호를 채번하여 신규 시나리오를 등록한다.<br/>
   *  리턴하는 대상은 다음과 같다.<br/>
   *  <ul>
   *  <li> 형식 : JSON 형식(List) </li>
   *  <li>데이터 리스트</li>
   *  <ul>
   *  <li>scnrio_num : 시나리오 번호</li>
   *  </ul>
   *  </ul>
   *
   *  @param request 서블릿 Request
   *          필수 파라미터 - 시나리오명(scnrio_nm)
   *          선택 파라미터 - 시나리오 설명(scnrio_desc)
   *  @param response 서블릿 response
   *  @return Map 타입의 시나리오번호
   *  @throws 기타 Exception
   */
  @RequestMapping(value="/skd/sel/sel_scnrio_mng/InsertNewScnrio", method=RequestMethod.POST)
  @ResponseBody
  public Map<String, Object> main(HttpServletRequest request, HttpServletResponse response) throws Exception {
    logger.debug("Start method of SkdSelInsertNewScnrioController.main[/skd/sel/sel_scnrio_mng/InsertNewScnrio]");
    validationCheck(request, response);
    Map<String, Object> inputMap = new HashMap<String, Object>();
    Map<String, Object> outputMap = new HashMap<String, Object>();
    inputMap.put("scnrio_nm", request.getParameter("scnrio_nm"));
    inputMap.put("scnrio_desc", request.getParameter("scnrio_desc"));
    outputMap.put("scnrio_num", insertNewScnrioSvc.insertNewScnrio(inputMap));
    return outputMap;
  }

  private void validationCheck(HttpServletRequest request, HttpServletResponse response) throws Exception {
    if (request.getParameter("scnrio_nm") == null) {
      throw systemException.systemException(3, "scnrio_nm");
    }
    return;
  }
}