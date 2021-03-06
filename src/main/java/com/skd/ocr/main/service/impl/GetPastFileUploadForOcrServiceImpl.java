/*
 * Title : GetPastFileUploadForOcrServiceImpl
 *
 * @Version : 1.0
 *
 * @Date : 2016-04-17
 *
 * @Copyright by 이민석
 */
package com.skd.ocr.main.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import com.skd.ocr.main.service.GetPastFileUploadForOcrService;
import com.skd.ocr.main.dao.GetPastFileUploadForOcrDao;

/**
 *  이 객체는 SK 주식회사 C&C DT 프로젝트 일환으로
 *  진행하고 있는  분석 프로젝트의
 *  개발 프로젝트 중 일부에 포함된다.<br/>
 *  기존에 미리 업로드된 파일 리스트를 가져오는 역할을 수행한다.
 */
@Service("getPastFileUploadForOcrService")
public class GetPastFileUploadForOcrServiceImpl implements GetPastFileUploadForOcrService {
  @Autowired
  private GetPastFileUploadForOcrDao getPastFileUploadForOcrDao;

  private static final Logger logger = LogManager.getLogger(GetImgOcrInfoByFileKeyServiceImpl.class);
  /**
   *  기존에 등록하였던 파일을 재조회하여 다시 처리하기 위한 서비스이다.
   *  DB에 담겨 있는 전체 파일 업로드 파일 리스트를 file_key와 파일명, 등록일시를 기준으로
   *  전체 데이터를 가져온다.
   *  @return List 타입으로 내부 요소는 Map<String, Object> 형태를 가지며 file_key, 등록일시, 파일명을 결과로 전달한다.
   *  @throws 기타 Exception
   */
  public List<Map<String, Object>> getPastFileUploadList() throws Exception {
    logger.debug("Start method of GetPastFileUploadForOcrServiceImpl.getPastFileUploadList");
    return getPastFileUploadForOcrDao.getPastFileUpload();
  }
  
  /**
   *  기존에 등록하였던 파일을 재조회 할 때, 더 이상 필요없는 파일을 삭제하고자 함이다.
   *  @param fileKey 삭제를 진행하고자 하는 파일의 file_key
   *  @throws 기타 Exception
   */
  public void deletePastFileUpload(String fileKey) throws Exception {
    logger.debug("Start method of GetPastFileUploadForOcrServiceImpl.deletePastFileUpload");
    HttpServletRequest request = null;
    Map<String, Object> inputMap = new HashMap<String, Object>();
    inputMap.put("file_key", fileKey);
    try {
      request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
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
    getPastFileUploadForOcrDao.deletePastFileUpload(inputMap);
  }
}