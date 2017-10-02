package com.opr.inf.main.service.impl;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.sql.Timestamp;
import java.util.Date;
import java.math.BigDecimal;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmn.err.SystemException;
import com.cmn.err.UserException;
import com.opr.inf.main.service.GetSystemInfoDataService;
import com.opr.inf.main.dao.GetSystemInfoDataDao;

@Service("getSystemInfoDataService")
public class GetSystemInfoDataServiceImpl implements GetSystemInfoDataService {
  @Autowired
  private SystemException systemException;
  
  @Autowired
  private UserException userException;
  
  @Autowired
  private GetSystemInfoDataDao getSystemInfoDataDao;
  
  private static Logger logger = LogManager.getLogger(GetSystemInfoDataServiceImpl.class);
  
  public List<Map<String, Object>> getSystemInfoData(long fromTime, long toTime, int cnt) throws Exception {
    logger.debug("============   Start method of GetSystemInfoDataServiceImpl.getSystemInfoData   ============");
    logger.debug(" Parameter - fromTime[" + fromTime + "], toTime[" + toTime + "], cnt[" + cnt + "]");
    List<Map<String, Object>> outputList = new ArrayList<Map<String, Object>>();
    Map<String, Object> memoryInfoMap = null;
    Map<String, Object> cpuInfoMap = null;
    memoryInfoMap = getMemoryInfraInfo(fromTime, toTime, cnt);
    outputList.add(memoryInfoMap);
    cpuInfoMap = getCpuInfraInfo(fromTime, toTime, cnt);
    outputList.add(cpuInfoMap);
    return outputList;
  }
  
  private Map<String, Object> getMemoryInfraInfo(long fromTime, long toTime, int cnt) throws Exception {
    logger.debug("============   Start method of GetSystemInfoDataServiceImpl.getMemoryInfraInfo   ============");
    logger.debug(" Parameter - fromTime[" + fromTime + "], toTime[" + toTime + "], cnt[" + cnt + "]");
    Map<String, Object> inputMap = new HashMap<String, Object>();
    List<Map<String, Object>> outputList = null;
    int i = 0;
    inputMap.put("from_datetime", new Date(fromTime));
    inputMap.put("to_datetime", new Date(toTime));
    inputMap.put("cnt", cnt);
    outputList = getSystemInfoDataDao.getMemoryUsed(inputMap);
    Map<String, Object> returnValue = new HashMap<String, Object>();
    List<Map<String, Object>> memoryInfoList = new ArrayList<Map<String, Object>>();
    List<Map<String, Object>> memoryInfoAPList = null;
    List<Map<String, Object>> memoryInfoContainerList = null;
    Map<String, Object> memoryInfoAPMap = null;
    Map<String, Object> memoryInfoContainerMap = null;
    Map<String, Object> memoryUsedInfoByTime = null;
    long beforeTime = 0L;
    int beforeAPNum = 0;
    for (i = 0; i < outputList.size(); i++) {
      if (memoryUsedInfoByTime == null || beforeTime != ((Timestamp)outputList.get(i).get("from_time")).getTime()) {
        if (memoryUsedInfoByTime != null) {
          memoryInfoAPMap.put("info", memoryInfoContainerList);
          memoryInfoAPList.add(memoryInfoAPMap);
          memoryUsedInfoByTime.put("info", memoryInfoAPList);
          memoryInfoList.add(memoryUsedInfoByTime);
        }
        memoryUsedInfoByTime = new HashMap<String, Object>();
        memoryUsedInfoByTime.put("from_time", ((Timestamp)outputList.get(i).get("from_time")).getTime());
        memoryUsedInfoByTime.put("to_time", ((Timestamp)outputList.get(i).get("to_time")).getTime());
        memoryUsedInfoByTime.put("mid_time", (((Timestamp)outputList.get(i).get("from_time")).getTime()
                                 + ((Timestamp)outputList.get(i).get("to_time")).getTime()) / 2L);
        beforeTime = ((Timestamp)outputList.get(i).get("from_time")).getTime();
        memoryInfoAPList = new ArrayList<Map<String, Object>>();
        memoryInfoAPMap = new HashMap<String, Object>();
        memoryInfoContainerList = new ArrayList<Map<String, Object>>();
        memoryInfoAPMap.put("ap_num", ((Integer)outputList.get(i).get("ap_num")).intValue());
        beforeAPNum = ((Integer)outputList.get(i).get("ap_num")).intValue();
      } else if (beforeAPNum != ((Integer)outputList.get(i).get("ap_num")).intValue()) {
        memoryInfoAPMap.put("info", memoryInfoContainerList);
        memoryInfoAPList.add(memoryInfoAPMap);
        memoryInfoAPMap = new HashMap<String, Object>();
        memoryInfoContainerList = new ArrayList<Map<String, Object>>();
      }
      memoryInfoContainerMap = new HashMap<String, Object>();
      memoryInfoContainerMap.put("container_num", ((Integer)outputList.get(i).get("container_num")).intValue());
      memoryInfoContainerMap.put("memory_used", ((BigDecimal)outputList.get(i).get("memory_used")).doubleValue());
      memoryInfoContainerList.add(memoryInfoContainerMap);
    }
    if (outputList.size() != 0) {
      memoryInfoAPMap.put("info", memoryInfoContainerList);
      memoryInfoAPList.add(memoryInfoAPMap);
      memoryUsedInfoByTime.put("info", memoryInfoAPList);
      memoryInfoList.add(memoryUsedInfoByTime);
    }
    returnValue.put("info_type", "memory_used");
    returnValue.put("info", memoryInfoList);
    return returnValue;
  }

