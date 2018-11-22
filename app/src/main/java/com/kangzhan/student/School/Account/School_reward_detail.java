package com.kangzhan.student.School.Account;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.kangzhan.student.Debug.mLog;
import com.kangzhan.student.R;
import com.kangzhan.student.School.Adapter.AccountRewardDetailAdapter;
import com.kangzhan.student.School.Bean.Account_reward_detail_Title;
import com.kangzhan.student.School.Bean.Account_reward_detail_content;
import com.kangzhan.student.School.Bean.EduLearnHours;
import com.kangzhan.student.ShowUI.mToast;
import com.kangzhan.student.ShowUI.showMessage;
import com.kangzhan.student.ShowUI.showProgress;
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


public class School_reward_detail extends BaseActivity {
    private TextView content;
    private ExpandableListView list;
    private String mmsg,mcontent;
    private RequestQueue requestQueue;
    private Gson gson;
    private AccountRewardDetailAdapter adapter;
    private ArrayList<Account_reward_detail_Title> parent=new ArrayList<>();        //组列表项，每个组都有一个子List
    private ArrayList<ArrayList<Account_reward_detail_content>> children=new ArrayList<>();
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
                            showProgress.showProgress(School_reward_detail.this,"加载中...");
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
                            showMessage.showMsg(School_reward_detail.this,"网络连接异常，请检查网络连接");
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
        setContentView(R.layout.activity_school_reward_detail);
        Toolbar toolbar= (Toolbar) findViewById(R.id.school_account_reward_detail_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        requestQueue=NoHttp.newRequestQueue();
        gson=new Gson();
        initView();
        initData();
    }

    private void initView() {
        content= (TextView) findViewById(R.id.school_account_reward_detail_content);
        list= (ExpandableListView) findViewById(R.id.school_reward_detail_expdList);
        adapter=new AccountRewardDetailAdapter(School_reward_detail.this,parent,children);
        list.setAdapter(adapter);
    }

    private void initData() {
        handler.sendEmptyMessage(1111);
        new Thread(new Runnable() {
            @Override
            public void run() {
                params.clear();
                values.clear();
                params.add("key");
                params.add("coach_id");
                params.add("bonus_id");
                values.add(school.schoolKey(School_reward_detail.this));
                values.add("17");
                values.add("5");
                sendRequest("GET",1, school.AccountRewardDetail(),params,values);
            }
        }).start();
    }
    private void showContent() {
        adapter.notifyDataSetChanged();
        content.setText(mcontent);
    }



    private void sendRequest(String way, int what, String url, ArrayList<String> params, ArrayList<String> values) {
        RequestMethod method=null;
        if(way.equals("POST")){
            method=RequestMethod.POST;
        }else if(way.equals("GET")){
            method=RequestMethod.GET;
        }
        Request<JSONObject> request= NoHttp.createJsonObjectRequest(url,method);
        for (int i = 0; i <params.size(); i++) {
            request.add(params.get(i),values.get(i));
        }
        requestQueue.add(what, request, new OnResponseListener<JSONObject>() {
            @Override
            public void onStart(int what) {

            }

            @Override
            public void onSucceed(int what, Response<JSONObject> response) {
                if(what==1){
                    try {
                        JSONObject objcet=new JSONObject(response.get().toString());
                        mLog.e("response","->"+response);
                        mmsg=objcet.getString("msg");
                        if(objcet.getString("code").equals("200")){
                            JSONObject bonus=new JSONObject(objcet.getString("bonus"));
                            JSONObject o=new JSONObject(bonus.getString("0"));
                            mcontent=o.getString("bonus_content");
                            JSONArray array=new JSONArray(bonus.getString("student_score"));
                            if(array.length()>0){
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject object1=new JSONObject(array.getJSONObject(i).toString());
                                    Account_reward_detail_Title title=new Account_reward_detail_Title();
                                    title.setStu_id(object1.getString("stu_id"));
                                    title.setExam_date(object1.getString("exam_date"));
                                    title.setStudent_name(object1.getString("student_name"));
                                    title.setExam_name(object1.getString("exam_name"));
                                    title.setAmount(object1.getString("amount"));
                                    title.setLength(object1.getString("length"));
                                    parent.add(title);
                                    JSONArray array2=new JSONArray(object1.getString("record"));
                                    ArrayList<Account_reward_detail_content> content=new ArrayList<Account_reward_detail_content>();
                                    if(array2.length()>0){
                                        for (int j = 0; j < array2.length(); j++) {
                                            Account_reward_detail_content m=gson.fromJson(array2.getJSONObject(j).toString(),Account_reward_detail_content.class);
                                            content.add(m);
                                        }
                                    }
                                    children.add(i,content);
                                }
                                handler.sendEmptyMessage(2222);
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

    @Override
    protected void onDestroy() {
        requestQueue.cancelAll();
        super.onDestroy();
    }
}
