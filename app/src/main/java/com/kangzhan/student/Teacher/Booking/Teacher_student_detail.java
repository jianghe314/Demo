package com.kangzhan.student.Teacher.Booking;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.kangzhan.student.Debug.mLog;
import com.kangzhan.student.R;
import com.kangzhan.student.ShowUI.mToast;
import com.kangzhan.student.ShowUI.showMessage;
import com.kangzhan.student.ShowUI.showProgress;
import com.kangzhan.student.Teacher.bean.StudentDetail;
import com.kangzhan.student.Teacher.bean.StudentDetailRemarkInfo;
import com.kangzhan.student.Teacher.bean.StudentDetailScoreInfo;
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

public class Teacher_student_detail extends BaseActivity implements View.OnClickListener{
    //考试列表item为：item_teacher_booking_studentdetail_exam
    //评价表item为：item_teacher_booking_studentdetail_remark
    private CircleImageView header;
    private String mmsg,Id;
    private ImageView sex;
    private TextView name,phone,status,mclass,carType,school,home,qq,TestRecord,remarkRecord;
    private LinearLayout TestContainer,RemarkContainer;
    private RelativeLayout TestClick,RemarkClick;
    private Button linkTa;
    private boolean isOpen=false;
    private StudentDetail personInfo;
    private ArrayList<StudentDetailScoreInfo> scoreData=new ArrayList<>();
    private ArrayList<StudentDetailRemarkInfo> remarkData=new ArrayList<>();
    private Gson gson;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0000:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.showProgress(Teacher_student_detail.this,"加载中...");
                        }
                    });
                    break;
                case 1111:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.closeProgress();
                            showContent();
                        }
                    });
                    break;
                case 2222:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.closeProgress();
                            mToast.showText(Teacher_student_detail.this,mmsg);
                        }
                    });
                    break;
                case 9999:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.closeProgress();
                            showMessage.showMsg(Teacher_student_detail.this,"网络连接异常,请检查网络连接");
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
        setContentView(R.layout.activity_teacher_student_detail);
        Toolbar toolbar= (Toolbar) findViewById(R.id.teacher_studentdetail_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        Intent getData=getIntent();
        Id=getData.getStringExtra("Id");
        gson=new Gson();
        initView();
        initData();
    }

    private void initView() {
        header= (CircleImageView) findViewById(R.id.teacher_booking_studentdetail_header);
        name= (TextView) findViewById(R.id.teacher_booking_studentdetail_name);
        phone= (TextView) findViewById(R.id.teacher_booking_studentdetail_phone);
        sex= (ImageView) findViewById(R.id.teacher_booking_studentdetail_sex);
        status= (TextView) findViewById(R.id.teacher_student_detail_lessonContent);
        mclass= (TextView) findViewById(R.id.teacher_student_detail_class);
        carType= (TextView) findViewById(R.id.teacher_student_detail_cartype);
        school= (TextView) findViewById(R.id.teacher_student_detail_school);
        home= (TextView) findViewById(R.id.teacher_student_detail_home);
        qq= (TextView) findViewById(R.id.teacher_student_detail_qq);
        //
        TestRecord= (TextView) findViewById(R.id.teaher_booking_studentdetail_exam_Num);
        remarkRecord= (TextView) findViewById(R.id.teacher_booking_studentdetail_remark);
        TestClick= (RelativeLayout) findViewById(R.id.teaher_booking_studentdetail_linear1);
        RemarkClick= (RelativeLayout) findViewById(R.id.teaher_booking_studentdetail_linear2);
        linkTa= (Button) findViewById(R.id.teacher_booking_studentdetail_linkTa);
        linkTa.setOnClickListener(this);
        TestClick.setOnClickListener(this);
        RemarkClick.setOnClickListener(this);
        //
        TestContainer= (LinearLayout) findViewById(R.id.teacher_booking_studentdetail_exam_listView);
        RemarkContainer= (LinearLayout) findViewById(R.id.teacher_booking_studentdetail_remark_listView);
        TestContainer.setTag(false);
        RemarkContainer.setTag(false);
    }

    private void initData() {
        handler.sendEmptyMessage(0000);
        new Thread(new Runnable() {
            @Override
            public void run() {
                sendRequest();
            }
        }).start();
    }

    private void showContent() {
        if(personInfo.getSex().equals("男")){
            sex.setImageResource(R.mipmap.boy);
        }else if(personInfo.getSex().equals("女")){
            sex.setImageResource(R.mipmap.girl);
        }
        Glide.with(Teacher_student_detail.this).load(personInfo.getOss_photo()).diskCacheStrategy(DiskCacheStrategy.NONE).error(R.drawable.header).crossFade().into(header);
        name.setText(personInfo.getName());
        phone.setText(personInfo.getPhone());
        status.setText(personInfo.getStatus_str());
        mclass.setText(personInfo.getClass_name());
        carType.setText(personInfo.getTraintype());
        school.setText(personInfo.getInst_name());
        home.setText(personInfo.getAddress());
        qq.setText(personInfo.getQqnum());
        TestRecord.setText("考试记录"+"("+scoreData.size()+")");
        remarkRecord.setText("全部评价"+"("+remarkData.size()+")");

        for (int i = 0; i <scoreData.size() ; i++) {
            View scoreView=LayoutInflater.from(this).inflate(R.layout.item_teacher_studentdetal_test,null);
            ImageView circle= (ImageView) scoreView.findViewById(R.id.item_teacher_studentDetal_exam_iv);
            TextView title= (TextView) scoreView.findViewById(R.id.item_teacher_studentDetal_exam_lessson);
            TextView score= (TextView) scoreView.findViewById(R.id.item_teacher_studentDetal_exam_grade);
            TextView time= (TextView) scoreView.findViewById(R.id.item_teacher_studentDetal_exam_time);
            title.setText(scoreData.get(i).getStage());
            try{
                if(Integer.valueOf(scoreData.get(i).getScore())>89){
                    score.setText(scoreData.get(i).getScore()+"(合格)");
                }else {
                    score.setText(scoreData.get(i).getScore()+"(不合格)");
                }
                time.setText(scoreData.get(i).getExam_date());
            }catch (Exception e){
                e.printStackTrace();
            }
            if(i%2==0){
                circle.setBackgroundResource(R.drawable.circle1);
            }else if(i%2==1){
                circle.setBackgroundResource(R.drawable.circle2);
            }else {
                circle.setBackgroundResource(R.drawable.circle3);
            }
            TestContainer.addView(scoreView);
        }
        mLog.e("长度","-->"+remarkData.size());
        for (int i = 0; i <remarkData.size(); i++) {
            View remarkView=LayoutInflater.from(this).inflate(R.layout.item_teacher_studentdetail_remark,null);
            ImageView header= (CircleImageView)findViewById(R.id.item_teacher_studentDetail_remark_header);
            TextView name= (TextView) remarkView.findViewById(R.id.item_teacher_studentDetail_remark_name);
            TextView time= (TextView) remarkView.findViewById(R.id.item_teacher_studentDetail_remark_time);
            TextView type= (TextView) remarkView.findViewById(R.id.item_teacher_studentDetail_remark_type);
            TextView content= (TextView) remarkView.findViewById(R.id.item_teacher_studentDetail_remark_content);
            name.setText(remarkData.get(i).getCoachName());
            time.setText(remarkData.get(i).getCreate_time());
            type.setText("课程:"+remarkData.get(i).getSubject_content());
            content.setText(remarkData.get(i).getContent());
            RemarkContainer.addView(remarkView);
        }
        TestContainer.requestLayout();
        RemarkContainer.requestLayout();
    }

    private void sendRequest() {
        //备注：学员ID暂时给25 有数据
        Request<JSONObject> request=NoHttp.createJsonObjectRequest(teacher.teacherStudentDetail(), RequestMethod.GET);
        request.add("key",teacher.teacherKey(getApplicationContext()));
        request.add("stu_id",Id);
        getRequestQueue().add(1, request, new OnResponseListener<JSONObject>() {
            @Override
            public void onStart(int what) {

            }

            @Override
            public void onSucceed(int what, Response<JSONObject> response) {
                try {
                    JSONObject object=new JSONObject(response.get().toString());
                    mLog.e("reponse","-->>"+response.get().toString());
                    mmsg=object.getString("msg");
                    if(object.getString("code").equals("200")){
                        String data=object.getString("data");
                        JSONObject obj=new JSONObject(data);
                        personInfo=gson.fromJson(obj.getString("studentInfo"),StudentDetail.class);
                        String score=obj.getString("examscoreInfo");
                        String reamrkInfo=obj.getString("suggestionInfo");
                        JSONArray array=new JSONArray(score);
                        if(array.length()>0){
                            scoreData.clear();
                            for (int i = 0; i < array.length(); i++) {
                                StudentDetailScoreInfo scoredata=gson.fromJson(array.getJSONObject(i).toString(),StudentDetailScoreInfo.class);
                                scoreData.add(scoredata);
                            }
                        }else {
                            scoreData.clear();
                        }
                        JSONArray remarkArray=new JSONArray(reamrkInfo);
                        if(remarkArray.length()>0){
                            remarkData.clear();
                            for (int i = 0; i <remarkArray.length(); i++) {
                                StudentDetailRemarkInfo remarkdata=gson.fromJson(remarkArray.getJSONObject(i).toString(),StudentDetailRemarkInfo.class);
                                remarkData.add(remarkdata);
                            }
                        }else {
                            remarkData.clear();
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


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.teacher_booking_studentdetail_linkTa:
                //拨打电话
                AlertDialog.Builder builder=new AlertDialog.Builder(this);
                builder.setTitle("请求权限");
                builder.setMessage("是否容许拨打电话权限");
                builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //检测权限
                        mrequestPermission();
                    }
                });
                builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mToast.showText(getApplicationContext(),"获取权限失败！");
                        dialog.dismiss();
                    }
                });
                builder.setCancelable(false);
                builder.create().show();
                break;
            case R.id.teaher_booking_studentdetail_linear1:
                isShow(TestContainer);
                break;
            case R.id.teaher_booking_studentdetail_linear2:
                isShow(RemarkContainer);
                break;
        }
    }

    private void mrequestPermission() {
        //>=23
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            int checkCallPhone= ContextCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE);
            if(checkCallPhone!= PackageManager.PERMISSION_GRANTED){
                //没有授权
                ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.CALL_PHONE},1);
            }else {
                //已经授权
                callPhone();
            }
        }else {
            callPhone();
        }
    }

    //申请权限回调

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    callPhone();
                }else {
                    mToast.showText(getApplicationContext(),"没有权限拨打电话！");
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                break;
        }
    }

    private void callPhone() {
        Intent intent=new Intent();
        intent.setAction(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:"+personInfo.getPhone()));
        startActivity(intent);
    }


    private void isShow(final LinearLayout content) {
        content.measure(0,0);      //测量高度
        isOpen= (boolean) content.getTag();     //默认false
        int measureHeight=content.getMeasuredHeight();
        if(isOpen){
            //打开
            int startH=0;
            ValueAnimator animator=ValueAnimator.ofInt(startH,measureHeight);
            animator.setDuration(800);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    ViewGroup.LayoutParams lp=content.getLayoutParams();
                    lp.height= (int) animation.getAnimatedValue();
                    content.setLayoutParams(lp);
                }
            });
            animator.start();
            content.setTag(false);
        }else {
            //关闭
            int endH=0;
            ValueAnimator animator=ValueAnimator.ofInt(measureHeight,endH);
            animator.setDuration(800);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int values= (int) animation.getAnimatedValue();
                    ViewGroup.LayoutParams lp=content.getLayoutParams();
                    lp.height= values;
                    content.setLayoutParams(lp);
                }
            });
            animator.start();
            content.setTag(true);
        }
    }

}
