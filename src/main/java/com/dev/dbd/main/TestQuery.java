/*
 * Title : TestQuery
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
import java.util.ArrayList;
import java.util.Iterator;
import java.text.SimpleDateFormat;
import java.sql.Timestamp;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;

import org.apache.ibatis.session.SqlSession;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.jdbc.BadSqlGrammarException;

import com.cmn.err.SystemException;
import com.cmn.err.UserException;
import com.dev.dbd.module.DevDbdModule;

/**
 *  이 객체는 등록된 쿼리를 개발기에 배포 한다.
 */
@Controller
public class TestQuery {
  @Autowired
  private SqlSession sqlSession;
  
  @Autowired
  private SystemException systemException;
  
  @Autowired
  private UserException userException;
  
  @Autowired
  private DevDbdModule devDbdModule;

  private static Logger logger = LogManager.getLogger(TestQuery.class);

  /**
    *  개발기에 쿼리를 배포하는 메서드
    *  @param request - 서블릿 Request    
    *  @param response - 서블릿 응답이 정의된 response
    *  @return - 데이터 없는 Map
    */
  @RequestMapping(value="/dev/dbd/main/TestQuery", method=RequestMethod.POST)
  @ResponseBody
  public Map<String, Object> main(HttpServletRequest request, HttpServletResponse response) throws Exception {
    validation(request, response);
    Map<String, Object> inputMap = new HashMap<String, Object>();
    JSONArray jsonArray = JSONArray.fromObject(request.getParameter("query_param"));
    inputMap.put("query", request.getParameter("query"));
    for (int i = 0; i < jsonArray.size(); i++) {
      inputMap.put(jsonArray.getJSONObject(i).getString("parameter_name"), jsonArray.getJSONObject(i).getString("parameter_value"));
    }
    String sqlType = devDbdModule.checkSqlType(request.getParameter("query"));
    Map<String, Object> returnMap = new HashMap<String, Object>();
    if (sqlType.equals("select") == true) {
      List<Map<String, Object>> outputTempList;
      logger.debug("Input Map of SQL getDevDbdSelectTest - " + inputMap);
      try {
        outputTempList = sqlSession.selectList("getDevDbdSelectTest", inputMap);
      } catch (BadSqlGrammarException e) {
        throw userException.userException(13, "쿼리", e.getMessage());
      }
      logger.debug("Output List of SQL getDevDbdSelectTest - " + outputTempList);
      List<String> columnList = new ArrayList<String>();
      if (outputTempList.size() != 0) {
        Iterator<String> key = outputTempList.get(0).keySet().iterator();
        while (key.hasNext() == true) {
          columnList.add(key.next());          
        }
      } else {
        inputMap.remove("query");
        String query;
        query = "SELEcT b.temp AS ___temp___, a.* FROM (SELECT 1 AS temp) AS b LEFT OUTER JOIN ("
          + request.getParameter("query") + ") AS a ON 1=1";
        inputMap.put("query", query);
        logger.debug("Input Map of SQL getDevDbdSelectTest - " + inputMap);
        Map<String, Object> outputMap = sqlSession.selectOne("getDevDbdSelectTest", inputMap);
        logger.debug("Output Map of SQL getDevDbdSelectTest - " + outputMap);
        Iterator<String> key = outputMap.keySet().iterator();
        while (key.hasNext() == true) {
          String tempKey = key.next();
          if (tempKey.equals("___temp___") == false) {
            columnList.add(tempKey);  
          }
        }
      }
      Map<String, Object> subMap = new HashMap<String, Object>();
      subMap.put("column", columnList);
      List<Map<String, Object>> outputList = new ArrayList<Map<String, Object>>();
      for (int i = 0; i < outputTempList.size(); i++) {
        for (int j = 0; j < columnList.size(); j++) {
          if (outputTempList.get(i).get(columnList.get(j)) instanceof Timestamp) {
            String datetime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(outputTempList.get(i).get(columnList.get(j)));
            outputTempList.get(i).remove(columnList.get(j));
            outputTempList.get(i).put(columnList.get(j), datetime);
          }
        }
        outputList.add(outputTempList.get(i));
      }
      subMap.put("data", outputList);
      returnMap.put("output", subMap);
    } else if (sqlType.equals("insert") == true) {
      int cnt = 0;
      logger.debug("Input Map of SQL insertDevDbdInsertTest - " + inputMap);
      try {
        cnt = sqlSession.insert("insertDevDbdInsertTest", inputMap);
      } catch (BadSqlGrammarException e) {
        throw userException.userException(13, "쿼리", e.getMessage());
      }
      logger.debug("Insert count of SQL insertDevDbdInsertTest - " + cnt);
      returnMap.put("cnt", cnt);
    } else if (sqlType.equals("update") == true) {
      int cnt = 0;
      logger.debug("Input Map of SQL updateDevDbdUpdateTest - " + inputMap);
      try {
        cnt = sqlSession.update("updateDevDbdUpdateTest", inputMap);
      } catch (BadSqlGrammarException e) {
        throw userException.userException(13, "쿼리", e.getMessage());
      }
      logger.debug("Update count of SQL updateDevDbdUpdateTest - " + cnt);
      returnMap.put("cnt", cnt);
    } else if (sqlType.equals("delete") == true) {
      int cnt = 0;
      logger.debug("Input Map of SQL deleteDevDbdDeleteTest - " + inputMap);
      try {
        cnt = sqlSession.delete("deleteDevDbdDeleteTest", inputMap);
      } catch (BadSqlGrammarException e) {
        throw userException.userException(13, "쿼리", e.getMessage());
      }
      logger.debug("Delete count of SQL deleteDevDbdDeleteTest - " + cnt);
      returnMap.put("cnt", cnt);
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
    if (request.getParameter("query") == null) {
      throw systemException.systemException(3, "query");
    }
    if (request.getParameter("query_param") == null) {
      throw systemException.systemException(3, "query_param");
    }
    try {
      JSONArray.fromObject(request.getParameter("query_param"));
    } catch (JSONException e) {
      throw systemException.systemException(9, "query_param", request.getParameter("query_param"));
    }
  }
}