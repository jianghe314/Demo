package com.kangzhan.student.School.Edu;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ExpandableListView;

import com.google.gson.Gson;
import com.kangzhan.student.Debug.mLog;
import com.kangzhan.student.R;
import com.kangzhan.student.School.Adapter.SchoolRewardDetailAdapter;
import com.kangzhan.student.ShowUI.mToast;
import com.kangzhan.student.ShowUI.showMessage;
import com.kangzhan.student.ShowUI.showProgress;
import com.kangzhan.student.Teacher.bean.ExpandChild;
import com.kangzhan.student.Teacher.bean.ExpandReward;
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

public class Edu_teacher_reward extends BaseActivity {
    private ExpandableListView expand;
    private ArrayList<ExpandReward> mparent=new ArrayList<>();        //组列表项，每个组都有一个子List
    private ArrayList<ArrayList<ExpandChild>> child=new ArrayList<>();     //子列表项
    private SchoolRewardDetailAdapter adapter;
    private Gson gson;
    private String Id,mmsg;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1111:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.showProgress(Edu_teacher_reward.this,"加载中...");
                        }
                    });
                    break;
                case 2222:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.closeProgress();
                            adapter.notifyDataSetChanged();
                        }
                    });
                    break;
                case 3333:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.closeProgress();
                            mToast.showText(getApplicationContext(),"当前没有数据！");
                        }
                    });
                    break;
                case 9999:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.closeProgress();
                            showMessage.showMsg(Edu_teacher_reward.this,"网络连接异常，请检查网络连接");
                        }
                    });
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edu_teacher_reward);
        Toolbar toolbar= (Toolbar) findViewById(R.id.school_teacher_reward);
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
        adapter=new SchoolRewardDetailAdapter(Edu_teacher_reward.this,mparent,child);
        expand= (ExpandableListView) findViewById(R.id.school_teacher_reward_detail);
        expand.setAdapter(adapter);
    }
    private void initData() {
        handler.sendEmptyMessage(1111);
        new Thread(new Runnable() {
            @Override
            public void run() {
                sendRequest();
            }
        }).start();
    }

    private void sendRequest() {
        Request<JSONObject> request= NoHttp.createJsonObjectRequest(school.EduTeacherDetail(), RequestMethod.GET);
        request.add("key",school.schoolKey(getApplicationContext()));
        request.add("coach_id",Id);
        getRequestQueue().add(1, request, new OnResponseListener<JSONObject>() {
            @Override
            public void onStart(int what) {

            }

            @Override
            public void onSucceed(int what, Response<JSONObject> response) {
                try {
                    JSONObject object=new JSONObject(response.get().toString());
                    mLog.e("reponse","-->"+response.get().toString());
                    mmsg=object.getString("msg");
                    if(object.getString("code").equals("200")){
                        JSONArray array=new JSONArray(object.getString("bonus"));
                        if(array.length()>0){
                            for (int i = 0; i < array.length(); i++) {
                                JSONArray array2=new JSONArray(array.getJSONObject(i).getString("student_score"));
                                if(array2.length()>0){
                                    for (int j = 0; j <array2.length(); j++) {
                                        JSONObject object2=new JSONObject(array2.getJSONObject(j).toString());
                                        ExpandReward record1=new ExpandReward();
                                        record1.setId(object2.getString("stu_id"));
                                        record1.setExam_date(object2.getString("exam_date"));
                                        record1.setStudent_name(object2.getString("student_name"));
                                        record1.setExam_name(object2.getString("exam_name"));
                                        record1.setAmount(object2.getString("amount"));
                                        mparent.add(record1);
                                        JSONArray array3=new JSONArray(object2.getString("record"));
                                        ArrayList<ExpandChild> childdata=new ArrayList<ExpandChild>();
                                        if(array3.length()>0){
                                            for (int k = 0; k <array3.length(); k++) {
                                                ExpandChild record2=gson.fromJson(array3.getJSONObject(k).toString(),ExpandChild.class);
                                                childdata.add(record2);
                                            }
                                        }
                                        child.add(j,childdata);
                                        handler.sendEmptyMessage(2222);
                                    }
                                }else {
                                    handler.sendEmptyMessage(3333);
                                }
                            }
                        }else {
                            handler.sendEmptyMessage(3333);
                        }
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
