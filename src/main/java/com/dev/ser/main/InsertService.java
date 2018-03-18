/*
 * Title : InsertService
 *
 * @Version : 1.0
 *
 * @Date : 2016-03-29
 *
 * @Copyright by 이민석
 */

package com.dev.ser.main;

import java.util.Map;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import org.apache.ibatis.session.SqlSession;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.cmn.err.SystemException;

/**
 *  신규 서비스를 생성한다.
 */
@Controller
public class InsertService {
  @Autowired
  private SqlSession sqlSession;
  
  private static Logger logger = LogManager.getLogger(InsertService.class);

  /**
    *  서비스 개발을 위해 전체 페이지 리스트를 가져오는 쿼리
    *  @param request - 서블릿 Request
    *  @param response - 서블릿 응답이 정의된 response
    *  @return - 링크 정보를 포함한 모델
    */
  @RequestMapping(value="/dev/ser/main/InsertService", method=RequestMethod.POST)
  @ResponseBody
  public Map<String, Object> main(HttpServletRequest request, HttpServletResponse response) throws Exception {
    validation(request, response);
    Map<String, Object> inputMap = new HashMap<String, Object>();
    inputMap.put("cd_num", 17);
    logger.debug("Input map of SQL getDevSerNewServiceSource - " + inputMap);
    Map<String, Object> outputMap = sqlSession.selectOne("getDevSerNewServiceSource", inputMap);
    logger.debug("Output map of SQL getDevSerNewServiceSource - " + outputMap);
    String source = (String)outputMap.get("source");
    inputMap.clear();
    inputMap.put("pgm_num", Integer.parseInt(request.getParameter("pgm_num")));
    inputMap.put("task_num", Integer.parseInt(request.getParameter("task_num")));
    inputMap.put("page_num", Integer.parseInt(request.getParameter("page_num")));
    inputMap.put("service_name", request.getParameter("service_name"));
    logger.debug("Input map of SQL getDevSerNewServiceInfo - " + inputMap);
    outputMap = sqlSession.selectOne("getDevSerNewServiceInfo", inputMap);
    logger.debug("Output map of SQL getDevSerNewServiceInfo - " + outputMap);
    source = changeParameter(source, (String)outputMap.get("pgm_abb"), (String)outputMap.get("task_abb"), (String)outputMap.get("page_abb"), request.getParameter("service_name"), ((String)(request.getAttribute("now_dtm"))).substring(10));
    inputMap.clear();
    inputMap.put("pgm_num", Integer.parseInt(request.getParameter("pgm_num")));
    inputMap.put("task_num", Integer.parseInt(request.getParameter("task_num")));
    inputMap.put("page_num", Integer.parseInt(request.getParameter("page_num")));
    inputMap.put("service_name", request.getParameter("service_name"));
    inputMap.put("source", source);
    inputMap.put("user_num", ((Integer)(request.getSession().getAttribute("user_num"))).intValue());
    inputMap.put("now_dtm", request.getAttribute("now_dtm"));
    logger.debug("Input map of SQL insertDevSerServiceSource - " + inputMap);
    sqlSession.insert("insertDevSerServiceSource", inputMap);
    inputMap.clear();
    outputMap = sqlSession.selectOne("getDevSerLastService", inputMap);
    logger.debug("Output map of SQL getDevSerLastService - " + outputMap);
    Map<String, Object> returnMap = new HashMap<String, Object>();
    returnMap.put("service_num", outputMap.get("service_num"));
    returnMap.put("source", source);
    return returnMap;
  }
  
