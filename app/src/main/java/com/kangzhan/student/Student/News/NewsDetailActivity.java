package com.kangzhan.student.Student.News;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.kangzhan.student.R;
import com.kangzhan.student.ShowUI.mToast;
import com.kangzhan.student.ShowUI.showMessage;
import com.kangzhan.student.ShowUI.showProgress;
import com.kangzhan.student.Student.bean.NoticeDetail;
import com.kangzhan.student.com.BaseActivity;
import com.kangzhan.student.mInterface.StudentInterface.student;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class NewsDetailActivity extends BaseActivity {
    private TextView sender,theme,time,content;
    private String id;
    private NoticeDetail detail;
    private Gson gson;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1111:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.showProgress(NewsDetailActivity.this,"加载中...");
                        }
                    });
                    sendRequest();
                    break;
                case 2222:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.closeProgress();
                            sender.setText(detail.getSender_id());
                            theme.setText(detail.getSummary());
                            time.setText(detail.getSend_time());
                            content.setText("\u3000\u3000"+detail.getContent());
                        }
                    });
                    break;
                case 3333:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.closeProgress();
                            mToast.showText(getApplicationContext(),"加载失败，请稍后再试");
                        }
                    });
                    break;
                case 9999:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.closeProgress();
                            showMessage.showMsg(NewsDetailActivity.this,"网络连接异常，请检查网络连接");
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
        setContentView(R.layout.activity_news_detail);
        Intent getIntent=getIntent();
        id=getIntent.getStringExtra("id");
        Toolbar toolbar= (Toolbar) findViewById(R.id.student_news_noticedetail_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar=getSupportActionBar();
        if(actionbar!=null){
            actionbar.setDisplayHomeAsUpEnabled(true);
        }
        gson=new Gson();
        initView();
    }

    private void initView() {
        sender= (TextView) findViewById(R.id.notice_sender);
        theme= (TextView) findViewById(R.id.notice_theme);
        time= (TextView) findViewById(R.id.notice_time);
        content= (TextView) findViewById(R.id.notice_content);
        handler.sendEmptyMessage(1111);
    }



    private void sendRequest() {
        Request<JSONObject> request=NoHttp.createJsonObjectRequest(student.studentMsgDetail(), RequestMethod.GET);
        request.add("key",student.studentKey(NewsDetailActivity.this));
        request.add("id",id);
        getRequestQueue().add(1, request, new OnResponseListener<JSONObject>() {
            @Override
            public void onStart(int what) {

            }

            @Override
            public void onSucceed(int what, Response<JSONObject> response) {
                try {
                    JSONObject object=new JSONObject(response.get().toString());
                    String code=object.getString("code");
                    String data=object.getString("data");
                    if(code.equals("200")){
                        JSONArray array=new JSONArray(data);
                        JSONObject obj=array.getJSONObject(0);
                        detail=gson.fromJson(obj.toString(),NoticeDetail.class);
                        handler.sendEmptyMessage(2222);
                    }else {
                        handler.sendEmptyMessage(3333);
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

}
