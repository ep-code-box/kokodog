/*
 * Title : ImgOcrServiceImpl
 *
 * @Version : 1.0
 *
 * @Date : 2017-09-23
 *
 * @Copyright by 이민석
 */
package com.skd.ocr.module.service.impl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONSerializer;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import com.skd.ocr.module.service.ImgOcrService;
import com.skd.ocr.module.dao.ImgOcrDao;
import com.cmn.err.SystemException;

/**
 *  이 객체는 Python 내 OCR 관련 정보를 얻는
 *  프로그램을 실행하기 위해 수행한다.
 *  상세 프로그램을 수정하기 위해선 이 class 내
 *  method 들이 호출하고 있는 python 프로그램
 *  수정이 필요한다.
 */
@Service("imgOcrService")
public class ImgOcrServiceImpl implements ImgOcrService {
  @Autowired
  private SystemException systemException;
  
  @Autowired
  private ImgOcrDao imgOcrDao;
  
  private String imgOcrPythonStr = "ocr.py";
  
  private static Logger logger = LogManager.getLogger(ImgOcrServiceImpl.class);

  /**
   *  이 메서드는 String 형태로 전달받은 byte stream 이미지를
   *  python 모듈을 통해 ocr 분석 후 분석된 ocr 정보를 되돌려주는 역할을 수행한다.
   *  @param img - 이미지 파일 내용. 반드시 byte 스트림 형태의 파일을 전달해야 한다.
   *  @return JSONObject 타입의 OCR 분석 결과
   *  @throws 기타 예외
   */
  public JSONArray getImgOcrInfoByImgWithStr(byte[] img) throws Exception {
    logger.debug("Start method of ImgOcrServiceImpl.getImgOcrInfoByImgWithStr");
    List<String> inputList = new ArrayList<String>();
    ServletRequestAttributes sra = null;
    String realPath = null;
    FileOutputStream fos = null;
    File file = null;
    try {
      sra = (ServletRequestAttributes)RequestContextHolder.currentRequestAttributes();
      realPath = sra.getRequest().getSession().getServletContext().getRealPath("/WEB-INF/classes/python");
      if ((new File(realPath)).isDirectory() == false) {
        realPath =  this.getClass().getResource("/").toURI().getPath();
        if (realPath.length() != 0 && realPath.charAt(realPath.length() - 1) != '/') {
          realPath = realPath + "/";
        }
        realPath = realPath + "../classes/python";
      }
    } catch (IllegalStateException e) {
      realPath = this.getClass().getResource("/").toURI().getPath();
      if (realPath.length() != 0 && realPath.charAt(realPath.length() - 1) != '/') {
        realPath = realPath + "/";
      }
      realPath = realPath + "../classes/python";
    }
    if (realPath.length() != 0 && realPath.charAt(realPath.length() - 1) != '/') {
      realPath = realPath + "/";
    }
    try {
      fos = new FileOutputStream(realPath + "/tmp");
      fos.write(img);
      inputList.add(realPath + "/tmp");
      return returnPythonExe(inputList, imgOcrPythonStr);
    } catch (Exception e) {
      throw e;
    } finally {
      if (fos != null) {
        fos.close();
      }
      file = new File(realPath + "/tmp");
      file.delete();
    }
  }
  
  /**
   *  이 메서드는 OCR Image 처리 전에 관리 대상 Image를 DB에 삽입하는 역할을 수행한다.
   *  @param fileKey - 이미지가 저장된 File Key
   *  @param userNum - 이미지를 관리할 사용자 번호
   *  @throws 기타 예외
   */
  public void insertUploadImgInfo(String fileKey, int userNum) throws Exception {
    Map<String, Object> inputMap = new HashMap<String, Object>();
    HttpServletRequest request = null;
    try {
      request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
      if (request == null || request.getSession() == null || request.getSession().getAttribute("user_num") == null) {
        inputMap.put("now_dtm", new Date());
      } else {
        inputMap.put("now_dtm", new Date(((Long)request.getSession().getAttribute("system_call_dtm")).longValue()));
      }
    } catch (Exception e) {
      inputMap.put("now_dtm", new Date());
    }
    inputMap.put("file_key", fileKey);
    inputMap.put("user_num", userNum);
    imgOcrDao.insertUploadImgInfo(inputMap);
  }
  
  private JSONArray returnPythonExe(List<String> argv, String exe) throws Exception {
    ServletRequestAttributes sra = null;
    String realPath = null;
    try {
      sra = (ServletRequestAttributes)RequestContextHolder.currentRequestAttributes();
      realPath = sra.getRequest().getSession().getServletContext().getRealPath("/WEB-INF/classes/python");
      if ((new File(realPath)).isDirectory() == false) {
        realPath =  this.getClass().getResource("/").toURI().getPath();
        if (realPath.length() != 0 && realPath.charAt(realPath.length() - 1) != '/') {
          realPath = realPath + "/";
        }
        realPath = realPath + "../classes/python";
      }
    } catch (IllegalStateException e) {
      realPath = this.getClass().getResource("/").toURI().getPath();
      if (realPath.length() != 0 && realPath.charAt(realPath.length() - 1) != '/') {
        realPath = realPath + "/";
      }
      realPath = realPath + "../classes/python";
    }
    if (realPath.length() != 0 && realPath.charAt(realPath.length() - 1) != '/') {
      realPath = realPath + "/";
    }
    List<String> list = new ArrayList<String>();
    list.add("python3");
    list.add(realPath + exe);
    list.addAll(argv);
    Process oProcess = new ProcessBuilder(list).start();
    BufferedReader stdOut   = new BufferedReader(new InputStreamReader(oProcess.getInputStream()));
    BufferedReader stdError = new BufferedReader(new InputStreamReader(oProcess.getErrorStream()));
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
      throw systemException.systemException(19, errorStr);
    }
    return (JSONArray)JSONSerializer.toJSON(stdOutStr);
  }
}