package com.humanheima.ibinderdemo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2016/9/20.
 */
public class Book implements Parcelable{
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
