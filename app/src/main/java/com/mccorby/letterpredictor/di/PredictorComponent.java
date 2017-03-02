package com.mccorby.letterpredictor.di;

import com.mccorby.letterpredictor.ui.PredictorActivity;

import dagger.Component;

@ActivityScope
@Component(modules = PredictorModule.class)
public interface PredictorComponent {
    void inject(PredictorActivity activity);
}
