package com.skd.sel.sel_scnrio_mng.dao;

import java.util.List;
import java.util.Map;
import java.sql.SQLException;

/**
 * 이 클래스는 셀레니움 테스트 플랫폼 시스템에서 시나리오 리스트를 가져오기 위한 목적의 Dao 클래스이다.<br/>
 * 아래 스크립트를 통해 데이터 구조가 마련되어 있어야 해당 클래스의 오류가 발생하지 않는다.<br/>
 * CREATE TABLE skd_sel_test_scnrio (<br/>
 *    scnrio_num INT(10) UNSIGNED NOT NULL COMMENT '시나리오번호'<br/>
 *  , eff_end_dtm DATETIME NOT NULL COMMENT '유효죵료일시'<br/>
 *  , audit_num INT(10) UNSIGNED NOT NULL COMMENT '권한자번호'<br/>
 *  , audit_dtm DATETIME NOT NULL COMMENT '권한일시'<br/>
 *  , scnrio_nm VARCHAR(255) NOT NULL COMMENT '시나리오명'<br/>
 *  , op_typ_cd VARCHAR(3) NOT NULL COMMENT '처리유형코드'<br/>
 *  , scnrio_desc TEXT(65535) NULL COMMENT '시나리오상세'<br/>
 *  , seq_num INT(10) UNSIGNED NOT NULL COMMENT '순차번호'<br/>
 *  , src_cd TEXT(65535) NOT NULL COMMENT '소스코드'<br/>
 *  , eff_sta_dtm DATETIME NOT NULL COMMENT '유효시작일시'<br/>
 *  , PRIMARY KEY(scnrio_num, eff_end_dtm)<br/>
 * )  ENGINE=INNODB, COMMENT = '셀레니움 테스트 시나리오';<br/><br/>
 *
 * @author  Minseok Lee
 * @since   2018.02.03
 * @version 1.0
 */
public interface GetScnrioLstDao {
  /**
   * 입력받은 Text를 기준으로 전체 시나리오 리스트를 되돌려준다.
   * 만약 입력받은 Text가 NULL혹은 빈 값이면 전체 리스트를 되돌려준다.
   * Paging 기능이 적용되어 있다.
   *
   * @param inputMap Map형식으로 아래 내용이 포함된다.
   *        sch_txt : 검색어(선택)
   *        page_num : 페이지 번호(선택)
   * @return List 형식으로 아래 내용이 있는 Map이 포함된다.
   *        scnrio_num : 시나리오 번호
   *        scnrio_nm : 시나리오명
   *        scnrio_desc : 시나리오 상세
   * @exception SQLException SQL 수행 시 발생할 수 있는 예외
   */
  public List<Map<String, Object>> getScnrioLst(Map<String, Object> inputMap) throws SQLException;
}