package com.mccorby.letterpredictor.predictor;

import com.mccorby.letterpredictor.domain.PredictLetterModelDefinition;
import com.mccorby.letterpredictor.domain.SharedConfig;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.tensorflow.contrib.android.TensorFlowInferenceInterface;

public class PredictLetterTest {

    private PredictLetter mPredictLetter;

    @Mock
    private TensorFlowInferenceInterface mTensorFlowInferenceInterface;
    @Mock
    private PredictLetterModelDefinition mModelDefinition;
    @Mock
    private SharedConfig mockedSharedConfig;


    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mPredictLetter = new PredictLetter(mTensorFlowInferenceInterface, mModelDefinition, mockedSharedConfig);
    }

    @Test
    public void testPredictLetter() {
        // TODO This has to be done with PowerMockito or new Mockito with support to mock static classes
/*
        // Given
        Graph mockedGraph = mock(Graph.class);
        when(mTensorFlowInferenceInterface.graph()).thenReturn(mockedGraph);
        Operation mockedOperation = mock(Operation.class);
        when(mockedGraph.operation(any(String.class))).thenReturn(mockedOperation);
        Output mockedOutput = mock(Output.class);
        when(mockedOperation.output(0)).thenReturn(mockedOutput);
        Shape mockedShape = mock(Shape.class);
        when(mockedOutput.shape()).thenReturn(mockedShape);

        when(mockedShape.size(1)).thenReturn(new Long(10));


        RawImage rawImage = new RawImage(new float[]{});
        mPredictLetter.predictLetter(rawImage);
*/
    }
}