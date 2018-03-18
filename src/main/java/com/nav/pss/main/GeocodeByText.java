package com.nav.pss.main;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import org.apache.ibatis.session.SqlSession;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.cmn.err.SystemException;
import com.cmn.cmn.service.GetDataFromURLService;

@Controller
public class GeocodeByText {
  @Autowired
  private SqlSession sqlSession;
  
  @Autowired
  private GetDataFromURLService getDataFromURLService;
  
  private static Logger logger = LogManager.getLogger(GeocodeByText.class);
	/**
	 * @param args
	 */
  @RequestMapping(value="/nav/pss/main/GeocodeByText", method=RequestMethod.POST)
  @ResponseBody
  public Map<String, Object> main(HttpServletRequest request, HttpServletResponse response) throws Exception {
    logger.debug("Start method of GeocodeByTest.main[/nav/pss/main/GeocodeByText");
    validation(request, response);
    Map<String, Object> inputMaps = new HashMap<String, Object>();
    Map<String, Object> returnMaps = new HashMap<String, Object>();
    List<Map<String, Object>> outputList = null;
    String strUrl = null;
    List<Map<String, String>> inputList = new ArrayList<Map<String, String>>();
    Map<String, String> tempMaps = null;
    JSONObject outputJSONObject = null;
    if (request.getParameter("query") != null && "".equals(request.getParameter("query")) == false) {
    	strUrl = "https://maps.googleapis.com/maps/api/place/textsearch/json";
      tempMaps = new HashMap<String, String>();
      tempMaps.put("key", "query");
      tempMaps.put("value", request.getParameter("query"));
      inputList.add(tempMaps);
      tempMaps = new HashMap<String, String>();
      tempMaps.put("key", "key");
      tempMaps.put("value", "AIzaSyAm5E0-Bm7zs650z153RCvDm4zvO0sXkU0");
      inputList.add(tempMaps);
    	outputJSONObject = (JSONObject)getDataFromURLService.getDataFromURL(strUrl, inputList, "GET", "UTF-8", GetDataFromURLService.TYPE_JSON);
      if (outputJSONObject == null) {
        throw new SystemException(16, "결과 Null pointer exception");
      }
      if (outputJSONObject.containsKey("error_message") == true && outputJSONObject.getString("error_message") != null) {
        throw new SystemException(16, outputJSONObject.getString("error_message"));
      }
      if (outputJSONObject.getJSONArray("results").size() != 1) {
        returnMaps.put("obj_search_result", false);
      } else {
        returnMaps.put("obj_search_result", true);
        returnMaps.put("formatted_address", outputJSONObject.getJSONArray("results").getJSONObject(0).getString("formatted_address"));
    	  returnMaps.put("lat", outputJSONObject.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getJSONObject("location")
                    .getDouble("lat"));
    	  returnMaps.put("lng", outputJSONObject.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getJSONObject("location")
                      .getDouble("lng"));
      }
    } else {
      inputMaps.put("key", request.getParameter("key"));
      outputList = sqlSession.selectList("getNavPssTimeBasePosByTimeAndUser", inputMaps);
      if (outputList.size() == 0) {
        outputList = sqlSession.selectList("getNavPssBasePosByTimeAndUser", inputMaps);
      }
      if (outputList.size() != 0) {
        returnMaps.put("obj_search_result", true);
        returnMaps.put("formatted_address", outputList.get(0).get("title"));
        returnMaps.put("lat", outputList.get(0).get("lat"));
        returnMaps.put("lng", outputList.get(0).get("lng"));
      } else {
        returnMaps.put("obj_search_result", false);
      }
    }
    return returnMaps;
  }
  
  protected void validation(HttpServletRequest request, HttpServletResponse response) throws Exception {
    return;
  }  
}
