package com.mccorby.letterpredictor.predictor;

public class PredictLetterModelDefintion {
    private String[] mOutputNames;
    private int[] mInputSize;
    private String mInputName;
    private String mOutputName;

    public PredictLetterModelDefintion(String inputName, String outputName, String[] outputNames, int[] inputSize) {
        mInputName = inputName;
        mOutputName = outputName;
        mOutputNames = outputNames;
        mInputSize = inputSize;
    }

    public int[] getInputSize() {
        return mInputSize;
    }

    public String getInputName() {
        return mInputName;
    }

    public String[] getOutputNames() {
        return mOutputNames;
    }

    public String getOutputName() {
        return mOutputName;
    }
}
