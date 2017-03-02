package com.mccorby.letterpredictor.domain;

public class SharedConfig {

    private int mImageSize;
    private int mBatchSize;
    private String mModelFileName;
    private String mOutputNodeName;
    private String mInputNodeName;
    private String[] mOutputNodeNames;

    public int getImageSize() {
        return mImageSize;
    }

    public void setImageSize(int imageSize) {
        mImageSize = imageSize;
    }

    public int getBatchSize() {
        return mBatchSize;
    }

    public void setBatchSize(int batchSize) {
        mBatchSize = batchSize;
    }

    public String getModelFileName() {
        return mModelFileName;
    }

    public void setModelFileName(String modelFileName) {
        mModelFileName = modelFileName;
    }

    public String getOutputNodeName() {
        return mOutputNodeName;
    }

    public void setOutputNodeName(String outputNodeName) {
        mOutputNodeName = outputNodeName;
    }

    public String getInputNodeName() {
        return mInputNodeName;
    }

    public void setInputNodeName(String inputNodeName) {
        mInputNodeName = inputNodeName;
    }

    public String[] getOutputNodeNames() {
        return mOutputNodeNames;
    }

    public void setOutputNodeNames(String[] outputNodeNames) {
        mOutputNodeNames = outputNodeNames;
    }
}
