package com.dev.ser.module;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.BufferedWriter;
import java.io.BufferedReader;

import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.nio.channels.FileChannel;
import java.lang.Process;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import net.sf.json.JSONArray;
import net.sf.json.JSONSerializer;
import net.sf.json.JSONException;

public class TestProcessThread extends Thread {
	private SqlSession sqlSession;
  private String testKey;
	private int userNum;
  private int serviceNum;
  private int repVer;
	private String param;
	private static Logger logger = Logger.getLogger(TestProcessThread.class);
  
  public TestProcessThread(SqlSession sqlSession, int userNum, String testKey, String param) {
		super();
		setSqlSession(sqlSession);
		setTestKey(testKey);
		setParam(param);
	}
  
  public void setSqlSession(SqlSession sqlSession) {
    this.sqlSession = sqlSession;
  }
  
	public void setUserNum(int userNum) {
		this.userNum = userNum;
	}
  
  public void setTestKey(String testKey) {
    this.testKey = new String(testKey);
  }
	
	public void setParam(String param) {
		this.param = new String(param);
	}
  
  public void run() {
		logger.debug("TestProcessThread Start");
		Map<String, Object> inputMap = new HashMap<String, Object>();
		Map<String, Object> outputMap = null;
		List<Map<String, Object>> outputList = null;
		int errCd = 1;
    try {
	    inputMap.put("cd_num", 16);
    	outputList = sqlSession.selectList("getCmnCdList", inputMap);
    	String devDir = (String)outputList.get(0).get("cd_seq_name");
			inputMap.clear();
			inputMap.put("test_key", testKey);
			logger.debug("Input map of SQL getDevSerTestServiceNumAndRepVer - " + inputMap);
			outputMap = sqlSession.selectOne("getDevSerTestServiceNumAndRepVer", inputMap);
			logger.debug("Output map of SQL getDevSerTestServiceNumAndRepVer - " + outputMap);
			int serviceNum = ((Integer)outputMap.get("service_num")).intValue();
			int repVer = ((Integer)outputMap.get("rep_ver")).intValue();
      allDevFileMakeWithoutTestFile("webapps/ROOT/WEB-INF", testKey, serviceNum);
			makeTestFile("webapps/ROOT/WEB-INF/test/" + testKey, serviceNum, repVer);
			makeLibraryFile(devDir + "/WEB-INF/lib", "webapps/ROOT/WEB-INF/test/" + testKey);
			makeTestServiceTestFile("webapps/ROOT/WEB-INF/test/" + testKey, serviceNum, repVer, param);
			sqlMapperCopy(devDir + "/WEB-INF/SqlMapper", "webapps/ROOT/WEB-INF/test/" + testKey);
			confFileCopy(devDir + "/WEB-INF/conf", "webapps/ROOT/WEB-INF/test/" + testKey);
			inputMap.clear();
			inputMap.put("test_process_cd", 2);
			inputMap.put("test_success_yn", null);
			inputMap.put("test_err_cd", null);
			inputMap.put("user_num", this.userNum);
			inputMap.put("test_key", this.testKey);
			logger.debug("Input map of SQL updateDevSerTestLogHst - " + inputMap);
			sqlSession.update("updateDevSerTestLogHst", inputMap);
			errCd = 2;
			compileTestFile("webapps/ROOT/WEB-INF/test/" + testKey);
			inputMap.clear();
			inputMap.put("test_process_cd", 3);
			inputMap.put("test_success_yn", null);
			inputMap.put("test_err_cd", null);
			inputMap.put("user_num", this.userNum);
			inputMap.put("test_key", this.testKey);
			logger.debug("Input map of SQL updateDevSerTestLogHst - " + inputMap);
			sqlSession.update("updateDevSerTestLogHst", inputMap);
			errCd = 3;
			test("webapps/ROOT/WEB-INF/test/" + testKey);
			inputMap.clear();
			inputMap.put("test_process_cd", 4);
			inputMap.put("test_success_yn", "Y");
			inputMap.put("test_err_cd", null);
			inputMap.put("user_num", this.userNum);
			inputMap.put("test_key", this.testKey);
			logger.debug("Input map of SQL updateDevSerTestLogHst - " + inputMap);
			sqlSession.update("updateDevSerTestLogHst", inputMap);
    } catch (Exception e) {
			logger.error(e.getClass().getName() + " - " + e.getMessage());
			inputMap.clear();
			inputMap.put("test_process_cd", 5);
			inputMap.put("test_success_yn", "N");
			inputMap.put("test_err_cd", errCd);
			inputMap.put("user_num", this.userNum);
			inputMap.put("test_key", this.testKey);
			logger.debug("Input map of SQL updateDevSerTestLogHst - " + inputMap);
			sqlSession.update("updateDevSerTestLogHst", inputMap);
			writeErrorLog(e);
    } finally {
			try {
      	allDevTestFileDelete("webapps/ROOT/WEB-INF", testKey);
			} catch (Exception e) {
				// Not to have...
			}
    }
  }
  
