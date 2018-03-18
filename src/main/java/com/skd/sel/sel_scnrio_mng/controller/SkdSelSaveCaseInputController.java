package com.skd.sel.sel_scnrio_mng.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.stereotype.Controller;

import com.skd.sel.sel_scnrio_mng.service.UpdateTestCaseInputSvc;
/**
 *  이 클래스는 SK 주식회사 C&C DT 프로젝트 일환으로
 *  진행하고 있는 셀레니움을 통한 테스트 자동화를 목적으로 수행한다.<br/>
 *  해당 시나리오 및 케이스에 해당하는 테스트를 위한 입력값을 저장하는 역할을 수행한다.
 */
@Controller
public class SkdSelSaveCaseInputController {
  @Autowired
  private UpdateTestCaseInputSvc updateTestCaseInputSvc;
  
  private static Logger logger = LogManager.getLogger(SkdSelSaveCaseInputController.class);
  
  /**
   *  해당 메서드는 /skd/sel/sel_scnrio_mng/SaveCaseInput URL을 통해 호출된다.<br/>
   *  입력값으로 전달받은 시나리오 번호, 케이스번호를 바탕으로 입력값을 변경한다.<br/>
   *  리턴하는 대상은 다음과 같다.<br/>
   *  <ul>
   *  <li> 형식 : JSON 형식(List) </li>
   *  <li>데이터 리스트</li>
   *  <ul>
   *  <li>scnrio_num : 시나리오 번호</li>
   *  <li>case_num : 케이스 번호</li>
   *  </ul>
   *  </ul>
   *
   *  @param request 서블릿 Request
   *          필수 파라미터 - 시나리오번호(scnrio_num)
   *          선택 파라미터 - 시나리오 소스 코드(src_cd)
   *  @param response 서블릿 response
   *  @return Map 타입의 시나리오번호
   *  @throws 기타 Exception
   */
  @RequestMapping(value="/skd/sel/sel_scnrio_mng/SaveCaseInput", method=RequestMethod.POST)
  @ResponseBody
  public List<Boolean> main(HttpServletRequest request, HttpServletResponse response) throws Exception {
    logger.debug("Start method of SkdSelSaveCaseInputController.main[/skd/sel/sel_scnrio_mng/SaveCaseInput]");
    validationCheck(request, response);
    Map<String, Object> inputMap = new HashMap<String, Object>();
    List<Boolean> outputList = null;
    Map<String, Object> tmpMap = null;
    JSONObject obj = (JSONObject)JSONSerializer.toJSON(request.getParameter("data"));
    inputMap.put("scnrio_num", ((Integer)obj.get("scnrio_num")).intValue());
    inputMap.put("case_num", ((Integer)obj.get("case_num")).intValue());
    List<Map<String, Object>> inputList = new ArrayList<Map<String, Object>>();
    for (int i = 0; i < ((JSONArray)obj.get("input")).size(); i++) {
      tmpMap = new HashMap<String, Object>();
      tmpMap.put("input_nm", ((String)((JSONObject)((JSONArray)obj.get("input")).get(i)).get("input_nm")));
      tmpMap.put("input_val", ((String)((JSONObject)((JSONArray)obj.get("input")).get(i)).get("input_val")));
      inputList.add(tmpMap);
    }
    inputMap.put("input", inputList);
    outputList = updateTestCaseInputSvc.updateTestCaseInput(inputMap);
    return outputList;
  }

  private void validationCheck(HttpServletRequest request, HttpServletResponse response) throws Exception {
    return;
  }
}