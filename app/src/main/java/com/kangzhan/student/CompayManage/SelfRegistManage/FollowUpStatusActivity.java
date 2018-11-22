package com.kangzhan.student.CompayManage.SelfRegistManage;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.kangzhan.student.CompayManage.Bean.ChoiceClerk;
import com.kangzhan.student.CompayManage.Bean.SchoolFollowUp;
import com.kangzhan.student.CompayManage.Bean.StuFollowUp;
import com.kangzhan.student.CompayManage.Bean.TeaFollowUp;
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

public class FollowUpStatusActivity extends BaseActivity {
    //private TextView name,sex,phone;
    private EditText note;
    private Button addNote;
    private LinearLayout container;
    private String mmsg,who,Id;
    private Gson gson;
    private ArrayList<StuFollowUp> mdata=new ArrayList<>();
    private ArrayList<TeaFollowUp> teadata=new ArrayList<>();
    private ArrayList<SchoolFollowUp> schooldata=new ArrayList<>();
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
                            showProgress.showProgress(FollowUpStatusActivity.this,"加载中...");
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
                            showProgress.showProgress(FollowUpStatusActivity.this,"添加中...");
                        }
                    });
                    break;
                case 9999:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.closeProgress();
                            showMessage.showMsg(FollowUpStatusActivity.this,"网络连接异常，请检查网络连接");
                        }
                    });
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow_up_status);
        Toolbar toolbar= (Toolbar) findViewById(R.id.follow_up_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        Intent getData=getIntent();
        who=getData.getStringExtra("who");      //who 有student和teacher,school
        Id=getData.getStringExtra("Id");
        mLog.e("who--->",who);
        gson=new Gson();
        initView();
        initData();
    }

    private void initView() {
        /*name= (TextView) findViewById(R.id.follow_up_name);
        sex= (TextView) findViewById(R.id.follow_up_sex);
        phone= (TextView) findViewById(R.id.follow_up_phone);*/
        note= (EditText) findViewById(R.id.follow_up_note);
        container= (LinearLayout) findViewById(R.id.follow_up_container);
        addNote= (Button) findViewById(R.id.follow_up_add);
        addNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.sendEmptyMessage(4444);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if(who.equals("student")){
                            params.clear();
                            values.clear();
                            params.add("key");
                            params.add("inten_stu_id");
                            params.add("content");
                            values.add(CompayInterface.CompayKey(getApplicationContext()));
                            values.add(Id);
                            values.add(note.getText().toString().trim());
                            sendRequest("POST",CompayInterface.AddFollowUpInfo(),2,params,values);
                        }else if(who.equals("teacher")){
                            params.clear();
                            values.clear();
                            params.add("key");
                            params.add("inten_coach_id");
                            params.add("content");
                            values.add(CompayInterface.CompayKey(getApplicationContext()));
                            values.add(Id);
                            values.add(note.getText().toString().trim());
                            sendRequest("POST",CompayInterface.AddTeaFollowUpInfo(),2,params,values);
                        }else if(who.equals("school")){
                            params.clear();
                            values.clear();
                            params.add("key");
                            params.add("inten_inst_id");
                            params.add("content");
                            values.add(CompayInterface.CompayKey(getApplicationContext()));
                            values.add(Id);
                            values.add(note.getText().toString().trim());
                            sendRequest("POST",CompayInterface.AddSchoolFollowUpInfo(),2,params,values);
                        }

                    }
                }).start();
            }
        });
    }

    private void initData() {
        handler.sendEmptyMessage(1111);
        if(who.equals("student")){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    params.clear();
                    values.clear();
                    params.add("key");
                    params.add("inten_stu_id");
                    params.add("type");
                    values.add(CompayInterface.CompayKey(getApplicationContext()));
                    values.add(Id);
                    values.add("1");
                    sendRequest("GET",CompayInterface.SelfStuInfo(),1,params,values);
                }
            }).start();
        }else if(who.equals("teacher")){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    params.clear();
                    values.clear();
                    params.add("key");
                    params.add("inten_coach_id");
                    values.add(CompayInterface.CompayKey(getApplicationContext()));
                    values.add(Id);
                    sendRequest("GET",CompayInterface.SelfTeaInfo(),3,params,values);
                }
            }).start();
        }else  if(who.equals("school")){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    params.clear();
                    values.clear();
                    params.add("key");
                    params.add("inten_inst_id");
                    values.add(CompayInterface.CompayKey(getApplicationContext()));
                    values.add(Id);
                    sendRequest("GET",CompayInterface.SelfSchoolInfo(),4,params,values);
                }
            }).start();
        }

    }


    private void showContent() {
        if(who.equals("student")){
            for (int i = 0; i < mdata.size(); i++) {
                LinearLayout lp=new LinearLayout(FollowUpStatusActivity.this);
                lp.setOrientation(LinearLayout.HORIZONTAL);
                TextView time=new TextView(FollowUpStatusActivity.this);
                TextView clerk=new TextView(FollowUpStatusActivity.this);
                TextView content=new TextView(FollowUpStatusActivity.this);
                time.setPadding(40,15,40,15);
                clerk.setPadding(60,15,60,15);
                content.setPadding(100,15,40,15);
                content.setEllipsize(TextUtils.TruncateAt.END);
                content.setMaxLines(1);
                time.setText(mdata.get(i).getContract_time());
                clerk.setText(mdata.get(i).getClerk_id());
                content.setText(mdata.get(i).getContent());
                lp.addView(time);
                lp.addView(clerk);
                lp.addView(content);
                container.addView(lp);
            }
        }else if(who.equals("teacher")){
            for (int i = 0; i <teadata.size() ; i++) {
                LinearLayout lp=new LinearLayout(FollowUpStatusActivity.this);
                lp.setOrientation(LinearLayout.HORIZONTAL);
                TextView time=new TextView(FollowUpStatusActivity.this);
                TextView clerk=new TextView(FollowUpStatusActivity.this);
                TextView content=new TextView(FollowUpStatusActivity.this);
                time.setPadding(40,15,40,15);
                clerk.setPadding(60,15,60,15);
                content.setPadding(100,15,40,15);
                content.setEllipsize(TextUtils.TruncateAt.END);
                content.setMaxLines(1);
                time.setText(teadata.get(i).getContract_time());
                clerk.setText(teadata.get(i).getClerk_id());
                content.setText(teadata.get(i).getContent());
                lp.addView(time);
                lp.addView(clerk);
                lp.addView(content);
                container.addView(lp);
            }
        }else if(who.equals("school")){
            for (int i = 0; i < schooldata.size(); i++) {
                LinearLayout lp=new LinearLayout(FollowUpStatusActivity.this);
                lp.setOrientation(LinearLayout.HORIZONTAL);
                TextView time=new TextView(FollowUpStatusActivity.this);
                TextView clerk=new TextView(FollowUpStatusActivity.this);
                TextView content=new TextView(FollowUpStatusActivity.this);
                time.setPadding(40,15,40,15);
                clerk.setPadding(60,15,60,15);
                content.setPadding(100,15,40,15);
                content.setEllipsize(TextUtils.TruncateAt.END);
                content.setMaxLines(1);
                time.setText(schooldata.get(i).getContract_time());
                clerk.setText(schooldata.get(i).getClerk_id());
                content.setText(schooldata.get(i).getContent());
                lp.addView(time);
                lp.addView(clerk);
                lp.addView(content);
                container.addView(lp);
            }
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
                            JSONArray array=new JSONArray(object.getString("data"));
                            if(array.length()>0){
                                mdata.clear();
                                for (int i = 0; i < array.length(); i++) {
                                    StuFollowUp stu=gson.fromJson(array.getJSONObject(i).toString(),StuFollowUp.class);
                                    mdata.add(stu);
                                }
                                handler.sendEmptyMessage(3333);
                            }else {
                                handler.sendEmptyMessage(2222);
                            }
                        }else {
                            handler.sendEmptyMessage(2222);
                        }
                    }else if(what==2){
                        handler.sendEmptyMessage(2222);
                    }else if(what==3){
                        if(object.getString("code").equals("200")){
                            JSONArray array=new JSONArray(object.getString("data"));
                            if(array.length()>0){
                                teadata.clear();
                                for (int i = 0; i < array.length(); i++) {
                                    TeaFollowUp tea=gson.fromJson(array.getJSONObject(i).toString(),TeaFollowUp.class);
                                    teadata.add(tea);
                                }
                                handler.sendEmptyMessage(3333);
                            }else {
                                handler.sendEmptyMessage(2222);
                            }
                        }else {
                            handler.sendEmptyMessage(2222);
                        }
                    }else if(what==4){
                        if(object.getString("code").equals("200")){
                            JSONArray array=new JSONArray(object.getString("data"));
                            if(array.length()>0){
                                schooldata.clear();
                                for (int i = 0; i <array.length() ; i++) {
                                    SchoolFollowUp school=gson.fromJson(array.getJSONObject(i).toString(),SchoolFollowUp.class);
                                    schooldata.add(school);
                                }
                                handler.sendEmptyMessage(3333);
                            }else {
                                handler.sendEmptyMessage(2222);
                            }
                        }else {
                            handler.sendEmptyMessage(2222);
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
