package com.kangzhan.student.ShowUI;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by kangzhan011 on 2017/5/4.
 */

public class mToast {
    public static void showText(Context context, String msg){
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}
