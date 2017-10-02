/*
 * Title : GetConvHtmlDaoImpl
 *
 * @Version : 1.0
 *
 * @Date : 2017-08-15
 *
 * @Copyright by 이민석
 */
package com.skd.ppa.main.dao.impl;

import java.util.Map;
import java.sql.SQLException;

import org.apache.ibatis.session.SqlSession;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.skd.ppa.main.dao.GetConvHtmlDao;

/**
 *  이 객체는 문서 변환 프로세스를 통해 변환되어 저장된 HTML 정보를
 *  돌려주는 역할을 수행한다.
 */
@Repository("getConvHtmlDao")
public class GetConvHtmlDaoImpl implements GetConvHtmlDao {
  @Autowired
  private SqlSession sqlSession;

  private static Logger logger = LogManager.getLogger(GetConvHtmlDaoImpl.class);
  /**
   *  이 메서드는 문서 변환 프로세스를 통해 변환되어 저장된 HTML 정보를
   *  돌려주는 역할을 수행한다.
   *  @param fileKey : HTML로 되돌려줄 파일의 키
   *  @return 결과 HTML
   *  @throws 기타 익셉션
   */
  public Map<String, Object> getHtml(Map<String, Object> inputMap) throws SQLException {
    logger.debug("============   Start method of GetConvHtmlDaoImpl.getHtml   ============");
    logger.debug(" Parameter - inputMap[" + inputMap + "]");
    return sqlSession.selectOne("com.skd.ppa.main.getHtml", inputMap);
  }
}