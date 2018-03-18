/*
 * Title : DocNlpServiceImpl
 *
 * @Version : 1.0
 *
 * @Date : 2017-08-15
 *
 * @Copyright by 이민석
 */
package com.skd.ppa.main.service.impl;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skd.ppa.main.dao.GetProdChkListDao;
import com.skd.ppa.main.service.DocNlpService;
import com.skd.ppa.main.service.GetConvHtmlService;
import com.skd.ppa.module.service.DocConvWithAibrilService;
import com.skd.ppa.module.service.NlpByKonlpyService;
import com.cmn.err.UserException;

/**
 *  이 객체는 Python 내 Konlpy를 사용하여 각종
 *  주어진 문서를 기준으로 Natural Langauge Processing을 수행하는 기능을 제공한다.
 *  사용할 수 있는 문서 타입은 MS Word(doc, docx), Excel(xls, xlsx), PPT(ppt, pptx)이다.
 *  모든 return은 JSONArray를 상속받은 JSON 객체들로 리턴된다.
 *  형태소 분리는 미리 작성된 Python 로직에 의해 수행된다.
 *  상세 내용을 수정하기 위헤서는 Python 로직 수정이 필요하다.
 */
@Service("docNlpService")
public class DocNlpServiceImpl implements DocNlpService {
  private static Logger logger = LogManager.getLogger(DocNlpServiceImpl.class);
  
  @Autowired
  private DocConvWithAibrilService docConvWithAibrilService;
  
  @Autowired
  private GetConvHtmlService getConvHtmlService;
  
  @Autowired
  private NlpByKonlpyService nlpByKonlpyService;
  
  @Autowired
  private GetProdChkListDao getProdChkListDao;
  
  /**
   *  이 메서드는 Konlpy 내 사전에 등록된 명사 리스트를 JSON으로 리턴해주는 역할을 수행한다.
   *  @param fis : 문서의 Input Stream
   *  @return 명사 리스트
   *  @throws 기타 익셉션
   */
  public JSONArray getNounList(FileInputStream fis) throws Exception {
    logger.debug("Start method of DocNlpServiceImpl.getNounList");
    return nlpByKonlpyService.getNounList(docConvWithAibrilService.convToHtml(fis));
  }

  /**
   *  이 메서드는 Konlpy 내 사전에 등록된 전체 형태소 리스트를 분류별로 JSON으로 리턴해주는 역할을 수행한다.
   *  @param fis : 문서의 Input Stream
   *  @return 형태소 리스트
   *  @throws 기타 익셉션
   */
  public JSONArray getMorpheme(FileInputStream fis) throws Exception {
    return nlpByKonlpyService.getMorpheme(docConvWithAibrilService.convToHtml(fis));
  }
  
  /**
   *  이 메서드는 Konlpy 내 사전에 등록된 전체 명사 리스트를 분류별로 JSON으로 리턴해주는 역할을 수행한다.
   *  @param fileKey : 분석하고자 하는 HTML의 파일 Key
   *  @return 형태소 리스트
   *  @throws 기타 익셉션
   */
  public JSONArray getNounList(String fileKey) throws Exception {
    String html = getConvHtmlService.getHtml(fileKey);
    return nlpByKonlpyService.getNounList(html);
  }
  
  /**
   *  이 메서드는 Konlpy 내 사전에 등록된 전체 형태소 리스트를 분류별로 JSON으로 리턴해주는 역할을 수행한다.
   *  다른 메서드와 달리 세세한 부분까지 모두 리턴한다.
   *  @param fileKey : 분석하고자 하는 HTML의 파일 Key
   *  @return 형태소 리스트
   *  @throws 기타 익셉션
   */
  public JSONArray getMorphemeDetail(String fileKey) throws Exception {
    String html = getConvHtmlService.getHtml(fileKey);
    return nlpByKonlpyService.getMorphemeDetail(html);
  }
  
  /**
   *  이 메서드는 Konlpy 내 사전에 등록된 전체 형태소 리스트를 분류하고
   *  분류된 결과를 바탕으로 상품요건서분석에 맞는 결과를 회신해주는 역할을 수행한다.
   *  @param fileKey : 분석하고자 하는 HTML의 파일 Key
   *  @return Map 타입의 결과 회신
   *       chk_lst_num : 체크리스트 번호
   *       chk_lst_nm : 체크리스트 명칭
   *       chk_lst_yn : 문서 내 요소 체크리스트 포함 여부
   *  @throws 기타 익셉션
   */
  @SuppressWarnings("unchecked")
  public Map<String, Object> getProdChkLstDetail(String fileKey) throws Exception {
    int i = 0;
    int j = 0;
    int k = 0;
    String[] splitType = null;
    Map<String, Object> returnMap = new HashMap<String, Object>();
    List<Map<String, Object>> outputList = null;
    JSONArray jsonMorphemeList = getMorphemeDetail(fileKey);
    List<Map<String, Object>> morphemeList = new ArrayList<Map<String, Object>>();
    Map<String, Object> tmpMap = null;
    List<String> tmpList = null;
    for (i = 0; i < jsonMorphemeList.size(); i++) {
      tmpMap = new HashMap<String, Object>();
      tmpMap.put("voca", jsonMorphemeList.getJSONObject(i).getString("voca"));
      tmpList = new ArrayList<String>(); 
      for (j = 0; j < jsonMorphemeList.getJSONObject(i).getJSONArray("type").size(); j++) {
        tmpList.add(jsonMorphemeList.getJSONObject(i).getJSONArray("type").getString(j));
      }
      tmpMap.put("type", tmpList);
      morphemeList.add(tmpMap);
    }
    returnMap.put("morpheme_detail_list", morphemeList);
    outputList = getProdChkListDao.getAllProdChkList();
    returnMap.put("chk_list", outputList);
    for (i = 0; i < outputList.size(); i++) {
      outputList.get(i).put("chk_lst_yn", "N");
      outputList.get(i).put("chk_lst_voca", new ArrayList<Map<String, Object>>());
    }
    for (i = 0; i < jsonMorphemeList.size(); i++) {
      if ((jsonMorphemeList.getJSONObject(i).getJSONArray("type").getString(1).length() >= 7)
          && (jsonMorphemeList.getJSONObject(i).getJSONArray("type").getString(1).substring(0, 7).equals("상품요건서분석") == true)) {
        splitType = jsonMorphemeList.getJSONObject(i).getJSONArray("type").getString(1).split("\\|");
        for (j = 1; j < splitType.length; j++) {
          try {
            Integer.parseInt(splitType[j]);
          } catch (NumberFormatException e) {
            throw new UserException(3, splitType[i]);
          }
          for (k = 0; k < outputList.size(); k++) {
            if (((Integer)outputList.get(k).get("lst_num")).intValue() == Integer.parseInt(splitType[j])) {
              break;
            }
          }
          if (k < outputList.size()) {
            outputList.get(k).remove("chk_lst_yn");
            outputList.get(k).put("chk_lst_yn", "Y");
            tmpMap = new HashMap<String, Object>();
            tmpMap.put("voca_num", i);
            tmpMap.put("voca", jsonMorphemeList.getJSONObject(i).getString("voca"));
            ((List<Map<String, Object>>)outputList.get(k).get("chk_lst_voca")).add(tmpMap);
          }
        }
      }
    }
    return returnMap;
  }
}