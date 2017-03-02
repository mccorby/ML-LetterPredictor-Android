package com.mccorby.letterpredictor.di;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {
    PredictorComponent plus(PredictorModule module);
}
