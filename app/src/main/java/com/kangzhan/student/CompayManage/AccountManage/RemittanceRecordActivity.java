package com.kangzhan.student.CompayManage.AccountManage;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.kangzhan.student.CompayManage.Adapter.AccountSchoolDetailAdapter;
import com.kangzhan.student.CompayManage.Bean.AccountCoachRecord;
import com.kangzhan.student.CompayManage.Bean.AccountInstiState;
import com.kangzhan.student.CompayManage.Bean.AccountSchoolChecking;
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

public class RemittanceRecordActivity extends BaseActivity {
    private TextView time,hour,money,draw;
    private ExpandableListView listView;
    private String Id,mmsg;
    private AccountSchoolDetailAdapter adapter;
    private Gson gson;
    private AccountInstiState mdata;
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
                            showProgress.showProgress(RemittanceRecordActivity.this,"加载中...");
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
                            showMessage.showMsg(RemittanceRecordActivity.this,"网络连接异常，请检查网络连接");
                        }
                    });
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remittance_record);
        Toolbar toolbar= (Toolbar) findViewById(R.id.compay_account_remittance_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        gson=new Gson();
        Intent getData=getIntent();
        Id=getData.getStringExtra("Id");
        initView();
        initData();
    }

    private void initView() {
        time= (TextView) findViewById(R.id.compay_account_remittance_time);
        hour= (TextView) findViewById(R.id.compay_account_remittance_hour);
        money= (TextView) findViewById(R.id.compay_account_remittance_money);
        draw= (TextView) findViewById(R.id.compay_account_remittance_draw);
        listView= (ExpandableListView) findViewById(R.id.compay_account_remittance_expandList);
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
                values.add(CompayInterface.CompayKey(RemittanceRecordActivity.this));
                values.add("1");
                values.add(Id);
                sendRequest("GET",CompayInterface.AccountSchoolDetail(),1,params,values);
            }
        }).start();
    }


    private void showContent() {
        time.setText(mdata.getStart_time()+"-"+mdata.getEnd_time());
        hour.setText(mdata.getTrain_length());
        money.setText(mdata.getAmount());
        draw.setText(mdata.getDraw());
        ArrayList<ArrayList<AccountCoachRecord>> childData=new ArrayList<>();
        for (int i = 0; i < mdata.getCoachState().size(); i++) {
            childData.add(i,mdata.getCoachState().get(i).getCoachRecord());
        }
        adapter=new AccountSchoolDetailAdapter(RemittanceRecordActivity.this,mdata.getCoachState(),childData);
        listView.setAdapter(adapter);
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
                    if(what==1) {
                        if(object.getString("code").equals("200")){
                            mdata=gson.fromJson(object.getString("instiState"),AccountInstiState.class);
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
