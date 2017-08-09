package com.mccorby.letterpredictor.domain;

public class PredictInteractor implements Interactor<Character> {

    private Predictor predictor;
    private RawImage rawImage;

    public PredictInteractor(Predictor predictor) {

        this.predictor = predictor;
    }

    @Override
    public void execute(InteractorCallback<Character> callback) {
        Character result = predictor.predictLetter(rawImage);
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