  private void allDevFileMakeWithoutTestFile(String devDirStr, String testKey, int serviceNum) throws Exception {
		logger.debug("allDevFileMakeWithoutTestFile method start");
    List<Map<String, Object>> outputList = null;
		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put("service_num", serviceNum);
		logger.debug("Input map of SQL getDevSerAllDevSourceWithoutTest - " + inputMap);
    outputList = sqlSession.selectList("getDevSerAllDevSourceWithoutTest", inputMap);
		logger.debug("Output list of SQL getDevSerAllDevSourceWithoutTest - " + outputList);
    File devDir = new File(devDirStr + "/test");
    if (devDir.exists() == false) {
      devDir.mkdirs(); 
    }
    File testDir = new File(devDirStr + "/test/" + testKey);
    testDir.mkdirs();
    BufferedWriter serviceSourceFile = null;
    for (int i = 0; i < outputList.size(); i++) {
      serviceSourceFile = new BufferedWriter(new FileWriter(devDirStr + "/test/" + testKey + "/" + (String)outputList.get(i).get("service_name") + ".java"));
			serviceSourceFile.write((String)outputList.get(i).get("source"));
			serviceSourceFile.close();
    }
  }
	
	private void makeTestFile(String devDirStr, int serviceNum, int repVer) throws Exception {
		Map<String, Object> inputMap = new HashMap<String, Object>();
		Map<String, Object> outputMap = null;
		inputMap.put("service_num", serviceNum);
		if (repVer == 0) {
			inputMap.put("rep_ver", null);
		} else {
			inputMap.put("rep_ver", repVer);
		}
		logger.debug("Input map of SQL getDevSerServiceSource - " + inputMap);
		outputMap = sqlSession.selectOne("getDevSerServiceSource", inputMap);
		logger.debug("Output map of SQL getDevSerServiceSource - " + outputMap);
		BufferedWriter serviceSourceFile = new BufferedWriter(new FileWriter(devDirStr + "/" + outputMap.get("service_name") + ".java"));
		serviceSourceFile.write((String)outputMap.get("source"));
		serviceSourceFile.close();
	}
	
	private void makeLibraryFile(String libObjFileDir, String libFileDir) throws Exception {
		File objDir = new File(libObjFileDir);
		File[] objLibFiles = objDir.listFiles();
		File webInfPath = new File(libFileDir + "/WEB-INF");
		if (webInfPath.exists() == false) {
			webInfPath.mkdirs();
		}
		File libPath = new File(libFileDir + "/WEB-INF/lib");
		if (libPath.exists() == false) {
			libPath.mkdirs();
		}
		for (int i = 0; i < objLibFiles.length; i++) {
			fileCopy(objLibFiles[i], new File(libFileDir + "/WEB-INF/lib/" + objLibFiles[i].getName()));
		}
	}
	
	private void compileTestFile(String devDirStr) throws Exception {
		File[] objJavaFiles = (new File(devDirStr)).listFiles();
		int javaFileCount = 0;
		for (int i = 0; i < objJavaFiles.length; i++) {
			if (objJavaFiles[i].getName().length() >= 5 && objJavaFiles[i].getName().substring(objJavaFiles[i].getName().length() - 5).equals(".java") == true) {
				javaFileCount++;
			}
		}
		String[] cmdStr = new String[6 + javaFileCount];
		cmdStr[0] = "javac";
		cmdStr[1] = "-deprecation";
		cmdStr[2] = "-classpath";
		File libPath = new File(devDirStr + "/WEB-INF/lib");
		File[] libFiles = libPath.listFiles();
		String libFilesStr = "";
		for (int i = 0; i < libFiles.length; i++) {
			libFilesStr = libFilesStr + libFiles[i].getParent() + "/" + libFiles[i].getName() + ":"; 
		}
		cmdStr[3] = libFilesStr;
		cmdStr[4] = "-d";
		cmdStr[5] = devDirStr + "/WEB-INF/classes";
		String objCompileFile = "";
		int strPos = 6;
		for (int i = 0; i < objJavaFiles.length; i++) {
			if (objJavaFiles[i].getName().length() >= 5 && objJavaFiles[i].getName().substring(objJavaFiles[i].getName().length() - 5).equals(".java") == true) {
				cmdStr[strPos] = devDirStr + "/" + objJavaFiles[i].getName();
				strPos++;
			}
		}
		String logging = "";
		for (int i = 0; i < cmdStr.length; i++) {
			logging = logging + cmdStr[i] + " ";
		}
		File classFile = new File(devDirStr + "/WEB-INF/classes");
		if (classFile.exists() == false) {
			classFile.mkdir();
		}
		ProcessBuilder pb = new ProcessBuilder(cmdStr);
		Process oProcess = pb.start();
		TestLogWriter tlwForGeneral = new TestLogWriter(sqlSession, testKey, 1, oProcess.getInputStream());
		TestLogWriter tlwForError = new TestLogWriter(sqlSession, testKey, 2, oProcess.getErrorStream());
		tlwForGeneral.start();
		tlwForError.start();
		oProcess.waitFor();
		Thread.sleep(100);
		tlwForGeneral.setClose();
		tlwForError.setClose();
		File[] classFileMade = classFile.listFiles();
		if (classFileMade.length == 0) {
			throw new Exception();
		}
	}
	
