
package com.mccorby.letterpredictor;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.mccorby.letterpredictor.domain.RawImage;
import com.mccorby.letterpredictor.predictor.PredictLetter;
import com.mccorby.letterpredictor.predictor.PredictLetterModelDefintion;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;
import org.tensorflow.contrib.android.TensorFlowInferenceInterface;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Based on http://www.oodlestechnologies.com/blogs/Capture-Signature-Using-FingerPaint-in-Android
 */

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    static {
        System.loadLibrary("opencv_java");
    }

    private Button b1;
    private ImageView signImage;
    private TextView theLetter;

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
    private TensorFlowInferenceInterface inferenceInterface;
    // TODO Model should be stored as Protobuf
    private String modelFilename = "frozen_model.pb";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.content_main);

        b1 = (Button) findViewById(R.id.getSign);
        signImage = (ImageView) findViewById(R.id.imageView1);
        b1.setOnClickListener(onButtonClick);

        theLetter = (TextView) findViewById(R.id.the_letter);

        initTensorFlow();
    }

    private void initTensorFlow() {
        inferenceInterface = new TensorFlowInferenceInterface();
        if (inferenceInterface.initializeTensorFlow(getAssets(), modelFilename) != 0) {
            throw new RuntimeException("TF initialization failed");
        }

    }

    Button.OnClickListener onButtonClick = new Button.OnClickListener() {

        @Override
        public void onClick(View v) {
            Intent i = new Intent(MainActivity.this, CaptureActivity.class);
            startActivityForResult(i, 0);

        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 1) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(
                    data.getByteArrayExtra("byteArray"), 0,
                    data.getByteArrayExtra("byteArray").length);

            Mat tmp = new Mat(bitmap.getHeight(), bitmap.getWidth(), CvType.CV_8UC1);

            Utils.bitmapToMat(bitmap, tmp);
            Imgproc.cvtColor(tmp, tmp, Imgproc.COLOR_RGB2GRAY);

            List<Mat> matList = new ArrayList<>();
            Core.split(tmp, matList);

            Utils.matToBitmap(tmp, bitmap);
            signImage.setImageBitmap(bitmap);

            Mat newImage = transformImage(tmp);

            saveToDisk(newImage);


            predictLetter(newImage);

//            saveToDisk(getResizedBitmap(bitmap, 28, 28));
        }
    }

    private void predictLetter(Mat newImage) {
        float[] result = new float[28*28];
        for (int row = 0; row < newImage.rows(); row++) {
            for (int col = 0; col < newImage.cols(); col++) {
                double[] valueAtPixel = newImage.get(row, col);
                result[row * 28 + col] = (float) valueAtPixel[0];
            }
        }

        int[] inputSizes = new int[]{128, 784};

        PredictLetterModelDefintion modelDefintion = new PredictLetterModelDefintion(
                "input_node",
                "output_node",
                new String[]{"output_node"},
                inputSizes
        );
        PredictLetter predictLetter = new PredictLetter(inferenceInterface, modelDefintion);

        RawImage rawImage = new RawImage(result);
        Character theCharacter = predictLetter.predictLetter(rawImage);
        theLetter.setText(theCharacter.toString());

    }

    private Mat transformImage(Mat image) {
        Mat newImage = new Mat();
        Imgproc.resize(image, newImage, new Size(28, 28));
        return newImage;
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

    private void saveToDisk(Mat image) {
        File dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File file = new File(dir, "letter.png");
        Highgui.imwrite(file.getAbsolutePath(), image);
    }

    private void saveToDisk(Bitmap resizedBitmap) {
//        File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
//        File dir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES) + File.separator + "/LetterPredictor");
//
//        if (!dir.exists()) {
//            dir.mkdirs();
//        }

        Log.d(MainActivity.class.getSimpleName(), "External Storage is Writable? " + isExternalStorageWritable());
        File dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File file = new File(dir, "letter.png");

        Log.d(MainActivity.class.getSimpleName(), "Saving to " + file.toString());

        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        resizedBitmap.compress(Bitmap.CompressFormat.PNG, 85, fOut);
        try {
            fOut.flush();
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Bitmap getResizedBitmap(Bitmap image, int bitmapWidth, int bitmapHeight) {
        return Bitmap.createScaledBitmap(image, bitmapWidth, bitmapHeight, true);
    }

    /* Checks if external storage is available for read and write */
    private boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }


}
