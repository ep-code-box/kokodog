/*
 * Title : GetPageList
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

/**
 *  이 객체는 검색어에 따라 모든 DBIO 리스트를 가져오는 역할을 한다.
 */
@Controller
public class GetPageList {
  @Autowired
  private SqlSession sqlSession;
  
  @Autowired
  private SystemException systemException;
  
  private static Logger logger = Logger.getLogger(GetPageList.class);

  /**
    *  서비스 개발을 위해 전체 페이지 리스트를 가져오는 쿼리
    *  @param request - 서블릿 Request    
    *  @param response - 서블릿 응답이 정의된 response
    *  @return - 링크 정보를 포함한 모델   
    */
  @RequestMapping(value="/dev/ser/main/GetPageList", method=RequestMethod.POST)
  @ResponseBody
  public List main(HttpServletRequest request, HttpServletResponse response) throws Exception {
    validation(request, response);
    Map<String, Object> inputMap = new HashMap<String, Object>();
    inputMap.put("user_num", request.getSession().getAttribute("user_num"));
    inputMap.put("pgm_num", Integer.parseInt(request.getParameter("pgm_num")));
    inputMap.put("task_num", Integer.parseInt(request.getParameter("task_num")));
    logger.debug("Output map of SQL getDevSerPageListByTaskNum - " + inputMap);
    List<Map<String, Object>> outputList = sqlSession.selectList("getDevSerPageListByTaskNum", inputMap);
    logger.debug("Output list of SQL getDevSerPageListByTaskNum - " + outputList);
    return outputList;
  }
  
  /**
    *  Input parameter 등의 유효성 체크
    *  @param request - 서블릿 Request    
    *  @param response - 서블릿 응답이 정의된 response
    *  @return - 없음
    */
  private void validation(HttpServletRequest request, HttpServletResponse response) throws Exception {
    if (request.getParameter("pgm_num") == null) {
      throw systemException.systemException(3, "pgm_num");
    }
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
  }
}