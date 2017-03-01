package com.mccorby.letterpredictor.domain;

import com.mccorby.letterpredictor.predictor.PredictLetter;

public class PredictInteractor implements Interactor<Character> {

    private PredictLetter mPredictLetter;
    private RawImage mRawImage;

    public PredictInteractor(PredictLetter predictLetter) {

        mPredictLetter = predictLetter;
    }

    @Override
    public void execute(InteractorCallback<Character> callback) {
        Character result = mPredictLetter.predictLetter(mRawImage);
        if (result != null) {
            callback.onSuccess(result);
        } else {
            callback.onError();
        }
    }

    public void setRawImage(RawImage rawImage) {
        mRawImage = rawImage;
    }
}
