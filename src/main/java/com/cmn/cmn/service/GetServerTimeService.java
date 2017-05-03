/*
 * Title : GetServerTimeService
 *
 * @Version : 1.0
 *
 * @Date : 2016-03-08
 *
 * @Copyright by 이민석
 */

package com.cmn.cmn.service;

import java.util.Map;

/**
  *  이 객체는 현재 서버 시간을 리턴을 하는 역할을 수행한다.
  */
public interface GetServerTimeService {
  public long getServerTime() throws Exception;
}