package com.skd.sel.module.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import org.springframework.stereotype.Service;

import com.skd.sel.module.service.MakeSrcCdWithInputParamSvc;

/**
 * 이 클래스는 Selenium 테스트 자동화 도구 개발 프로젝트 내에서
 * 파라미터로 지정되어 있는 소스코드를 입력 파라미터와 함께 메서드 호출 시
 * 전달받아 소스코드 내 입력 변수를 치환하여 완벽한 테스트 소스 코드를 구현하는 데에 있다.<br/>
 * 치환 파라미터는 소스 내에 다음 형식으로 구성되어 있을 경우 치환된다.<br/>
 * ${ + 입력변수 명 + }
 * 예) a와 b가 변수이며 이를 소스에서 치환할 변수로 지정하기 위해서는 다음과 같이 구성한다.<br/>
 *     sum(Integer.parseInt("${a}") + Integer.parseInt("${b}"))
 *
 * @author  Minseok Lee
 * @since   2018.02.03
 * @version 1.0
 */
@Service("makeSrcCdWithInputParamSvc")
public class MakeSrcCdWithInputParamSvcImpl implements MakeSrcCdWithInputParamSvc {
  private static Logger logger = LogManager.getLogger(MakeSrcCdWithInputParamSvcImpl.class);
  
  /**
   * 입력 파라미터가 조합된 소스코드를 돌려준다.
   *
   * @param baseSrcCd 조합되기 전 입력 파라미터를 변수로 갖고 있는 소스
   * @param input 치환될 입력 값
   * @return 조합된 소스 코드소스 코드
   * @exception Exception 예상하지 못한 Exception으로 정의한다.
   */
  public String makeSrcCd(String baseSrcCd, List<Map<String, Object>> input) {
    String madeSrcCd = new String(baseSrcCd);
    String inputNm = null;
    int i = 0;
    int j = 0;
    int k = 0;
    for (i = 0; i < madeSrcCd.length(); i++) {
      if (madeSrcCd.charAt(i) == '$' && (i == 0 || madeSrcCd.charAt(i - 1) != '\\')) {
        i++;
        if (madeSrcCd.charAt(i) == '{') {
          for (j = i + 1; j < madeSrcCd.length(); j++) {
            if (madeSrcCd.charAt(j) == '{' && madeSrcCd.charAt(j - 1) == '$') {
              break;
            }
            if (madeSrcCd.charAt(j) == '}') {
              break;
            }
          }
          if (madeSrcCd.charAt(j) == '{' && madeSrcCd.charAt(j - 1) == '$') {
            i = j - 2;
            continue;
          } else if (j == madeSrcCd.length()) {
            i++;
            continue;
          } else {
            inputNm = madeSrcCd.substring(i + 1, j);
            for (k = 0; k < input.size(); k++) {
              if (inputNm.equals(input.get(k).get("input_nm")) == true) {
                break;
              }
            }
            if (k != input.size()) {
              madeSrcCd = madeSrcCd.substring(0, i - 1) + (String)input.get(k).get("input_val") + madeSrcCd.substring(j + 1);
              i = i + 1 + ((String)input.get(k).get("input_val")).length() - (j - i);
            } else {
              i++;
              madeSrcCd = madeSrcCd.substring(0, i - 2) + madeSrcCd.substring(j + 1);
              i = i + 1 - (j - i);
            }
          }
        }
      }
    }
    return madeSrcCd;
  }
  
  /**
   * 입력 파라미터 명 및 위치를 되돌려준다.
   *
   * @param baseSrcCd 조합되기 전 입력 파라미터를 변수로 갖고 있는 소스
   * @return 다음 맵 정보를 갖고 있는 리스트 집합
   *       input_nm : 입력 명
   *       pos: 위치
   * @exception Exception 예상하지 못한 Exception으로 정의한다.
   */
  public List<Map<String, Object>> getSrcCdInputNm(String baseSrcCd) throws Exception {
    String madeSrcCd = new String(baseSrcCd);
    String inputNm = null;
    int i = 0;
    int j = 0;
    List<Map<String, Object>> outputList = new ArrayList<Map<String, Object>>();
    for (i = 0; i < madeSrcCd.length(); i++) {
      if (madeSrcCd.charAt(i) == '$' && (i == 0 || madeSrcCd.charAt(i - 1) != '\\')) {
        i++;
        if (madeSrcCd.charAt(i) == '{') {
          for (j = i + 1; j < madeSrcCd.length(); j++) {
            if (madeSrcCd.charAt(j) == '{' && madeSrcCd.charAt(j - 1) == '$') {
              break;
            }
            if (madeSrcCd.charAt(j) == '}') {
              break;
            }
          }
          if (madeSrcCd.charAt(j) == '{' && madeSrcCd.charAt(j - 1) == '$') {
            i = j - 2;
            continue;
          } else if (j == madeSrcCd.length()) {
            i++;
            continue;
          } else {
            String inputVal = madeSrcCd.substring(i + 1, j).trim();
            for (j = 0; j < outputList.size(); j++) {
              if (((String)outputList.get(j).get("input_nm")).equals(inputVal) == true) {
                break;
              }
            }
            if (j == outputList.size()) {
              Map<String, Object> tmpMap = new HashMap<String, Object>();
              tmpMap.put("input_nm", inputVal);
              tmpMap.put("pos", i + 1);
              outputList.add(tmpMap);
            }
          }
        }
      }
    }
    return outputList;
  }
}