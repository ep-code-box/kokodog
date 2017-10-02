package com.res.sto.module;

import org.springframework.stereotype.Service;

@Service
public interface AnalysisByNN {
    public double[] getOutputData(double[] input) throws Exception;
}