package com.cmn.cmn.controller;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.stereotype.Controller;

import com.cmn.cmn.service.FileControlService;

@Controller
public class FileControlController {
  @Autowired
  private FileControlService fileControlService;
  
  private static Logger logger = Logger.getLogger(FileControlController.class);
  
  @RequestMapping(value="/FileDown", method=RequestMethod.GET)
  public String fileDownload(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Map<String, Object> inputMap = new HashMap<String, Object>();
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
}