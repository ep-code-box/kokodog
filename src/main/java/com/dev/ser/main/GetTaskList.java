/*
 * Title : GetPtmList
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
public class GetTaskList {
  @Autowired
  private SqlSession sqlSession;
  
  private static Logger logger = LogManager.getLogger(GetTaskList.class);

  /**
    *  서비스 개발을 위해 전체 업무 리스트를 가져오는 쿼리
    *  @param request - 서블릿 Request    
    *  @param response - 서블릿 응답이 정의된 response
    *  @return - 링크 정보를 포함한 모델   
    */
  @RequestMapping(value="/dev/ser/main/GetTaskList", method=RequestMethod.POST)
  @ResponseBody
  public List<Map<String, Object>> main(HttpServletRequest request, HttpServletResponse response) throws Exception {
    validation(request, response);
    Map<String, Object> inputMap = new HashMap<String, Object>();
    inputMap.put("user_num", request.getSession().getAttribute("user_num"));
    inputMap.put("pgm_num", Integer.parseInt(request.getParameter("pgm_num")));
    logger.debug("Output map of SQL getDevSerTaskListByPgmNum - " + inputMap);
    List<Map<String, Object>> outputList = sqlSession.selectList("getDevSerTaskListByPgmNum", inputMap);
    logger.debug("Output list of SQL getDevSerTaskListByPgmNum - " + outputList);
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
  }
}