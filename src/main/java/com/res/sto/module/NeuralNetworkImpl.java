package com.res.sto.module;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import java.security.SecureRandom;
import org.apache.ibatis.session.SqlSession;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.res.sto.module.NeuralNetwork;

public class NeuralNetworkImpl implements NeuralNetwork {
  public static final int HYPER_TAN = 1;
  public static final int INV_EXP = 2;
  public static final int LINEAR = 3;
  
  
  public static final int UNIT_RANDOM = 1;
  public static final int ZERO = 2;
  private static final double MULTIPLE_FACTOR = 0.5;
  
  private int nnNum;
  private int userNum = 0;
  private double[][][] weight;
  private double[][] bias;
  private Object nowDtm;
  private int activationFunction;
  
  private static Logger logger = LogManager.getLogger(NeuralNetworkImpl.class);
  
  public NeuralNetworkImpl() {
    
  }
  
  private void setNowDtm() {
    Date date = new Date();
    this.nowDtm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
  }

  public int setInitValue(int inputSize, int outputSize, int layerSize, int neuranSize, int activationFunction, SqlSession sqlSession, int initValueMethod) throws Exception {
    int[] neuranSizeVec = new int[layerSize];
    for (int i = 0; i < layerSize; i++) {
      neuranSizeVec[i] = neuranSize;
    }
    return setInitValue(inputSize, outputSize, layerSize, neuranSizeVec, activationFunction, sqlSession, initValueMethod);
  }

  public int setInitValue(int inputSize, int outputSize, int layerSize, int[] neuranSize, int activationFunction, SqlSession sqlSession, int initValueMethod) throws Exception {
    this.activationFunction = activationFunction;
    if (neuranSize.length != layerSize) {
      throw new Exception("Not proper for neuran size vector.");
    }
    if (nowDtm == null) {
      setNowDtm();
    }
    Random random = SecureRandom.getInstance("SHA1PRNG");
    Map<String, Object> inputMap = new HashMap<String, Object>();
    inputMap.put("user_num", this.userNum);
    inputMap.put("input_size", inputSize);
    inputMap.put("output_size", outputSize);
    inputMap.put("layer_size", layerSize);
    inputMap.put("now_dtm", this.nowDtm);
    logger.debug("Input map of insertResStoNeuralNetwork - " + inputMap);
    sqlSession.insert("insertResStoNeuralNetwork", inputMap);
    inputMap.clear();
    inputMap.put("user_num", this.userNum);
    inputMap.put("input_size", inputSize);
    inputMap.put("output_size", outputSize);
    inputMap.put("layer_size", layerSize);
    logger.debug("Input map of getResStoMaxNeuralNetNum - " + inputMap);
    Map<String, Object> outputMap = sqlSession.selectOne("getResStoMaxNeuralNetNum", inputMap);
    logger.debug("Output map of getResStoMaxNeuralNetNum - " + outputMap);
    this.nnNum = ((Long)outputMap.get("nn_num")).intValue();
    for (int i = 0; i < layerSize; i++) {
      inputMap.clear();
      inputMap.put("user_num", this.userNum);
      inputMap.put("nn_num", this.nnNum);
      inputMap.put("layer_num", i + 1);
      inputMap.put("neuran_size", neuranSize[i]);
      inputMap.put("now_dtm", this.nowDtm);
      logger.debug("Input map of insertResStoNeuralNetLayerSize - " + inputMap);
      sqlSession.insert("insertResStoNeuralNetLayerSize", inputMap);
    }
    for (int i = 0; i < layerSize + 1; i++) {
      int inputWeightSize = 0;
      int outputWeightSize = 0;
      if (i == 0) {
        inputWeightSize = inputSize;
      } else {
        inputWeightSize = neuranSize[i - 1];
      }
      if (i == layerSize) {
        outputWeightSize = outputSize;
      } else {
        outputWeightSize = neuranSize[i];
      }
      for (int j = 0; j < inputWeightSize; j++) {
        for (int k = 0; k < outputWeightSize; k++) {
          inputMap.clear();
          inputMap.put("user_num", this.userNum);
          inputMap.put("nn_num", this.nnNum);
          inputMap.put("layer_num", i);
          inputMap.put("input_num", j + 1);
          inputMap.put("output_num", k + 1);
          inputMap.put("now_dtm", this.nowDtm);
          if (initValueMethod == NeuralNetworkImpl.ZERO) {
            inputMap.put("weight", 0.0);
          } else {
            inputMap.put("weight", random.nextDouble() * 2 - 1);
          }
          logger.debug("Input map of insertResStoNeuralNetworkWeight - " + inputMap);
          sqlSession.insert("insertResStoNeuralNetworkWeight", inputMap);
        }
      }
    }
    for (int i = 0; i < layerSize + 1; i++) {
      int biasSize = 0;
      if (i == layerSize) {
        biasSize = outputSize;
      } else {
        biasSize = neuranSize[i];
      }
      for (int j = 0; j < biasSize; j++) {
        inputMap.clear();
        inputMap.put("user_num", this.userNum);
        inputMap.put("nn_num", this.nnNum);
        inputMap.put("layer_num", i);
        inputMap.put("output_num", j + 1);
        inputMap.put("now_dtm", this.nowDtm);
        if (initValueMethod == NeuralNetworkImpl.ZERO) {
          inputMap.put("bias", 0.0);
        } else {
          inputMap.put("bias", random.nextDouble() * 2 - 1);
        }
        logger.debug("Input map of insertResStoNeuralNetworkBias - " + inputMap);
        sqlSession.insert("insertResStoNeuralNetworkBias", inputMap);        
      }
    }
    return this.nnNum;
  }
  
