package com.kangzhan.student.School.Lesson;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.kangzhan.student.R;
import com.kangzhan.student.School.Bean.LessonManageDetail;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class LessonManageDetailAcitivity extends BaseActivity {
    private TextView name,num,hour,stage,startT,endT,status;
    private Gson gson;
    private LinearLayout container;
    private String mmsg,lessonId;
    private LessonManageDetail detail;
    private ArrayList<String> stageData=new ArrayList<>();
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1111:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.showProgress(LessonManageDetailAcitivity.this,"加载中...");
                        }
                    });
                    break;
                case 2222:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.closeProgress();
                            LoadData();
                        }
                    });
                    break;
                case 9999:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.closeProgress();
                            showMessage.showMsg(LessonManageDetailAcitivity.this,"网络连接异常，请检查网络连接");
                        }
                    });
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson_manage_detail_acitivity);
        Toolbar toolbar= (Toolbar) findViewById(R.id.school_lesson_detail_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        Intent getId=getIntent();
        lessonId=getId.getStringExtra("LessonId");
        gson=new Gson();
        initView();
        initData();
    }

    private void initView() {
        name= (TextView) findViewById(R.id.lesson_detail_name);
        num= (TextView) findViewById(R.id.lesson_detail_num);
        hour= (TextView) findViewById(R.id.lesson_detail_hour);
        stage= (TextView) findViewById(R.id.lesson_detail_stage);
        startT= (TextView) findViewById(R.id.lesson_detail_startT);
        endT= (TextView) findViewById(R.id.lesson_detail_endT);
        status= (TextView) findViewById(R.id.lesson_detail_stastu);
        container= (LinearLayout) findViewById(R.id.lesson_detail_container);
    }

    private void initData() {
        Request<JSONObject> request= NoHttp.createJsonObjectRequest(school.LessonDetail(), RequestMethod.GET);
        request.add("key",school.schoolKey(getApplicationContext()));
        request.add("id",lessonId);
        request.add("type",1);
        getRequestQueue().add(1, request, new OnResponseListener<JSONObject>() {
            @Override
            public void onStart(int what) {

            }

            @Override
            public void onSucceed(int what, Response<JSONObject> response) {
                try {
                    JSONObject objcet=new JSONObject(response.get().toString());
                    mmsg=objcet.getString("msg");
                    JSONObject data1=new JSONObject(objcet.getString("data"));
                    JSONObject data2=new JSONObject(data1.getString("0"));
                    JSONArray array=new JSONArray(data1.getString("peroidArr"));
                    detail=gson.fromJson(data2.toString(),LessonManageDetail.class);
                    if(array.length()>0){
                       stageData.clear();
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject data3=new JSONObject(array.getJSONObject(i).toString());
                            String str=data3.getString("period");
                            stageData.add(str);
                        }
                        handler.sendEmptyMessage(2222);
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

    private void LoadData() {
        name.setText(detail.getName());
        num.setText(detail.getMax_stu());
        hour.setText(detail.getDuration());
        stage.setText(detail.getStage());
        startT.setText(detail.getStart_time());
        endT.setText(detail.getEnd_time());
        status.setText(detail.getStatus());
        if(stageData.size()>0){
            for (int i = 0; i < stageData.size(); i++) {
                LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                LinearLayout linear=new LinearLayout(LessonManageDetailAcitivity.this);
                linear.setLayoutParams(lp);
                linear.setOrientation(LinearLayout.HORIZONTAL);
                if(i%2==0){
                    linear.setBackgroundColor(ContextCompat.getColor(LessonManageDetailAcitivity.this,R.color.background_color1));
                }
                RelativeLayout.LayoutParams relp=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
                RelativeLayout rela1=new RelativeLayout(LessonManageDetailAcitivity.this);
                RelativeLayout rela2=new RelativeLayout(LessonManageDetailAcitivity.this);
                rela1.setLayoutParams(relp);
                rela2.setLayoutParams(relp);
                TextView t1=new TextView(this);
                TextView t2=new TextView(this);
                t1.setPadding(50,40,40,40);
                t2.setPadding(200,40,40,40);
                t2.setGravity(Gravity.RELATIVE_HORIZONTAL_GRAVITY_MASK);
                ViewGroup.LayoutParams vlp=new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                t1.setLayoutParams(vlp);
                t2.setLayoutParams(vlp);
                t1.setText(i+1+"时段");
                t2.setText(stageData.get(i));
                rela1.addView(t1);
                rela2.addView(t2);
                linear.addView(rela1,new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT,1.0f));
                linear.addView(rela2,new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT,2.0f));
                container.addView(linear);
            }
        }
    }

}
