package com.dev.ser.module;

import java.util.Map;
import java.util.HashMap;
import java.io.InputStream;

import org.apache.ibatis.session.SqlSession;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class TestLogWriter extends Thread {
	SqlSession sqlSession;
	String testKey;
	int logType;
	boolean isClose;
	InputStream is;
	private static Logger logger = LogManager.getLogger(TestLogWriter.class);
	
	public TestLogWriter(SqlSession sqlSession, String testKey, int logType, InputStream is) {
		this.sqlSession = sqlSession;
		this.testKey = testKey;
		this.logType = logType;
		this.isClose = false;
		this.is = is;
	}
	
	public void run() {
		logger.debug("Start of addTestLogToDatabase[" + logType + "]");
		StringBuffer sb = null;
    byte[] b = new byte[4096];
		try {
			while (isClose == false) {
				boolean isDataExists = false;
				sb = new StringBuffer();
				int max = 0;
				while (is.available() != 0) {
					isDataExists = true;
					if (is.available() >= 4096) {
						max = 4096;
					} else {
						max = is.available();
					}
					is.read(b, 0, max);
					sb.append(new String(b, 0, max, "UTF-8"));
				}
				if (isDataExists == true) {
					Map<String, Object> inputMap = new HashMap<String, Object>();
					inputMap.put("test_key", testKey);
					inputMap.put("log_typ", logType);
					inputMap.put("log_msg", sb.toString());
					logger.debug("Input map of insertDevSerTestLog - " + inputMap);
					int cnt = sqlSession.insert("insertDevSerTestLog", inputMap);
					logger.debug("Insert count of insertDevSerTestLog - " + cnt);
				}
				Thread.sleep(1);
    	}
		} catch (Exception e) {
			logger.error(e.getClass().getName() + " - " + e.getMessage());
		}
		try {
			is.close();
		} catch (Exception e) {
			logger.error(e.getClass().getName() + " - " + e.getMessage());
		}
		logger.debug("Log Writer Thread ends...[" + logType + "]");
	}
	
	public void setClose() {
		this.isClose = true;
	}
}