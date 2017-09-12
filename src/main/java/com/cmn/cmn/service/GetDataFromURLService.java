/*
 * Title : GetDataFromURLService
 *
 * @Version : 1.0
 *
 * @Date : 2017-08-15
 *
 * @Copyright by 이민석
 */
package com.cmn.cmn.service;

import java.io.FileInputStream;
import java.util.Map;
import java.util.List;

/**
 *  이 객체는 입력받은 URL을 통해 http 혹은 https 프로토콜을 통하여 전달받은
 *  데이터를 되돌려 주는 역할을 수행한다.<br/>
 *  되돌려주는 데이터는 response를 통해 받은 String 그대로를 전달할 수 있고
 *  혹은 json 형태의 경우에는 json 형태로 되돌려주는 경우도 있다.<br/>
 *  이는 마지막 returnType 파라미터에 의해 결정되며 TYPE_JSON, TYPE_STRING, TYPE_BYTE에 따라서 결정된다.
 */
public interface GetDataFromURLService {
  /**
   *  getDataFromURL 호출 시 5번째 파라미터로 return Type을 결정해 줄 때 사용한다.
   *  TYPE_JSON은 json 형태로 되돌려받을 때 수행되며
   *  만약 response로 받은 데이터가 정상적인 JSON 형태가 아니라면 오류를 전달한다.
   */
  public static final int TYPE_JSON = 1;
  /**
   *  getDataFromURL 호출 시 5번째 파라미터로 return Type을 결정해 줄 때 사용한다.
   *  TYPE_STRING은 전달받은 response 의 string 형태를 그대로 되돌려준다.
   */
  public static final int TYPE_STRING = 2;
  /**
   *  getDataFromURL 호출 시 5번째 파라미터로 return Type을 결정해 줄 때 사용한다.
   *  TYPE_STRING은 전달받은 response 를 byte 배열로 전달한다.
   */
  public static final int TYPE_BYTE = 3;
  /**
   *  이 메서드는 URL을 호출한 결과를 되돌려받기 위한 함수이다.
   *  오류 발생시에는 exeption과 함께 결과를 되돌려줄 것이다.
   *  파라미터가 key=value 형태일 때 해당 데이터를 listData에 담아 전달한다.
   *  @param strURL - 호출 URL
   *  @param listData - key=value 형태의 list 형태의 데이터 셋
   *  @param method - 호출방식(POST, GET, PUT 등)
   *  @param encodingType - 인코딩 방식(UTF-8, EUC-KR 등)
   *  @param returnType - 리턴 형식(TYPE_JSON, TYPE_STRING, TYPE_BYTE)
   *  @return String 혹은 JSONObject 형태의 결과
   *  @throws 기타 오류
   */
  public Object getDataFromURL(String strUrl, List<Map<String, String>> listData, String method, String encodingType, int returnType) throws Exception;
  /**
   *  이 메서드는 URL을 호출한 결과를 되돌려받기 위한 함수이다.
   *  오류 발생시에는 exeption과 함께 결과를 되돌려줄 것이다.
   *  파라미터가 일반 스트링 형태로 전달받아야 할 때 사용한다.
   *  @param strURL - 호출 URL
   *  @param data - String 형태의 데이터 리스트
   *  @param method - 호출방식(POST, GET, PUT 등)
   *  @param encodingType - 인코딩 방식(UTF-8, EUC-KR 등)
   *  @param returnType - 리턴 형식(TYPE_JSON, TYPE_STRING, TYPE_BYTE)
   *  @return String 혹은 JSONObject 형태의 결과
   *  @throws 기타 오류
   */
  public Object getDataFromURL(String strUrl, String data, String method, String encodingType, int returnType, String user, String password) throws Exception;
}