package com.mccorby.letterpredictor.domain;

public class PredictLetterModelDefinition {

    private int[] inputSize;
    private String inputName;
    private String outputName;
    private String[] outputNames;
    private int outputSize;

    public PredictLetterModelDefinition(String inputName, String outputName, String[] outputNames,
                                        int[] inputSize, int outputSize) {
        this.inputName = inputName;
        this.outputName = outputName;
        this.outputNames = outputNames;
        this.inputSize = inputSize;
        this.outputSize = outputSize;
    }

    public int[] getInputSize() {
        return inputSize;
    }

    public String getInputName() {
        return inputName;
    }

    public String[] getOutputNames() {
        return outputNames;
    }

    public String getOutputName() {
        return outputName;
    }

    public int getOutputSize() {
        return outputSize;
    }
}
