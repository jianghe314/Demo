package com.kangzhan.student.School.Notice;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.kangzhan.student.R;
import com.kangzhan.student.School.Bean.SchoolMsg;
import com.kangzhan.student.School.Bean.SchoolMsgDeatil;
import com.kangzhan.student.ShowUI.mToast;
import com.kangzhan.student.ShowUI.showMessage;
import com.kangzhan.student.ShowUI.showProgress;
import com.kangzhan.student.com.BaseActivity;
import com.kangzhan.student.mInterface.CompayInterface.CompayInterface;
import com.kangzhan.student.mInterface.SchoolInterface.school;
import com.kangzhan.student.mUI.mLableLayout;
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

public class School_Notice_MsgDetail extends BaseActivity {
    private TextView time,status,content;
    private mLableLayout container;
    private String mmsg,Id,who;
    private Gson gson;
    private SchoolMsgDeatil detail;
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
                            showProgress.showProgress(School_Notice_MsgDetail.this,"加载中...");
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
                            showMessage.showMsg(School_Notice_MsgDetail.this,"网络连接异常，请检测网络连接");
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
        setContentView(R.layout.activity_school__notice__msg_detail);
        Toolbar toolbar= (Toolbar) findViewById(R.id.school_notice_msg_detail_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        Intent getData=getIntent();
        Id=getData.getStringExtra("Id");
        who=getData.getStringExtra("who");
        gson=new Gson();
        initView();
        initData();
    }

    private void initView() {
        time= (TextView) findViewById(R.id.school_notice_msg_detail_time);
        status= (TextView) findViewById(R.id.school_notice_msg_detail_status);
        content= (TextView) findViewById(R.id.school_notice_msg_detail_content);
        container= (mLableLayout) findViewById(R.id.school_notice_msg_detail_container);
    }

    private void initData() {
        handler.sendEmptyMessage(1111);
        if(who.equals("school")){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    params.clear();
                    values.clear();
                    params.add("key");
                    params.add("sms_id");
                    values.add(school.schoolKey(getApplicationContext()));
                    values.add(Id);
                    sendRequest("POST",school.SchoolMsgDeatil(),1,params,values);
                }
            }).start();
        }else if(who.equals("compay")){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    params.clear();
                    values.clear();
                    params.add("key");
                    params.add("sms_id");
                    values.add(CompayInterface.CompayKey(School_Notice_MsgDetail.this));
                    values.add(Id);
                    sendRequest("POST",CompayInterface.CompayMsgDetail(),1,params,values);
                }
            }).start();
        }

    }

    private void showContent() {
        String[] phones=detail.getReceiver_phone().split(",");
        LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(5,5,5,5);
        for (int i = 0; i < phones.length; i++) {
            TextView txt=new TextView(this);
            txt.setText(phones[i]+"");
            txt.setTextColor(ContextCompat.getColor(this,R.color.textColor_black));
            txt.setBackgroundResource(R.drawable.sendmsgbackground);
            container.addView(txt,lp);
        }
        time.setText(detail.getSend_time());
        status.setText(detail.getSms_status());
        content.setText("正文："+detail.getContent());
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
                    Log.e("reponse","->"+response.get().toString());
                    mmsg=object.getString("msg");
                    if(object.getString("code").equals("200")){
                        JSONArray array=new JSONArray(object.getString("data"));
                        detail=gson.fromJson(array.getJSONObject(0).toString(),SchoolMsgDeatil.class);
                        handler.sendEmptyMessage(2222);
                    }else {
                        handler.sendEmptyMessage(3333);
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
