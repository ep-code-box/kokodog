package com.dev.dbd.module;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.OutputKeys;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import org.apache.ibatis.session.SqlSession;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.cmn.err.UserException;
import com.dev.dbd.module.DevDbdModule;
import com.dev.cmn.module.DevCmnModule;

@Service("devDbdModule")
public class DevDbdModuleImpl implements DevDbdModule {
  @Autowired
  private SqlSession sqlSession;
  
  @Autowired
  private DevCmnModule devCmnModule;
  
  private static Logger logger = LogManager.getLogger(DevDbdModuleImpl.class);
  
  public void dbdDist(int queryNum, int repVer, int instance, HttpServletRequest request) throws Exception {
    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    DocumentBuilder parser = dbf.newDocumentBuilder();
    Map<String, Object> inputMap = new HashMap<String, Object>();
    inputMap.put("cd_num", 16);
    logger.debug("Input map of SQL getCmnCdList - " + inputMap);
    List<Map<String, Object>> outputList = sqlSession.selectList("getCmnCdList", inputMap);
    logger.debug("Output list of SQL getCmnCdList - " + outputList);
    String url = new String((String)(outputList.get(0).get("cd_seq_name"))) + "/WEB-INF/SqlMapper/sqlMapper.xml";
    inputMap.clear();
    inputMap.put("query_num", queryNum);
    inputMap.put("dist_instance", instance);
    logger.debug("Input map of SQL getDevDbdLastDistQueryName - " + inputMap);
    Map<String, Object> outputMap = sqlSession.selectOne("getDevDbdLastDistQueryName", inputMap);
    logger.debug("Output map of SQL getDevDbdLastDistQueryName - " + outputMap);
    XPath xpath = XPathFactory.newInstance().newXPath();
    Document xmlDoc = null;
    Element root = null;
    try {
      xmlDoc = parser.parse(url);
    } catch (FileNotFoundException e) {
      xmlDoc = parser.newDocument();
      root = xmlDoc.createElement("mapper");
      root.setAttribute("namespace", "SqlMapper");
      xmlDoc.appendChild(root);
    }
    if (root == null) {
      root = xmlDoc.getDocumentElement();
    }
    if (outputMap != null && outputMap.get("query_name") != null) {
      Node node = (Node)xpath.evaluate("//*[@id='" + outputMap.get("query_name") + "']", xmlDoc, XPathConstants.NODE);
      root.removeChild(node);
    }
    inputMap.clear();
    inputMap.put("query_num", queryNum);
    if (repVer == 0) {
      inputMap.put("rep_ver", null);
    } else {
      inputMap.put("rep_ver", repVer);
    }
    logger.debug("Input map of SQL getDevDbdDBDistInfo - " + inputMap);
    outputMap = sqlSession.selectOne("getDevDbdDBDistInfo", inputMap);
    logger.debug("Output map of SQL getDevDbdDBDistInfo - " + outputMap);
    String strSqlType = checkSqlType((String)outputMap.get("query"));
    Element newQuery = xmlDoc.createElement(strSqlType);
    newQuery.setAttribute("id", (String)outputMap.get("query_name"));
    newQuery.setAttribute("parameterType", "HashMap");
    if (strSqlType.equals("select") == true) {
      newQuery.setAttribute("resultType", "HashMap");
    }
    newQuery.appendChild(xmlDoc.createTextNode((String)outputMap.get("query")));
    File file = new File(url);
    if (file.exists() == true) {
      File fileToMove = new File(url + ".bak");
      file.renameTo(fileToMove);
    }
    try {
      root.appendChild(newQuery);
      DOMImplementation domImpl = xmlDoc.getImplementation();
      DocumentType docType = domImpl.createDocumentType("mapper", "-//mybatis.org//DTD Mapper 3.0//EN", "http://mybatis.org/dtd/mybatis-3-mapper.dtd");
      Transformer transformer = TransformerFactory.newInstance().newTransformer();
      transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, docType.getPublicId());
      transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, docType.getSystemId());
      DOMSource xmlDOM = new DOMSource(xmlDoc);
      StreamResult xmlFile = new StreamResult(new File(url));
      transformer.transform(xmlDOM, xmlFile);
      devCmnModule.distResultInsert(queryNum, ((Long)outputMap.get("rep_ver")).intValue(), instance, 1, request);
    } catch (Exception e) {
      file = new File(url + ".bak");
      if (file.exists() == true) {
        File fileToMove = new File(url);
        file.renameTo(fileToMove);
      } else {
        file.delete();        
      }
      throw e;
    }
    file = new File(url + ".bak");
    if (file.exists() == true) {
      file.delete();
    }
  }
  
  /**
    *  주어진 Query의 타입 리턴
    *  @param query - 파악하고자 하는 쿼리   
    *  @return - 결과(select, update, insert, delete  
    */
  public String checkSqlType(String query) throws Exception {
    int pos = 0;
    String strQueryLower = new String(query.toLowerCase());
    String[] tempStr = new String[4];
    char[] tempBrace1 = new char[4];
    char[] tempBrace2 = new char[4];
    int[] tempPos = new int[tempStr.length + tempBrace1.length];
    for (int i = 0; i < tempPos.length; i++) {
      tempPos[i] = 0;
    }
    tempStr[0] = "insert";
    tempStr[1] = "select";
    tempStr[2] = "update";
    tempStr[3] = "delete";
    tempBrace1[0] = '[';
    tempBrace1[1] = '(';
    tempBrace1[2] = '{';
    tempBrace1[3] = '\'';
    tempBrace2[0] = ']';
    tempBrace2[1] = ')';
    tempBrace2[2] = '}';
    tempBrace2[3] = '\'';
    while (true) {
      for (int i = 0; i < tempStr.length; i++) {
        tempPos[i] = strQueryLower.indexOf(tempStr[i], pos);
        logger.debug("tempPos Value(" + i + ")[" + tempPos[i] + "]");
      }
      for (int i = 0; i < tempBrace1.length; i++) {
        tempPos[tempStr.length + i] = strQueryLower.indexOf(tempBrace1[i], pos);
        logger.debug("tempPos Value(" + (i + tempStr.length) + ")[" + tempPos[tempStr.length + i] + "]");
      }
      int min = 0;
      for (int i = 1; i < tempPos.length; i++) {
        if (tempPos[min] < 0) {
          min = i;
        } else if (tempPos[i] >= 0 && tempPos[min] > tempPos[i]) {
          min = i;
        }
      }
      logger.debug("tempPos Min : " + min);
      if (tempPos[min] < 0) {
        throw new UserException(11, "쿼리");        
      }
      if (min < tempStr.length) {
        return tempStr[min];
      } else {
        pos = strQueryLower.indexOf(tempBrace2[min - tempStr.length], tempPos[min] + 1) + 1;
        if (pos < 0) {
          throw new UserException(11, "쿼리");
        }
        continue;
      }
    }
  }
}