package com.mccorby.letterpredictor.domain;

import com.mccorby.letterpredictor.predictor.PredictLetter;

public class PredictInteractor implements Interactor<Character> {

    private PredictLetter predictLetter;
    private RawImage rawImage;

    public PredictInteractor(PredictLetter predictLetter) {

        this.predictLetter = predictLetter;
    }

    @Override
    public void execute(InteractorCallback<Character> callback) {
        Character result = predictLetter.predictLetter(rawImage);
        if (result != null) {
            callback.onSuccess(result);
        } else {
            callback.onError();
        }
    }

    public void setRawImage(RawImage rawImage) {
        this.rawImage = rawImage;
    }
}
