package com.mccorby.letterpredictor.di;

import com.mccorby.letterpredictor.ui.PredictorActivity;

import dagger.Subcomponent;

@ActivityScope
@Subcomponent(modules = PredictorModule.class)
public interface PredictorComponent {
    void inject(PredictorActivity activity);
}
