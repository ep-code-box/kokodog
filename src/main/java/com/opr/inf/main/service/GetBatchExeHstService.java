package com.opr.inf.main.service;

import java.util.Map;
import java.util.List;

public interface GetBatchExeHstService {
  public List<Map<String, Object>> getBatchExeHst(long fromDatetime, long toDatetime) throws Exception;
}