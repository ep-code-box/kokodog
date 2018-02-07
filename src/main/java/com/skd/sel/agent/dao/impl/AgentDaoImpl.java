package com.skd.sel.agent.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.sql.SQLException;

import org.apache.ibatis.session.SqlSession;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.skd.sel.agent.dao.AgentDao;

/**
 * 이 클래스는 AgentSvc 에서 사용하는 데이터를 로드하기 위한
 * 목적의 Dao 클래스이다.<br/>
 * 아래 스크립트를 통해 데이터 구조가 마련되어 있어야 해당 클래스의 오류가 발생하지 않는다.<br/>
 * Table 1 : skd_sel_test_scnrio(셀레니움 테스트 시나리오)<br/>
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
 * Table 2 : skd_sel_test_case(셀레니움 테스트 케이스)<br/>
 * CREATE TABLE skd_sel_test_case (<br/>
 *    scnrio_num INT(10) UNSIGNED NOT NULL COMMENT '시나리오번호'<br/>
 *  , case_num INT(10) UNSIGNED NOT NULL COMMENT '케이스번호'<br/>
 *  , eff_end_dtm DATETIME NOT NULL COMMENT '유효죵료일시'<br/>
 *  , audit_num INT(10) UNSIGNED NOT NULL COMMENT '권한자번호'<br/>
 *  , audit_dtm DAT6ㅛ78굔ETIME NOT NULL COMMENT '권한일시'<br/>
 *  , case_nm VARCHAR(255) NOT NULL COMMENT '케이스명'<br/>
 *  , case_desc TEXT(65535) NULL COMMENT '케이스상세'<br/>
 *  , seq_num INT(10) UNSIGNED NOT NULL COMMENT '순차번호'<br/>
 *  , eff_sta_dtm DATETIME NOT NULL COMMENT '유효시작일시'<br/>
 *  , PRIMARY KEY(scnrio_num, case_num, eff_end_dtm)<br/>
 * )  ENGINE=INNODB, COMMENT='셀레니움 테스트 케이스';<br/><br/>
 * 
 * Table 3 : skd_sel_test_input(셀레니움 테스트 입력)<br/>
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
 * Table 4 : skd_sel_test_case_input(셀레니움 테스트 케이스별 입력)<br/>
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
 * Table 5 : skd_sel_test_expt_rslt(셀레니움 테스트 기대 결과)<br/>
 * CREATE TABLE skd_sel_test_expt_rslt (<br/>
 *    scnrio_num INT(10) UNSIGNED NOT NULL COMMENT '시나리오번호'<br/>
 *  , case_num INT(10) UNSIGNED NOT NULL COMMENT '케이스번호'<br/>
 *  , test_step_num INT(10) UNSIGNED NOT NULL COMMENT '테스트순차번호'<br/>
 *  , eff_end_dtm DATETIME NOT NULL COMMENT '유효죵료일시'<br/>
 *  , audit_num INT(10) UNSIGNED NOT NULL COMMENT '권한자번호'<br/>
 *  , audit_dtm DATETIME NOT NULL COMMENT '권한일시'<br/>
 *  , rslt_strd VARCHAR(255) NULL COMMENT '결과기준'<br/>
 *  , judg_typ_cd INT(2) NOT NULL COMMENT '판별유형코드'<br/>
 *  , eff_sta_dtm DATETIME NOT NULL COMMENT '유효시작일시'<br/>
 *  , PRIMARY KEY(scnrio_num, case_num, test_step_num, eff_end_dtm)<br/>
 *  , INDEX SKD_SEL_TEST_EXPT_RSLT_N1(scnrio_num, case_num, eff_end_dtm, test_step_num)<br/>
 * )  ENGINE=INNODB, COMMENT='셀레니움 테스트 기대 결과';<br/><br/>
 * 
 * Table 6 : skd_sel_test_rslt_spc(셀레니움 테스트 결과 명세)<br/>
 * CREATE TABLE skd_sel_test_rslt_spc (<br/>
 *    scnrio_num INT(10) UNSIGNED NOT NULL COMMENT '시나리오번호'<br/>
 *  , case_num INT(10) UNSIGNED NOT NULL COMMENT '케이스번호'<br/>
 *  , test_expt_dtm DATETIME NOT NULL COMMENT '테스트예정일시'<br/>
 *  , audit_num INT(10) UNSIGNED NOT NULL COMMENT '권한자번호'<br/>
 *  , audit_dtm DATETIME NOT NULL COMMENT '권한일시'<br/>
 *  , real_test_sta_dtm DATETIME NOT NULL COMMENT '실테스트시작일시'<br/>
 *  , test_end_dtm DATETIME NOT NULL COMMENT '테스트종료일시'<br/>
 *  , PRIMARY KEY(scnrio_num, case_num, test_expt_dtm)<br/>
 * )  ENGINE=INNODB, COMMENT='셀레니움 테스트 결과 명세';<br/><br/>
 * 
 * Table 7 : skd_sel_test_rslt_dtl(셀레니움 테스트 결과 상세)<br/>
 * CREATE TABLE skd_sel_test_rslt_dtl (<br/>
 *    scnrio_num INT(10) UNSIGNED NOT NULL COMMENT '시나리오번호'<br/>
 *  , case_num INT(10) UNSIGNED NOT NULL COMMENT '케이스번호'<br/>
 *  , test_expt_dtm DATETIME NOT NULL COMMENT '테스트예정일시'<br/>
 *  , test_step_num INT(10) UNSIGNED NOT NULL COMMENT '테스트순차번호'<br/>
 *  , audit_num INT(10) UNSIGNED NOT NULL COMMENT '권한자번호'<br/>
 *  , audit_dtm DATETIME NOT NULL COMMENT '권한일시'<br/>
 *  , test_succ_yn VARCHAR(1) NOT NULL COMMENT '테스트성공여부'<br/>
 *  , test_rslt_log TEXT(65535) NULL COMMENT '테스트결과로그'<br/>
 *  , PRIMARY KEY(scnrio_num, case_num, test_expt_dtm, test_step_num)<br/>
 * )  ENGINE=INNODB, COMMENT='셀레니움 테스트 결과 상세';<br/><br/>
 * 
 * Table 8 : skd_sel_test_plan(셀레니움 테스트 계획)<br/>
 * CREATE TABLE skd_sel_test_plan (<br/>
 *    scnrio_num INT(10) UNSIGNED NOT NULL COMMENT '시나리오번호'<br/>
 *  , test_seq_num INT(5) UNSIGNED NOT NULL COMMENT '테스트순차번호'<br/>
 *  , eff_end_dtm DATETIME NOT NULL COMMENT '유효죵료일시'<br/>
 *  , audit_num INT(10) UNSIGNED NOT NULL COMMENT '권한자번호'<br/>
 *  , audit_dtm DATETIME NOT NULL COMMENT '권한일시'<br/>
 *  , sta_dt DATE NULL COMMENT '시작일자'<br/>
 *  , end_dt DATE NULL COMMENT '종료일자'<br/>
 *  , sta_tm TIME NULL COMMENT '시작시각'<br/>
 *  , end_tm TIME NULL COMMENT '종료시각'<br/>
 *  , exec_cycl INT(4) NULL COMMENT '수행주기'<br/>
 *  , wkday_exec_yn VARCHAR(1) NOT NULL COMMENT '평일수행여부'<br/>
 *  , hkday_exec_yn VARCHAR(1) NOT NULL COMMENT '휴일수행여부'<br/>
 *  , eff_sta_dtm DATETIME NOT NULL COMMENT '유효종료일시'<br/>
 *  , PRIMARY KEY(scnrio_num, test_seq_num, eff_end_dtm)<br/>
 *  , INDEX skd_sel_test_plan_n1(sta_dt, end_dt, scnrio_num)<br/>
 * )  ENGINE=INNODB, COMMENT='셀레니움 테스트 계획';<br/><br/>
 *
 * @author  Minseok Lee
 * @since   2018.02.03
 * @version 1.0
 */
