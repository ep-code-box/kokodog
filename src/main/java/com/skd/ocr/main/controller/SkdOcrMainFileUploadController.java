/*
 * Title : SkdOcrMainFileUploadController
 *
 * @Version : 1.0
 *
 * @Date : 2016-04-17
 *
 * @Copyright by 이민석
 */
package com.skd.ocr.main.controller;

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
import com.skd.ocr.module.service.ImgOcrService;
import com.cmn.err.SystemException;

/**
 *  이 클래스는 SK 주식회사 C&C DT 프로젝트 일환으로
 *  진행하고 있는 상품 요구명세서 분석 프로젝트의
 *  개발 프로젝트 중 일부에 포함된다.<br/>
 *  요구명세서 파일 업로드 시 업로드 된 파일을 DB에 삽입하는 역할을 수행한다.
 */
@Controller
public class SkdOcrMainFileUploadController {
  @Autowired
  private ImgOcrService imgOcrService;
  
  @Autowired
  private FileControlService fileControlService;
  
  @Autowired
  private SystemException systemException;

  private static Logger logger = LogManager.getLogger(SkdOcrMainFileUploadController.class);
  
  /**
   *  해당 메서드는 /skd/ocr/main/FileUpload URL을 통해 호출된다.
   *  Multipart request 방식으로 호출해야 하며 호출된 파일이
   *  DB Blob 형태로 삽입된다.
   *  @param request : 서블릿 Request
   *  @param response : 서블릿 response
   *  @return Map 타입의 업로드 된 파일의 파일 번호를 담고 있는 형태의 리스트
   *  @throws 기타 Exception
   */
  @RequestMapping(value="/skd/ocr/main/FileUpload", method=RequestMethod.POST)
  @ResponseBody
  @SuppressWarnings({"rawtypes", "unchecked"})
  public Map<String, Object> main(HttpServletRequest request, HttpServletResponse response) throws Exception {
    logger.debug("Start method of SkdOcrMainFileUploadController.main[skd/ocr/main/FileUpload");
    validationCheck(request, response);
    Map<String, Object> outputMap = (Map)((List)fileControlService.insertFile(request).get("__img_info")).get(0);
    imgOcrService.insertUploadImgInfo((String)outputMap.get("file_key"), request.getSession().getAttribute("user_num") == null ? 0 : ((Integer)(request.getSession().getAttribute("user_num"))).intValue());
    return outputMap;
  }

  private void validationCheck(HttpServletRequest request, HttpServletResponse response) throws Exception {
    return;
  }
}