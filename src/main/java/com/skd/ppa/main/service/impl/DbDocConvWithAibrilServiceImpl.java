/*
 * Title : DbDocConvWithAibrilServiceImpl
 *
 * @Version : 1.0
 *
 * @Date : 2017-08-10
 *
 * @Copyright by 이민석
 */
package com.skd.ppa.main.service.impl;

import java.io.ByteArrayInputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;


import com.skd.ppa.main.service.DbDocConvWithAibrilService;
import com.skd.ppa.module.service.DocConvWithAibrilService;
import com.skd.ppa.main.dao.DbDocConvWithAibrilDao;
import com.cmn.cmn.service.FileControlService;

/**
 *  이 객체는 Aibril 에서 제공하는 Document Converting 기능을 수행한다.
 *  MS Word, Presentation, Excel의 문서를 HTML형식으로 변환하는 기능을
 *  갖고 있다.
 *  단, DocConvWithAibrilService와는 다르게 DB에 저장되어 있는 파일 데이터를 변환한다.
 */
@Service("dbDocConvWithAibrilService")
public class DbDocConvWithAibrilServiceImpl implements DbDocConvWithAibrilService {
  @Autowired
  private DocConvWithAibrilService docConvWithAibrilService;
  
  @Autowired
  private FileControlService fileControlService;
  
  @Autowired
  private DbDocConvWithAibrilDao dbDocConvWithAibrilDao;
  
  private static Logger logger = Logger.getLogger(DbDocConvWithAibrilServiceImpl.class);
  
  /**
   *  이 메쏘드는 MS word, Presentation, Excel의 문서를
   *  HTML 형식으로 리턴해주는 기능을 갖는다.
   *  @param file - File 형식의 변환 전 파일 위치
   *  @return HTML 형식의 문자열
   *  @throws 기타 모든 예외
   */
  public void convToHtml(String fileKey) throws Exception {
    ServletRequestAttributes sra = null;
    HttpServletRequest request = null;
    Map<String, Object> inputMap = new HashMap<String, Object>();
    byte[] fileContent = fileControlService.getFileContent(fileKey);
    ByteArrayInputStream bais = new ByteArrayInputStream(fileContent);
    String html = docConvWithAibrilService.convToHtml(bais);
    inputMap.put("file_key", fileKey);
    inputMap.put("html", html);
    try {
      sra = (ServletRequestAttributes)RequestContextHolder.currentRequestAttributes();
      request = sra.getRequest();
      if (request == null || request.getSession() == null || request.getSession().getAttribute("user_num") == null) {
        inputMap.put("user_num", 0);
        inputMap.put("now_dtm", new Date());
      } else {
        inputMap.put("user_num", ((Integer)request.getSession().getAttribute("user_num")).intValue());
        inputMap.put("now_dtm", new Date(((Long)request.getSession().getAttribute("system_call_dtm")).longValue()));
      }
    } catch (Exception e) {
      inputMap.put("user_num", 0);
      inputMap.put("now_dtm", new Date());
    }
    dbDocConvWithAibrilDao.insertConvDocHtml(inputMap);
  }
}