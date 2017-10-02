/*
 * Title : GetDBIOList
 *
 * @Version : 1.0
 *
 * @Date : 2016-03-08
 *
 * @Copyright by 이민석
 */

package com.dev.dbd.main;

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
public class GetDBIOList {
  @Autowired
  private SqlSession sqlSession;
  
  @Autowired
  private SystemException systemException;
  
  private static int ROW_NUMBER_PER_PAGE = 20;
  
  private static Logger logger = LogManager.getLogger(GetDBIOList.class);

  /**
    *  Google OAuth 로그인을 위하여 링크 정보를 가져오는 method
    *  @param request - 서블릿 Request    
    *  @param response - 서블릿 응답이 정의된 response
    *  @return - 링크 정보를 포함한 모델   
    */
  @RequestMapping(value="/dev/dbd/main/GetDBIOList", method=RequestMethod.POST)
  @ResponseBody
  public List<Map<String, Object>> main(HttpServletRequest request, HttpServletResponse response) throws Exception {
    validation(request, response);
    Map<String, Object> inputMap = new HashMap<String, Object>();
    if (request.getParameter("search_txt") == null) {
      inputMap.put("search_txt", "");
    } else {
      inputMap.put("search_txt", request.getParameter("search_txt"));
    }
    inputMap.put("row_num", ROW_NUMBER_PER_PAGE);
    if (request.getParameter("page") == null) {
      inputMap.put("from", 0);
    } else {
      inputMap.put("from", (Integer.parseInt(request.getParameter("page")) - 1) * ROW_NUMBER_PER_PAGE);
    }
    logger.debug("Input map of SQL getDevDbdDBIOListBySearchTxt - " + inputMap);
    List<Map<String, Object>> outputList = sqlSession.selectList("getDevDbdDBIOListBySearchTxt", inputMap);
    logger.debug("Output list of SQL getDevDbdDBIOListBySearchTxt - " + outputList);
    return outputList;
  }
  
  /**
    *  Input parameter 등의 유효성 체크
    *  @param request - 서블릿 Request    
    *  @param response - 서블릿 응답이 정의된 response
    *  @return - 없음  
    */
  private void validation(HttpServletRequest request, HttpServletResponse response) throws Exception {
    int temp = 0;
    if (request.getParameter("page") != null) {
      try {
        temp = Integer.parseInt(request.getParameter("page"));
      } catch (NumberFormatException e) {
        throw systemException.systemException(9, "page", request.getParameter("page"));
      }
      if (temp < 1) {
        throw systemException.systemException(9, "page", request.getParameter("page"));
      }
    }
  }
}