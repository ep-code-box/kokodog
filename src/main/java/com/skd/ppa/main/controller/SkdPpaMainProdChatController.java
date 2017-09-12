/*
 * Title : SkdPpaMainProdChatController
 *
 * @Version : 1.0
 *
 * @Date : 2016-04-17
 *
 * @Copyright by 이민석
 */
package com.skd.ppa.main.controller;

import java.util.Map;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.stereotype.Controller;

import com.skd.ppa.main.service.GetProdChatService;
import com.cmn.err.SystemException;

/**
 *  이 클래스는 SK 주식회사 C&C DT 프로젝트 일환으로
 *  진행하고 있는 상품 요구명세서 분석 프로젝트의
 *  개발 프로젝트 중 일부에 포함된다.<br/>
 *  상품 개발 Chatbot 기능을ㅈ ㅔ공한다.
 */
@Controller
public class SkdPpaMainProdChatController {
  @Autowired
  private GetProdChatService getProdChatService;
  
  @Autowired
  private SystemException systemException;
  
  private static Logger logger = Logger.getLogger(SkdPpaMainProdChatController.class);
  
  /**
   *  해당 메서드는 /skd/ppa/main/ProdChat URL을 통해 호출된다.
   *  Multipart request 방식으로 호출해야 하며 호출된 파일이
   *  DB Blob 형태로 삽입된다.
   *  @param request : 서블릿 Request
   *  @param response : 서블릿 response
   *  @return Map 타입의 업로드 된 파일의 파일 번호를 담고 있는 형태의 리스트
   *  @throws 기타 Exception
   */
  @RequestMapping(value="/skd/ppa/main/ProdChat", method=RequestMethod.POST)
  @ResponseBody
  public Map<String, Object> main(HttpServletRequest request, HttpServletResponse response) throws Exception {
    validationCheck(request, response);
    Map<String, Object> returnMap = new HashMap<String, Object>();
    returnMap.put("text", getProdChatService.getProdChat(request.getParameter("text"), ((Integer)request.getSession().getAttribute("user_num")).intValue()));
    return returnMap;
  }

  private void validationCheck(HttpServletRequest request, HttpServletResponse response) throws Exception {
    return;
  }
}