package com.mccorby.letterpredictor.ui;

import com.mccorby.letterpredictor.domain.InteractorCallback;
import com.mccorby.letterpredictor.domain.PredictInteractor;
import com.mccorby.letterpredictor.domain.RawImage;

import java.util.concurrent.Executor;

public class PredictorPresenter implements InteractorCallback<Character> {


    private Executor executor;
    private PredictInteractor predictInteractor;
    private PredictorView view;

    public PredictorPresenter(PredictorView view, Executor executor, PredictInteractor predictInteractor) {
        this.view = view;
        this.executor = executor;
        this.predictInteractor = predictInteractor;
    }

    public void predictLetter(final RawImage rawImage) {
        predictInteractor.setRawImage(rawImage);
        executor.execute(new Runnable() {
            @Override
            public void run() {
                predictInteractor.execute(PredictorPresenter.this);
            }
        });
    }

    @Override
    public void onSuccess(Character result) {
        view.showResult(result);
    }

    @Override
    public void onError() {

    }
}
