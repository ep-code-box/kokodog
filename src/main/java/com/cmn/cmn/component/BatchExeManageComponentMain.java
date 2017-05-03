/*
 * Title : BatchExeManageComponentMain
 *
 * @Version : 1.0
 *
 * @Date : 2016-03-08
 *
 * @Copyright by 이민석
 */
package com.cmn.cmn.component;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import com.cmn.cmn.component.BatchExeManageComponent;

/**
 *  이 객체는 Spring 기동 시에 com.cmn.cmn.batch.BatchExeManageComponent class의 backgroundProcess 메서드를 비동기로
 *  호출하여 다른 업무가 실행됨과 동시에 배치잡이 관리되도록 구현되어 있다.
 *  참고
 *  {@link com.cmn.cmn.component.BatchExeMangeComponent com.cmn.cmn.component.BatchExeMangeComponent}
 */
@Component
@EnableAsync
public class BatchExeManageComponentMain {
  @Autowired
  private BatchExeManageComponent batchExeManageComponent;
  
  /**
   *  Spring 기동 시에 수행되며, 배치 관리 잡 매니저를 비동기로 수행한다.
   */
  @PostConstruct
  public void postConstruct() {
    batchExeManageComponent.backgroundProcess();
  }
}