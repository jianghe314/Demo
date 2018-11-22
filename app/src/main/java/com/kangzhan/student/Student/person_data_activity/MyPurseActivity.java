package com.kangzhan.student.Student.person_data_activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.kangzhan.student.R;
import com.kangzhan.student.ShowUI.mToast;
import com.kangzhan.student.ShowUI.showMessage;
import com.kangzhan.student.ShowUI.showProgress;
import com.kangzhan.student.com.BaseActivity;
import com.kangzhan.student.mInterface.StudentInterface.student;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.Response;

import org.json.JSONException;
import org.json.JSONObject;

public class MyPurseActivity extends BaseActivity implements View.OnClickListener{
    private TextView purse_Num;
    private Button excharge;
    private String purse;
    private String mmsg;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0000:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.showProgress(MyPurseActivity.this,"加载中...");
                        }
                    });
                    sendRequest();
                    break;
                case 1111:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.closeProgress();
                            purse_Num.setText(purse);
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
                            showMessage.showMsg(getApplicationContext(),"网络连接异常，请检测网络连接");
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
        setContentView(R.layout.activity_my_purse);
        Toolbar toolbar= (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar=getSupportActionBar();
        //设置返回箭头
        if(actionbar!=null){
            actionbar.setDisplayHomeAsUpEnabled(true);
        }
        initView();
    }

    private void initView() {
        purse_Num= (TextView) findViewById(R.id.purse_number);
        excharge= (Button) findViewById(R.id.btn_excharge);
        excharge.setOnClickListener(this);
        Message msg=new Message();
        msg.what=0000;
        handler.sendMessage(msg);
    }

    private void sendRequest() {
        Request<JSONObject> request=NoHttp.createJsonObjectRequest(student.studentMyPurse(), RequestMethod.GET);
        request.add("key",student.studentKey(getApplicationContext()));
        getRequestQueue().add(1, request, new OnResponseListener<JSONObject>() {
            @Override
            public void onStart(int what) {

            }

            @Override
            public void onSucceed(int what, Response<JSONObject> response) {
                try {
                    JSONObject object=new JSONObject(response.get().toString());
                    mmsg=object.getString("msg");
                    if(object.getString("code").equals("200")){
                        purse=object.getString("data");
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
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_excharge:
                Intent excharg=new Intent(this,PurseExchargeActicity.class);
                excharg.putExtra("who","student");
                startActivity(excharg);
                break;
            default:
                break;
        }
    }
    //创建菜单
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mypurse_menu,menu);
        return true;
    }
    //菜单的点击事件

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.purse_detail:
                Intent detail=new Intent(MyPurseActivity.this,MyPurseDetail.class);
                startActivity(detail);
                break;
            //返回箭头的Id
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return true;
    }

}
