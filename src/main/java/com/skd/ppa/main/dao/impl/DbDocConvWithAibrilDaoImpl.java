/*
 * Title : DbDocConvWithAibrilDaoImpl
 *
 * @Version : 1.0
 *
 * @Date : 2017-08-10
 *
 * @Copyright by 이민석
 */
package com.skd.ppa.main.dao.impl;

import java.util.Map;
import java.sql.SQLException;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.skd.ppa.main.dao.DbDocConvWithAibrilDao;

/**
 *  이 객체는 DbDocConvWithAibrilService에서 사용하는 각종 쿼리를
 *  수행하는 DAO로 정의된다.
 */
@Repository("dbDocConvWithAibrilDao")
public class DbDocConvWithAibrilDaoImpl implements DbDocConvWithAibrilDao {
  @Autowired
  private SqlSession sqlSession;

  private static Logger logger = Logger.getLogger(DbDocConvWithAibrilDaoImpl.class);
  /**
   *  이 메서드는 문서를 html 형식으로 변환된 결과를 저장하는 역할을 수행한다.
   */
  public int insertConvDocHtml(Map<String, Object> inputMap) throws SQLException {
    logger.debug("============   Start method of DbDocConvWithAibrilDaoImpl.insertConvDocHtml   ============");
    logger.debug(" Parameter - inputMap[" + inputMap + "]");
    return sqlSession.insert("com.skd.ppa.main.insertConvDocHtml", inputMap);
  }
}