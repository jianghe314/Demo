package com.kangzhan.student.School.Edu;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.kangzhan.student.Debug.mLog;
import com.kangzhan.student.R;
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

import org.json.JSONException;
import org.json.JSONObject;

public class TeacherRestActivity extends BaseActivity implements View.OnClickListener{
    private Button sure;
    private String mmsg,nname,id;
    private ScrollView scroll;
    private EditText things,remark;
    private TextView startT,endT,name;
    private ImageView choiceS,choiceE;
    private int[] mm= mCalender.getCalender();
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1111:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.showProgress(TeacherRestActivity.this,"上传中...");
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
                            showMessage.showMsg(TeacherRestActivity.this,"网络连接异常，请检查网络连接");
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
        setContentView(R.layout.activity_teacher_rest);
        Toolbar toolbar= (Toolbar) findViewById(R.id.teacher_rest_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        Intent getData=getIntent();
        nname=getData.getStringExtra("name");
        id=getData.getStringExtra("id");
        mLog.e("教练ID","->"+id);
        initView();
        addLayoutListener(scroll,sure);
    }

    private void initView() {
        sure= (Button) findViewById(R.id.teacher_rest_btn);
        sure.setOnClickListener(this);
        scroll= (ScrollView) findViewById(R.id.teacher_rest_scroll);
        name= (TextView) findViewById(R.id.school_teacher_rest_name);
        name.setText(nname);
        things= (EditText) findViewById(R.id.school_teacher_rest_things);
        remark= (EditText) findViewById(R.id.school_teacher_rest_remark);
        startT= (TextView) findViewById(R.id.school_teacher_rest_sTime);
        endT= (TextView) findViewById(R.id.school_teacher_rest_endT);
        choiceS= (ImageView) findViewById(R.id.school_teacher_rest_choiceST);
        choiceS.setOnClickListener(this);
        choiceE= (ImageView) findViewById(R.id.school_teacher_rest_choiceET);
        choiceE.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.school_teacher_rest_choiceST:
                //设置开始时间
                com.kangzhan.student.utils.picker.DatePicker picker=new com.kangzhan.student.utils.picker.DatePicker(TeacherRestActivity.this);
                picker.setRangeStart(2016,1,1);
                picker.setRangeEnd(2050,10,14);
                picker.setSelectedItem(mm[0],mm[1],mm[2]);
                picker.setOnDatePickListener(new com.kangzhan.student.utils.picker.DatePicker.OnYearMonthDayPickListener() {
                    @Override
                    public void onDatePicked(String year, String month, String day) {
                        startT.setText(year+"年"+month+"月"+day+"日");
                    }
                });
                picker.show();
                break;
            case R.id.school_teacher_rest_choiceET:
                //设置结束时间
                com.kangzhan.student.utils.picker.DatePicker picker1=new com.kangzhan.student.utils.picker.DatePicker(TeacherRestActivity.this);
                picker1.setRangeStart(2016,1,1);
                picker1.setRangeEnd(2050,10,14);
                picker1.setSelectedItem(mm[0],mm[1],mm[2]);
                picker1.setOnDatePickListener(new com.kangzhan.student.utils.picker.DatePicker.OnYearMonthDayPickListener() {
                    @Override
                    public void onDatePicked(String year, String month, String day) {
                        endT.setText(year+"年"+month+"月"+day+"日");
                    }
                });
                picker1.show();
                break;
            case R.id.teacher_rest_btn:
                if(isRight()){
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            handler.sendEmptyMessage(1111);
                            sendRequest();
                        }
                    }).start();
                }
                break;
            default:
                break;
        }
    }

    private void sendRequest() {
        Request<JSONObject> request=NoHttp.createJsonObjectRequest(school.EduTeacherRest(), RequestMethod.POST);
        request.add("key",school.schoolKey(getApplicationContext()));
        request.add("id",0);
        request.add("coach_id",id);
        request.add("start_time",getTime(startT));
        request.add("end_time",getTime(endT));
        request.add("reason",things.getText().toString().trim());
        request.add("status",10);
        request.add("remark",remark.getText().toString().trim());
        getRequestQueue().add(1, request, new OnResponseListener<JSONObject>() {
            @Override
            public void onStart(int what) {

            }

            @Override
            public void onSucceed(int what, Response<JSONObject> response) {
                try {
                    JSONObject objcet=new JSONObject(response.get().toString());
                    mLog.e("reponse","-->"+response.get().toString());
                    mmsg=objcet.getString("msg");
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

    private boolean isRight() {
        boolean mname=false,st=false,et=false,mth=false;
        if(name.getText().toString().trim().equals("")){
            mToast.showText(getApplicationContext(),"姓名不能为空");
        }else {
            mname=true;
        }
        if(startT.getText().toString().trim().equals("")){
            mToast.showText(getApplicationContext(),"开始时间不能为空");
        }else {
            st=true;
        }
        if(endT.getText().toString().trim().equals("")){
            mToast.showText(getApplicationContext(),"结束时间不能为空");
        }else {
            et=true;
        }
        if(things.getText().toString().trim().equals("")){
            mToast.showText(getApplicationContext(),"缘由不能为空");
        }else {
            mth=true;
        }
        if(mname&&st&&et&&mth){
            return true;
        }else {
            return false;
        }




    }


    private String getTime(TextView edit){
        String getTime=edit.getText().toString().trim();
        getTime=getTime.replace("年","-");
        getTime=getTime.replace("月","-");
        getTime=getTime.replace("日","");
        return getTime;
    }

    public void addLayoutListener(final View main, final View scroll) {
        main.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect rect = new Rect();
                //1、获取main在窗体的可视区域
                main.getWindowVisibleDisplayFrame(rect);
                //2、获取main在窗体的不可视区域高度，在键盘没有弹起时，main.getRootView().getHeight()调节度应该和rect.bottom高度一样
                int mainInvisibleHeight = main.getRootView().getHeight() - rect.bottom;
                int screenHeight = main.getRootView().getHeight();//屏幕高度
                //3、不可见区域大于屏幕本身高度的1/4：说明键盘弹起了
                if (mainInvisibleHeight > screenHeight / 4) {
                    int[] location = new int[2];
                    scroll.getLocationInWindow(location);
                    // 4､获取Scroll的窗体坐标，算出main需要滚动的高度
                    int srollHeight = (location[1] + scroll.getHeight()) - rect.bottom;
                    //5､让界面整体上移键盘的高度
                    main.scrollTo(0, srollHeight);
                } else {
                    //3、不可见区域小于屏幕高度1/4时,说明键盘隐藏了，把界面下移，移回到原有高度
                    main.scrollTo(0, 0);
                }
            }
        });
    }

}
