/*
 * Title : GetServiceInfo
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
 *  선택된 서비스의 소스 및 정보를 가져온다.
 */
@Controller
public class GetServiceInfo {
  @Autowired
  private SqlSession sqlSession;
  
  @Autowired
  private SystemException systemException;
  
  private static Logger logger = Logger.getLogger(GetServiceInfo.class);

  /**
    *  선택된 서비스의 소스 및 정보를 가져오기 위해 호출 시 main 처이
    *  @param request - 서블릿 Request    
    *  @param response - 서블릿 응답이 정의된 response
    *  @return - 링크 정보를 포함한 모델   
    */
  @RequestMapping(value="/dev/ser/main/GetServiceInfo", method=RequestMethod.POST)
  @ResponseBody
  public Map main(HttpServletRequest request, HttpServletResponse response) throws Exception {
    validation(request, response);
    Map<String, Object> inputMap = new HashMap<String, Object>();
    inputMap.put("service_num", Integer.parseInt(request.getParameter("service_num")));
    if (request.getParameter("rep_ver") == null) {
      inputMap.put("rep_ver", null);
    } else {
      inputMap.put("rep_ver", Integer.parseInt(request.getParameter("rep_ver")));
    }
    logger.debug("Input map of SQL getDevSerSourceInfoByServiceNum - " + inputMap);
    Map<String, Object> outputMap = sqlSession.selectOne("getDevSerSourceInfoByServiceNum", inputMap);
    logger.debug("Output map of SQL getDevSerSourceInfoByServiceNum - " + outputMap);
    Map<String, Object> returnMap = new HashMap<String, Object>();
    returnMap.put("source", outputMap.get("source"));
    returnMap.put("max_rep_ver", outputMap.get("max_rep_ver"));
    returnMap.put("rep_ver", outputMap.get("rep_ver"));
    returnMap.put("is_auth", outputMap.get("is_auth"));
    returnMap.put("is_exist_no_dist", outputMap.get("is_exist_no_dist"));
    inputMap.clear();
    inputMap.put("service_num", Integer.parseInt(request.getParameter("service_num")));
    logger.debug("Input map of SQL getDevSerServiceRepInfo - " + inputMap);
    List<Map<String, Object>> outputList = sqlSession.selectList("getDevSerServiceRepInfo", inputMap);
    logger.debug("Output list of SQL getDevSerServiceRepInfo - " + outputList);
    returnMap.put("service_rep_info", outputList);
    return returnMap;
  }
  
  /**
    *  Input parameter 등의 유효성 체크
    *  @param request - 서블릿 Request    
    *  @param response - 서블릿 응답이 정의된 response
    *  @return - 없음
    */
  private void validation(HttpServletRequest request, HttpServletResponse response) throws Exception {
    if (request.getParameter("service_num") == null) {
      throw systemException.systemException(3, "service_num");
    }
    int tmpSourceNum = 0;
    try {
      tmpSourceNum = Integer.parseInt(request.getParameter("service_num"));
    } catch (NumberFormatException e) {
      throw systemException.systemException(9, "service_num", request.getParameter("service_num"));
    }
    if (tmpSourceNum <= 0) {
      throw systemException.systemException(9, "service_num", request.getParameter("service_num"));      
    }
    int tmpRepNum = 0;
    if (request.getParameter("rep_ver") != null) {
      try {
        tmpRepNum = Integer.parseInt(request.getParameter("rep_ver"));
      } catch (NumberFormatException e) {
        throw systemException.systemException(9, "rep_ver", request.getParameter("rep_ver"));
      }
      if (tmpRepNum <= 0) {
        throw systemException.systemException(9, "rep_ver", request.getParameter("rep_ver"));      
      }
    }
  }
}