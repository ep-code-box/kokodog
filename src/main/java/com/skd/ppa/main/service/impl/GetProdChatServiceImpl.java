/*
 * Title : GetProdChatServiceImpl
 *
 * @Version : 1.0
 *
 * @Date : 2016-04-17
 *
 * @Copyright by 이민석
 */
package com.skd.ppa.main.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import com.cmn.cmn.service.GetDataFromURLService;
import com.skd.ppa.main.service.GetProdChatService;
import com.skd.ppa.main.dao.GetProdChatDao;
import com.cmn.err.SystemException;

/**
 *  이 클래스는 SK 주식회사 C&C DT 프로젝트 일환으로
 *  진행하고 있는 상품 요구명세서 분석 프로젝트의
 *  개발 프로젝트 중 일부에 포함된다.<br/>
 *  챗봇의 대화를 통해 상품 요건서를 정형화된 포맷에 만족할 수 있도록
 *  하는 기능을 제공한다.
 */
@Service("getProdChatService")
public class GetProdChatServiceImpl implements GetProdChatService {
  private static final String URL = "https://gateway.aibril-watson.kr/conversation/api/v1/workspaces/794a6b19-d9b3-495a-af13-e0ce2a993d7a/message?version=2017-05-26";
  private static final String USER= "7e406e3e-7f66-44ed-8473-fd6fc1f0f888";
  private static final String PASSWORD = "rQ3Bk6FqdxmR";
  private static final Logger logger = LogManager.getLogger(GetProdChatServiceImpl.class);

  @Autowired
  private GetDataFromURLService getDataFromURLService;
  
  @Autowired
  private GetProdChatDao getProdChatDao;
  
  @Autowired
  private SystemException systemException;
  /**
   *  이 메서드는 대화의 output을 리턴으로 받아주는 역할을 수행한다.
   *  @param text - 전달하고자 하는 메시지
   *  @param userNum - 사용자 번호
   *  @return 챗봇이 대답하는 데이터
   *  @throws 기타 예외
   */
  public String getProdChat(String text, int userNum) throws Exception {
    logger.debug("Start method of GetProdChatServiceImpl.getProdChat");
    boolean isFirstData = false;
    ServletRequestAttributes sra = null;
    HttpServletRequest request = null;
    Map<String, Object> inputMap = new HashMap<String, Object>();
    Map<String, Object> outputMap = null;
    Date systemCallDate = null;
    try {
      sra = (ServletRequestAttributes)RequestContextHolder.currentRequestAttributes();
      request = sra.getRequest();
      if (request == null || request.getSession() == null || request.getSession().getAttribute("system_call_dtm") == null) {
        systemCallDate = new Date();
      } else {
        systemCallDate = new Date(((Long)request.getSession().getAttribute("system_call_dtm")).longValue());
      }
    } catch (Exception e) {
      systemCallDate = new Date();
    }
    int conversationNum = 0;
    JSONObject urlParam = new JSONObject();
    JSONObject urlParamSubContext = new JSONObject();
    JSONObject urlParamSubContextSubSystem = new JSONObject();
    JSONObject urlParamSubInput = new JSONObject();
    inputMap.put("user_num", userNum);
    outputMap = getProdChatDao.getChatIdAndSeq(inputMap);
    if (outputMap != null && outputMap.get("conv_num") != null && outputMap.get("conv_id") != null) {
      conversationNum = ((Long)outputMap.get("conv_num")).intValue();
      urlParamSubContext.put("conversation_id", outputMap.get("conv_id"));
      urlParamSubContextSubSystem.put("dialog_turn_counter", ((Long)outputMap.get("dialog_counter")).intValue() + 1);
      urlParamSubContextSubSystem.put("dialog_request_counter", ((Long)outputMap.get("dialog_counter")).intValue() + 1);
      urlParamSubContextSubSystem.put("dialog_stack", outputMap.get("dialog_stack") != null ? JSONSerializer.toJSON(new String((byte[])outputMap.get("dialog_stack"), "UTF-8")) : null);
      urlParamSubContext.put("system", urlParamSubContextSubSystem);
      urlParamSubInput.put("text", text);
      urlParam.put("input", urlParamSubInput);
      urlParam.put("context", urlParamSubContext);
    } else if (outputMap != null && outputMap.get("conv_num") != null && outputMap.get("conv_id") == null) {
      throw systemException.systemException(21);
    } else {
      inputMap.clear();
      inputMap.put("user_num", userNum);
      outputMap = getProdChatDao.getMaxConvNum(inputMap);
      conversationNum = ((BigDecimal)outputMap.get("max_num")).intValue() + 1;
      inputMap.clear();
      inputMap.put("conv_num", conversationNum);
      inputMap.put("user_num", userNum);
      inputMap.put("now_dtm", systemCallDate);
      getProdChatDao.insertConvSpc(inputMap);
      isFirstData = true;
    }
    JSONObject retObject = (JSONObject)getDataFromURLService.getDataFromURL(URL, urlParam.toString(), "POST", "UTF-8", GetDataFromURLService.TYPE_JSON, USER, PASSWORD);
    if (isFirstData == true) {
      inputMap.clear();
      inputMap.put("conv_num", conversationNum);
      inputMap.put("user_num", userNum);
      inputMap.put("conv_id", retObject.getJSONObject("context").getString("conversation_id"));
      getProdChatDao.updateConvSpcConvId(inputMap);
    }
    if (urlParamSubContext.get("conversation_id") != null) {
      inputMap.clear();
      inputMap.put("user_num", userNum);
      inputMap.put("conv_num", conversationNum);
      inputMap.put("dialog_counter", urlParamSubContextSubSystem.get("dialog_turn_counter"));
      inputMap.put("text", text);
      inputMap.put("now_dtm", systemCallDate);
      inputMap.put("conv_main_cd", 1);
      inputMap.put("dialog_stack", ((JSONArray)urlParamSubContextSubSystem.get("dialog_stack")).toString());
      getProdChatDao.insertChatSeq(inputMap);
    }
    String outputStr = retObject.getJSONObject("output").getJSONArray("text").getString(0);
    inputMap.clear();
    inputMap.put("user_num", userNum);
    inputMap.put("conv_num", conversationNum);
    inputMap.put("dialog_counter", retObject.getJSONObject("context").getJSONObject("system").getInt("dialog_turn_counter"));
    inputMap.put("text", outputStr);
    inputMap.put("now_dtm", systemCallDate);
    inputMap.put("conv_main_cd", 2);
    inputMap.put("dialog_stack", retObject.getJSONObject("context").getJSONObject("system").getJSONArray("dialog_stack").toString());
    getProdChatDao.insertChatSeq(inputMap);
    return outputStr;
  }
  
