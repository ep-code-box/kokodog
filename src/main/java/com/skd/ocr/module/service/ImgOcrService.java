/*
 * Title : ImgOcrService
 *
 * @Version : 1.0
 *
 * @Date : 2017-08-10
 *
 * @Copyright by 이민석
 */
package com.skd.ocr.module.service;

import net.sf.json.JSONArray;

/**
 *  이 객체는 Python 내 OCR 관련 정보를 얻는
 *  프로그램을 실행하기 위해 수행한다.
 *  상세 프로그램을 수정하기 위해선 이 class 내
 *  method 들이 호출하고 있는 python 프로그램
 *  수정이 필요한다.
 */
public interface ImgOcrService {
  /**
   *  이 메서드는 String 형태로 전달받은 byte stream 이미지를
   *  python 모듈을 통해 ocr 분석 후 분석된 ocr 정보를 되돌려주는 역할을 수행한다.
   *  @param img - 이미지 파일 내용. 반드시 byte 스트림 형태의 파일을 전달해야 한다.
   *  @return JSONObject 타입의 OCR 분석 결과
   *  @throws 기타 예외
   */
  public JSONArray getImgOcrInfoByImgWithStr(byte[] img) throws Exception;
  
  /**
   *  이 메서드는 OCR Image 처리 전에 관리 대상 Image를 DB에 삽입하는 역할을 수행한다.
   *  @param fileKey - 이미지가 저장된 File Key
   *  @param userNum - 이미지를 관리할 사용자 번호
   *  @throws 기타 예외
   */
  public void insertUploadImgInfo(String fileKey, int userNum) throws Exception;
}