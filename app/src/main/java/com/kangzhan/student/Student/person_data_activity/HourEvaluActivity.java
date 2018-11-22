package com.kangzhan.student.Student.person_data_activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.kangzhan.student.Debug.mLog;
import com.kangzhan.student.R;
import com.kangzhan.student.ShowUI.mToast;
import com.kangzhan.student.ShowUI.showMessage;
import com.kangzhan.student.ShowUI.showProgress;
import com.kangzhan.student.Student.Train.EvalutionActivity;
import com.kangzhan.student.Student.bean.Pay_item_booking;
import com.kangzhan.student.Student.mutils.StarBar;
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

public class HourEvaluActivity extends BaseActivity implements View.OnClickListener,StarBar.OnStarChangeListener{
    private TextView name,lesson,address,price,carLable,stastus,time,score;
    private StarBar starBar;
    private EditText content;
    private Button send;
    private String id,mmsg,mremark;
    private Gson gson;
    private Pay_item_booking hourItem;
    private ArrayList<String> params=new ArrayList<>();
    private ArrayList<String> valuse=new ArrayList<>();
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0000:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.showProgress(HourEvaluActivity.this,"加载中...");
                        }
                    });
                    break;
                case 1111:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.closeProgress();
                            name.setText(hourItem.getCoach_name());
                            lesson.setText(hourItem.getStage());
                            address.setText(hourItem.getAddress());
                            price.setText(hourItem.getAmount()+"元");
                            carLable.setText(hourItem.getCar_id());
                            stastus.setText(hourItem.getStatus());
                            time.setText(hourItem.getSdate()+" "+hourItem.getStarttime()+" "+hourItem.getEndtime());
                        }
                    });
                    break;
                case 2222:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.closeProgress();
                            mToast.showText(getApplicationContext(),mmsg);
                        }
                    });
                    break;
                case 3333:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.showProgress(HourEvaluActivity.this,"发布中...");
                        }
                    });
                    break;
                case 4444:
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
                            showMessage.showMsg(HourEvaluActivity.this,"网络连接异常，请检测网络连接");
                        }
                    });
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hour_evalu);
        Toolbar toolbar= (Toolbar) findViewById(R.id.hour_evalu_toolbar);
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
        name= (TextView) findViewById(R.id.hour_evalu_name);
        lesson= (TextView) findViewById(R.id.hour_eval_lesson);
        address= (TextView) findViewById(R.id.hour_evalu_address);
        price= (TextView) findViewById(R.id.hour_eval_price);
        carLable= (TextView) findViewById(R.id.hour_evalu_car_label);
        stastus= (TextView) findViewById(R.id.hour_eval_stastu);
        time= (TextView) findViewById(R.id.hour_evalu_time);
        starBar= (StarBar) findViewById(R.id.hour_starBar);
        starBar.setOnStarChangeListener(this);
        score= (TextView) findViewById(R.id.hour_evalu_value);
        content= (EditText) findViewById(R.id.hour_eval_content);
        send= (Button) findViewById(R.id.hour_evalu_publish);
        send.setOnClickListener(this);
    }

    private void initData() {
        params.add("key");
        params.add("hour_id");
        valuse.add(student.studentKey(HourEvaluActivity.this));
        valuse.add(id);
        handler.sendEmptyMessage(0000);
        new Thread(new Runnable() {
            @Override
            public void run() {
                sendReqeust("GET",1, student.studentDetail(),params,valuse);
            }
        }).start();
    }

    private void sendReqeust(String ways, int what,String url, ArrayList<String> params,ArrayList<String> values) {
        RequestMethod method=null;
        if(ways.equals("GET")){
            method=RequestMethod.GET;
        }else if(ways.equals("POST")){
            method=RequestMethod.POST;
        }
        Request<JSONObject> request=NoHttp.createJsonObjectRequest(url,method);
        for (int i = 0; i < params.size(); i++) {
            request.add(params.get(i),values.get(i));
        }
        getRequestQueue().add(what, request, new OnResponseListener<JSONObject>() {
            @Override
            public void onStart(int what) {

            }

            @Override
            public void onSucceed(int what, Response<JSONObject> response) {
                mLog.i("response","->"+response.get().toString());
                try {
                    JSONObject obejct=new JSONObject(response.get().toString());
                    mmsg=obejct.getString("msg");
                    switch (what){
                        case 1:
                            if(obejct.getString("code").equals("200")){
                                JSONArray array=new JSONArray(obejct.getString("data"));
                                if(array.length()>0){
                                   hourItem=gson.fromJson(array.getJSONObject(0).toString(),Pay_item_booking.class);
                                }
                                handler.sendEmptyMessage(1111);
                            }else {
                                handler.sendEmptyMessage(2222);
                            }

                            break;
                        case 2:
                            handler.sendEmptyMessage(4444);
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(int what, Response<JSONObject> response) {

            }

            @Override
            public void onFinish(int what) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.hour_evalu_publish:
                if(!score.getText().toString().trim().equals("0.0分")){
                    if(content.getText().toString().trim().equals("")){
                        mToast.showText(getApplicationContext(),"评价内容不能为空");
                    }else {
                        handler.sendEmptyMessage(3333);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                params.clear();
                                valuse.clear();
                                params.add("key");
                                params.add("rid");
                                params.add("evalobject_id");
                                params.add("soure");
                                params.add("type");
                                params.add("evalsubjcontent");
                                params.add("overall");
                                params.add("teachlevel");
                                //
                                valuse.add(student.studentKey(HourEvaluActivity.this));
                                valuse.add(hourItem.getRid());
                                valuse.add(hourItem.getCoach_id());
                                valuse.add("1");
                                valuse.add("1");
                                valuse.add(hourItem.getSdate()+hourItem.getStarttime()+hourItem.getEndtime()+hourItem.getCoach_name()+hourItem.getCar_id()+hourItem.getStage());
                                valuse.add(mremark);
                                valuse.add(content.getText().toString().trim());
                                sendReqeust("POST",2,student.studentTrainRemark(),params,valuse);
                            }
                        }).start();
                    }
                }else {
                    mToast.showText(getApplicationContext(),"评分不能为空");
                }
                break;

        }
    }

    @Override
    public void onStarChange(float mark) {
        score.setText(mark+"分");
        mremark=String.valueOf(mark);
    }

}
