package com.res.sto.batch;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Date;
import java.text.SimpleDateFormat;

import org.apache.ibatis.session.SqlSession;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.cmn.cmn.batch.Batch;
import com.cmn.cmn.service.GetDataFromURLService;
import com.cmn.cmn.service.impl.GetDataFromURLServiceImpl;

public class StoInfoCollect extends Batch {
  
  private static Logger logger = LogManager.getLogger(StoInfoCollect.class);
  private String report = "";
  private String dateStr = "";
  private Calendar dateTime;
  
  @Override
  public void run(long exeDateTime, String param) throws Exception {
    logger.debug("StoInfoCollect Start");
    init(sqlSession, new Date(exeDateTime));
    if (processNeed(new Date(exeDateTime)) == false) {
      return;
    }
    process(sqlSession, new Date(exeDateTime));
  }
  
  private boolean processNeed(Date exeDateTime) throws Exception {
    int dayOfWeek = dateTime.get(Calendar.DAY_OF_WEEK);
    if (dayOfWeek == 1 || dayOfWeek == 7) {
      this.report = "배치가 수행되는 요일이 아닙니다.";
      return false;
    } else {
      return true;
    }
  }
  
  private void init(SqlSession sqlSession, Date exeDateTime) throws Exception {
    SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");
    this.sqlSession = sqlSession;
    this.dateStr = sdf2.format(exeDateTime);
    this.dateTime = new GregorianCalendar();
    this.dateTime.setTime(exeDateTime);
  }
  
