package com.kangzhan.student.Teacher.News;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.kangzhan.student.Debug.mLog;
import com.kangzhan.student.R;
import com.kangzhan.student.ShowUI.mToast;
import com.kangzhan.student.ShowUI.showMessage;
import com.kangzhan.student.ShowUI.showProgress;
import com.kangzhan.student.Student.bean.NoticeDetail;
import com.kangzhan.student.com.BaseActivity;
import com.kangzhan.student.mInterface.TeacherInterface.teacher;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class NoticeDetailActivity extends BaseActivity {
    private String id;
    private TextView sendMan,title,time,content;
    private NoticeDetail detail;
    private Gson gson;
    private String mmsg;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0000:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.showProgress(NoticeDetailActivity.this,"加载中...");
                        }
                    });
                    break;
                case 1111:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.closeProgress();
                            sendMan.setText(detail.getSender_id());
                            title.setText(detail.getSummary());
                            time.setText(detail.getSend_time());
                            content.setText("\u3000\u3000"+detail.getContent());
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
                            showMessage.showMsg(getApplicationContext(),"网络连接异常，请检查网络连接");
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
        setContentView(R.layout.activity_notice_detail);
        Intent getIntent=getIntent();
        id=getIntent.getStringExtra("id");
        Toolbar toolbar= (Toolbar) findViewById(R.id.teacher_mydata_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        gson=new Gson();
        initView();

    }

    private void initView() {
        sendMan= (TextView) findViewById(R.id.notice_sender);
        title= (TextView) findViewById(R.id.notice_theme);
        time= (TextView) findViewById(R.id.notice_time);
        content= (TextView) findViewById(R.id.notice_content);
        handler.sendEmptyMessage(0000);
        new Thread(new Runnable() {
            @Override
            public void run() {
                sendRequest();
            }
        }).start();
    }

    private void sendRequest() {
        Request<JSONObject> request=NoHttp.createJsonObjectRequest(teacher.teacherNewsCheckNoticeDetail(), RequestMethod.GET);
        request.add("id",id);
        getRequestQueue().add(1, request, new OnResponseListener<JSONObject>() {
            @Override
            public void onStart(int what) {

            }

            @Override
            public void onSucceed(int what, Response<JSONObject> response) {
                mLog.e("response","->"+response.get().toString());
                try {
                    JSONObject object=new JSONObject(response.get().toString().trim());
                    mmsg=object.getString("msg");
                    if(object.getString("code").equals("200")){
                        String data=object.getString("data");
                        JSONArray array=new JSONArray(data);
                        if(array.length()>0){
                            JSONObject obj=array.getJSONObject(0);
                            detail=gson.fromJson(obj.toString(),NoticeDetail.class);
                        }
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

}
