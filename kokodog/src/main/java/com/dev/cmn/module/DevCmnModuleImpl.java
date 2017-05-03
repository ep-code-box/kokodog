package com.dev.cmn.module;

import java.util.Map;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import com.dev.cmn.module.DevCmnModule;

@Service("devCmnModule")
public class DevCmnModuleImpl implements DevCmnModule {
  @Autowired
  private SqlSession sqlSession;
  
  private static Logger logger = Logger.getLogger(DevCmnModuleImpl.class);
  
  public void distResultInsert(int sourceNum, int repVer, int instance, int distType, HttpServletRequest request) throws Exception {
    Map<String, Object> inputMap = new HashMap<String, Object>();
    inputMap = new HashMap<String, Object>();
    inputMap.put("source_num", sourceNum);
    inputMap.put("rep_ver", repVer);
    inputMap.put("dist_instance", instance);
    inputMap.put("dist_typ", distType);
    inputMap.put("user_num", request.getSession().getAttribute("user_num"));
    inputMap.put("now_dtm", request.getAttribute("now_dtm"));
    logger.debug("Input map of SQL insertDevCmnDistHist - " + inputMap);
    sqlSession.insert("insertDevCmnDistHist", inputMap);
  }
}