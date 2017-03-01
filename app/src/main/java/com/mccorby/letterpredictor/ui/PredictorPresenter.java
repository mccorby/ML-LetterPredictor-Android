package com.mccorby.letterpredictor.ui;

import com.mccorby.letterpredictor.domain.InteractorCallback;
import com.mccorby.letterpredictor.domain.PredictInteractor;
import com.mccorby.letterpredictor.domain.RawImage;

import java.util.concurrent.Executor;

public class PredictorPresenter implements InteractorCallback<Character> {


    private Executor mExecutor;
    private PredictInteractor mPredictInteractor;
    private PredictorView mView;

    public PredictorPresenter(PredictorView view, Executor executor, PredictInteractor predictInteractor) {
        mView = view;
        mExecutor = executor;
        mPredictInteractor = predictInteractor;
    }

    public void predictLetter(final RawImage rawImage) {
        mPredictInteractor.setRawImage(rawImage);
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                mPredictInteractor.execute(PredictorPresenter.this);
            }
        });
    }

    @Override
    public void onSuccess(Character result) {
        mView.showResult(result);
    }

    @Override
    public void onError() {

    }
}
