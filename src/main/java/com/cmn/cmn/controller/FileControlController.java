/*
 * Title : FileControlController
 *
 * @Version : 1.0
 *
 * @Date : 2017-09-25
 *
 * @Copyright by 이민석
 */
package com.cmn.cmn.controller;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;

import com.cmn.cmn.service.FileControlService;
import com.cmn.err.SystemException;

/**
 *  해당 컨트롤러는 파일 다운로드 처리를 하는 컨트롤러이다.
 *  파일은 DB에 미리 저장되어 있어야 하고 DB에 있는 파일을 가져와서 클라이언트에 전달해주는 역할을 수행한다.
 *  cmn_file_content 테이블에는 하나의 파일이 사이즈별로 분산되어서 저장되어 있는데
 *  이 분산되어 있는 단위로 클라이언트에 파일을 내려준다.
 */
@Controller
public class FileControlController {
  @Autowired
  private FileControlService fileControlService;
  
  @Autowired
  private SystemException systemException;
  
  private static Logger logger = LogManager.getLogger(FileControlController.class);
  
  /**
   *  파일 다운로드를 수행하는 함수이며, /FileDown path를 통해 이 메서드가 수행된다.
   *  @param request - 서블릿 리퀘스트, 이 리퀘스트의 파라미터로 file_key 파라미터가 반드시 포함되어야 한다.
   *  @param response - 서블릿 response, 메서드 내부에서 처리하는 값은 없으나 최종적으로 파일 컨텐츠가 포함된다.
   *  @return null을 리턴하여 의미가 없다.
   *  @throws 기타 알 수 없는 예외
   */
  @RequestMapping(value="/FileDown", produces={"image/*", "audio/*", "video/*", "application/*", "!application/json"})
  public String fileDownload(HttpServletRequest request, HttpServletResponse response) throws Exception {
    validation(request, response);
    logger.debug("Start controller of com.cmn.cmn.controller.fileDownload[/FileDown]");
    Map<String, Object> outputMap = null;
    int fileNum = 0;
    String fileNm = null;
    int contentCnt = 0;
    long fileLength = 0L;
    outputMap = fileControlService.getFileInfo(request.getParameter("file_key"));
    fileNum = ((Long)outputMap.get("file_num")).intValue();
    fileNm = (String)outputMap.get("file_nm");
    contentCnt = ((Long)outputMap.get("content_cnt")).intValue();
    fileLength = ((BigDecimal)outputMap.get("file_length")).longValue();
    response.setContentType("application/octet-stream");
    response.setHeader("Content-Disposition", "attachment; fileName=\"" + URLEncoder.encode(fileNm, "UTF-8") + "\";");
    response.setHeader("Content-Transfer-Encoding", "binary");
    response.setContentType("application/octet-stream");
    response.setContentLength((int)fileLength);
    for (int i = 0; i < contentCnt; i++) {
      response.getOutputStream().write(fileControlService.getFileContent(fileNum, i + 1));
      response.getOutputStream().flush();
      outputMap.clear();
    }   
    response.getOutputStream().close();
    return null;
  }
  
  private void validation(HttpServletRequest request, HttpServletResponse response) throws Exception {
    if (request.getParameter("file_key") == null) {
      throw systemException.systemException(3, "file_key");
    }
  }
}