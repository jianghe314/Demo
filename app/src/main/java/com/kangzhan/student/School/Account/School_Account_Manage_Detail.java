package com.kangzhan.student.School.Account;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.kangzhan.student.Debug.mLog;
import com.kangzhan.student.R;
import com.kangzhan.student.School.Adapter.AccountAffirmDetailAdapter;
import com.kangzhan.student.School.Bean.AccountAffirm;
import com.kangzhan.student.School.Bean.AccountAffirmdetail_Child;
import com.kangzhan.student.School.Bean.AccountAffirmdetail_Parent;
import com.kangzhan.student.School.Bean.EduLearnHours;
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

public class School_Account_Manage_Detail extends BaseActivity{
    private TextView time,hour,price,draw,attachH,attachP,attachdraw,status;
    private ExpandableListView list;
    private String Id,mmsg;
    private RequestQueue requestQueue;
    private Gson gson;
    private AccountAffirmDetailAdapter adapter;
    private AccountAffirm affirmDetail;
    private ArrayList<AccountAffirmdetail_Parent> mparent=new ArrayList<>();        //组列表项，每个组都有一个子List
    private ArrayList<ArrayList<AccountAffirmdetail_Child>> mchild=new ArrayList<>();
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
                            showProgress.showProgress(School_Account_Manage_Detail.this,"加载中...");
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
                            showProgress.showProgress(School_Account_Manage_Detail.this,"确认中...");
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
                            showMessage.showMsg(School_Account_Manage_Detail.this,"网络连接异常，请检查网络连接");
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
        setContentView(R.layout.activity_school__account__manage__detail);
        Toolbar toolbar= (Toolbar) findViewById(R.id.school_account_manage_detail_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        requestQueue=NoHttp.newRequestQueue();
        gson=new Gson();
        Intent getData=getIntent();
        Id=getData.getStringExtra("Id");
        initView();
        initData();
    }


    private void initView() {
        status= (TextView) findViewById(R.id.account_affirm_detail_status);
        time= (TextView) findViewById(R.id.account_affirm_detail_time);
        hour= (TextView) findViewById(R.id.account_affirm_detail_hour);
        price= (TextView) findViewById(R.id.account_affirm_detail_price);
        draw= (TextView) findViewById(R.id.account_affirm_detail_draw);
        //
        attachH= (TextView) findViewById(R.id.account_affirm_detail_attachhour);
        attachP= (TextView) findViewById(R.id.account_affirm_detail_attachprice);
        attachdraw= (TextView) findViewById(R.id.account_affirm_detail_attachdraw);
        //
        list= (ExpandableListView) findViewById(R.id.account_affirm_detail_list);
        adapter=new AccountAffirmDetailAdapter(School_Account_Manage_Detail.this,mparent,mchild);
        list.setAdapter(adapter);
    }

    private void showContent() {
        status.setText(affirmDetail.getStatus());
        if(affirmDetail.getStatus().equals("已确认")){
            status.setTextColor(ContextCompat.getColor(this,R.color.colorPrimary));
        }else {
            status.setTextColor(ContextCompat.getColor(this,R.color.textColor_orange));
        }
        time.setText(affirmDetail.getTime());
        hour.setText(affirmDetail.getTrain_length());
        price.setText(affirmDetail.getAmount());
        draw.setText(affirmDetail.getDraw());
        //
        attachH.setText(affirmDetail.getAttach_train_length());
        attachP.setText(affirmDetail.getAttach_amount());
        attachdraw.setText(affirmDetail.getAttach_draw());
        //
        adapter.notifyDataSetChanged();
    }

    private void initData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                handler.sendEmptyMessage(1111);
                params.clear();
                values.clear();
                params.add("key");
                params.add("statement_id");
                values.add(school.schoolKey(School_Account_Manage_Detail.this));
                values.add(Id);
                sendRequest("GET",1, school.AccountAffirmDetail(),params,values);
            }
        }).start();
    }


    //添加账单确认按钮
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.school_menu_sure,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.school_menu_sure){
            handler.sendEmptyMessage(4444);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    params.clear();
                    values.clear();
                    params.add("key");
                    params.add("bill_id");
                    values.add(school.schoolKey(School_Account_Manage_Detail.this));
                    values.add(Id);
                    sendRequest("POST",2, school.AccountAffirmDetailConfirm(),params,values);
                }
            }).start();
        }
        return super.onOptionsItemSelected(item);
    }

    private void sendRequest(final String way, int what, String url, final ArrayList<String> params, ArrayList<String> values) {
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
                        mLog.e("reponse","->"+response.toString());
                        mmsg=objcet.getString("msg");
                        if(objcet.getString("code").equals("200")){
                            JSONArray array=new JSONArray(objcet.getString("data"));
                            JSONObject objcet1=new JSONObject(array.getJSONObject(0).toString());
                            affirmDetail=new AccountAffirm();
                            affirmDetail.setTime(objcet1.getString("time"));
                            affirmDetail.setAmount(objcet1.getString("amount"));
                            affirmDetail.setTrain_length(objcet1.getString("train_length"));
                            affirmDetail.setDraw(objcet1.getString("draw"));
                            affirmDetail.setAttach_amount(objcet1.getString("attach_amount"));
                            affirmDetail.setAttach_train_length(objcet1.getString("attach_train_length"));
                            affirmDetail.setAttach_draw(objcet1.getString("attach_draw"));
                            affirmDetail.setStatus(objcet1.getString("status_name"));
                            JSONArray array1=new JSONArray(objcet1.getString("coach_statement"));
                            if(array1.length()>0){
                                mparent.clear();
                                for (int i = 0; i < array1.length(); i++) {
                                    JSONObject objcet3=new JSONObject(array1.getJSONObject(i).toString());
                                    AccountAffirmdetail_Parent parent=new AccountAffirmdetail_Parent();
                                    parent.setId(objcet3.getString("id"));
                                    parent.setAmount(objcet3.getString("amount"));
                                    parent.setContent(objcet3.getString("content"));
                                    parent.setDraw(objcet3.getString("draw"));
                                    mparent.add(parent);
                                    JSONArray array3=new JSONArray(objcet3.getString("train_record"));
                                    ArrayList<AccountAffirmdetail_Child> mdata=new ArrayList<>();
                                    if(array3.length()>0){
                                        mchild.clear();
                                        for (int j = 0; j < array3.length(); j++) {
                                            AccountAffirmdetail_Child child=gson.fromJson(array3.getJSONObject(j).toString(),AccountAffirmdetail_Child.class);
                                            mdata.add(child);
                                        }
                                    }
                                    mchild.add(i,mdata);
                                    handler.sendEmptyMessage(2222);
                                }
                            }else {
                                handler.sendEmptyMessage(2222);
                            }
                        }else {
                            handler.sendEmptyMessage(3333);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else if(what==2){
                    try {
                        JSONObject object=new JSONObject(response.get().toString());
                        mmsg=object.getString("msg");
                        handler.sendEmptyMessage(5555);
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
