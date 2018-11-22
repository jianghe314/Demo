package com.kangzhan.student.CompayManage.SelfRegistManage;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.kangzhan.student.CompayManage.Bean.AdviseDetail;
import com.kangzhan.student.CompayManage.Bean.AdviseManage;
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

public class AdviseDetailActivity extends BaseActivity implements View.OnClickListener{
    private TextView title,time,person,school,problem,replyStatus;
    private LinearLayout replayContainer;
    private RelativeLayout proContainer;
    private EditText replyContent;
    private Button reply;
    private AdviseDetail detail;
    private String mmsg,Id,status;
    private Gson gson;
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
                            showProgress.showProgress(AdviseDetailActivity.this,"加载中...");
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
                case 4444:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.showProgress(AdviseDetailActivity.this,"回复中...");
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
                            showMessage.showMsg(AdviseDetailActivity.this,"网络连接异常，请检查网络连接");
                        }
                    });
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compay_advise_detail);
        Toolbar toolbar= (Toolbar) findViewById(R.id.compay_self_regist_adviseDetail_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        Intent getData=getIntent();
        Id=getData.getStringExtra("Id");
        status=getData.getStringExtra("status");
        gson=new Gson();
        initView();
        initData();
    }

    private void initView() {
        title= (TextView) findViewById(R.id.item_compay_advise_title);
        time= (TextView) findViewById(R.id.item_compay_advise_time);
        person= (TextView) findViewById(R.id.item_compay_advise_person);
        school= (TextView) findViewById(R.id.item_compay_advise_school);
        problem= (TextView) findViewById(R.id.item_compay_advise_problem);
        replyStatus= (TextView) findViewById(R.id.item_compay_advise_reply_status);
        replyContent= (EditText) findViewById(R.id.item_compay_advise_reply_problem);
        replayContainer= (LinearLayout) findViewById(R.id.item_compay_advise_reply_container);
        proContainer= (RelativeLayout) findViewById(R.id.item_compay_advise_reply_problem_container);
        reply= (Button) findViewById(R.id.item_compay_advise_reply_btn);
        reply.setOnClickListener(this);
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
                sendRequest("GET",CompayInterface.AdviseDetail(),1,params,values);
            }
        }).start();
    }

    private void showContent() {
        title.setText(detail.getSummary());
        time.setText(detail.getCreate_time());
        person.setText(detail.getSender_name());
        school.setText(detail.getInst_name());
        problem.setText(detail.getDescription());
        replyStatus.setText(detail.getStatus_name());
        if(detail.getTicketreplay().size()>0){
            //处理
            replayContainer.setVisibility(View.VISIBLE);
            for (int i = 0; i <detail.getTicketreplay().size(); i++) {
                TextView time=new TextView(AdviseDetailActivity.this);
                TextView content=new TextView(AdviseDetailActivity.this);
                time.setPadding(30,20,20,20);
                content.setPadding(30,20,20,20);
                time.setText("回复时间："+detail.getTicketreplay().get(i).getReply_time());
                View liner=new View(AdviseDetailActivity.this);
                liner.setBackgroundColor(ContextCompat.getColor(this,R.color.color_line));
                ViewGroup.LayoutParams lp=new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,3);
                liner.setLayoutParams(lp);
                content.setText("回复内容："+detail.getTicketreplay().get(i).getContent());
                replayContainer.addView(time);
                replayContainer.addView(content);
                replayContainer.addView(liner);
            }
            if(detail.getStatus_name().equals("已处理")){
                reply.setVisibility(View.GONE);
                proContainer.setVisibility(View.GONE);
            }else {
                reply.setVisibility(View.VISIBLE);
                proContainer.setVisibility(View.VISIBLE);
                reply.setVisibility(View.VISIBLE);
            }

        }else {
            //没有处理
            replayContainer.setVisibility(View.GONE);
            proContainer.setVisibility(View.VISIBLE);
            reply.setVisibility(View.VISIBLE);

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.item_compay_advise_reply_btn:
                //回复按钮
                if(replyContent.getText().toString().trim().equals("")){
                    mToast.showText(getApplicationContext(),"回复内容不能为空");
                }else {
                    handler.sendEmptyMessage(4444);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            params.clear();
                            values.clear();
                            params.add("key");
                            params.add("id");
                            params.add("content");
                            values.add(CompayInterface.CompayKey(getApplicationContext()));
                            values.add(Id);
                            values.add(replyContent.getText().toString().trim());
                            sendRequest("POST",CompayInterface.AdviseReplay(),2,params,values);
                        }
                    }).start();
                }
                break;
        }
    }


    private void sendRequest(String Way,String url,int what,ArrayList<String> params,ArrayList<String> values) {
        RequestMethod method=null;
        if(Way.equals("POST")){
            method=RequestMethod.POST;
        }else if(Way.equals("GET")){
            method=RequestMethod.GET;
        }
        Request<JSONObject> request= NoHttp.createJsonObjectRequest(url,method);
        for (int i = 0; i <params.size(); i++) {
            request.add(params.get(i),values.get(i));
            //mLog.e("params--values",params.get(i)+"--"+values.get(i));
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
                            if(object.getString("code").equals("200")){
                                detail=gson.fromJson(object.getString("data"),AdviseDetail.class);
                                handler.sendEmptyMessage(3333);
                            }else {
                                handler.sendEmptyMessage(2222);
                            }
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
