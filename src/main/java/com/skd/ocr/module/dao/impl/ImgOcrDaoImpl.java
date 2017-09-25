/*
 * Title : ImgOcrDaoImpl
 *
 * @Version : 1.0
 *
 * @Date : 2017-09-23
 *
 * @Copyright by 이민석
 */
package com.skd.ocr.module.dao.impl;

import java.util.Map;
import java.sql.SQLException;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.skd.ocr.module.dao.ImgOcrDao;
/**
 *  이 객체는 com.skd.ocr.dao.module.ImgOcrSerivce에 사용하는
 *  각종 Dao Method들의 묶음으로 정의한다.
 *  주로 Image의 OCR 사전 작업 관련한 데이터를 관리하는 역할을 수행한다.
 */
@Repository("imgOcrDao")
public class ImgOcrDaoImpl implements ImgOcrDao {
  /**
   *  이 메서드는 OCR을 위한 이미지를 업로드했을 시
   *  업로드한 이미지 관리를 위하여 대상 파일을
   *  DB에 저장하는 역할을 수행한다.
   *  @param inputMap - 해당 Map은 파일의 Key 값인 file_key 및 사용자 번호인 user_num을 필수로 포함하고 있다.
   *  @throws SQL 쿼리 수행 시 발생할 수 있는 오류를 정의한다.
   */
  @Autowired
  private SqlSession sqlSession;

  private static Logger logger = Logger.getLogger(ImgOcrDaoImpl.class);
  /**
   *  이 메서드는 OCR을 위한 이미지를 업로드했을 시
   *  업로드한 이미지 관리를 위하여 대상 파일을
   *  DB에 저장하는 역할을 수행한다.
   *  @param inputMap - 해당 Map은 파일의 Key 값인 file_key 및 사용자 번호인 user_num을 필수로 포함하고 있다.
   *  @throws SQL 쿼리 수행 시 발생할 수 있는 오류를 정의한다.
   */
  public void insertUploadImgInfo(Map<String, Object> inputMap) throws SQLException {
    logger.debug("============   Start method of ImgOcrDaoImpl.insertUploadImgInfo   ============");
    logger.debug(" Parameter - inputMap[" + inputMap + "]");
    sqlSession.insert("com.skd.ocr.main.insertUploadImgInfo", inputMap);    
  }
}