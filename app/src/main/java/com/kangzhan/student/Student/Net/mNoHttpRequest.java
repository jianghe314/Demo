package com.kangzhan.student.Student.Net;

import android.content.Context;

import com.yanzhenjie.nohttp.NoHttp;

/**
 * Created by kangzhan011 on 2017/3/30.
 */

public class mNoHttpRequest {
    public static void sendNohttp(Context context){
        NoHttp.initialize(context);
        
    }

}
