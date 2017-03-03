package com.mccorby.letterpredictor.domain;

public class RawImage {

    private float[] values;

    public RawImage(float[] values) {

        this.values = values;
    }

    public float[] getValues() {
        return values;
    }

}