@Repository("agentDao")
public class AgentDaoImpl implements AgentDao {
  private static Logger logger = LogManager.getLogger(AgentDaoImpl.class);

  @Autowired
  private SqlSession sqlSession;

  /**
   * 현재 시각 기준으로 수행계획에는 있지만 수행되지 않는 모든 시나리오 리스트를 되돌려준다.
   *
   * @return 시나리오 리스트가 담겨진 집합
   *         scnrio_num - 시나리오 번호
   *         src_cd - 파이썬 코드
   * @exception SQLException 쿼리 수행 중 발생할 수 있는 오류
   */
  public List<Map<String, Object>> getAllPlannedScnrio() throws SQLException {
    logger.debug("============   Start method of AgentDaoImpl.getAllPlanedScnrioInfo   ============");
    List<Map<String, Object>> outputList = null;
    outputList = sqlSession.selectList("com.skd.sel.agent.getAllPlannedScnrio");
    logger.debug("Output List[" + outputList.toString() + "]");
    logger.debug("============   End method of AgentDaoImpl.getAllPlanedScnrioInfo   ============");
    return outputList;
  }
  
  /**
   * 시나리오를 Input 으로 받아 해당 시나리오에 해당하는 각종 정보를 되돌려준다.
   *
   * @param inputMap 아래 정보를 담고 있는 Map 집합니다.
   *        scnrio_num - 정보를 조회하고자 하는 시나리오 번호(int, 필수)
   * @return 시나리오 리스트가 담겨진 집합
   *         src_cd - 파이썬 코드
   * @exception SQLException 쿼리 수행 중 발생할 수 있는 오류
   */
  public Map<String, Object> getScnrioInfo(Map<String, Object> inputMap) throws SQLException {
    logger.debug("============   Start method of AgentDaoImpl.getScnrioInfo   ============");
    logger.debug("Input Map[" + inputMap.toString() + "]");
    Map<String, Object> outputMap = null;
    outputMap = sqlSession.selectOne("com.skd.sel.agent.getScnrioInfo", inputMap);
    logger.debug("Output Map[" + outputMap.toString() + "]");
    logger.debug("============   End method of AgentDaoImpl.getScnrioInfo   ============");
    return outputMap;
  }
  
