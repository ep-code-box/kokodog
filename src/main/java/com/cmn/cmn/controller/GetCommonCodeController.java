/*
 * Title : DistributeQuery
 *
 * @Version : 1.0
 *
 * @Date : 2017-08-18
 *
 * @Copyright by 이민석
 */
package com.cmn.cmn.controller;

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

import com.cmn.cmn.service.GetCodeValService;
import com.cmn.err.SystemException;

/**
 *  이 객체는 공통코드 리스트가 리턴되도록 기능을 제공하는 컨트롤러 클래스이다.
 */
@Controller
public class GetCommonCodeController {
  private static Logger logger = LogManager.getLogger(GetCommonCodeController.class);
  
  @Autowired
  private GetCodeValService getCodeValService;

  /**
    *  공통코드 리스트를 리턴해주는 Controller이다.
    *  /cmn/cmn/GetCommonCode URL을 통해 호출 가능하다.
    *  parameter로 반드시 code 파라미터가 양수로 담겨있어야 한다.
    *  @param request - 서블릿 Request    
    *  @param response - 서블릿 응답이 정의된 response
    *  @return - 공통코드 리스트가 담겨있는 List
    */
  @RequestMapping(value="/cmn/cmn/main/GetCommonCode", method=RequestMethod.POST)
  @ResponseBody
  public Map<Integer, String> main(HttpServletRequest request, HttpServletResponse response) throws Exception {
    logger.debug("Start of controller com.cmn.cmn.controller.GetCommonCodeController[/cmn/cmn/main/GetCommonCode]");
    validationCheck(request, response);
    return getCodeValService.getCodeVal(Integer.parseInt(request.getParameter("code")));
  }
  
  /**
    *  Input parameter 등의 유효성 체크
    *  @param request - 서블릿 Request    
    *  @param response - 서블릿 응답이 정의된 response
    *  @return - 없음  
    */
  private void validationCheck(HttpServletRequest request, HttpServletResponse response) throws Exception {
    int codeNum = 0;
    if (request.getParameter("code") == null) {
      throw new SystemException(3, "code");
    }
    try {
      codeNum = Integer.parseInt(request.getParameter("code"));
    } catch (NumberFormatException e) {
      throw new SystemException(9, "code", request.getParameter("code"));
    }
    if (codeNum <= 0) {
      throw new SystemException(9, "code", request.getParameter("code"));
    }
  }
}