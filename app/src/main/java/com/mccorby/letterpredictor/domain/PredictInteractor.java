package com.mccorby.letterpredictor.domain;

import com.mccorby.letterpredictor.predictor.PredictLetter;

public class PredictInteractor implements Interactor {

    private PredictLetter mPredictLetter;
    private RawImage mRawImage;

    public PredictInteractor(PredictLetter predictLetter, RawImage rawImage) {

        mPredictLetter = predictLetter;
        mRawImage = rawImage;
    }

    @Override
    public void execute() {
        mPredictLetter.predictLetter(mRawImage);
    }
}
