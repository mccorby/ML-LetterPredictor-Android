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
import com.mccorby.letterpredictor.domain.PredictInteractor;
import com.mccorby.letterpredictor.domain.PredictLetterModelDefinition;
import com.mccorby.letterpredictor.domain.RawImage;
import com.mccorby.letterpredictor.image.ImageProcessor;
import com.mccorby.letterpredictor.predictor.PredictLetter;

import org.tensorflow.contrib.android.TensorFlowInferenceInterface;

import java.io.ByteArrayOutputStream;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class PredictorActivity extends AppCompatActivity implements PredictorView {

    private static final String MODEL_FILE_NAME = "frozen_model.pb";
    private static final int IMAGE_SIZE = 28;

    private ViewGroup mContent;
    private PredictorPresenter mPresenter;
    private TensorFlowInferenceInterface mInferenceInterface;
    private DrawingArea mDrawingArea;
    private TextView mResultView;

    private ImageProcessor mImageProcessor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_predictor);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setupViews();
        initTensorFlow();

        // TODO Inject presenter
        Executor executor = Executors.newSingleThreadExecutor();
        int[] inputSizes = new int[]{128, 784};

        // TODO These values from Shared Config
        PredictLetterModelDefinition modelDefintion = new PredictLetterModelDefinition(
                "input_node",
                "output_node",
                new String[]{"output_node"},
                inputSizes
        );

        PredictLetter predictLetter = new PredictLetter(mInferenceInterface, modelDefintion);
        PredictInteractor predictorInteractor = new PredictInteractor(predictLetter);

        mPresenter = new PredictorPresenter(this, executor, predictorInteractor);
        mImageProcessor = new ImageProcessor();
    }

    private void initTensorFlow() {
        mInferenceInterface = new TensorFlowInferenceInterface();
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

    @Override
    protected void onResume() {
        super.onResume();
        mImageProcessor.resume(this);
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
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable = mContent.getBackground();
        if (bgDrawable != null) {
            bgDrawable.draw(canvas);
        } else {
            canvas.drawColor(Color.WHITE);
        }
        mContent.draw(canvas);

        ByteArrayOutputStream bs = new ByteArrayOutputStream();
        returnedBitmap.compress(Bitmap.CompressFormat.PNG, 50, bs);
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
