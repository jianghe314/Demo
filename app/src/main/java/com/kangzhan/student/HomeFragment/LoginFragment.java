package com.kangzhan.student.HomeFragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.kangzhan.student.Advisetment.RecomendSchool;
import com.kangzhan.student.Advisetment.RecommendInstActivity;
import com.kangzhan.student.Advisetment.RecommendNewsActivity;
import com.kangzhan.student.Advisetment.RecommendTeacher;
import com.kangzhan.student.CompayManage_Login;
import com.kangzhan.student.Debug.mLog;
import com.kangzhan.student.HomeFragment.Bean.EventMessage;
import com.kangzhan.student.R;
import com.kangzhan.student.School_Login;
import com.kangzhan.student.Student_Login;
import com.kangzhan.student.Teacher_Login;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by kangzhan011 on 2017/11/16.
 */

public class LoginFragment extends HomeBaseFragment implements View.OnClickListener {
    private RelativeLayout student,teacher,Recommendschool,recommendTeacher,news,teach,school,Compay;
    @Override
    protected int setContentView() {
        return R.layout.findloginfragment_layout;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EventMessage msg){

    }

    @Override
    protected void initView(View v) {
        EventBus.getDefault().register(this);
        Recommendschool= (RelativeLayout) v.findViewById(R.id.main_login_11);
        Recommendschool.setOnClickListener(this);
        teach= (RelativeLayout) v.findViewById(R.id.main_login_13);
        teach.setOnClickListener(this);
        recommendTeacher= (RelativeLayout) v.findViewById(R.id.main_login_12);
        recommendTeacher.setOnClickListener(this);
        student= (RelativeLayout) v.findViewById(R.id.main_login_21);
        student.setOnClickListener(this);
        teacher= (RelativeLayout) v.findViewById(R.id.main_login_22);
        teacher.setOnClickListener(this);
        school= (RelativeLayout) v.findViewById(R.id.main_login_23);
        school.setOnClickListener(this);
        Compay= (RelativeLayout) v.findViewById(R.id.main_login_33);
        Compay.setOnClickListener(this);
        news= (RelativeLayout) v.findViewById(R.id.main_login_32);
        news.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.main_login_11:
                //驾校推介
                Intent rschool=new Intent(getActivity(), RecomendSchool.class);
                rschool.putExtra("id","34");
                startActivity(rschool);
                break;
            case R.id.main_login_12:
                //教练推介
                Intent reTeacher=new Intent(getActivity(), RecommendTeacher.class);
                reTeacher.putExtra("id","26");
                startActivity(reTeacher);
                break;
            case R.id.main_login_13:
                Intent inst=new Intent(getActivity(), RecommendInstActivity.class);
                startActivity(inst);
                break;
            case R.id.main_login_21:
                Intent student=new Intent(getActivity(),Student_Login.class);
                startActivity(student);
                break;
            case R.id.main_login_22:
                Intent teacher=new Intent(getActivity(),Teacher_Login.class);
                startActivity(teacher);
                break;
            case R.id.main_login_23:
                //驾校登录
                Intent school=new Intent(getActivity(),School_Login.class);
                startActivity(school);
                break;
            case R.id.main_login_31:
                //运管登录

                break;
            case R.id.main_login_32:
                //检测更新
               EventBus.getDefault().post(new EventMessage("check_version"));
                break;
            case R.id.main_login_33:
                //员工登录
                Intent compay=new Intent(getActivity(),CompayManage_Login.class);
                startActivity(compay);
                break;
        }
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
