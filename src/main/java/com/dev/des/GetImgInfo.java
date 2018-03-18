/*
 * Title : GetImgInfo
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
public class GetImgInfo {
  @Autowired
  private SqlSession sqlSession;
  
  private static Logger logger = LogManager.getLogger(GetImgInfo.class);
  
  /**
    *  개발기에 쿼리를 배포하는 메서드
    *  @param request - 서블릿 Request    
    *  @param response - 서블릿 응답이 정의된 response
    *  @return - 데이터 없는 Map
    */
  @RequestMapping(value="/dev/des/image_manage/GetImgInfo", method=RequestMethod.POST)
  @ResponseBody
  public Map<String, Object> main(HttpServletRequest request, HttpServletResponse response) throws Exception {
    validation(request, response);
    Map<String, Object> inputMap = new HashMap<String, Object>();
    inputMap.put("img_num", Integer.parseInt(request.getParameter("img_num")));
    logger.debug("Input map of SQL getDevDesImgInfo - " + inputMap);
    Map<String, Object> outputMap = sqlSession.selectOne("getDevDesImgInfo", inputMap);
    logger.debug("Output map of SQL getDevDesImgInfo - " + outputMap);
    return outputMap;
  }
  
  /**
    *  Input parameter 등의 유효성 체크
    *  @param request - 서블릿 Request    
    *  @param response - 서블릿 응답이 정의된 response
    *  @return - 없음  
    */
  private void validation(HttpServletRequest request, HttpServletResponse response) throws Exception {
    int tempImgNum;
    if (request.getParameter("img_num") == null) {
      throw new SystemException(3, "img_num", "img_num");
    }
    try {
      tempImgNum = Integer.parseInt(request.getParameter("img_num"));
    } catch (NumberFormatException e) {
      throw new SystemException(9, "img_num", request.getParameter("img_num"));
    }
    if (tempImgNum <= 0) {
      throw new SystemException(9, "img_num", request.getParameter("img_num"));
    }
  }
}