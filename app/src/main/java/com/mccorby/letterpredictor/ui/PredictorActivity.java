package com.mccorby.letterpredictor.ui;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mccorby.letterpredictor.R;
import com.mccorby.letterpredictor.domain.PredictInteractor;
import com.mccorby.letterpredictor.domain.PredictLetterModelDefinition;
import com.mccorby.letterpredictor.domain.RawImage;
import com.mccorby.letterpredictor.predictor.PredictLetter;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.tensorflow.contrib.android.TensorFlowInferenceInterface;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class PredictorActivity extends AppCompatActivity implements PredictorView {

    private static final String MODEL_FILE_NAME = "frozen_model.pb";
    private static final String TAG = PredictorActivity.class.getSimpleName();

    private ViewGroup mContent;
    private PredictorPresenter mPresenter;
    private TensorFlowInferenceInterface mInferenceInterface;
    private DrawingArea mDrawingArea;
    private TextView mResultView;

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS: {
                    Log.i(TAG, "OpenCV loaded successfully");
                }
                break;
                default: {
                    super.onManagerConnected(status);
                }
                break;
            }
        }
    };

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
        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_11, this, mLoaderCallback);
        } else {
            Log.d(TAG, "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }


    private void clearArea() {
        mDrawingArea.clear();
    }

    // TODO All this in the presenter? A different object for sure. Some ImageManipulator?
    private void guessLetter() {
        Bitmap bitmap = save();
        Mat matImage = getImage(bitmap);
        predictLetter(matImage);
    }

    private Bitmap save() {
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

    private Mat getImage(Bitmap bitmap) {
        Mat tmp = new Mat(bitmap.getHeight(), bitmap.getWidth(), CvType.CV_8UC1);

        Utils.bitmapToMat(bitmap, tmp);
        Imgproc.cvtColor(tmp, tmp, Imgproc.COLOR_RGB2GRAY);

        List<Mat> matList = new ArrayList<>();
        Core.split(tmp, matList);

        Utils.matToBitmap(tmp, bitmap);

        Mat newImage = transformImage(tmp);

        return newImage;
    }

    private void predictLetter(Mat newImage) {
        float[] values = new float[28 * 28];
        for (int row = 0; row < newImage.rows(); row++) {
            for (int col = 0; col < newImage.cols(); col++) {
                double[] valueAtPixel = newImage.get(row, col);
                values[row * 28 + col] = (float) valueAtPixel[0];
            }
        }
        mPresenter.predictLetter(new RawImage(values));
    }

    private Mat transformImage(Mat image) {
        Mat newImage = new Mat();
        Imgproc.resize(image, newImage, new Size(28, 28));
        return newImage;
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
