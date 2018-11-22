package com.kangzhan.student.Student.person_data_activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.google.gson.Gson;
import com.kangzhan.student.Debug.mLog;
import com.kangzhan.student.R;
import com.kangzhan.student.ShowUI.mToast;
import com.kangzhan.student.ShowUI.showMessage;
import com.kangzhan.student.ShowUI.showProgress;
import com.kangzhan.student.com.BaseActivity;
import com.kangzhan.student.com.RegistExchangeActivity;
import com.kangzhan.student.mInterface.StudentInterface.student;
import com.kangzhan.student.mInterface.TeacherInterface.teacher;
import com.kangzhan.student.mInterface.payBackInterface.PayBack;
import com.kangzhan.student.wxapi.WXpay;
import com.kangzhan.student.zfbapi.AliPay;
import com.kangzhan.student.zfbapi.OrderInfoUtil;
import com.kangzhan.student.zfbapi.PayResult;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

public class PurseExchargeActicity extends BaseActivity implements View.OnClickListener{
    private TextView Num1,Num2,Num5,Num11;
    private EditText Numo;
    private LinearLayout zfb,wechat;
    private ImageView zfb_iv,wc_iv;
    private Button send;
    private WXpay wXpay;
    private AliPay aliPay;
    private Gson gson;
    private String price,mmsg,who;
    private int payWay;
    private boolean isother=false;
    //
    private static final int SDK_PAY_FLAG = 1;
    private static final int ZFB_PAY_FAIL = 2;
    private static final int ZFB_PAY_SUCCESS = 3;
    private ArrayList<String> params=new ArrayList<>();
    private ArrayList<String> values=new ArrayList<>();
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1000:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.showProgress(PurseExchargeActicity.this,"支付中...");
                        }
                    });
                    break;
                case 1011:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.closeProgress();
                        }
                    });
                    break;
                case 1111:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.closeProgress();
                        }
                    });
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            startWXPay();
                        }
                    }).start();
                    break;
                case 2222:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.closeProgress();
                        }
                    });
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            startZfbPay();
                        }
                    }).start();
                    break;
                case SDK_PAY_FLAG:
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);

                    String resultStatus=payResult.getResultStatus();
                    //判断resultStatus为9000代表支付成功
                    if(TextUtils.equals(resultStatus,"9000")){
                        //本地显示支付成功，回调服务端是否成功
                        showProgress.showProgress(PurseExchargeActicity.this,"支付中...");
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                QueryServerInfo();
                            }
                        }).start();
                    }else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                                showMessage.showMsg(PurseExchargeActicity.this,"支付失败");
                            }
                        });
                    }
                    break;
                case ZFB_PAY_SUCCESS:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.closeProgress();
                            showMessage.showMsg(PurseExchargeActicity.this,"支付成功");
                        }
                    });
                    break;
                case ZFB_PAY_FAIL:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.closeProgress();
                            showMessage.showMsg(PurseExchargeActicity.this,mmsg);
                        }
                    });
                    break;
                case 9999:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showMessage.showMsg(PurseExchargeActicity.this,"网络连接异常，请检查网络连接");
                        }
                    });
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purse_excharge_acticity);
        Toolbar toolbar= (Toolbar) findViewById(R.id.student_PurseExcgharge);
        setSupportActionBar(toolbar);
        ActionBar actionbar=getSupportActionBar();
        //设置返回箭头
        if(actionbar!=null){
            actionbar.setDisplayHomeAsUpEnabled(true);
        }
        Intent getWho=getIntent();
        who=getWho.getStringExtra("who");
        gson=new Gson();
        initView();
    }

    private void initView() {
        Num1= (TextView) findViewById(R.id.purse_excharge_1);
        Num1.setOnClickListener(this);
        Num2= (TextView) findViewById(R.id.purse_excharge_2);
        Num2.setOnClickListener(this);
        Num5= (TextView) findViewById(R.id.purse_excharge_5);
        Num5.setOnClickListener(this);
        Num11= (TextView) findViewById(R.id.purse_excharge_11);
        Num11.setOnClickListener(this);
        Numo= (EditText) findViewById(R.id.purse_excharge_o);
        Numo.setOnClickListener(this);
        zfb= (LinearLayout) findViewById(R.id.purse_choice_zfb);
        zfb.setOnClickListener(this);
        wechat= (LinearLayout) findViewById(R.id.purse_choice_wecaht);
        wechat.setOnClickListener(this);
        zfb_iv= (ImageView) findViewById(R.id.purse_excharge_zfb);
        wc_iv= (ImageView) findViewById(R.id.purse_excharge_wechat);
        send= (Button) findViewById(R.id.purse_excharg_btn);
        send.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.purse_excharge_1:
                isother=false;
                Numo.setText("");
                setTextBackgroundColor();
                Num1.setBackgroundResource(R.drawable.excharge_background1);
                price=Num1.getText().toString().trim();
                break;
            case R.id.purse_excharge_2:
                isother=false;
                Numo.setText("");
                setTextBackgroundColor();
                Num2.setBackgroundResource(R.drawable.excharge_background1);
                price=Num2.getText().toString().trim();
                break;
            case R.id.purse_excharge_5:
                isother=false;
                Numo.setText("");
                setTextBackgroundColor();
                Num5.setBackgroundResource(R.drawable.excharge_background1);
                price=Num5.getText().toString().trim();
                break;
            case R.id.purse_excharge_11:
                isother=false;
                Numo.setText("");
                setTextBackgroundColor();
                Num11.setBackgroundResource(R.drawable.excharge_background1);
                price=Num11.getText().toString().trim();
                break;
            case R.id.purse_excharge_o:
                isother=true;
                setTextBackgroundColor();
                Numo.setBackgroundResource(R.drawable.excharge_background1);
                break;
            case R.id.purse_choice_zfb:
                payWay=10;
                zfb_iv.setImageResource(R.mipmap.pay_confirm1);
                wc_iv.setImageResource(R.mipmap.pay_confirm0);
                break;
            case R.id.purse_choice_wecaht:
                payWay=20;
                wc_iv.setImageResource(R.mipmap.pay_confirm1);
                zfb_iv.setImageResource(R.mipmap.pay_confirm0);
                break;
            case R.id.purse_excharg_btn:
                //支付
                handler.sendEmptyMessage(1000);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if(who.equals("student")){
                            if(isother){
                                params.clear();
                                values.clear();
                                params.add("key");
                                params.add("type");
                                params.add("name");
                                params.add("price");
                                params.add("order_type");
                                //
                                values.add(student.studentKey(PurseExchargeActicity.this));
                                values.add(payWay+"");
                                values.add("充值");
                                values.add(price=Numo.getText().toString().trim());
                                values.add(40+"");
                                sendRequest("POST",student.studentMPay(),payWay,params,values);
                            }else {
                                params.clear();
                                values.clear();
                                params.add("key");
                                params.add("type");
                                params.add("name");
                                params.add("price");
                                params.add("order_type");
                                //
                                values.add(student.studentKey(PurseExchargeActicity.this));
                                values.add(payWay+"");
                                values.add("充值");
                                values.add(price);
                                values.add(40+"");
                                sendRequest("POST",student.studentMPay(),payWay,params,values);
                            }
                        }else if(who.equals("teacher")){
                            if(isother){
                                params.clear();
                                values.clear();
                                params.add("key");
                                params.add("type");
                                params.add("name");
                                params.add("price");
                                params.add("order_type");
                                //
                                values.add(teacher.teacherKey(PurseExchargeActicity.this));
                                values.add(payWay+"");
                                values.add("充值");
                                values.add(price=Numo.getText().toString().trim());
                                values.add(20+"");
                                sendRequest("POST",teacher.teacherPay(),payWay,params,values);
                            }else {
                                params.clear();
                                values.clear();
                                params.add("key");
                                params.add("type");
                                params.add("name");
                                params.add("price");
                                params.add("order_type");
                                //
                                values.add(teacher.teacherKey(PurseExchargeActicity.this));
                                values.add(payWay+"");
                                values.add("充值");
                                values.add(price);
                                values.add(20+"");
                                sendRequest("POST",teacher.teacherPay(),payWay,params,values);
                            }
                        }

                    }
                }).start();
                break;
        }
    }

    private void sendRequest(int payWay,String price) {
        Request<JSONObject> request=NoHttp.createJsonObjectRequest(student.studentMPay(), RequestMethod.POST);
        request.add("key",student.studentKey(this));
        request.add("type",payWay);
        request.add("name","充值");
        request.add("price",price);
        request.add("order_type",40);
        getRequestQueue().add(payWay, request, new OnResponseListener<JSONObject>() {
            @Override
            public void onStart(int what) {

            }

            @Override
            public void onSucceed(int what, Response<JSONObject> response) {
                try {
                    JSONObject object=new JSONObject(response.get().toString());
                    //微信
                    if(what==20){
                        Log.e("充值微信","->"+response.get().toString());
                        wXpay=gson.fromJson(object.getString("data"),WXpay.class);
                        saveTradeId(wXpay.getOut_trade_no());
                        //发送请求调微信支付
                        handler.sendEmptyMessage(1111);
                    }else if(what==10){
                        //支付宝
                        Log.e("充值支付宝","->"+response.get().toString());
                        aliPay=gson.fromJson(object.getString("data"),AliPay.class);
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

    private void sendRequest(String Way,String url,int what,ArrayList<String> params,ArrayList<String> values) {
        RequestMethod method=null;
        if(Way.equals("POST")){
            method=RequestMethod.POST;
        }else if(Way.equals("GET")){
            method=RequestMethod.GET;
        }
        Request<JSONObject> request=NoHttp.createJsonObjectRequest(url,method);
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
                    if(what==20){
                        mLog.e("充值微信","->"+response.get().toString());
                        wXpay=gson.fromJson(object.getString("data"),WXpay.class);
                        saveTradeId(wXpay.getOut_trade_no());
                        //发送请求调微信支付
                        handler.sendEmptyMessage(1111);
                    }else if(what==10){
                        //支付宝
                        mLog.e("充值支付宝","->"+response.get().toString());
                        aliPay=gson.fromJson(object.getString("data"),AliPay.class);
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

    private void startWXPay() {
        IWXAPI mWxApi= WXAPIFactory.createWXAPI(PurseExchargeActicity.this,wXpay.getAppid(),true);
        mWxApi.registerApp(wXpay.getAppid());
        //挑起微信支付，设置参数
        PayReq req=new PayReq();
        req.appId=wXpay.getAppid();
        req.partnerId=wXpay.getPartnerid();
        req.prepayId=wXpay.getPrepayid();
        req.packageValue=wXpay.getMpackage();
        req.nonceStr=wXpay.getNoncestr();
        req.timeStamp=wXpay.getTimestamp();
        req.sign=wXpay.getSign();
        boolean resulet=mWxApi.sendReq(req);
//        //是否调起微信支付
//        Log.e("调起结果","->"+resulet);
    }

    private void startZfbPay() {
        Map<String,String> params= OrderInfoUtil.builderOrderParamMap(aliPay);
        String orderParam=OrderInfoUtil.buildOrderParam(params);
        String sign=OrderInfoUtil.getSign(params,aliPay.getPrivatekey(),true);
        String orderInfo=orderParam+"&"+sign;
        //开始支付
        PayTask alipay=new PayTask(PurseExchargeActicity.this);
        Map<String,String> result=alipay.payV2(orderInfo,true);
        mLog.e("msp","---->"+result.toString());
        Message msg=new Message();
        msg.what=SDK_PAY_FLAG;
        msg.obj=result;
        handler.sendMessage(msg);
    }

    private void QueryServerInfo() {
        Request<JSONObject> request=NoHttp.createJsonObjectRequest(PayBack.stuPaybackZfb(),RequestMethod.GET);
        request.add("Out_trade_no",aliPay.getBiz_content().getOut_trade_no());
        getRequestQueue().add(11, request, new OnResponseListener<JSONObject>() {
            @Override
            public void onStart(int what) {

            }

            @Override
            public void onSucceed(int what, Response<JSONObject> response) {
                try {
                    JSONObject object=new JSONObject(response.get().toString());
                    mmsg=object.getString("msg");
                    if(object.getString("code").equals("200")){
                        JSONObject obj=new JSONObject(object.getString("data"));
                        JSONObject obj1=new JSONObject(obj.getString("alipay_trade_query_response"));
                        if(obj1.getString("trade_status").equals("TRADE_SUCCESS")){
                            //服务器端支付成功
                            handler.sendEmptyMessage(ZFB_PAY_SUCCESS);
                        }else {
                            handler.sendEmptyMessage(ZFB_PAY_FAIL);
                        }
                    }else {
                        handler.sendEmptyMessage(ZFB_PAY_FAIL);
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

    //颜色更改
    private void setTextBackgroundColor(){
        Num1.setBackgroundResource(R.drawable.text_background5);
        Num2.setBackgroundResource(R.drawable.text_background5);
        Num5.setBackgroundResource(R.drawable.text_background5);
        Num11.setBackgroundResource(R.drawable.text_background5);
        Numo.setBackgroundResource(R.drawable.text_background5);
    }

    private void saveTradeId(String tradeId){
        SharedPreferences sp=getSharedPreferences("Trade_no",MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putString("TradeId",tradeId);
        editor.apply();
    }


}
