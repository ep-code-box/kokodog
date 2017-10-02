/*
 * Title : InsertQuery
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
import com.cmn.err.UserException;

/**
 *  DB Query를 입력한다.
 */
@Controller
public class InsertQuery {
  @Autowired
  private SqlSession sqlSession;
  
  @Autowired
  private SystemException systemException;
  
  @Autowired
  private UserException userException;

  private static Logger logger = LogManager.getLogger(InsertQuery.class);

  /**
    *  DB Query를 입력하는 main method
    *  @param request - 서블릿 Request    
    *  @param response - 서블릿 응답이 정의된 response
    *  @return - 비어있는 Map
    */
  @RequestMapping(value="/dev/dbd/main/InsertQuery", method=RequestMethod.POST)
  @ResponseBody
  public Map<String, Object> main(HttpServletRequest request, HttpServletResponse response) throws Exception {
    validation(request, response);
    Map<String, Object> inputMap = new HashMap<String, Object>();
    Map<String, Object> outputMap = null;
    boolean isQueryUpdate = false;
    if (request.getParameter("query_num") != null) {
      inputMap.put("query_num", Integer.parseInt(request.getParameter("query_num")));
      inputMap.put("query", request.getParameter("query"));
      logger.debug("Input map of SQL getDevDbdIsUpdateQuery - " + inputMap);
      outputMap = sqlSession.selectOne("getDevDbdIsUpdateQuery", inputMap);
      logger.debug("Output map of SQL getDevDbdIsUpdateQuery - " + outputMap);
      if (outputMap.get("is_update") != null && outputMap.get("is_update").equals("Y") == true) {
        isQueryUpdate = true;
      }
      inputMap.clear();
    }
    Map<String, Object> returnMap = new HashMap<String, Object>();
    if (isQueryUpdate == true) {
      inputMap.put("query_num", Integer.parseInt(request.getParameter("query_num")));
      logger.debug("Input map of SQL getDevDbdMaxRepVerQueryByQueryNum - " + inputMap);
      outputMap = sqlSession.selectOne("getDevDbdMaxRepVerQueryByQueryNum", inputMap);
      logger.debug("Output map of SQL getDevDbdMaxRepVerQueryByQueryNum - " + outputMap);
      int repVer = ((Long)outputMap.get("rep_ver")).intValue();
      inputMap.clear();
      inputMap.put("query_num", Integer.parseInt(request.getParameter("query_num")));
      inputMap.put("rep_ver", repVer);
      if (request.getParameter("query_name") != null) {
        inputMap.put("query_name", request.getParameter("query_name").trim());
      } else {
        inputMap.put("query_name", null);
      }
      inputMap.put("query", request.getParameter("query").trim());
      inputMap.put("user_num", request.getSession().getAttribute("user_num"));
      logger.debug("Input map of SQL updateDevDbdQuery - " + inputMap);
      sqlSession.update("updateDevDbdQuery", inputMap);
      returnMap.put("is_update", true);
    } else {
      if (request.getParameter("query_num") == null) {
        inputMap.put("query_num", null);
      } else {
        inputMap.put("query_num", Integer.parseInt(request.getParameter("query_num")));
      }
      if (request.getParameter("query_name") != null) {
        inputMap.put("query_name", request.getParameter("query_name").trim());
      } else {
        inputMap.put("query_name", null);
      }
      inputMap.put("query", request.getParameter("query").trim());
      inputMap.put("user_num", request.getSession().getAttribute("user_num"));
      logger.debug("Input map of SQL insertDevDbdQuery - " + inputMap);
      sqlSession.insert("insertDevDbdQuery", inputMap);
      if (request.getParameter("query_num") == null) {
        outputMap = sqlSession.selectOne("getDevDbdMaxQueryNum");
        logger.debug("Output map of SQL getDevDbdMaxQueryNum - " + outputMap);
        returnMap.put("query_num", outputMap.get("query_num"));
      }
      returnMap.put("is_update", false);
    }
    return returnMap;
  }
  
  /**
    *  Input parameter 등의 유효성 체크
    *  @param request - 서블릿 Request    
    *  @param response - 서블릿 응답이 정의된 response
    *  @return - 없음  
    */
  private void validation(HttpServletRequest request, HttpServletResponse response) throws Exception {
    int tempQueryNum = 0;
    Map<String, Object> inputMap = new HashMap<String, Object>();
    Map<String, Object> outputMap = null;
    if (request.getParameter("query_num") != null) {
      try {
        tempQueryNum = Integer.parseInt(request.getParameter("query_num"));
      } catch (NumberFormatException e) {
        throw systemException.systemException(9, "query_num", request.getParameter("query_num"));
      }
      if (tempQueryNum < 1) {
        throw systemException.systemException(9, "query_num", request.getParameter("query_num"));      
      }
    } else {
      if (request.getParameter("query_name") == null) {
        throw systemException.systemException(3, "query_name");      
      } else {
        checkQueryNameValidation(request.getParameter("query_name").trim());
      }
    }
    if (request.getParameter("query") == null) {
      throw systemException.systemException(3, "query");
    }
    if (request.getParameter("query").trim().equals("") == true) {
      throw systemException.systemException(9, "query", request.getParameter("query"));      
    }
    if (request.getParameter("query_num") != null) {
      inputMap.put("query_num", Integer.parseInt(request.getParameter("query_num")));
      inputMap.put("user_num", request.getSession().getAttribute("user_num"));
      logger.debug("Input map of SQL getDevDbdInsertQueryAuth - " + inputMap);
      outputMap = sqlSession.selectOne("getDevDbdInsertQueryAuth", inputMap);
      logger.debug("Output map of SQL getDevDbdInsertQueryAuth - " + outputMap);
      if (outputMap.get("is_auth") == null || outputMap.get("is_auth").equals("N") == true) {
        throw userException.userException(2);
      }
    }
    inputMap.clear();
    inputMap.put("query_name", request.getParameter("query_name"));
    logger.debug("Input map of SQL getDevDbdCheckQueryNameExist - " + inputMap);
    outputMap = sqlSession.selectOne("getDevDbdCheckQueryNameExist", inputMap);
    logger.debug("Output map of SQL getDevDbdCheckQueryNameExist - " + outputMap);
    if (outputMap == null || outputMap.get("query_name_exist") == null || outputMap.get("query_name_exist").equals("Y") == true) {
      throw userException.userException(10);
    }
  }
  
    /**
    *  Input parameter 등의 유효성 체크
    *  @param queryName - 쿼리명
    *  @return - 없음  
    */
  private void checkQueryNameValidation(String queryName) throws Exception {
    if ((queryName.charAt(0) < 'a' || queryName.charAt(0) > 'z') && (queryName.charAt(0) < 'A' || queryName.charAt(0) > 'Z')) {
      throw systemException.systemException(3, "query_name");       
    }
    for (int i = 1; i < queryName.length(); i++) {
      if ((queryName.charAt(i) < '0' || queryName.charAt(i) > '9') && (queryName.charAt(i) < 'a' || queryName.charAt(i) > 'z') && (queryName.charAt(i) < 'A' || queryName.charAt(i) > 'Z') && queryName.charAt(i) != '-') {
        throw systemException.systemException(3, "query_name"); 
      }
    }
  }
}