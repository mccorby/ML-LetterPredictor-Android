package com.mccorby.letterpredictor.predictor;

import com.mccorby.letterpredictor.domain.PredictLetterModelDefinition;
import com.mccorby.letterpredictor.domain.Predictor;
import com.mccorby.letterpredictor.domain.RawImage;

import org.tensorflow.contrib.android.TensorFlowInferenceInterface;


public class PredictLetter implements Predictor {

    public static final String TAG = PredictLetter.class.getSimpleName();
    // TODO This magic numbers should come from shared config
    private static final int BATCH_SIZE = 128;
    private static final int IMAGE_SIZE = 28;

    private TensorFlowInferenceInterface mInferenceInterface;
    private PredictLetterModelDefinition mModel;

    public PredictLetter(TensorFlowInferenceInterface inferenceInterface, PredictLetterModelDefinition model) {
        mInferenceInterface = inferenceInterface;

        mModel = model;
    }

    public Character predictLetter(RawImage rawImage) {
        // Note: The size of the input tensor includes the batch size!!
        float[] inputTensor = new float[BATCH_SIZE * IMAGE_SIZE * IMAGE_SIZE];
        for (int i = 0; i < rawImage.getValues().length; i++) {
            inputTensor[i] = rawImage.getValues()[i];
        }

        mInferenceInterface.fillNodeFloat(mModel.getInputName(), mModel.getInputSize(), inputTensor);
        mInferenceInterface.runInference(mModel.getOutputNames());

        int numClasses = (int) mInferenceInterface.graph().operation(mModel.getOutputName()).output(0).shape().size(1);
        float[] outputs = new float[BATCH_SIZE * numClasses];
        mInferenceInterface.readNodeFloat(mModel.getOutputName(), outputs);

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
