package com.dev.ser.main;

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

import net.sf.json.JSONSerializer;
import net.sf.json.JSONException;

import com.cmn.cmn.service.GetRandomStringArrayService;
import com.cmn.err.SystemException;
import com.cmn.err.UserException;
import com.dev.ser.module.TestProcessThread;

@Controller
public class TestService {
  /** Mybatis에서 관리하는 SQL 접근 객체 */
  @Autowired
  private SqlSession sqlSession;
  
  @Autowired
  private GetRandomStringArrayService getRandomStringArrayService;

  /** Application Log를 관리해주는 객체 */
  private static Logger logger = LogManager.getLogger(TestService.class);
  
  @RequestMapping(value="/dev/ser/main/TestService", method=RequestMethod.POST)
  @ResponseBody
  public Map<String, Object> main(HttpServletRequest request, HttpServletResponse response) throws Exception {
    validation(request,response);
    Map<String, Object> inputMap = new HashMap<String, Object>();
    Map<String, Object> outputMap = null;
    Map<String, Object> returnMap = new HashMap<String, Object>();
    String randomStr = null;
    boolean isExistTestKey = false;
    do {
      randomStr = getRandomStringArrayService.getRandomStringArray(40);
      inputMap.clear();
      inputMap.put("service_key", randomStr);
      logger.debug("Input map of SQL getDevSerIsExistServiceTestKey - " + inputMap);
      outputMap = sqlSession.selectOne("getDevSerIsExistServiceTestKey", inputMap);
      logger.debug("Output map of SQL getDevSerIsExistServiceTestKey - " + outputMap);
      if (outputMap == null || outputMap.get("is_exist_test_key") == null || outputMap.get("is_exist_test_key").equals("Y") == false) {
        isExistTestKey = false;
      } else {
        isExistTestKey = true;
      }
    } while (isExistTestKey == true);
    inputMap.clear();
    inputMap.put("test_key", randomStr);
    inputMap.put("user_num", request.getSession().getAttribute("user_num"));
    inputMap.put("service_num", Integer.parseInt(request.getParameter("service_num")));
    if (request.getParameter("rep_ver") == null) {
      inputMap.put("rep_ver", null);
    } else {
      inputMap.put("rep_ver", Integer.parseInt(request.getParameter("rep_ver")));
    }
    inputMap.put("now_dtm", request.getAttribute("now_dtm"));
    logger.debug("Input map of SQL insertDevSerTestHst - " + inputMap);
    int cnt = sqlSession.insert("insertDevSerTestHst", inputMap);
    logger.debug("Insert count of SQL insertDevSerTestHst - " + cnt);
    TestProcessThread tpt = new TestProcessThread(sqlSession, ((Integer)request.getSession().getAttribute("user_num")).intValue(),
                                                  randomStr, request.getParameter("param"));
    tpt.start();
    returnMap.put("test_key", randomStr);
    return returnMap;
  }

  private void validation(HttpServletRequest request, HttpServletResponse response) throws Exception {
    if (request.getParameter("service_num") == null) {
      throw new SystemException(3, "service_num");
    }
    int serviceNum = 0;
    try {
      serviceNum = Integer.parseInt(request.getParameter("service_num"));
    } catch (NumberFormatException e) {
      throw new SystemException(9, "service_num", request.getParameter("service_num"));
    }
    if (serviceNum <= 0) {
      throw new SystemException(9, "service_num", request.getParameter("service_num"));      
    }
    int repVer = 0;
    if (request.getParameter("rep_ver") != null) {
      try {
        repVer = Integer.parseInt(request.getParameter("rep_ver"));
      } catch (NumberFormatException e) {
        throw new SystemException(9, "rep_ver", request.getParameter("rep_ver"));
      }
      if (repVer <= 0) {
        throw new SystemException(9, "rep_ver", request.getParameter("rep_ver"));      
      }      
    }
    if (request.getParameter("parameter") != null) {
      try {
        JSONSerializer.toJSON(request.getParameter("parameter"));
      } catch (JSONException e) {
        throw new SystemException(9, "parameter", request.getParameter("parameter"));  
      }
    }
  }
}