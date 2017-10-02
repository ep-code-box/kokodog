/*
 * Title : GetConvHtmlServiceImpl
 *
 * @Version : 1.0
 *
 * @Date : 2017-08-15
 *
 * @Copyright by 이민석
 */
package com.skd.ppa.main.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skd.ppa.main.service.GetConvHtmlService;
import com.skd.ppa.main.dao.GetConvHtmlDao;

/**
 *  이 객체는 문서 변환 프로세스를 통해 변환되어 저장된 HTML 정보를
 *  돌려주는 역할을 수행한다.
 */
@Service("getConvHtmlService")
public class GetConvHtmlServiceImpl implements GetConvHtmlService {
  @Autowired
  private GetConvHtmlDao getConvHtmlDao;
  
  private static final Logger logger = LogManager.getLogger(GetConvHtmlServiceImpl.class);
  /**
   *  이 메서드는 문서 변환 프로세스를 통해 변환되어 저장된 HTML 정보를
   *  돌려주는 역할을 수행한다.
   *  @param fileKey : HTML로 되돌려줄 파일의 키
   *  @return 결과 HTML
   *  @throws 기타 익셉션
   */
  public String getHtml(String fileKey) throws Exception {
    logger.debug("Start method of GetConvHtmlServiceImpl.getHtml");
    Map<String, Object> inputMap = new HashMap<String, Object>();
    Map<String, Object> outputMap = null;
    inputMap.put("file_key", fileKey);
    outputMap = getConvHtmlDao.getHtml(inputMap);
    return (String)outputMap.get("html");
  }
}