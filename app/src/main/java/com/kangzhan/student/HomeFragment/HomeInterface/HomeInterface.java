package com.kangzhan.student.HomeFragment.HomeInterface;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by kangzhan011 on 2017/11/21.
 */

public class HomeInterface {

    //获取设置的城市
    public static String getCurrentCity(Context context){
        SharedPreferences sp=context.getSharedPreferences("CurrentCity",Context.MODE_PRIVATE);
        String city=sp.getString("city","新余市");
        return city;
    }

    //获得设置的城市的名称包括定位和手动设置的
    public static String getLocation(Context context){
        SharedPreferences sp=context.getSharedPreferences("Location",Context.MODE_PRIVATE);
        String city=sp.getString("city","新余市");
        return city;
    }

    //获取设置的城市，默认新余市
    public static String[] getHomeLocation(Context context){
        SharedPreferences sp=context.getSharedPreferences("HomeLocation",Context.MODE_PRIVATE);
        String[] data=new String[3];
        data[0]=sp.getString("city","新余市");
        data[1]=sp.getString("long","114.917346");
        data[2]=sp.getString("lati","27.817808");
        return data;
    }



    //驾校列表
    public static String schoolList(){
        String v="http://app.kzxueche.com/studentapi/Index/instList";
        return v;
    }
    //教练列表
    public static String teacherList(){
        String v="http://app.kzxueche.com/studentapi/Index/coachList";
        return v;
    }
    //驾校详情
    public static String schoolDetail(){
        String v="http://app.kzxueche.com/studentapi/Index/instiDetail";
        return v;
    }
    //学员点评
    public static String schoolDetail_Remark(){
        String v="http://app.kzxueche.com/studentapi/Index/getEnvalation";
        return v;
    }
    //学员问答
    public static String schoolDetail_Ques(){
        String v="http://app.kzxueche.com/studentapi/Index/getInterlocution";
        return v;
    }
    //学员提问
    public static String schoolDetail_Answer(){
        String v="http://app.kzxueche.com/studentapi/Index/addinterlocution";
        return v;
    }
    //我要报名，我要试学   type 1 报名 2试学
    public static String schoolDetail_learn(){
        String v="http://app.kzxueche.com/studentapi/Index/signUp";
        return v;
    }
    public static String TeacherDetail(){
        String v="http://app.kzxueche.com/studentapi/Index/coachDetail";
        return v;
    }
    //训练场列表
    public static String TrainPlace(){
        String v="http://app.kzxueche.com/studentapi/Index/regionList";
        return v;
    }
    //开通城市
    public static String OpenedCity(){
        String v="http://app.kzxueche.com/studentapi/Index/openArea";
        return v;
    }

    //轮播广告活动，轮播  type 1 轮播 2活动
    public static String Adviset(){
        String v="http://app.kzxueche.com/studentapi/Index/ad";
        return v;
    }


}
