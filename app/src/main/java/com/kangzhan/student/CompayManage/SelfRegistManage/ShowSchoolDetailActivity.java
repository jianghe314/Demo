package com.kangzhan.student.CompayManage.SelfRegistManage;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.kangzhan.student.CompayManage.Bean.SchoolDetail;
import com.kangzhan.student.CompayManage.Bean.TeaDetail;
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

public class ShowSchoolDetailActivity extends BaseActivity {
    private TextView code,name,person,phone,time,address,status;
    private Button look;
    private Gson gson;
    private String mmsg,schoolId;
    private SchoolDetail detail;
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
                            showProgress.showProgress(ShowSchoolDetailActivity.this,"加载中...");
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
                            showProgress.closeProgress();
                            showContent();
                        }
                    });
                    break;
                case 9999:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.closeProgress();
                            showMessage.showMsg(ShowSchoolDetailActivity.this,"网络连接异常，请检查网络连接");
                        }
                    });
                    break;
            }
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_school_detail);
        Toolbar toolbar= (Toolbar) findViewById(R.id.show_school_detail_toolbar);
        setSupportActionBar(toolbar);
        ActionBar action=getSupportActionBar();
        if(action!=null){
            action.setDisplayHomeAsUpEnabled(true);
        }
        Intent getData=getIntent();
        schoolId=getData.getStringExtra("Id");
        gson=new Gson();
        initView();
        initData();
    }



    private void initView() {
        code= (TextView) findViewById(R.id.compay_self_school_detail_code);
        name= (TextView) findViewById(R.id.compay_self_school_detail_name);
        person= (TextView) findViewById(R.id.compay_self_school_detail_person);
        phone= (TextView) findViewById(R.id.compay_self_school_detail_phone);
        time= (TextView) findViewById(R.id.compay_self_school_detail_time);
        address= (TextView) findViewById(R.id.compay_self_school_detail_address);
        status= (TextView) findViewById(R.id.compay_self_school_detail_car);
        look= (Button) findViewById(R.id.compay_show_school_detail_look);
        look.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent add=new Intent(ShowSchoolDetailActivity.this,FollowUpStatusActivity.class);
                add.putExtra("Id",detail.getId());
                add.putExtra("who","school");
                startActivity(add);
            }
        });
    }

    private void initData() {
        handler.sendEmptyMessage(1111);
        new Thread(new Runnable() {
            @Override
            public void run() {
                params.clear();
                values.clear();
                params.add("key");
                params.add("type");
                params.add("id");
                values.add(CompayInterface.CompayKey(getApplicationContext()));
                values.add("3");
                values.add(schoolId);
                sendRequest("GET",CompayInterface.SelfStuDetail(),1,params,values);
            }
        }).start();
    }


    private void showContent() {
        code.setText(detail.getId());
        name.setText(detail.getName());
        phone.setText(detail.getMobile());
        address.setText(detail.getProvince_id()+detail.getCity_id()+detail.getCounty_id());
        person.setText(detail.getReal_name());
        time.setText(detail.getSign_time());
        status.setText(detail.getProcess_flag());
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
                    mLog.e("reponse","-->"+object.toString());
                    mmsg=object.getString("msg");
                    if(object.getString("code").equals("200")){
                        JSONArray array=new JSONArray(object.getString("data"));
                        if(array.length()>0){
                            detail=gson.fromJson(array.getJSONObject(0).toString(),SchoolDetail.class);
                            handler.sendEmptyMessage(3333);
                        }
                    }else {
                        handler.sendEmptyMessage(2222);
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