  /**
   * 시나리오 번호 기준으로 수행할 모든 케이스 리스트를 돌려준다.
   *
   * @param inputMap 시나리오 정보가 담긴 SQL Query 입력값
   *        scnrio_num - 시나리오 번호 (숫자형식(int), 필수)
   * @return 테스트 케이스별 정보가 담겨진 집합
   *         case_num - 케이스 번호
   * @exception SQLException 쿼리 수행 중 발생할 수 있는 오류
   */
  public List<Map<String, Object>> getCaseWithScnrio(Map<String, Object> inputMap) throws SQLException {
    logger.debug("============   Start method of AgentDaoImpl.getCaseWithScnrio   ============");
    logger.debug("Input Map[" + inputMap.toString() + "]");
    List<Map<String, Object>> outputList = null;
    outputList = sqlSession.selectList("com.skd.sel.agent.getCaseWithScnrio", inputMap);
    logger.debug("Output Map[" + outputList.toString() + "]");
    logger.debug("============   End method of AgentDaoImpl.getCaseWithScnrio   ============");
    return outputList;
  }

  /**
   * 시나리오 번호 기준으로 각 케이스별 수행할 모든 입력값 리스트를 돌려준다.
   *
   * @param inputMap 시나리오 정보가 담긴 SQL Query 입력값
   *        scnrio_num - 시나리오 번호 (숫자형식(int), 필수)
   *        case_num - 케이스 번호 (숫자형식(int), 필수)
   * @return 테스트 케이스별 입력가 담겨진 집합
   *         input_num - 입력 번호
   *         input_nm - 입력 명칭
   *         input_val - 입력 값
   * @exception SQLException 쿼리 수행 중 발생할 수 있는 오류
   */
  public List<Map<String, Object>> getInputWithScnrioAndCase(Map<String, Object> inputMap) throws SQLException {
    logger.debug("============   Start method of AgentDaoImpl.getInputWithScnrioAndCase   ============");
    logger.debug("Input Map[" + inputMap.toString() + "]");
    List<Map<String, Object>> outputList = null;
    outputList = sqlSession.selectList("com.skd.sel.agent.getInputWithScnrioAndCase", inputMap);
    logger.debug("Output Map[" + outputList.toString() + "]");
    logger.debug("============   End method of AgentDaoImpl.getInputWithScnrioAndCase   ============");
    return outputList;
  }
}