package com.humanheima.blurimgdemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.Element;
import android.support.v8.renderscript.RenderScript;
import android.support.v8.renderscript.ScriptIntrinsicBlur;
/**
 * Created by LiuZhichao on 1/26/16.
 */
public class RenderScriptGaussianBlur {
    private RenderScript rs;
    public RenderScriptGaussianBlur(Context context){
    this.rs = RenderScript.create(context);
    }
    public Bitmap blur(int radius, Bitmap bitmapOriginal) {
        final Allocation input = Allocation.createFromBitmap(rs, bitmapOriginal);
        final Allocation output = Allocation.createTyped(rs, input.getType());
        final ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        script.setRadius(radius);
        script.setInput(input);
        script.forEach(output);
        output.copyTo(bitmapOriginal);
        return  bitmapOriginal;
    }
}