/*
 * Title : GetImgList
 *
 * @Version : 1.0
 *
 * @Date : 2016-04-17
 *
 * @Copyright by 이민석
 */

package com.dev.des;

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
import com.cmn.err.UserException;

/**
 *  이 객체는 등록된 쿼리를 개발기에 배포 한다.
 */
@Controller
public class GetImgList {
  @Autowired
  private SqlSession sqlSession;
  
  private static Logger logger = LogManager.getLogger(GetImgList.class);
  private static final int ROW_NUM = 20;
  
  /**
    *  개발기에 쿼리를 배포하는 메서드
    *  @param request - 서블릿 Request    
    *  @param response - 서블릿 응답이 정의된 response
    *  @return - 데이터 없는 Map
    */
  @RequestMapping(value="/dev/des/image_manage/GetImgList", method=RequestMethod.POST)
  @ResponseBody
  public List<Map<String, Object>> main(HttpServletRequest request, HttpServletResponse response) throws Exception {
    validation(request, response);
    Map<String, Object> inputMap = new HashMap<String, Object>();
    if (request.getParameter("search_txt") == null) {
      inputMap.put("search_txt", "");
    } else {
      inputMap.put("search_txt", request.getParameter("search_txt"));
    }
    if (request.getParameter("from") == null) {
      inputMap.put("from", 0);
    } else {
      inputMap.put("from", (Integer.parseInt(request.getParameter("from")) - 1) * ROW_NUM);
    }
    inputMap.put("row_num", ROW_NUM);
    logger.debug("Input map of SQL getDevDesImgList - " + inputMap);
    List<Map<String, Object>> outputList = sqlSession.selectList("getDevDesImgList", inputMap);
    return outputList;
  }
  
  /**
    *  Input parameter 등의 유효성 체크
    *  @param request - 서블릿 Request    
    *  @param response - 서블릿 응답이 정의된 response
    *  @return - 없음  
    */
  private void validation(HttpServletRequest request, HttpServletResponse response) throws Exception {
    int tempPage;
    if (request.getParameter("page") != null) {
      try {
        tempPage = Integer.parseInt(request.getParameter("page"));
      } catch (NumberFormatException e) {
        throw new SystemException(9, "page", request.getParameter("page"));
      }
      if (tempPage <= 0) {
        throw new SystemException(9, "page", request.getParameter("page"));
      }
    }
  }
}