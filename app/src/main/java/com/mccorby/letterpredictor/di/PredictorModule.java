package com.mccorby.letterpredictor.di;

import com.mccorby.letterpredictor.domain.PredictInteractor;
import com.mccorby.letterpredictor.domain.PredictLetterModelDefinition;
import com.mccorby.letterpredictor.image.ImageProcessor;
import com.mccorby.letterpredictor.predictor.PredictLetter;
import com.mccorby.letterpredictor.ui.PredictorPresenter;
import com.mccorby.letterpredictor.ui.PredictorView;

import org.tensorflow.contrib.android.TensorFlowInferenceInterface;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import dagger.Module;
import dagger.Provides;

@Module
public class PredictorModule {

    private PredictorView mView;

    public PredictorModule(PredictorView view) {

        mView = view;
    }

    @ActivityScope
    @Provides
    public ImageProcessor provideImageProcessor() {
        return new ImageProcessor();
    }

    @ActivityScope
    @Provides
    public TensorFlowInferenceInterface provideInferenceInterface() {
        return new TensorFlowInferenceInterface();
    }

    @ActivityScope
    @Provides
    public Executor provideExecutor() {
        return Executors.newSingleThreadExecutor();
    }

    @ActivityScope
    @Provides
    public PredictLetterModelDefinition provideModelDefinition() {
        // TODO These values coming from SharedConfig
        int[] inputSizes = new int[]{128, 784};
        return new PredictLetterModelDefinition(
                "input_node",
                "output_node",
                new String[]{"output_node"},
                inputSizes
        );
    }

    @Provides
    public PredictLetter providePredictLetter(TensorFlowInferenceInterface inferenceInterface,
                                              PredictLetterModelDefinition modelDefinition) {
        return new PredictLetter(inferenceInterface, modelDefinition);
    }

    @Provides
    public PredictInteractor providePredictInteractor(PredictLetter predictLetter) {
        return new PredictInteractor(predictLetter);
    }

    @Provides
    PredictorPresenter providePredictorPresenter(Executor executor, PredictInteractor predictInteractor) {
        return new PredictorPresenter(mView, executor, predictInteractor);
    }
}
