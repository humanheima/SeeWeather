package com.humanheima.hmweather.utils;

import android.content.Context;
import android.widget.ImageView;

import com.humanheima.hmweather.R;
import com.squareup.picasso.Picasso;

/**
 * Created by Administrator on 2016/9/12.
 */
public class ImageUtil {

    public static void loadImg(Context context, ImageView imageView, int resId) {
        Picasso.with(context).load(resId)
                .error(R.mipmap.type_one_cloudy)
                .into(imageView);
    }
}
