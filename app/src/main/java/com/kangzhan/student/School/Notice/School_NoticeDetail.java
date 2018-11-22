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
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.kangzhan.student.CompayManage.Bean.CompayNoticeDetail;
import com.kangzhan.student.Debug.mLog;
import com.kangzhan.student.R;
import com.kangzhan.student.School.Bean.SchoolMsgDeatil;
import com.kangzhan.student.School.Bean.SchoolNoticeDetail;
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

public class School_NoticeDetail extends BaseActivity {
    private String Id,mmsg,who;
    private TextView title,createT,sendT,content,status;
    private mLableLayout container;
    private Gson gson;
    private SchoolNoticeDetail sdetail;
    private CompayNoticeDetail cdetail;
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
                            showProgress.showProgress(School_NoticeDetail.this,"加载中...");
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
                            showMessage.showMsg(School_NoticeDetail.this,"网络连接异常，请检测网络连接");
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
        setContentView(R.layout.activity_school__notice_detail);
        Toolbar toolbar= (Toolbar) findViewById(R.id.school_notice_detail_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        gson=new Gson();
        Intent getData=getIntent();
        Id=getData.getStringExtra("Id");
        who=getData.getStringExtra("who");
        initView();
        initData();
    }

    private void initView() {
        container= (mLableLayout) findViewById(R.id.school_news_sendNotice_toMan);
        status= (TextView) findViewById(R.id.school_notice_detail_stastu);
        title= (TextView) findViewById(R.id.school_notice_detail_title);
        createT= (TextView) findViewById(R.id.school_notice_detail_recordTime);
        sendT= (TextView) findViewById(R.id.school_notice_detail_sendT);
        content= (TextView) findViewById(R.id.school_notice_detail_content);
    }

    private void initData() {
        handler.sendEmptyMessage(1111);
        if(who.equals("School")){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    params.clear();
                    values.clear();
                    params.add("key");
                    params.add("push_id");
                    values.add(school.schoolKey(getApplicationContext()));
                    values.add(Id);
                    sendRequest("POST",school.SchoolNoticeDetail(),1,params,values);
                }
            }).start();
        }else if(who.equals("Compay")) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    params.clear();
                    values.clear();
                    params.add("key");
                    params.add("push_id");
                    values.add(CompayInterface.CompayKey(School_NoticeDetail.this));
                    values.add(Id);
                    sendRequest("POST", CompayInterface.CompayNoticeDetail(),1,params,values);
                }
            }).start();
        }

    }

    private void showContent() {
        if(who.equals("Compay")){
            String[] phones=cdetail.getReceiver_name().split(",");
            LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.setMargins(5,5,5,5);
            for (int i = 0; i < phones.length; i++) {
                TextView txt=new TextView(this);
                txt.setText(phones[i]+"");
                txt.setTextColor(ContextCompat.getColor(this,R.color.textColor_black));
                txt.setBackgroundResource(R.drawable.sendmsgbackground);
                container.addView(txt,lp);
            }
            title.setText(cdetail.getSummary());
            status.setText(cdetail.getPush_status());
            createT.setText(cdetail.getCreate_time());
            sendT.setText(cdetail.getSend_time());
            content.setText(cdetail.getContent());
        }else if(who.equals("School")){
            String[] phones=sdetail.getReceiver_name().split(",");
            LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.setMargins(5,5,5,5);
            for (int i = 0; i < phones.length; i++) {
                TextView txt=new TextView(this);
                txt.setText(phones[i]+"");
                txt.setTextColor(ContextCompat.getColor(this,R.color.textColor_black));
                txt.setBackgroundResource(R.drawable.sendmsgbackground);
                container.addView(txt,lp);
            }
            title.setText(sdetail.getSummary());
            status.setText(sdetail.getPush_status());
            createT.setText(sdetail.getCreate_time());
            sendT.setText(sdetail.getSend_time());
            content.setText(sdetail.getContent());
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
                    mLog.e("reponse","->"+response.get().toString());
                    mmsg=object.getString("msg");
                    if(object.getString("code").equals("200")){
                        JSONArray array=new JSONArray(object.getString("data"));
                        if(who.equals("Compay")){
                            cdetail=gson.fromJson(array.getJSONObject(0).toString(),CompayNoticeDetail.class);
                        }else if(who.equals("School")){
                            sdetail=gson.fromJson(array.getJSONObject(0).toString(),SchoolNoticeDetail.class);
                        }
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
