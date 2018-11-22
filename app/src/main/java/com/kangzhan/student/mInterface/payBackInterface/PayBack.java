package com.kangzhan.student.mInterface.payBackInterface;

/**
 * Created by kangzhan011 on 2017/6/16.
 */

public class PayBack {
    public static String stuPayback(){
        String v="http://app.kzxueche.com/api/Pay/wxorderQuery";
        return v;
    }
    public static String stuPaybackZfb(){
        String v="http://app.kzxueche.com/api/Pay/aliPayQuery";
        return v;
    }
}
