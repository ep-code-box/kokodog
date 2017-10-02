/*
 * Title : Batch
 *
 * @Version : 1.0
 *
 * @Date : 2017-04-30
 *
 * @Copyright by 이민석
 */

package com.cmn.cmn.batch;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import org.apache.ibatis.session.SqlSession;

/**
 *  이 객체는 프로그램 Batch 개발 시에 해당 프로그램을 확장하여 개발을 진행하도록
 *  도와주는 객체이다.<br/>
 *  모든 Kokodog 내부에서 구현되는 Batch는 해당 Batch를 확장하여 개발을 진행해야 하며
 *  이를 어길시에는 배치가 수행되지 않고 UserException이 발생하게 된다.(메시지 번호 : 17번)<br/>
 *  해당 Batch를 확장하여 개발 시 유용한 메서드는 다음과 같다.<br/>
 *  <ul>
 *  <li> addLog(String msg) - 로그를 실시간으로 기록할 필요가 있을 때 작성한다.
 *  ex) addLog("테스트 로그를 작성합니다.");
 *  </li> getErrNum() - 오류 메시지 번호를 리턴해준다.
 *  </ul>
 *  주의 - 해당 프로그램은 반드시 Database에 다음 스크립트를 갖는 Table이
 *  구축되어 있어야 한다.<br/>
 *  1번 : 테이블명 - cmn_batch, 설명 - 수행해야 할 배치 리스트를 정의한다. <br/>
 *  CREATE TABLE `cmn_batch` (<br/>
 *   `batch_num` int(5) NOT NULL,<br/>
 *   `eff_end_dtm` datetime NOT NULL,<br/>
 *   `audit_id` int(10) NOT NULL,<br/>
 *   `audit_dtm` datetime NOT NULL,<br/>
 *   `batch_nm` varchar(80) NOT NULL,<br/>
 *   `batch_exe_nm` varchar(200) NOT NULL,<br/>
 *   `start_time` time DEFAULT NULL,<br/>
 *   `end_time` time DEFAULT NULL,<br/>
 *   `period` int(5) DEFAULT NULL,<br/>
 *   `week_yn` varchar(1) NOT NULL,<br/>
 *   `holy_yn` varchar(1) NOT NULL,<br/>
 *   `auto_rerun` varchar(1) NOT NULL,<br/>
 *   `exe_param` varchar(255) DEFAULT NULL,<br/>
 *   `eff_sta_dtm` datetime NOT NULL<br/>
 * ) ENGINE=InnoDB DEFAULT CHARSET=utf8;<br/>
 *  <br/>
 *  2번 : 테이블명 - cmn_batch_exe_hst, 설명 - 배치 수행 리포트를 관리한다. <br/>
 *  CREATE TABLE `cmn_batch_exe_hst` (<br/>
 *   `exe_dtm` datetime NOT NULL,<br/>
 *   `batch_num` int(5) NOT NULL,<br/>
 *   `seq` int(5) NOT NULL,<br/>
 *   `audit_id` int(10) NOT NULL,<br/>
 *   `audit_dtm` datetime NOT NULL,<br/>
 *   `real_exe_dtm` datetime NOT NULL,<br/>
 *   `real_end_dtm` datetime DEFAULT NULL,<br/>
 *   `batch_exe_state` int(2) NOT NULL,<br/>
 *   `batch_exe_err_num` int(11) DEFAULT NULL,<br/>
 *   `batch_result_report` mediumtext,<br/>
 *   `batch_proc_log_report` mediumtext,<br/>
 *   `batch_err_report` mediumtext,<br/>
 *   `ap_num` int(2) UNSIGNED NOT NULL,<br/>
 *   `container_num` int(2) UNSIGNED NOT NULL<br/>
 *  ) ENGINE=MyISAM DEFAULT CHARSET=utf8;<br/>
 */
public abstract class Batch {
  private static Logger logger = LogManager.getLogger(Batch.class);
  protected SqlSession sqlSession;
  private String report;
  private int errNum = 0;
  private int batchNum = 0;
  private long exeDtm = 0L;
  
  /**
   *  Batch 프로세스 수행 생성자
   */
  public Batch() {
    logger.debug("============   Start constructor of Batch(Parameter - null)   ============");
  }
  
