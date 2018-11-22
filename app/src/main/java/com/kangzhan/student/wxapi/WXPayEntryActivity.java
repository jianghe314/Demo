package com.kangzhan.student.wxapi;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.kangzhan.student.Debug.mLog;
import com.kangzhan.student.R;
import com.kangzhan.student.ShowUI.mToast;
import com.kangzhan.student.ShowUI.showMessage;
import com.kangzhan.student.ShowUI.showProgress;
import com.kangzhan.student.com.BaseActivity;
import com.kangzhan.student.mInterface.StudentInterface.student;
import com.kangzhan.student.mInterface.payBackInterface.PayBack;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.Response;

import org.json.JSONException;
import org.json.JSONObject;

public class WXPayEntryActivity extends BaseActivity implements IWXAPIEventHandler{
    //
    private TextView result;
    private Button complete;
    private int resultCode,mResultCode;
    private String mmsg,appId="wx69f0305db6ab47e4";
    private IWXAPI wXApi;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1011:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.showProgress(WXPayEntryActivity.this,"同步中...");
                        }
                    });
                    break;
                case 1000:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                showProgress.closeProgress();
                                if(resultCode==0&&mResultCode==0){
                                    result.setText(R.string.pay_success);
                                }else if(resultCode==-1){
                                    result.setText(R.string.pay_error);
                                }else if(resultCode==-2){
                                    result.setText(R.string.pay_faile);
                                }
                            }catch (NullPointerException e){
                                e.printStackTrace();
                            }
                        }
                    });
                    break;
                case 1122:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.closeProgress();
                            result.setText(R.string.pay_error);
                            mToast.showText(getApplicationContext(),mmsg);
                        }
                    });
                    break;
                case 2222:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.closeProgress();
                            result.setText(R.string.pay_error);
                            mToast.showText(getApplicationContext(),"支付失败");
                        }
                    });
                    break;
                case 9999:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.closeProgress();
                            showMessage.showMsg(WXPayEntryActivity.this,"网络连接异常，请检测网络");
                        }
                    });
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wxpay_entry);
        wXApi=WXAPIFactory.createWXAPI(this,appId);
        wXApi.handleIntent(getIntent(),this);
        intiView();
    }

    private void intiView() {
        result= (TextView) findViewById(R.id.wx_pay_result);
        complete= (Button) findViewById(R.id.pay_finsh);
        complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        wXApi.handleIntent(intent,this);
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }
    //得到支付回调结果
    @Override
    public void onResp(BaseResp baseResp) {
        //  0：表示成功，-1：发送错误   -2：支付取消

        resultCode=baseResp.errCode;
        mLog.e("支付回调结果","->"+baseResp.errCode+"->"+baseResp.errStr);
        if(resultCode==0){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    handler.sendEmptyMessage(1011);
                    getServerInfo(student.studentTradeId(WXPayEntryActivity.this));
                }
            }).start();
        }else {
            handler.sendEmptyMessage(2222);
        }
    }
    //获取服务器支付结果
    private void getServerInfo(String trade_no) {
        Request<JSONObject> request=NoHttp.createJsonObjectRequest(PayBack.stuPayback(), RequestMethod.GET);
        request.add("Out_trade_no",trade_no);
        getRequestQueue().add(1, request, new OnResponseListener<JSONObject>() {
            @Override
            public void onStart(int what) {

            }

            @Override
            public void onSucceed(int what, Response<JSONObject> response) {
                mLog.e("resultBack","->"+response.get().toString());
                try {
                    JSONObject object=new JSONObject(response.get().toString());
                    mmsg=object.getString("msg");
                    if(object.getString("code").equals("200")){
                        JSONObject obj=new JSONObject(object.getString("data"));
                        if(obj.getString("trade_state").equals("SUCCESS")){
                            mResultCode=0;
                            handler.sendEmptyMessage(1000);
                        }else {
                            handler.sendEmptyMessage(1000);
                        }
                    }else {
                        handler.sendEmptyMessage(1122);
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

    //重写返回事件将支付结果返回到发起支付界面，结束支付行为
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}
