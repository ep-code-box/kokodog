package com.cmn.cmn.service;

import java.util.Map;

public interface PageAuthService {
  public boolean getIsPageExist(String pgmAbb, String taskAbb, String pageAbb) throws Exception;
  public boolean getIsLoginAuth(String pgmAbb, String taskAbb, String pageAbb, int userNum) throws Exception;
  public boolean getIsMobilePageExist(String pgmAbb, String taskAbb, String pageAbb) throws Exception;
}
