package com.dev.ser.main;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.lang.StackTraceElement;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import net.sf.json.JSONArray;
import net.sf.json.JSONSerializer;
import net.sf.json.JSONException;

import com.cmn.err.SystemException;
import com.cmn.err.UserException;
import com.dev.ser.module.TestProcessThread;


@Controller
public class GetTestLog {
	/** Mybatis에서 관리하는 SQL 접근 객체 */
	@Autowired
	private SqlSession sqlSession;
  
	@Autowired
  private SystemException systemException;

  @Autowired
  private UserException userException;

	/** Application Log를 관리해주는 객체 */
	private static Logger logger = Logger.getLogger(GetTestLog.class);
	
  @RequestMapping(value = "/dev/ser/main/GetTestLog", method=RequestMethod.POST)
  @ResponseBody
  public Map main(HttpServletRequest request, HttpServletResponse response) throws Exception {
    validation(request,response);
    Map<String, Object> inputMap = new HashMap<String, Object>();
		Map<String, Object> outputMap = null;
		Map<String, Object> returnMap = new HashMap<String, Object>();
    List<Map<String, Object>> outputList = null;
		inputMap.put("test_key", request.getParameter("test_key"));
		if (request.getParameter("min_log_seq") == null) {
			inputMap.put("min_log_seq", null);
		} else {
			inputMap.put("min_log_seq", Integer.parseInt(request.getParameter("min_log_seq")));
		}
		outputList = sqlSession.selectList("getDevSerTestLog", inputMap);
		List<Map<String, Object>> outputConsoleResultList = new ArrayList<Map<String, Object>>();
		String resultOutput = "";
		for (int i = 0; i < outputList.size(); i++) {
			if (((Integer)outputList.get(i).get("log_typ")).intValue() == 3 && ((String)(outputList.get(i).get("log_msg"))).length() >= 14 && ((String)(outputList.get(i).get("log_msg"))).substring(0, 14).equals("__DATARESULT__") == true) {
				resultOutput = ((String)(outputList.get(i).get("log_msg"))).substring(14);
			} else {
				outputConsoleResultList.add(outputList.get(i));
			}
		}
		returnMap.put("log_list", outputConsoleResultList);
		returnMap.put("output_data", resultOutput);
		inputMap.clear();
		inputMap.put("test_key", request.getParameter("test_key"));
		outputMap = sqlSession.selectOne("getDevSerTestState", inputMap);
		if (outputMap.get("test_success_yn") == null || outputMap.get("test_success_yn").equals("") == true) {
			returnMap.put("test_success_yn", "");
		} else {
			returnMap.put("test_success_yn", new String((String)outputMap.get("test_success_yn")));
		}
		returnMap.put("test_process_nm", new String((String)outputMap.get("test_process_nm")));
		if (outputMap.get("test_err_nm") != null) {
			returnMap.put("test_err_nm", new String((String)outputMap.get("test_err_nm")));
		} else {
			returnMap.put("test_err_nm", "");
		}
		if (request.getParameter("min_log_seq") == null) {
			returnMap.put("min_log_seq", outputList.size());
		} else {
			returnMap.put("min_log_seq", Integer.parseInt(request.getParameter("min_log_seq")) + outputList.size());
		}
    return returnMap;
  }

  private void validation(HttpServletRequest request, HttpServletResponse response) throws Exception {
    if (request.getParameter("test_key") == null) {
      throw systemException.systemException(3, "test_key");
    }
		int minLogSeq = 0;
		if (request.getParameter("min_log_seq") != null) {
			try {
				minLogSeq = Integer.parseInt(request.getParameter("min_log_seq"));
			} catch (NumberFormatException e) {
				throw systemException.systemException(9, "min_log_seq", request.getParameter("min_log_seq"));
			}
			if (minLogSeq < 0) {
				throw systemException.systemException(9, "min_log_seq", request.getParameter("min_log_seq"));				
			}
		}
  }
}