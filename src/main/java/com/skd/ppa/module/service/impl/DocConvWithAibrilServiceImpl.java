/*
 * Title : DocConvWithAibrilServiceImpl
 *
 * @Version : 1.0
 *
 * @Date : 2017-08-10
 *
 * @Copyright by 이민석
 */
package com.skd.ppa.module.service.impl;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmn.cmn.service.GetDataFromURLService;
import com.skd.ppa.module.service.DocConvWithAibrilService;
import com.cmn.err.SystemException;
import com.cmn.err.UserException;

/**
 *  이 객체는 Aibril 에서 제공하는 Document Converting 기능을 수행한다.
 *  MS Word, Presentation, Excel의 문서를 HTML형식으로 변환하는 기능을
 *  갖고 있다.
 */
@Service("docConvWithAibrilService")
public class DocConvWithAibrilServiceImpl implements DocConvWithAibrilService {
  private static Logger logger = Logger.getLogger(DocConvWithAibrilServiceImpl.class);
  private static final String AIBRIL_DOC_CONF_URL = "https://gateway.aibril-watson.kr/document-conversion/api/v1/convert_document?version=2015-12-15";
  private static final String AIBRIL_USER_NAME = "42abed68-c1d7-49a3-9882-2f1e5fd04518";
  private static final String AIBRIL_PASSWORD = "f3pZUAKvkDFW";
  private static final String BOUNDARY = "dkjsei40f9844djs8dviwdf";
  
  @Autowired
  private SystemException systemException;

  @Autowired
  private UserException userException;

  @Autowired
  private GetDataFromURLService getDataFromURLService;

  /**
   *  이 메쏘드는 MS word, Presentation, Excel의 문서를
   *  HTML 형식으로 리턴해주는 기능을 갖는다.
   *  @param fis - FileInputStream 형식의 파일 스트림
   *  @return HTML 형식의 문자열
   *  @throws 기타 모든 예외
   */
  public String convToHtml(InputStream is) throws Exception {
    OutputStreamWriter wsr = null;
    BufferedReader br = null;
    InputStreamReader isr = null;
    URL url = null;
    URLConnection conn = null;
    DataOutputStream dos = null;
    String outputStr = null;
    String line = null;
    try {
      url = new URL(AIBRIL_DOC_CONF_URL);
      conn = url.openConnection();
      conn.setDoInput(true);
      conn.setDoOutput(true);
      String basicAuth = "Basic " + new String(Base64.getEncoder().encode(new String(AIBRIL_USER_NAME + ":" + AIBRIL_PASSWORD).getBytes()));
      conn.setRequestProperty ("Authorization", basicAuth);
      conn.setRequestProperty("Connection", "Keep-Alive");
      conn.setRequestProperty("Content-Type","multipart/form-data;boundary=" + BOUNDARY);
      dos = new DataOutputStream(conn.getOutputStream());
      dos.writeBytes("--" + BOUNDARY + "\r\n");
      dos.writeBytes("Content-Disposition:form-data;name=\"config\"\r\n");
      dos.writeBytes("\r\n");
      dos.writeBytes("{\"conversion_target\":\"normalized_html\"}");
      dos.writeBytes("\r\n");
      dos.writeBytes("--" + BOUNDARY + "\r\n");
      dos.writeBytes("Content-Disposition:form-data;name=\"file\";filename=\"tempFile.docx\"\r\n");
      dos.writeBytes("\r\n");
      int bytesAvailable = bytesAvailable = is.available();
      int maxBufferSize = 1024;
      int bufferSize = Math.min(bytesAvailable, maxBufferSize); 
      byte[] buffer = new byte[bufferSize];
      int bytesRead = is.read(buffer, 0, bufferSize);
      while (bytesRead > 0) {
        dos.write(buffer, 0, bufferSize);
        bytesAvailable = is.available();
        bufferSize = Math.min(bytesAvailable, maxBufferSize);
        bytesRead = is.read(buffer, 0, bufferSize);
      }
      is.close();
      dos.writeBytes("\r\n");
      dos.writeBytes("--" + BOUNDARY + "--" + "\r\n");
      dos.flush();
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
    try {
      is = conn.getInputStream();
    } catch (IOException e) {
      if (conn instanceof HttpURLConnection == false) {
        throw e;
      } else {
        isr = new InputStreamReader(((HttpURLConnection)conn).getErrorStream(), "UTF-8");
        br = new BufferedReader(isr);
        outputStr = "";
        while ((line = br.readLine()) != null) {
          outputStr = outputStr + line;
        }
        JSONObject jsonObject = (JSONObject)JSONSerializer.toJSON(outputStr);;
        if (jsonObject.getInt("code") == 415) {
          throw userException.userException(20, jsonObject.getString("error"));
        } else {
          throw systemException.systemException(18, "" + jsonObject.getInt("code"), jsonObject.getString("error"));
        }
      }
    }
    try {
      isr = new InputStreamReader(is, "UTF-8");
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
    return outputStr;
  }

  /**
   *  이 메쏘드는 MS word, Presentation, Excel의 문서를
   *  HTML 형식으로 리턴해주는 기능을 갖는다.
   *  @param fileName - 전달할 파일이 위치하는 파일 명
   *  @return HTML 형식의 문자열
   *  @throws 기타 모든 예외
   */
  public String convToHtml(String fileName) throws Exception {
    Process oProcess = new ProcessBuilder("curl", "-X", "POST", "-u", AIBRIL_USER_NAME + ":" + AIBRIL_PASSWORD, "-F", "config=\"{\\\"conversion_target\\\":\\\"normalized_html\\\"}\""
                                         , "file=@" + fileName, AIBRIL_DOC_CONF_URL).start();

    // 외부 프로그램 출력 읽기
    BufferedReader stdOut   = new BufferedReader(new InputStreamReader(oProcess.getInputStream()));
    BufferedReader stdError = new BufferedReader(new InputStreamReader(oProcess.getErrorStream()));

    // "표준 출력"과 "표준 에러 출력"을 출력
    String errorStr = "";
    String stdOutStr = "";
    String tmpStr = null;
    while ((tmpStr = stdOut.readLine()) != null) {
      stdOutStr = stdOutStr + tmpStr;
    }
    while ((tmpStr = stdError.readLine()) != null) {
      errorStr = errorStr + tmpStr;
    }
    if ("".equals(errorStr) == false) {
      throw new Exception(errorStr);
    }
    return stdOutStr;
  }
}