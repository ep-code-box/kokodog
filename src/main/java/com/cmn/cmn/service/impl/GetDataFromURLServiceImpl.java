/*
 * Title : GetDataFromURLService
 *
 * @Version : 1.0
 *
 * @Date : 2017-08-15
 *
 * @Copyright by 이민석
 */
package com.cmn.cmn.service.impl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Base64;
import java.util.Map;
import java.util.List;

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import org.springframework.stereotype.Service;

import com.cmn.cmn.service.GetDataFromURLService;

/**
 *  이 객체는 입력받은 URL을 통해 http 혹은 https 프로토콜을 통하여 전달받은
 *  데이터를 되돌려 주는 역할을 수행한다.<br/>
 *  되돌려주는 데이터는 response를 통해 받은 String 그대로를 전달할 수 있고
 *  혹은 json 형태의 경우에는 json 형태로 되돌려주는 경우도 있다.
 */
@Service("getDataFromURLService")
public class GetDataFromURLServiceImpl implements GetDataFromURLService {
  private static Logger logger = LogManager.getLogger(GetDataFromURLServiceImpl.class);
  
  /**
   *  이 메서드는 URL을 호출한 결과를 되돌려받기 위한 함수이다.
   *  오류 발생시에는 exeption과 함께 결과를 되돌려줄 것이다.
   *  파라미터가 key=value 형태일 때 해당 데이터를 listData에 담아 전달한다.
   *  @param strURL - 호출 URL
   *  @param listData - key=value 형태의 list 형태의 데이터 셋
   *  @param method - 호출방식(POST, GET, PUT 등)
   *  @param encodingType - 인코딩 방식(UTF-8, EUC-KR 등)
   *  @param returnType - 리턴 형식(TYPE_JSON, TYPE_STRING, TYPE_BYTE)
   *  @return String 혹은 JSONObject 형태의 결과
   *  @throws 기타 오류
   */
  public Object getDataFromURL(String strUrl, List<Map<String, String>> listData, String method, String encodingType, int returnType) throws Exception {
    logger.debug("============   Start method of GetDataFromURLServiceImpl.getDataFromURL   ============");
    logger.debug(" Parameter - strUrl[" + strUrl + "], listData[" + listData + "], method[" + method + "], encodingType[" + encodingType + "], returnType[" + returnType + "]");
    URL url = null;
    String strData = "";
    OutputStreamWriter wsr = null;
    BufferedReader br = null;
    String outputStr = null;
    String line = null;
    URLConnection conn = null;
    InputStreamReader isr = null;
    int i = 0;
    for (i = 0; i < listData.size(); i++) {
      strData = strData + URLEncoder.encode(listData.get(i).get("key"), encodingType) + "=" + URLEncoder.encode(listData.get(i).get("value"), encodingType);
      if (i != listData.size() - 1) {
        strData = strData + "&";
      }
    }
    if (method.equals("POST") == true) {
      url = new URL(strUrl);
    } else if (strData.equals("") == false) {
      url = new URL(strUrl + "?" + strData);
    } else {
      url = new URL(strUrl);
    }
    conn = url.openConnection();
    try {
      if (method.equals("POST") == true) {
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        wsr = new OutputStreamWriter(conn.getOutputStream());
        wsr.write(strData);
        wsr.flush();
      } else {
        conn.setDoOutput(false);
        conn.setRequestProperty("Content-Type", "text/html;");
      }
      isr = new InputStreamReader(conn.getInputStream(), encodingType);
      br = new BufferedReader(isr);
      outputStr = "";
      while ((line = br.readLine()) != null) {
        outputStr = outputStr + line;
      }
    } catch (Exception e) {
      throw e;
    } finally {
      if (wsr != null) {
        wsr.close();
      }
      if (br != null) {
        br.close();
      }
      if (isr != null) {
        isr.close();
      }
    }
    if (returnType == TYPE_STRING) {
      return outputStr;
    } else if (returnType == TYPE_JSON) {
      return (JSONObject)JSONSerializer.toJSON(outputStr);
    } else {
      return outputStr.getBytes(encodingType);
    }
  }
  
  /**
   *  이 메서드는 URL을 호출한 결과를 되돌려받기 위한 함수이다.
   *  오류 발생시에는 exeption과 함께 결과를 되돌려줄 것이다.
   *  파라미터가 일반 스트링 형태로 전달받아야 할 때 사용한다.
   *  @param strURL - 호출 URL
   *  @param data - String 형태의 데이터 리스트
   *  @param method - 호출방식(POST, GET, PUT 등)
   *  @param encodingType - 인코딩 방식(UTF-8, EUC-KR 등)
   *  @param returnType - 리턴 형식(TYPE_JSON, TYPE_STRING, TYPE_BYTE)
   *  @return String 혹은 JSONObject 형태의 결과
   *  @throws 기타 오류
   */
  public Object getDataFromURL(String strUrl, String data, String method, String encodingType, int returnType, String user, String password) throws Exception {
    logger.debug("============   Start method of GetDataFromURLServiceImpl.getDataFromURL   ============");
    logger.debug(" Parameter - strUrl[" + strUrl + "], data[" + data + "], method[" + method + "], encodingType[" + encodingType + "], returnType[" + returnType + "]");
    URL url = null;
    OutputStreamWriter wsr = null;
    BufferedReader br = null;
    String outputStr = null;
    String line = null;
    String basicAuth = null;
    URLConnection conn = null;
    InputStreamReader isr = null;
    if (method.equals("POST") == true) {
      url = new URL(strUrl);
    } else if (data.equals("") == false) {
      url = new URL(strUrl + "?" + data);
    } else {
      url = new URL(strUrl);
    }
    conn = url.openConnection();
    try {
      if (user != null && password != null) {
        basicAuth = "Basic " + new String(Base64.getEncoder().encode(new String(user + ":" + password).getBytes()));
        conn.setRequestProperty("Authorization", basicAuth);
      }
      if (method.equals("POST") == true) {
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type", "application/json");
        wsr = new OutputStreamWriter(conn.getOutputStream());
        wsr.write(data);
        wsr.flush();
      } else {
        conn.setDoOutput(false);
        conn.setRequestProperty("Content-Type", "application/json");
      }
      isr = new InputStreamReader(conn.getInputStream(), encodingType);
      br = new BufferedReader(isr);
      outputStr = "";
      while ((line = br.readLine()) != null) {
        outputStr = outputStr + line;
      }
    } catch (Exception e) {
      throw e;
    } finally {
      if (wsr != null) {
        wsr.close();
      }
      if (br != null) {
        br.close();
      }
      if (isr != null) {
        isr.close();
      }
    }
    if (returnType == TYPE_STRING) {
      return outputStr;
    } else if (returnType == TYPE_JSON) {
      return (JSONObject)JSONSerializer.toJSON(outputStr);
    } else {
      return outputStr.getBytes(encodingType);
    }
    
  }
}