  private Map<String, Object> getCpuInfraInfo(long fromTime, long toTime, int cnt) throws Exception {
    logger.debug("============   Start method of GetSystemInfoDataServiceImpl.getCpuInfraInfo   ============");
    logger.debug(" Parameter - fromTime[" + fromTime + "], toTime[" + toTime + "], cnt[" + cnt + "]");
    Map<String, Object> inputMap = new HashMap<String, Object>();
    List<Map<String, Object>> outputList = null;
    int i = 0;
    inputMap.put("from_datetime", new Date(fromTime));
    inputMap.put("to_datetime", new Date(toTime));
    inputMap.put("cnt", cnt);
    outputList = getSystemInfoDataDao.getCpuUsed(inputMap);
    Map<String, Object> returnValue = new HashMap<String, Object>();
    List<Map<String, Object>> memoryInfoList = new ArrayList<Map<String, Object>>();
    List<Map<String, Object>> memoryInfoAPList = null;
    List<Map<String, Object>> memoryInfoContainerList = null;
    Map<String, Object> memoryInfoAPMap = null;
    Map<String, Object> memoryInfoContainerMap = null;
    Map<String, Object> memoryUsedInfoByTime = null;
    long beforeTime = 0L;
    int beforeAPNum = 0;
    for (i = 0; i < outputList.size(); i++) {
      if (memoryUsedInfoByTime == null || beforeTime != ((Timestamp)outputList.get(i).get("from_time")).getTime()) {
        if (memoryUsedInfoByTime != null) {
          memoryInfoAPMap.put("info", memoryInfoContainerList);
          memoryInfoAPList.add(memoryInfoAPMap);
          memoryUsedInfoByTime.put("info", memoryInfoAPList);
          memoryInfoList.add(memoryUsedInfoByTime);
        }
        memoryUsedInfoByTime = new HashMap<String, Object>();
        memoryUsedInfoByTime.put("from_time", ((Timestamp)outputList.get(i).get("from_time")).getTime());
        memoryUsedInfoByTime.put("to_time", ((Timestamp)outputList.get(i).get("to_time")).getTime());
        memoryUsedInfoByTime.put("mid_time", (((Timestamp)outputList.get(i).get("from_time")).getTime()
                                 + ((Timestamp)outputList.get(i).get("to_time")).getTime()) / 2L);
        beforeTime = ((Timestamp)outputList.get(i).get("from_time")).getTime();
        memoryInfoAPList = new ArrayList<Map<String, Object>>();
        memoryInfoAPMap = new HashMap<String, Object>();
        memoryInfoContainerList = new ArrayList<Map<String, Object>>();
        memoryInfoAPMap.put("ap_num", ((Integer)outputList.get(i).get("ap_num")).intValue());
        beforeAPNum = ((Integer)outputList.get(i).get("ap_num")).intValue();
      } else if (beforeAPNum != ((Integer)outputList.get(i).get("ap_num")).intValue()) {
        memoryInfoAPMap.put("info", memoryInfoContainerList);
        memoryInfoAPList.add(memoryInfoAPMap);
        memoryInfoAPMap = new HashMap<String, Object>();
        memoryInfoContainerList = new ArrayList<Map<String, Object>>();
      }
      memoryInfoContainerMap = new HashMap<String, Object>();
      memoryInfoContainerMap.put("core_num", ((Integer)outputList.get(i).get("core_num")).intValue());
      memoryInfoContainerMap.put("cpu_used", ((Double)outputList.get(i).get("cpu_used")).doubleValue());
      memoryInfoContainerList.add(memoryInfoContainerMap);
    }
    if (outputList.size() != 0) {
      memoryInfoAPMap.put("info", memoryInfoContainerList);
      memoryInfoAPList.add(memoryInfoAPMap);
      memoryUsedInfoByTime.put("info", memoryInfoAPList);
      memoryInfoList.add(memoryUsedInfoByTime);
    }
    returnValue.put("info_type", "cpu_used");
    returnValue.put("info", memoryInfoList);
    return returnValue;
  }
}