package com.kangzhan.student.Teacher.Train;

import android.content.Context;
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
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.kangzhan.student.Debug.mLog;
import com.kangzhan.student.R;
import com.kangzhan.student.ShowUI.mToast;
import com.kangzhan.student.ShowUI.showMessage;
import com.kangzhan.student.ShowUI.showProgress;
import com.kangzhan.student.Student.bean.TrainRecord;
import com.kangzhan.student.Teacher.Booking.Teacher_student_remark;
import com.kangzhan.student.Teacher.bean.TeacherTrainRecord;
import com.kangzhan.student.Teacher.bean.TeacherTrainRemarkDet;
import com.kangzhan.student.com.BaseActivity;
import com.kangzhan.student.mInterface.TeacherInterface.teacher;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.Response;

import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;

public class Teacher_train_RemarkActivity extends BaseActivity implements View.OnClickListener{
    private CircleImageView header;
    private TextView lesson,name,time,price,carType;
    private ImageView sex;
    private Button send;
    private EditText content;
    private Gson gson;
    private String mmsg,Id;
    private TeacherTrainRemarkDet item;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0000:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.showProgress(Teacher_train_RemarkActivity.this,"加载中...");
                        }
                    });
                    break;
                case 1111:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.closeProgress();
                            lesson.setText(item.getDetail());
                            if(item.getSex().equals("1")){
                                sex.setImageResource(R.mipmap.boy);
                            }else {
                                sex.setImageResource(R.mipmap.girl);
                            }
                            name.setText(item.getStuname());
                            time.setText(item.getStart_time()+" "+item.getEnd_time());
                            price.setText(item.getPrice()+"元");
                            carType.setText(item.getDetail()+" "+item.getCar_name());
                            Glide.with(Teacher_train_RemarkActivity.this).load(item.getOss_photo()).diskCacheStrategy(DiskCacheStrategy.NONE).error(R.drawable.header).crossFade().into(header);
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
                            showProgress.showProgress(Teacher_train_RemarkActivity.this,"评价中...");
                        }
                    });
                    break;
                case 9999:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.closeProgress();
                            showMessage.showMsg(Teacher_train_RemarkActivity.this,"网络连接异常，请检查网络连接");
                        }
                    });
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_train__remark);
        Toolbar toolbar= (Toolbar) findViewById(R.id.teacher_train_remark_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        Intent getIntent=getIntent();
        Id=getIntent.getStringExtra("Id");
        gson=new Gson();
        initView();
        initData();
    }

    private void initView() {
        header= (CircleImageView) findViewById(R.id.teacher_train_remark_header);
        lesson= (TextView) findViewById(R.id.teacher_train_remark_lesson);
        sex= (ImageView) findViewById(R.id.teacher_train_remark_sex);
        name= (TextView) findViewById(R.id.teacher_train_remark_name);
        time= (TextView) findViewById(R.id.teacher_train_remark_time);
        price= (TextView) findViewById(R.id.teacher_student_remark_price);
        carType= (TextView) findViewById(R.id.teacher_train_remark_carLabel);
        send= (Button) findViewById(R.id.teacher_train_remark_btn);
        send.setOnClickListener(this);
        content= (EditText) findViewById(R.id.teacher_train_remarkContent);
    }

    private void initData() {
        handler.sendEmptyMessage(0000);
        new Thread(new Runnable() {
            @Override
            public void run() {
                getStudentInfo();
            }
        }).start();

    }

    private void getStudentInfo() {
        Request<JSONObject> request=NoHttp.createJsonObjectRequest(teacher.teacherRecordRemark(),RequestMethod.GET);
        request.add("key",teacher.teacherKey(getApplicationContext()));
        request.add("id",Id);
        getRequestQueue().add(1, request, new OnResponseListener<JSONObject>() {
            @Override
            public void onStart(int what) {

            }

            @Override
            public void onSucceed(int what, Response<JSONObject> response) {
                try {
                    JSONObject obejct=new JSONObject(response.get().toString());
                    mLog.e("reponse","--->>"+response.get().toString());
                    if(obejct.getString("code").equals("200")){
                        item=gson.fromJson(obejct.getString("data"),TeacherTrainRemarkDet.class);
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
            case R.id.teacher_train_remark_btn:
                if(content.getText().toString().trim().equals("")){
                    mToast.showText(Teacher_train_RemarkActivity.this,"点评内容不能为空");
                }else {
                    handler.sendEmptyMessage(3333);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            sendRemarkInfo(content.getText().toString().trim());
                        }
                    }).start();
                }
                break;
        }
    }



    private void sendRemarkInfo(String s) {
        Request<JSONObject> request= NoHttp.createJsonObjectRequest(teacher.teacherUpdateRemark(), RequestMethod.POST);
        request.add("key",teacher.teacherKey(getApplicationContext()));
        request.add("source",2);
        request.add("stu_id",item.getStu_id());
        request.add("refer_id",item.getId());
        request.add("subject_content",item.getCoach_id()+item.getStart_time()+item.getEnd_time());
        request.add("content",s);
        getRequestQueue().add(2, request, new OnResponseListener<JSONObject>() {
            @Override
            public void onStart(int what) {

            }

            @Override
            public void onSucceed(int what, Response<JSONObject> response) {
                mLog.i("response","->"+response.get().toString());
                try {
                    JSONObject object=new JSONObject(response.get().toString());
                    mmsg=object.getString("msg");
                    handler.sendEmptyMessage(2222);
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
