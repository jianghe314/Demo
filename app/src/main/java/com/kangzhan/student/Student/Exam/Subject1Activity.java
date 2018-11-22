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
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
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
import com.kangzhan.student.R;
import com.kangzhan.student.Student.bean.Test1;
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

public class Subject1Activity extends BaseActivity implements View.OnClickListener{
    private Gson gson;
    private String mmsg;
    private int grade;
    private Subject1ViewPagerAdapter adapter;
    //
    private String Simulation_id;
    private Test1 test1;
    private ViewPager viewPager;
    private ArrayList<Test1> test1sdata=new ArrayList<>();
    private String TestContent;
    private Button PgUp,PgDn,HandOn;
    //倒计时与题型
    private TextView complete_tv,sum_tv,problem_type,time;
    private CountDownTimer timer;
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
                            showProgress.showProgress(Subject1Activity.this,"拼命加载中...");
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
                            values.add("1");
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
                            showProgress.showProgress(Subject1Activity.this,"上传成绩中...");
                        }
                    });
                    break;
                case 7788:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.closeProgress();
                            mToast.showText(getApplicationContext(),mmsg);
                            Intent exam=new Intent(Subject1Activity.this,ExamResultAcivity.class);
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
                            showMessage.showMsg(Subject1Activity.this,"网络连接异常，请检查网络连接");
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
        initView();
        gson= new Gson();
        handler.sendEmptyMessage(1111);
        new Thread(new Runnable() {
            @Override
            public void run() {
                params.clear();
                values.clear();
                params.add("key");
                params.add("kemu");
                params.add("stu_id");
                values.add(student.studentKey(Subject1Activity.this));
                values.add("1");
                values.add(student.studentId(Subject1Activity.this));
                sendReuqest("POST",1,student.startTest(),params,values);
            }
        }).start();
    }

    private void initView() {
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


    //解析数据
    private void ParserData(){
        JSONArray array= null;
        try {
            array = new JSONArray(TestContent);
            for (int i = 0; i <array.length() ; i++) {
                JSONObject object=array.getJSONObject(i);
                 test1=gson.fromJson(object.toString(),Test1.class);
                //Log.i("wenti",test1.getPhotourl());
                test1sdata.add(test1);
            }
            handler.sendEmptyMessage(6666);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //显示题目
    private void showtQuestions(){
        viewPager= (ViewPager) findViewById(R.id.exam_viewpager);
        adapter=new Subject1ViewPagerAdapter(test1sdata,this,getView());
        sum_tv.setText(test1sdata.size()+"");
        handler.sendEmptyMessage(6677);
    }
    //按钮点击事件
    @Override
    public void onClick(View v) {
        //当没有网络是要提示用户没有网络
        try {
            switch (v.getId()){
                case R.id.exam_pageUp:
                    //showText("上一题");
                    if(viewPager.getCurrentItem()+1>=1){
                        viewPager.setCurrentItem(viewPager.getCurrentItem()-1);
                        complete_tv.setText(viewPager.getCurrentItem()+1+"");
                    }else {
                        complete_tv.setText("1");
                    }
                    problem_type.setText(test1sdata.get(viewPager.getCurrentItem()+1).getQuestion_type());
                    break;
                case R.id.exam_pageDn:
                    //showText("下一题");
                    if(viewPager.getCurrentItem()<99){
                        viewPager.setCurrentItem(viewPager.getCurrentItem()+1);
                        complete_tv.setText(viewPager.getCurrentItem()+1+"");
                    }else {
                        complete_tv.setText("100");
                    }
                    problem_type.setText(test1sdata.get(viewPager.getCurrentItem()+1).getQuestion_type());
                    break;
                case R.id.exam_handon:
                    Holdon();
                    break;
                default:
                    break;
            }
        }catch (Exception e){

        }
    }

    private void Holdon() {
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
                values.add(student.studentKey(Subject1Activity.this));
                values.add(Simulation_id);
                values.add(grade+"");
                sendReuqest("POST",3,student.sendGrade(),params,values);
            }
        }).start();
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
                showText("考试时间已到，自动提交结果");
                Holdon();
            }
        }.start();
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

    @Override
    protected void onDestroy() {
        timer.cancel();
        super.onDestroy();
    }

    private void showText(String text){
        Toast.makeText(getApplicationContext(),text,Toast.LENGTH_SHORT).show();
    }



}
