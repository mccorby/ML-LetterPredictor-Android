package com.mccorby.letterpredictor.domain;

public class RawImage {

    private float[] mValues;

    public RawImage(float[] values) {

        mValues = values;
    }

    public float[] getValues() {
        return mValues;
    }

}
