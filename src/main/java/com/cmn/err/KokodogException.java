package com.cmn.err;

import java.lang.RuntimeException;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.LinkedList;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

public class KokodogException extends RuntimeException {
  private static Logger logger = Logger.getLogger(KokodogException.class);

  private int messageNum;
  private String message;
  private int errTyp;
  
  public KokodogException kokodogException(int messageNum, SqlSession sqlSession, String[] msg) throws Exception {
    Map<String, Object> inputMap = new HashMap<String, Object>();
    inputMap.put("msg_num", messageNum);
    logger.debug("Input Map of getCmnErrorMessage SQL Map - " + inputMap);
    Map<String, Object> outputMap = sqlSession.selectOne("com.cmn.cmn.getErrMessageByMessageNum", inputMap);
    logger.debug("Output Map of getCmnErrorMessage SQL Map - " + outputMap);
    String tempMsg = (String)outputMap.get("msg");
    if (outputMap.get("err_typ") == null) {
      this.errTyp = 500;
    } else {
      this.errTyp = ((Integer)outputMap.get("err_typ")).intValue();
    }
    int index = 0;
    int pos = 0;
    logger.debug("Find ## character from message");
    while ((pos = tempMsg.indexOf("#", index)) >= 0) {
      if (tempMsg.charAt(pos + 1) == '#') {
        tempMsg = tempMsg.substring(0, pos) + tempMsg.substring(pos + 1);
        index = pos + 1;
      } else {
        int nextSharp = tempMsg.indexOf("#", pos + 1);
        if (nextSharp < 0) {
          if (messageNum != 5 && messageNum != 6) {
            throw this.kokodogException(5, sqlSession, new String[0]);
          } else {
            throw new Exception();
          }
        }
        for (int i = 0; i < (nextSharp - pos - 1); i++) {
          if (tempMsg.charAt(pos + i + 1) > '9' || tempMsg.charAt(pos + i + 1) < '0') {
            if (messageNum != 5 && messageNum != 6) {
              throw this.kokodogException(5, sqlSession, new String[0]);
            } else {
              throw new Exception();
            }
          }
        }
        int tempMessageNum = Integer.parseInt(tempMsg.substring(pos + 1, nextSharp));
        if (tempMessageNum <= 0) {
          if (messageNum != 5 && messageNum != 6) {
            throw this.kokodogException(5, sqlSession, new String[0]);
          } else {
            throw new Exception();
          }
        } else if (tempMessageNum > msg.length) {
          if (messageNum != 5 && messageNum != 6) {
            String[] strErrTemp = new String[2];
            strErrTemp[0] = "" + tempMessageNum;
            strErrTemp[1] = "" + msg.length;
            throw this.kokodogException(6, sqlSession, strErrTemp);
          } else {
            throw new Exception();
          }
        }
        tempMsg = tempMsg.substring(0, pos) + msg[tempMessageNum - 1] + tempMsg.substring(nextSharp + 1);
        index = pos + msg[tempMessageNum - 1].length();
      }
    }
    this.messageNum = messageNum;
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