  /**
   *  Batch 프로세스 수행 시 DB에 접근하기 위한 sqlSession을 전달받는 메서드
   *  일반적인 배치에서는 사용해서는 안되며, Manage에서만 사용하도록 구현되어 있다.
   *  @param sqlSession : DB Connection Pool을 관리하는 객체
   */
  public void setSqlSession(SqlSession sqlSession) {
    logger.debug("============   Start method of Batch.setSqlSession   ============");
    this.sqlSession = sqlSession;
  }
  
  /**
   *  Batch 프로그램의 작성은 해당 메서드를 반드시 override해서 구현한다.
   *  @param batchRunTime : 요청받은 수행 일시. 실제 수행 일시와 다를 수 있다.
   *  @param param : 배치 수행 요청 시 참조할 파라미터. 배치 등록 시 DB cmn_batch.exe_param 값이 전달된다.
   *  @throw Exception : 배치 수행 시에 발생할 수 있는 모든 Exception을 정의한다.
   */
  public abstract void run(long batchRunTime, String param) throws Exception;
  
  /**
   *  해당 배치의 수행 결과 Report를 얻기 위한 메서드이다. 일반적으로 배치 수행 시에
   *  setReport(String) 메서드를 통해 배치 결과를 기록하면 DB 내 cmn_batch_exe_hst.batch_result_report
   *  컬럼에 결과가 남는다.
   *  @return 결과 리포트
   */
  public String getReport() {
    return this.report;
  }
  
  /**
   *  해당 배치의 수행 Log를 남기기 위한 메서드이다. 일반적으로 배치 수행 시에
   *  setReport(String) 메서드를 통해 배치 결과를 기록하면 DB 내 cmn_batch_exe_hst.batch_proc_log_report
   *  컬럼에 결과가 남는다. 로그는 해당 메서드를 통해 내용 추가만 가능하며, 이미 작성된 로그의 삭제 등은 원칙적으로 불가하다.
   *  @param str : 수행 로그에 추가할 문구 내용으로 정의한다.
   *  @throw Exception : 배치 수행 로그 작성 시 발생할 수 있는 Exception을 정의한다.
   */
  public void addLog(String str) throws Exception {
    Map<String, Object> inputMap = new HashMap<String, Object>();
    inputMap.put("proc_log", str);
    inputMap.put("batch_num", batchNum);
    inputMap.put("exe_dtm", new Date(exeDtm));
    sqlSession.update("com.cmn.cmn.batch.updateBatchAddLog", inputMap);
  }
  
  /**
   *  해당 배치의 오류 번호를 남기기 위함이다. cmn_batch_exe_hst.batch_exe_err_num에 남으며
   *  예상되는 배치 오류의 번호를 그룹화하기 위함이다.
   *  @return 오류 번호
   */
  public int getErrNum() {
    return this.errNum;
  }
  
  /**
   *  수행되는 배치의 번호이다. 배치 매니저 외에 해당 번호를 수정해서는 안된다.
   *  @param batchNum : 배치 번호. DB내 cmn_batch.batch_num 값이 전달된다.
   */
  public void setBatchNum(int batchNum) {
    this.batchNum = batchNum;
  }
  
  /**
   *  수행되는 배치의 수행 일시이다. 실제 수행 일시가 아닌, 요청해야 하는 일시를 의미한다.
   *  배치가 정상적으로 수행된다면 실제 수행 일시와 같아야 하나, 작업 등의 원인으로
   *  배치가 딜레이 될 경우는 실제 수행 일시와 다를 수 있다.
   *  @param exeDtm : 배치 수행 시각. 오류 후 재수행이나 배치 딜레이로 인해 실제 수행 시각과 다를 수 있으며 DB내 cmn_batch.exe_dtm 값이 전달된다.
   */
  public void setExeDtm(long exeDtm) {
    this.exeDtm = exeDtm;
  }
  
  /**
   *  수행되는 배치의 최종 리포트를 전달한다.
   *  @param report : 저장해야 할 배치의 최종 리포트 결과 내용.
   */
  public void setReport(String report) {
    this.report = report;
  }
  
  /**
   *  수행되는 배치의 배치 번호를 전달한다.
   *  @param batchNum : 저장해야 할 배치의 번호.
   */
  public int getBatchNum() {
    return this.batchNum;
  }
}