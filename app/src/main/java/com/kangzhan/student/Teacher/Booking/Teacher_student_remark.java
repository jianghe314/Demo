package com.kangzhan.student.Teacher.Booking;

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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.kangzhan.student.Debug.mLog;
import com.kangzhan.student.R;
import com.kangzhan.student.ShowUI.mToast;
import com.kangzhan.student.ShowUI.showMessage;
import com.kangzhan.student.ShowUI.showProgress;
import com.kangzhan.student.Teacher.bean.TeacherBookingTrain1;
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

import de.hdodenhof.circleimageview.CircleImageView;

public class Teacher_student_remark extends BaseActivity implements View.OnClickListener{
    private CircleImageView header;
    private TextView lesson,name,time,price,carType;
    private ImageView sex;
    private EditText remarkContent;
    private TeacherBookingTrain1 item;
    private Button sure;
    private String Id,mmsg,Source;
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
                            showProgress.showProgress(Teacher_student_remark.this,"加载中...");
                        }
                    });
                    break;
                case 1111:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.closeProgress();
                            if(item.getStudent_sex().equals("女")){
                                sex.setImageResource(R.mipmap.girl);
                            }else {
                                sex.setImageResource(R.mipmap.boy);
                            }
                            lesson.setText(item.getStage_name());
                            name.setText(item.getStudent_name());
                            time.setText(item.getTime_name());
                            price.setText(item.getAmount()+"元");
                            carType.setText(item.getStudent_traintype()+" "+item.getCar_name());
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
                case 3333:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.showProgress(Teacher_student_remark.this,"评价中...");

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
                            showMessage.showMsg(Teacher_student_remark.this,"网络连接异常，请检测网络连接");
                        }
                    });
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_student_remark);
        Toolbar toolbar= (Toolbar) findViewById(R.id.teacher_student_remark_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        Intent info=getIntent();
        Id=info.getStringExtra("id");
        Source=info.getStringExtra("source");
        gson=new Gson();
        initView();
        initData();
    }

    private void initView() {
        header= (CircleImageView) findViewById(R.id.teacher_student_remark_header);
        lesson= (TextView) findViewById(R.id.teacher_student_remark_lesson);
        sex= (ImageView) findViewById(R.id.teacher_student_remark_sex);
        name= (TextView) findViewById(R.id.teacher_student_remark_name);
        time= (TextView) findViewById(R.id.teacher_student_remark_time);
        price= (TextView) findViewById(R.id.teacher_student_remark_price);
        carType= (TextView) findViewById(R.id.teacher_student_remark_carLabel);
        remarkContent= (EditText) findViewById(R.id.teacher_student_remarkContent);
        sure= (Button) findViewById(R.id.teacher_student_remark_btn);
        sure.setOnClickListener(this);
    }
    private void initData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                params.clear();
                valuse.clear();
                params.add("key");
                params.add("classrecord_id");
                valuse.add(teacher.teacherKey(getApplicationContext()));
                valuse.add(Id);
                sendRequest("GET",teacher.teacherShowStuInfo(),1,params,valuse);
            }
        }).start();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.teacher_student_remark_btn:
                if(remarkContent.getText().toString().trim().equals("")){
                    mToast.showText(getApplicationContext(),"点评内容不能为空");
                }else {
                    handler.sendEmptyMessage(3333);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            params.clear();
                            valuse.clear();
                            params.add("key");
                            params.add("classrecord_id");
                            params.add("coachnum");
                            valuse.add(teacher.teacherKey(getApplicationContext()));
                            valuse.add(Id);
                            valuse.add(remarkContent.getText().toString().trim());
                            sendRequest("POST",teacher.teacherSuggestion(),2,params,valuse);
                        }
                    }).start();
                }
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
                    mmsg=object.getString("msg");
                    if(what==1){
                        if(object.getString("code").equals("200")){
                            JSONArray array=new JSONArray(object.getString("data"));
                            JSONObject obj=array.getJSONObject(0);
                            item=gson.fromJson(obj.toString(),TeacherBookingTrain1.class);
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
