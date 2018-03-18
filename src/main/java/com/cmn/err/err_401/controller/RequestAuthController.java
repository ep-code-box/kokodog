/*
 * Title : GetPageAuthController
 *
 * @Version : 1.0
 *
 * @Date : 2016-04-17
 *
 * @Copyright by 이민석
 */
package com.cmn.err.err_401.controller;

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

import com.cmn.err.err_401.service.GetPageAuthService;

/**
 *  이 클래스는 권한을 요청할 수 있는 Request 를 호출하는 Controller이다.
 */
@Controller
public class RequestAuthController {
  @Autowired
  private GetPageAuthService getPageAuthService;
  
  private static Logger logger = LogManager.getLogger(RequestAuthController.class);
  
  /**
   *  해당 메서드는 /cmn/err/err_401/RequestAuth URL을 통해 호출된다.
   *  사용자가 페이지에 대한 권한이 없을 때 관리자에게 페이지 권한 요청을 할 수 있는 기능을 수행한다.
   *  @param request : 서블릿 Request
   *  @param response : 서블릿 response
   *  @return List 타입의 해당 페이지를 접근할 수 있는 전체 리스트
   *  @throws 기타 Exception
   */
  @RequestMapping(value="/cmn/err/err_401/RequestAuth", method=RequestMethod.POST)
  @ResponseBody
  public Map<String, Object> main(HttpServletRequest request, HttpServletResponse response) throws Exception {
    logger.debug("Start method of RequestAuthController.main[/cmn/err/err_401/RequestAuth]");
    validationCheck(request, response);
    getPageAuthService.requestAuth(Integer.parseInt(request.getParameter("auth_num")), ((Integer)request.getSession().getAttribute("user_num")).intValue());
    return new HashMap<String, Object>();
  }

  private void validationCheck(HttpServletRequest request, HttpServletResponse response) throws Exception {
    return;
  }
}