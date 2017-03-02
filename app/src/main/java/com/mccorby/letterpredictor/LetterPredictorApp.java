package com.mccorby.letterpredictor;

import android.app.Application;

import com.mccorby.letterpredictor.di.ApplicationComponent;
import com.mccorby.letterpredictor.di.ApplicationModule;
import com.mccorby.letterpredictor.di.DaggerApplicationComponent;
import com.mccorby.letterpredictor.di.PredictorComponent;
import com.mccorby.letterpredictor.di.PredictorModule;
import com.mccorby.letterpredictor.ui.PredictorView;


public class LetterPredictorApp extends Application {

    private ApplicationComponent mApplicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        mApplicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this)).build();
    }

    public PredictorComponent createPredictorComponent(PredictorView view) {
        return mApplicationComponent.plus(new PredictorModule(view));
    }
}
