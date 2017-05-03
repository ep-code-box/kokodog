package com.res.sto.batch;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.text.SimpleDateFormat;

import org.apache.log4j.Logger;
import org.apache.ibatis.session.SqlSession;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;

import com.cmn.cmn.batch.Batch;
import com.cmn.cmn.service.GetDataFromURLService;
import com.cmn.cmn.service.impl.GetDataFromURLServiceImpl;

public class GetStoProgressHstInfo extends Batch {  
  private static Logger logger = Logger.getLogger(GetStoProgressHstInfo.class);

  @Override
  public void run(long batchRunTime, String param) throws Exception {
    addLog("============   Start method of GetStoProgressHstInfo.run   ============");
    addLog(" Parameter - batchRunTime[" + batchRunTime + "], param[" + param + "]");
    process(batchRunTime);
  }
  
  private void process(long batchRunTime) throws Exception {
    SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
    SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
    GetDataFromURLService getDataFromURLService = new GetDataFromURLServiceImpl();
    String url = null;
    Map<String, Object> inputMap = new HashMap<String, Object>();
    Map<String, Object> outputMap = null;
    String dateStr = sdf2.format(new Date(batchRunTime));
    inputMap.put("date", sdf1.parse(dateStr));
    List<Map<String, Object>> outputList = null;
    logger.debug("input map of SQL getResStoAllCorpList - " + inputMap);
    outputList = sqlSession.selectList("getResStoAllCorpList", inputMap);
    logger.debug("input map of SQL getResStoAllCorpList - " + outputList);
    int i = 0;
    int j = 0;
    int k = 0;
    String stockNum = null;
    String data = null;
    Calendar dateCal = new GregorianCalendar();
    Document doc = null;
    Elements elements = null;
    Element element = null;
    boolean isDataInserted = false;
    String firstDateStr = "";
    int insertDataCnt = 0;
    boolean isStockInserted = false;
    int insertStockCnt = 0;
    List<Map<String, String>> array = new ArrayList<Map<String, String>>();
    for (i = 0; i < outputList.size(); i++) {
      stockNum = (String)outputList.get(i).get("stock_num");
      isDataInserted = false;
      isStockInserted = false;
      for (j = 0; isDataInserted == false; j++) {
        doc = null;
        element = null;
        elements = null;
        data = null;
        url = "http://finance.naver.com/item/sise_day.nhn?code=" + stockNum + "&page=" + (j + 1);
        data = (String)getDataFromURLService.getDataFromURL(url, array, "GET", "EUC-KR", GetDataFromURLService.TYPE_STRING);
        if (data.trim().equals("") == true) {
          break;
        }
        doc = Jsoup.parse(data);
        elements = doc.select("table.type2").first().children().first().children();
        logger.debug("table list - " + elements.first().toString());
        logger.debug("size of elements - " + elements.size());
        if (elements.size() <= 2) {
          break;
        }
        for (k = 0; k < elements.size() - 2; k++) {
          if (elements.eq(k + 2).first().children().size() < 7) {
            continue;
          }
          if (elements.eq(k + 2).first().children().first().html().equals("&nbsp;") == true) {
            isDataInserted = true;
            break;
          }
          element = elements.eq(k + 2).first().children().first().children().first();
          if (sdf1.format(dateCal.getTime()).equals(element.html().replaceAll("\\.", ""))) {
            continue;
          }
          inputMap.clear();
          inputMap.put("stock_num", stockNum);
          if (element.html().equals("") == true) {
            continue;
          }
          if (k == 0 && firstDateStr.equals(element.html().replaceAll("\\.", ""))) {
            isDataInserted = true;
            break;
          }
          inputMap.put("date", sdf1.parse(element.html().replaceAll("\\.", "")));
          logger.debug("input map of SQL getResStoPriceHstExist - " + inputMap);
          outputMap = sqlSession.selectOne("getResStoPriceHstExist", inputMap);
          logger.debug("output map of SQL getResStoPriceHstExist - " + outputMap);
          if (outputMap == null || outputMap.get("is_exist") == null || outputMap.get("is_exist").equals("Y") == true) {
            isDataInserted = true;
            break;
          }
          outputMap.clear();
          outputMap = null;
          inputMap.clear();
          inputMap.put("stock_num", stockNum);
          inputMap.put("user_num", 0);
          element = elements.eq(k + 2).first().children().first().children().first();
          inputMap.put("date", sdf1.parse(element.html().replaceAll("\\.", "")));
          element = elements.eq(k + 2).first().children().eq(1).first().children().first();
          inputMap.put("close_price", Integer.parseInt(element.html().replaceAll(",", "")));
          element = elements.eq(k + 2).first().children().eq(3).first().children().first();
          inputMap.put("open_price", Integer.parseInt(element.html().replaceAll(",", "")));
          element = elements.eq(k + 2).first().children().eq(4).first().children().first();
          inputMap.put("high_price", Integer.parseInt(element.html().replaceAll(",", "")));
          element = elements.eq(k + 2).first().children().eq(5).first().children().first();
          inputMap.put("low_price", Integer.parseInt(element.html().replaceAll(",", "")));
          element = elements.eq(k + 2).first().children().eq(6).first().children().first();
          inputMap.put("volume", Integer.parseInt(element.html().replaceAll(",", "")));
          logger.debug("input map of SQL insertResStoPriceHst - " + inputMap);
          sqlSession.insert("insertResStoPriceHst", inputMap);
          insertDataCnt++;
          isStockInserted = true;
        }
      }
      if (isStockInserted == true) {
        insertStockCnt++;
      }
    }
    setReport("==========================================\nData Insert Count : " + insertDataCnt + "\nStock Insert Count : " + insertStockCnt + "\n===========================================\n");
  }
}