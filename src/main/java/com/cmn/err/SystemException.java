package com.cmn.err;

import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.session.SqlSession;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import com.cmn.err.KokodogException;

@SuppressWarnings("serial")
public class SystemException extends KokodogException {
  private static Logger logger = LogManager.getLogger(SystemException.class);

  public SystemException(int messageNum, String... msg) throws Exception {
    systemException(messageNum, msg);
  }

  public SystemException systemException(int messageNum, String... msg) throws Exception {
    logger.debug("Start method of SystemException.systemException");
    ServletRequestAttributes sra = null;
    HttpServletRequest request = null;
    SqlSession sqlSession = null;
    if (sqlSession == null) {
      try {
        sra = (ServletRequestAttributes)RequestContextHolder.currentRequestAttributes();
        request = sra.getRequest();
        if (request == null || request.getSession() == null || request.getSession().getAttribute("_SQL_SESSION_") == null) {
          sqlSession = null;
        } else {
          sqlSession = (SqlSession)request.getAttribute("_SQL_SESSION_");
        }
      } catch (Exception e) {
      }
    }
    super.kokodogException(messageNum, sqlSession, msg);
    return this;
  }
}