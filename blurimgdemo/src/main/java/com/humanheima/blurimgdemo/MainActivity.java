package com.humanheima.blurimgdemo;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    private ImageView img, img1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        img = (ImageView) findViewById(R.id.img);
        img1 = (ImageView) findViewById(R.id.img1);
        Picasso.with(this).load(R.drawable.sun_main).into(img, new Callback() {
            @Override
            public void onSuccess() {
                Bitmap bitmap = ((BitmapDrawable) img.getDrawable()).getBitmap();
                img1.setImageBitmap(BlurBitmap.blur(MainActivity.this, bitmap));
                img.setImageBitmap(BlurBitmap.blur(MainActivity.this, bitmap));
            }

            @Override
            public void onError() {

            }
        });
    }

}
