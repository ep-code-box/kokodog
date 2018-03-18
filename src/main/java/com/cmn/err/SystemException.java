package com.cmn.err;

import org.apache.ibatis.session.SqlSession;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cmn.err.KokodogException;

@SuppressWarnings("serial")
public class SystemException extends KokodogException {
  private static Logger logger = LogManager.getLogger(SystemException.class);

  public SystemException(int messageNum, String... msg) throws Exception {
    systemException(messageNum, msg);
  }

  public SystemException systemException(int messageNum, String... msg) throws Exception {
    logger.debug("Start method of SystemException.systemException");
    super.kokodogException(messageNum, msg);
    return this;
  }
}