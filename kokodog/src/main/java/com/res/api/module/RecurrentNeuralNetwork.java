package com.res.api.module;

import java.util.Calendar;

import org.apache.ibatis.session.SqlSession;

public interface RecurrentNeuralNetwork {
  public static final int HYPER_TAN = 1;
  public static final int INV_EXP = 2;
  public static final int LINEAR = 3;
  public double[] forwardPropagation(double[] input) throws Exception;
  public void init(double weight_internal[][], double weight_input[][], double weight_output[][]) throws Exception;
  public void init(double weight_internal[][], double weight_input[][], double weight_output[][], int actFunc) throws Exception;
  public void init(SqlSession sqlSession, int rnnNum) throws Exception;
  public void updateDataBase(SqlSession sqlSession, int userNum, Calendar calendar) throws Exception;
  public void backPropagation(double[][] input, double[][] output, int truncLevel) throws Exception;
}