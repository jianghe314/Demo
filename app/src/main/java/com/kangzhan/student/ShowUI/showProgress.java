package com.kangzhan.student.ShowUI;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.kangzhan.student.R;

/**
 * Created by kangzhan011 on 2017/4/7.
 */

public class showProgress {
     static ProgressDialog dialog;
    //自定义进度条
    public static void showProgress(Context context, String text){
        dialog=new ProgressDialog(context);
        dialog.setMessage(text);
        dialog.setCancelable(true);
        dialog.show();
    }

    public static void closeProgress(){
        if(dialog!=null){
            dialog.dismiss();
        }
    }
}
