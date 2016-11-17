package com.humanheima.blurimgdemo;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.img_big_below)
    ImageView imgBigBelow;
    @BindView(R.id.img_big_above)
    ImageView imgBigAbove;
    @BindView(R.id.img)
    CircleImageView img;
    @BindView(R.id.seekbar)
    SeekBar seekbar;
    /**
     * 透明度
     */
    private int mAlpha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        //模拟加载网络图片,
        Picasso.with(this).load(R.drawable.sun_main).into(img);
        //加载图片，添加回调，当加载成功的时候模糊图片
        Picasso.with(this).load(R.drawable.sun_main).into(imgBigBelow, new Callback() {
            @Override
            public void onSuccess() {
                Bitmap bitmap = ((BitmapDrawable) img.getDrawable()).getBitmap();
                imgBigBelow.setImageBitmap(BlurBitmap.blur(MainActivity.this, bitmap));
            }

            @Override
            public void onError() {
                Log.e("tag", "加载图片出错");
            }
        });
        Picasso.with(this).load(R.drawable.sun_main).into(imgBigAbove);

        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mAlpha = progress;
                //动态改变上面的大图的透明度
                Log.e("alph", "progress" + progress + "," + (1f - ((float) progress / 100f)));
                imgBigAbove.setAlpha((1f - ((float) progress / 100f)));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }


}
