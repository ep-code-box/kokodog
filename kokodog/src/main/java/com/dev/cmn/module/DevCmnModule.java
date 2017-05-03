package com.dev.cmn.module;

import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

@Service
public interface DevCmnModule {
  public void distResultInsert(int sourceNum, int repVer, int instance, int distType, HttpServletRequest request) throws Exception;
}