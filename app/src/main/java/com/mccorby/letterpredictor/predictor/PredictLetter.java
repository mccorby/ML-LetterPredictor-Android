package com.mccorby.letterpredictor.predictor;

import com.mccorby.letterpredictor.domain.PredictLetterModelDefinition;
import com.mccorby.letterpredictor.domain.Predictor;
import com.mccorby.letterpredictor.domain.RawImage;
import com.mccorby.letterpredictor.domain.SharedConfig;

import org.tensorflow.contrib.android.TensorFlowInferenceInterface;


public class PredictLetter implements Predictor {

    private TensorFlowInferenceInterface inferenceInterface;
    private PredictLetterModelDefinition modelDefinition;
    private SharedConfig sharedConfig;

    public PredictLetter(TensorFlowInferenceInterface inferenceInterface,
                         PredictLetterModelDefinition model,
                         SharedConfig sharedConfig) {
        this.inferenceInterface = inferenceInterface;

        modelDefinition = model;
        this.sharedConfig = sharedConfig;
    }

    public Character predictLetter(RawImage rawImage) {
        int imageSize = sharedConfig.getImageSize();
        int batchSize = sharedConfig.getBatchSize();
        // Note: The size of the input tensor includes the batch size!!
        float[] inputTensor = new float[batchSize * imageSize * imageSize];
        for (int i = 0; i < rawImage.getValues().length; i++) {
            inputTensor[i] = rawImage.getValues()[i];
        }

        inferenceInterface.fillNodeFloat(modelDefinition.getInputName(), modelDefinition.getInputSize(), inputTensor);
        inferenceInterface.runInference(modelDefinition.getOutputNames());

        int numClasses = sharedConfig.getOutputSize();
        float[] outputs = new float[batchSize * numClasses];
        inferenceInterface.readNodeFloat(modelDefinition.getOutputName(), outputs);

        // TODO Refactor this into a method to calculate the best output
        // TODO Combine it with a "evaluation" method or similar
        // TODO if confidence is too low it should return null
        int idxOfMax = 0;
        for (int i = 0; i < numClasses; i++) {
            if (outputs[i] > outputs[idxOfMax]) {
                idxOfMax = i;
            }
        }
        return (char) ('A' + idxOfMax);
    }


}
