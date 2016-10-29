package com.humanheima.blurimgdemo;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    private ImageView img, imgBig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        img = (ImageView) findViewById(R.id.img);
        imgBig = (ImageView) findViewById(R.id.imgbig);

        //模拟加载网络图片
        Picasso.with(this).load(R.drawable.sun_main).into(img);
        //加载图片，添加回调，当加载成功的时候模糊图片
        Picasso.with(this).load(R.drawable.sun_main).into(imgBig, new Callback() {
            @Override
            public void onSuccess() {
                Bitmap bitmap = ((BitmapDrawable) img.getDrawable()).getBitmap();
                imgBig.setImageBitmap(BlurBitmap.blur(MainActivity.this, bitmap));
            }

            @Override
            public void onError() {
                Log.e("tag", "加载图片出错");
            }
        });
    }

}
