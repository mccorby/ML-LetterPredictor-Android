package com.mccorby.letterpredictor.domain;

import com.mccorby.letterpredictor.predictor.PredictLetter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PredictInteractorTest {

    private PredictInteractor mInteractor;
    @Mock
    private PredictLetter mockPredictLetter;
    @Mock
    private RawImage mockRawImage;
    @Mock
    private InteractorCallback mockCallback;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mInteractor = new PredictInteractor(mockPredictLetter);
    }

    @Test
    public void testExecuteNoError() throws Exception {
        // Given
        Character result = new Character('A');
        when(mockPredictLetter.predictLetter(mockRawImage)).thenReturn(result);
        when(mockRawImage.getValues()).thenReturn(new float[]{});

        // When
        mInteractor.execute(mockCallback);

        // Then
        verify(mockCallback).onSuccess(result);
    }

    @Test
    public void testExecuteError() throws Exception {
        // Given
        Character result = null;
        when(mockPredictLetter.predictLetter(mockRawImage)).thenReturn(result);
        when(mockRawImage.getValues()).thenReturn(new float[]{});

        // When
        mInteractor.execute(mockCallback);

        // Then
        verify(mockCallback).onError();
    }

}