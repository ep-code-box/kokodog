package com.skd.sel.sel_scnrio_mng.dao.impl;

import java.util.List;
import java.util.Map;
import java.sql.SQLException;

import org.apache.ibatis.session.SqlSession;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.skd.sel.sel_scnrio_mng.dao.InsertNewScnrioDao;

/**
 * 이 클래스는 셀레니움 테스트 플랫폼 시스템에서 시나리오 리스트를 등록하기 위한 목적의 Dao 클래스이다.<br/>
 * 아래 스크립트를 통해 데이터 구조가 마련되어 있어야 해당 클래스의 오류가 발생하지 않는다.<br/>
 * CREATE TABLE skd_sel_test_scnrio (<br/>
 *    scnrio_num INT(10) UNSIGNED NOT NULL COMMENT '시나리오번호'<br/>
 *  , eff_end_dtm DATETIME NOT NULL COMMENT '유효죵료일시'<br/>
 *  , audit_num INT(10) UNSIGNED NOT NULL COMMENT '권한자번호'<br/>
 *  , audit_dtm DATETIME NOT NULL COMMENT '권한일시'<br/>
 *  , scnrio_nm VARCHAR(255) NOT NULL COMMENT '시나리오명'<br/>
 *  , op_typ_num INT(5) NOT NULL COMMENT '처리유형번호'<br/>
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
@Repository("insertNewScnrioDao")
public class InsertNewScnrioDaoImpl implements InsertNewScnrioDao {
  private static Logger logger = LogManager.getLogger(InsertNewScnrioDaoImpl.class);

  @Autowired
  private SqlSession sqlSession;
  /**
   * 입력받은 시나리오 명 및 시나리오 설명을 신규 등록한다.
   *
   * @param inputMap Map형식으로 아래 내용이 포함된다.
   *        scnrio_nm : 시나리오 명(필수)
   *        scnrio_desc : 시나리오 설명(선택)
   *        user_num : 사용자번호(필수)
   *        system_call_dtm : AP 호출 시각(필수)
   * @exception SQLException SQL 수행 시 발생할 수 있는 예외
   */
  public void insertNewScnrio(Map<String, Object> inputMap) throws SQLException {
    logger.debug("============   Start method of InsertNewScnrioDaoImpl.insertNewScnrio   ============");
    logger.debug("Parameter inputMap[" + inputMap.toString() + "]");
    sqlSession.insert("com.skd.sel.sel_scnrio_mng.insertNewScnrio", inputMap);
    logger.debug("============   End method of InsertNewScnrioDaoImpl.insertNewScnrio   ============");
  }
  
  /**
   * 입력받은 시나리오명에 동일한 시나리오명이 존재하는지 체크한다.
   * 
   * @param inputMap Map 형식으로 아래 내용이 포함된다.
   *        scnrio_nm : 시나리오 명(필수)
   * @return 존재하면 true, 존재하지 않으면 false를 리턴한다.
   * @exception SQLException SQL 수행 시 발생할 수 있는 예외
   */
  public Map<String, Object> chkSameScnrioNm(Map<String, Object> inputMap) throws SQLException {
    logger.debug("============   Start method of InsertNewScnrioDaoImpl.chkSameScnrioNm   ============");
    logger.debug("Parameter inputMap[" + inputMap.toString() + "]");
    Map<String, Object> outputMap = null;
    outputMap = sqlSession.selectOne("com.skd.sel.sel_scnrio_mng.chkSameScnrioNm", inputMap);
    logger.debug("Output Map[" + outputMap.toString() + "]");
    logger.debug("============   End method of InsertNewScnrioDaoImpl.chkSameScnrioNm   ============");
    return outputMap;
  }
  
  /**
   * 신규로 등록된 시나리오번호를 출력한다.
   * 
   * @param inputMap Map 형식으로 아래 내용이 포함된다.
   *        scnrio_nm : 시나리오 명(필수)
   * @return 아래 항목을 갖는 맵을 출력한다.
   *         scnrio_num : 시나리오번호
   * @exception SQLException SQL 수행 시 발생할 수 있는 예외
   */
  public Map<String, Object> getNewScnrioNum(Map<String, Object> inputMap) throws SQLException {
    logger.debug("============   Start method of InsertNewScnrioDaoImpl.getNewScnrioNum   ============");
    logger.debug("Parameter inputMap[" + inputMap.toString() + "]");
    Map<String, Object> outputMap = null;
    outputMap = sqlSession.selectOne("com.skd.sel.sel_scnrio_mng.getNewScnrioNum", inputMap);
    logger.debug("Output Map[" + outputMap.toString() + "]");
    logger.debug("============   End method of InsertNewScnrioDaoImpl.getNewScnrioNum   ============");
    return outputMap;
  }
}