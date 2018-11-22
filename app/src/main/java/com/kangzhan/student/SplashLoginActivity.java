package com.kangzhan.student;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.kangzhan.student.CompayManage.CompayMainActivity;
import com.kangzhan.student.Debug.mLog;
import com.kangzhan.student.HeadView.HeadViewActivity;
import com.kangzhan.student.School.SchoolMainActivity;
import com.kangzhan.student.Student.Student_MainActivity;
import com.kangzhan.student.Teacher.TeacherMainActivity;
import com.kangzhan.student.com.BaseActivity;

import java.util.logging.Logger;

public class SplashLoginActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //小米手机这里有OOM
        //setTheme(R.style.SplashTheme);
        super.onCreate(savedInstanceState);
        //备注：注销引导界面
        if(getFirst()){
            recordFirst();
            Intent intent1=new Intent(getApplicationContext(),GuideActivity.class);
            overridePendingTransition(R.anim.login_in,R.anim.login_out);
            startActivity(intent1);
            finish();
        }else {
            //登录判断
            if(!getStuStatus().equals("false")){
                Intent student=new Intent(getApplicationContext(),Student_MainActivity.class);
                startActivity(student);
                finish();
            }else if(!getTeaStatus().equals("false")){
                Intent student=new Intent(getApplicationContext(),TeacherMainActivity.class);
                startActivity(student);
                finish();
            }else if(!getSchoolStatus().equals("false")){
                Intent student=new Intent(getApplicationContext(),SchoolMainActivity.class);
                startActivity(student);
                finish();
            }else if(!getCompayStatus().equals("false")){
                Intent student=new Intent(getApplicationContext(),CompayMainActivity.class);
                startActivity(student);
                finish();
            }else {
                Intent intent2=new Intent(getApplicationContext(),HeadViewActivity.class);
                //overridePendingTransition(R.anim.login_in,R.anim.login_out);
                startActivity(intent2);
                finish();
            }
        }
    }
    private void recordFirst(){
        SharedPreferences sp=getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putBoolean("First",false);
        editor.apply();
    }
    private boolean getFirst(){
        SharedPreferences sp=getPreferences(MODE_PRIVATE);
        return sp.getBoolean("First",true);
    }
    private String getStuStatus(){
        SharedPreferences sp=getSharedPreferences("studentKey", Context.MODE_PRIVATE);
        String key=sp.getString("key","false");
        return key;
    }
    private String getTeaStatus(){
        SharedPreferences sp=getSharedPreferences("teacherKey",Context.MODE_PRIVATE);
        String key=sp.getString("key","false");
        return key;
    }
    private String getSchoolStatus(){
        SharedPreferences sp=getSharedPreferences("schoolKey",Context.MODE_PRIVATE);
        String key=sp.getString("key","false");
        return key;
    }
    private String getCompayStatus(){
        SharedPreferences sp=getSharedPreferences("compayKey",Context.MODE_PRIVATE);
        String key=sp.getString("key","false");
        return key;
    }



}
