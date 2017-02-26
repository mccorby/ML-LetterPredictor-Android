package com.mccorby.letterpredictor.predictor;

import com.mccorby.letterpredictor.domain.RawImage;

import org.tensorflow.contrib.android.TensorFlowInferenceInterface;

import java.util.Arrays;
import java.util.Comparator;

public class PredictLetter {

    private TensorFlowInferenceInterface mInferenceInterface;
    private PredictLetterModelDefintion mModel;

    public PredictLetter(TensorFlowInferenceInterface inferenceInterface, PredictLetterModelDefintion model) {
        mInferenceInterface = inferenceInterface;

        mModel = model;
    }

    public Character predictLetter(RawImage rawImage) {
        // TODO Define numClasses properly
        // The shape of the output is [N, NUM_CLASSES], where N is the batch size.
        int numClasses =
                (int) mInferenceInterface.graph().operation(mModel.getOutputName()).output(0).shape().size(1);


        mInferenceInterface.fillNodeFloat(mModel.getInputName(), mModel.getInputSize(), rawImage.getValues());
        mInferenceInterface.runInference(mModel.getOutputNames());
        // TODO Define outputs
        float[] outputs = new float[]{};
        mInferenceInterface.readNodeFloat(mModel.getOutputName(), outputs);

//        Arrays.asList(outputs).stream().max(new Comparator<float[]>() {
//            @Override
//            public int compare(float[] o1, float[] o2) {
//                return 0;
//            }
//        });

        return null;
    }
}
