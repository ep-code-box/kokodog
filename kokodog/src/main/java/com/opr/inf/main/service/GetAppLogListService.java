package com.opr.inf.main.service;

import java.util.Map;
import java.util.List;

public interface GetAppLogListService {
  public Map<String, Object> getAppLogList(long fromDatetime, long toDatetime, int logTypCd, String filterTxt, long startSeq, List<String> logLevelList) throws Exception;
  public long getAppLogListCnt(Map<String, Object> inputMap) throws Exception;
}