  public void setInitValue(int nnNum, SqlSession sqlSession) throws Exception {
    this.nnNum = nnNum;
    Map<String, Object> inputMap = new HashMap<String, Object>();
    inputMap.put("nn_num", nnNum);
    Map<String, Object> outputMap = null;
    logger.debug("Input map of SQL getResStoNeuralNetworkInfo - " + inputMap);
    outputMap = sqlSession.selectOne("getResStoNeuralNetworkInfo", inputMap);
    logger.debug("Output map of SQL getResStoNeuralNetworkInfo - " + outputMap);
    int inputSize = ((Long)(outputMap.get("input_size"))).intValue();
    int outputSize = ((Long)(outputMap.get("output_size"))).intValue();
    int layerSize = ((Long)(outputMap.get("layer_size"))).intValue();
    int[] neuranSize = new int[layerSize];
    List<Map<String, Object>> outputList = new ArrayList<Map<String, Object>>();
    int i = 0;
    int j = 0;
    int inputNum = 0;
    int outputNum = 0;
    for (i = 0; i < layerSize; i++) {
      inputMap.clear();
      inputMap.put("nn_num", nnNum);
      inputMap.put("layer_num", i + 1);
      logger.debug("Input map of SQL getResStoNeuralNetworkNeuranSize - " + inputMap);
      outputMap = sqlSession.selectOne("getResStoNeuralNetworkNeuranSize", inputMap);
      logger.debug("Output list of SQL getResStoNeuralNetworkNeuranSize - " + outputList);
      neuranSize[i] = ((Long)outputMap.get("neuran_size")).intValue();
    }
    this.weight = new double[layerSize + 1][][];
    this.bias = new double[layerSize + 1][];
    for (i = 0; i < layerSize + 1; i++) {
      if (i == 0) {
        this.weight[0] = new double[inputSize][];
      } else {
        this.weight[i] = new double[neuranSize[i - 1]][];
      }
      for (j = 0; j < weight[i].length; j++) {
        if (i != layerSize) {
          this.weight[i][j] = new double[neuranSize[i]];
        } else {
          this.weight[i][j] = new double[outputSize];
        }
      }
      inputMap.clear();
      inputMap.put("nn_num", this.nnNum);
      inputMap.put("layer_num", i);
      logger.debug("Input map of SQL getResStoNeuralNetworkWeight - " + inputMap);
      outputList = sqlSession.selectList("getResStoNeuralNetworkWeight", inputMap);
      logger.debug("Output list of SQL getResStoNeuralNetworkWeight - " + outputList);
      for (j = 0; j < outputList.size(); j++) {
        inputNum = ((Long)outputList.get(j).get("input_num")).intValue();
        outputNum = ((Long)outputList.get(j).get("output_num")).intValue();
        this.weight[i][inputNum - 1][outputNum - 1] = ((Double)outputList.get(j).get("weight")).doubleValue();
        logger.debug("value of weight[" + i + "][" + inputNum + "][" + outputNum + "] - " + this.weight[i][inputNum - 1][outputNum - 1]);
      }
      if (i != layerSize) {
        this.bias[i] = new double[neuranSize[i]];
      } else {
        this.bias[layerSize] = new double[outputSize];
      }
      inputMap.clear();
      inputMap.put("nn_num", this.nnNum);
      inputMap.put("layer_num", i);
      logger.debug("Input map of SQL getResStoNeuralNetworkBias - " + inputMap);
      outputList = sqlSession.selectList("getResStoNeuralNetworkBias", inputMap);
      logger.debug("Output list of SQL getResStoNeuralNetworkBias - " + outputList);
      for (j = 0; j < outputList.size(); j++) {
        outputNum = ((Long)outputList.get(j).get("output_num")).intValue();
        this.bias[i][outputNum - 1] = ((Double)outputList.get(j).get("bias")).doubleValue();
        logger.debug("value of bias[" + i + "][" + outputNum + "] - " + bias[i][outputNum - 1]);
      }
    }
  }
  
