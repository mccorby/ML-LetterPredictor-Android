package com.mccorby.letterpredictor.ui;

import com.mccorby.letterpredictor.domain.PredictInteractor;
import com.mccorby.letterpredictor.domain.RawImage;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.concurrent.Executor;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

public class PredictorPresenterTest {

    private PredictorPresenter mPredictorPresenter;

    @Mock
    private PredictorView mockedView;
    @Mock
    private PredictInteractor mockedPredictInteractor;
    @Mock
    private Executor mockedExecutor;
    @Mock
    private RawImage mockedRawImage;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mPredictorPresenter = new PredictorPresenter(mockedView, mockedExecutor, mockedPredictInteractor);
    }

    @Test
    public void testPredictLetter() {
        mPredictorPresenter.predictLetter(mockedRawImage);

        verify(mockedPredictInteractor).setRawImage(any(RawImage.class));
        verify(mockedPredictInteractor).execute(mPredictorPresenter);
        verify(mockedView).showResult(any(Character.class));
    }
}