package com.kangzhan.student.Debug;

import android.util.Log;

/**
 * Created by kangzhan011 on 2017/8/1.
 */

public class mLog {
    private static boolean startLog=false;
    public static void i (String Tag,String Info){
        if(startLog){
            Log.i(Tag,Info);
        }
    }
    public static void e (String Tag,String Info){
        if(startLog){
            Log.e(Tag,Info);
        }
    }
}
