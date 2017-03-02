package com.mccorby.letterpredictor.ui;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mccorby.letterpredictor.R;
import com.mccorby.letterpredictor.di.DaggerPredictorComponent;
import com.mccorby.letterpredictor.di.PredictorModule;
import com.mccorby.letterpredictor.domain.RawImage;
import com.mccorby.letterpredictor.image.ImageProcessor;

import org.tensorflow.contrib.android.TensorFlowInferenceInterface;

import javax.inject.Inject;

public class PredictorActivity extends AppCompatActivity implements PredictorView {

    private static final String MODEL_FILE_NAME = "frozen_model.pb";
    private static final int IMAGE_SIZE = 28;

    @Inject
    PredictorPresenter mPresenter;
    @Inject
    ImageProcessor mImageProcessor;
    @Inject
    TensorFlowInferenceInterface mInferenceInterface;

    private ViewGroup mContent;
    private DrawingArea mDrawingArea;
    private TextView mResultView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_predictor);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        injectMembers();

        setupViews();
        initTensorFlow();
    }

    private void injectMembers() {
        DaggerPredictorComponent.builder()
                .predictorModule(new PredictorModule(this))
                .build()
                .inject(this);
    }

    private void initTensorFlow() {
        if (mInferenceInterface.initializeTensorFlow(getAssets(), MODEL_FILE_NAME) != 0) {
            throw new RuntimeException("TF initialization failed");
        }

    }

    private void setupViews() {
        mContent = (ViewGroup) findViewById(R.id.drawing_area);
        mDrawingArea = new DrawingArea(this);
        mContent.addView(mDrawingArea);

        mResultView = (TextView) findViewById(R.id.prediction_result);

        findViewById(R.id.guess_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guessLetter();
            }
        });

        findViewById(R.id.clear_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearArea();
            }
        });
    }

    private void clearArea() {
        mDrawingArea.clear();
    }

    private void guessLetter() {
        Bitmap bitmap = obtainInputAsBitmap();
        RawImage rawImage = mImageProcessor.getImage(bitmap, IMAGE_SIZE);
        mPresenter.predictLetter(rawImage);
    }

    private Bitmap obtainInputAsBitmap() {
        Bitmap returnedBitmap = Bitmap.createBitmap(mContent.getWidth(),
                mContent.getHeight(), Bitmap.Config.ARGB_8888);
        // Next steps are necessary to pin the background to the image?
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable = mContent.getBackground();
        if (bgDrawable != null) {
            bgDrawable.draw(canvas);
        } else {
            canvas.drawColor(Color.WHITE);
        }
        mContent.draw(canvas);

        return returnedBitmap;
    }

    @Override
    public void showResult(final Character result) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mResultView.setText(result.toString());
            }
        });
    }
}
