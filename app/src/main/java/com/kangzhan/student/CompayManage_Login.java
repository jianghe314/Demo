package com.kangzhan.student;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.kangzhan.student.CompayManage.CompayMainActivity;
import com.kangzhan.student.Debug.mLog;
import com.kangzhan.student.ShowUI.mToast;
import com.kangzhan.student.ShowUI.showMessage;
import com.kangzhan.student.ShowUI.showProgress;
import com.kangzhan.student.com.BaseActivity;
import com.kangzhan.student.mInterface.CompayInterface.CompayInterface;
import com.kangzhan.student.mInterface.SchoolInterface.school;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CompayManage_Login extends BaseActivity implements View.OnClickListener {
    private EditText userName,userPsd,userCode;
    private Button login;
    private ImageView code;
    private TextView forgotPsd;
    private LinearLayout test;
    private String mmsg,codeId;
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
                            showProgress.showProgress(CompayManage_Login.this,"登录中...");
                        }
                    });
                    break;
                case 2222:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.closeProgress();
                            Intent Login=new Intent(CompayManage_Login.this, CompayMainActivity.class);
                            startActivity(Login);
                            finish();
                        }
                    });
                    break;
                case 3333:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.closeProgress();
                            mToast.showText(getApplicationContext(),mmsg);
                            refreshCode();
                        }
                    });
                    break;
                case 9999:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.closeProgress();
                            showMessage.showMsg(CompayManage_Login.this,"网络连接异常，请检查网络连接");
                        }
                    });
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compay_manage_login);
        Toolbar toolbar= (Toolbar) findViewById(R.id.compay_login_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        initView();
        addLayoutListener(test,userCode);
        getCodeHelp();
    }

    private void initView() {
        test= (LinearLayout) findViewById(R.id.compay_login_lin);
        userName= (EditText) findViewById(R.id.compay_login_userName);
        userPsd= (EditText) findViewById(R.id.compay_login_userPsd);
        userCode= (EditText) findViewById(R.id.compay_login_code);
        login= (Button) findViewById(R.id.compay_login_btn);
        code= (ImageView) findViewById(R.id.compay_login_code_iv);
        //forgotPsd= (TextView) findViewById(R.id.compay_login_forgotPsd);
        login.setOnClickListener(this);
        code.setOnClickListener(this);
        //forgotPsd.setOnClickListener(this);
    }

    private void getCodeHelp() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                params.clear();
                values.clear();
                sendRequest("GET",1, CompayInterface.CodeHelp(),params,values);
            }
        }).start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.compay_login_code_iv:
                refreshCode();
                break;
            /*
            case R.id.compay_login_forgotPsd:
                Intent psd=new Intent(this,ForgotPasswordActivity.class);
                psd.putExtra("Type",4);
                startActivity(psd);
                finish();
                break;
                */
            case R.id.compay_login_btn:
                if(isRight()){
                    handler.sendEmptyMessage(1111);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            params.clear();
                            values.clear();
                            params.add("username");
                            params.add("password");
                            params.add("verify");
                            params.add("code");
                            values.add(userName.getText().toString().trim());
                            values.add(userPsd.getText().toString().trim());
                            values.add(userCode.getText().toString().trim());
                            values.add(codeId);
                            sendRequest("POST",2,CompayInterface.CompayLogin(),params,values);
                        }
                    }).start();
                }
                break;
        }
    }

    private void refreshCode() {
        Glide.with(this).load(CompayInterface.LoginCode()+"?code="+codeId).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).error(R.mipmap.login_error).into(code);
    }

    private boolean isRight() {
        boolean isName=false,isPsd=false,isCode=false;
        if(userName.getText().toString().trim().equals("")){
            mToast.showText(getApplicationContext(),"用户名不能为空");
        }else {
            isName=true;
        }
        if(userPsd.getText().toString().trim().equals("")){
            mToast.showText(getApplicationContext(),"密码不能为空");
        }else {
            isPsd=true;
        }
        if(userCode.getText().toString().trim().equals("")){
            mToast.showText(getApplicationContext(),"验证码不能为空");
        }else {
            isCode=true;
        }
        if(isName&&isPsd&&isCode){
            return true;
        }else {
            return false;
        }
    }


    private void sendRequest(final String way, int what, String url, ArrayList<String> params, ArrayList<String> values) {
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
        getRequestQueue().add(what, request, new OnResponseListener<JSONObject>() {
            @Override
            public void onStart(int what) {

            }

            @Override
            public void onSucceed(int what, Response<JSONObject> response) {
                try {
                    JSONObject object=new JSONObject(response.get().toString());
                    mLog.e("key","->"+response.toString());
                    mmsg=object.getString("msg");
                    if(what==1){
                        if(object.getString("code").equals("200")){
                            codeId=object.getString("data");
                        }else {
                            codeId="";
                        }
                    }else if(what==2){
                        if(object.getString("code").equals("200")){
                            saveCompayKey(object.getString("key"));
                            saveCompayId(object.getString("role_id"));
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

    private void saveCompayKey(String key){
        SharedPreferences sp=getSharedPreferences("compayKey",MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putString("key",key);
        editor.apply();
    }

    private void saveCompayId(String Id){
        SharedPreferences sp=getSharedPreferences("compayId",MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putString("Id",Id);
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
