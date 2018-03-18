package com.cmn.err;

import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.session.SqlSession;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import com.cmn.err.KokodogException;

@SuppressWarnings("serial")
@Component
public class UserException extends KokodogException {
  @Autowired
  private SqlSession sqlSession;
  
  private static Logger logger = LogManager.getLogger(UserException.class);

  public UserException userException(int messageNum, String... msg) throws Exception {
    logger.debug("Start method of UserException.userException");
    super.kokodogException(messageNum, sqlSession, msg);
    return this;
  }
}