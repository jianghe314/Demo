package com.kangzhan.student.com;

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
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.google.gson.Gson;
import com.kangzhan.student.MainLoginActivity;
import com.kangzhan.student.R;
import com.kangzhan.student.ShowUI.mToast;
import com.kangzhan.student.ShowUI.showProgress;
import com.kangzhan.student.Student.person_data_activity.PurseExchargeActicity;
import com.kangzhan.student.mInterface.StudentInterface.student;
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

import java.util.Map;

public class RegistExchangeActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView price1,price2,price3,price4;
    private LinearLayout zfb,weChat;
    private ImageView zfb_iv,wc_iv;
    private String  mprice,mmsg;
    private Button send;
    private RequestQueue requestQueue;
    private Gson gson;
    private int payWay;
    private String key,Info;
    private WXpay mWxPay;
    private AliPay aliPay;
    //
    private static final int SDK_PAY_FLAG = 1;
    private static final int ZFB_PAY_FAIL = 2;
    private static final int ZFB_PAY_SUCCESS = 3;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1111:
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            WXpay();
                        }
                    }).start();
                    break;
                case 1122:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mToast.showText(RegistExchangeActivity.this,mmsg);
                        }
                    });
                    break;
                case 2222:
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            ALIpay();
                        }
                    }).start();
                    break;
                case 2233:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mToast.showText(RegistExchangeActivity.this,mmsg);
                        }
                    });
                    break;
                case SDK_PAY_FLAG:
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    String resultInfo=payResult.getResult();        //同步返回需要验证的信息
                    Log.e("支付同步返回结果","--->"+resultInfo);
                    String resultStatus=payResult.getResultStatus();
                    //判断resultStatus为9000代表支付成功
                    if(TextUtils.equals(resultStatus,"9000")){
                        //如果本地返回9000，查询服务端支付情况
                        showProgress.showProgress(RegistExchangeActivity.this,"支付中...");
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                QueryZfb(aliPay.getBiz_content().getOut_trade_no());
                            }
                        }).start();
                    }else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                                mToast.showText(RegistExchangeActivity.this,"支付失败");
                            }
                        });
                    }
                    break;
                case ZFB_PAY_SUCCESS:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.closeProgress();
                            ShowDialog(getRegistInfo()[0],getRegistInfo()[1]);
                        }
                    });
                    break;
                case ZFB_PAY_FAIL:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.closeProgress();
                            mToast.showText(RegistExchangeActivity.this,mmsg);
                        }
                    });
                    break;
                case 9999:
                    AlertDialog.Builder builder=new AlertDialog.Builder(RegistExchangeActivity.this);
                    builder.setMessage("网络连接异常，请检查网络连接");
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.create().show();
                    break;
                default:
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist_exchange);
        Toolbar toolbar= (Toolbar) findViewById(R.id.RegistExchange_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        NoHttp.initialize(RegistExchangeActivity.this);
        gson=new Gson();
        initView();
    }

    private void initView() {
        price1= (TextView) findViewById(R.id.reg_excharge_1);
        price2= (TextView) findViewById(R.id.reg_excharge_2);
        price3= (TextView) findViewById(R.id.reg_excharge_5);
        price4= (TextView) findViewById(R.id.reg_excharge_11);
        zfb= (LinearLayout) findViewById(R.id.reg_choice_zfb);
        weChat= (LinearLayout) findViewById(R.id.reg_choice_wecaht);
        send= (Button) findViewById(R.id.reg_excharg_btn);
        zfb_iv= (ImageView) findViewById(R.id.reg_excharge_zfb);
        wc_iv= (ImageView) findViewById(R.id.reg_excharge_wechat);
        send.setOnClickListener(this);
        zfb.setOnClickListener(this);
        weChat.setOnClickListener(this);
        price1.setOnClickListener(this);
        price2.setOnClickListener(this);
        price3.setOnClickListener(this);
        price4.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.reg_excharge_1:
                mprice="0.1";
                setTextBackground();
                price1.setBackgroundResource(R.drawable.excharge_background1);
                break;
            case R.id.reg_excharge_2:
                mprice="500";
                setTextBackground();
                price2.setBackgroundResource(R.drawable.excharge_background1);
                break;
            case R.id.reg_excharge_5:
                mprice="800";
                setTextBackground();
                price3.setBackgroundResource(R.drawable.excharge_background1);
                break;
            case R.id.reg_excharge_11:
                mprice="1000";
                setTextBackground();
                price4.setBackgroundResource(R.drawable.excharge_background1);
                break;
            case R.id.reg_choice_zfb:
                payWay=10;
                zfb_iv.setImageResource(R.mipmap.pay_confirm1);
                wc_iv.setImageResource(R.mipmap.pay_confirm0);
                break;
            case R.id.reg_choice_wecaht:
                payWay=20;
                zfb_iv.setImageResource(R.mipmap.pay_confirm0);
                wc_iv.setImageResource(R.mipmap.pay_confirm1);
                break;
            case R.id.reg_excharg_btn:
                Log.e("PayKey","->"+getRegistKey());
                sendInfoToServer(getRegistKey(),payWay,mprice);
                break;
            default:
                break;
        }
    }


    private void WXpay() {
        IWXAPI wxApi= WXAPIFactory.createWXAPI(RegistExchangeActivity.this,mWxPay.getAppid(),true);
        wxApi.registerApp(mWxPay.getAppid());
        PayReq req=new PayReq();
        req.appId=mWxPay.getAppid();
        req.sign=mWxPay.getSign();
        req.timeStamp=mWxPay.getTimestamp();
        req.nonceStr=mWxPay.getNoncestr();
        req.packageValue=mWxPay.getMpackage();
        req.prepayId=mWxPay.getPrepayid();
        req.partnerId=mWxPay.getPartnerid();
        boolean result=wxApi.sendReq(req);
        Log.e("是否挑起微信","->"+result);
    }


    private void ALIpay() {
        Map<String,String> params= OrderInfoUtil.builderOrderParamMap(aliPay);
        String orderParam=OrderInfoUtil.buildOrderParam(params);
        String sign=OrderInfoUtil.getSign(params,aliPay.getPrivatekey(),true);
        String orderInfo=orderParam+"&"+sign;
        //开始支付
        PayTask alipay=new PayTask(RegistExchangeActivity.this);
        Map<String,String> result=alipay.payV2(orderInfo,true);
        Log.e("msp","---->"+result.toString());
        Message msg=new Message();
        msg.what=SDK_PAY_FLAG;
        msg.obj=result;
        handler.sendMessage(msg);
    }

    private void QueryZfb(String trade_no) {
        Request<JSONObject> request=NoHttp.createJsonObjectRequest(PayBack.stuPaybackZfb(),RequestMethod.GET);
        request.add("Out_trade_no",trade_no);
        requestQueue.add(5, request, new OnResponseListener<JSONObject>() {
            @Override
            public void onStart(int what) {

            }

            @Override
            public void onSucceed(int what, Response<JSONObject> response) {
                Log.e("支付宝回调结果","->"+response.get().toString());
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


    private void sendInfoToServer(String mkey,int payWay,String price) {
        requestQueue=NoHttp.newRequestQueue();
        Request<JSONObject> request=NoHttp.createJsonObjectRequest(student.studentMPay(), RequestMethod.POST);
        request.add("key",mkey);
        request.add("type",payWay);
        request.add("name","报名定金");
        request.add("price",price);
        request.add("order_type",10);
        requestQueue.add(payWay, request, new OnResponseListener<JSONObject>() {
            @Override
            public void onStart(int what) {

            }

            @Override
            public void onSucceed(int what, Response<JSONObject> response) {
                Log.e("报名充值","->"+response.get().toString());
                if(what==20){
                    //微信支付
                    try {
                        JSONObject object=new JSONObject(response.get().toString());
                        mmsg=object.getString("msg");
                        Log.e("registResult","->"+object.toString());
                        if(object.getString("code").equals("200")){
                            mWxPay=gson.fromJson(object.getString("data"),WXpay.class);
                            saveTradeId(mWxPay.getOut_trade_no());
                            //发送请求调微信支付
                            handler.sendEmptyMessage(1111);
                        }else {
                            //服务器异常情况
                            handler.sendEmptyMessage(1122);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }else if(what==10){
                    //支付宝
                    try {
                        JSONObject object=new JSONObject(response.get().toString());
                        mmsg=object.getString("msg");
                        Log.e("registResult","->"+object.toString());
                        if(object.getString("code").equals("200")){
                            aliPay=gson.fromJson(object.getString("data"),AliPay.class);
                            //发送请求支付宝支付
                            //Log.i("AliPay--->>",""+aliPay.toString());
                           // Log.i("Biz_content","--->>"+aliPay.getBiz_content().toString());
                            handler.sendEmptyMessage(2222);
                        }else {
                            //服务器异常情况
                            handler.sendEmptyMessage(2233);
                        }
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

    private void setTextBackground() {
        price1.setBackgroundResource(R.drawable.text_background5);
        price2.setBackgroundResource(R.drawable.text_background5);
        price3.setBackgroundResource(R.drawable.text_background5);
        price4.setBackgroundResource(R.drawable.text_background5);
    }


    private void saveTradeId(String tradeId){
        SharedPreferences sp=getSharedPreferences("Trade_no",MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putString("TradeId",tradeId);
        editor.apply();
    }

    private void ShowDialog(String name,String phone){
        //dialog有一个灰色底边
        AlertDialog.Builder builder=new AlertDialog.Builder(RegistExchangeActivity.this);
        View dialog=getLayoutInflater().inflate(R.layout.item_regist_dialog,null);
        TextView title= (TextView) dialog.findViewById(R.id.item_regist_dialog_title);
        title.setText("充值成功！");
        TextView info= (TextView) dialog.findViewById(R.id.item_regist_dialog_info);
        String Info="业务员<font color='#ff001e'>"+name+"</font>(电话<font color='#ff001e'>"+phone+"</font>)";
        info.setText(Html.fromHtml(Info));
        ImageView back= (ImageView) dialog.findViewById(R.id.item_regist_dialog_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent star=new Intent(RegistExchangeActivity.this, MainLoginActivity.class);
                startActivity(star);
                finish();
            }
        });
        builder.setView(dialog);
        builder.setCancelable(true);
        builder.create().show();
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
        }
        return true;
    }


    private String getRegistKey(){
        SharedPreferences sp=getSharedPreferences("RegistKey",MODE_PRIVATE);
        return sp.getString("Regist_key","");
    }
    private String[] getRegistInfo(){
        String[] mm = new String[2];
        SharedPreferences sp=getSharedPreferences("RegistInfo",MODE_PRIVATE);
        String data= sp.getString("Regist_Info","");
        if(!data.equals("")){
            try {
                JSONObject object=new JSONObject(data);
                mm[0]=object.getString("real_name");
                mm[1]=object.getString("phone");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return mm;
    }

}
