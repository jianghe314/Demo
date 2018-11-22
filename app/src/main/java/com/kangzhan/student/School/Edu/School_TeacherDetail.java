package com.kangzhan.student.School.Edu;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.kangzhan.student.Debug.mLog;
import com.kangzhan.student.R;
import com.kangzhan.student.School.Bean.SchoolTeacherDetail;
import com.kangzhan.student.ShowUI.mToast;
import com.kangzhan.student.ShowUI.showMessage;
import com.kangzhan.student.ShowUI.showProgress;
import com.kangzhan.student.com.BaseActivity;
import com.kangzhan.student.mInterface.CompayInterface.CompayInterface;
import com.kangzhan.student.mInterface.SchoolInterface.school;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class School_TeacherDetail extends BaseActivity implements View.OnClickListener{
    private CircleImageView header;
    private TextView name,sex,phone,address,wx,email,carType,lesson,price,carLabel,score;
    private RelativeLayout menu1,menu2;
    private Gson gson;
    private String mmsg,teacherId,who;
    private SchoolTeacherDetail teacherInfo;
    private ArrayList<String> params=new ArrayList<>();
    private ArrayList<String> values=new ArrayList<>();
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1111:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.showProgress(School_TeacherDetail.this,"加载中...");
                        }
                    });
                    break;
                case 2222:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.closeProgress();
                            Glide.with(School_TeacherDetail.this).load(teacherInfo.getOss_photo()).diskCacheStrategy(DiskCacheStrategy.NONE).error(R.drawable.header).crossFade().into(header);
                            name.setText(teacherInfo.getName());
                            sex.setText(teacherInfo.getSex());
                            phone.setText(teacherInfo.getMobile());
                            address.setText(teacherInfo.getAddress());
                            wx.setText(teacherInfo.getWechatnum());
                            email.setText(teacherInfo.getEmail());
                            carType.setText(teacherInfo.getTeachpermitted());
                            lesson.setText(teacherInfo.getSubject_name());
                            carLabel.setText(teacherInfo.getLicnum());
                            price.setText(teacherInfo.getSubject_price()+"元/时");
                            score.setText(teacherInfo.getScore()+"分");
                        }
                    });
                    break;
                case 8888:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.closeProgress();
                            mToast.showText(getApplicationContext(),mmsg);
                        }
                    });
                    break;
                case 9999:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.closeProgress();
                            showMessage.showMsg(School_TeacherDetail.this,"网络连接异常，请检查网络连接");
                        }
                    });
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //教练详情，加个请求数据字段type=1
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school__teacher_detail);
        Toolbar toolbar= (Toolbar) findViewById(R.id.school_edu_tDetail_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        Intent getId=getIntent();
        teacherId=getId.getStringExtra("teacherId");
        who=getId.getStringExtra("who");
        gson=new Gson();
        initView();
        iniData();
    }


    private void initView() {
        header= (CircleImageView) findViewById(R.id.school_teacher_detail_header);
        name= (TextView) findViewById(R.id.school_teacher_detail_name);
        sex= (TextView) findViewById(R.id.school_teacher_detail_sex);
        phone= (TextView) findViewById(R.id.school_teacher_detail_phone);
        address= (TextView) findViewById(R.id.school_teacher_detail_address);
        wx= (TextView) findViewById(R.id.school_teacher_detail_wx);
        email= (TextView) findViewById(R.id.school_teacher_detail_email);
        carType= (TextView) findViewById(R.id.school_teacher_detail_carType);
        lesson= (TextView) findViewById(R.id.school_teacher_detail_lesson);
        carLabel= (TextView) findViewById(R.id.school_teacher_detail_carLabel);
        price= (TextView) findViewById(R.id.school_teacher_detail_price);
        score= (TextView) findViewById(R.id.school_teacher_detail_score);
        //
        menu1= (RelativeLayout) findViewById(R.id.item_school_edu_teacher_detail_stu);
        menu1.setOnClickListener(this);
        menu2= (RelativeLayout) findViewById(R.id.item_school_edu_teacher_detail_reward);
        menu2.setOnClickListener(this);
    }

    private void iniData() {
        handler.sendEmptyMessage(1111);
        if(who.equals("school")){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    params.clear();
                    values.clear();
                    params.add("key");
                    params.add("coach_id");
                    values.add(school.schoolKey(getApplicationContext()));
                    values.add(teacherId);
                    sendReuqest("GET",1, school.EduTeacherDetail(),params,values);
                }
            }).start();
        }else if(who.equals("compay")){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    params.clear();
                    values.clear();
                    params.add("key");
                    params.add("coach_id");
                    values.add(CompayInterface.CompayKey(School_TeacherDetail.this));
                    values.add(teacherId);
                    sendReuqest("GET",1, school.EduTeacherDetail(),params,values);
                }
            }).start();
        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.item_school_edu_teacher_detail_stu:
                //在训学员  fragment_school_edu_training_sut
                Intent trainingstu=new Intent(this,Edu_Training_Stu.class);
                trainingstu.putExtra("Id",teacherId);
                startActivity(trainingstu);
                break;
            case R.id.item_school_edu_teacher_detail_reward:
                //合格奖  fragment_school_edu_teacher_detail_reward
                Intent reward=new Intent(this,Edu_teacher_reward.class);
                reward.putExtra("Id",teacherId);
                startActivity(reward);
                break;
            default:
                break;
        }
    }



    private void sendReuqest(String way, int what, String url, ArrayList<String> params, ArrayList<String> values) {
        RequestMethod method=null;
        if(way.equals("POST")){
            method=RequestMethod.POST;
        }else if(way.equals("GET")){
            method=RequestMethod.GET;
        }
        Request<JSONObject> request= NoHttp.createJsonObjectRequest(url,method);
        for (int i = 0; i <params.size(); i++) {
            request.add(params.get(i),values.get(i));
        }
        getRequestQueue().add(what, request, new OnResponseListener<JSONObject>() {
            @Override
            public void onStart(int what) {

            }

            @Override
            public void onSucceed(int what, Response<JSONObject> response) {
                mLog.e("response","->"+response.get().toString());
                if(what==1){
                    try {
                        JSONObject object=new JSONObject(response.get().toString());
                        mmsg=object.getString("msg");
                        if(object.getString("code").equals("200")){
                            JSONArray array=new JSONArray(object.getString("baseinfo"));
                            if(array.length()>0){
                                teacherInfo=gson.fromJson(array.getJSONObject(0).toString(),SchoolTeacherDetail.class);
                                handler.sendEmptyMessage(2222);
                            }else {
                                handler.sendEmptyMessage(8888);
                            }
                        }else {
                            handler.sendEmptyMessage(8888);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailed(int what, Response<JSONObject> response) {
                handler.sendEmptyMessage(9999);
            }

            @Override
            public void onFinish(int what) {

            }
        });
    }

}
