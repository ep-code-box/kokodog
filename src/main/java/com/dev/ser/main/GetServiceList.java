/*
 * Title : GetServiceList
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

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.cmn.err.SystemException;

/**
 *  이 객체는 검색어에 따라 모든 DBIO 리스트를 가져오는 역할을 한다.
 */
@Controller
public class GetServiceList {
  @Autowired
  private SqlSession sqlSession;
  
  private static Logger logger = LogManager.getLogger(GetServiceList.class);

  /**
    *  서비스 개발을 위해 전체 페이지 리스트를 가져오는 쿼리
    *  @param request - 서블릿 Request    
    *  @param response - 서블릿 응답이 정의된 response
    *  @return - 링크 정보를 포함한 모델   
    */
  @RequestMapping(value="/dev/ser/main/GetServiceList", method=RequestMethod.POST)
  @ResponseBody
  public List<Map<String, Object>> main(HttpServletRequest request, HttpServletResponse response) throws Exception {
    validation(request, response);
    Map<String, Object> inputMap = new HashMap<String, Object>();
    inputMap.put("pgm_num", Integer.parseInt(request.getParameter("pgm_num")));
    inputMap.put("task_num", Integer.parseInt(request.getParameter("task_num")));
    inputMap.put("page_num", Integer.parseInt(request.getParameter("page_num")));
    if (request.getParameter("search_text") == null) {
      inputMap.put("search_text", "");
    } else {
      inputMap.put("search_text", request.getParameter("search_text"));
    }
    logger.debug("Output map of SQL getDevSerServiceListByPageNum - " + inputMap);
    List<Map<String, Object>> outputList = sqlSession.selectList("getDevSerServiceListByPageNum", inputMap);
    logger.debug("Output list of SQL getDevSerServiceListByPageNum - " + outputList);
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
  }
}