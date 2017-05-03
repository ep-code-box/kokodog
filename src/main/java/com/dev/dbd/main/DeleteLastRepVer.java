/*
 * Title : DeleteLastRepVer
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
import org.apache.log4j.Logger;
import com.cmn.err.SystemException;
import com.cmn.err.UserException;
import com.dev.dbd.module.DevDbdModule;

/**
 *  이 객체는 등록된 쿼리를 개발기에 배포 한다.
 */
@Controller
public class DeleteLastRepVer {
  @Autowired
  private SqlSession sqlSession;
  
  @Autowired
  private SystemException systemException;
  
  @Autowired
  private UserException userException;
  
  @Autowired
  private DevDbdModule devDbdModule;

  private static Logger logger = Logger.getLogger(DeleteLastRepVer.class);

  /**
    *  개발기에 쿼리를 배포하는 메서드
    *  @param request - 서블릿 Request    
    *  @param response - 서블릿 응답이 정의된 response
    *  @return - 데이터 없는 Map
    */
  @RequestMapping(value="/dev/dbd/main/DeleteLastRepVer", method=RequestMethod.POST)
  @ResponseBody
  public Map main(HttpServletRequest request, HttpServletResponse response) throws Exception {
    validation(request, response);
    Map<String, Object> inputMap = new HashMap<String, Object>();
    inputMap.put("query_num", Integer.parseInt(request.getParameter("query_num")));
    logger.debug("Input Map of SQL deleteLastRepVerOfQuery - " + inputMap);
    sqlSession.delete("deleteLastRepVerOfQuery");
    return new HashMap<String, Object>();
  }
  
  /**
    *  Input parameter 등의 유효성 체크
    *  @param request - 서블릿 Request    
    *  @param response - 서블릿 응답이 정의된 response
    *  @return - 없음  
    */
  private void validation(HttpServletRequest request, HttpServletResponse response) throws Exception {
    int tempQueryNum = 0;
    if (request.getParameter("query_num") == null) {
      throw systemException.systemException(3, "query_num");
    }
    try {
      tempQueryNum = Integer.parseInt(request.getParameter("query_num"));
    } catch (NumberFormatException e) {
      throw systemException.systemException(9, "query_num", request.getParameter("query_num"));
    }
    if (tempQueryNum <= 0) {
      throw systemException.systemException(9, "query_num", request.getParameter("query_num"));
    }
  }
}