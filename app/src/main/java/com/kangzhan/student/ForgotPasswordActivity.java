package com.kangzhan.student;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.kangzhan.student.Debug.mLog;
import com.kangzhan.student.ShowUI.mToast;
import com.kangzhan.student.ShowUI.showMessage;
import com.kangzhan.student.ShowUI.showProgress;
import com.kangzhan.student.com.BaseActivity;
import com.kangzhan.student.mInterface.StudentInterface.student;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.Response;

import org.json.JSONException;
import org.json.JSONObject;

public class ForgotPasswordActivity extends BaseActivity implements View.OnClickListener{
    private EditText phoneNum,Code,psd1,psd2;
    private TextView getCode;
    private Button holdOn;
    private int type;
    private String mmsg;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0000:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.showProgress(ForgotPasswordActivity.this,"修改中...");
                        }
                    });
                    break;
                case 0011:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.showProgress(ForgotPasswordActivity.this,"发送中...");
                        }
                    });
                    break;
                case 1111:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.closeProgress();
                            AlertDialog.Builder builder=new AlertDialog.Builder(ForgotPasswordActivity.this);
                            builder.setMessage(mmsg);
                            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            builder.create().show();
                        }
                    });
                    break;
                case 1122:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.closeProgress();
                            AlertDialog.Builder builder=new AlertDialog.Builder(ForgotPasswordActivity.this);
                            builder.setMessage(mmsg);
                            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            builder.create().show();
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
                            showMessage.showMsg(ForgotPasswordActivity.this,"网络连接异常，请检测网络");
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
        setContentView(R.layout.activity_forgot_password);
        Toolbar toolbar= (Toolbar) findViewById(R.id.forgetPassword_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        Intent Type=getIntent();
        type=Type.getIntExtra("Type",5);
        initView();
    }

    private void initView() {
        phoneNum= (EditText) findViewById(R.id.forget_psd_phone_Num);
        Code= (EditText) findViewById(R.id.forget_psd_code);
        psd1= (EditText) findViewById(R.id.forget_psd_phone_new1);
        psd2= (EditText) findViewById(R.id.forget_psd_new2);
        getCode= (TextView) findViewById(R.id.forget_psd_getCode);
        getCode.setOnClickListener(this);
        holdOn= (Button) findViewById(R.id.forget_psd_holdOn);
        holdOn.setOnClickListener(this);
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //获取验证码
            case R.id.forget_psd_getCode:
                if(phoneNum.getText().toString().trim().equals("")){
                    AlertDialog.Builder builder=new AlertDialog.Builder(ForgotPasswordActivity.this);
                    builder.setMessage("手机号码不能为空");
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.create().show();
                }else {
                    if(phoneNum.getText().toString().trim().length()!=11){
                        AlertDialog.Builder builder=new AlertDialog.Builder(ForgotPasswordActivity.this);
                        builder.setMessage("非手机号码，请重新输入！");
                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        builder.create().show();
                    }else {
                        getCode.setEnabled(false);
                        TimeCount(60*1000,1000);
                        Message msg=new Message();
                        msg.what=0011;
                        handler.sendMessage(msg);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                getRequestCode();
                            }
                        }).start();
                    }

                }
                break;
            //提交
            case R.id.forget_psd_holdOn:
                if(isRight()){
                    handler.sendEmptyMessage(0000);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            sendRequest(type,Code.getText().toString().trim(),phoneNum.getText().toString().trim(),psd1.getText().toString().trim(),psd2.getText().toString().trim());
                        }
                    }).start();
                }
                break;
            default:
                break;
        }
    }

    private void getRequestCode() {
        Request<JSONObject> request=NoHttp.createJsonObjectRequest(student.getCode(),RequestMethod.GET);
        request.add("type",1);
        request.add("phone",phoneNum.getText().toString().trim());
        getRequestQueue().add(2, request, new OnResponseListener<JSONObject>() {
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

    private void sendRequest(int mType,String code,String phone,String new1,String new2) {
        Request<JSONObject> request=NoHttp.createJsonObjectRequest(student.studentForgotPsd(), RequestMethod.POST);
        request.add("type",mType);
        request.add("code",code);
        request.add("phone",phone);
        request.add("newPassword",new1);
        request.add("comfirmPassword",new2);
        getRequestQueue().add(1, request, new OnResponseListener<JSONObject>() {
            @Override
            public void onStart(int what) {

            }

            @Override
            public void onSucceed(int what, Response<JSONObject> response) {
                mLog.i("msg","->"+response.get().toString());
                try {
                    JSONObject object=new JSONObject(response.get().toString());
                    String code=object.getString("code");
                    mmsg=object.getString("msg");
                    if(code.equals("200")){
                       handler.sendEmptyMessage(1122);
                    }else {
                        handler.sendEmptyMessage(1111);
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
        boolean mphone=false,mcode = false, mnew1 = false, mnew2 = false;
        if(phoneNum.getText().toString().trim().equals("")){
            mToast.showText(getApplicationContext(),"手机号码不能为空");
        }else {
            mphone=true;
        }
        if(Code.getText().toString().trim().equals("")){
            mToast.showText(getApplicationContext(),"验证码不能为空");
        }else {
            mcode=true;
        }
        if(psd1.getText().toString().trim().equals("")){
            mToast.showText(getApplicationContext(),"新密码不能为空");
        }else {
            mnew1=true;
        }
        if(psd2.getText().toString().trim().equals("")){
            mToast.showText(getApplicationContext(),"确认新密码不能为空");
        }else {
            mnew2=true;
        }

        if(mphone&&mcode&&mnew1&&mnew2){
            return true;
        }else {
            return false;
        }
    }


    private void TimeCount (long TotalTime,long intervalTime){
        CountDownTimer time=new CountDownTimer(TotalTime,intervalTime) {
            @Override
            public void onTick(long millisUntilFinished) {
                getCode.setText((millisUntilFinished/1000)+"秒");
            }

            @Override
            public void onFinish() {
                getCode.setEnabled(true);
                getCode.setText("获取验证码");
            }
        };
        time.start();
    }

}
