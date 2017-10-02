/*
 * Title : GetQueryInfo
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
public class GetQueryInfo {
  @Autowired
  private SqlSession sqlSession;
  
  @Autowired
  private SystemException systemException;
  
  private static Logger logger = LogManager.getLogger(GetQueryInfo.class);

  /**
    *  Google OAuth 로그인을 위하여 링크 정보를 가져오는 method
    *  @param request - 서블릿 Request    
    *  @param response - 서블릿 응답이 정의된 response
    *  @return - 링크 정보를 포함한 모델   
    */
  @RequestMapping(value="/dev/dbd/main/GetQueryInfo", method=RequestMethod.POST)
  @ResponseBody
  public Map<String, Object> main(HttpServletRequest request, HttpServletResponse response) throws Exception {
    validation(request, response);
    Map<String, Object> inputMap = new HashMap<String, Object>();
    inputMap.put("query_num", Integer.parseInt(request.getParameter("query_num")));
    if (request.getParameter("rep_ver") != null) {
      inputMap.put("rep_ver", Integer.parseInt(request.getParameter("rep_ver")));
    } else {
      inputMap.put("rep_ver", null);
    }
    inputMap.put("user_num", request.getSession().getAttribute("user_num"));
    logger.debug("Input map of SQL getDevDbdQuery - " + inputMap);
    Map<String, Object> outputMap = sqlSession.selectOne("getDevDbdQuery", inputMap);
    logger.debug("Output map of SQL getDevDbdQuery - " + outputMap);
    Map<String, Object> returnMap = new HashMap<String, Object>();
    returnMap.put("query", outputMap.get("query"));
    returnMap.put("max_rep_ver", outputMap.get("max_rep_ver"));
    returnMap.put("rep_ver", outputMap.get("rep_ver"));
    returnMap.put("is_auth", outputMap.get("is_auth"));
    returnMap.put("is_exist_no_dist", outputMap.get("is_exist_no_dist"));
    inputMap.clear();
    inputMap.put("query_num", Integer.parseInt(request.getParameter("query_num")));
    logger.debug("Input map of SQL getDevDbdQueryRepInfo - " + inputMap);
    List<Map<String, Object>> outputList = sqlSession.selectList("getDevDbdQueryRepInfo", inputMap);
    logger.debug("Output list of SQL getDevDbdQueryRepInfo - " + outputList);
    returnMap.put("query_rep_info", outputList);
    return returnMap;
  }
  
  /**
    *  Input parameter 등의 유효성 체크
    *  @param request - 서블릿 Request    
    *  @param response - 서블릿 응답이 정의된 response
    *  @return - 없음  
    */
  private void validation(HttpServletRequest request, HttpServletResponse response) throws Exception {
    if (request.getParameter("query_num") == null) {
      throw systemException.systemException(3, "query_num");
    }
    int tempQueryNum = 0;
    try {
      tempQueryNum = Integer.parseInt(request.getParameter("query_num"));
    } catch (NumberFormatException e) {
      throw systemException.systemException(9, "query_num", request.getParameter("query_num"));
    }
    if (tempQueryNum < 1) {
      throw systemException.systemException(9, "query_num", request.getParameter("query_num"));      
    }
    if (request.getParameter("rep_ver") != null) {
      int tempRepVer = 0;
      try {
        tempRepVer = Integer.parseInt(request.getParameter("rep_ver"));
      } catch (NumberFormatException e) {
        throw systemException.systemException(9, "rep_ver", request.getParameter("rep_ver"));
      }
      if (tempRepVer < 1) {
        throw systemException.systemException(9, "rep_ver", request.getParameter("rep_ver"));      
      }      
    }
  }
}