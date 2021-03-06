package com.cmn.err;

import java.lang.RuntimeException;
import java.util.Map;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.session.SqlSession;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

@SuppressWarnings("serial")
public class KokodogException extends RuntimeException {
  private static Logger logger = LogManager.getLogger(KokodogException.class);

  private int messageNum;
  private String message;
  private int errTyp;

  public KokodogException kokodogException(int messageNum, String[] msg) throws Exception {
    Map<String, Object> inputMap = new HashMap<String, Object>();
    ServletRequestAttributes sra = null;
    HttpServletRequest request = null;
    SqlSession sqlSession = null;
    Map<String, Object> outputMap = null;
    String tempMsg = null;
    int index = 0;
    int pos = 0;
    this.messageNum = messageNum;
    try {
      sra = (ServletRequestAttributes)RequestContextHolder.currentRequestAttributes();
      request = sra.getRequest();
      if (request == null || request.getAttribute("_SQL_SESSION_") == null) {
        sqlSession = null;
        tempMsg = "알 수 없는 오류로 인해 정상적인 결과를 도출하지 못했습니다.";
        this.errTyp = 500;
      } else {
        sqlSession = (SqlSession)request.getAttribute("_SQL_SESSION_");
        inputMap.put("msg_num", messageNum);
        logger.debug("Input Map of getCmnErrorMessage SQL Map - " + inputMap);
        outputMap = sqlSession.selectOne("com.cmn.cmn.getErrMessageByMessageNum", inputMap);
        logger.debug("Output Map of getCmnErrorMessage SQL Map - " + outputMap);
        tempMsg = (String)outputMap.get("msg");
        if (outputMap.get("err_typ") == null) {
          this.errTyp = 500;
        } else {
          this.errTyp = ((Integer)outputMap.get("err_typ")).intValue();
        }
      }
    } catch (Exception e) {
      sqlSession = null;
      tempMsg = "알 수 없는 오류로 인해 정상적인 결과를 도출하지 못했습니다.";
      this.errTyp = 500;
    }
    while ((pos = tempMsg.indexOf("#", index)) >= 0) {
      if (tempMsg.charAt(pos + 1) == '#') {
        tempMsg = tempMsg.substring(0, pos) + tempMsg.substring(pos + 1);
        index = pos + 1;
      } else {
        int nextSharp = tempMsg.indexOf("#", pos + 1);
        if (nextSharp < 0) {
          if (messageNum != 5 && messageNum != 6) {
            throw this.kokodogException(5, new String[0]);
          } else {
            throw new Exception();
          }
        }
        for (int i = 0; i < (nextSharp - pos - 1); i++) {
          if (tempMsg.charAt(pos + i + 1) > '9' || tempMsg.charAt(pos + i + 1) < '0') {
            if (messageNum != 5 && messageNum != 6) {
              throw this.kokodogException(5, new String[0]);
            } else {
              throw new Exception();
            }
          }
        }
        int tempMessageNum = Integer.parseInt(tempMsg.substring(pos + 1, nextSharp));
        if (tempMessageNum <= 0) {
          if (messageNum != 5 && messageNum != 6) {
            throw this.kokodogException(5, new String[0]);
          } else {
            throw new Exception();
          }
        } else if (tempMessageNum > msg.length) {
          if (messageNum != 5 && messageNum != 6) {
            String[] strErrTemp = new String[2];
            strErrTemp[0] = "" + tempMessageNum;
            strErrTemp[1] = "" + msg.length;
            throw this.kokodogException(6, strErrTemp);
          } else {
            throw new Exception();
          }
        }
        tempMsg = tempMsg.substring(0, pos) + msg[tempMessageNum - 1] + tempMsg.substring(nextSharp + 1);
        index = pos + msg[tempMessageNum - 1].length();
      }
    }
    this.message = tempMsg;
    return this;
  }
  
  public int getMessageNum() {
    return this.messageNum;
  }
  
  public String getMessage() {
    return this.message;
  }
  
  public int getErrTyp() {
    return this.errTyp;
  }
}