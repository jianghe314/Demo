package com.kangzhan.student.Teacher.person_data;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.kangzhan.student.Debug.mLog;
import com.kangzhan.student.R;
import com.kangzhan.student.ShowUI.mToast;
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

public class Teacher_addAdvise extends BaseActivity implements View.OnClickListener{
    private EditText title,content;
    private Button holdOff;
    private Gson gson;
    private String mmsg;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0000:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.showProgress(Teacher_addAdvise.this,"上传中...");
                        }
                    });
                    break;
                case 1111:
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
                            showMessage.showMsg(Teacher_addAdvise.this,"网络连接异常，请检查网络连接");
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
        setContentView(R.layout.activity_teacher_add_advise);
        Toolbar toolbar= (Toolbar) findViewById(R.id.teacher_addAdvise_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        initView();
        gson=new Gson();
    }

    private void initView() {
        title= (EditText) findViewById(R.id.teacher_addadvise_title);
        content= (EditText) findViewById(R.id.teacher_addadvise_content);
        holdOff= (Button) findViewById(R.id.teacher_addadvise_holdOff);
        holdOff.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.teacher_addadvise_holdOff:
                if(isRight()){
                    handler.sendEmptyMessage(0000);
                    sendRequest();
                }

                break;
            default:
                break;
        }
    }

    private boolean isRight() {
        boolean mt=false,mc=false;
        if(title.getText().toString().trim().equals("")){
            Toast.makeText(getApplicationContext(),"摘要不能为空哦",Toast.LENGTH_SHORT).show();
        }else {
            mt=true;
        }
        if(content.getText().toString().trim().equals("")){
            Toast.makeText(getApplicationContext(),"问题描述不能为空哦",Toast.LENGTH_SHORT).show();
        }else {
            mc=true;
        }

        if(mt&&mc){
            return true;
        }else {
            return false;
        }
    }


    private void sendRequest() {
        Request<JSONObject> request=NoHttp.createJsonObjectRequest(teacher.teacherAddAdvise(), RequestMethod.POST);
        request.add("key",teacher.teacherKey(getApplicationContext()));
        request.add("source",20);
        request.add("summary",title.getText().toString().trim());
        request.add("description",content.getText().toString().trim());
        getRequestQueue().add(1, request, new OnResponseListener<JSONObject>() {
            @Override
            public void onStart(int what) {

            }

            @Override
            public void onSucceed(int what, Response<JSONObject> response) {
                mLog.i("response","->"+response.get().toString());
                try {
                    JSONObject object=new JSONObject(response.get().toString().trim());
                    String code=object.getString("code");
                    mmsg=object.getString("msg");
                    handler.sendEmptyMessage(1111);
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
