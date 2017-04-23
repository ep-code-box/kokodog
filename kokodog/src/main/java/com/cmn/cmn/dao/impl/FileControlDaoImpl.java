package com.cmn.cmn.dao.impl;

import java.util.Map;
import java.sql.SQLException;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import org.apache.ibatis.session.SqlSession;

import com.cmn.cmn.dao.FileControlDao;

@Repository("fileControlDao")
public class FileControlDaoImpl implements FileControlDao {
  @Autowired
  private SqlSession sqlSession;

  private static Logger logger = Logger.getLogger(FileControlDaoImpl.class);
  
  public Map<String, Object> getIsExistFileKeyByKey(Map<String, Object> inputMap) throws SQLException {
    logger.debug("============   Start method of FileControlDaoImpl.getIsExistFileKeyByKey   ============");
    logger.debug(" Parameter - inputMap[" + inputMap + "]");
    Map<String, Object> outputMap = null;
    outputMap = sqlSession.selectOne("com.cmn.cmn.getIsExistFileKeyByKey", inputMap);
    logger.debug(" return - outputMap[" + outputMap + "]");
    return outputMap;
  }

  public void insertFile(Map<String, Object> inputMap) throws SQLException {
    logger.debug("============   Start method of FileControlDaoImpl.insertFile   ============");
    logger.debug(" Parameter display skipped");
    sqlSession.insert("com.cmn.cmn.insertFile", inputMap);
  }

  public Map<String, Object> getLastFileNum() throws SQLException {
    logger.debug("============   Start method of FileControlDaoImpl.getLastFileNum   ============");
    Map<String, Object> outputMap = null;
    outputMap = sqlSession.selectOne("com.cmn.cmn.getLastFileNum");
    logger.debug(" return - outputMap[" + outputMap + "]");
    return outputMap;
  }
  
  public void insertFileContent(Map<String, Object> inputMap) throws SQLException {
    logger.debug("============   Start method of FileControlDaoImpl.insertFileContent   ============");
    logger.debug(" Parameter display skipped");
    sqlSession.insert("com.cmn.cmn.insertFileContent", inputMap);
  }
  
  public void deleteFileDeleteByFileNum(Map<String, Object> inputMap) throws SQLException {
    logger.debug("============   Start method of FileControlDaoImpl.insertFileContent   ============");    
    logger.debug(" Parameter - inputMap[" + inputMap + "]");
    sqlSession.delete("com.cmn.cmn.deleteFileDeleteByFileNum", inputMap);    
  }
}

 