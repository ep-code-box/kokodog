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

import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.stereotype.Controller;

import com.cmn.err.err_401.service.GetPageAuthService;
import com.cmn.err.SystemException;

/**
 *  이 클래스는 권한을 요청할 수 있는 Request 를 호출하는 Controller이다.
 */
@Controller
public class GetPageAuthController {
  @Autowired
  private GetPageAuthService getPageAuthService;
  
  @Autowired
  private SystemException systemException;

  private static Logger logger = Logger.getLogger(GetPageAuthController.class);
  
  /**
   *  해당 메서드는 /cmn/err/err_401/GetPagetAuth URL을 통해 호출된다.
   *  사용자가 페이지에 대한 권한이 없을 때 관리자에게 페이지 권한 요청을 할 수 있는 기능을 수행한다.
   *  @param request : 서블릿 Request
   *  @param response : 서블릿 response
   *  @return List 타입의 해당 페이지를 접근할 수 있는 전체 리스트
   *  @throws 기타 Exception
   */
  @RequestMapping(value="/cmn/err/err_401/GetPagetAuth", method=RequestMethod.POST)
  @ResponseBody
  public List<Map<String, Object>> main(HttpServletRequest request, HttpServletResponse response) throws Exception {
    validationCheck(request, response);
    return getPageAuthService.getAuthListByPath(request.getParameter("path"), ((Integer)request.getSession().getAttribute("user_num")).intValue());
  }

  private void validationCheck(HttpServletRequest request, HttpServletResponse response) throws Exception {
    return;
  }
}