	private void allDevTestFileDelete(String devDirStr, String testKey) throws Exception {
		deleteDirectory(new File(devDirStr + "/test/" + testKey));
		File file = new File(devDirStr + "/test");
		if (file.listFiles().length == 0) {
			file.delete();
		}
	}
	
	private boolean deleteDirectory(File path) throws Exception {
		if (path.exists() == false) {
			return false;
		}
		File[] files = path.listFiles();
		for (int i = 0; i < files.length; i++) {
			if (files[i].isDirectory() == true) {
				deleteDirectory(files[i]);
			} else {
				files[i].delete();
			}
		}
		return path.delete();
  }
	
	private void fileCopy(File orgFilePath, File newFilePath) throws Exception {
		FileInputStream inputStream = new FileInputStream(orgFilePath);
		FileOutputStream outputStream = new FileOutputStream(newFilePath); 
		FileChannel fcin =  inputStream.getChannel();
		FileChannel fcout = outputStream.getChannel(); 
		long size = fcin.size();
		fcin.transferTo(0, size, fcout);
		fcout.close();
		fcin.close(); 
		outputStream.close();
		inputStream.close();
	}
	
  @SuppressWarnings("unchecked")
	private void makeTestServiceTestFile(String devDirStr, int serviceNum, int repVer, String parameters) throws Exception {
		logger.debug("Start of makeTestServiceTestFile method");
		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put("service_num", serviceNum);
		if (repVer == 0) {
			inputMap.put("rep_ver", null);
		} else {
			inputMap.put("rep_ver", repVer);
		}
		Map<String, Object> outputMap = sqlSession.selectOne("getDevSerServiceSource", inputMap);
		String source = (String)outputMap.get("source");
		String packageName = getPackageNameBySource(source);
		String serviceName = (String)outputMap.get("service_name");
		inputMap.clear();
		inputMap.put("cd_num", 18);
		outputMap = (Map<String, Object>)(sqlSession.selectList("getCmnCdList", inputMap).get(0));
		String testSource = (String)outputMap.get("cd_seq_name");
		testSource = testSource.replaceAll("#1#", packageName + "." + serviceName);
		testSource = testSource.replaceAll("#2#", serviceName);
		testSource = testSource.replaceAll("#3#", devDirStr);
		String sourceParameterAdd = "";
		if (parameters != null) {
			JSONArray parameterJsonArray = (JSONArray)JSONSerializer.toJSON(parameters);
			for (int i = 0; i < parameterJsonArray.size(); i++) {
				sourceParameterAdd = sourceParameterAdd + "request.setParameter(\"" + parameterJsonArray.getJSONObject(i).get("key") + "\", \"" + parameterJsonArray.getJSONObject(i).get("value") + "\");";
			}
		}
		testSource = testSource.replaceAll("#4#", sourceParameterAdd);
		BufferedWriter serviceSourceFile = new BufferedWriter(new FileWriter(devDirStr + "/TestServiceTest.java"));
		serviceSourceFile.write(testSource);
		serviceSourceFile.close();
	}
	
	private String getPackageNameBySource(String source) throws Exception {
		int pos1 = 0;
		int pos2 = 0;
		int pos3 = 0;
		int curPos = 0;
		while (true) {
			pos1 = source.indexOf("/*", curPos);
			pos2 = source.indexOf("//", curPos);
			pos3 = source.indexOf("package", curPos);
			if (pos3 < 0) {
				throw new Exception();
			}
			if ((pos1 == -1 && pos2 == -1) || (pos3 < pos1 && pos3 < pos2) || (pos3 < pos1 && pos2 == -1) || (pos3 < pos2 && pos1 == -1)) {
				if (source.charAt(pos3 + 7) <= 0x20) {
					curPos = pos3 + 8;
					break;
				} else {
					curPos = pos3 + 6;
				}
			} else if (pos1 == -1 || pos1 < pos2) {
				curPos = source.indexOf("*/", pos1) + 2;
			} else {
				curPos = source.indexOf("\n", pos2) + 1;
			}
		}
		int lastPos = source.indexOf(";", curPos);
		return source.substring(curPos, lastPos).trim();
	}
	
