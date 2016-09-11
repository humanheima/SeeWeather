package com.humanheima.hmweather.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by dmw on 2016/9/11.
 * toast
 */
public class T {
    private static Toast toast;

    public static void showToast(Context context,
                                 String content) {
        if (toast == null) {
            toast = Toast.makeText(context,
                    content,
                    Toast.LENGTH_SHORT);
        } else {
            toast.setText(content);
        }
        toast.show();
    }

}
