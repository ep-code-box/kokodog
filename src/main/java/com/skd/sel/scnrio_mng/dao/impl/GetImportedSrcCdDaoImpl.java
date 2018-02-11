package com.skd.sel.sel_scnrio_mng.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.sql.SQLException;

import org.apache.ibatis.session.SqlSession;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.skd.sel.sel_scnrio_mng.dao.GetImportedSrcCdDao;

/**
 * 이 클래스는 셀레니움 테스트 플랫폼 시스템에서 시나리오 번호 및 케이스 번호가 소스코드를 제공하기 위한 DB 데이터를 조회해주는 역할을 수행한다.
 * 소스코드 생성을 위해서는 해당 시나리오에 해당하는 케이스번호 리스트, 각 케이스에 해당하는 입력값 리스트, 시나리오별 소스코드를 조회할 수 있어야 한다.<br/>
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
 * CREATE TABLE skd_sel_test_case (<br/>
 *    scnrio_num INT(10) UNSIGNED NOT NULL COMMENT '시나리오번호'<br/>
 *  , case_num INT(10) UNSIGNED NOT NULL COMMENT '케이스번호'<br/>
 *  , eff_end_dtm DATETIME NOT NULL COMMENT '유효죵료일시'<br/>
 *  , audit_num INT(10) UNSIGNED NOT NULL COMMENT '권한자번호'<br/>
 *  , audit_dtm DATETIME NOT NULL COMMENT '권한일시'<br/>
 *  , case_nm VARCHAR(255) NOT NULL COMMENT '케이스명'<br/>
 *  , case_desc TEXT(65535) NULL COMMENT '케이스상세'<br/>
 *  , seq_num INT(10) UNSIGNED NOT NULL COMMENT '순차번호'<br/>
 *  , eff_sta_dtm DATETIME NOT NULL COMMENT '유효시작일시'<br/>
 *  , PRIMARY KEY(scnrio_num, case_num, eff_end_dtm)<br/>
 * )  ENGINE=INNODB, COMMENT='셀레니움 테스트 케이스';<br/><br/>
 * 
 * CREATE TABLE skd_sel_test_input (<br/>
 *    scnrio_num INT(10) UNSIGNED NOT NULL COMMENT '시나리오번호'<br/>
 *  , input_num INT(5) UNSIGNED NOT NULL COMMENT '입력번호'<br/>
 *  , eff_end_dtm DATETIME NOT NULL COMMENT '유효죵료일시'<br/>
 *  , audit_num INT(10) UNSIGNED NOT NULL COMMENT '권한자번호'<br/>
 *  , audit_dtm DATETIME NOT NULL COMMENT '권한일시'<br/>
 *  , INPUT_NM VARCHAR(255) NOT NULL COMMENT '입력명'<br/>
 *  , INPUT_DESC TEXT(65535) NULL COMMENT '입력상세'<br/>
 *  , eff_sta_dtm DATETIME NOT NULL COMMENT '유효시작일시'<br/>
 *  , PRIMARY KEY(scnrio_num, input_num, eff_end_dtm)<br/>
 *  , INDEX SKD_SEL_TEST_INPUT_N1(scnrio_num, INPUT_NM)<br/>
 * )  ENGINE=INNODB, COMMENT='셀레니움 테스트 입력';<br/><br/>
 * 
 * CREATE TABLE skd_sel_test_case_input (<br/>
 *    scnrio_num INT(10) UNSIGNED NOT NULL COMMENT '시나리오번호'<br/>
 *  , case_num INT(10) UNSIGNED NOT NULL COMMENT '케이스번호'<br/>
 *  , input_num INT(5) UNSIGNED NOT NULL COMMENT '입력번호'<br/>
 *  , eff_end_dtm DATETIME NOT NULL COMMENT '유효죵료일시'<br/>
 *  , audit_num INT(10) UNSIGNED NOT NULL COMMENT '권한자번호'<br/>
 *  , audit_dtm DATETIME NOT NULL COMMENT '권한일시'<br/>
 *  , input_val VARCHAR(255) NOT NULL COMMENT '입력값'<br/>
 *  , eff_sta_dtm DATETIME NOT NULL COMMENT '유효시작일시'<br/>
 *  , PRIMARY KEY(scnrio_num, case_num, input_num, eff_end_dtm)<br/>
 * )  ENGINE=INNODB, COMMENT='셀레니움 테스트 케이스별 입력';<br/><br/>
 * 
 * @author  Minseok Lee
 * @since   2018.02.03
 * @version 1.0
 */
