package com.cmn.cmn.service;

import java.util.Map;
import java.util.List;

public interface GetDataFromURLService {
  public static final int TYPE_JSON = 1;
  public static final int TYPE_STRING = 2;
  public static final int TYPE_BYTE = 3;
  public Object getDataFromURL(String strUrl, List<Map<String, String>> listData, String method, String encodingType, int returnType) throws Exception;
}