  public void setUserNum(int userNum) {
    this.userNum = userNum;
  }
  
  public double[] get(double[] input) throws Exception {
    if (weight == null || bias == null) {
      throw new Exception("Plz. set weight and bias first");      
    }
    if (this.weight[0].length != input.length) {
      throw new Exception("Input is not proper value");
    }
    double[] inputTemp = null;
    double[] outputTemp = null;
    inputTemp = new double[this.weight[0].length];
    int i = 0;
    int j = 0;
    for (i = 0; i < this.weight[0].length; i++) {
      inputTemp[i] = input[i];
    }
    for (i = 0; i < this.weight.length; i++) {
      outputTemp = getInLayer(inputTemp, i);
      for (j = 0; j < outputTemp.length; j++) {
        outputTemp[j] = takeFunc(outputTemp[j]);
      }
      inputTemp = new double[outputTemp.length];
      for (j = 0; j < outputTemp.length; j++) {
        inputTemp[j] = outputTemp[j];
      }
    }
    return inputTemp;
  }

  public void backPropagation(double[][] input, double[][] output) throws Exception {
    if (weight == null || bias == null) {
      throw new Exception("Plz. set weight and bias first");      
    }
    if (input.length != output.length) {
      throw new Exception("Set proper input value!!");
    }
    for (int i = 0; i < input.length - 1; i++) {
      if (input[i].length != input[i + 1].length) {
        throw new Exception("Set proper input value!!");
      }
    }
  }

