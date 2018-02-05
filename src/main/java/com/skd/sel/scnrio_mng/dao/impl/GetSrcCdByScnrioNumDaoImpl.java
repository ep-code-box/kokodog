package com.skd.sel.sel_scnrio_mng.dao.impl;

import java.util.Map;
import java.sql.SQLException;

import org.apache.ibatis.session.SqlSession;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.skd.sel.sel_scnrio_mng.dao.GetSrcCdByScnrioNumDao;

/**
 * 이 클래스는 셀레니움 테스트 플랫폼 시스템에서 시나리오 번호 입력값이 있을 때 Python 소스 코드를 가져오기 위한 클래스이다.<br/>
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
@Repository("getSrcCdByScnrioNumDao")
public class GetSrcCdByScnrioNumDaoImpl implements GetSrcCdByScnrioNumDao {
  private static Logger logger = LogManager.getLogger(GetSrcCdByScnrioNumDaoImpl.class);

  @Autowired
  private SqlSession sqlSession;
  /**
   * 입력받은 시나리오 번호를 기준으로 소스코드를 되돌려준다.
   *
   * @param inputMap Map형식으로 아래 내용이 포함된다.
   *        scnrio_num : 시나리오 번호(필수)
   * @return Map 형식으로 아래 내용이 포함된다.
   *        src_cd : 소스코드
   * @exception SQLException SQL 수행 시 발생할 수 있는 예외
   */
  public Map<String, Object> getSrcCdByScnrioNum(Map<String, Object> inputMap) throws SQLException {
    logger.debug("============   Start method of GetSrcCdByScnrioNumDaoImpl.getSrcCdByScnrioNum   ============");
    logger.debug("Parameter inputMap[" + inputMap.toString() + "]");
    Map<String, Object> outputMap = null;
    outputMap = sqlSession.selectOne("com.skd.sel.sel_scnrio_mng.getSrcCdByScnrioNum", inputMap);
    logger.debug("Output Map[" + outputMap.toString() + "]");
    logger.debug("============   End method of GetSrcCdByScnrioNumDaoImpl.getSrcCdByScnrioNum   ============");
    return outputMap;    
  }
}