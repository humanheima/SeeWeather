package com.humanheima.hmweather.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.humanheima.hmweather.C;
import com.humanheima.hmweather.R;
import com.humanheima.hmweather.base.BaseActivity;
import com.humanheima.hmweather.bean.Version;
import com.humanheima.hmweather.network.NetWork;
import com.humanheima.hmweather.ui.dialog.Dialog;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by dumingwei on 2016/9/26.
 * 检车版本更新
 */

public class CheckVersionUtil {

    public static void checkVersion(final BaseActivity context) {
        NetWork.getApi().mVersionAPI(C.API_TOKEN)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Version>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e("tag", "onerror:" + e.getMessage());
                        T.showToast(context, e.getMessage());
                    }

                    @Override
                    public void onNext(Version version) {
                        String newVersionName = version.getVersionShort();
                        String curVersionName = getCurrentVersion(context);
                        LogUtil.e("tag", newVersionName + "," + curVersionName);
                        if (curVersionName.compareTo(newVersionName) < 0) {
                            Dialog dialog = Dialog.newInstance(context, version);
                            dialog.show(context.getSupportFragmentManager(), "update");
                        } else {
                            T.showToast(context, context.getString(R.string.current));
                        }
                    }
                });
    }

    /**
     * 获取版本号
     *
     * @return 当前应用的版本号
     */
    public static String getCurrentVersion(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            return info.versionName;
        } catch (Exception e) {
            e.printStackTrace();
            return context.getString(R.string.can_not_find_version_name);
        }
    }
}
