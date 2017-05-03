package com.opr.inf.main.service;

import java.util.List;
import java.util.Map;

public interface GetSystemInfoDataService {
  public List<Map<String, Object>> getSystemInfoData(long fromTime, long toTime, int cnt) throws Exception;
}