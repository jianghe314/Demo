package com.kangzhan.student.Student.person_data_activity;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.kangzhan.student.R;
import com.kangzhan.student.ShowUI.showProgress;
import com.kangzhan.student.com.BaseActivity;
import com.kangzhan.student.mInterface.StudentInterface.student;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.Response;

import org.apache.http.conn.scheme.HostNameResolver;
import org.json.JSONException;
import org.json.JSONObject;

public class ChangePasswordActivity extends BaseActivity {
    private TextView old,news,confirm;
    private Button save;
    private Gson gson;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0000:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.showProgress(ChangePasswordActivity.this,"保存中...");
                        }
                    });
                    break;
                case 1111:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //成功
                            showProgress.closeProgress();
                            Toast.makeText(getApplicationContext(),"修改密码成功",Toast.LENGTH_SHORT).show();
                            try {
                                Thread.sleep(1500);
                                finish();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    break;
                case 2222:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //不一致
                            showProgress.closeProgress();
                            Toast.makeText(getApplicationContext(),"新旧密码不一致，请再试",Toast.LENGTH_SHORT).show();
                        }
                    });
                    break;
                case 3333:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //元密码不对
                            showProgress.closeProgress();
                            Toast.makeText(getApplicationContext(),"原密码不对，请再试",Toast.LENGTH_SHORT).show();
                        }
                    });
                    break;
                case 9999:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //网络异常
                            showProgress.closeProgress();
                            Toast.makeText(getApplicationContext(),"保存失败，请稍后再试",Toast.LENGTH_SHORT).show();
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
        setContentView(R.layout.activity_change_password);
        Toolbar toolbar= (Toolbar) findViewById(R.id.student_changePassword_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar=getSupportActionBar();
        if(actionbar!=null){
            actionbar.setDisplayHomeAsUpEnabled(true);
        }
        gson=new Gson();
        initView();
    }

    private void initView() {
        old= (TextView) findViewById(R.id.student_changePas_old);
        news= (TextView) findViewById(R.id.student_changePas_new);
        confirm= (TextView) findViewById(R.id.student_changePas_newConfirm);
        save= (Button) findViewById(R.id.student_changePas_save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Message msg=new Message();
                        msg.what=0000;
                        handler.sendMessage(msg);
                        sendRequest("1",old.getText().toString().trim(),news.getText().toString().trim(),confirm.getText().toString().trim());
                    }
                }).start();
            }
        });
    }

    private void sendRequest(String id,String old,String news,String confirm) {
        Request<JSONObject> request=NoHttp.createJsonObjectRequest(student.studentChangePas(), RequestMethod.POST);
        request.add("key",student.studentKey(getApplicationContext()));
        request.add("stu_id",id);
        request.add("oldPassword",old);
        request.add("newPassword",news);
        request.add("comfirmPassword",confirm);
        getRequestQueue().add(1, request, new OnResponseListener<JSONObject>() {
            @Override
            public void onStart(int what) {

            }

            @Override
            public void onSucceed(int what, Response<JSONObject> response) {
                try {
                    JSONObject object=new JSONObject(response.get().toString());
                    String code=object.getString("code");
                    //200：成功 400：新旧密码不一致  401：原密码错误
                    if(code.equals("200")){
                        Message msg=new Message();
                        msg.what=1111;
                        handler.sendMessage(msg);
                    }else if(code.equals("400")){
                        Message msg=new Message();
                        msg.what=2222;
                        handler.sendMessage(msg);
                    }else if(code.equals("401")){
                        Message msg=new Message();
                        msg.what=3333;
                        handler.sendMessage(msg);
                    }else {
                        Message msg=new Message();
                        msg.what=9999;
                        handler.sendMessage(msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(int what, Response<JSONObject> response) {
                Message msg=new Message();
                msg.what=9999;
                handler.sendMessage(msg);
            }

            @Override
            public void onFinish(int what) {

            }
        });
    }

}
