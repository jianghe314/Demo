package com.kangzhan.student.CompayManage.AccountManage;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.kangzhan.student.CompayManage.Bean.AccountSchoolList;
import com.kangzhan.student.CompayManage.Bean.MessageCountDetail;
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

public class MessageCountDetailActivity extends BaseActivity {
    private String Id,mmsg;
    private TextView time,mounts,cost;
    private Gson gson;
    private LinearLayout container;
    private MessageCountDetail mdata;
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
                            showProgress.showProgress(MessageCountDetailActivity.this,"加载中...");
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
                            showMessage.showMsg(MessageCountDetailActivity.this,"网络连接异常，请检查网络连接");
                        }
                    });
                    break;

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_count_detail);
        Toolbar toolbar= (Toolbar) findViewById(R.id.account_message_count_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        Intent getData=getIntent();
        Id=getData.getStringExtra("Id");
        gson=new Gson();
        initView();
        initData();
    }

    private void initView() {
        container= (LinearLayout) findViewById(R.id.compay_account_msg_count_container);
        time= (TextView) findViewById(R.id.compay_account_msg_count_time);
        mounts= (TextView) findViewById(R.id.compay_account_msg_count_mount);
        cost= (TextView) findViewById(R.id.compay_account_msg_count_money);
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
                values.add(CompayInterface.CompayKey(MessageCountDetailActivity.this));
                values.add(Id);
                sendRequest("GET",CompayInterface.AccountMsgDetail(),1,params,values);
            }
        }).start();
    }

    private void showContent() {
        time.setText(mdata.getSms_month());
        mounts.setText(mdata.getSuccess_counts());
        cost.setText(mdata.getAmount());
        for (int i = 0; i < mdata.getDetail().size(); i++) {
            LinearLayout liner=new LinearLayout(MessageCountDetailActivity.this);
            ViewGroup.LayoutParams lp=new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            liner.setLayoutParams(lp);
            liner.setOrientation(LinearLayout.HORIZONTAL);
            TextView time=new TextView(MessageCountDetailActivity.this);
            TextView name=new TextView(MessageCountDetailActivity.this);
            time.setTextSize(14f);
            name.setTextSize(14f);
            time.setPadding(20,10,50,10);
            name.setPadding(50,10,20,10);
            time.setText(mdata.getDetail().get(i).getSend_time());
            name.setText(mdata.getDetail().get(i).getReceiver_name());
            liner.addView(time);
            liner.addView(name);
            container.addView(liner);
        }



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
                    mLog.e("reponse","---->"+response.get().toString());
                    mmsg=object.getString("msg");
                    if(what==1){
                        if(object.getString("code").equals("200")){
                            mdata=gson.fromJson(object.getString("list"),MessageCountDetail.class);
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
