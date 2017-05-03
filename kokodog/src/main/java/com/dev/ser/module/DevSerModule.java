package com.dev.ser.module;

import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

@Service
public interface DevSerModule {
  public void serDist(int serviceNum, int repVer, int instance, HttpServletRequest request) throws Exception;
}