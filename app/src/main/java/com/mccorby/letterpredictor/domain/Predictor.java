package com.mccorby.letterpredictor.domain;

public interface Predictor {

    Character predictLetter(RawImage rawImage);
}
