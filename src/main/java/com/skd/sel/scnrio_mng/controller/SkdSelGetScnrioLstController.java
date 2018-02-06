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

import com.skd.sel.sel_scnrio_mng.service.GetScnrioLstSvc;
import com.cmn.err.SystemException;
/**
 *  이 클래스는 SK 주식회사 C&C DT 프로젝트 일환으로
 *  진행하고 있는 셀레니움을 통한 테스트 자동화를 목적으로 수행한다.<br/>
 *  전체 시나리오 리스트를 리턴해주는 메서드를 포함한 컨트롤러이다.
 */
@Controller
public class SkdSelGetScnrioLstController {
  @Autowired
  private GetScnrioLstSvc getScnrioLstSvc;
  
  @Autowired
  private SystemException systemException;
  
  private static Logger logger = LogManager.getLogger(SkdSelGetScnrioLstController.class);
  
  /**
   *  해당 메서드는 /skd/sel/sel_scnrio_mng/GetScnrioLst URL을 통해 호출된다.<br/>
   *  전체 시나리오 목록을 검색어를 기준으로 리턴해준다.<br/>
   *  페이징 기능이 있어 페이지별로 40건의 시나리오를 가져오도록 구성되어 있다.<br/>
   *  리턴하는 대상은 다음과 같다.<br/>
   *  <ul>
   *  <li> 형식 : JSON 형식(List) </li>
   *  <li>데이터 리스트</li>
   *  <ul>
   *  <li>scniro_num : 시나리오 번호 </li>
   *  <li>scnrio_nm : 시나리오명</li>
   *  <li>scnrio_desc : 시나리오 내용</li>
   *  </ul>
   *  </ul>
   *
   *  @param request 서블릿 Request
   *          필수 파라미터 - 없음
   *  @param response 서블릿 response
   *  @return List 타입의 시나리오 번호 전체 리스트
   *  @throws 기타 Exception
   */
  @RequestMapping(value="/skd/sel/sel_scnrio_mng/GetScnrioLst", method=RequestMethod.POST)
  @ResponseBody
  public List<Map<String, Object>> main(HttpServletRequest request, HttpServletResponse response) throws Exception {
    logger.debug("Start method of SkdSelGetScnrioLstController.main[/skd/sel/sel_scnrio_mng/GetScnrioLst]");
    validationCheck(request, response);
    Map<String, Object> inputMap = new HashMap<String, Object>();
    inputMap.put("page_num", request.getParameter("page_num"));
    inputMap.put("sch_txt", request.getParameter("sch_txt"));
    List<Map<String, Object>> outputList = getScnrioLstSvc.getScnrioLst(inputMap);
    return outputList;
  }

  private void validationCheck(HttpServletRequest request, HttpServletResponse response) throws Exception {
    int tempNum = 0;
    if (request.getParameter("page_num") != null) {
      try {
        tempNum = Integer.parseInt(request.getParameter("page_num"));
      } catch (NumberFormatException e) {
        throw systemException.systemException(9, "page_num", request.getParameter("page_num"));
      }
    }
    return;
  }
}