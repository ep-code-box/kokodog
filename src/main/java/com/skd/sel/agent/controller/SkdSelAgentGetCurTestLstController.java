package com.skd.sel.agent.controller;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.stereotype.Controller;

import com.skd.sel.agent.service.AgentSvc;
/**
 *  이 클래스는 SK 주식회사 C&C DT 프로젝트 일환으로
 *  진행하고 있는 셀레니움을 통한 테스트 자동화를 목적으로 수행한다.<br/>
 *  현재 시점에 테스트를 진행해야 할 소스코드, 시나리오, 케이스 리스트를 서버에서 받아오는 역할을 수행한다.
 */
@Controller
public class SkdSelAgentGetCurTestLstController {
  @Autowired
  private AgentSvc agentSvc;
  
  private static Logger logger = LogManager.getLogger(SkdSelAgentGetCurTestLstController.class);
  
  /**
   *  해당 메서드는 /skd/sel/agent/GetCurTestLst URL을 통해 호출된다.<br/>
   *  현재 시점 기준으로 셀레니움 테스트를 진행하여야 할 전수 시나리오, 케이스 및
   *  각 소스코드를 모두 가져온다.<br/>
   *  리턴하는 대상은 다음과 같다.<br/>
   *  <ul>
   *  <li> 형식 : JSON 형식(List) </li>
   *  <li>데이터 리스트</li>
   *  <ul>
   *  <li>scniro_num : 시나리오 번호 </li>
   *  <li>exec_expt_dtm : 수행 예정 시각</li>
   *  <li>server_dtm : 서버 시각</li>
   *  <li>test_case : 테스트 케이스</li>
   *  <ul>
   *  <li>case_num : 케이스 번호</li>
   *  <li>src_cd : 소스 코드</li>
   *  </ul>
   *  </ul>
   *  </ul>
   *
   *  @param request 서블릿 Request
   *          필수 파라미터 - 없음
   *  @param response 서블릿 response
   *  @return List 타입의 시나리오 번호 전체 리스트
   *  @throws 기타 Exception
   */
  @RequestMapping(value="/skd/sel/agent/GetCurTestLst", method=RequestMethod.POST)
  @ResponseBody
  public List<Object> main(HttpServletRequest request, HttpServletResponse response) throws Exception {
    logger.debug("Start method of SkdSelAgentGetCurTestLstController.main[/skd/sel/agent/GetCurTestLst]");
    validationCheck(request, response);
    List<Object> outputList = agentSvc.getCurTestLst();
    return outputList;
  }

  private void validationCheck(HttpServletRequest request, HttpServletResponse response) throws Exception {
    return;
  }
}