  /**
    *  Input parameter 등의 유효성 체크
    *  @param request - 서블릿 Request    
    *  @param response - 서블릿 응답이 정의된 response
    *  @return - 없음
    */
  private void validation(HttpServletRequest request, HttpServletResponse response) throws Exception {
    if (request.getParameter("pgm_num") == null) {
      throw new SystemException(3, "pgm_num");
    }
    int tmpPgmNum = 0;
    try {
      tmpPgmNum = Integer.parseInt(request.getParameter("pgm_num"));
    } catch (NumberFormatException e) {
      throw new SystemException(9, "pgm_num", request.getParameter("pgm_num"));
    }
    if (tmpPgmNum <= 0) {
      throw new SystemException(9, "pgm_num", request.getParameter("pgm_num"));      
    }
    if (request.getParameter("task_num") == null) {
      throw new SystemException(3, "task_num");
    }
    int tmpTaskNum = 0;
    try {
      tmpTaskNum = Integer.parseInt(request.getParameter("task_num"));
    } catch (NumberFormatException e) {
      throw new SystemException(9, "task_num", request.getParameter("task_num"));
    }
    if (tmpTaskNum <= 0) {
      throw new SystemException(9, "task_num", request.getParameter("task_num"));      
    }
    if (request.getParameter("page_num") == null) {
      throw new SystemException(3, "page_num");
    }
    int tmpPageNum = 0;
    try {
      tmpPageNum = Integer.parseInt(request.getParameter("page_num"));
    } catch (NumberFormatException e) {
      throw new SystemException(9, "page_num", request.getParameter("page_num"));
    }
    if (tmpPageNum <= 0) {
      throw new SystemException(9, "page_num", request.getParameter("page_num"));      
    }
    if (request.getParameter("service_name") == null) {
      throw new SystemException(3, "service_name");
    }
    if (checkServiceName(request.getParameter("service_name")) == false) {
      throw new SystemException(13, "서비스명", request.getParameter("service_name"));      
    }
    if (isExistPastServiceName(Integer.parseInt(request.getParameter("pgm_num")),
                               Integer.parseInt(request.getParameter("task_num")),
                               Integer.parseInt(request.getParameter("page_num")),
                               request.getParameter("service_name")) == true) {
      throw new SystemException(10, "서비스명");            
    }
  }
  
  private String changeParameter(String prevSource, String pgm, String task, String page, String serviceName, String today) throws Exception {
    int index = 0;
    int pos = 0;
    String retSource = new String(prevSource);
    while ((pos = retSource.indexOf("#", index)) >= 0) {
      if (retSource.charAt(pos + 1) == '#') {
        retSource = retSource.substring(0, pos) + retSource.substring(pos + 1);
        index = pos + 1;
      } else {
        int nextSharp = retSource.indexOf("#", pos + 1);
        if (nextSharp < 0) {
          throw new SystemException(5);
        }
        String tempStr = retSource.substring(pos + 1, nextSharp);
        logger.debug("tempStr - " + tempStr);
        if (tempStr.equals("pgm") == true) {
          retSource = retSource.substring(0, pos) + pgm + retSource.substring(nextSharp + 1);
          index = pos + pgm.length() + 1;
        } else if (tempStr.equals("task") == true) {
          retSource = retSource.substring(0, pos) + task + retSource.substring(nextSharp + 1);          
          index = pos + task.length() + 1;
        } else if (tempStr.equals("page") == true) {
          retSource = retSource.substring(0, pos) + page + retSource.substring(nextSharp + 1);
          index = pos + page.length() + 1;
        } else if (tempStr.equals("service_name") == true) {
          retSource = retSource.substring(0, pos) + serviceName + retSource.substring(nextSharp + 1);         
          index = pos + serviceName.length() + 1;
        } else if (tempStr.equals("today") == true) {
          retSource = retSource.substring(0, pos) + today + retSource.substring(nextSharp + 1);
          index = pos + today.length() + 1;
        } else {
          throw new SystemException(5);
        }
      }
    }
    return retSource;
  }
  
  private boolean checkServiceName(String name) {
    if (name.charAt(0) < 'A' || name.charAt(0) > 'Z') {
      return false;
    }
    for (int i = 1; i < name.length(); i++) {
      if ((name.charAt(i) < 'A' || name.charAt(i) > 'Z') && ((name.charAt(i) < 'a') || name.charAt(i) > 'z')) {
        return false;
      }
    }
    return true;
  }
  
  private boolean isExistPastServiceName(int pgmNum, int taskNum, int pageNum, String serviceName) throws Exception {
    Map<String, Object> inputMap = new HashMap<String, Object>();
    inputMap.put("pgm_num", pgmNum);
    inputMap.put("task_num", pgmNum);
    inputMap.put("page_num", pgmNum);
    inputMap.put("service_name", serviceName);
    logger.debug("Input map of SQL getDevSerCheckServiceNameExist - " + inputMap);
    Map<String, Object> outputMap = sqlSession.selectOne("getDevSerCheckServiceNameExist", inputMap);
    logger.debug("Output map of SQL getDevSerCheckServiceNameExist - " + outputMap);
    if (outputMap == null || outputMap.get("service_name_exist") == null || outputMap.get("service_name_exist").equals("Y") == true) {
      return true;
    }
    return false;
  }
}