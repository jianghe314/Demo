package com.kangzhan.student.Student.Train;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.kangzhan.student.Debug.mLog;
import com.kangzhan.student.R;
import com.kangzhan.student.ShowUI.mToast;
import com.kangzhan.student.ShowUI.showMessage;
import com.kangzhan.student.ShowUI.showProgress;
import com.kangzhan.student.Student.bean.Evalution;
import com.kangzhan.student.Student.mutils.StarBar;
import com.kangzhan.student.com.BaseActivity;
import com.kangzhan.student.mInterface.CompayInterface.CompayInterface;
import com.kangzhan.student.mInterface.StudentInterface.student;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class EvalutionActivity extends BaseActivity implements StarBar.OnStarChangeListener,View.OnClickListener{
    private TextView name,lesson,address,price,car_label,stastu,time,eval_value;
    private EditText eval_content;
    private Button publishBtn;
    private StarBar starBar;
    private Evalution evalu;
    private String mmg;
    private Gson gson;
    private String id;
    private int mremark;
    private String remark;
    private ArrayList<String> params=new ArrayList<>();
    private ArrayList<String> values=new ArrayList<>();
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0000:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.showProgress(EvalutionActivity.this,"评价中...");
                        }
                    });
                    break;
                case 1111:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.closeProgress();
                            mToast.showText(getApplicationContext(),mmg);
                        }
                    });
                    break;
                case 2222:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.closeProgress();
                            mToast.showText(getApplicationContext(),"评分不能为空");
                        }
                    });
                    break;
                case 3333:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mToast.showText(getApplicationContext(),"评价内容不能为空");
                        }
                    });
                    break;
                case 4444:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.showProgress(EvalutionActivity.this,"加载中...");
                        }
                    });
                    break;
                case 5555:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.closeProgress();
                            name.setText(evalu.getCoachname());
                            lesson.setText(evalu.getDetail());
                            address.setText(evalu.getAddress());
                            price.setText(evalu.getPrice());
                            car_label.setText(evalu.getCar_licnum());
                            stastu.setText(evalu.getStatus());
                            time.setText(evalu.getStart_time()+"-"+evalu.getEnd_time());
                        }
                    });
                    break;
                case 6666:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.closeProgress();
                            mToast.showText(getApplicationContext(),mmg);
                        }
                    });
                    break;
                case 9999:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.closeProgress();
                            showMessage.showMsg(EvalutionActivity.this,"网络连接异常，请检测网络连接");
                        }
                    });
                    break;
                default:
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evalution);
        Toolbar  toolbar= (Toolbar) findViewById(R.id.evalu_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        Intent data=getIntent();
        id=data.getStringExtra("Id");
        gson=new Gson();
        initView();
        initData();

    }

    private void initView() {
        name= (TextView) findViewById(R.id.evalu_name);
        lesson= (TextView) findViewById(R.id.eval_lesson);
        address= (TextView) findViewById(R.id.evalu_address);
        price= (TextView) findViewById(R.id.eval_price);
        car_label= (TextView) findViewById(R.id.evalu_car_label);
        stastu= (TextView) findViewById(R.id.eval_stastu);
        time= (TextView) findViewById(R.id.evalu_time);
        //
        eval_value= (TextView) findViewById(R.id.evalu_value);
        eval_content= (EditText) findViewById(R.id.eval_content);
        publishBtn= (Button) findViewById(R.id.evalu_publish);
        publishBtn.setOnClickListener(this);
        starBar= (StarBar) findViewById(R.id.starBar);
        starBar.setOnStarChangeListener(this);
    }

    private void initData() {
        handler.sendEmptyMessage(4444);
        new Thread(new Runnable() {
            @Override
            public void run() {
                params.clear();
                values.clear();
                params.add("key");
                params.add("id");
                values.add(student.studentKey(EvalutionActivity.this));
                values.add(id);
                sendRequest("GET",student.studentGetTrainRemarkInfo(),1,params,values);
            }
        }).start();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.evalu_publish:
                //预约评价
                    if(!eval_content.getText().toString().trim().equals("")){
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                remark=String.valueOf(mremark);
                                params.clear();
                                values.clear();
                                params.add("key");
                                params.add("source");
                                params.add("type");
                                params.add("evalobject_id");
                                params.add("evalsubjcontent");
                                params.add("overall");
                                params.add("teachlevel");
                                params.add("part");
                                params.add("task_id");
                                //
                                values.add(student.studentKey(EvalutionActivity.this));
                                values.add("2");
                                values.add("1");
                                values.add(evalu.getCoach_id());
                                values.add(evalu.getStart_time()+evalu.getEnd_time()+evalu.getCoachname()+evalu.getCar_licnum()+evalu.getDetail());
                                values.add(remark);
                                values.add(eval_content.getText().toString().trim());
                                values.add(evalu.getDetail());
                                values.add(evalu.getTask_id());
                                sendRequest("POST",student.studentTrainRemark(),2,params,values);
                            }
                        }).start();
                    }else {
                        handler.sendEmptyMessage(3333);
                    }
                //陪练评价
                break;
            case R.id.starBar:
                break;
            default:
                break;
        }
    }

    private void sendRequest(String Way, String url, int what, ArrayList<String> params, ArrayList<String> values) {
        RequestMethod method=null;
        if(Way.equals("POST")){
            method=RequestMethod.POST;
        }else if(Way.equals("GET")){
            method=RequestMethod.GET;
        }
        Request<JSONObject> request=NoHttp.createJsonObjectRequest(url,method);
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
                    mLog.e("评价","--->"+response.get().toString());
                    mmg=object.getString("msg");
                    if(what==1){
                        if(object.getString("code").equals("200")){
                            JSONObject obj=new JSONObject(object.getString("data"));
                            evalu=gson.fromJson(obj.toString(),Evalution.class);
                            handler.sendEmptyMessage(5555);
                        }else {
                            handler.sendEmptyMessage(6666);
                        }
                    }else if(what==2){
                        //陪练评价
                        handler.sendEmptyMessage(1111);
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
    public void onStarChange(float mark) {
        eval_value.setText(mark+"分");
        remark=String.valueOf(remark);
        mremark=(int)mark;
    }

}
