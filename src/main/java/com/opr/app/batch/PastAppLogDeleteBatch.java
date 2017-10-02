/*
 * Title : PastAppLogDeleteBatch
 *
 * @Version : 1.0
 *
 * @Date : 2016-03-08
 *
 * @Copyright by 이민석
 */
package com.opr.app.batch;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.HashMap;
import java.lang.Process;

import org.apache.log4j.Logger;

import com.cmn.cmn.batch.Batch;

/**
 *  이 객체는 주기적으로 로그를 삭제하면서 로그가 무한정 쌓여 용량에 문제가 생기는 것을
 *  방지해주는 역할을 수행한다.<br/>
 *  해당 전체 프로그램은 DB 내 cmn_app_log에 쌓여 있는 로그를 삭제하는 역할을 수행하며
 *  만일을 대비하여 Trace, Debug, Info 로그만 삭제할 뿐 Warn, Error, Fatal 로그는
 *  삭제하지 않는다.<br/>
 *  삭제 기간은 공통 파라미터로 DB 내에 cmn_cd_spc 및 cmn_cd_dtl 테이블에서
 *  cd_num = 32 번 항목에 저장되어 있으며
 *  이 값을 수정하면 며칠 전에 로그를 삭제할 대상인지 수정 가능하다.
 */
public class PastAppLogDeleteBatch extends Batch {
  private static Logger logger = Logger.getLogger(PastAppLogDeleteBatch.class);
  
/**
 *  배치를 수행하는 메서드이다.
 *  @param batchRunTime - 배치 수행을 요청받은 시각
 *  @param param - 파라미터 목록(이 배치는 미사용한다.)
 *  @throws 기타 익셉션
 */
  public void run(long batchRunTime, String param) throws Exception {
    addLog("============   Start method of pastAppLogDeleteBatch.run   ============");
    addLog(" Parameter - batchRunTime[" + batchRunTime + "], param[" + param + "]");
    pastAppLogDeleteBatch(batchRunTime);
    addLog("============   End method of pastAppLogDeleteBatch.run   ============");
  }
  
  private void pastAppLogDeleteBatch(long currentTime) throws Exception {
    addLog("============   Start method of pastAppLogDeleteBatch.pastAppLogDeleteBatch   ============");
    addLog(" Parameter - batchRunTime[" + currentTime + "]");
    long currentDate = 0L;
    int deleteCnt = 0;
    Map<String, Object> inputMap = new HashMap<String, Object>();
    currentDate = getDate(currentTime);
    inputMap.put("current_time", new Date(currentDate));
    deleteCnt = sqlSession.delete("com.opr.app.batch.deletePastAppLog", inputMap);
    setReport("================= Report of Batch PastAppLogDeleteBatch ==============================");
    setReport("Total Delete Count[" + deleteCnt + "]");
    addLog("============   End method of pastAppLogDeleteBatch.pastAppLogDeleteBatch   ============");
  }
  
  private long getDate(long currentTime) throws Exception {
    Calendar date = GregorianCalendar.getInstance();
    Calendar currentTimeCalendar = GregorianCalendar.getInstance();
    currentTimeCalendar.setTimeInMillis(currentTime);
    date.set(currentTimeCalendar.get(Calendar.YEAR), currentTimeCalendar.get(Calendar.MONTH), currentTimeCalendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
    return date.getTimeInMillis() / 1000L * 1000L;
  }
}