package com.kangzhan.student.School.Edu;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
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
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.kangzhan.student.Debug.mLog;
import com.kangzhan.student.R;
import com.kangzhan.student.School.Bean.StudentDetail;
import com.kangzhan.student.ShowUI.mToast;
import com.kangzhan.student.ShowUI.showMessage;
import com.kangzhan.student.ShowUI.showProgress;
import com.kangzhan.student.com.BaseActivity;
import com.kangzhan.student.mInterface.CompayInterface.CompayInterface;
import com.kangzhan.student.mInterface.SchoolInterface.school;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.Response;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class School_StudentDetail extends BaseActivity implements View.OnClickListener {
    //evalu:点评  remark:评价
    private TextView name,sex,phone,address,mclass,ID,zsr,carType,teacher,carLable,clerk;
    private ImageView test,exam,booking,training,learnH,evalu,advise,remark,callPhone,order;
    private LinearLayout test_content,exam_content,booking_content,training_content,learnH_content,evalu_content,advise_content,remark_content,order_content;
    private RelativeLayout r1,r2,r3,r4,r5,r6,r7,r8,r9;
    private String mmsg;
    private Gson gson;
    private String Id,who;
    private boolean isOpen=false;
    private StudentDetail detail;
    private ArrayList<String> params=new ArrayList<>();
    private ArrayList<String> values=new ArrayList<>();
    private CircleImageView header;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1111:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.showProgress(School_StudentDetail.this,"加载中...");
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
                            showProgress.closeProgress();
                            showContent();
                        }
                    });
                    break;
                case 9999:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.closeProgress();
                            showMessage.showMsg(School_StudentDetail.this,"网络连接异常，请检查网络连接");
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
        setContentView(R.layout.activity_school__student_detail);
        Toolbar toolbar= (Toolbar) findViewById(R.id.school_edu_stuDetail_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        gson=new Gson();
        Intent getId=getIntent();
        Id=getId.getStringExtra("Id");
        who=getId.getStringExtra("who");
        initView();
        iniData();
    }


    private void initView() {
        header= (CircleImageView) findViewById(R.id.school_stu_detail_header);
        name= (TextView) findViewById(R.id.school_stu_detail_name);
        sex= (TextView) findViewById(R.id.school_stu_detail_sex);
        phone= (TextView) findViewById(R.id.school_stu_detail_phone);
        callPhone= (ImageView) findViewById(R.id.school_stu_detail_Callphone);
        address= (TextView) findViewById(R.id.school_stu_detail_address);
        mclass= (TextView) findViewById(R.id.school_stu_detail_class);
        ID= (TextView) findViewById(R.id.school_stu_detail_ID);
        zsr= (TextView) findViewById(R.id.school_stu_detail_zsr);
        carType= (TextView) findViewById(R.id.school_stu_detail_carType);
        teacher= (TextView) findViewById(R.id.school_stu_detail_teacher);
        carLable= (TextView) findViewById(R.id.school_stu_detail_crLabel);
        clerk= (TextView) findViewById(R.id.school_stu_detail_clerk);
        //
        callPhone.setOnClickListener(this);
        r1= (RelativeLayout) findViewById(R.id.school_stu_detail_r1);
        r2= (RelativeLayout) findViewById(R.id.school_stu_detail_r2);
        r3= (RelativeLayout) findViewById(R.id.school_stu_detail_r3);
        r4= (RelativeLayout) findViewById(R.id.school_stu_detail_r4);
        r5= (RelativeLayout) findViewById(R.id.school_stu_detail_r5);
        r6= (RelativeLayout) findViewById(R.id.school_stu_detail_r6);
        r7= (RelativeLayout) findViewById(R.id.school_stu_detail_r7);
        r8= (RelativeLayout) findViewById(R.id.school_stu_detail_r8);
        r9= (RelativeLayout) findViewById(R.id.school_stu_detail_r9);
        r1.setOnClickListener(this);
        r2.setOnClickListener(this);
        r3.setOnClickListener(this);
        r4.setOnClickListener(this);
        r5.setOnClickListener(this);
        r6.setOnClickListener(this);
        r7.setOnClickListener(this);
        r8.setOnClickListener(this);
        r9.setOnClickListener(this);
        //
        test= (ImageView) findViewById(R.id.school_stu_detail_test);
        exam= (ImageView) findViewById(R.id.school_stu_detail_exam);
        booking= (ImageView) findViewById(R.id.school_stu_detail_order);
        training= (ImageView) findViewById(R.id.school_stu_detail_train);
        learnH= (ImageView) findViewById(R.id.school_stu_detail_learnH);
        evalu= (ImageView) findViewById(R.id.school_stu_detail_eval);
        advise= (ImageView) findViewById(R.id.school_stu_detail_advise);
        remark= (ImageView) findViewById(R.id.school_stu_detail_remark);
        order= (ImageView) findViewById(R.id.school_stu_detail_orderInfo);
        //
        //

        //
        test_content= (LinearLayout) findViewById(R.id.school_stu_detail_test_content);
        test_content.setTag(false);
        isShow(test_content,test);
        exam_content= (LinearLayout) findViewById(R.id.school_stu_detail_exam_content);
        exam_content.setTag(false);
        isShow(exam_content,exam);
        booking_content= (LinearLayout) findViewById(R.id.school_stu_detail_order_content);
        booking_content.setTag(false);
        isShow(booking_content,booking);
        training_content= (LinearLayout) findViewById(R.id.school_stu_detail_train_content);
        training_content.setTag(false);
        isShow(training_content,training);
        learnH_content= (LinearLayout) findViewById(R.id.school_stu_detail_learnH_content);
        learnH_content.setTag(false);
        isShow(learnH_content,learnH);
        evalu_content= (LinearLayout) findViewById(R.id.school_stu_detail_eval_content);
        evalu_content.setTag(false);
        isShow(evalu_content,evalu);
        advise_content= (LinearLayout) findViewById(R.id.school_stu_detail_advise_content);
        advise_content.setTag(false);
        isShow(advise_content,advise);
        remark_content= (LinearLayout) findViewById(R.id.school_stu_detail_remark_content);
        remark_content.setTag(false);
        isShow(remark_content,remark);
        order_content= (LinearLayout) findViewById(R.id.school_stu_detail_orderInfo_content);
        order_content.setTag(false);
        isShow(order_content,order);
        //
    }

    private void iniData() {
        handler.sendEmptyMessage(1111);
        if(who.equals("school")){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    params.clear();
                    values.clear();
                    params.add("key");
                    params.add("stu_id");
                    values.add(school.schoolKey(getApplicationContext()));
                    values.add(Id);

                    mLog.e("stu_id","-->"+Id);
                    sendReuqest("GET",1,school.EduStudentDetail(),params,values);
                }
            }).start();
        }else if(who.equals("compay")){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    params.clear();
                    values.clear();
                    params.add("key");
                    params.add("stu_id");
                    values.add(CompayInterface.CompayKey(School_StudentDetail.this));
                    values.add(Id);
                    sendReuqest("GET",1,school.EduStudentDetail(),params,values);
                }
            }).start();
        }

    }


    private void showContent() {
        Glide.with(this).load(detail.getStudentInfo().getOss_photo()).diskCacheStrategy(DiskCacheStrategy.NONE).error(R.drawable.header).crossFade().into(header);
        name.setText(detail.getStudentInfo().getStuname());
        sex.setText(detail.getStudentInfo().getSex().equals("1")?"男":"女");
        phone.setText(detail.getStudentInfo().getPhone());
        address.setText(detail.getStudentInfo().getHome_address());
        mclass.setText(detail.getStudentInfo().getClass_name());
        ID.setText(detail.getStudentInfo().getIdcard());
        zsr.setText(detail.getStudentInfo().getEnroller());
        carType.setText(detail.getStudentInfo().getTraintype());
        teacher.setText(detail.getStudentInfo().getName()+"-"+detail.getStudentInfo().getCoachmobile());
        carLable.setText(detail.getStudentInfo().getCar_id());
        clerk.setText(detail.getStudentInfo().getClerkname()+"-"+detail.getStudentInfo().getClerkphone());
        //模拟成绩

        for (int i = 0; i < detail.getSimulationscoreInfo().size(); i++) {
            LinearLayout linear1=new LinearLayout(this);
            linear1.setOrientation(LinearLayout.HORIZONTAL);
            TextView tv1=new TextView(this);
            TextView tv2=new TextView(this);
            TextView tv3=new TextView(this);
            TextView tv4=new TextView(this);
            tv1.setPadding(30,15,40,15);
            tv2.setPadding(30,15,40,15);
            tv3.setPadding(30,15,40,15);
            tv4.setPadding(30,15,40,15);
            tv1.setText(detail.getSimulationscoreInfo().get(i).getStart_time());
            tv2.setText(detail.getSimulationscoreInfo().get(i).getStage());
            tv3.setText(detail.getSimulationscoreInfo().get(i).getScore());
            tv4.setText(detail.getSimulationscoreInfo().get(i).getQualified());
            linear1.addView(tv1);
            linear1.addView(tv2);
            linear1.addView(tv3);
            linear1.addView(tv4);
            test_content.addView(linear1);
        }
        //考试成绩
        for (int i = 0; i < detail.getExamscoreInfo().size(); i++) {
            LinearLayout linear1=new LinearLayout(this);
            linear1.setOrientation(LinearLayout.HORIZONTAL);
            TextView tv1=new TextView(this);
            TextView tv2=new TextView(this);
            TextView tv3=new TextView(this);
            TextView tv4=new TextView(this);
            tv1.setPadding(30,15,30,15);
            tv2.setPadding(30,15,30,15);
            tv3.setPadding(30,15,30,15);
            tv4.setPadding(30,15,30,15);
            tv1.setText(detail.getExamscoreInfo().get(i).getExam_date());
            tv2.setText(detail.getExamscoreInfo().get(i).getStage());
            tv3.setText(detail.getExamscoreInfo().get(i).getScore());
            tv4.setText(detail.getExamscoreInfo().get(i).getQualified());
            linear1.addView(tv1);
            linear1.addView(tv2);
            linear1.addView(tv3);
            linear1.addView(tv4);
           exam_content.addView(linear1);
        }
        //预约订单
        for (int i = 0; i <detail.getAppointInfo().size(); i++) {
            LinearLayout linear1=new LinearLayout(this);
            linear1.setOrientation(LinearLayout.HORIZONTAL);
            TextView tv1=new TextView(this);
            TextView tv2=new TextView(this);
            TextView tv3=new TextView(this);
            TextView tv4=new TextView(this);
            tv1.setPadding(30,15,15,15);
            tv2.setPadding(30,15,15,15);
            tv3.setPadding(30,15,15,15);
            tv4.setPadding(30,15,15,15);
            tv1.setText(detail.getAppointInfo().get(i).getSdate()+" "+detail.getAppointInfo().get(i).getStarttime()+"-"+detail.getAppointInfo().get(i).getEndtime());
            tv2.setText(detail.getAppointInfo().get(i).getName());
            tv3.setText(detail.getAppointInfo().get(i).getCar_licnum());
            tv4.setText(detail.getAppointInfo().get(i).getPrice()+"元");
            tv4.setTextColor(ContextCompat.getColor(this,R.color.colorPrimary));
            linear1.addView(tv1);
            linear1.addView(tv2);
            linear1.addView(tv3);
            linear1.addView(tv4);
            booking_content.addView(linear1);
        }
        //陪练订单
        for (int i = 0; i <detail.getSparringInfo().size(); i++) {
            LinearLayout linear1=new LinearLayout(this);
            linear1.setOrientation(LinearLayout.HORIZONTAL);
            TextView tv1=new TextView(this);
            TextView tv2=new TextView(this);
            TextView tv3=new TextView(this);
            TextView tv4=new TextView(this);
            tv1.setPadding(30,15,30,15);
            tv2.setPadding(30,15,30,15);
            tv3.setPadding(30,15,30,15);
            tv4.setPadding(30,15,30,15);
            tv1.setText(detail.getSparringInfo().get(i).getCreate_time());
            tv2.setText(detail.getSparringInfo().get(i).getCoachName());
            tv3.setText(detail.getSparringInfo().get(i).getDetail());
            tv4.setText(detail.getSparringInfo().get(i).getAmount()+"元");
            tv4.setTextColor(ContextCompat.getColor(this,R.color.colorPrimary));
            linear1.addView(tv1);
            linear1.addView(tv2);
            linear1.addView(tv3);
            linear1.addView(tv4);
            training_content.addView(linear1);
        }
        //学时记录
        for (int i = 0; i <detail.getClassrecordInfo().size(); i++) {
            LinearLayout linear1=new LinearLayout(this);
            linear1.setOrientation(LinearLayout.HORIZONTAL);
            TextView tv1=new TextView(this);
            TextView tv2=new TextView(this);
            TextView tv3=new TextView(this);
            TextView tv4=new TextView(this);
            tv1.setPadding(30,15,30,15);
            tv2.setPadding(30,15,30,15);
            tv3.setPadding(30,15,30,15);
            tv4.setPadding(30,15,30,15);
            tv1.setText(detail.getClassrecordInfo().get(i).getStarttime()+"-"+detail.getClassrecordInfo().get(i).getEndtime());
            tv2.setText(detail.getClassrecordInfo().get(i).getCoach_id());
            tv3.setText(detail.getClassrecordInfo().get(i).getStage());
            tv4.setText(detail.getClassrecordInfo().get(i).getDuration()+"课时");
            tv4.setTextColor(ContextCompat.getColor(this,R.color.colorPrimary));
            linear1.addView(tv1);
            linear1.addView(tv2);
            linear1.addView(tv3);
            linear1.addView(tv4);
            learnH_content.addView(linear1);
        }
        //点评记录
        for (int i = 0; i <detail.getSuggestionInfo().size(); i++) {
            LinearLayout linear=new LinearLayout(this);
            LinearLayout linear1=new LinearLayout(this);
            LinearLayout linear2=new LinearLayout(this);
            linear.setOrientation(LinearLayout.VERTICAL);
            linear1.setOrientation(LinearLayout.HORIZONTAL);
            linear2.setOrientation(LinearLayout.HORIZONTAL);
            TextView time=new TextView(this);
            time.setPadding(30,10,50,10);
            time.setText(detail.getSuggestionInfo().get(i).getSubject_content());
            linear1.addView(time);
            ImageView iv=new ImageView(this);
            iv.setPadding(30,10,20,10);
            iv.setImageResource(R.mipmap.evalu_mark);
            TextView content=new TextView(this);
            content.setPadding(5,10,0,10);
            content.setText(detail.getSuggestionInfo().get(i).getContent());
            linear2.addView(iv);
            linear2.addView(content);
            linear.addView(linear1);
            linear.addView(linear2);
            evalu_content.addView(linear);
        }
        //投诉记录
        for (int i = 0; i <detail.getComplaintInfo().size(); i++) {
            LinearLayout linear=new LinearLayout(this);
            linear.setOrientation(LinearLayout.VERTICAL);
            TextView time=new TextView(this);
            time.setPadding(30,10,40,10);
            TextView content=new TextView(this);
            content.setPadding(30,10,40,10);
            time.setText(detail.getComplaintInfo().get(i).getCreate_time());
            content.setText(detail.getComplaintInfo().get(i).getContent());
            linear.addView(time);
            linear.addView(content);
            advise_content.addView(linear);
        }
        //评价记录
        for (int i = 0; i < detail.getEvaluationInfo().size(); i++) {
            LinearLayout linear=new LinearLayout(this);
            linear.setOrientation(LinearLayout.VERTICAL);
            LinearLayout linear1=new LinearLayout(this);
            linear1.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout linear2=new LinearLayout(this);
            linear2.setOrientation(LinearLayout.HORIZONTAL);
            TextView content=new TextView(this);
            content.setPadding(30,10,50,10);
            content.setText(detail.getEvaluationInfo().get(i).getEvalsubjcontent());
            linear1.addView(content);
            ImageView iv=new ImageView(this);
            iv.setPadding(30,10,10,10);
            iv.setImageResource(R.mipmap.evalu_mark);
            linear2.addView(iv);
            for (int j = 0; j <Integer.valueOf(detail.getEvaluationInfo().get(i).getOverall()); j++) {
                ImageView star=new ImageView(this);
                star.setPadding(10,10,10,10);
                star.setLayoutParams(new ViewGroup.LayoutParams(60,60));
                star.setImageResource(R.mipmap.star);
                linear2.addView(star);
            }
            linear.addView(linear1);
            linear.addView(linear2);
            remark_content.addView(linear);
        }
        //交易记录
        for (int i = 0; i <detail.getOrderInfo().size() ; i++) {
            LinearLayout linear=new LinearLayout(this);
            linear.setOrientation(LinearLayout.VERTICAL);
            LinearLayout linear1=new LinearLayout(this);
            linear1.setOrientation(LinearLayout.HORIZONTAL);
            TextView time=new TextView(this);
            TextView content=new TextView(this);
            time.setPadding(30,10,40,10);
            content.setPadding(30,10,40,10);
            time.setText(detail.getOrderInfo().get(i).getCreate_time());
            content.setText(detail.getOrderInfo().get(i).getDescription()+"  "+detail.getOrderInfo().get(i).getAmount()+"元");
            linear1.addView(time);
            linear1.addView(content);
            linear.addView(linear1);
            order_content.addView(linear);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.school_stu_detail_Callphone:
                //打电话
                mrequestPermission();
                break;
            case R.id.school_stu_detail_r1:
                isShow(test_content,test);
                break;
            case R.id.school_stu_detail_r2:
                isShow(exam_content,exam);
                break;
            case R.id.school_stu_detail_r3:
                isShow(booking_content,booking);
                break;
            case R.id.school_stu_detail_r4:
                isShow(training_content,training);
                break;
            case R.id.school_stu_detail_r5:
                isShow(learnH_content,learnH);
                break;
            case R.id.school_stu_detail_r6:
                isShow(evalu_content,evalu);
                break;
            case R.id.school_stu_detail_r7:
                isShow(advise_content,advise);
                break;
            case R.id.school_stu_detail_r8:
                isShow(remark_content,remark);
                break;
            case R.id.school_stu_detail_r9:
                isShow(order_content,order);
                break;


        }
    }
    private void sendReuqest(final String way, int what, String url, ArrayList<String> params, ArrayList<String> values) {
        RequestMethod method=null;
        if(way.equals("POST")){
            method=RequestMethod.POST;
        }else if(way.equals("GET")){
            method=RequestMethod.GET;
        }
        Request<JSONObject> request= NoHttp.createJsonObjectRequest(url,method);
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
                    mLog.e("reponse","-->"+response.get().toString());
                    JSONObject object=new JSONObject(response.get().toString());
                    mmsg=object.getString("msg");
                    mLog.e("reponse","->"+response);
                    if(what==1){
                        if(object.getString("code").equals("200")){
                            detail=gson.fromJson(object.getString("data"),StudentDetail.class);
                            handler.sendEmptyMessage(3333);
                        }else {
                            handler.sendEmptyMessage(2222);

                        }
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

    private void isShow(final LinearLayout content,ImageView iv) {
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
            ObjectAnimator.ofFloat(iv,"rotation",0,180).setDuration(800).start();
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
            ObjectAnimator.ofFloat(iv,"rotation",-180,0).setDuration(800).start();
            content.setTag(true);
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
        intent.setData(Uri.parse("tel:"+detail.getStudentInfo().getPhone()));
        startActivity(intent);
    }

}
