/*
 * Title : GetPageAuthServiceImpl
 *
 * @Version : 1.0
 *
 * @Date : 2017-09-24
 *
 * @Copyright by 이민석
 */
package com.cmn.err.err_401.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;

import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import com.cmn.err.err_401.service.GetPageAuthService;
import com.cmn.err.err_401.dao.GetPageAuthDao;

/**
 *  이 객체는 사용자가 Page접근 권한이 없을 때
 *  각종 권한을 관리하고 요청하는 역할을 정의하는
 *  서비스로 이루어진다.
 */
@Service("getPageAuthService")
public class GetPageAuthServiceImpl implements GetPageAuthService {
  /**
   *  요청 request 별로 갖고 있는 권한 리스트를 불러온다.
   *  @param path - 패스 리스트로 정의된다.
   *  @return List 타입으로 내부 요소는 Map<String, Object> 형태를 가지며 file_key, 등록일시, 파일명을 결과로 전달한다.
   *  @throws 기타 Exception
   */
  
  @Autowired
  private GetPageAuthDao getPageAuthDao;
  public List<Map<String, Object>> getAuthListByPath(String path, int userNum) throws Exception {
    String[] pathSplit = path.split("/");
    int startPos = 0;
    Map<String, Object> inputMap = new HashMap<String, Object>();
    if ("".equals(pathSplit[0]) == true) {
      startPos = 1;
    } else {
      startPos = 0;
    }
    if (pathSplit.length <= startPos || "".equals(pathSplit[startPos])) {
      inputMap.put("pgm_abb", "cmn");
    } else {
      inputMap.put("pgm_abb", pathSplit[startPos]);
    }
    if (pathSplit.length <= startPos + 1 || "".equals(pathSplit[startPos + 1])) {
      inputMap.put("task_abb", "cmn");
    } else {
      inputMap.put("task_abb", pathSplit[startPos + 1]);
    }
    if (pathSplit.length <= startPos + 2 || "".equals(pathSplit[startPos + 2])) {
      inputMap.put("page_abb", "main");
    } else {
      inputMap.put("page_abb", pathSplit[startPos + 2]);
    }
    inputMap.put("user_num", userNum);
    return getPageAuthDao.getAuthListByPath(inputMap);
  }
  
  /**
   *  권한을 요청한다.
   *  @param authNum - 요청하고자 하는 권한 번호
   *  @param userNum - 요청자의 요청번호
   *  @throws 기타 Exceptioni
   */
  public void requestAuth(int authNum, int userNum) throws Exception {
    Map<String, Object> inputMap = new HashMap<String, Object>();
    HttpServletRequest request = null;
    inputMap.put("auth_num", authNum);
    inputMap.put("user_num", userNum);
    try {
      request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
      if (request == null || request.getSession() == null || request.getSession().getAttribute("user_num") == null) {
        inputMap.put("now_dtm", new Date());
      } else {
        inputMap.put("now_dtm", new Date(((Long)request.getSession().getAttribute("system_call_dtm")).longValue()));
      }
    } catch (Exception e) {
      inputMap.put("now_dtm", new Date());
    }
    getPageAuthDao.requestAuth(inputMap);
  }
}