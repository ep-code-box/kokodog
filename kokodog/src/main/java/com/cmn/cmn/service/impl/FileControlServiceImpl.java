package com.cmn.cmn.service.impl;

import java.io.InputStream;
import java.util.Map;
import java.util.HashMap;

import org.apache.log4j.Logger;

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

  private static Logger logger = Logger.getLogger(FileControlServiceImpl.class);
  
  public Map<String, Object> insertFile(InputStream is, String fileName, int userNum, String nowDtm) throws Exception {
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
    inputMap.put("now_dtm", nowDtm);
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
}