package com.kangzhan.student.School.SlideMenu;

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
import android.widget.EditText;

import com.kangzhan.student.R;
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

import org.json.JSONException;
import org.json.JSONObject;

public class School_Change_Psd extends BaseActivity implements View.OnClickListener{
    private EditText oldPsd,newPsd,rePsd;
    private Button save;
    private String mmsg;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1111:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.showProgress(School_Change_Psd.this,"保存中...");
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
                            showMessage.showMsg(School_Change_Psd.this,"网络连接异常，请检查网络连接");
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
        setContentView(R.layout.activity_school__change__psd);
        Toolbar toolbar= (Toolbar) findViewById(R.id.school_chang_psd_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        initView();
    }

    private void initView() {
        oldPsd= (EditText) findViewById(R.id.student_changePas_old);
        newPsd= (EditText) findViewById(R.id.student_changePas_new);
        rePsd= (EditText) findViewById(R.id.student_changePas_newConfirm);
        save= (Button) findViewById(R.id.student_changePas_save);
        save.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.student_changePas_save:
                if(isRight()){
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            handler.sendEmptyMessage(1111);
                            sendRequest();
                        }
                    }).start();
                }
                break;
            default:
                break;
        }
    }

    private void sendRequest() {
        Request<JSONObject> request=NoHttp.createJsonObjectRequest(school.ModifPsd(), RequestMethod.POST);
        request.add("key",school.schoolKey(getApplicationContext()));
        request.add("oldpassword",oldPsd.getText().toString().trim());
        request.add("password",newPsd.getText().toString().trim());
        request.add("repassword",rePsd.getText().toString().trim());
        getRequestQueue().add(1, request, new OnResponseListener<JSONObject>() {
            @Override
            public void onStart(int what) {

            }

            @Override
            public void onSucceed(int what, Response<JSONObject> response) {
                try {
                    JSONObject object=new JSONObject(response.get().toString());
                    mmsg=object.getString("msg");
                    handler.sendEmptyMessage(2222);
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
        boolean old=false,new1=false,new2=false;
        if(oldPsd.getText().toString().trim().equals("")){
            mToast.showText(getApplicationContext(),"旧密码不能为空");
        }else {
            old=true;
        }
        if(newPsd.getText().toString().trim().equals("")){
            mToast.showText(getApplicationContext(),"新密码不能为空");
        }else {
            new1=true;
        }
        if(rePsd.getText().toString().trim().equals("")){
            mToast.showText(getApplicationContext(),"新密码不能为空");
        }else {
            new2=true;
        }
        if(old&&new1&&new2){
            return true;
        }else {
            return false;
        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return true;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            if(getCurrentFocus()!=null && getCurrentFocus().getWindowToken()!=null){
                manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
        return super.onTouchEvent(event);
    }


}
