package com.cmn.err;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.LinkedList;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import com.cmn.err.SystemException;
import com.cmn.err.KokodogException;

@Component
public class UserException extends KokodogException {
  @Autowired
  private SqlSession sqlSession;
  
  private static Logger logger = Logger.getLogger(UserException.class);

  public UserException userException(int messageNum, String... msg) throws Exception {
    super.kokodogException(messageNum, sqlSession, msg);
    return this;
  }
}