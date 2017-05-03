/*
 * Title : SaveService
 *
 * @Version : 1.0
 *
 * @Date : 2016-03-08
 *
 * @Copyright by 이민석
 */

package com.dev.ser.main;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import com.cmn.err.SystemException;
import com.cmn.err.UserException;

/**
 *  이 객체는 등록된 쿼리를 개발기에 배포 한다.
 */
@Controller
public class SaveService {
  @Autowired
  private SqlSession sqlSession;
  
  @Autowired
  private SystemException systemException;
  
  @Autowired
  private UserException userException;
  
  private static Logger logger = Logger.getLogger(SaveService.class);

  /**
    *  개발기에 쿼리를 배포하는 메서드
    *  @param request - 서블릿 Request    
    *  @param response - 서블릿 응답이 정의된 response
    *  @return - 데이터 없는 Map
    */
  @RequestMapping(value="/dev/ser/main/SaveService", method=RequestMethod.POST)
  @ResponseBody
  public Map main(HttpServletRequest request, HttpServletResponse response) throws Exception {
    validation(request, response);
    Map<String, Object> inputMap = new HashMap<String, Object>();
    inputMap.put("service_num", Integer.parseInt(request.getParameter("service_num")));
    Map<String, Object> outputMap = sqlSession.selectOne("getDevSerIsServiceUpdateMode", inputMap);
    boolean isUpdateMode = false;
    if (outputMap == null || outputMap.get("is_update_mode") == null || outputMap.get("is_update_mode").equals("Y") == false) {
      isUpdateMode = false;
    } else {
      isUpdateMode = true;
    }
    inputMap.put("source", request.getParameter("source"));
    inputMap.put("user_num", request.getSession().getAttribute("user_num"));
    if (request.getParameter("service_name") != null) {
      inputMap.put("service_name", request.getParameter("service_name"));
    } else {
      inputMap.put("service_name", null);
    }
    if (request.getParameter("pgm_num") != null) {
      inputMap.put("pgm_num", Integer.parseInt(request.getParameter("pgm_num")));
      inputMap.put("task_num", Integer.parseInt(request.getParameter("task_num")));
      inputMap.put("page_num", Integer.parseInt(request.getParameter("page_num")));
    }
    inputMap.put("now_dtm", request.getAttribute("now_dtm"));
    if (isUpdateMode == false) {
      sqlSession.insert("insertDevSerServiceVersionUp", inputMap);
    } else {
      sqlSession.update("updateDevSerService", inputMap);
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
    int tempServiceNum = 0;
    if (request.getParameter("service_num") == null) {
      throw systemException.systemException(3, "service_num");
    }
    try {
      tempServiceNum = Integer.parseInt(request.getParameter("service_num"));
    } catch (NumberFormatException e) {
      throw systemException.systemException(9, "service_num", request.getParameter("service_num"));
    }
    if (tempServiceNum <= 0) {
      throw systemException.systemException(9, "service_num", request.getParameter("service_num"));
    }
    Map<String, Object> inputMap = new HashMap<String, Object>();
    inputMap.put("service_num", Integer.parseInt(request.getParameter("service_num")));
    inputMap.put("user_num", request.getSession().getAttribute("user_num"));
    Map<String, Object> outputMap = sqlSession.selectOne("getDevSerServiceChgAuth", inputMap);
    if (outputMap == null || outputMap.get("is_auth") == null || outputMap.get("is_auth").equals("Y") == false) {
      throw userException.userException(2);
    }
    if (request.getParameter("source") == null) {
      throw systemException.systemException(9, "source", request.getParameter("source"));
    }
    if (request.getParameter("pgm_num") != null) {
      int tmpPgmNum = 0;
      try {
        tmpPgmNum = Integer.parseInt(request.getParameter("pgm_num"));
      } catch (NumberFormatException e) {
        throw systemException.systemException(9, "pgm_num", request.getParameter("pgm_num"));
      }
      if (tmpPgmNum <= 0) {
        throw systemException.systemException(9, "pgm_num", request.getParameter("pgm_num"));      
      }
      if (request.getParameter("task_num") == null) {
        throw systemException.systemException(3, "task_num");
      }
      int tmpTaskNum = 0;
      try {
        tmpTaskNum = Integer.parseInt(request.getParameter("task_num"));
      } catch (NumberFormatException e) {
        throw systemException.systemException(9, "task_num", request.getParameter("task_num"));
      }
      if (tmpTaskNum <= 0) {
        throw systemException.systemException(9, "task_num", request.getParameter("task_num"));      
      }
      if (request.getParameter("page_num") == null) {
        throw systemException.systemException(3, "page_num");
      }
      int tmpPageNum = 0;
      try {
        tmpPageNum = Integer.parseInt(request.getParameter("page_num"));
      } catch (NumberFormatException e) {
        throw systemException.systemException(9, "page_num", request.getParameter("page_num"));
      }
      if (tmpPageNum <= 0) {
        throw systemException.systemException(9, "page_num", request.getParameter("page_num"));      
      }
    }
  }
}