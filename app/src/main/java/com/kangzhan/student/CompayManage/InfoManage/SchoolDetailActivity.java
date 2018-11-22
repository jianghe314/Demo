package com.kangzhan.student.CompayManage.InfoManage;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.kangzhan.student.CompayManage.Bean.CompaySchoolDetail;
import com.kangzhan.student.CompayManage.Bean.InfoSchoolManage;
import com.kangzhan.student.CompayManage.Bean.SchoolDetail;
import com.kangzhan.student.Debug.mLog;
import com.kangzhan.student.R;
import com.kangzhan.student.ShowUI.mToast;
import com.kangzhan.student.ShowUI.showMessage;
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

public class SchoolDetailActivity extends BaseActivity {
    private TextView name,address,type,linker,phone,isSchool,isTea;
    private String Id,mmsg;
    private Gson gson;
    private ArrayList<String> params=new ArrayList<>();
    private ArrayList<String> values=new ArrayList<>();
    private CompaySchoolDetail detail;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1111:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.showProgress(SchoolDetailActivity.this,"加载中...");
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
                            showMessage.showMsg(SchoolDetailActivity.this,"网络连接异常，请检查网络连接");
                        }
                    });
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_detail);
        Toolbar toolbar= (Toolbar) findViewById(R.id.compay_schooldetail_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        gson=new Gson();
        Intent getData=getIntent();
        Id=getData.getStringExtra("Id");
        initView();
        initData();

    }
    private void initView() {
        name= (TextView) findViewById(R.id.item_compay_school_detail_name);
        address= (TextView) findViewById(R.id.item_compay_school_detail_address);
        type= (TextView) findViewById(R.id.item_compay_school_detail_type);
        linker= (TextView) findViewById(R.id.item_compay_school_detail_person);
        phone= (TextView) findViewById(R.id.item_compay_school_detail_phone);
        isSchool= (TextView) findViewById(R.id.item_compay_school_detail_school);
        isTea= (TextView) findViewById(R.id.item_compay_school_detail_teacher);
    }
    private void showContent() {
        name.setText(detail.getInsti_name());
        if(detail.getAddress()!=null){
            address.setText(detail.getAddress());
        }
        if(detail.getLevel_name()!=null){
            type.setText(detail.getLevel_name());
        }
        if(detail.getPhone()!=null){
            phone.setText(detail.getPhone());
        }
        if(detail.getContact()!=null){
            linker.setText(detail.getContact());
        }
        if(detail.getCross_inst()!=null){
            isSchool.setText(detail.getCross_inst());
        }
        if(detail.getCross_coach()!=null){
            isTea.setText(detail.getCross_coach());
        }
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
                values.add(Id);
                sendRequest("GET",CompayInterface.InfoSchoolDetail(),1,params,values);
            }
        }).start();
    }


    private void sendRequest(String Way, String url, int what, ArrayList<String> params, ArrayList<String> values) {
        RequestMethod method=null;
        if(Way.equals("POST")){
            method=RequestMethod.POST;
        }else if(Way.equals("GET")){
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
                    mLog.e("reponse","-->"+response.get().toString());
                    mmsg=object.getString("msg");
                    if(what==1){
                        if(object.getString("code").equals("200")){
                            detail=gson.fromJson(object.getString("institution"),CompaySchoolDetail.class);
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
