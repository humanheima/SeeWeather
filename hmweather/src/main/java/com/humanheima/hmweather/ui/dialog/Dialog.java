package com.humanheima.hmweather.ui.dialog;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.humanheima.hmweather.R;
import com.humanheima.hmweather.base.BaseActivity;
import com.humanheima.hmweather.bean.Version;

/**
 * Created by dumingwei on 2016/9/26.
 * 使用DialogFragment创建对话框
 */

public class Dialog extends DialogFragment {

    private AlertDialog alertDialog;
    private static String title, message;
    private static String TITLE = "title";
    private static String MESSAGE = "message";
    private static String updateUrl;
    private static BaseActivity context;

    public Dialog() {

    }

    public static Dialog newInstance(BaseActivity activity, Version version) {
        Dialog dialogFragment = new Dialog();
        context = activity;
        title = "发现新版本" + version.getVersionShort();
        message = version.getChangelog();
        updateUrl = version.getUpdate_url();
        return dialogFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            title = getArguments().getString(TITLE);
            message = getArguments().getString(MESSAGE);
        }
    }

    @NonNull
    @Override
    public android.app.Dialog onCreateDialog(Bundle savedInstanceState) {
        alertDialog = new AlertDialog.Builder(getActivity()).setTitle(title).setMessage(message)
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Uri uri = Uri.parse(updateUrl);   //指定网址
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);           //指定Action
                        intent.setData(uri);                            //设置Uri
                        context.startActivity(intent);        //启动Activity
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.dismiss();
                    }
                }).create();
        return alertDialog;
    }
}
