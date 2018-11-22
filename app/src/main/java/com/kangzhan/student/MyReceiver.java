package com.kangzhan.student;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by kangzhan011 on 2017/4/18.
 */

public class MyReceiver extends BroadcastReceiver {
    private ActivityManager activityManager;
    @Override
    public void onReceive(Context context, Intent intent) {
        if(JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())){
            //用户点击了通知后的处理行为，不能判断是教练端登录还是学员端登录。。。

        }
    }
}
