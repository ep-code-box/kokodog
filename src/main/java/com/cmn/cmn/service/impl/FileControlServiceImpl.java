package com.cmn.cmn.service.impl;

import java.io.File;
import java.io.InputStream;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.cmn.cmn.service.FileControlService;
import com.cmn.cmn.service.GetRandomStringArrayService;
import com.cmn.cmn.dao.FileControlDao;

@Service("fileControlService")
public class FileControlServiceImpl implements FileControlService {
  @Autowired
  private FileControlDao fileControlDao;
  
  @Autowired
  private GetRandomStringArrayService getRandomStringArrayService;
  
  private static int FILE_DIV_SIZE = 1024 * 1024 * 7;

  private static Logger logger = LogManager.getLogger(FileControlServiceImpl.class);
  
  private static long FILE_MAX_SIZE = 2 * 1024 * 1024 * 1024;
  private static int FILE_THREADHOLD_SIZE = 1 * 1024 * 1024;
  private static String UPLOAD_DIR = "/home/leems83/data/webappdev/ROOT/WEB-INF/files";

  public Map<String, Object> insertFile(HttpServletRequest request) throws Exception {
    Map<String, Object> returnMap = new HashMap<String, Object>();
    File tempDirectory = new File(UPLOAD_DIR);
    DiskFileItemFactory factory = new DiskFileItemFactory();
    factory.setSizeThreshold(FILE_THREADHOLD_SIZE);
    factory.setRepository(tempDirectory);
    ServletFileUpload upload = new ServletFileUpload(factory);
    upload.setSizeMax(FILE_MAX_SIZE);
    upload.setHeaderEncoding("UTF-8");
    String fileName = null;
    Map<String, Object> outputMap = null;
    List<Map<String, Object>> outputList = new ArrayList<Map<String, Object>>();
    List<FileItem> items = null;
    Iterator<FileItem> iter = null;
    items = upload.parseRequest(request);
    iter = items.iterator();
    try {
      while (iter.hasNext() == true) {
        FileItem item = (FileItem)iter.next();
        if (item.isFormField() == false) {
          logger.debug("IsFormField is false");
          logger.debug("=================================");
          logger.debug("Content-type : " + item.getContentType());
          logger.debug("Field Name : " + item.getFieldName());
          logger.debug("Name : " + item.getName());
          logger.debug("Size : " + item.getSize());
          logger.debug("=================================");
          fileName = item.getName();
          outputMap = insertFile(item.getInputStream(), fileName
                                 , request.getSession().getAttribute("user_num") == null ? 0 : ((Integer)request.getSession().getAttribute("user_num")).intValue()
                                 , (Long)request.getAttribute("system_call_dtm"));
          outputList.add(outputMap);
        } else {
          logger.debug("IsFormField is true");
          logger.debug("=================================");
          logger.debug("Content-type : " + item.getContentType());
          logger.debug("Field Name : " + item.getFieldName());
          logger.debug("Name : " + item.getName());
          logger.debug("Size : " + item.getSize());
          logger.debug("String : " + item.getString());
          logger.debug("=================================");
          returnMap.put(item.getFieldName(), item.getString());
        }
      }
    } catch (Exception e) {
      for (int i = 0; i < outputList.size(); i++) {
        deleteFile(((Integer)outputList.get(i).get("file_num")).intValue());
      }
      throw e;
    }
    returnMap.put("__img_info", outputList);
    return returnMap;    
  }
  
  public Map<String, Object> insertFile(InputStream is, String fileName, int userNum, Long systemCallDtm) throws Exception {
    Map<String, Object> inputMap = new HashMap<String, Object>();
    byte[] buffer = new byte[FILE_DIV_SIZE];
    int fileNum = 0;
    int readSize = 0;
    Map<String, Object> outputMap = null;
    String key = null;
    do {
      key = getRandomStringArrayService.getRandomStringArray(40);
      inputMap.put("key", key);
      outputMap = fileControlDao.getIsExistFileKeyByKey(inputMap);
    } while (outputMap.get("is_file_key_exist").equals("Y") == true);
    inputMap.clear();
    inputMap.put("file_key", key);
    inputMap.put("file_nm", fileName);
    inputMap.put("now_dtm", new Date(systemCallDtm));
    inputMap.put("user_num", userNum);
    fileControlDao.insertFile(inputMap);
    fileNum = ((Long)(fileControlDao.getLastFileNum().get("file_num"))).intValue();
    Map<String, Object> returnMap = new HashMap<String, Object>();
    try {
      while ((readSize = is.read(buffer)) != -1) {
        inputMap.clear();
        inputMap.put("file_num", fileNum);
        inputMap.put("user_num", userNum);
        if (readSize != FILE_DIV_SIZE) {
          byte[] tempBuffer = new byte[readSize];
          System.arraycopy(buffer, 0, tempBuffer, 0, readSize);
          inputMap.put("content", tempBuffer);
        } else {
          inputMap.put("content", buffer);
        }
        fileControlDao.insertFileContent(inputMap);
      }
      returnMap.put("file_num", fileNum);
      returnMap.put("file_key", key);
    } catch (Exception e) {
      deleteFile(fileNum);
      throw e;
    }
    return returnMap;    
  }
  
  public void deleteFile(int fileNum) throws Exception {
    Map<String, Object> inputMap = new HashMap<String, Object>();
    inputMap.put("file_num", fileNum);
    fileControlDao.deleteFileDeleteByFileNum(inputMap);    
  }

  public Map<String, Object> getFileInfo(String fileKey) throws Exception {
    Map<String, Object> inputMap = new HashMap<String, Object>();
    inputMap.put("file_key", fileKey);
    return fileControlDao.getFileInfo(inputMap);
  }
  
  public byte[] getFileContent(int fileNum, int seq) throws Exception {
    Map<String, Object> inputMap = new HashMap<String, Object>();
    Map<String, Object> outputMap = null;
    inputMap.put("file_num", fileNum);
    inputMap.put("seq", seq);
    outputMap = fileControlDao.getFileContent(inputMap);
    return (byte[])outputMap.get("content");
  }

  public byte[] getFileContent(String fileKey) throws Exception {
    int fileNum = 0;
    int contentCnt = 0;
    Map<String, Object> outputMap = null;
    outputMap = getFileInfo(fileKey);
    fileNum = ((Long)outputMap.get("file_num")).intValue();
    contentCnt = ((Long)outputMap.get("content_cnt")).intValue();
    byte[] fileContent = getFileContent(fileNum, 1);
    byte[] fileContentNext = null;
    for (int i = 1; i < contentCnt; i++) {
      fileContentNext = getFileContent(fileNum, i + 1);
      byte[] fileContentTmp = new byte[fileContent.length + fileContentNext.length];
      System.arraycopy(fileContent, 0, fileContentTmp, 0, fileContent.length);
      System.arraycopy(fileContentNext, 0, fileContentTmp, fileContent.length, fileContentNext.length);
      fileContent = fileContentTmp;
    }
    return fileContent;
  }
}