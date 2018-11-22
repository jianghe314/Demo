package com.kangzhan.student.School.SlideMenu;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.kangzhan.student.Debug.mLog;
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

import java.util.ArrayList;

public class Add_Account extends BaseActivity implements View.OnClickListener,View.OnFocusChangeListener{
    private TextView nameL,phoneL,idL,psdL,addressL,detailL,wxL,qqL,emialL,otherL;
    private EditText name,phone,Id,psd,address,detail,wx,qq,email,other;
    private View line1,line2,line3,line4,line5,line6,line7,line8,lin9,lin10;
    private Button save;
    private String mmsg;
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
                            showProgress.showProgress(Add_Account.this,"保存中...");
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
                            showMessage.showMsg(Add_Account.this,"网络连接异常，请检查网络连接");
                        }
                    });
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //备注，还有一个单位没有加
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__account);
        Toolbar toolbar= (Toolbar) findViewById(R.id.school_add_account_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        initView();
    }

    private void initView() {
        line1=findViewById(R.id.school_add_account_l1);
        line2=findViewById(R.id.school_add_account_l2);
        line3=findViewById(R.id.school_add_account_l3);
        line4=findViewById(R.id.school_add_account_l4);
        line5=findViewById(R.id.school_add_account_l5);
        line6=findViewById(R.id.school_add_account_l6);
        line7=findViewById(R.id.school_add_account_l7);
        line8=findViewById(R.id.school_add_account_l8);
        lin9=findViewById(R.id.school_add_account_9);
        lin10=findViewById(R.id.school_add_account_l10);
        nameL= (TextView) findViewById(R.id.school_add_account_n);
        idL= (TextView) findViewById(R.id.school_add_account_i);
        psdL= (TextView) findViewById(R.id.school_add_account_m);
        addressL= (TextView) findViewById(R.id.school_add_account_a);
        detailL= (TextView) findViewById(R.id.school_add_account_d);
        wxL= (TextView) findViewById(R.id.school_add_account_w);
        qqL= (TextView) findViewById(R.id.school_add_account_q);
        phoneL= (TextView) findViewById(R.id.school_add_account_p);
        emialL= (TextView) findViewById(R.id.school_add_account_em);
        otherL= (TextView) findViewById(R.id.school_add_account_o);
        //
        name= (EditText) findViewById(R.id.school_add_account_name);
        phone= (EditText) findViewById(R.id.school_add_account_phone);
        Id= (EditText) findViewById(R.id.school_add_account_id);
        psd= (EditText) findViewById(R.id.school_add_account_mima);
        address= (EditText) findViewById(R.id.school_add_account_address);
        detail= (EditText) findViewById(R.id.school_add_account_detail);
        wx= (EditText) findViewById(R.id.school_add_account_wx);
        qq= (EditText) findViewById(R.id.school_add_account_qq);
        email= (EditText) findViewById(R.id.school_add_account_email);
        other= (EditText) findViewById(R.id.school_add_account_other);
        save= (Button) findViewById(R.id.school_add_account_btn_save);
        save.setOnClickListener(this);
        //
        name.setOnFocusChangeListener(this);
        phone.setOnFocusChangeListener(this);
        Id.setOnFocusChangeListener(this);
        psd.setOnFocusChangeListener(this);
        address.setOnFocusChangeListener(this);
        detail.setOnFocusChangeListener(this);
        wx.setOnFocusChangeListener(this);
        qq.setOnFocusChangeListener(this);
        email.setOnFocusChangeListener(this);
        other.setOnFocusChangeListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.school_add_account_btn_save:
                //保存
                if(isRight()){
                    handler.sendEmptyMessage(1111);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            params.clear();
                            values.clear();
                            params.add("key");
                            params.add("id");
                            params.add("real_name");
                            params.add("phone");
                            params.add("idcard");
                            params.add("home_address");
                            params.add("email");
                            params.add("qqnum");
                            params.add("wecharnum");
                            params.add("remark");
                            params.add("status");
                            //
                            values.add(school.schoolKey(getApplicationContext()));
                            values.add("0");
                            values.add(name.getText().toString().trim());
                            values.add(phone.getText().toString().trim());
                            values.add(Id.getText().toString().trim());
                            values.add(address.getText().toString().trim()+detail.getText().toString());
                            values.add(email.getText().toString().trim());
                            values.add(qq.getText().toString().trim());
                            values.add(wx.getText().toString().trim());
                            values.add(other.getText().toString().trim());
                            values.add("1");
                            sendRequest(params,values);
                        }
                    }).start();
                }
                break;
            default:
                break;
        }
    }

    private void sendRequest(ArrayList<String> params,ArrayList<String> values) {
        Request<JSONObject> request=NoHttp.createJsonObjectRequest(school.AccountAdd(), RequestMethod.POST);
        for (int i = 0; i < params.size(); i++) {
            request.add(params.get(i),values.get(i));
            //mLog.e("params--values","-->"+params.get(i)+"--"+values.get(i));
        }
        getRequestQueue().add(1, request, new OnResponseListener<JSONObject>() {
            @Override
            public void onStart(int what) {

            }

            @Override
            public void onSucceed(int what, Response<JSONObject> response) {
                try {
                    JSONObject object=new JSONObject(response.get().toString());
                    mLog.e("reponse","-->"+response.get().toString());
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
        boolean nameb=false,phoneb=false,Idb=false,psdb=false,addressb=false,detailb=false;
        if(TextUtils.equals(name.getText().toString().trim(),"")){
            mToast.showText(Add_Account.this,"姓名不能为空");
        }else {
            nameb=true;
        }
        if(TextUtils.equals(phone.getText().toString().trim(),"")){
            mToast.showText(Add_Account.this,"手机号码不能为空");
        }else {
            phoneb=true;
        }
        if(TextUtils.equals(Id.getText().toString().trim(),"")){
            mToast.showText(Add_Account.this,"身份证号码不能为空");
        }else {
            Idb=true;
        }
        if(TextUtils.equals(psd.getText().toString().trim(),"")){
            mToast.showText(Add_Account.this,"密码不能为空");
        }else {
           psdb=true;
        }
        if(TextUtils.equals(address.getText().toString().trim(),"")){
            mToast.showText(Add_Account.this,"地址不能为空");
        }else {
            addressb=true;
        }
        if(nameb&&phoneb&&Idb&&psdb&&addressb){
            return true;
        }else {
            return false;
        }
    }


    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()){
            case R.id.school_add_account_name:
                if(hasFocus){
                    setTextViewColor(nameL);
                    setTextViewColor(name);
                    setLineColor(line1);
                }else {
                    clearLineColor(line1);
                    clearTextViewColor(nameL);
                    clearTextViewColor(name);
                }
                break;
            case R.id.school_add_account_phone:
                if(hasFocus){
                    setTextViewColor(phone);
                    setTextViewColor(phoneL);
                    setLineColor(line2);
                }else {
                    clearLineColor(line2);
                    clearTextViewColor(phone);
                    clearTextViewColor(phoneL);
                }
                break;
            case R.id.school_add_account_id:
                if(hasFocus){
                    setTextViewColor(Id);
                    setTextViewColor(idL);
                    setLineColor(line3);
                }else {
                    clearLineColor(line3);
                    clearTextViewColor(Id);
                    clearTextViewColor(idL);
                }
                break;
            case R.id.school_add_account_mima:
                if(hasFocus){
                    setTextViewColor(psd);
                    setTextViewColor(psdL);
                    setLineColor(line4);
                }else {
                    clearLineColor(line4);
                    clearTextViewColor(psd);
                    clearTextViewColor(psdL);
                }
                break;
            case R.id.school_add_account_email:
                if(hasFocus){
                    setTextViewColor(email);
                    setTextViewColor(emialL);
                    setLineColor(lin9);
                }else {
                    clearLineColor(lin9);
                    clearTextViewColor(emialL);
                    clearTextViewColor(email);
                }
                break;
            case R.id.school_add_account_address:
                if(hasFocus){
                    setTextViewColor(address);
                    setTextViewColor(addressL);
                    setLineColor(line5);
                }else {
                    clearLineColor(line5);
                    clearTextViewColor(address);
                    clearTextViewColor(addressL);
                }
                break;
            case R.id.school_add_account_detail:
                if(hasFocus){
                    setTextViewColor(detail);
                    setTextViewColor(detailL);
                    setLineColor(line6);
                }else {
                    clearLineColor(line6);
                    clearTextViewColor(detail);
                    clearTextViewColor(detailL);
                }
                break;
            case R.id.school_add_account_wx:
                if(hasFocus){
                    setTextViewColor(wx);
                    setTextViewColor(wxL);
                    setLineColor(line7);
                }else {
                    clearLineColor(line7);
                    clearTextViewColor(wx);
                    clearTextViewColor(wxL);
                }
                break;
            case R.id.school_add_account_qq:
                if(hasFocus){
                    setTextViewColor(qq);
                    setTextViewColor(qqL);
                    setLineColor(line8);
                }else {
                    clearLineColor(line8);
                    clearTextViewColor(qq);
                    clearTextViewColor(qqL);
                }
                break;
            case R.id.school_add_account_other:
                if(hasFocus){
                    setTextViewColor(other);
                    setTextViewColor(otherL);
                    setLineColor(lin10);
                }else {
                    clearLineColor(lin10);
                    clearTextViewColor(otherL);
                    clearTextViewColor(other);
                }
                break;
        }
    }

    private void setTextViewColor(TextView txt){
        txt.setTextColor(ContextCompat.getColor(Add_Account.this,R.color.text_background_color_aqua));
    }
    private void setLineColor(View view){
        view.setBackgroundColor(ContextCompat.getColor(Add_Account.this,R.color.text_background_color_aqua));
    }
    private void clearLineColor(View view){
        view.setBackgroundColor(ContextCompat.getColor(Add_Account.this,R.color.color_line));
    }
    private void clearTextViewColor(TextView txt){
        txt.setTextColor(ContextCompat.getColor(Add_Account.this,R.color.textColor_gray));
    }

}
