package com.humanheima.ibinderdemo;

import android.os.Parcel;
import android.os.Parcelable;

import com.humanheima.ibinderdemo.aidl.Book;

/**
 * Created by Administrator on 2016/9/20.
 */
public class UserParcelable implements Parcelable {
    private int userId;
    private String userName;
    private Book book;
    public UserParcelable(int userId,String userName,Book book) {
        this.userId=userId;
        this.userName=userName;
        this.book=book;
    }

    private UserParcelable(Parcel in){
        userId=in.readInt();
        userName=in.readString();
        book=in.readParcelable(Thread.currentThread().getContextClassLoader());
    }
    public static final Creator<UserParcelable> CREATOR = new Creator<UserParcelable>() {
        @Override
        public UserParcelable createFromParcel(Parcel in) {
            return new UserParcelable(in);
        }

        @Override
        public UserParcelable[] newArray(int size) {
            return new UserParcelable[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(userId);
        dest.writeString(userName);
        dest.writeParcelable(book,0);
    }
}
