package com.kangzhan.student.Teacher.News;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.model.Text;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.kangzhan.student.Debug.mLog;
import com.kangzhan.student.R;
import com.kangzhan.student.ShowUI.mToast;
import com.kangzhan.student.ShowUI.showMessage;
import com.kangzhan.student.ShowUI.showProgress;
import com.kangzhan.student.Teacher.bean.TeacherNewsAdviseDetail;
import com.kangzhan.student.Teacher.person_data.Teacher_addAdvise;
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

import java.util.ArrayList;
import java.util.List;

public class AdviseDetailActivity extends BaseActivity implements View.OnClickListener{
    //新增工单写在侧拉菜单里面
    private TextView problemTitle,problemContent,problemAnswer,timeP;
    private LinearLayout info;
    private Button addadvise;
    private String id,mmsg;
    private TeacherNewsAdviseDetail detail;
    private Gson gson;
    private ArrayList<String> params=new ArrayList<>();
    private ArrayList<String> valuse=new ArrayList<>();
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0000:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.showProgress(AdviseDetailActivity.this,"加载中...");
                        }
                    });
                    break;
                case 1111:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.closeProgress();
                            problemTitle.setText(detail.getSummary());
                            problemContent.setText(detail.getDescription());
                            if(detail.getReply_record().size()>0){
                                problemAnswer.setText(detail.getReply_record().get(0).getContent());
                                timeP.setText("回复时间："+detail.getReply_record().get(0).getReply_time()+"-"+detail.getReply_record().get(0).getReply_name());
                            }else {
                                info.setVisibility(View.GONE);
                                problemAnswer.setText("工单暂未处理！");
                            }
                        }
                    });
                    break;
                case 2222:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.closeProgress();
                            mToast.showText(AdviseDetailActivity.this,mmsg);
                        }
                    });
                    break;
                case 3333:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.showProgress(AdviseDetailActivity.this,"关闭中");
                        }
                    });
                    break;
                case 4444:
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
                            showMessage.showMsg(AdviseDetailActivity.this,"网络连接异常，请检查网络连接");
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
        setContentView(R.layout.activity_advise_detail2);
        Intent getIntent=getIntent();
        id=getIntent.getStringExtra("id");
        Toolbar toolbar= (Toolbar) findViewById(R.id.teacher_news_advise_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        gson=new Gson();
        initView();
    }

    private void initView() {
        problemTitle= (TextView) findViewById(R.id.teacher_news_adviseDetail_problemTitle);
        problemContent= (TextView) findViewById(R.id.teacher_news_advisedetail_problemContent);
        timeP= (TextView) findViewById(R.id.teacher_news_advisedetail_tp);
        info= (LinearLayout) findViewById(R.id.teacher_news_advisedetail_pp);
        problemAnswer= (TextView) findViewById(R.id.teacher_news_advisedetail_problemAnswer);
        addadvise= (Button) findViewById(R.id.teacher_news_advisedetail_btn);
        addadvise.setOnClickListener(this);
        new Thread(new Runnable() {
            @Override
            public void run() {
                handler.sendEmptyMessage(0000);
                params.clear();
                valuse.clear();
                params.add("key");
                params.add("ticketid");
                valuse.add(teacher.teacherKey(getApplicationContext()));
                valuse.add(id);
                sendRequest("GET",teacher.teacherAdviseDetail(),1,params,valuse);
            }
        }).start();
    }




    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.teacher_news_advisedetail_btn:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        handler.sendEmptyMessage(3333);
                        params.clear();
                        valuse.clear();
                        params.add("key");
                        params.add("ticketid");
                        params.add("close_type");
                        valuse.add(teacher.teacherKey(getApplicationContext()));
                        valuse.add(id);
                        valuse.add("20");
                        sendRequest("POST",teacher.teacherCloseAdvise(),2,params,valuse);
                    }
                }).start();
                break;
            default:
                break;
        }
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
                    mLog.e("reponse","-->"+response.get().toString());
                    mmsg=object.getString("msg");
                    if(what==1){
                        if(object.getString("code").equals("200")){
                            JSONArray array=new JSONArray(object.getString("data"));
                            JSONObject obj=new JSONObject(array.getJSONObject(0).toString());
                            detail=gson.fromJson(obj.toString(),TeacherNewsAdviseDetail.class);
                            handler.sendEmptyMessage(1111);
                        }else {
                            handler.sendEmptyMessage(2222);
                        }
                    }else if(what==2){
                        handler.sendEmptyMessage(4444);
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
