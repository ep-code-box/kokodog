package com.skd.sel.agent.service;

import java.util.List;
/**
 * 이 클래스는 Selenium 테스트 자동화 도구 개발 프로젝트 내에서
 * Agent에서 필요한 정보를 제공해주는 역할과
 * 테스트 결과를 Agent로 부터 받아 alert 을 띄워줄 데이터를 수집하고
 * 매니징 화면에 필요한 데이터들을 Display할 수 있는 기반의 데이터를
 * 만들어주는 역할을 수행한다.<br/>
 * 지금까지 수행하는 역할을 정리해보면 다음과 같다.<br/>
 * <ul>
 * <li>주기적으로 Agent에게 테스트에 필요한 데이터를 요청받으면
 *     입력된 테스트 주기를 기반으로 추가 테스트가 필요한 대상을 산출하여
 *     Agent에게 전달하는 역할을 수행한다.</li>
 * <li>Agent에서 테스트가 완료된 경우 해당 테스트 결과를 시스템 내 데이터에
 *     삽입할 수 있는 기능을 제공한다.</li>
 * </ul>
 *
 * @author  Minseok Lee
 * @since   2018.02.03
 * @version 1.0
 */
public interface AgentSvc {
  /**
   * 현재 시점에 필요한 테스트 리스트 및 소스코드를 전달한다.
   *
   * @return List에 아래 내용을 담긴 Map을 순차적으로 담아 현재 테스트에 필요한 정보를 제공한다.
   *         scnrio_num : 시나리오 번호 (Integer 타입)
   *         해당 시나리오 별 Map 하위에 다음 정보를 담음(test_case).
   *               case_num : 케이스 번호 (Integer 타입)
   *               src_cd : Python으로 되어 있는 text 소스 코드
   *         exec_expt_dtm : 테스트 예정 시각
   *         server_dtm : 서버시각
   * @exception Exception 예상하지 못한 Exception으로 정의한다.
   */
  public List<Object> getCurTestLst() throws Exception;
  
  /**
   * 테스트 결과를 전달받아 데이터에 저장한다.
   *
   * @param input List에 아래 내용이 담긴 Map을 담은 Input을 기반으로 테스트 결과를 저장한다.
   *         ScnrioId : 시나리오 ID (Integer 타입, 필수)
   *         해당 시나리오 ID별 Map에 아래 내용을 추가로 삽입(Test)
   *                 CaseId : 케이스 ID (Integer 타입, 필수)
   *                 해당 CaseID 별 아래 내용을 Map 타입으로 추가로 삽입(TestDtl)
   *                         StepNum : Case 수행 별 Step
   *                         Log : Step별 결과 로그
   *         TestExptDtm : 테스트 예정 시각
   *         TestDtm : 서버에서 전달한 실제 테스트 시각
   * @exception Exception 예상하지 못한 Exception으로 정의한다.
   */
  public void insertTestRslt(List<Object> inputList) throws Exception;
}