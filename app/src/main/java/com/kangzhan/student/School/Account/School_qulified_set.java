package com.kangzhan.student.School.Account;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kangzhan.student.Debug.mLog;
import com.kangzhan.student.R;
import com.kangzhan.student.School.Bean.AccountAffirm;
import com.kangzhan.student.School.Edu.StudentManageActivity;
import com.kangzhan.student.ShowUI.mToast;
import com.kangzhan.student.ShowUI.showMessage;
import com.kangzhan.student.ShowUI.showProgress;
import com.kangzhan.student.com.BaseActivity;
import com.kangzhan.student.mInterface.SchoolInterface.school;
import com.kangzhan.student.utils.mCalendar.mCalender;
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

public class School_qulified_set extends BaseActivity implements View.OnClickListener{
    private ImageView choiceStartT,choiceEndT,twoArrow,threeArrow;
    private TextView StartT,EndT;
    private EditText carType,sujectTwo1,sujectTwo2,sujectTwo3,sujectTwo4,sujectTwo5,subjcetThree1,subjcetThree2,subjcetThree3,subjcetThree4,subjcetThree5,other;
    private RelativeLayout subjectTwoTitle,subjectThreeTitle;
    private LinearLayout content1,content2;
    private Button save;
    private String mmsg;
    private int[] mcalender= mCalender.getCalender();
    private boolean Open1=false;
    private boolean Open2=false;
    private ArrayList<String> params=new ArrayList<>();
    private ArrayList<String> values=new ArrayList<>();
    private RequestQueue requestQueue;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1111:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.showProgress(School_qulified_set.this,"保存中...");
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
                            showMessage.showMsg(School_qulified_set.this,"网络连接异常，请检查网络连接");
                        }
                    });
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_qulified_set);
        Toolbar toolbar= (Toolbar) findViewById(R.id.school_account_qulified_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        requestQueue=NoHttp.newRequestQueue();
        initView();
    }

    private void initView() {
        twoArrow= (ImageView) findViewById(R.id.school_account_qulifi_arrow_two);
        threeArrow= (ImageView) findViewById(R.id.school_account_qulifi_arrow_three);
        choiceStartT= (ImageView) findViewById(R.id.school_account_qulifi_start_choice);
        choiceEndT= (ImageView) findViewById(R.id.school_account_qulifi_endt_choice);
        choiceStartT.setOnClickListener(this);
        choiceEndT.setOnClickListener(this);
        save= (Button) findViewById(R.id.school_qulifi_save);
        save.setOnClickListener(this);
        //
        StartT= (TextView) findViewById(R.id.school_account_qulifi_start);
        EndT= (TextView) findViewById(R.id.school_account_qulifi_endT);
        //
        carType= (EditText) findViewById(R.id.school_account_qulifi_carType);
        other= (EditText) findViewById(R.id.school_qulifi_other);
        //
        subjectTwoTitle= (RelativeLayout) findViewById(R.id.school_qulifi_setSubject_two_title);
        subjectThreeTitle= (RelativeLayout) findViewById(R.id.school_qulifi_setSubject_three_title);
        subjectTwoTitle.setOnClickListener(this);
        subjectThreeTitle.setOnClickListener(this);
        //
        content1= (LinearLayout) findViewById(R.id.school_qulifi_setSubject_two);
        content2= (LinearLayout) findViewById(R.id.school_qulifi_setSubject_three);
        //默认content1和content2不展开
        View view1= LayoutInflater.from(School_qulified_set.this).inflate(R.layout.item_school_account_qulifi_subjecttwo,null);
        View view2= LayoutInflater.from(School_qulified_set.this).inflate(R.layout.item_school_account_qulifi_three,null);
        sujectTwo1= (EditText) view1.findViewById(R.id.item_school_sbjTwo_1);
        sujectTwo2= (EditText) view1.findViewById(R.id.item_school_sbjTwo_2);
        sujectTwo3= (EditText) view1.findViewById(R.id.item_school_sbjTwo_3);
        sujectTwo4= (EditText) view1.findViewById(R.id.item_school_sbjTwo_4);
        sujectTwo5= (EditText) view1.findViewById(R.id.item_school_sbjTwo_5);
        subjcetThree1= (EditText) view2.findViewById(R.id.item_school_sbjthree_1);
        subjcetThree2= (EditText) view2.findViewById(R.id.item_school_sbjthree_2);
        subjcetThree3= (EditText) view2.findViewById(R.id.item_school_sbjthree_3);
        subjcetThree4= (EditText) view2.findViewById(R.id.item_school_sbjthree_4);
        subjcetThree5= (EditText) view2.findViewById(R.id.item_school_sbjthree_5);
        content1.addView(view1);
        content2.addView(view2);
        ViewGroup.LayoutParams lp1=content1.getLayoutParams();
        lp1.height=0;
        content1.setLayoutParams(lp1);
        ViewGroup.LayoutParams lp2=content2.getLayoutParams();
        lp2.height=0;
        content2.setLayoutParams(lp2);
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.school_qulifi_setSubject_two_title:
                isShow1(Open1);
                break;
            case R.id.school_qulifi_setSubject_three_title:
                isShow2(Open2);
                break;
            case R.id.school_account_qulifi_start_choice:
                com.kangzhan.student.utils.picker.DatePicker picker1=new com.kangzhan.student.utils.picker.DatePicker(School_qulified_set.this);
                picker1.setRangeStart(2016,1,1);
                picker1.setRangeEnd(2050,10,14);
                picker1.setSelectedItem(mcalender[0],mcalender[1],mcalender[2]);
                picker1.setOnDatePickListener(new com.kangzhan.student.utils.picker.DatePicker.OnYearMonthDayPickListener() {
                    @Override
                    public void onDatePicked(String year, String month, String day) {
                        StartT.setText(year+"年"+month+"月"+day+"日");
                    }
                });
                picker1.show();
                break;
            case R.id.school_account_qulifi_endt_choice:
                com.kangzhan.student.utils.picker.DatePicker picker2=new com.kangzhan.student.utils.picker.DatePicker(School_qulified_set.this);
                picker2.setRangeStart(2016,1,1);
                picker2.setRangeEnd(2050,10,14);
                picker2.setSelectedItem(mcalender[0],mcalender[1],mcalender[2]);
                picker2.setOnDatePickListener(new com.kangzhan.student.utils.picker.DatePicker.OnYearMonthDayPickListener() {
                    @Override
                    public void onDatePicked(String year, String month, String day) {
                        EndT.setText(year+"年"+month+"月"+day+"日");
                    }
                });
                picker2.show();
                break;
            case R.id.school_qulifi_save:
                //保存
                handler.sendEmptyMessage(1111);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        params.clear();
                        values.clear();
                        params.add("key");
                        params.add("vehicletype");
                        params.add("part2_once");
                        params.add("part2_twice");
                        params.add("part2_thrice");
                        params.add("part2_quartic");
                        params.add("part2_quintic");
                        params.add("part3_once");
                        params.add("part3_twice");
                        params.add("part3_thrice");
                        params.add("part3_quartic");
                        params.add("part3_quintic");
                        params.add("remark");
                        //
                        values.add(school.schoolKey(School_qulified_set.this));
                        values.add(carType.getText().toString().trim());
                        values.add(sujectTwo1.getText().toString().trim());
                        values.add(sujectTwo2.getText().toString().trim());
                        values.add(sujectTwo3.getText().toString().trim());
                        values.add(sujectTwo4.getText().toString().trim());
                        values.add(sujectTwo5.getText().toString().trim());
                        values.add(subjcetThree1.getText().toString().trim());
                        values.add(subjcetThree2.getText().toString().trim());
                        values.add(subjcetThree3.getText().toString().trim());
                        values.add(subjcetThree4.getText().toString().trim());
                        values.add(subjcetThree5.getText().toString().trim());
                        values.add(other.getText().toString().trim());
                        sendRequest("POST",1, school.AccountQulifiedSet(),params,values);
                    }
                }).start();
                break;
            default:
                break;
        }
    }


    private void sendRequest(String way, int what, String url, ArrayList<String> params, ArrayList<String> values) {
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
        requestQueue.add(what, request, new OnResponseListener<JSONObject>() {
            @Override
            public void onStart(int what) {

            }

            @Override
            public void onSucceed(int what, Response<JSONObject> response) {
                mLog.e("reponse","->"+response.get().toString());
                if(what==1){
                    try {
                        JSONObject object=new JSONObject(response.get().toString());
                        mmsg=object.getString("msg");
                        handler.sendEmptyMessage(2222);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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


    private void isShow1(boolean isOpen) {
        content1.measure(0,0);      //测量高度
        int measureHeight=content1.getMeasuredHeight();
        if(isOpen){
            int endH=0;
            ValueAnimator animator=ValueAnimator.ofInt(measureHeight,endH);
            animator.setDuration(500);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int values= (int) animation.getAnimatedValue();
                    ViewGroup.LayoutParams lp=content1.getLayoutParams();
                    lp.height= values;
                    content1.setLayoutParams(lp);
                }
            });
            animator.start();
            ObjectAnimator.ofFloat(twoArrow,"rotation",-180,0).setDuration(500).start();
            Open1=false;
        }else {
            int startH=0;
            ValueAnimator animator=ValueAnimator.ofInt(startH,measureHeight);
            animator.setDuration(500);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    ViewGroup.LayoutParams lp=content1.getLayoutParams();
                    lp.height= (int) animation.getAnimatedValue();
                    content1.setLayoutParams(lp);
                }
            });
            animator.start();
            ObjectAnimator.ofFloat(twoArrow,"rotation",0,180).setDuration(500).start();
            Open1=true;
        }
    }
    private void isShow2(boolean isOpen) {
        content2.measure(0,0);      //测量高度
        int measureHeight=content2.getMeasuredHeight();
        if(isOpen){
            int endH=0;
            ValueAnimator animator=ValueAnimator.ofInt(measureHeight,endH);
            animator.setDuration(500);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int values= (int) animation.getAnimatedValue();
                    ViewGroup.LayoutParams lp= content2.getLayoutParams();
                    lp.height= values;
                    content2.setLayoutParams(lp);
                }
            });
            animator.start();
            ObjectAnimator.ofFloat(threeArrow,"rotation",-180,0).setDuration(500).start();
            Open2=false;
        }else {
            int startH=0;
            ValueAnimator animator=ValueAnimator.ofInt(startH,measureHeight);
            animator.setDuration(500);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    ViewGroup.LayoutParams lp=content2.getLayoutParams();
                    lp.height= (int) animation.getAnimatedValue();
                    content2.setLayoutParams(lp);
                }
            });
            animator.start();
            ObjectAnimator.ofFloat(threeArrow,"rotation",0,180).setDuration(500).start();
            Open2=true;
        }
    }

    @Override
    protected void onDestroy() {
        requestQueue.cancelAll();
        super.onDestroy();
    }
}
