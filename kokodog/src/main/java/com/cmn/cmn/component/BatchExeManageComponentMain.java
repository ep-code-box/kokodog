package com.cmn.cmn.component;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import com.cmn.cmn.component.BatchExeManageComponent;

@Component
@EnableAsync
public class BatchExeManageComponentMain {
  @Autowired
  private BatchExeManageComponent batchExeManageComponent;
  
  @PostConstruct
  public void postConstruct() {
    batchExeManageComponent.backgroundProcess();
  }
}