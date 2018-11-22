package com.kangzhan.student.School;

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.kangzhan.student.Debug.mLog;
import com.kangzhan.student.R;
import com.kangzhan.student.School.Bean.EduCarManage;
import com.kangzhan.student.ShowUI.mToast;
import com.kangzhan.student.ShowUI.showMessage;
import com.kangzhan.student.ShowUI.showProgress;
import com.kangzhan.student.com.BaseActivity;
import com.kangzhan.student.com.RegisterActivity;
import com.kangzhan.student.entity.City;
import com.kangzhan.student.entity.County;
import com.kangzhan.student.entity.Province;
import com.kangzhan.student.mInterface.SchoolInterface.school;
import com.kangzhan.student.mInterface.StudentInterface.student;
import com.kangzhan.student.mInterface.TeacherInterface.teacher;
import com.kangzhan.student.utils.AddressPickTask;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SchoolRegistActivity extends BaseActivity implements View.OnClickListener {
    private EditText name,person,phone,qq,weChat,email,code;
    private ImageView getCode,rule,choiceAddress;
    private TextView readRule,address;
    private Button send;
    private String mmsg,codeId,mprovince,mcity,mcounty;
    private boolean isRead=false;
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
                            showProgress.showProgress(SchoolRegistActivity.this,"注册中...");
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
                            showMessage.showMsg(SchoolRegistActivity.this,"网络连接异常，请检查网络连接");
                        }
                    });
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_regist);
        Toolbar toolbar= (Toolbar) findViewById(R.id.school_Regist_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        initView();
    }

    private void initView() {
        name= (EditText) findViewById(R.id.school_regist_userName);
        person= (EditText) findViewById(R.id.school_regist_person);
        phone= (EditText) findViewById(R.id.school_regist_phone);
        qq= (EditText) findViewById(R.id.school_regist_qq);
        weChat= (EditText) findViewById(R.id.school_regist_wechat);
        email= (EditText) findViewById(R.id.school_regist_email);
        address= (TextView) findViewById(R.id.school_regist_address);
        code= (EditText) findViewById(R.id.regist_code);
        choiceAddress= (ImageView) findViewById(R.id.school_regist_choice_address);
        getCode= (ImageView) findViewById(R.id.school_regist_code_iv);
        rule= (ImageView) findViewById(R.id.school_regist_lawRule_read);
        readRule= (TextView) findViewById(R.id.school_regist_lawRule);
        String str="康展学车<font color='#378dbf'>《条款与服务协议》</font>";
        readRule.setText(Html.fromHtml(str));
        choiceAddress.setOnClickListener(this);
        readRule.setOnClickListener(this);
        rule.setOnClickListener(this);
        getCode.setOnClickListener(this);
        send= (Button) findViewById(R.id.school_regist_btn);
        send.setOnClickListener(this);
        new Thread(new Runnable() {
            @Override
            public void run() {
                params.clear();
                values.clear();
                sendReuqst("GET",1, school.schoolRegistHelpCode(),params,values);
            }
        }).start();
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.school_regist_choice_address:
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
                            address.setText(province.getAreaName()+city.getAreaName());

                        } else {
                            //showToast(province.getAreaName() + city.getAreaName() + county.getAreaName());
                            String str=(province.getAreaName().equals(city.getAreaName())?(province.getAreaName()+county.getAreaName()):(province.getAreaName()+city.getAreaName()+county.getAreaName()));
                            address.setText(str);
                            mprovince=province.getAreaId();
                            mcity=city.getAreaId();
                            mcounty=county.getAreaId();
                            //mLog.e("PCC","p->"+mprovince+":city->"+city.getAreaId()+":mcounty->"+mcounty);
                        }
                    }
                });
                task.execute("北京市", "北京市", "东城区");
                break;
            case R.id.school_regist_lawRule_read:
                if(!isRead){
                    //没有读
                    rule.setImageResource(R.mipmap.agree_rule);
                    isRead=true;
                }else {
                    //已读
                    rule.setImageResource(R.mipmap.unagress_rule);
                    isRead=false;
                }
                break;
            case R.id.school_regist_lawRule:
                //法律条款，调用手机浏览器查看
                Intent intent=new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_uri=Uri.parse("http://www.kzxueche.com/wap/kangzhan_web/modules/sample/views/clause.html");
                intent.setData(content_uri);
                startActivity(intent);
                break;
            case R.id.school_regist_code_iv:
                //获取验证码
                Glide.with(this).load(school.schoolRegistCode()+"?code="+codeId).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).error(R.mipmap.login_error).into(getCode);
                mLog.e("code","-->"+school.schoolRegistCode()+"?code="+codeId);
                break;
            case R.id.school_regist_btn:
                //登录按钮
                if(isRead){
                    if(isRight()){
                        Message msg=new Message();
                        msg.what=1111;
                        handler.sendMessage(msg);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                params.clear();
                                values.clear();
                                params.add("real_name");
                                params.add("mobile");
                                params.add("qqnum");
                                params.add("wechatnum");
                                params.add("email");
                                params.add("verify");
                                params.add("province_id");
                                params.add("city_id");
                                params.add("county_id");
                                params.add("name");
                                params.add("code");
                                params.add("shortname");
                                //
                                values.add(person.getText().toString().trim());
                                values.add(phone.getText().toString().trim());
                                values.add(qq.getText().toString().trim());
                                values.add(weChat.getText().toString().trim());
                                values.add(email.getText().toString().trim());
                                values.add(code.getText().toString().trim());
                                values.add(mprovince);
                                values.add(mcity);
                                values.add(mcounty);
                                values.add(name.getText().toString().trim());
                                values.add(codeId);
                                values.add("");
                                sendReuqst("POST",2,school.schoolRegist(),params,values);
                            }
                        }).start();

                    }
                }else {
                    mToast.showText(getApplicationContext(),"您还没有同意条款与服务协议");
                }
                break;
        }
    }




    private void sendReuqst(String way, int what, String url, ArrayList<String> params, ArrayList<String> values) {
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
                    mmsg=object.getString("msg");
                    mLog.e("reponse","->"+response.get().toString());
                    if(what==1){
                        if(object.getString("code").equals("200")){
                            codeId=object.getString("data");
                        }else {
                            codeId="";
                        }
                    }else if(what==2){
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
        boolean bn=false,bp=false,bph=false,bem=false,badd=false,bcod=false;
        if(name.getText().toString().trim().equals("")){
            mToast.showText(getApplicationContext(),"用户名不能为空");
        }else {
            bn=true;
        }
        if(person.getText().toString().trim().equals("")){
            mToast.showText(getApplicationContext(),"联系人不能为空");
        }else {
            bp=true;
        }
        if(phone.getText().toString().trim().equals("")){
            mToast.showText(getApplicationContext(),"手机号码不能为空");
        }else {
            bph=true;
        }
        if(email.getText().toString().trim().equals("")){
            mToast.showText(getApplicationContext(),"邮箱不能为空");
        }else {
            bem=true;
        }
        if(address.getText().toString().trim().equals("")){
            mToast.showText(getApplicationContext(),"地址不能为空");
        }else {
            badd=true;
        }
        if(code.getText().toString().trim().equals("")){
            mToast.showText(getApplicationContext(),"验证码不能为空");
        }else {
            bcod=true;
        }
        if(bn&&bp&&bph&&bem&&badd&&bcod){
            return true;
        }else {
            return false;
        }
    }

}
