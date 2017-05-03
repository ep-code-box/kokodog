package com.cmn.cmn.service;

import javax.servlet.http.HttpServletRequest;

public interface ConnLogService {
  public long insertConnLog(long systemCallDtm, int userNum, String remoteAddr, String requesrURL, String queryString, String method) throws Exception;
  public void updateConnEndLog(long systemCallDtm, long systemEndDtm, int responseNum, String errMsg, long seq, int userNum) throws Exception;
  public void updateTimeoutConnEndLog(long systemCallDtm, long systemEndDtm, long seq, int userNum) throws Exception;
}