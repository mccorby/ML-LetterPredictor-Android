package com.mccorby.letterpredictor.predictor;

import android.util.Log;

import com.mccorby.letterpredictor.domain.RawImage;

import org.tensorflow.contrib.android.TensorFlowInferenceInterface;


public class PredictLetter {

    public static final String TAG = PredictLetter.class.getSimpleName();
    private TensorFlowInferenceInterface mInferenceInterface;
    private PredictLetterModelDefintion mModel;

    public PredictLetter(TensorFlowInferenceInterface inferenceInterface, PredictLetterModelDefintion model) {
        mInferenceInterface = inferenceInterface;

        mModel = model;
    }

    public Character predictLetter(RawImage rawImage) {
        // TODO Define numClasses properly
        // https://www.tensorflow.org/extend/tool_developers/
        // The shape of the output is [N, NUM_CLASSES], where N is the batch size.
//        int numClasses =
//                (int) mInferenceInterface.graph().operation(mModel.getOutputName()).output(0).shape().size(1);

        float[] inputTensor = new float[128*784];
        for (int i = 0; i < rawImage.getValues().length; i++) {
            inputTensor[i] = rawImage.getValues()[i];
        }

        Log.d(TAG, "" + inputTensor[1000]);

        mInferenceInterface.fillNodeFloat(mModel.getInputName(), mModel.getInputSize(), inputTensor);
        mInferenceInterface.runInference(mModel.getOutputNames());
        // TODO Define outputs
        int numClasses = (int) mInferenceInterface.graph().operation(mModel.getOutputName()).output(0).shape().size(1);
        float[] outputs = new float[128 * numClasses];
        mInferenceInterface.readNodeFloat(mModel.getOutputName(), outputs);

        int idxOfMax = 0;
        for (int i = 0; i < numClasses; i++) {
            Log.d(TAG, "tusmuertos: " + outputs[i]);
            if (outputs[i] > outputs[idxOfMax]) {
                idxOfMax = i;
            }
        }
        int charA = (int) 'A';
        return new Character((char) (charA + idxOfMax));

    }
}
