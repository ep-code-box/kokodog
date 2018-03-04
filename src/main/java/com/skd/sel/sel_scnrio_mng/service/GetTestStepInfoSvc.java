package com.skd.sel.sel_scnrio_mng.service;

import java.util.List;
import java.util.Map;

public interface GetTestStepInfoSvc {
  public List<Map<String, Object>> getTestStepInfo(Map<String, Object> inputMap) throws Exception;
}