  public void backPropagation(double[] input, double[] output) throws Exception {
    if (weight == null || bias == null) {
      throw new Exception("Plz. set weight and bias first");      
    }
    if (input.length != weight[0].length) {
      throw new Exception("Set proper input value!!");
    }
    if (output.length != weight[weight.length - 1][0].length) {
      throw new Exception("Set proper input value!!");
    }
    int i = 0;
    int j = 0;
    int k = 0;
    double z[][] = new double[this.weight.length][];
    double a[][] = new double[this.weight.length][];
    double tempWeight[][] = null;
    for (i = 0; i < this.weight.length; i++) {
      z[i] = new double[this.weight[i][0].length];
      a[i] = new double[this.weight[i][0].length];
      for (j = 0; j < this.weight[i][0].length; j++) {
        z[i][j] = 0.0;
        a[i][j] = 0.0;
        for (k = 0; k < this.weight[i].length; k++) {
          if (i == 0) {
            z[i][j] = z[i][j] + this.weight[i][k][j] * input[k];
          } else {
            z[i][j] = z[i][j] + this.weight[i][k][j] * a[i - 1][k];
          }
        }
        z[i][j] = z[i][j] + bias[i][j];
        logger.debug("vaule of z[" + i + "][" + j + "] - " + z[i][j]);
        a[i][j] = takeFunc(z[i][j]);
        logger.debug("vaule of a[" + i + "][" + j + "] - " + a[i][j]);
      }
    }
    double[] delta = null;
    double[] pastDelta = null;
    for (i = 0; i < this.weight.length; i++) {
      if (i == 0) {
        delta = new double[weight[this.weight.length - 1][0].length];
        for (j = 0; j < z[this.weight.length - 1].length; j++) {
          logger.debug("vaule of a[" + (this.weight.length - 1) + "][" + j + "] - " + a[this.weight.length - 1][j]);
          logger.debug("value of output[" + j + "] - " + output[j]);
          logger.debug("value of Diff z[" + (this.weight.length - 1) + "][" + j + "] - " + takeDiffFunc(z[this.weight.length - 1][j]));
          delta[j] = (a[this.weight.length - 1][j] - output[j]) * takeDiffFunc(z[this.weight.length - 1][j]);
          logger.debug("value of delta[" + (this.weight.length - 1) + "][" + j + "] - " + delta[j]);
        }
      } else {
        pastDelta = new double[delta.length];
        for (j = 0; j < delta.length; j++) {
          pastDelta[j] = delta[j];
        }
        delta = new double[weight[this.weight.length - i - 1][0].length];
        for (j = 0; j < weight[this.weight.length - i - 1][0].length; j++) {
          delta[j] = 0.0;
          for (k = 0; k < weight[this.weight.length- i][j].length; k++) {
            logger.debug("value of weight factor[" + (this.weight.length - i) + "][" + j + "][" + k + "] - " + (weight[this.weight.length - i][j][k] * pastDelta[k]));
            delta[j] = delta[j] + weight[this.weight.length - i][j][k] * pastDelta[k];
          }
          logger.debug("value of delta[" + (this.weight.length - i - 1) + "]["+ j + "] - " + delta[j]);
          delta[j] = delta[j] * takeDiffFunc(z[this.weight.length - i - 1][j]);
        }
      }
      if (i != 0) {
        for (j = 0; j < weight[this.weight.length - i].length; j++) {
          for (k = 0; k < weight[this.weight.length - i][j].length; k++) {
            weight[this.weight.length - i][j][k] = tempWeight[j][k];
          }
        }
      }
      tempWeight = new double[weight[this.weight.length - i - 1].length][];
      for (j = 0; j < weight[this.weight.length - i - 1].length; j++) {
        tempWeight[j] = new double[weight[this.weight.length - i - 1][j].length];
      }
      for (j = 0; j < this.weight[this.weight.length - i - 1].length; j++) {
        for(k = 0; k < this.weight[this.weight.length - i - 1][j].length; k++) {
          if (i != this.weight.length - 1) {
            tempWeight[j][k] = weight[this.weight.length - i - 1][j][k] - NeuralNetworkImpl.MULTIPLE_FACTOR * a[this.weight.length - i - 2][j] * delta[k];
          } else {
            tempWeight[j][k] = weight[this.weight.length - i - 1][j][k] - NeuralNetworkImpl.MULTIPLE_FACTOR * input[j] * delta[k];              
          }
          logger.debug("value of weight[" + (this.weight.length - i - 1) + "][" + j + "][" + k + "] - " + tempWeight[j][k]);
        }
      }
      for (j = 0; j < this.weight[this.weight.length - i - 1][0].length; j++) {
        bias[this.weight.length - i - 1][j] = bias[this.weight.length - i - 1][j] - NeuralNetworkImpl.MULTIPLE_FACTOR * delta[j];
        logger.debug("value of bias[" + (this.weight.length - i - 1) + "][" + j + "] - " + bias[this.weight.length - i - 1][j]);        
      }
    }
    for (i = 0; i < this.weight[0].length; i++) {
      for (j = 0; j < this.weight[0][i].length; j++) {
        weight[0][i][j] = tempWeight[i][j];
      }
    }
  }
  
  public double[][][] getWeight() {
    return weight;
  }
  
  public double[][] getBias() {
    return bias;
  }
  
  private double[] getInLayer(double[] input, int layerNum) throws Exception {
    double[] output = null;
    int i = 0;
    int j = 0;
    output = new double[this.weight[layerNum][0].length];
    for (i = 0; i < output.length; i++) {
      output[i] = 0.0;
    }
    for (i = 0; i < output.length; i++) {
      for (j = 0; j < input.length; j++) {
        output[i] = output[i] + input[j] * this.weight[layerNum][j][i];
      }
      output[i] = output[i] + this.bias[layerNum][i];
      logger.debug("Temp result for [" + (i + 1) + "][" + (j + 1) + "] - " + output[i]);
    }
    return output;
  }
  
  private double hyperTan(double x) throws Exception {
    double exp = Math.exp(x);
    double inverseOfExp = 1.0 / exp;
    return (exp - inverseOfExp) / (exp + inverseOfExp);
  }

