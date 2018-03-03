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

import com.skd.sel.sel_scnrio_mng.service.UpdateTestExptRsltSvc;
import com.cmn.err.SystemException;
/**
 *  이 클래스는 SK 주식회사 C&C DT 프로젝트 일환으로
 *  진행하고 있는 셀레니움을 통한 테스트 자동화를 목적으로 수행한다.<br/>
 *  해당 시나리오 및 케이스에 테스트 기대 결과 설정값을 저장한다.
 */
@Controller
public class SkdSelSaveTestExptRsltController {
  @Autowired
  private UpdateTestExptRsltSvc updateTestExptRsltSvc;
  
  @Autowired
  private SystemException systemException;
  
  private static Logger logger = LogManager.getLogger(SkdSelSaveTestExptRsltController.class);
  
  /**
   *  해당 메서드는 /skd/sel/sel_scnrio_mng/SaveTestExptRslt URL을 통해 호출된다.<br/>
   *  입력값으로 전달받은 시나리오 번호 및 케이스번호에 해당하는 각 기대결과 설정값을 DB에 저장한다.
   *
   *  @param request 서블릿 Request
   *          필수 파라미터 - 시나리오번호(data)
   *  @param response 서블릿 response
   *  @return Map 타입의 시나리오번호
   *  @throws 기타 Exception
   */
  @RequestMapping(value="/skd/sel/sel_scnrio_mng/SaveTestExptRslt", method=RequestMethod.POST)
  @ResponseBody
  public Map<String, Object> main(HttpServletRequest request, HttpServletResponse response) throws Exception {
    logger.debug("Start method of SkdSelSaveTestExptRsltController.main[/skd/sel/sel_scnrio_mng/SaveTestExptRslt]");
    validationCheck(request, response);
    Map<String, Object> inputMap = null;
    JSONArray arr = (JSONArray)JSONSerializer.toJSON(request.getParameter("data"));
    List<Map<String, Object>> inputList = new ArrayList<Map<String, Object>>();
    for (int i = 0; i < arr.size(); i++) {
      inputMap = new HashMap<String, Object>();
      inputMap.put("test_step_num", arr.getJSONObject(i).get("test_step_num"));
      inputMap.put("judg_typ_cd", arr.getJSONObject(i).get("judg_typ_cd"));
      inputMap.put("rslt_strd", arr.getJSONObject(i).get("rslt_strd"));
      inputMap.put("modify_typ", arr.getJSONObject(i).get("modify_typ"));
      inputMap.put("old_test_step_num", arr.getJSONObject(i).get("old_test_step_num"));
      inputList.add(inputMap);
    }
    updateTestExptRsltSvc.updateTestExptRslt(Integer.parseInt(request.getParameter("scnrio_num")), Integer.parseInt(request.getParameter("case_num")), inputList);
    return new HashMap<String, Object>();
  }

  private void validationCheck(HttpServletRequest request, HttpServletResponse response) throws Exception {
    if (request.getParameter("scnrio_num") == null) {
      throw systemException.systemException(3, "scnrio_num");
    }
    if (request.getParameter("case_num") == null) {
      throw systemException.systemException(3, "case_num");
    }
    return;
  }
}