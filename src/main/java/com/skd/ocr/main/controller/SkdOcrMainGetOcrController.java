/*
 * Title : SkdOcrMainGetOcrController
 *
 * @Version : 1.0
 *
 * @Date : 2016-04-17
 *
 * @Copyright by 이민석
 */
package com.skd.ocr.main.controller;

import java.util.List;
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

import com.skd.ocr.main.service.GetImgOcrInfoByFileKeyService;
import com.cmn.err.SystemException;

/**
 *  이 클래스는 SK 주식회사 C&C DT 프로젝트 일환으로
 *  진행하고 있는 상품 요구명세서 분석 프로젝트의
 *  개발 프로젝트 중 일부에 포함된다.<br/>
 *  기존에 등록하였던 HTML 변환 파일을 재조회하기 위함이 이 클래스의 주 사용 목적이다.
 */
@Controller
public class SkdOcrMainGetOcrController {
  @Autowired
  private GetImgOcrInfoByFileKeyService getImgOcrInfoByFileKeyService;
  
  @Autowired
  private SystemException systemException;
  
  private static Logger logger = LogManager.getLogger(SkdOcrMainGetOcrController.class);
  
  /**
   *  해당 메서드는 /skd/ocr/main/GetOcr URL을 통해 호출된다.
   *  기존에 등록하였던 파일을 재조회하기 위한 리스트이다.
   *  DB에 담겨 있는 전체 파일 업로드 파일 리스트를 file_key와 파일명, 등록일시를 기준으로
   *  전체 데이터를 가져온다.
   *  @param request : 서블릿 Request
   *  @param response : 서블릿 response
   *  @return List 타입의 업로드 된 파일의 파일 키, 등록일시, 파일명을 갖고 있는 전체 목록
   *  @throws 기타 Exception
   */
  @RequestMapping(value="/skd/ocr/main/GetOcr", method=RequestMethod.POST)
  @ResponseBody
  public List<Map<String, Object>> main(HttpServletRequest request, HttpServletResponse response) throws Exception {
    logger.debug("Start method of SkdOcrMainGetOcrController.main[/skd/ocr/main/GetOcr]");
    validationCheck(request, response);
    return getImgOcrInfoByFileKeyService.getImgOcrByFileKey(request.getParameter("file_key"));
  }

  private void validationCheck(HttpServletRequest request, HttpServletResponse response) throws Exception {
    if (request.getParameter("file_key") == null || request.getParameter("file_key").length() == 0) {
      throw systemException.systemException(3);
    }
    return;
  }
}