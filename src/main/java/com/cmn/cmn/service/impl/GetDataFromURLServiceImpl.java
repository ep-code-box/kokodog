package com.cmn.cmn.service.impl;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Map;
import java.util.List;

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.apache.log4j.Logger;

import org.springframework.stereotype.Service;

import com.cmn.cmn.service.GetDataFromURLService;

@Service("getDataFromURLService")
public class GetDataFromURLServiceImpl implements GetDataFromURLService {
  private static Logger logger = Logger.getLogger(GetDataFromURLServiceImpl.class);
  
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
    for (int i = 0; i < listData.size(); i++) {
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
}