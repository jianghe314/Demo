package com.kangzhan.student;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.LabeledIntent;
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
import com.kangzhan.student.ShowUI.showMessage;
import com.kangzhan.student.ShowUI.showProgress;
import com.kangzhan.student.Teacher.TeacherMainActivity;
import com.kangzhan.student.com.BaseActivity;
import com.kangzhan.student.com.RegisterActivity;
import com.kangzhan.student.mInterface.StudentInterface.student;
import com.kangzhan.student.mInterface.TeacherInterface.teacher;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.Response;

import org.json.JSONException;
import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;


public class Teacher_Login extends BaseActivity implements View.OnClickListener,AMapLocationListener{

    private EditText userName,userPassword,code;
    private TextView forgetpassword,learn_car;
    private Button login;
    private ImageView code_iv;
    private String info,Id,codeId;
    private double mlong,mlat;
    private LinearLayout test;
    //定位
    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;
    //初始化AMapLocationClientOption对象

   private Handler handler=new Handler(){
       @Override
       public void handleMessage(Message msg) {
           switch (msg.what){
               case 0000:
                   runOnUiThread(new Runnable() {
                       @Override
                       public void run() {
                           showProgress.showProgress(Teacher_Login.this,"登录中...");
                       }
                   });
                   break;
               case 1111:
                   runOnUiThread(new Runnable() {
                       @Override
                       public void run() {
                           showProgress.closeProgress();
                           JPushInterface.setAlias(Teacher_Login.this,"DEV_COACH_"+Id,null);
                           Intent login=new Intent(Teacher_Login.this,TeacherMainActivity.class);
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
                           mToast.showText(getApplicationContext(),info);
                           refreshCode();
                       }
                   });
                   break;
               case 9999:
                   runOnUiThread(new Runnable() {
                       @Override
                       public void run() {
                           showProgress.closeProgress();
                           showMessage.showMsg(getApplicationContext(),"网络连接异常，请检测网络连接");
                       }
                   });
                   break;
           }
       }
   };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher__login);
        Toolbar toolbar= (Toolbar) findViewById(R.id.teacher_login_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar=getSupportActionBar();
        if(actionbar!=null){
            actionbar.setDisplayHomeAsUpEnabled(true);
        }
        initView();
        addLayoutListener(test,code_iv);
        initCode();
    }



    private void initView() {
        test= (LinearLayout) findViewById(R.id.teacher_login_l);
        userName= (EditText) findViewById(R.id.teacher_login_userName);
        userPassword= (EditText) findViewById(R.id.teacher_login_password);
        code= (EditText) findViewById(R.id.teacher_login_code);
        forgetpassword= (TextView) findViewById(R.id.teacher_forget_password);
        forgetpassword.setOnClickListener(this);
        learn_car= (TextView) findViewById(R.id.teacher_learn_car);
        learn_car.setOnClickListener(this);
        login= (Button) findViewById(R.id.teacher_login_btn);
        login.setOnClickListener(this);
        code_iv= (ImageView) findViewById(R.id.teacher_login_iv);
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

    private void initCode() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                getCodeId();
            }
        }).start();

    }

    private void getCodeId() {
        Request<JSONObject> request=NoHttp.createJsonObjectRequest(teacher.teacherCodeHelp(),RequestMethod.GET);
        getRequestQueue().add(2, request, new OnResponseListener<JSONObject>() {
            @Override
            public void onStart(int what) {

            }

            @Override
            public void onSucceed(int what, Response<JSONObject> response) {
                mLog.e("Verify","->"+response.get().toString());
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


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //验证码
            case R.id.teacher_login_iv:
                refreshCode();
                break;
            //登录按钮
            case R.id.teacher_login_btn:
                if(isRight()){
                    handler.sendEmptyMessage(0000);
                    sendLogin();
                }
                break;
            //想学车side_nav_bar.xml

            case R.id.teacher_learn_car:
                Intent teacher = new Intent(this, RegisterActivity.class);
                teacher.putExtra("who", "teacher");
                startActivity(teacher);
                finish();
                break;
            //忘记密码
            case R.id.teacher_forget_password:
                Intent psd=new Intent(Teacher_Login.this,ForgotPasswordActivity.class);
                psd.putExtra("Type",2);
                startActivity(psd);
                break;
        }
    }

    private void refreshCode() {
        Glide.with(this).load(teacher.teacherLoginverify()+"?code="+codeId).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).error(R.mipmap.login_error).into(code_iv);
    }

    private void sendLogin() {
        Request<JSONObject> request=NoHttp.createJsonObjectRequest(teacher.teacherLogin(), RequestMethod.POST);
        request.add("username",userName.getText().toString().trim());
        request.add("password",userPassword.getText().toString().trim());
        request.add("verify",code.getText().toString().trim());
        request.add("code",codeId);
        request.add("latitude",mlat);
        request.add("longitude",mlong);
        getRequestQueue().add(1, request, new OnResponseListener<JSONObject>() {
            @Override
            public void onStart(int what) {

            }

            @Override
            public void onSucceed(int what, Response<JSONObject> response) {
                //{"code":200,"msg":"登录成功","key":"2kzc149751842877542690","id":2,"name":"冯玉林","oss_photo":null}
                mLog.e("teacher_log","->"+response.get().toString());
                try {
                    JSONObject object=new JSONObject(response.get().toString());
                    String code=object.getString("code");
                    info=object.getString("msg");
                    if(code.equals("200")){
                        saveTeacherKey(object.getString("key").trim());
                        saveTeacherId(object.getString("id").trim());
                        Id=object.getString("id");
                        saveTeacherName(object.getString("name"));
                        saveTeacherPhoto(object.getString("oss_photo"));
                        saveTeacherAttach(object.getString("attach"));
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

    private boolean isRight() {
        boolean user=false,pas=false,ver=false;
        if(userName.getText().toString().trim().equals("")){
            mToast.showText(getApplicationContext(),"用户名不能为空");
        }else {
            user=true;
        }
        if(userPassword.getText().toString().trim().equals("")){
            mToast.showText(getApplicationContext(),"密码不能为空");
        }else {
            pas=true;
        }
        if(code.getText().toString().trim().equals("")){
            mToast.showText(getApplicationContext(),"验证码不能为空");
        }else {
            ver=true;
        }
        if(user&&pas&&ver){
            return true;
        }else {
            return false;
        }
    }


    private void saveTeacherKey(String key){
        SharedPreferences sp=getSharedPreferences("teacherKey",MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putString("key",key);
        editor.apply();
    }

    private void saveTeacherName(String name){
        SharedPreferences sp=getSharedPreferences("teacherName",MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putString("name",name);
        editor.apply();
    }

    private void saveTeacherPhoto(String photoUrl){
        SharedPreferences sp=getSharedPreferences("teacherPhoto",MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putString("photo",photoUrl);
        editor.apply();
    }
    private void saveTeacherId(String Id){
        SharedPreferences sp=getSharedPreferences("teacherId",MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putString("id",Id);
        editor.apply();
    }
    private void saveTeacherAttach(String attach){
        SharedPreferences sp=getSharedPreferences("teacherAttach",MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putString("Attach",attach);
        editor.apply();
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {

        if(aMapLocation!=null){
            if(aMapLocation.getErrorCode()==0){
                mlong=aMapLocation.getLongitude();
                mlat=aMapLocation.getLatitude();
//                mToast.showText(Teacher_Login.this,"mlong->"+mlong+"mlat->"+mlat);
            }else {
                Log.e("AmapError","location Error, ErrCode:"
                        + aMapLocation.getErrorCode() + ", errInfo:"
                        + aMapLocation.getErrorInfo());
            }
        }
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
