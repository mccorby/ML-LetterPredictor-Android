package com.mccorby.letterpredictor.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.mccorby.letterpredictor.domain.RawImage;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

public class ImageProcessor {

    private static final String TAG = ImageProcessor.class.getSimpleName();

    static {
        if (OpenCVLoader.initDebug()) {
            Log.i(TAG, "OpenCV initialize success");
        } else {
            Log.i(TAG, "OpenCV initialize failed");
        }
    }

    public RawImage getImage(Bitmap bitmap, int imageSize) {
        Mat tmp = new Mat(bitmap.getHeight(), bitmap.getWidth(), CvType.CV_8UC1);

        Utils.bitmapToMat(bitmap, tmp);
        Imgproc.cvtColor(tmp, tmp, Imgproc.COLOR_RGB2GRAY);

        List<Mat> matList = new ArrayList<>();
        Core.split(tmp, matList);

        Utils.matToBitmap(tmp, bitmap);

        Mat newImage = transformImage(tmp);

        float[] values = new float[imageSize * imageSize];
        for (int row = 0; row < newImage.rows(); row++) {
            for (int col = 0; col < newImage.cols(); col++) {
                double[] valueAtPixel = newImage.get(row, col);
                values[row * imageSize + col] = (float) valueAtPixel[0];
            }
        }

        return new RawImage(values);
    }

    private Mat transformImage(Mat image) {
        Mat newImage = new Mat();
        Imgproc.resize(image, newImage, new Size(28, 28));
        return newImage;
    }

    public void resume(Context context) {
        BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(context) {
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
        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_11, context, mLoaderCallback);
        } else {
            Log.d(TAG, "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }

    }
}
