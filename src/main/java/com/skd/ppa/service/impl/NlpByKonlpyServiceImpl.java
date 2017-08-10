/*
 * Title : NlpByKonlpyServiceImpl
 *
 * @Version : 1.0
 *
 * @Date : 2017-08-10
 *
 * @Copyright by 이민석
 */
package com.skd.ppa.service.impl;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import net.sf.json.JSONArray;
import net.sf.json.JSONSerializer;

import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skd.ppa.service.NlpByKonlpyService;
import com.cmn.err.SystemException;

/**
 *  이 객체는 Python 내 Konlpy를 사용하여 각종
 *  Natural Langauge Processing을 수행하는 기능을 제공한다.
 *  모든 return은 JSONArray를 상속받은 JSON 객체들로 리턴된다.
 *  형태소 분리는 미리 작성된 Python 로직에 의해 수행된다.
 *  상세 내용을 수정하기 위헤서는 Python 로직 수정이 필요하다.
 */
@Service("nlpByKonlpyService")
public class NlpByKonlpyServiceImpl implements NlpByKonlpyService {
  private static Logger logger = Logger.getLogger(NlpByKonlpyServiceImpl.class);
  private static final String KONLPY_EXE = "/home/leems83/workspace/proposal_analysis/python/keyword_for_proposal.py";
  
  @Autowired
  private SystemException systemException;

  /**
   *  이 메서드는 Konlpy 내 사전에 등록된 명사 리스트를 JSON으로 리턴해주는 역할을 수행한다.
   *  @param str : 명사 리스트를 추출하기 위한 기본 텍스트
   *  @return 명사 리스트
   *  @throws 기타 익셉션
   */
  public JSONArray getNounList(String str) throws Exception {
    Process oProcess = new ProcessBuilder("python3", KONLPY_EXE, str).start();

    // 외부 프로그램 출력 읽기
    BufferedReader stdOut   = new BufferedReader(new InputStreamReader(oProcess.getInputStream()));
    BufferedReader stdError = new BufferedReader(new InputStreamReader(oProcess.getErrorStream()));

    // "표준 출력"과 "표준 에러 출력"을 출력
    String errorStr = "";
    String stdOutStr = "";
    String tmpStr = null;
    while ((tmpStr = stdOut.readLine()) != null) {
      stdOutStr = stdOutStr + tmpStr;
    }
    while ((tmpStr = stdError.readLine()) != null) {
      errorStr = errorStr + tmpStr;
    }
    if ("".equals(errorStr) == false) {
      throw systemException.systemException(35);
    }
    return (JSONArray)JSONSerializer.toJSON(stdOutStr);
  }
  /**
   *  이 메서드는 Konlpy 내 사전에 등록된 전체 형태소 리스트를 분류별로 JSON으로 리턴해주는 역할을 수행한다.
   *  @param str : 형태소를 추출하기 위한 기본 글
   *  @return 형태소 리스트
   *  @throws 기타 익셉션
   */
  public JSONArray getMorpheme(String str) throws Exception {
    return null;
  }
}