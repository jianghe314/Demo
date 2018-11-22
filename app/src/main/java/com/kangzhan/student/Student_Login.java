package com.kangzhan.student;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
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
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.kangzhan.student.Debug.mLog;
import com.kangzhan.student.ShowUI.mToast;
import com.kangzhan.student.ShowUI.showProgress;
import com.kangzhan.student.Student.Student_MainActivity;
import com.kangzhan.student.com.BaseActivity;
import com.kangzhan.student.com.RegisterActivity;
import com.kangzhan.student.mInterface.StudentInterface.student;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.Response;

import org.json.JSONException;
import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;

public class Student_Login extends BaseActivity implements View.OnClickListener,AMapLocationListener{

    private EditText userName,userPassword,code;
    private TextView forgetpassword,learn_car;
    private Button login;
    private ImageView code_iv;
    private ProgressDialog dialog;
    private String info,Id,codeId;
    //
    private LinearLayout test;
    //定位
    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;
    //初始化AMapLocationClientOption对象
    //
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0000:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.showProgress(getApplicationContext(),"登录中...");
                        }
                    });
                    break;
                case 1111:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.closeProgress();
                            JPushInterface.setAlias(getApplicationContext(),"DEV_STU_"+Id,null);
                            Intent login=new Intent(Student_Login.this,Student_MainActivity.class);
                            startActivity(login);
                            finish();
                        }
                    });
                    break;
                case 2222:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.closeProgress();
                            showText(info);
                            refreshCode();
                        }
                    });
                    break;
                case 9999:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.closeProgress();
                            showText("网络异常，请稍后再试！");
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
        setContentView(R.layout.activity_student__login);
        Toolbar toolbar= (Toolbar) findViewById(R.id.student_login_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar=getSupportActionBar();
        if(actionbar!=null){
            actionbar.setDisplayHomeAsUpEnabled(true);
        }
        initView();
        addLayoutListener(test,code_iv);
        getCodeInfo();
    }

    private void initView() {
        test= (LinearLayout) findViewById(R.id.student_login_l);
        //
        userName= (EditText) findViewById(R.id.student_login_userName);
        userPassword= (EditText) findViewById(R.id.student_login_password);
        code= (EditText) findViewById(R.id.student_login_code);
        forgetpassword= (TextView) findViewById(R.id.student_forget_password);
        forgetpassword.setOnClickListener(this);
        learn_car= (TextView) findViewById(R.id.student_learn_car);
        learn_car.setOnClickListener(this);
        login= (Button) findViewById(R.id.student_login_btn);
        login.setOnClickListener(this);
        code_iv= (ImageView) findViewById(R.id.student_login_iv);
        code_iv.setOnClickListener(this);
        //
        mLocationClient=new AMapLocationClient(getApplicationContext());
        mLocationClient.setLocationListener(this);
        mLocationOption=new AMapLocationClientOption();
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        mLocationOption.setOnceLocation(false);
        mLocationOption.setOnceLocationLatest(true);
        mLocationOption.setMockEnable(true);        //容许开启模拟器定位
        mLocationOption.setHttpTimeOut(5000);
        mLocationClient.setLocationOption(mLocationOption);
        mLocationClient.startLocation();

    }

    private void getCodeInfo() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                getVerifyCode();
            }
        }).start();
    }

    private void getVerifyCode() {
        Request<JSONObject> request=NoHttp.createJsonObjectRequest(student.studentHelpCode(),RequestMethod.GET);
        getRequestQueue().add(2, request, new OnResponseListener<JSONObject>() {
            @Override
            public void onStart(int what) {

            }

            @Override
            public void onSucceed(int what, Response<JSONObject> response) {
                //Log.e("Verify","->"+response.get().toString());
                try {
                    JSONObject object=new JSONObject(response.get().toString());
                    if(object.getString("code").equals("200")){
                        codeId=object.getString("data");

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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //验证码
            case R.id.student_login_iv:
                refreshCode();
                break;
            //登录按钮
            case R.id.student_login_btn:
                if(isRight()){
                    sendLogin(userName.getText().toString().trim(),userPassword.getText().toString().trim(),code.getText().toString().trim());
                }
                break;
            //想学车
            case R.id.student_learn_car:
                Intent student=new Intent(this, RegisterActivity.class);
                student.putExtra("who","student");
                startActivity(student);
                finish();
                break;
            //忘记密码
            case R.id.student_forget_password:
                Intent psd=new Intent(Student_Login.this,ForgotPasswordActivity.class);
                //学员的type值：1   教练type：2
                psd.putExtra("Type",1);
                startActivity(psd);
                break;
            default:
                break;
        }
    }

    private void refreshCode() {
        Glide.with(this).load(student.studentLoginVerify()+"?code="+codeId).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).error(R.mipmap.login_error).into(code_iv);
    }

    private boolean isRight(){
        boolean name=false,pass=false,ver=false;
        if(userName.getText().toString().trim().equals("")){
            showText("用户名不能为空");
        }else {
            name=true;
        }
        if(userPassword.getText().toString().trim().equals("")){
            showText("密码不能为空");
        }else {
            pass=true;
        }
        if(code.getText().toString().trim().equals("")){
            showText("验证码不能为空");
        }else {
            ver=true;
        }
        if(name&&pass&&ver){
            return true;
        }else {
            return false;
        }
    }
    private void sendLogin(String name,String pass,String code){
        Request<JSONObject> request=NoHttp.createJsonObjectRequest(student.studentLogin(), RequestMethod.POST);
        request.add("username",name);
        request.add("password",pass);
        request.add("code",codeId);
        request.add("verify",code);
        getRequestQueue().add(1, request, new OnResponseListener<JSONObject>() {
            @Override
            public void onStart(int what) {

            }
//{"code":200,"msg":"登录成功","key":"1kz149751021295221312","id":1,"name":"刘新","oss_photo":"http://kangzhan-pub-bucket.oss-cn-shenzhen.aliyuncs.com/studentHead/149672468620170606125126480380.png"}
            @Override
            public void onSucceed(int what, Response<JSONObject> response) {
                mLog.e("student_log","->"+response.get().toString());
                try {
                    JSONObject object=new JSONObject(response.get().toString());
                    String code=object.getString("code");
                    info=object.getString("msg");
                    if(code.equals("200")){
                        String key=object.getString("key");
                        Id=object.getString("id");
                        saveKey(key);
                        saveStudentId(Id);
                        saveName(object.getString("name"));
                        savePhoto(object.getString("oss_photo"));
                        handler.sendEmptyMessage(1111);
                    }else {
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

    private void showText(String text){
        Toast.makeText(getApplicationContext(),text,Toast.LENGTH_SHORT).show();
    }

    private void saveKey(String key){
        SharedPreferences sp=getSharedPreferences("studentKey",MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putString("key",key);
        editor.apply();
    }

    private void saveName(String name){
        SharedPreferences sp=getSharedPreferences("studentName",MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putString("name",name);
        editor.apply();
    }
    private void savePhoto(String photoUrl){
        SharedPreferences sp=getSharedPreferences("studentPhoto",MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putString("photo",photoUrl);
        editor.apply();
    }

    private void saveStudentId(String Id){
        SharedPreferences sp=getSharedPreferences("studentId",MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putString("Id",Id);
        editor.apply();
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if(aMapLocation!=null){
            if(aMapLocation.getErrorCode()==0){
                double mlong=aMapLocation.getLongitude();
                double mlat=aMapLocation.getLatitude();
                saveStduentLocation(mlat,mlong);
            }else {
                mLog.e("AmapError","location Error, ErrCode:"
                        + aMapLocation.getErrorCode() + ", errInfo:"
                        + aMapLocation.getErrorInfo());
            }
        }
    }

    private void saveStduentLocation(double mlat,double mlong) {
        SharedPreferences sp=getSharedPreferences("studentLocation",MODE_PRIVATE);
        SharedPreferences.Editor edtor=sp.edit();
        edtor.putString("lat",String.valueOf(mlat));
        edtor.putString("long",String.valueOf(mlong));
        edtor.apply();
    }


    @Override
    protected void onStop() {
        mLocationClient.stopLocation();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mLocationClient.onDestroy();
        super.onDestroy();
    }



}
