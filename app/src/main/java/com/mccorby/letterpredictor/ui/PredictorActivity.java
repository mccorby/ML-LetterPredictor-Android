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

import com.mccorby.letterpredictor.LetterPredictorApp;
import com.mccorby.letterpredictor.R;
import com.mccorby.letterpredictor.di.PredictorComponent;
import com.mccorby.letterpredictor.domain.RawImage;
import com.mccorby.letterpredictor.domain.SharedConfig;
import com.mccorby.letterpredictor.image.ImageProcessor;

import org.tensorflow.contrib.android.TensorFlowInferenceInterface;

import javax.inject.Inject;

public class PredictorActivity extends AppCompatActivity implements PredictorView {

    @Inject
    PredictorPresenter presenter;
    @Inject
    ImageProcessor imageProcessor;
    @Inject
    TensorFlowInferenceInterface inferenceInterface;
    @Inject
    SharedConfig sharedConfig;

    private ViewGroup contentView;
    private DrawingArea drawingArea;
    private TextView resultView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_predictor);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        injectMembers();

        setupViews();
    }

    private void injectMembers() {
        PredictorComponent component = ((LetterPredictorApp) getApplication()).createPredictorComponent(this);
        component.inject(this);
    }

    private void setupViews() {
        contentView = (ViewGroup) findViewById(R.id.drawing_area);
        drawingArea = new DrawingArea(this);
        contentView.addView(drawingArea);

        resultView = (TextView) findViewById(R.id.prediction_result);

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
        drawingArea.clear();
    }

    private void guessLetter() {
        Bitmap bitmap = obtainInputAsBitmap();
        RawImage rawImage = imageProcessor.getImage(bitmap);
        presenter.predictLetter(rawImage);
    }

    private Bitmap obtainInputAsBitmap() {
        Bitmap returnedBitmap = Bitmap.createBitmap(contentView.getWidth(),
                contentView.getHeight(), Bitmap.Config.ARGB_8888);
        // Next steps are necessary to pin the background to the image?
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable = contentView.getBackground();
        if (bgDrawable != null) {
            bgDrawable.draw(canvas);
        } else {
            canvas.drawColor(Color.WHITE);
        }
        contentView.draw(canvas);

        return returnedBitmap;
    }

    @Override
    public void showResult(final Character result) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                resultView.setText(result.toString());
            }
        });
    }
}
