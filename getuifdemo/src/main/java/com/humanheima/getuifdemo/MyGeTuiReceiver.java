package com.humanheima.getuifdemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.igexin.sdk.PushConsts;

public class MyGeTuiReceiver extends BroadcastReceiver {
    /**
     * 应用未启动, 个推 service已经被唤醒,保存在该时间段内离线消息(此时 GetuiSdkDemoActivity.tLogView == null)
     */
    public static StringBuilder payloadData = new StringBuilder();

    public MyGeTuiReceiver() {

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        switch (bundle.getInt(PushConsts.CMD_ACTION)) {
            case PushConsts.GET_MSG_DATA:
                // 获取透传数据
                // String appid = bundle.getString("appid");
                byte[] payload = bundle.getByteArray("payload");

                String taskid = bundle.getString("taskid");
                String messageid = bundle.getString("messageid");
                if (payload != null) {
                    String data = new String(payload);
                    //Log.d("GetuiSdkDemo", "receiver payload : " + data);
                    payloadData.append(data);
                    payloadData.append("\n");
                    Log.d("GetuiSdkDemo", "receiver payload : " + payloadData.toString());
                }
                break;
            case PushConsts.GET_CLIENTID:
                String cid = bundle.getString("clientid");
                if (!TextUtils.isEmpty(cid)) {
                    Log.e("cid", cid);
                }
                break;
            case PushConsts.GET_SDKONLINESTATE:
                boolean online = bundle.getBoolean("onlineState");
                Log.d("GetuiSdkDemo", "online = " + online);
                break;
            default:
                break;
        }
    }
}
