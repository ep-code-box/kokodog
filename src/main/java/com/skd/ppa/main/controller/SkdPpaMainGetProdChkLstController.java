

/*
 * Title : SkdPpaMainRefreshCahtBotController
 *
 * @Version : 1.0
 *
 * @Date : 2016-04-17
 *
 * @Copyright by 이민석
 */
package com.skd.ppa.main.controller;

import java.util.List;
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

import com.skd.ppa.main.service.DocNlpService;
import com.cmn.err.SystemException;

/**
 *  이 클래스는 SK 주식회사 C&C DT 프로젝트 일환으로
 *  진행하고 있는 상품 요구명세서 분석 프로젝트의
 *  개발 프로젝트 중 일부에 포함된다.<br/>
 *  상품 개발 체크리스트를 돌려준다.
 */
@Controller
public class SkdPpaMainGetProdChkLstController {
  @Autowired
  private DocNlpService docNlpService;
  
  @Autowired
  private SystemException systemException;
  
  private static Logger logger = Logger.getLogger(SkdPpaMainGetProdChkLstController.class);
  
  /**
   *  해당 메서드는 /skd/ppa/main/GetProdChkLst URL을 통해 호출된다.
   *  상품봇의 전체 체크리스트 결과를 되돌려준다.
   *  @param request : 서블릿 Request
   *  @param response : 서블릿 response
   *  @return Map 타입의 업로드 된 파일의 파일 번호를 담고 있는 형태의 리스트
   *  @throws 기타 Exception
   */
  @RequestMapping(value="/skd/ppa/main/GetProdChkLst", method=RequestMethod.POST)
  @ResponseBody
  public Map<String, Object> main(HttpServletRequest request, HttpServletResponse response) throws Exception {
    validationCheck(request, response);
    Map<String, Object> returnMap = null;
    returnMap = docNlpService.getProdChkLstDetail(request.getParameter("file_key"));
    return returnMap;
  }

  private void validationCheck(HttpServletRequest request, HttpServletResponse response) throws Exception {
    if (request.getParameter("file_key") == null || request.getParameter("file_key").length() < 10) {
      throw systemException.systemException(3, "file_key");
    }
  }
}