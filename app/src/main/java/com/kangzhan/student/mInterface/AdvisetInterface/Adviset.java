package com.kangzhan.student.mInterface.AdvisetInterface;

/**
 * Created by kangzhan011 on 2017/5/22.
 */

public class Adviset {
    public static String SchoolRecommend(){
        String v="http://app.kzxueche.com/api/News/index";
        return v;
    }
    //版本更新接口
    public static String checkVersion(){
        String v="http://app.kzxueche.com/clerkapi/Login/appVersionInfo";
        return v;
    }
}
