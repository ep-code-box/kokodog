/*
 * Title : SkdPpaMainFileUploadController
 *
 * @Version : 1.0
 *
 * @Date : 2016-04-17
 *
 * @Copyright by 이민석
 */
package com.skd.ppa.main.controller;

import java.util.Map;
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

import com.cmn.cmn.service.FileControlService;
import com.skd.ppa.main.service.DocNlpService;

/**
 *  이 클래스는 SK 주식회사 C&C DT 프로젝트 일환으로
 *  진행하고 있는 상품 요구명세서 분석 프로젝트의
 *  개발 프로젝트 중 일부에 포함된다.<br/>
 *  요구명세서 파일 업로드 시 업로드 된 파일을 DB에 삽입하는 역할을 수행한다.
 */
@Controller
public class SkdPpaMainFileUploadController {
  @Autowired
  private DocNlpService docNlpService;
  
  @Autowired
  private FileControlService fileControlService;
  
  private static Logger logger = LogManager.getLogger(SkdPpaMainFileUploadController.class);
  
  /**
   *  해당 메서드는 /skd/ppa/main/FileUpload URL을 통해 호출된다.
   *  Multipart request 방식으로 호출해야 하며 호출된 파일이
   *  DB Blob 형태로 삽입된다.
   *  @param request : 서블릿 Request
   *  @param response : 서블릿 response
   *  @return Map 타입의 업로드 된 파일의 파일 번호를 담고 있는 형태의 리스트
   *  @throws 기타 Exception
   */
  @SuppressWarnings("unchecked")
  @RequestMapping(value="/skd/ppa/main/FileUpload", method=RequestMethod.POST)
  @ResponseBody
  public Object main(HttpServletRequest request, HttpServletResponse response) throws Exception {
    logger.debug("Start method of SkdPpaMainFileUploadController.main[/skd/ppa/main/FileUpload]");
    validationCheck(request, response);
    return (Map<Object, Object>)((List<Object>)fileControlService.insertFile(request).get("__img_info")).get(0);
  }

  private void validationCheck(HttpServletRequest request, HttpServletResponse response) throws Exception {
    return;
  }
}