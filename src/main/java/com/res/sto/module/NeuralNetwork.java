package com.res.sto.module;

import org.apache.ibatis.session.SqlSession;

public interface NeuralNetwork {
  public static final int HYPER_TAN = 1;
  public static final int INV_EXP = 2;
  public static final int LINEAR = 3;
  
  public static final int UNIT_RANDOM = 1;
  public static final int ZERO = 2;
  public int setInitValue(int inputSize, int outputSize, int layerSize, int[] neuranSize, int activationFunction, SqlSession sqlSession, int initValueMethod) throws Exception;
  public int setInitValue(int inputSize, int outputSize, int layerSize, int neuranSize, int activationFunction, SqlSession sqlSession, int initValueMethod) throws Exception;
  public void setInitValue(int nnNum, SqlSession sqlSession) throws Exception;
  public void setUserNum(int userNum);
  public void backPropagation(double[][] input, double[][] output) throws Exception;
  public void backPropagation(double[] input, double[] output) throws Exception;
  public double[][][] getWeight();
  public double[][] getBias();
  public double[] get(double[] input) throws Exception;
  public void updateWeightBias(SqlSession sqlSession) throws Exception;
  public int getInputSize() throws Exception;
  public int getOutputSize() throws Exception;
}