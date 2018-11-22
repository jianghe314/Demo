package com.kangzhan.student.Student.Exam;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.kangzhan.student.Debug.mLog;
import com.kangzhan.student.ShowUI.mToast;
import com.kangzhan.student.ShowUI.showMessage;
import com.kangzhan.student.ShowUI.showProgress;
import com.kangzhan.student.Student.Adapter.Subject1ViewPagerAdapter;
import com.kangzhan.student.Student.Adapter.Subject4ViewPagerAdapter;
import com.kangzhan.student.R;
import com.kangzhan.student.Student.bean.Test4;
import com.kangzhan.student.com.BaseActivity;
import com.kangzhan.student.mInterface.StudentInterface.student;
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
import java.util.List;

public class Subject4Activity extends BaseActivity implements View.OnClickListener{
    private Gson gson;
    private int grade;
    private Subject4ViewPagerAdapter adapter;
    //
    private String Simulation_id,mmsg;
    private Test4 test4;
    private ViewPager viewPager;
    private ArrayList<Test4> test4sdata=new ArrayList<>();
    private String TestContent;
    private Button PgUp,PgDn,HandOn;
    //倒计时与题型
    private TextView complete_tv,sum_tv,problem_type,time;
    private CountDownTimer timer;
    private ArrayList<String> params=new ArrayList<>();
    private ArrayList<String> values=new ArrayList<>();
    //
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1111:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.showProgress(Subject4Activity.this,"拼命加载中...");
                        }
                    });
                    break;
                case 2222:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.closeProgress();
                            handler.sendEmptyMessage(3333);
                        }
                    });
                    break;
                case 3333:
                    handler.sendEmptyMessage(1111);
                    //加载题目
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            params.clear();
                            values.clear();
                            params.add("kemu");
                            params.add("traintype");
                            values.add("4");
                            values.add("C1");
                            sendReuqest("GET",2,student.startExam(),params,values);
                        }
                    }).start();
                    break;
                case 4444:
                    //题目加载完成,解析题目
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            ParserData();
                        }
                    }).start();
                    break;
                case 5555:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.closeProgress();
                            mToast.showText(getApplicationContext(),mmsg);
                        }
                    });
                    break;
                case 6666:
                    //显示题目
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showtQuestions();
                        }
                    });
                    break;
                case 6677:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.closeProgress();
                            viewPager.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                            showTime(0,"");
                        }
                    });
                    break;
                case 7777:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //这里有个leak
                            showProgress.showProgress(Subject4Activity.this,"上传成绩中...");
                        }
                    });
                    break;
                case 7788:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.closeProgress();
                            mToast.showText(getApplicationContext(),mmsg);
                            Intent exam=new Intent(Subject4Activity.this,ExamResultAcivity.class);
                            exam.putExtra("time",time.getText().toString());
                            exam.putExtra("grade",grade);
                            exam.putExtra("subject",1);
                            startActivity(exam);
                            finish();

                        }
                    });
                    break;
                case 9999:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.closeProgress();
                            showMessage.showMsg(Subject4Activity.this,"网络连接异常，请检查网络连接");
                        }
                    });
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject1);
        Toolbar toolbar= (Toolbar) findViewById(R.id.subject1_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        gson=new Gson();
        intiView();
        handler.sendEmptyMessage(1111);
        //开始考试
        new Thread(new Runnable() {
            @Override
            public void run() {
                params.clear();
                values.clear();
                params.add("key");
                params.add("kemu");
                params.add("stu_id");
                values.add(student.studentKey(Subject4Activity.this));
                values.add("4");
                values.add(student.studentId(Subject4Activity.this));
                sendReuqest("POST",1,student.startTest(),params,values);
            }
        }).start();
    }

    private void intiView() {
        complete_tv= (TextView) findViewById(R.id.exam_hasfinish);
        sum_tv= (TextView) findViewById(R.id.textView2);
        problem_type= (TextView) findViewById(R.id.textView5);
        time= (TextView) findViewById(R.id.textView8);
        //
        PgDn= (Button) findViewById(R.id.exam_pageDn);
        PgDn.setOnClickListener(this);
        PgUp= (Button) findViewById(R.id.exam_pageUp);
        PgUp.setOnClickListener(this);
        HandOn= (Button) findViewById(R.id.exam_handon);
        HandOn.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        //当没有网络是要提示用户没有网络
        try {
            switch (v.getId()) {
                case R.id.exam_pageUp:
                    //showText("上一题");
                    if(viewPager.getCurrentItem()+1>=1){
                        viewPager.setCurrentItem(viewPager.getCurrentItem()-1);
                        complete_tv.setText(viewPager.getCurrentItem()+1+"");
                    }else {
                        complete_tv.setText("1");
                    }
                    problem_type.setText(test4sdata.get(viewPager.getCurrentItem()+1).getQuestion_type());
                    break;
                case R.id.exam_pageDn:
                    //showText("下一题");
                    if(viewPager.getCurrentItem()<49){
                        viewPager.setCurrentItem(viewPager.getCurrentItem()+1);
                        complete_tv.setText(viewPager.getCurrentItem()+1+"");
                    }else {
                        complete_tv.setText("50");
                    }
                    problem_type.setText(test4sdata.get(viewPager.getCurrentItem()+1).getQuestion_type());
                    break;
                case R.id.exam_handon:
                    HoldOn();
                    break;
                default:
                    break;
            }
        }catch (Exception e){

        }

    }

    private void HoldOn() {
        viewPager.removeAllViews();
        handler.sendEmptyMessage(7777);
        new Thread(new Runnable() {
            @Override
            public void run() {
                SharedPreferences sp=getSharedPreferences("Grade",Context.MODE_PRIVATE);
                grade=sp.getInt("grade",0);
                mLog.i("grade",grade+"");
                params.clear();
                values.clear();
                params.add("key");
                params.add("simulation_id");
                params.add("score");
                values.add(student.studentKey(Subject4Activity.this));
                values.add(Simulation_id);
                values.add(grade+"");
                sendReuqest("POST",3,student.sendGrade(),params,values);
            }
        }).start();
    }

    private void sendReuqest(final String way, int what, String url, ArrayList<String> params, ArrayList<String> values) {
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
                try {
                    JSONObject object=new JSONObject(response.get().toString());
                    mLog.e("reponse","->"+response.get().toString());
                    if(what==1){
                        //mmsg=object.getString("msg");
                        Simulation_id=object.getString("simulation_id");
                        handler.sendEmptyMessage(2222);
                    }else if(what==2){
                        if(object.getString("code").equals("200")){
                            TestContent=object.getString("question");
                            handler.sendEmptyMessage(4444);
                        }else {
                            handler.sendEmptyMessage(5555);
                        }
                    }else if(what==3){
                        //成绩上传
                        mmsg=object.getString("msg");
                        handler.sendEmptyMessage(7788);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
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

    //解析数据
    private void ParserData(){
        JSONArray array= null;
        try {
            array = new JSONArray(TestContent);
            for (int i = 0; i <array.length() ; i++) {
                JSONObject object=array.getJSONObject(i);
                test4=gson.fromJson(object.toString(),Test4.class);
                test4sdata.add(test4);
            }
            handler.sendEmptyMessage(6666);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    //显示题目
    private void showtQuestions(){
        viewPager= (ViewPager) findViewById(R.id.exam_viewpager);
        adapter=new Subject4ViewPagerAdapter(test4sdata,this,getView());
        sum_tv.setText(test4sdata.size()+"");
        handler.sendEmptyMessage(6677);
    }
    //创建页面
    private List<View> getView(){
        List<View> getView=new ArrayList<>();
        for (int i = 1; i <=100 ; i++) {
            View view= LayoutInflater.from(this).inflate(R.layout.item_exam_content_layout,null);
            getView.add(view);
        }
        return getView;
    }
    //到计时
    private void showTime(int grade,String spend){
        timer=new CountDownTimer(45*60*1000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                time.setText((millisUntilFinished/1000)/60+":"+(millisUntilFinished/1000)%60);
            }

            @Override
            public void onFinish() {
                mToast.showText(getApplicationContext(),"考试时间已到，自动提交结果");
                HoldOn();
            }
        }.start();
    }

    @Override
    protected void onDestroy() {
        timer.cancel();
        super.onDestroy();
    }
}
