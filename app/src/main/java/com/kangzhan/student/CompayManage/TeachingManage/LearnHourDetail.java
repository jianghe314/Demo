package com.kangzhan.student.CompayManage.TeachingManage;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import com.google.gson.Gson;
import com.kangzhan.student.CompayManage.Bean.CompayLearnHour;
import com.kangzhan.student.CompayManage.Bean.mLearnHourDetail;
import com.kangzhan.student.R;
import com.kangzhan.student.ShowUI.mToast;
import com.kangzhan.student.ShowUI.showProgress;
import com.kangzhan.student.com.BaseActivity;
import com.kangzhan.student.mInterface.CompayInterface.CompayInterface;
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

public class LearnHourDetail extends BaseActivity {
    private TextView name,clerk,school,Id,code,trainT,time,address,teacher,car,type,status,subject;
    private String mId,mmsg;
    private Gson gson;
    private ArrayList<String> params=new ArrayList<>();
    private ArrayList<String> values=new ArrayList<>();
    private mLearnHourDetail detail;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1111:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.showProgress(LearnHourDetail.this,"加载中...");
                        }
                    });
                    break;
                case 2222:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.closeProgress();
                            showContent();
                        }
                    });
                    break;
                case 3333:
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
                            showProgress.showProgress(LearnHourDetail.this,"网络连接异常，请检查网络连接");
                        }
                    });
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn_hour_detail);
        Toolbar toolbar= (Toolbar) findViewById(R.id.compay_teaching_lhDetail_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        gson=new Gson();
        Intent getData=getIntent();
        mId=getData.getStringExtra("Id");       //学时Id
        initView();
        initData();
    }

    private void initView() {
        name= (TextView) findViewById(R.id.compay_learnH_name);
        clerk= (TextView) findViewById(R.id.compay_learnH_clerk);
        school= (TextView) findViewById(R.id.compay_learnH_school);
        Id= (TextView) findViewById(R.id.compay_learnH_Id);
        code= (TextView) findViewById(R.id.compay_learnH_code);
        trainT= (TextView) findViewById(R.id.compay_learnH_trainT);
        time= (TextView) findViewById(R.id.compay_learnH_time);
        address= (TextView) findViewById(R.id.compay_learnH_address);
        teacher= (TextView) findViewById(R.id.compay_learnH_teacher);
        car= (TextView) findViewById(R.id.compay_learnH_car);
        type= (TextView) findViewById(R.id.compay_learnH_type);
        status= (TextView) findViewById(R.id.compay_learnH_status);
    }

    private void initData() {
        handler.sendEmptyMessage(1111);
        new Thread(new Runnable() {
            @Override
            public void run() {
                params.clear();
                values.clear();
                params.add("key");
                params.add("id");
                values.add(CompayInterface.CompayKey(getApplicationContext()));
                values.add(mId);
                sendRequest("GET",CompayInterface.LearnHourDetail(),1,params,values);
            }
        }).start();
    }

    private void showContent() {
        name.setText(detail.getName());
        clerk.setText(detail.getClerk_id());
        school.setText(detail.getInstname());
        Id.setText(detail.getIdcard());
        code.setText(detail.getDevice_id());
        trainT.setText(detail.getSdate()+" "+detail.getStarttime()+" "+detail.getEndtime());
        time.setText(detail.getDuration()+" 小时");
        address.setText(detail.getRegion_id());
        teacher.setText(detail.getCoach_id());
        car.setText(detail.getCar_id());
        type.setText(detail.getType());
        status.setText(detail.getSynchro_flag());
    }

    private void sendRequest(String Way,String url,int what,ArrayList<String> params,ArrayList<String> values) {
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
                    Log.e("reponse","-->"+response.get().toString());
                    mmsg=object.getString("msg");
                    if(what==1) {
                        if(object.getString("code").equals("200")){
                            JSONArray array=new JSONArray(object.getString("data"));
                            detail=gson.fromJson(array.getJSONObject(0).toString(),mLearnHourDetail.class);
                            handler.sendEmptyMessage(2222);
                        }else {
                            handler.sendEmptyMessage(3333);
                        }
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
}
