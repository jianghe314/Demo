package com.kangzhan.student.School.Notice;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.kangzhan.student.Debug.mLog;
import com.kangzhan.student.R;
import com.kangzhan.student.School.Bean.SchoolAdvise;
import com.kangzhan.student.School.Bean.SchoolAdviseDetail;
import com.kangzhan.student.ShowUI.mToast;
import com.kangzhan.student.ShowUI.showMessage;
import com.kangzhan.student.ShowUI.showProgress;
import com.kangzhan.student.Student.bean.Notice;
import com.kangzhan.student.com.BaseActivity;
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

public class School_Advise_detail extends BaseActivity implements View.OnClickListener{
    private String Id,mmsg;
    private TextView title,content,time,replyContent;
    private Button dealBtn;
    private CardView container;
    private SchoolAdviseDetail detail;
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
                            showProgress.showProgress(School_Advise_detail.this,"加载中...");
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
                case 4444:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.showProgress(School_Advise_detail.this,"关闭中...");
                        }
                    });
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
                case 9999:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.closeProgress();
                            showMessage.showMsg(School_Advise_detail.this,"网络连接异常，请检查网络连接");
                        }
                    });
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school__advise_detail);
        Toolbar toolbar= (Toolbar) findViewById(R.id.school_news_advise_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        Intent getData=getIntent();
        Id=getData.getStringExtra("Id");
        initView();
        initData();
    }

    private void initView() {
        title= (TextView) findViewById(R.id.school_advise_detail_title);
        content= (TextView) findViewById(R.id.school_advise_detail_content);
        time= (TextView) findViewById(R.id.school_advise_detail_time);
        replyContent= (TextView) findViewById(R.id.school_advise_detail_reply);
        dealBtn= (Button) findViewById(R.id.school_advise_detail_deal);
        container= (CardView) findViewById(R.id.school_advise_detail_container);
        dealBtn.setOnClickListener(this);
    }

    private void initData() {
        handler.sendEmptyMessage(1111);
        new Thread(new Runnable() {
            @Override
            public void run() {
                params.clear();
                values.clear();
                params.add("key");
                params.add("ticketid");
                values.add(school.schoolKey(getApplicationContext()));
                values.add(Id);
                sendRequest("GET",school.SchoolAdviseDetail(),1,params,values);
            }
        }).start();
    }


    private void showContent() {
        title.setText(detail.getSummary());
        content.setText(detail.getDescription());
        if(detail.getReply_record().size()>0){
            container.setVisibility(View.VISIBLE);
            time.setText("回复时间："+detail.getReply_record().get(0).getReply_time()+" "+detail.getReply_record().get(0).getReply_name());
            replyContent.setText(detail.getReply_record().get(0).getContent());
            replyContent.setEnabled(false);
        }else {
            container.setVisibility(View.GONE);
            time.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.school_advise_detail_deal:
                //关闭工单
                handler.sendEmptyMessage(4444);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        params.clear();
                        values.clear();
                        params.add("key");
                        params.add("ticketid");
                        params.add("close_type");
                        values.add(school.schoolKey(School_Advise_detail.this));
                        values.add(Id);
                        values.add("30");
                        sendRequest("POST",school.SchoolAdviseClose(),2,params,values);
                    }
                }).start();
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
                    mLog.e("repose","->"+response.toString());
                    mmsg=object.getString("msg");
                    if(what==1){
                        if(object.getString("code").equals("200")){
                            Gson gson=new Gson();
                            JSONArray array=new JSONArray(object.getString("data"));
                            detail=gson.fromJson(array.getJSONObject(0).toString(),SchoolAdviseDetail.class);
                            handler.sendEmptyMessage(2222);
                        }else {
                            handler.sendEmptyMessage(3333);
                        }
                    }else if(what==2){
                        handler.sendEmptyMessage(5555);
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