  private void process(SqlSession sqlSession, Date exeDateTime) throws Exception {
    addLog("============   Start method of StoInfoCollect.process   ============");
    Map<String, Object> inputMap = new HashMap<String, Object>();
    inputMap.put("date", new SimpleDateFormat("yyyy-MM-dd").format(this.dateTime.getTime()));
    inputMap.put("datetime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(this.dateTime.getTime()));
    getStockCorpInfo();
  }
  
  private void getStockCorpInfo() throws Exception {
    addLog("============   Start method of StoInfoCollect.getStockCorpInfo   ============");
    GetDataFromURLService getDataFromURLService = new GetDataFromURLServiceImpl();
    String data = (String)getDataFromURLService.getDataFromURL("https://m.nhqv.com/codes/jcode.js", new ArrayList<Map<String, String>>(), "GET", "EUC-KR", GetDataFromURLService.TYPE_STRING);
    int index = 0;
    String stockNum = null;
    String[] stockInfo = new String[12];
    Map<String, Object> inputMap = new HashMap<String, Object>();
    Map<String, Object> outputMap = null;
    Map<String, String> stockMap = new HashMap<String, String>();
    List<Map<String, Object>> outputList = null;
    SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
    boolean isInfoChanged = false;
    boolean isNewInfo = false;
    int i = 0;
    int numberOfTotalCount = 0;
    int numberOfInsert = 0;
    int numberOfUpdate = 0;
    int numberOfDelete = 0;
    addLog("Corporation data[1, 100]" + data.substring(0, 100));
    while ((index = data.indexOf("codes[\"", index)) >= 0) {
      numberOfTotalCount++;
      logger.debug("first point of codes[ index[" + index + "]");
      stockNum = data.substring(index + 7, index + 13);
      logger.debug("stock num[" + stockNum + "]");
      index = data.indexOf("[", index + 6) + 1;
      logger.debug("next point of codes[ index[" + index + "]");
      for (i = 0; i < stockInfo.length; i++) {
        stockInfo[i] = data.substring(data.indexOf("\"", index) + 1, data.indexOf("\"", index + 1));
        logger.debug("stock info for " + stockNum + "(" + i + ") - [" + stockInfo[i] + "]");
        if (i != stockInfo.length - 1) {
          index = data.indexOf("\"", data.indexOf("\"", index + 1) + 1);
        }
        logger.debug("next point of index[" + index + "]");
      }
      stockMap.put(new String(stockNum), new String(stockInfo[0]));
      inputMap.clear();
      inputMap.put("stock_num", stockNum);
      inputMap.put("date", sdf1.parse(this.dateStr));
      logger.debug("Input map of SQL getResStoCorpInfo - " + inputMap);
      outputMap = sqlSession.selectOne("com.res.sto.batch.getResStoCorpInfo", inputMap);
      logger.debug("Output map of SQL getResStoCorpInfo - " + outputMap);
      if (outputMap == null) {
        isNewInfo = true;
      } else {
        isNewInfo = false;
      }
      isInfoChanged = false;
      if (isNewInfo == false) {
        if (((Integer)outputMap.get("stock_type")).intValue() == Integer.parseInt(stockInfo[1])) {
          for (i = 0; i < stockInfo.length - 2; i++) {
            if (((String)outputMap.get("stock_info" + (i + 1))).equals(stockInfo[i + 2]) == false) {
              logger.debug((String)outputMap.get("stock_info" + (i + 1)) + " differs from " + stockInfo[i + 2]);
              isInfoChanged = true;
              break;
            }
          }
        } else {
          logger.debug("" + outputMap.get("stock_type") + " differs from " + stockInfo[1]);
          isInfoChanged = true;
        }
      }
      if (isNewInfo == false && isInfoChanged == true) {
        inputMap.clear();
        inputMap.put("stock_num", stockNum);
        inputMap.put("user_num", 0);
        inputMap.put("date", sdf1.parse(this.dateStr));
        logger.debug("Input map of SQL updateResStoCorpInfoDelete - " + inputMap);
        sqlSession.update("com.res.sto.batch.updateResStoCorpInfoDelete", inputMap);
      }
      if (isNewInfo == true || isInfoChanged == true) {
        inputMap.clear();
        inputMap.put("stock_num", stockNum);
        inputMap.put("user_num", 0);
        inputMap.put("stock_nm", stockInfo[0]);
        inputMap.put("stock_type", stockInfo[1]);
        for (i = 0; i < 10; i++) {
          inputMap.put("stock_info" + (i + 1), stockInfo[i + 2]);
        }
        inputMap.put("eff_sta_dt", sdf1.parse(this.dateStr));
        logger.debug("Input map of SQL insertResStoNewStockCorpInfo - " + inputMap);
        sqlSession.insert("com.res.sto.batch.insertResStoNewStockCorpInfo", inputMap);
      }
      if (isNewInfo == true) {
        numberOfInsert++;
      }
      if (isInfoChanged == true) {
        numberOfUpdate++;
      }
    }
    inputMap.clear();
    inputMap.put("date", sdf1.parse(this.dateStr));
    logger.debug("Input map of SQL getResStoAllCorpList - " + inputMap);
    outputList = sqlSession.selectList("com.res.sto.batch.getResStoAllCorpList", inputMap);
    logger.debug("Output list of SQL getResStoAllCorpList - " + outputList);
    inputMap.clear();
    for (i = 0; i < outputList.size(); i++) {
      stockNum = (String)outputList.get(i).get("stock_num");
      if (stockMap.get(stockNum) == null) {
        inputMap.clear();
        inputMap.put("stock_num", stockNum);
        inputMap.put("date", sdf1.parse(this.dateStr));
        inputMap.put("user_num", 0);
        logger.debug("Input map of SQL updateResStoCorpInfoDelete - " + inputMap);
        outputList = sqlSession.selectList("com.res.sto.batch.updateResStoCorpInfoDelete", inputMap);
        numberOfDelete++;
      }
    }
    report += "=======================================\n";
    report += "Stock Info Data Process Finished\n";
    report += "Total : " + numberOfTotalCount + "\n";
    report += "Insert : " + numberOfInsert + "\n";
    report += "Update : " + numberOfUpdate + "\n";
    report += "Delete : " + numberOfDelete + "\n";
    report += "=======================================\n";
    setReport(report);
  }
}