  /**
   *  이 메서드는 기존 대화를 완전히 삭제하는 역할을 수행한다.
   *  @param userNum - 대화 결과를 삭제하고자 하는 사용자 번호
   *  @throws 기타 예외
   */
  public void refresh(int userNum) throws Exception {
    Map<String, Object> inputMap = new HashMap<String, Object>();
    ServletRequestAttributes sra = null;
    HttpServletRequest request = null;
    Date systemCallDate = null;
    try {
      sra = (ServletRequestAttributes)RequestContextHolder.currentRequestAttributes();
      request = sra.getRequest();
      if (request == null || request.getSession() == null || request.getSession().getAttribute("system_call_dtm") == null) {
        systemCallDate = new Date();
      } else {
        systemCallDate = new Date(((Long)request.getSession().getAttribute("system_call_dtm")).longValue());
      }
    } catch (Exception e) {
      systemCallDate = new Date();
    }
    inputMap.put("user_num", userNum);
    inputMap.put("now_dtm", systemCallDate);
    getProdChatDao.updateConvSpcPastEndDataUpdateToNow(inputMap);
  }

  /**
   *  이 메서드는 기존 대화를 기반으로 최초 30개의 데이터를 화면으로 전달한다.
   *  @param userNum - 대화 결과를 삭제하고자 하는 사용자 번호
   *  @throws 기타 예외
   */
  public List<Map<String, Object>> getProdInitChat(int userNum) throws Exception {
    Map<String, Object> inputMap = new HashMap<String, Object>();
    List<Map<String, Object>> outputList = null;
    Map<String, Object> outputMap = new HashMap<String, Object>();
    inputMap.put("user_num", userNum);
    outputList = getProdChatDao.getInitProdChat(inputMap);
    if (outputList.size() == 0) {
      outputList.clear();
      outputMap.put("text", getProdChat("", userNum));
      outputList.add(outputMap);
      return outputList;
    } else {
      return outputList;
    }
  }
}