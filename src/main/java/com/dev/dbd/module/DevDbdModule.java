package com.dev.dbd.module;

import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

@Service
public interface DevDbdModule {
  public void dbdDist(int queryNum, int repVer, int instance, HttpServletRequest request) throws Exception;
  public String checkSqlType(String query) throws Exception;
}