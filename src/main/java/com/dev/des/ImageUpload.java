/*
 * Title : ImageUpload
 *
 * @Version : 1.0
 *
 * @Date : 2016-04-17
 *
 * @Copyright by 이민석
 */
package com.dev.des.image_manage;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cmn.err.SystemException;
import com.cmn.err.UserException;
import com.cmn.cmn.service.FileControlService;

/**
 *  이 객체는 등록된 쿼리를 개발기에 배포 한다.
 */
@Controller
public class ImageUpload {
  @Autowired
  private SqlSession sqlSession;
  
  @Autowired
  private SystemException systemException;
  
  @Autowired
  private UserException userException;
  
  @Autowired
  private FileControlService fileControlService;
  
  private static Logger logger = Logger.getLogger(ImageUpload.class);
  
  /**
    *  개발기에 쿼리를 배포하는 메서드
    *  @param request - 서블릿 Request    
    *  @param response - 서블릿 응답이 정의된 response
    *  @return - 데이터 없는 Map
    */
  @RequestMapping(value="/dev/des/image_manage/ImageUpload", method=RequestMethod.POST)
  @ResponseBody
  public Map main(HttpServletRequest request, HttpServletResponse response) throws Exception {
    validation(request, response);
    List<Map<String, Object>> outputList = new ArrayList<Map<String, Object>>();
    Map<String, Object> outputMap = null;
    Map<String, Object> inputMap = new HashMap<String, Object>();
    outputMap = fileControlService.insertFile(request);
    logger.debug("OutputMap of method fileControlService.insertFile[" + outputMap + "]");
    for (int i = 0; i < ((List)outputMap.get("__img_info")).size(); i++) {
      inputMap.clear();
      inputMap.put("img_name", outputMap.get("img_name"));
      inputMap.put("img_memo", outputMap.get("img_memo"));
      inputMap.put("file_num", (((Map)((List)outputMap.get("__img_info")).get(i)).get("file_num")));
      inputMap.put("now_dtm", request.getAttribute("now_dtm"));
      inputMap.put("user_num", request.getSession().getAttribute("user_num"));
      logger.debug("Input map of SQL insertDevDesImageInfo - " + inputMap);
      sqlSession.insert("com.dev.des.image_manage.insertDevDesImageInfo", inputMap);
    }
    return new HashMap<String, Object>();
  }
  
  /**
    *  Input parameter 등의 유효성 체크
    *  @param request - 서블릿 Request    
    *  @param response - 서블릿 응답이 정의된 response
    *  @return - 없음  
    */
  private void validation(HttpServletRequest request, HttpServletResponse response) throws Exception {
    boolean isMultipart = ServletFileUpload.isMultipartContent(request);
    if (isMultipart == false) {
      throw systemException.systemException(5);
    }
  }
}