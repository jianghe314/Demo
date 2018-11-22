package com.kangzhan.student.Teacher.person_data;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.kangzhan.student.R;
import com.kangzhan.student.ShowUI.showMessage;
import com.kangzhan.student.ShowUI.showProgress;
import com.kangzhan.student.com.BaseActivity;
import com.kangzhan.student.mInterface.TeacherInterface.teacher;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.Response;

import org.json.JSONException;
import org.json.JSONObject;

public class Teacher_ChangePassword extends BaseActivity implements View.OnClickListener{
    private EditText old,news1,news2;
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
                            showProgress.showProgress(Teacher_ChangePassword.this,"加载中...");
                        }
                    });
                    break;
                case 1111:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.closeProgress();
                            Toast.makeText(getApplicationContext(),"修改成功",Toast.LENGTH_SHORT).show();
                        }
                    });
                    break;
                case 2222:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.closeProgress();
                            Toast.makeText(getApplicationContext(),"两次输入新密码不一致",Toast.LENGTH_SHORT).show();
                        }
                    });
                    break;
                case 3333:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.closeProgress();
                            Toast.makeText(getApplicationContext(),"原密码错误",Toast.LENGTH_SHORT).show();
                        }
                    });
                    break;
                case 9999:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.closeProgress();
                            showMessage.showMsg(Teacher_ChangePassword.this,"网络连接异常，请检查网络连接");
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
        setContentView(R.layout.activity_teacher__change_password);
        Toolbar toolbar= (Toolbar) findViewById(R.id.teacher_changePassword_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        initView();
        gson=new Gson();
    }

    private void initView() {
        old= (EditText) findViewById(R.id.teacher_change_old);
        news1= (EditText) findViewById(R.id.teacher_change_news1);
        news2= (EditText) findViewById(R.id.teacher_change_news2);
        save= (Button) findViewById(R.id.teacher_change_pas);
        save.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.teacher_change_pas:
                if(isRight()){
                    Message msg=new Message();
                    msg.what=0000;
                    handler.sendMessage(msg);
                    sendRequest();
                }
                break;
            default:
                break;
        }
    }

    private void sendRequest() {
        Request<JSONObject> request=NoHttp.createJsonObjectRequest(teacher.teacherChangePwd(), RequestMethod.POST);
        request.add("key",teacher.teacherKey(getApplicationContext()));
        request.add("oldpassword",old.getText().toString().trim());
        request.add("password",news1.getText().toString().trim());
        request.add("repassword",news2.getText().toString().trim());
        getRequestQueue().add(1, request, new OnResponseListener<JSONObject>() {
            @Override
            public void onStart(int what) {

            }

            @Override
            public void onSucceed(int what, Response<JSONObject> response) {
                Log.e("response","->"+response.get().toString().trim());
                try {
                    JSONObject object=new JSONObject(response.get().toString().trim());
                    String code=object.getString("code");
                    if(code.equals("200")){
                        handler.sendEmptyMessage(1111);
                    }else if(code.equals("400")||code.equals("201")){
                       handler.sendEmptyMessage(2222);
                    }else if(code.equals("401")){
                       handler.sendEmptyMessage(3333);
                    }else {
                        handler.sendEmptyMessage(9999);
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

    private boolean isRight() {
        boolean mold=false,n1=false,n2=false;
        if(old.getText().toString().trim().equals("")){
            Toast.makeText(getApplicationContext(),"旧密码不能为空",Toast.LENGTH_SHORT).show();
        }else {
            mold=true;
        }
        if(news1.getText().toString().trim().equals("")){
            Toast.makeText(getApplicationContext(),"新密码不能为空",Toast.LENGTH_SHORT).show();
        }else {
            n1=true;
        }
        if(news2.getText().toString().trim().equals("")){
            Toast.makeText(getApplicationContext(),"确认新密码不能为空",Toast.LENGTH_SHORT).show();
        }else {
            n2=true;
        }
        if(mold&&n1&&n2){
            return true;
        }else {
            return false;
        }
    }
}
