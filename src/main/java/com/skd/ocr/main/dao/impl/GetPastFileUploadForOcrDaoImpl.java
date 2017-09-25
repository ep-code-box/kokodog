/*
 * Title : GetPastFileUploadForOcrDaoImpl
 *
 * @Version : 1.0
 *
 * @Date : 2017-08-15
 *
 * @Copyright by 이민석
 */
package com.skd.ppa.main.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.sql.SQLException;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.skd.ocr.main.dao.GetPastFileUploadForOcrDao;

/**
 *  이 객체는 문서 변환 프로세스를 통해 변환되어 저장된 HTML 정보를
 *  돌려주는 역할을 수행한다.
 */
@Repository("getPastFileUploadForOcrDao")
public class GetPastFileUploadForOcrDaoImpl implements GetPastFileUploadForOcrDao {
  @Autowired
  private SqlSession sqlSession;

  private static Logger logger = Logger.getLogger(GetPastFileUploadForOcrDaoImpl.class);
  /**
   *  이 메서드는 문서 변환 프로세스를 통해 변환되어 저장된 HTML 정보를
   *  돌려주는 역할을 수행한다.
   *  @param fileKey : HTML로 되돌려줄 파일의 키
   *  @return 결과 HTML
   *  @throws 기타 익셉션
   */
  public List<Map<String, Object>> getPastFileUpload() throws SQLException {
    logger.debug("============   Start method of GetPastFileUploadForOcrDaoImpl.getPastFileUpload   ============");
    return sqlSession.selectList("com.skd.ocr.main.getPastFileUpload");
  }

  /**
   *  기존에 등록하였던 파일을 재조회 할 때, 더 이상 필요없는 파일을 삭제하고자 함이다.
   *  @param fileKey 삭제를 진행하고자 하는 파일의 file_key
   *  @throws 기타 Exception
   */
  public void deletePastFileUpload(Map<String, Object> inputMap) throws SQLException {
    logger.debug("============   Start method of GetPastFileUploadForOcrDaoImpl.deletePastFileUpload   ============");
    logger.debug(" Parameter - inputMap[" + inputMap + "]");
    sqlSession.update("com.skd.ocr.main.deletePastFileUpload", inputMap);
  }
}