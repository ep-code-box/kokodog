package com.cmn.cmn.batch;

import java.util.Date;
import java.util.Map;
import java.util.HashMap;

import org.apache.log4j.Logger;

import org.apache.ibatis.session.SqlSession;

public abstract class Batch {
  private static Logger logger = Logger.getLogger(Batch.class);
  protected SqlSession sqlSession;
  private String report;
  private int errNum = 0;
  private int batchNum = 0;
  private long exeDtm = 0L;
  
  public Batch() {
    logger.debug("============   Start constructor of Batch(Parameter - null)   ============");
  }
  
  public void setSqlSession(SqlSession sqlSession) {
    logger.debug("============   Start method of Batch.setSqlSession   ============");
    this.sqlSession = sqlSession;
  }
  
  public abstract void run(long batchRunTime, String[] param) throws Exception;
  
  public String getReport() throws Exception {
    return this.report;
  }
  
  public void addLog(String str) throws Exception {
    Map<String, Object> inputMap = new HashMap<String, Object>();
    inputMap.put("proc_log", str);
    inputMap.put("batch_num", batchNum);
    inputMap.put("exe_dtm", new Date(exeDtm));
    sqlSession.update("com.cmn.cmn.batch.updateBatchAddLog", inputMap);
  }
  
  public int getErrNum() throws Exception {
    return this.errNum;
  }
  
  public void setBatchNum(int batchNum) {
    this.batchNum = batchNum;
  }
  
  public void setExeDtm(long exeDtm) {
    this.exeDtm = exeDtm;
  }
  
  public void setReport(String report) {
    this.report = report;
  }
}