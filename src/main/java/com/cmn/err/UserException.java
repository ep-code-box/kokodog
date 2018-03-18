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
public class UserException extends KokodogException {
  private static Logger logger = LogManager.getLogger(UserException.class);

  public UserException(int messageNum, String... msg) throws Exception {
    userException(messageNum, msg);
  }

  public UserException userException(int messageNum, String... msg) throws Exception {
    logger.debug("Start method of UserException.systemException");
    super.kokodogException(messageNum, msg);
    return this;
  }
}