package com.skd.sel.sel_scnrio_mng.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skd.sel.sel_scnrio_mng.service.GetImportedSrcCdSvc;
import com.skd.sel.sel_scnrio_mng.dao.GetImportedSrcCdDao;
import com.skd.sel.module.service.MakeSrcCdWithInputParamSvc;

/**
 * 이 클래스는 Selenium 테스트 자동화 도구 개발 프로젝트 내에서
 * 시나리오 혹은 시나리오번호 + 케이스번호 조합에 해당하는 소스코드를 전부 되돌려주는 역할을 수행한다.
 *
 * @author  Minseok Lee
 * @since   2018.02.03
 * @version 1.0
 */
@Service("getImportedSrcCdSvc")
public class GetImportedSrcCdSvcImpl implements GetImportedSrcCdSvc {
  private static Logger logger = LogManager.getLogger(GetImportedSrcCdSvcImpl.class);
  
  @Autowired
  private GetImportedSrcCdDao getImportedSrcCdDao;
  
  @Autowired
  private MakeSrcCdWithInputParamSvc makeSrcCdWithInputParamSvc;

  /**
   * 시나리오번호 혹은 시나리오번호 및 케이스번호에 해당하는 소스코드를 되돌려준다.
   *
   * @param inputMap 아래 값을 갖는 Map으로 정의한다.
   *        scnrio_num - 시나리오번호(필수)
   *        case_num - 케이스번호(선택)
   * @return List에 아래 내용을 담긴 Map을 순차적으로 담아 소스코드를 제공한다.
   *         case_num : 케이스 번호 (Integer 타입)
   *         src_cd : 소스코드(String 타입)
   * @exception Exception 예상하지 못한 Exception으로 정의한다.
   */
  public List<Map<String, Object>> getImportedSrcCd(Map<String, Object> inputMap) throws Exception {
    Map<String, Object> outputMap = null;
    List<Map<String, Object>> outputList = null;
    List<Map<String, Object>> retList = new ArrayList<Map<String, Object>>();
    Map<String, Object> methodInputMap = new HashMap<String, Object>();
    Map<String, Object> retMap = null;
    String srcCd = null;
    String importedSrc = null;
    int[] caseNumList = null;
    if (inputMap.get("case_num") == null) {
      methodInputMap.put("scnrio_num", inputMap.get("scnrio_num"));
      outputList = getImportedSrcCdDao.getTestCaseInfoByScnrioNum(methodInputMap);
      caseNumList = new int[outputList.size()];
      for (int i = 0; i < outputList.size(); i++) {
        caseNumList[i] = ((Long)outputList.get(i).get("case_num")).intValue();
      }
    } else {
      caseNumList = new int[1];
      caseNumList[0] = ((Integer)inputMap.get("case_num")).intValue();
    }
    methodInputMap.clear();
    methodInputMap.put("scnrio_num", inputMap.get("scnrio_num"));
    srcCd = (String)getImportedSrcCdDao.getSrcCdByScnrioNum(methodInputMap).get("src_cd");
    for (int i = 0; i < caseNumList.length; i++) {
      methodInputMap.clear();
      methodInputMap.put("case_num", caseNumList[i]);
      outputList = getImportedSrcCdDao.getTestInputByCaseNum(methodInputMap);
      retMap = new HashMap<String, Object>();
      retMap.put("case_num", caseNumList[i]);
      retMap.put("src_cd", new String(makeSrcCdWithInputParamSvc.makeSrcCd(srcCd, outputList)));
      retList.add(retMap);
    }
    return retList;
  }
}