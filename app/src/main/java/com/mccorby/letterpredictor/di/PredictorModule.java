package com.mccorby.letterpredictor.di;

import com.mccorby.letterpredictor.domain.PredictInteractor;
import com.mccorby.letterpredictor.domain.PredictLetterModelDefinition;
import com.mccorby.letterpredictor.domain.SharedConfig;
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

    private PredictorView view;

    public PredictorModule(PredictorView view) {

        this.view = view;
    }

    @ActivityScope
    @Provides
    public ImageProcessor provideImageProcessor(SharedConfig sharedConfig) {
        return new ImageProcessor(sharedConfig);
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
    public PredictLetterModelDefinition provideModelDefinition(SharedConfig sharedConfig) {
        int imageSize = sharedConfig.getImageSize();
        int[] inputSizes = new int[]{sharedConfig.getBatchSize(), imageSize * imageSize};
        return new PredictLetterModelDefinition(
                sharedConfig.getInputNodeName(),
                sharedConfig.getOutputNodeName(),
                sharedConfig.getOutputNodeNames(),
                inputSizes,
                sharedConfig.getOutputSize()
        );
    }

    @Provides
    public PredictLetter providePredictLetter(TensorFlowInferenceInterface inferenceInterface,
                                              PredictLetterModelDefinition modelDefinition,
                                              SharedConfig sharedConfig) {
        return new PredictLetter(inferenceInterface, modelDefinition, sharedConfig);
    }

    @Provides
    public PredictInteractor providePredictInteractor(PredictLetter predictLetter) {
        return new PredictInteractor(predictLetter);
    }

    @Provides
    PredictorPresenter providePredictorPresenter(Executor executor, PredictInteractor predictInteractor) {
        return new PredictorPresenter(view, executor, predictInteractor);
    }
}
