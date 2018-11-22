package com.kangzhan.student.ShowUI;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

/**
 * Created by kangzhan011 on 2017/6/23.
 */

public class showMessage {
    public static void showMsg(Context context,String msg){
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setTitle(msg);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }
}
