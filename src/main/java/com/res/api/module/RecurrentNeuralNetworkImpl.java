package com.res.api.module;

import java.util.Calendar;
import java.util.Arrays;

import org.apache.ibatis.session.SqlSession;

import com.res.api.module.RecurrentNeuralNetwork;

public class RecurrentNeuralNetworkImpl implements RecurrentNeuralNetwork {
  public static final int HYPER_TAN = 1;
  public static final int INV_EXP = 2;
  public static final int LINEAR = 3;

  private double[][] weight_internal;
  private double[][] weight_input;
  private double[][] weight_output;
  private int actFunc;
  private double[] state;
  
  public RecurrentNeuralNetworkImpl() throws Exception {
    this.weight_internal = null;
    this.weight_input = null;
    this.weight_output = null;
    this.actFunc = INV_EXP;
    this.state = null;
  }
  
  public double[] forwardPropagation(double[] input) throws Exception {
    int i = 0;
    int j = 0;
    if (weight_internal == null) {
      throw new Exception("Need to init RNN value..");
    }
    if (state == null) {
      state = new double[weight_internal.length];
      for (i = 0; i < state.length; i++) {
        state[i] = 0.0;
      }
    }
    double[] stateNext = new double[state.length];
    for (i = 0; i < state.length; i++) {
      for (j = 0; j < state.length; j++) {
        stateNext[i] = stateNext[i] + weight_internal[i][j] * state[j];
      }
    }
    for (i = 0; i < state.length; i++) {
      for (j = 0; j < input.length; j++) {
        stateNext[i] = stateNext[i] + weight_input[i][j] * input[j];
      }
      state[i] = takeFunc(stateNext[i]);
    }
    double[] output = new double[weight_output.length];
    for (i = 0; i < weight_output.length; i++) {
      output[i] = 0.0;
    }
    for (i = 0 ; i < weight_output.length; i++) {
      for (j = 0; j < weight_output[i].length; j++) {
        output[i] = output[i] + weight_output[i][j] * state[j];
      }
      output[i] = takeFunc(output[i]);
    }
    return output;
  }
  
  public void init(double weight_internal[][], double weight_input[][], double weight_output[][]) throws Exception {
    
  }
  
  public void init(double weight_internal[][], double weight_input[][], double weight_output[][], int actFunc) throws Exception {
    
  }
  
  public void init(SqlSession sqlSession, int rnnNum) throws Exception {
    
  }
  
  public void updateDataBase(SqlSession sqlSession, int userNum, Calendar calendar) throws Exception {
    
  }
  
  public void backPropagation(double[][] input, double[][] output, int truncLevel) throws Exception {
    int i = 0;
    int j = 0;
    int k = 0;
    double[][] outputEstimate = new double[output.length][];
    double[][] stateList = new double[output.length][];
    double[][] dLdV = new double[this.weight_output.length][];
    double[][] dLdU = new double[this.weight_input.length][];
    double[][] dLdW = new double[this.weight_internal.length][];
    for (i = 0; i < this.weight_output.length; i++) {
      dLdV[i] = new double[this.weight_output[i].length];
    }
    for (i = 0; i < this.weight_input.length; i++) {
      dLdU[i] = new double[this.weight_input[i].length];
    }
    for (i = 0; i < this.weight_internal.length; i++) {
      dLdW[i] = new double[this.weight_internal[i].length];
    }
    for (i = 0; i < input.length; i++) {
      outputEstimate[i] = forwardPropagation(input[i]);
      stateList[i] = Arrays.copyOf(this.state, this.state.length);
    }
    for (i = 0; i < input.length; i++) {
      for (j = 0; j < output[output.length - 1 - i].length; j++) {
        for (k = 0; k < state.length; k++) {
          dLdV[j][k] =  (outputEstimate[i][j] - output[output.length - 1 - i][j]);
        }
      }
      for (j = 0; j < truncLevel && j >= i; j++) {
        
      }
    }
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
    switch (this.actFunc) {
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
    switch (this.actFunc) {
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
  
  private double softmax(double[] x, int index) throws Exception {
    double modulus = 0.0;
    int i = 0;
    for (i = 0; i < x.length; i++) {
      modulus = modulus + Math.exp(x[i]);
    }
    return Math.exp(x[index]) / modulus;
  }
}