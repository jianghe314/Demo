package com.kangzhan.student.School.Account;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.kangzhan.student.Debug.mLog;
import com.kangzhan.student.R;
import com.kangzhan.student.School.Adapter.OneLineSimpleAdapter;
import com.kangzhan.student.School.Bean.AccountMsg;
import com.kangzhan.student.School.Bean.AccountMsgDetail;
import com.kangzhan.student.School.Bean.AccountMsgDetailContent;
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

public class School_Msg_detail extends BaseActivity implements View.OnClickListener {
    private TextView time,status,num,price;
    private Button sure;
    private LinearLayout listContainer;
    private RequestQueue requestQueue;
    private Gson gson;
    private String mmsg,bill_id;
    private ArrayList<AccountMsgDetail> data=new ArrayList<>();
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
                            showProgress.showProgress(School_Msg_detail.this,"加载中...");
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
                            showProgress.showProgress(School_Msg_detail.this,"确认中...");
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
                            showMessage.showMsg(School_Msg_detail.this,"网络连接异常，请检查网络连接");
                        }
                    });
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school__msg_detail);
        Toolbar toolbar= (Toolbar) findViewById(R.id.school_msg_detail_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        Intent getData=getIntent();
        bill_id=getData.getStringExtra("Id");
        requestQueue=NoHttp.newRequestQueue();
        gson=new Gson();
        initView();
        initData();
    }

    private void initView() {
        time= (TextView) findViewById(R.id.school_msg_detail_time);
        status= (TextView) findViewById(R.id.school_msg_detail_status);
        num= (TextView) findViewById(R.id.school_msg_detail_num);
        price= (TextView) findViewById(R.id.school_msg_detail_price);
        listContainer= (LinearLayout) findViewById(R.id.school_msg_detail_list);
        sure= (Button) findViewById(R.id.school_msg_detail_sure);
        sure.setOnClickListener(this);
    }

    private void initData() {
        handler.sendEmptyMessage(1111);
        new Thread(new Runnable() {
            @Override
            public void run() {
                params.clear();
                values.clear();
                params.add("key");
                params.add("bill_id");
                values.add(school.schoolKey(School_Msg_detail.this));
                values.add(bill_id);
                sendRequest("GET",1,school.AccountMsgDetail(),params,values);
            }
        }).start();
    }


    private void showContent() {
        AccountMsgDetail item=data.get(0);
        time.setText(item.getYear()+"年"+item.getMonth()+"月");
        num.setText(item.getSend_counts());
        price.setText(item.getAmount());
        status.setText(item.getStatus_name());
        if(item.getStatus_name().equals("已确认")){
            status.setTextColor(ContextCompat.getColor(School_Msg_detail.this,R.color.colorPrimary));
        }else {
            status.setTextColor(ContextCompat.getColor(School_Msg_detail.this,R.color.swipe_color1));
        }
        for (int i = 0; i < item.getSms_detail().size(); i++) {
            TextView txt=new TextView(School_Msg_detail.this);
            txt.setPadding(80,40,80,40);
            txt.setText(item.getSms_detail().get(i).getContent());
            listContainer.addView(txt);
        }
        listContainer.requestLayout();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.school_msg_detail_sure:
                handler.sendEmptyMessage(4444);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        params.clear();
                        values.clear();
                        params.add("key");
                        params.add("bill_id");
                        values.add(school.schoolKey(School_Msg_detail.this));
                        values.add(bill_id);
                        sendRequest("POST",2,school.AccountMsgDetailConfirm(),params,values);
                    }
                }).start();
                break;
            default:
                break;
        }
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
                try {
                    JSONObject objcet=new JSONObject(response.get().toString());
                    mLog.e("reponse","->"+response.toString());
                    mmsg=objcet.getString("msg");
                    if(what==1){
                        if(objcet.getString("code").equals("200")){
                            JSONArray array=new JSONArray(objcet.getString("data"));
                            if(array.length()>0){
                                data.clear();
                                for (int i = 0; i < array.length(); i++) {
                                    AccountMsgDetail test=gson.fromJson(array.getJSONObject(i).toString(),AccountMsgDetail.class);
                                    data.add(test);
                                }
                               handler.sendEmptyMessage(2222);
                            }else {
                                data.clear();
                                handler.sendEmptyMessage(3333);
                            }
                        }else {
                            data.clear();
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


    @Override
    protected void onDestroy() {
        requestQueue.cancelAll();
        super.onDestroy();
    }
}