  private double derivativeOfHyperTan(double x) throws Exception {
    double hyperSec = hyperSec(x);
    return hyperSec * hyperSec;
  }
  
  private double hyperSec(double x) throws Exception {
    double exp = Math.exp(x);
    double inverseOfExp = 1.0 / exp;
    return 2.0 / (exp + inverseOfExp);
  }

  private double invExp(double x) throws Exception {
    return 1.0 / (1.0 + Math.exp(x * -1.0));
  }

  private double derivativeOfInvExp(double x) throws Exception {
    double expMinus = Math.exp(x * -1.0);
    double funcValue = 1.0 / (1.0 + expMinus);
    return funcValue * funcValue * expMinus;
  }
  
  private double linear(double x) throws Exception {
    return x;
  }
  
  private double derivativeOfLinear(double x) throws Exception {
    return 1.0;
  }
  
  private double takeFunc(double x) throws Exception {
    switch (activationFunction) {
      case HYPER_TAN:
        return hyperTan(x);
      case INV_EXP:
        return invExp(x);
      case LINEAR:
        return linear(x);
      default:
        return x;
    }
  }
  
  private double takeDiffFunc(double x) throws Exception {
    switch (activationFunction) {
      case HYPER_TAN:
        return derivativeOfHyperTan(x);
      case INV_EXP:
        return derivativeOfInvExp(x);
      case LINEAR:
        return derivativeOfLinear(x);
      default:
        return 1.0;
    }
  }
  
  public void updateWeightBias(SqlSession sqlSession) throws Exception {
    if (weight == null || bias == null) {
      throw new Exception("Plz. set weight and bias first");      
    }
    if (this.nowDtm == null) {
      setNowDtm();
    }
    Map<String, Object> inputMap = new HashMap<String, Object>();
    for (int i = 0; i < weight.length; i++) {
      for (int j = 0; j < weight[i].length; j++) {
        for (int k = 0; k < weight[i][j].length; k++) {
          inputMap.clear();
          inputMap.put("user_num", this.userNum);
          inputMap.put("now_dtm", nowDtm);
          inputMap.put("nn_num", this.nnNum);
          inputMap.put("layer_num", i);
          inputMap.put("input_num", j + 1);
          inputMap.put("output_num", k + 1);
          logger.debug("Input map of updateResStoNeuralNetworkWeightForDelete - " + inputMap);
          sqlSession.update("updateResStoNeuralNetworkWeightForDelete", inputMap);
          inputMap.clear();
          inputMap.put("user_num", this.userNum);
          inputMap.put("now_dtm", nowDtm);
          inputMap.put("nn_num", this.nnNum);
          inputMap.put("layer_num", i);
          inputMap.put("input_num", j + 1);
          inputMap.put("output_num", k + 1);
          inputMap.put("weight", weight[i][j][k]);
          logger.debug("Input map of insertResStoNeuralNetworkWeight - " + inputMap);
          sqlSession.update("insertResStoNeuralNetworkWeight", inputMap);
        }
      }
      for (int j = 0; j < bias[i].length; j++) {
        inputMap.clear();
        inputMap.put("user_num", this.userNum);
        inputMap.put("now_dtm", nowDtm);
        inputMap.put("nn_num", this.nnNum);
        inputMap.put("layer_num", i);
        inputMap.put("output_num", j + 1);
        logger.debug("Input map of updateResStoNeuralNetworkBiasForDelete - " + inputMap);
        sqlSession.update("updateResStoNeuralNetworkBiasForDelete", inputMap);
        inputMap.clear();
        inputMap.put("user_num", this.userNum);
        inputMap.put("now_dtm", nowDtm);
        inputMap.put("nn_num", this.nnNum);
        inputMap.put("layer_num", i);
        inputMap.put("output_num", j + 1);
        inputMap.put("bias", bias[i][j]);
        logger.debug("Input map of insertResStoNeuralNetworkBias - " + inputMap);
        sqlSession.update("insertResStoNeuralNetworkBias", inputMap);        
      }
    }
  }
  
  public int getInputSize() throws Exception {
    return this.weight[0].length;
  }
  public int getOutputSize() throws Exception {
    return this.weight[weight.length - 1][0].length;
  }
}