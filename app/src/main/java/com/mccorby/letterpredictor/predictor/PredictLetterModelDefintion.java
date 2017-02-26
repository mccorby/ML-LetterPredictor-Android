package com.mccorby.letterpredictor.predictor;

public class PredictLetterModelDefintion {
    private int[] mInputSize;
    private String mInputName;
    private String mOutputName;

    public int[] getInputSize() {
        return mInputSize;
    }

    public String getInputName() {
        return mInputName;
    }

    public String[] getOutputNames() {
        return new String[0];
    }

    public String getOutputName() {
        return mOutputName;
    }
}
