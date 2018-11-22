package com.kangzhan.student.CompayManage.SlideMenu;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.kangzhan.student.CompayManage.Bean.StaffManage;
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

public class StaffChangePsdActivity extends BaseActivity {
    private String mmsg,Id;
    private EditText psd1,psd2;
    private Button save;
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
                            showProgress.showProgress(StaffChangePsdActivity.this,"修改中...");
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
                case 9999:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.closeProgress();
                            showMessage.showMsg(StaffChangePsdActivity.this,"网络连接异常，请检查网络连接");
                        }
                    });
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_change_psd);
        Toolbar toolbar= (Toolbar) findViewById(R.id.compay_slide_staff_psd_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        Intent getData=getIntent();
        Id=getData.getStringExtra("Id");
        initView();
    }

    private void initView() {
        psd1= (EditText) findViewById(R.id.compay_staff_change_psd1);
        psd2= (EditText) findViewById(R.id.compay_staff_change_psd2);
        save= (Button) findViewById(R.id.compay_staff_change_save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(psd1.getText().toString().trim().length()>0){
                    if(psd2.getText().toString().trim().length()>0){
                        handler.sendEmptyMessage(1111);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                params.clear();
                                values.clear();
                                params.add("key");
                                params.add("clerk_id");
                                params.add("newPassword");
                                params.add("comfirmPassword");
                                values.add(CompayInterface.CompayKey(getApplicationContext()));
                                values.add(Id);
                                values.add(psd1.getText().toString().trim());
                                values.add(psd2.getText().toString().trim());
                                sendRequest("POST",CompayInterface.StaffChangePsd(),1,params,values);
                            }
                        }).start();
                    }else {
                        mToast.showText(getApplicationContext(),"请输入密码");
                    }
                }else {
                    mToast.showText(getApplicationContext(),"请输入密码");
                }
            }
        });
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
                        handler.sendEmptyMessage(2222);
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
