package com.mccorby.letterpredictor.domain;

import com.google.gson.annotations.SerializedName;

// TODO This class should not be polluted with Gson annotations
// TODO Provide a low level object and a mapper
public class SharedConfig {

    @SerializedName("imageSize")
    private int mImageSize;
    @SerializedName("batchSize")
    private int mBatchSize;
    @SerializedName("modelFileName")
    private String mModelFileName;
    @SerializedName("outputNodeName")
    private String mOutputNodeName;
    @SerializedName("outputNodeNames")
    private String[] mOutputNodeNames;
    @SerializedName("inputNodeName")
    private String mInputNodeName;

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
