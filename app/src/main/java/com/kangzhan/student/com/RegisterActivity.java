package com.kangzhan.student.com;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.kangzhan.student.Debug.mLog;
import com.kangzhan.student.MainLoginActivity;
import com.kangzhan.student.R;
import com.kangzhan.student.ShowUI.mToast;
import com.kangzhan.student.ShowUI.showMessage;
import com.kangzhan.student.ShowUI.showProgress;
import com.kangzhan.student.Student_Login;
import com.kangzhan.student.entity.City;
import com.kangzhan.student.entity.County;
import com.kangzhan.student.entity.Province;
import com.kangzhan.student.mInterface.SchoolInterface.school;
import com.kangzhan.student.mInterface.StudentInterface.student;
import com.kangzhan.student.mInterface.TeacherInterface.teacher;
import com.kangzhan.student.utils.AddressPickTask;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.JsonObjectRequest;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.Response;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends BaseActivity implements View.OnClickListener{

    private EditText userName,userPhone,qq,email,wechat,code;
    private Button regist;
    private ImageView code_iv,choice_iv,regist_header,read;
    private TextView address,who_regist,rule;
    private String who,mprovince,mcity,mcounty ,url,codeId,mmsg;
    private boolean isRead=false;
    //
    private RequestQueue requestQueue;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0000:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.showProgress(RegisterActivity.this,"注册中...");
                        }
                    });

                    break;
                case 1111:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //学员注册的跳转
                            showProgress.closeProgress();
                            Intent complete=new Intent(RegisterActivity.this,RegisterCompleteActivity.class);
                            startActivity(complete);
                        }
                    });
                    break;
                case 1122:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.closeProgress();
                            Toast.makeText(getApplicationContext(),mmsg,Toast.LENGTH_SHORT).show();
                        }
                    });
                    break;
                case 9999:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.closeProgress();
                            showMessage.showMsg(RegisterActivity.this,"网络连接异常，请检测网络连接");
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
        setContentView(R.layout.activity_register);
        requestQueue=NoHttp.newRequestQueue();
        Intent type=getIntent();
        who=type.getStringExtra("who");
        //根据who来判断是哪一类人注册
        Toolbar toolbar= (Toolbar) findViewById(R.id.teacher_studentRegist_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar=getSupportActionBar();
        if(actionbar!=null){
            actionbar.setDisplayHomeAsUpEnabled(true);
        }
        initView();
        initData();
    }

    private void initView() {
        userName= (EditText) findViewById(R.id.regist_userName);
        userPhone= (EditText) findViewById(R.id.regist_userPhone);
        qq= (EditText) findViewById(R.id.regist_qq);
        email= (EditText) findViewById(R.id.regist_userPassword);
        wechat= (EditText) findViewById(R.id.regist_wechat);
        code= (EditText) findViewById(R.id.regist_code);
        //
        regist= (Button) findViewById(R.id.regist_btn);
        regist.setOnClickListener(this);
        //
        code_iv= (ImageView) findViewById(R.id.regist_code_iv);
        code_iv.setOnClickListener(this);
        choice_iv= (ImageView) findViewById(R.id.regist_choice_address);
        choice_iv.setOnClickListener(this);
        regist_header= (ImageView) findViewById(R.id.regist_header);
        //
        address= (TextView) findViewById(R.id.regist_address);
        who_regist= (TextView) findViewById(R.id.regist_who);
        read= (ImageView) findViewById(R.id.regist_lawRule_read);
        read.setOnClickListener(this);
        rule= (TextView) findViewById(R.id.regist_lawRule);
        String str="康展学车<font color='#378dbf'>《条款与服务协议》</font>";
        rule.setText(Html.fromHtml(str));
        rule.setOnClickListener(this);
        if(who.equals("student")){
            who_regist.setText("学员注册");
            regist_header.setImageResource(R.drawable.student_header);
            url=student.studentRegistHelpCode();
            showMessage.showMsg(this,"已报名学员，请联系驾校获取账号密码");
        }else if(who.equals("teacher")){
            who_regist.setText("教练注册");
            regist_header.setImageResource(R.drawable.teacher_header);
            url=teacher.teacherReigstHelpCode();
        }


    }

    private void initData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                getCodeId();
            }
        }).start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //注册按钮  注册成功后返回到学员登录
            case R.id.regist_btn:
                //判断是否点击已读，再进行TextView为空判断
                if(isRead){
                    if(isRight()){
                        handler.sendEmptyMessage(0000);
                        if(who.equals("student")){
                            sendRegist(student.studentRegist(),1);
                        }else if(who.equals("teacher")){
                            sendRegist(teacher.teacherReigst(),2);
                        }
                    }
                }else {
                    mToast.showText(getApplicationContext(),"您还没有同意条款与服务协议");
               }
                break;
            //验证码
            case R.id.regist_code_iv:
                //先没有填占位图
                if(who.equals("student")){
                    Glide.with(this).load(student.studentResistVerify()+"?code="+codeId).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).error(R.mipmap.login_error).into(code_iv);
                }else if(who.equals("teacher")){
                    Glide.with(this).load(teacher.teacherReigstCode()+"?code="+codeId).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).error(R.mipmap.login_error).into(code_iv);
                }

                break;
            //选择地址
            case R.id.regist_choice_address:
                //选择地区
                AddressPickTask task=new AddressPickTask(this);
                task.setHideCounty(false);
                task.setHideProvince(false);
                task.setCallback(new AddressPickTask.Callback() {
                    @Override
                    public void onAddressInitFailed() {
                        mToast.showText(getApplicationContext(),"数据初始化失败");
                    }

                    @Override
                    public void onAddressPicked(Province province, City city, County county) {
                        if (county == null) {
                            //看这里可以改不，，，，，
                            // showToast(province.getAreaName() + city.getAreaName());
                            address.setText(province.getAreaName()+city.getAreaName());

                        } else {
                            //showToast(province.getAreaName() + city.getAreaName() + county.getAreaName());
                            String str=(province.getAreaName().equals(city.getAreaName())?(province.getAreaName()+county.getAreaName()):(province.getAreaName()+city.getAreaName()+county.getAreaName()));
                            address.setText(str);
                            mprovince=province.getAreaId();
                            mcity=city.getAreaId();
                            mcounty=county.getAreaId();

                            mLog.e("PCC","p->"+mprovince+":city->"+city.getAreaId()+":mcounty->"+mcounty);
                        }
                    }
                });
                task.execute("北京市", "北京市", "东城区");
                break;
            case R.id.regist_lawRule:
                //法律条款，调用手机浏览器查看
                Intent intent=new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_uri=Uri.parse("http://www.kzxueche.com/wap/kangzhan_web/modules/sample/views/clause.html");
                intent.setData(content_uri);
                startActivity(intent);
                break;
            case R.id.regist_lawRule_read:
                if(!isRead){
                    //没有读
                    read.setImageResource(R.mipmap.agree_rule);
                    isRead=true;
                }else {
                    //已读
                    read.setImageResource(R.mipmap.unagress_rule);
                   isRead=false;
                }
                break;
            default:
                break;

        }
    }
    private void getCodeId() {
        Request<JSONObject> request=NoHttp.createJsonObjectRequest(url,RequestMethod.GET);
        requestQueue.add(2, request, new OnResponseListener<JSONObject>() {
            @Override
            public void onStart(int what) {

            }

            @Override
            public void onSucceed(int what, Response<JSONObject> response) {
                mLog.e("code","->"+response.get().toString());
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
                handler.sendEmptyMessage(9999);
            }

            @Override
            public void onFinish(int what) {

            }
        });
    }


    //发送网络请求
    private void sendRegist(String url,int what){
        Request<JSONObject> request=NoHttp.createJsonObjectRequest(url, RequestMethod.POST);
        request.add("real_name",userName.getText().toString().trim());
        request.add("mobile",userPhone.getText().toString().trim());
        request.add("qqnum",qq.getText().toString().trim());
        request.add("wechatnum",wechat.getText().toString().trim());
        request.add("email",email.getText().toString().trim());
        request.add("verify",code.getText().toString().trim());
        request.add("code",codeId);
        request.add("province_id",mprovince);
        request.add("city_id",mprovince.equals(mcity)?mcounty:mcity);
        request.add("county_id",mprovince.equals(mcity)?"":mcounty);
        requestQueue.add(what, request, new OnResponseListener<JSONObject>() {
            @Override
            public void onStart(int what) {

            }

            @Override
            public void onSucceed(int what, Response<JSONObject> response) {
               if(what==1){
                   //学员
                   mLog.e("response-->",response.get().toString()+"");
                   try {
                       JSONObject object=new JSONObject(response.get().toString());
                       mmsg=object.getString("msg");
                       if(object.getString("code").equals("200")){
                           //保存工作人员信息
                           saveWorker_key(object.getString("key"));
                           saveWorker_Info(object.getString("clerkData"));
                           handler.sendEmptyMessage(1111);
                       }else {
                           handler.sendEmptyMessage(1122);
                       }

                   } catch (JSONException e) {
                       e.printStackTrace();
                   }
               }else if(what==2){
                   //教练
                   try {
                       JSONObject object=new JSONObject(response.get().toString());
                       mmsg=object.getString("msg");
                       handler.sendEmptyMessage(1122);
                   } catch (JSONException e) {
                       e.printStackTrace();
                   }
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
        boolean name=false,phone=false,add=false,cod=false;
        if(userName.getText().toString().trim().equals("")){
            showText("用户名不能为空");
        }else {
            name=true;
        }
        if(userPhone.getText().toString().trim().equals("")){
            showText("手机号码不能为空");
        }else {
            phone=true;
        }
        if(address.getText().toString().trim().equals("")){
            showText("地址不能为空");
        }else {
            add=true;
        }
        if(code.getText().toString().trim().equals("")){
            showText("验证码不能为空");
        }else {
            cod=true;
        }

        if(name&&phone&add&&cod==true){
            return true;
        }else {
            return false;
        }
    }

    private void showText(String text){
        Toast.makeText(getApplicationContext(),text,Toast.LENGTH_SHORT).show();
    }

    private void saveWorker_key(String key){
        SharedPreferences sp=getSharedPreferences("RegistKey",MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putString("Regist_key",key);
        editor.apply();
    }

    /**clerkData={
     *  "phone":"13667360207"
     *  "real_name":"2222"
     * }
     */
    private void saveWorker_Info(String WorkInfo){
        SharedPreferences sp=getSharedPreferences("RegistInfo",MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putString("Regist_Info",WorkInfo);
        editor.apply();
    }

    @Override
    protected void onDestroy() {
        requestQueue.cancelAll();
        super.onDestroy();
    }
}
