/*
 * Title : MainController
 *
 * @Version : 1.0
 *
 * @Date : 2017-09-25
 *
 * @Copyright by 이민석
 */
package com.cmn.cmn.controller;

import java.math.BigDecimal;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;

import com.cmn.cmn.service.FileControlService;

/**
 *  해당 컨트롤러는 단순 jsp로 되어있는 웹 페이지를 Get 방식으로 요청받았을 때,
 *  view에 jsp 페이지를 설정하여 jsp 페이지를 response로 되돌려주는 역할을 수행하는
 *  controller class이다.
 */
@CrossOrigin(origins="*")
@Controller
public class MainController {
  private static Logger logger = LogManager.getLogger(MainController.class);
  
  @Autowired
  private FileControlService fileControlService;
  /**
   *  일반적인 Page를 로드하도록 요청받았을 경우 요청받은 jsp 파일을 response에 담아 보내준다.
   *  @param request - 서블릿 리퀘스트, 이 리퀘스트의 파라미터로 file_key 파라미터가 반드시 포함되어야 한다.
   *  @param response - 서블릿 response, 메서드 내부에서 처리하는 값은 없으나 최종적으로 파일 컨텐츠가 포함된다.
   *  @return null을 리턴하여 의미가 없다.
   *  @throws 기타 알 수 없는 예외
   */
  @RequestMapping(value="/**", produces="text/*")
  public String main(HttpServletRequest request, HttpServletResponse response) throws Exception {
    logger.debug("Start of controller com.cmn.cmn.controller.MainController[/**]");
    return (String)request.getAttribute("_VIEW_URL");
  }
  
  @RequestMapping(value="/favicon.ico")
  public String getGeneralFavIcon(HttpServletRequest request, HttpServletResponse response) throws Exception {
    logger.debug("Start controller of com.cmn.cmn.controller.getGeneralFavIcon[/favicon.ico]");
    Map<String, Object> outputMap = null;
    int fileNum = 0;
    int contentCnt = 0;
    long fileLength = 0L;
    outputMap = fileControlService.getFileInfo("tId6m0peoFkF2GYcAfAJG48kOE5Djo5ky49MFtnX");
    fileNum = ((Long)outputMap.get("file_num")).intValue();
    contentCnt = ((Long)outputMap.get("content_cnt")).intValue();
    fileLength = ((BigDecimal)outputMap.get("file_length")).longValue();
    response.setContentType("application/octet-stream");
    response.setHeader("Content-Disposition", "attachment; fileName=\"favicon\";");
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

}