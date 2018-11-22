package com.kangzhan.student.Student.Train;

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
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.google.gson.Gson;
import com.kangzhan.student.Debug.mLog;
import com.kangzhan.student.R;
import com.kangzhan.student.ShowUI.mToast;
import com.kangzhan.student.ShowUI.showMessage;
import com.kangzhan.student.ShowUI.showProgress;
import com.kangzhan.student.Student.bean.Pay_item_train;
import com.kangzhan.student.Student.person_data_activity.PurseExchargeActicity;
import com.kangzhan.student.com.BaseActivity;
import com.kangzhan.student.com.RegistExchangeActivity;
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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class PayActivity extends BaseActivity implements View.OnClickListener{
    private CircleImageView header;
    private RelativeLayout rela;
    private LinearLayout wc,zfb;
    private TextView lesson,name,time,carLable,should_pay,address, price,purse;
    private ImageView sex,choice_squre, choice_wc,chocie_zfb;
    private Button payBtn;
    private String Id,mmsg,mprice,payType,payWay="";
    private Pay_item_train payitem;
    private Gson gson;
    private WXpay wXpay;
    private AliPay aliPay;
    private boolean isEnough=false;          //默认余额不足支付订单
    private boolean isPurse=false;          //默认不适用钱包余额支付
    private ArrayList<String> params=new ArrayList<>();
    private ArrayList<String> values=new ArrayList<>();
    //
    private static final int SDK_PAY_FLAG = 1;
    private static final int ZFB_PAY_FAIL = 2;
    private static final int ZFB_PAY_SUCCESS = 3;

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0000:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.showProgress(PayActivity.this,"加载中...");
                        }
                    });
                    break;
                case 1111:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.closeProgress();
                            lesson.setText(payitem.getDetail());
                            if(payitem.getSex().equals("1")){
                                sex.setImageResource(R.mipmap.boy);
                            }else {
                                sex.setImageResource(R.mipmap.girl);
                            }
                            name.setText(payitem.getCoachname());
                            time.setText(payitem.getStart_time()+" "+payitem.getEnd_time());
                            carLable.setText(payitem.getCar_licnum());
                            address.setText(payitem.getAddress());
                            should_pay.setText("￥"+payitem.getPrice());

                        }
                    });
                    break;
                case 0011:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            float jiage=Float.valueOf(payitem.getPrice());     //价格
                            float mpay=Float.valueOf(mprice);                  //账号余额
                            float result=mpay-jiage;
                            Log.e("需要支付的价格","->"+result);
                            //显示余额，当余额足时微信和支付宝不能点
                            purse.setText("￥"+mprice);
                            //余额不足
                            if(result<0){
                                isEnough=true;
                                rela.setVisibility(View.VISIBLE);
                                DecimalFormat decimalFormat=new DecimalFormat(".00");
                                price.setText("￥"+decimalFormat.format(Math.abs(result)));
                            }else {
                                //余额足显示余额，其他方式不能点击
                                wc.setEnabled(false);
                                zfb.setEnabled(false);
                            }
                        }
                    });
                    break;
                case 0012:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mToast.showText(PayActivity.this,mmsg);
                        }
                    });
                    break;
                case 2222:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.closeProgress();
                            mToast.showText(PayActivity.this,mmsg);
                        }
                    });
                    break;
                case 3333:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.closeProgress();
                        }
                    });
                    break;
                //付款耗时
                case 4444:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.showProgress(PayActivity.this,"支付中...");
                        }
                    });
                    break;
                case 4455:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.closeProgress();
                        }
                    });
                    break;
                //付款结果
                case 5555:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.closeProgress();
                            AlertDialog.Builder builder=new AlertDialog.Builder(PayActivity.this);
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
                case 5544:
                    //挑起微信支付
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.closeProgress();
                        }
                    });
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            StartWxPay();
                        }
                    }).start();
                    break;
                case 6666:
                    //挑起支付宝支付
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.closeProgress();
                        }
                    });
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            StartZfbPay();
                        }
                    }).start();
                    break;
                case 6677:
                    //获取支付宝,微信订单信息失败
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mToast.showText(PayActivity.this,mmsg);
                        }
                    });
                    break;
                case SDK_PAY_FLAG:
                    //支付宝支付显示结果
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    String resultInfo=payResult.getResult();        //同步返回需要验证的信息
                    mLog.e("支付同步返回结果","--->"+resultInfo);
                    String resultStatus=payResult.getResultStatus();
                    //判断resultStatus为9000代表支付成功
                    if(TextUtils.equals(resultStatus,"9000")){
                        //本地支付成功，回调服务端
                        showProgress.showProgress(PayActivity.this,"支付中...");
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
                                // 该笔订单真实的支付结果，需要依赖服务端的异步通
                                showMessage.showMsg(PayActivity.this,"支付失败");
                            }
                        });
                    }
                    break;
                case ZFB_PAY_SUCCESS:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.closeProgress();
                            showMessage.showMsg(PayActivity.this,"支付成功");
                        }
                    });
                    break;
                case ZFB_PAY_FAIL:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.closeProgress();
                            mToast.showText(PayActivity.this,mmsg);
                        }
                    });
                    break;
                case 9999:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.closeProgress();
                            AlertDialog.Builder builder=new AlertDialog.Builder(PayActivity.this);
                            builder.setMessage("网络连接异常，请检测网络连接");
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
            }
        }
    };




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        Toolbar toolbar= (Toolbar) findViewById(R.id.student_pay_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        Intent getIntent=getIntent();
        Id=getIntent.getStringExtra("Id");
        payType=getIntent.getStringExtra("Type");
        gson=new Gson();
        initView();
        handler.sendEmptyMessage(0000);
        params.add("key");
        params.add("id");
        values.add(student.studentKey(PayActivity.this));
        values.add(Id);
        new Thread(new Runnable() {
            @Override
            public void run() {
                sendOrgetRequest("GET",1,student.studentGetTrainRemarkInfo(),params,values);
            }
        }).start();
    }




    private void initView() {
        header= (CircleImageView) findViewById(R.id.item_pay_item_header);
        lesson= (TextView) findViewById(R.id.item_pay_item_lesson);
        sex= (ImageView) findViewById(R.id.item_pay_item_sex);
        name= (TextView) findViewById(R.id.item_pay_item_name);
        time= (TextView) findViewById(R.id.item_pay_item_time);
        carLable= (TextView) findViewById(R.id.item_pay_item_calLabel);
        should_pay= (TextView) findViewById(R.id.item_pay_item_should_pay);
        address= (TextView) findViewById(R.id.item_pay_item_address);
        //
        purse= (TextView) findViewById(R.id.item_pay_item_price);
        choice_squre= (ImageView) findViewById(R.id.item_pay_item_choice_purse);
        choice_squre.setOnClickListener(this);

        rela= (RelativeLayout) findViewById(R.id.student_pay_more);
        rela.setVisibility(View.GONE);
        price= (TextView) findViewById(R.id.student_pay_price);
        wc= (LinearLayout) findViewById(R.id.student_pay_wc);
        wc.setOnClickListener(this);
        zfb= (LinearLayout) findViewById(R.id.student_pay_zfb);
        zfb.setOnClickListener(this);
        choice_wc= (ImageView) findViewById(R.id.student_pay_wc_choiceIv);
        chocie_zfb= (ImageView) findViewById(R.id.student_zfb_choiceIv);
        payBtn= (Button) findViewById(R.id.student_pay);
        payBtn.setOnClickListener(this);
    }
    //网络请求封装
    private void sendOrgetRequest(String requestWay,int what,String url,ArrayList<String> params,ArrayList<String> values){
        RequestMethod method=null;
        if(requestWay.equals("GET")){
            method=RequestMethod.GET;
        }else if(requestWay.equals("POST")){
            method=RequestMethod.POST;
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
                if(what==1){
                    try {
                        JSONObject object=new JSONObject(response.get().toString());
                        String code=object.getString("code");
                        mmsg=object.getString("msg");
                        if(code.equals("200")){
                            payitem=gson.fromJson(object.getJSONObject("data").toString(),Pay_item_train.class);
                            handler.sendEmptyMessage(1111);
                        }else {
                            handler.sendEmptyMessage(2222);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else if(what==2){
                    //查看钱包余额
                    try {
                        JSONObject object=new JSONObject(response.get().toString());
                        String code=object.getString("code");
                        mmsg=object.getString("msg");
                        if(code.equals("200")){
                           mprice=object.getString("data");
                            handler.sendEmptyMessage(0011);
                        }else {
                            handler.sendEmptyMessage(0012);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else if(what==20){           //20:微信   10：支付宝  30：余额
                    mLog.e("WcpayResult","->"+response.get().toString());
                    try {
                        JSONObject object=new JSONObject(response.get().toString());
                        mmsg=object.getString("msg");
                        if(object.getString("code").equals("200")){
                            wXpay=gson.fromJson(object.getString("data"),WXpay.class);
                            saveTradeId(wXpay.getOut_trade_no());
                            //挑起微信支付；
                           handler.sendEmptyMessage(5544);
                        }else {
                            handler.sendEmptyMessage(6677);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else if(what==10){
                    mLog.e("zfbpayResult","->"+response.get().toString());
                    try {
                        JSONObject object=new JSONObject(response.get().toString());
                        mmsg=object.getString("msg");
                        if(object.getString("code").equals("200")){
                            aliPay=gson.fromJson(object.getString("data"),AliPay.class);
                            handler.sendEmptyMessage(6666);
                        }else {
                            handler.sendEmptyMessage(6677);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else if(what==30){         //余额支付
                    mLog.e("MyPurseResult","->"+response.get().toString());
                    try {
                        JSONObject object=new JSONObject(response.get().toString());
                        mmsg=object.getString("msg");
                        handler.sendEmptyMessage(5555);
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

    private void StartWxPay() {
        IWXAPI mWxApi= WXAPIFactory.createWXAPI(PayActivity.this,wXpay.getAppid(),true);
        mWxApi.registerApp(wXpay.getAppid());
        mLog.e("wxResult","->"+wXpay.getAppid()+"-"+wXpay.getPartnerid()+"-"+wXpay.getPrepayid()+"-"+wXpay.getNoncestr()+"-"+wXpay.getTimestamp()+"-"+wXpay.getMpackage()+"-"+wXpay.getSign());

        PayReq req=new PayReq();
        req.appId=wXpay.getAppid();
        req.partnerId=wXpay.getPartnerid();
        req.prepayId=wXpay.getPrepayid();
        req.nonceStr=wXpay.getNoncestr();
        req.timeStamp=wXpay.getTimestamp();
        req.packageValue=wXpay.getMpackage();
        req.sign=wXpay.getSign();
        boolean result=mWxApi.sendReq(req);
//        Log.e("发起微信支付结果","->"+result);
    }

    private void StartZfbPay() {
        Map<String,String> params= OrderInfoUtil.builderOrderParamMap(aliPay);
        String orderParam=OrderInfoUtil.buildOrderParam(params);
        String sign=OrderInfoUtil.getSign(params,aliPay.getPrivatekey(),true);
        String orderInfo=orderParam+"&"+sign;
        //开始支付
        PayTask alipay=new PayTask(PayActivity.this);
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




    @Override
    public void onClick(View v) {
        //三个按钮做成单选按钮
        switch (v.getId()){
            //使用钱包付款
            case R.id.item_pay_item_choice_purse:
                //查看钱包还剩多少
                setImageViewColor();
                choice_squre.setImageResource(R.mipmap.choice_squre1);
                isPurse=true;
                payWay="30";
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        params.clear();
                        values.clear();
                        params.add("key");
                        values.add(student.studentKey(PayActivity.this));
                        sendOrgetRequest("GET",2,student.studentMyPurse(),params,values);
                    }
                }).start();
                break;
            case R.id.student_pay_wc:
                //mToast.showText(PayActivity.this,"点击了微信支付");
                setImageViewColor();
                choice_wc.setImageResource(R.mipmap.pay_confirm1);
                isPurse=false;
                payWay="20";
                break;
            case R.id.student_pay_zfb:
                //mToast.showText(PayActivity.this,"点击了支付宝支付");
                setImageViewColor();
                chocie_zfb.setImageResource(R.mipmap.pay_confirm1);
                isPurse=false;
                payWay="10";
                break;
            case R.id.student_pay:
                //handler.sendEmptyMessage(4444);
                //判断是否选择支付类型
                if(payWay.equals("")){
                    AlertDialog.Builder builder=new AlertDialog.Builder(PayActivity.this);
                    builder.setMessage("请选择支付方式");
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.create().show();
                }else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            //支付
                            params.clear();
                            values.clear();
                            //参数
                            params.add("key");
                            //params.add("order_num");
                            params.add("type");
                            params.add("blanceType");
                            params.add("name");
                            params.add("price");    //设置支付价格
                            params.add("order_type");
                            params.add("out_id");
                            //参数对应的值
                            values.add(student.studentKey(PayActivity.this));
                            //这里优先使用余额里面的钱，余额不足选择微信或者支付宝支付，当余额足是其他支付方式不能用
                            values.add(isPurse?"":payWay);     //type
                            mLog.e("type","type->"+(isPurse?"":payWay));
                            values.add((isEnough?"30":(isPurse?payWay:"")));        //blanceTyep
                            mLog.e("blanceType","blanceType->"+(isEnough?"30":(isPurse?payWay:"")));
                            values.add("陪练-"+payitem.getCoachname());
                            values.add(payitem.getPrice());//payitem.getPrice()
                            values.add(payType);
                            values.add(payitem.getTask_id());
                            sendOrgetRequest("POST",Integer.valueOf(payWay),student.studentMPay(),params,values);
                        }
                    }).start();
                }
                break;
            default:
                break;
        }
    }

    private void saveTradeId(String tradeId){
        SharedPreferences sp=getSharedPreferences("Trade_no",MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putString("TradeId",tradeId);
        editor.apply();
    }

    private void setImageViewColor(){
        choice_wc.setImageResource(R.mipmap.pay_confirm0);
        chocie_zfb.setImageResource(R.mipmap.pay_confirm0);
    }

}
