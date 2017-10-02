/*
 * Title : GetProdChkListDaoImpl
 *
 * @Version : 1.0
 *
 * @Date : 2017-08-15
 *
 * @Copyright by 이민석
 */
package com.skd.ppa.main.dao.impl;

import java.util.List;
import java.util.Map;
import java.sql.SQLException;

import org.apache.ibatis.session.SqlSession;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.skd.ppa.main.dao.GetProdChkListDao;

/**
 *  이 객체는 상품 요건서분석에 필요한 체크리스트를 관리하는
 *  SQL 집합이다.
 */
@Repository("getProdChkListDao")
public class GetProdChkListDaoImpl implements GetProdChkListDao {
  @Autowired
  private SqlSession sqlSession;

  private static Logger logger = LogManager.getLogger(GetProdChkListDaoImpl.class);
  /**
   *  이 메서드는 상품 요건서분석 시스템의 체크리스트 전체를 불러오는 역할을 수행한다.
   *  @return 상품 전체 체크리스트를 List 형태로 되돌려준다.
   *  @throws DB 조회 시에 오류를 리턴해준다.
   */
  public List<Map<String, Object>> getAllProdChkList() throws SQLException {
    logger.debug("============   Start method of GetProdChkListDaoImpl.getAllProdChkList   ============");
    return sqlSession.selectList("com.skd.ppa.main.getAllProdChkList");
  }
}