
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Based on http://www.oodlestechnologies.com/blogs/Capture-Signature-Using-FingerPaint-in-Android
 */

public class MainActivity extends AppCompatActivity {

    Button b1;
    ImageView signImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);

        b1 = (Button) findViewById(R.id.getSign);
        signImage = (ImageView) findViewById(R.id.imageView1);
        b1.setOnClickListener(onButtonClick);
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
        // TODO Auto-generated method stub
        if (resultCode == 1) {
            Bitmap b = BitmapFactory.decodeByteArray(
                    data.getByteArrayExtra("byteArray"), 0,
                    data.getByteArrayExtra("byteArray").length);
            signImage.setImageBitmap(b);

            saveToDisk(getResizedBitmap(b, 28, 28));
        }
    }

    private void saveToDisk(Bitmap resizedBitmap) {
        File dir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES) + File.separator + "/LetterPredictor");

        if (!dir.exists()) {
            dir.mkdirs();
        }
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


}