	private void sqlMapperCopy(String sqlObjFileDir, String testFileDir) throws Exception {
		File objDir = new File(sqlObjFileDir);
		File[] objSqlFiles = objDir.listFiles();
		File webInfPath = new File(testFileDir + "/WEB-INF");
		if (webInfPath.exists() == false) {
			webInfPath.mkdirs();
		}
		File sqlPath = new File(testFileDir + "/WEB-INF/SqlMapper");
		if (sqlPath.exists() == false) {
			sqlPath.mkdirs();
		}
		for (int i = 0; i < objSqlFiles.length; i++) {
			fileCopy(objSqlFiles[i], new File(testFileDir + "/WEB-INF/SqlMapper/" + objSqlFiles[i].getName()));
		}		
	}
	
	private void confFileCopy(String confObjFileDir, String testFileDir) throws Exception {
		File objDir = new File(testFileDir + "/WEB-INF/conf");
		if (objDir.exists() == false) {
			objDir.mkdirs();
		}
		List<Map<String, Object>> outputList = null;
		outputList = sqlSession.selectList("getDevSerServiceTestConf");
		String fileName = null;
		String fileContent = null;
		BufferedWriter serviceConfFile = null;
		for (int i = 0; i < outputList.size(); i++) {
			fileName = (String)outputList.get(i).get("file_nm");
			fileContent = (String)outputList.get(i).get("file_content");
			fileContent = fileContent.replaceAll("#1#", testFileDir);
      serviceConfFile = new BufferedWriter(new FileWriter(testFileDir + "/WEB-INF/conf/" + fileName));
			serviceConfFile.write(fileContent);
			serviceConfFile.close();
		}
	}
	
	private void test(String dir) throws Exception {
		logger.debug("Start of test method");
		File appLogFileDir = new File(dir + "/WEB-INF/logs");
		if (appLogFileDir.exists() == false) {
			appLogFileDir.mkdirs();
		}
		File appLogFile = new File(dir + "/WEB-INF/logs/application.log");
		if (appLogFile.exists() == false) {
			appLogFile.createNewFile();
		}
		String testClass = "com.cmn.cmn.TestServiceTest";
		String libDir = dir + "/WEB-INF/lib";
		String classDir = dir + "/WEB-INF/classes";
		String allLibFile = classDir + ":";
		File[] libFiles = (new File(libDir)).listFiles();
		Map<String, String> returnMap = new HashMap<String, String>();
		for (int i = 0; i < libFiles.length; i++) {
			allLibFile = allLibFile + libDir + "/" + libFiles[i].getName() + ":";
		}
		String[] cmdStr = new String[5];
		cmdStr[0] = "java";
		cmdStr[1] = "-classpath";
		cmdStr[2] = allLibFile;
		cmdStr[3] = "-Dlog4j.configuration=" + dir + "/WEB-INF/conf/log4j.xml";
		cmdStr[4] = testClass;
		ProcessBuilder pb = new ProcessBuilder(cmdStr);
		Process oProcess = pb.start();
		FileInputStream fis = new FileInputStream(appLogFile);
		TestLogWriter tlwForGeneral = new TestLogWriter(sqlSession, testKey, 3, oProcess.getInputStream());
		TestLogWriter tlwForError = new TestLogWriter(sqlSession, testKey, 4, oProcess.getErrorStream());
		TestLogWriter tlwAppLog = new TestLogWriter(sqlSession, testKey, 5, fis);
		try {
			tlwForGeneral.start();
			tlwForError.start();
			tlwAppLog.start();
			oProcess.waitFor();
			Thread.sleep(100);
			if (oProcess.exitValue() != 0) {
				throw new Exception();
			}
		} catch (Exception e) {
			throw e;
		} finally {
			tlwForGeneral.setClose();
			tlwForError.setClose();
			tlwAppLog.setClose();
		}
	}
	
	private void writeErrorLog(Exception ex) {
    logger.debug("=================     User Exception Start    ==================");
    logger.debug("Error Trace!!");
    logger.debug(ex.getClass().getName() + ": " + ex.getMessage());
    StackTraceElement[] ste = ex.getStackTrace();
    for (int i = 0; i < ste.length; i++) {
      logger.debug("       at " + ste[i].toString());
    }
    logger.debug("=================      User Exception End     ==================");
	}
}