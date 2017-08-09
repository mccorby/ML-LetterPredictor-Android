package com.mccorby.letterpredictor.predictor;

import android.util.Log;

import com.mccorby.letterpredictor.domain.PredictLetterModelDefinition;
import com.mccorby.letterpredictor.domain.Predictor;
import com.mccorby.letterpredictor.domain.RawImage;
import com.mccorby.letterpredictor.domain.SharedConfig;

import org.tensorflow.Operation;
import org.tensorflow.contrib.android.TensorFlowInferenceInterface;

import static android.content.ContentValues.TAG;


public class PredictNumber implements Predictor {

    private TensorFlowInferenceInterface inferenceInterface;
    private PredictLetterModelDefinition modelDefinition;
    private SharedConfig sharedConfig;

    public PredictNumber(TensorFlowInferenceInterface inferenceInterface,
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
        int inputSize = batchSize * imageSize * imageSize;
        float[] inputTensor = new float[inputSize];
        for (int i = 0; i < rawImage.getValues().length; i++) {
            inputTensor[i] = rawImage.getValues()[i];
        }

        Operation operation = inferenceInterface.graphOperation(modelDefinition.getOutputName());
        Log.d(TAG, "Output shape " + operation.output(0).shape());
        final int numClassesModel = (int) operation.output(0).shape().size(1);
        Log.i(TAG, "Output layer size is " + numClassesModel);

        inferenceInterface.feed(sharedConfig.getInputNodeName(), inputTensor, inputSize);
        inferenceInterface.feed("dropout_prob", new float[]{1.0f});
        inferenceInterface.run(modelDefinition.getOutputNames(), true);

        int numClasses = sharedConfig.getOutputSize();
        float[] outputs = new float[batchSize * numClasses];
        inferenceInterface.fetch(modelDefinition.getOutputName(), outputs);

        // TODO Refactor this into a method to calculate the best output
        // TODO Combine it with a "evaluation" method or similar
        // TODO if confidence is too low it should return null
        int idxOfMax = 0;
        for (int i = 0; i < numClasses; i++) {
            if (outputs[i] > outputs[idxOfMax]) {
                idxOfMax = i;
            }
        }
        Log.d(TAG, "Number classified " + idxOfMax);
        return Character.forDigit(idxOfMax, 10);
    }


}
