/*
 * Title : GetProdChkListDao
 *
 * @Version : 1.0
 *
 * @Date : 2017-09-13
 *
 * @Copyright by 이민석
 */
package com.skd.ppa.main.dao;

import java.util.List;
import java.util.Map;
import java.sql.SQLException;
/**
 *  이 객체는 상품 요건서분석에 필요한 체크리스트를 관리하는
 *  SQL 집합이다.
 */
public interface GetProdChkListDao {
  /**
   *  이 메서드는 상품 요건서분석 시스템의 체크리스트 전체를 불러오는 역할을 수행한다.
   *  @return 상품 전체 체크리스트를 List 형태로 되돌려준다.
   *  @throws DB 조회 시에 오류를 리턴해준다.
   */
  public List<Map<String, Object>> getAllProdChkList() throws SQLException;
}