@Repository("getImportedSrcCdDao")
public class GetImportedSrcCdDaoImpl implements GetImportedSrcCdDao {
  private static Logger logger = LogManager.getLogger(GetImportedSrcCdDaoImpl.class);

  @Autowired
  private SqlSession sqlSession;

  /**
   * 입력받은 시나리오 번호를 기준으로 전체 케이스번호 리스트를 되돌려준다.
   *
   * @param inputMap Map형식으로 아래 내용이 포함된다.
   *        scnrio_num : 시나리오 번호(필수)
   * @return List 형식으로 아래 내용이 포함된 Map의 리스트를 되돌려준다.
   *        case_num : 케이스 번호
   * @exception SQLException SQL 수행 시 발생할 수 있는 예외
   */
  public List<Map<String, Object>> getTestCaseInfoByScnrioNum(Map<String, Object> inputMap) throws SQLException {
    logger.debug("============   Start method of GetImportedSrcCdDaoImpl.getTestCaseInfoByScnrioNum   ============");
    logger.debug("Parameter inputMap[" + inputMap.toString() + "]");
    List<Map<String, Object>> outputList = null;
    outputList = sqlSession.selectList("com.skd.sel.sel_scnrio_mng.getTestCaseInfoByScnrioNum", inputMap);
    logger.debug("Output List[" + outputList.toString() + "]");
    logger.debug("============   End method of GetImportedSrcCdDaoImpl.getTestCaseInfoByScnrioNum   ============");
    return outputList;
  }
  /**
   * 입력받은 시나리오 번호를 기준으로 전체 입력 리스트를 되돌려준다.
   *
   * @param inputMap Map형식으로 아래 내용이 포함된다.
   *        scnrio_num : 시나리오 번호(필수)
   * @return List 형식으로 아래 내용이 포함된 Map의 리스트를 되돌려준다.
   *        input_num : 입력 번호
   *        input_nm : 입력명
   *        input_val : 입력값
   * @exception SQLException SQL 수행 시 발생할 수 있는 예외
   */
  public List<Map<String, Object>> getTestInputByCaseNum(Map<String, Object> inputMap) throws SQLException {
    logger.debug("============   Start method of GetImportedSrcCdDaoImpl.getTestInputByCaseNum   ============");
    logger.debug("Parameter inputMap[" + inputMap.toString() + "]");
    List<Map<String, Object>> outputList = null;
    outputList = sqlSession.selectList("com.skd.sel.sel_scnrio_mng.getTestInputByCaseNum", inputMap);
    logger.debug("Output List[" + outputList.toString() + "]");
    logger.debug("============   End method of GetImportedSrcCdDaoImpl.getTestInputByCaseNum   ============");
    return outputList;
  }
  /**
   * 입력받은 시나리오 번호 기준으로 소스코드값을 되돌려준다.
   *
   * @param inputMap Map형식으로 아래 내용이 포함된다.
   *        scnrio_num : 시나리오 번호(필수)
   * @return Map 형식으로 아래 내용이 포함된 Map의 리스트를 되돌려준다.
   *        src_cd : 소스코드
   * @exception SQLException SQL 수행 시 발생할 수 있는 예외
   */
  public Map<String, Object> getSrcCdByScnrioNum(Map<String, Object> inputMap) throws SQLException {
    logger.debug("============   Start method of GetImportedSrcCdDaoImpl.getSrcCdByScnrioNum   ============");
    logger.debug("Parameter inputMap[" + inputMap.toString() + "]");
    Map<String, Object> outputMap = null;
    outputMap = sqlSession.selectOne("com.skd.sel.sel_scnrio_mng.getSrcCdByScnrioNum", inputMap);
    logger.debug("Output Map[" + outputMap.toString() + "]");
    logger.debug("============   End method of GetImportedSrcCdDaoImpl.getSrcCdByScnrioNum   ============");
    return outputMap;    
  }
}