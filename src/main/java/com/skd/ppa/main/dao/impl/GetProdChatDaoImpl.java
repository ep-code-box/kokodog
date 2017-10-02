/*
 * Title : GetProdChatDaoImpl
 *
 * @Version : 1.0
 *
 * @Date : 2017-08-15
 *
 * @Copyright by 이민석
 */
package com.skd.ppa.main.dao.impl;

import java.util.List;
import java.util.Map;
import java.sql.SQLException;

import org.apache.ibatis.session.SqlSession;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.skd.ppa.main.dao.GetProdChatDao;

/**
 *  이 객체는 GetProdChatService 서비스에서 수행되는 DBIO 리스트로 정의된다.
 *  두 가지 역할을 수행하는데, 첫째는 현재 사용자가 수행하고 있는 채팅의 대화 내용 ID 및
 *  어디까지 대화가 진행되고 있는지를 되돌려주며<br/>
 *  두 번째는 현재 대화 결과를 DB에 저장해주는 역할을 수행한다.
 */
@Repository("getProdChatDao")
public class GetProdChatDaoImpl implements GetProdChatDao {
  @Autowired
  private SqlSession sqlSession;

  private static Logger logger = LogManager.getLogger(GetProdChatDaoImpl.class);
  /**
   *  이 메서드는 사용자가 현재 대화를 어디까지 진행하고 있는지를 알려준다.
   *  @param inputMap - DB 조회를 위한 인풋 데이터(필수로 conv_num 과 user_num이 포함되어야 한다.)
   *  @return DB 조회 결과. 데이터에는 Chatting ID와, 현재까지 대화를 진행한 번호를 되돌려준다.
   *  @throws DB 조회 시에 오류를 리턴해준다.
   */
  public Map<String, Object> getChatIdAndSeq(Map<String, Object> inputMap) throws SQLException {
    logger.debug("============   Start method of GetProdChatDaoImpl.getChatIdAndSeq   ============");
    return sqlSession.selectOne("com.skd.ppa.main.getChatIdAndSeq", inputMap);
  }

  /**
   *  이 메서드는 사용자가 대화를 저장한다.
   *  @param inputMap - DB 조회를 위한 인풋 데이터(필수로 conv_num 과 user_num, dialog_counter, text, 현재시각이 포함되어야 한다.)
   *  @return DB가 수정된 행의 전체 갯수를 되돌려준다.
   *  @throws DB 조회 시에 오류를 리턴해준다.
   */
  public int insertChatSeq(Map<String, Object> inputMap) throws SQLException {
    logger.debug("============   Start method of GetProdChatDaoImpl.insertChatSeq   ============");
    logger.debug(" Parameter - inputMap[" + inputMap + "]");
    return sqlSession.insert("com.skd.ppa.main.insertChatSeq", inputMap);
  }

  /**
   *  이 메서드는 사용자가 초기 대화를 시작할 때 대화 명세 테이블에 데이터를 적재한다.
   *  @param inputMap - DB 조회를 위한 인풋 데이터(필수로 conv_num 과 user_num, 현재시각이 포함되어야 한다.)
   *  @return DB가 수정된 행의 전체 갯수를 되돌려준다.
   *  @throws DB 조회 시에 오류를 리턴해준다.
   */
  public int insertConvSpc(Map<String, Object> inputMap) throws SQLException {
    logger.debug("============   Start method of GetProdChatDaoImpl.insertConvSpc   ============");
    logger.debug(" Parameter - inputMap[" + inputMap + "]");
    return sqlSession.insert("com.skd.ppa.main.insertConvSpc", inputMap);
  }

  /**
   *  이 메서드는 사용자가 초기 대화를 시작하고 나서 최종 Conversation ID가 결정되면 이 결과를 업데이트한다.
   *  @param inputMap - DB 조회를 위한 인풋 데이터(필수로 conv_num 과 user_num, 현재시각이 포함되어야 한다.)
   *  @return DB가 수정된 행의 전체 갯수를 되돌려준다.
   *  @throws DB 조회 시에 오류를 리턴해준다.
   */
  public int updateConvSpcConvId(Map<String, Object> inputMap) throws SQLException {
    logger.debug("============   Start method of GetProdChatDaoImpl.updateConvSpcConvId   ============");
    logger.debug(" Parameter - inputMap[" + inputMap + "]");
    return sqlSession.update("com.skd.ppa.main.updateConvSpcConvId", inputMap);    
  }

  /**
   *  이 메서드는 기존의 대화 명세에 쌓여있던 것을 refresh 하여 모두 삭제하는 작업을 진행한다.
   *  @param inputMap - DB 조회를 위한 인풋 데이터(필수로 user_num, 현재시각이 포함되어야 한다.)
   *  @return DB가 수정된 행의 전체 갯수를 되돌려준다.
   *  @throws DB 조회 시에 오류를 리턴해준다.
   */
  public int updateConvSpcPastEndDataUpdateToNow(Map<String, Object> inputMap) throws SQLException {
    logger.debug("============   Start method of GetProdChatDaoImpl.updateConvSpcPastEndDataUpdateToNow   ============");
    logger.debug(" Parameter - inputMap[" + inputMap + "]");
    return sqlSession.update("com.skd.ppa.main.updateConvSpcPastEndDataUpdateToNow", inputMap);
  }

  /**
   *  이 메서드는 기존의 대화 명세에 쌓여 있는 전체 데이터 중(만료된 것 포함) 가장 Max 값을 가져와 해당 사용자의
   *  데이터 중 conv_id가 다음에 어떤 값이 되어야 하는지 결정하는 기본 데이터로 제공한다.
   *  @param inputMap - DB 조회를 위한 인풋 데이터(필수로 user_num, 현재시각이 포함되어야 한다.)
   *  @return DB가 수정된 행의 전체 갯수를 되돌려준다.
   *  @throws DB 조회 시에 오류를 리턴해준다.
   */
  public Map<String, Object> getMaxConvNum(Map<String, Object> inputMap) throws SQLException {
    logger.debug("============   Start method of GetProdChatDaoImpl.getMaxConvNum   ============");
    logger.debug(" Parameter - inputMap[" + inputMap + "]");
    Map<String, Object> outputMap = sqlSession.selectOne("com.skd.ppa.main.getMaxConvNum", inputMap);
    logger.debug(" Parameter - outputMap[" + outputMap + "]");
    return outputMap;
  }
  
  /**
   *  이 메서드는 기존의 대화 명세에 쌓여 있는 전체 데이터 중 최근 30개의 대화 목록 데이터를 제공한다.
   *  @param inputMap - DB 조회를 위한 인풋 데이터(필수로 user_num, 현재시각이 포함되어야 한다.)
   *  @return DB가 수정된 행의 전체 갯수를 되돌려준다.
   *  @throws DB 조회 시에 오류를 리턴해준다.
   */
  public List<Map<String, Object>> getInitProdChat(Map<String, Object> inputMap) throws SQLException {
    logger.debug("============   Start method of GetProdChatDaoImpl.getInitProdChat   ============");
    logger.debug(" Parameter - inputMap[" + inputMap + "]");
    List<Map<String, Object>> outputList = sqlSession.selectList("com.skd.ppa.main.getInitProdChat", inputMap);
    logger.debug(" Parameter - outputList[" + outputList + "]");
    return outputList;    
  }
}