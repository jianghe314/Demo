package com.kangzhan.student;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
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
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.kangzhan.student.Debug.mLog;
import com.kangzhan.student.School.SchoolMainActivity;
import com.kangzhan.student.ShowUI.mToast;
import com.kangzhan.student.ShowUI.showMessage;
import com.kangzhan.student.ShowUI.showProgress;
import com.kangzhan.student.School.SchoolRegistActivity;
import com.kangzhan.student.com.BaseActivity;
import com.kangzhan.student.com.RegisterActivity;
import com.kangzhan.student.mInterface.SchoolInterface.school;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.Response;

import org.json.JSONException;
import org.json.JSONObject;

public class School_Login extends BaseActivity implements View.OnClickListener{
    private EditText userName,userPsd,code;
    private Button Login;
    private LinearLayout test;
    private ImageView ImageCode;
    private String codeId;
    private String mmsg;
    private TextView joinus,forgetPsd;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0000:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.showProgress(School_Login.this,"登录中...");
                        }
                    });
                    break;
                case 1111:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.closeProgress();
                            mToast.showText(getApplicationContext(),mmsg);
                            refreshCode();
                        }
                    });
                    break;
                case 1122:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.closeProgress();
                            Intent maim=new Intent(School_Login.this, SchoolMainActivity.class);
                            startActivity(maim);
                            finish();
                        }
                    });
                    break;
                case 9999:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.closeProgress();
                            showMessage.showMsg(School_Login.this,"网络连接异常，请检测网络连接");
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
        setContentView(R.layout.activity_school);
        Toolbar toolbar= (Toolbar) findViewById(R.id.school_login_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        initView();
        addLayoutListener(test,code);
        getCode();
    }

    private void getCode() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                getCodeId();
            }
        }).start();
    }

    private void getCodeId() {
        Request<JSONObject> request=NoHttp.createJsonObjectRequest(school.schoolCodeId(), RequestMethod.GET);
        getRequestQueue().add(1, request, new OnResponseListener<JSONObject>() {
            @Override
            public void onStart(int what) {

            }

            @Override
            public void onSucceed(int what, Response<JSONObject> response) {
                try {
                    JSONObject object=new JSONObject(response.get().toString());
                    codeId=object.getString("data");
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

    private void initView() {
        test= (LinearLayout) findViewById(R.id.school_test);
        Login= (Button) findViewById(R.id.school_login_btn);
        Login.setOnClickListener(this);
        ImageCode= (ImageView) findViewById(R.id.school_login_iv);
        ImageCode.setOnClickListener(this);
        userName= (EditText) findViewById(R.id.school_login_userName);
        userPsd= (EditText) findViewById(R.id.school_login_password);
        code= (EditText) findViewById(R.id.school_login_code);
        //
        joinus= (TextView) findViewById(R.id.school_learn_car);
        joinus.setOnClickListener(this);
        forgetPsd= (TextView) findViewById(R.id.school_forget_password);
        forgetPsd.setOnClickListener(this);
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.school_login_btn:
                if(isRight()){
                    sendLogin(userName.getText().toString(),userPsd.getText().toString(),code.getText().toString());
                }
                break;
            case R.id.school_login_iv:
                refreshCode();
                break;
            case R.id.school_learn_car:
                Intent join=new Intent(School_Login.this, SchoolRegistActivity.class);
                startActivity(join);
                break;
            case R.id.school_forget_password:
                Intent psd=new Intent(School_Login.this,ForgotPasswordActivity.class);
                startActivity(psd);
                break;
            default:
                break;
        }
    }

    private void refreshCode() {
        Glide.with(this).load(school.LoginCode()+"?code="+codeId).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).error(R.mipmap.login_error).into(ImageCode);
    }

    private void sendLogin(String name,String psd,String ver) {
        final Request<JSONObject> request=NoHttp.createJsonObjectRequest(school.schoolLogin(),RequestMethod.POST);
        request.add("username",name);
        request.add("password",psd);
        request.add("verify",ver);
        request.add("code",codeId);
        getRequestQueue().add(2, request, new OnResponseListener<JSONObject>() {
            @Override
            public void onStart(int what) {

            }

            @Override
            public void onSucceed(int what, Response<JSONObject> response) {
                mLog.i("login","->"+response.get().toString());
                try {
                    JSONObject object=new JSONObject(response.get().toString());
                    if(object.getString("code").equals("200")){
                        saveSchoolrKey(object.getString("key"));
                        saveSchoolrName(object.getString("instname"));
                        handler.sendEmptyMessage(1122);
                    }else {
                        mmsg=object.getString("msg");
                        handler.sendEmptyMessage(1111);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailed(int what, Response<JSONObject> response) {

            }

            @Override
            public void onFinish(int what) {

            }
        });
    }

    private boolean isRight() {
        boolean n=false,p=false,c=false;
        if(userName.getText().toString().trim().equals("")){
            mToast.showText(getApplicationContext(),"用户名不能为空");
        }else {
            n=true;
        }
        if(userPsd.getText().toString().trim().equals("")){
            mToast.showText(getApplicationContext(),"密码不能为空");
        }else {
            p=true;
        }
        if(code.getText().toString().trim().equals("")){
            mToast.showText(getApplicationContext(),"验证码不能为空");
        }else {
            c=true;
        }
        if(n&&p&&c){
            return true;
        }else {
            return false;
        }
    }



    private void saveSchoolrKey(String key){
        SharedPreferences sp=getSharedPreferences("schoolKey",MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putString("key",key);
        editor.apply();
    }
    private void saveSchoolrName(String name){
        SharedPreferences sp=getSharedPreferences("schoolName",MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putString("name",name);
        editor.apply();
    }


    public void addLayoutListener(final View main, final View scroll) {
        main.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect rect = new Rect();
                //1、获取main在窗体的可视区域
                main.getWindowVisibleDisplayFrame(rect);
                //2、获取main在窗体的不可视区域高度，在键盘没有弹起时，main.getRootView().getHeight()调节度应该和rect.bottom高度一样
                int mainInvisibleHeight = main.getRootView().getHeight() - rect.bottom;
                int screenHeight = main.getRootView().getHeight();//屏幕高度
                //3、不可见区域大于屏幕本身高度的1/4：说明键盘弹起了
                if (mainInvisibleHeight > screenHeight / 4) {
                    int[] location = new int[2];
                    scroll.getLocationInWindow(location);
                    // 4､获取Scroll的窗体坐标，算出main需要滚动的高度
                    int srollHeight = (location[1] + scroll.getHeight()) - rect.bottom;
                    //5､让界面整体上移键盘的高度
                    main.scrollTo(0, srollHeight);
                } else {
                    //3、不可见区域小于屏幕高度1/4时,说明键盘隐藏了，把界面下移，移回到原有高度
                    main.scrollTo(0, 0);
                }
            }
        });
    }

}
