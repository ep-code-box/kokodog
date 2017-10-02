/*
 * Title : SkdPpaMainGetMorphemeDetailController
 *
 * @Version : 1.0
 *
 * @Date : 2016-04-17
 *
 * @Copyright by 이민석
 */
package com.skd.ppa.main.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

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
 *  해당 클래스는 현재 저장되어있는 파일을
 *  형태소분석을 진행하여 되돌려주는 역할을 수행한다.
 */
@Controller
public class SkdPpaMainGetMorphemeDetailController {
  @Autowired
  private DocNlpService docNlpService;
  
  @Autowired
  private SystemException systemException;
  
  private static Logger logger = LogManager.getLogger(SkdPpaMainGetMorphemeDetailController.class);
  
  /**
   *  해당 메서드는 /skd/ppa/main/ConvHtmlPage URL을 통해 호출된다.
   *  Multipart request 방식으로 호출해야 하며 필수 파라미터는 file_key이고
   *  형태소분석한 결과를 List 형태로 리턴한다.
   *  @param request : 서블릿 Request
   *  @param response : 서블릿 response
   *  @return Map 타입의 업로드 된 파일의 파일 번호를 담고 있는 형태의 리스트
   *  @throws 기타 Exception
   */
  @RequestMapping(value="/skd/ppa/main/GetMorphemeDetailList", method=RequestMethod.POST)
  @ResponseBody
  public JSONArray main(HttpServletRequest request, HttpServletResponse response) throws Exception {
    logger.debug("Start method of SkdPpaMainGetMorphemeDetailController.main[/skd/ppa/main/GetMorphemeDetailList]");
    validationCheck(request, response);
    return docNlpService.getMorphemeDetail(request.getParameter("file_key"));
  }

  private void validationCheck(HttpServletRequest request, HttpServletResponse response) throws Exception {
    if (request.getParameter("file_key") == null) {
      throw systemException.systemException(3, "file_key");
    }
  }
}