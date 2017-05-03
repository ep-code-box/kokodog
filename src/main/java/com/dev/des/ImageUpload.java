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

import java.io.File;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
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
  private static long FILE_MAX_SIZE = 2 * 1024 * 1024 * 1024;
  private static int FILE_THREADHOLD_SIZE = 1 * 1024 * 1024;
  private static long FILE_MAX_MEMORY_SIZE = 10 * 1024;
  private static String UPLOAD_DIR = "/var/lib/tomcat7/webapps/ROOT/WEB-INF/files";
  
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
    File tempDirectory = new File(UPLOAD_DIR);
    DiskFileItemFactory factory = new DiskFileItemFactory();
    factory.setSizeThreshold(FILE_THREADHOLD_SIZE);
    factory.setRepository(tempDirectory);
    ServletFileUpload upload = new ServletFileUpload(factory);
    upload.setSizeMax(FILE_MAX_SIZE);
    upload.setHeaderEncoding("UTF-8");
    String fileName = null;
    List items = upload.parseRequest(request);  
    Iterator iter = items.iterator();
    Map<String, Object> resultMap = new HashMap<String, Object>();
    Map<String, Object> outputMap = null;
    Map<String, Object> inputMap = new HashMap<String, Object>();
    Map<String, Object> parameterTempMap = new HashMap<String, Object>();
    List<Map<String, Object>> outputList = new ArrayList<Map<String, Object>>();
    try {
      while (iter.hasNext() == true) {
        FileItem item = (FileItem) iter.next();
        if (item.isFormField() == false) {
          logger.debug("IsFormField is false");
          logger.debug("=================================");
          logger.debug("Content-type : " + item.getContentType());
          logger.debug("Field Name : " + item.getFieldName());
          logger.debug("Name : " + item.getName());
          logger.debug("Size : " + item.getSize());
          logger.debug("=================================");
          fileName = item.getName();
          outputMap = fileControlService.insertFile(item.getInputStream(), fileName,
                                                    request.getSession().getAttribute("user_num") == null ? 0 : ((Integer)request.getSession().getAttribute("user_num")).intValue(),
                                                    (String)request.getSession().getAttribute("now_dtm"));
          outputList.add(outputMap);
        } else {
          logger.debug("IsFormField is true");
          logger.debug("=================================");
          logger.debug("Content-type : " + item.getContentType());
          logger.debug("Field Name : " + item.getFieldName());
          logger.debug("Name : " + item.getName());
          logger.debug("Size : " + item.getSize());
          logger.debug("String : " + item.getString());
          logger.debug("=================================");
          parameterTempMap.put(item.getFieldName(), item.getString());
        }
      }
      for (int i = 0; i < outputList.size(); i++) {
        inputMap.clear();
        inputMap.putAll(parameterTempMap);
        inputMap.put("file_num", outputList.get(i).get("file_num"));
        inputMap.put("now_dtm", request.getAttribute("now_dtm"));
        inputMap.put("user_num", request.getSession().getAttribute("user_num"));
        logger.debug("Input map of SQL insertDevDesImageInfo - " + inputMap);
        sqlSession.insert("insertDevDesImageInfo", inputMap);        
      }
    } catch (Exception e) {
      for (int i = 0; i < outputList.size(); i++) {
        fileControlService.deleteFile(((Integer)outputList.get(i).get("file_num")).intValue());
      }
      throw e;
    }
    return resultMap;
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