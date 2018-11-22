package com.kangzhan.student.School.Edu;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.ArraySet;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kangzhan.student.CompayManage.Bean.SelfRegStuM;
import com.kangzhan.student.CompayManage.SelfRegistManage.StudentMangeActivity;
import com.kangzhan.student.Debug.mLog;
import com.kangzhan.student.R;
import com.kangzhan.student.School.SlideMenu.Add_Account;
import com.kangzhan.student.School.ToolActivity.ChocieTeacherActivity;
import com.kangzhan.student.School.ToolActivity.ChoiceDeviceActivity;
import com.kangzhan.student.School.ToolActivity.ChoiceRegionActivity;
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
import java.util.Set;

public class EduCarManage_Add extends BaseActivity implements View.OnFocusChangeListener,View.OnClickListener {
    private EditText label,brand,emgine,frame;
    private View line1,line2,line3,line4,line5,line6,line7,line9;
    private TextView tv1,tv2,tv3,tv4,tv5,tv6,tv7,tv9,teacherName,pratice,deviceId,buyTime,carType;
    private RelativeLayout choice1,choice2,choice3;
    private Button save;
    private int[] mcalender= mCalender.getCalender();
    private String mmsg;
    private ArrayList<String> params=new ArrayList<>();
    private ArrayList<String> values=new ArrayList<>();
    private String[] mCarType={"A1","A2","A3","B1","B2","C1","C2","C3","C4","C5"};
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1111:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.showProgress(EduCarManage_Add.this,"上传中...");
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
                            showMessage.showMsg(EduCarManage_Add.this,"网络连接异常，请检查网络连接");
                        }
                    });
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edu_car_manage__add);
        Toolbar toolbar= (Toolbar) findViewById(R.id.teacher_CarMA_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        initView();
        //iniData();
    }
    private void initView() {
        label= (EditText) findViewById(R.id.edu_add_car_et1);
        brand= (EditText) findViewById(R.id.edu_add_car_et2);
        emgine= (EditText) findViewById(R.id.edu_add_car_et3);
        frame= (EditText) findViewById(R.id.edu_add_car_et4);
        buyTime= (TextView) findViewById(R.id.edu_add_car_et5);
        teacherName= (TextView) findViewById(R.id.edu_add_car_et6);
        pratice= (TextView) findViewById(R.id.edu_add_car_et7);
        deviceId= (TextView) findViewById(R.id.edu_add_car_et9);
        carType= (TextView) findViewById(R.id.edu_add_car_et51);
        carType.setOnClickListener(this);
        //
        label.setOnFocusChangeListener(this);
        brand.setOnFocusChangeListener(this);
        emgine.setOnFocusChangeListener(this);
        emgine.setOnFocusChangeListener(this);
        frame.setOnFocusChangeListener(this);
        buyTime.setOnClickListener(this);
        teacherName.setOnFocusChangeListener(this);
        pratice.setOnFocusChangeListener(this);
        deviceId.setOnFocusChangeListener(this);
        //
        choice1= (RelativeLayout) findViewById(R.id.edu_car_manage_choice_t);
        choice2= (RelativeLayout) findViewById(R.id.edu_car_manage_choice_p);
        choice3= (RelativeLayout) findViewById(R.id.edu_car_manage_choice_d);
        choice1.setOnClickListener(this);
        choice2.setOnClickListener(this);
        choice3.setOnClickListener(this);
        //
        line1=findViewById(R.id.edu_add_car_l1);
        line2=findViewById(R.id.edu_add_car_l2);
        line3=findViewById(R.id.edu_add_car_l3);
        line4=findViewById(R.id.edu_add_car_l4);
        line5=findViewById(R.id.edu_add_car_l5);
        line6=findViewById(R.id.edu_add_car_l6);
        line7=findViewById(R.id.edu_add_car_l7);
        line9=findViewById(R.id.edu_add_car_l9);
        //
        tv1= (TextView) findViewById(R.id.edu_add_car1);
        tv2= (TextView) findViewById(R.id.edu_add_car2);
        tv3= (TextView) findViewById(R.id.edu_add_car3);
        tv4= (TextView) findViewById(R.id.edu_add_car4);
        tv5= (TextView) findViewById(R.id.edu_add_car5);
        tv6= (TextView) findViewById(R.id.edu_add_car6);
        tv7= (TextView) findViewById(R.id.edu_add_car7);
        tv9= (TextView) findViewById(R.id.edu_add_car9);
        //
        save= (Button) findViewById(R.id.edu_car_manage_addCar_save);
        save.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        teacherName.setText(getChoiceTeacher()[0]);
        pratice.setText(getRegionData()[0]);
        deviceId.setText(getChoiceDevice()[0]);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()){
            case R.id.edu_add_car_et1:
                if(hasFocus){
                    setTextViewColor(tv1);
                    setTextViewColor(label);
                    setLineColor(line1);
                }else {
                    clearLineColor(line1);
                    clearTextViewColor(tv1);
                    clearTextViewColor(label);
                }
                break;
            case R.id.edu_add_car_et2:
                if(hasFocus){
                    setTextViewColor(tv2);
                    setTextViewColor(brand);
                    setLineColor(line2);
                }else {
                    clearLineColor(line2);
                    clearTextViewColor(tv2);
                    clearTextViewColor(brand);
                }
                break;
            case R.id.edu_add_car_et3:
                if(hasFocus){
                    setTextViewColor(tv3);
                    setTextViewColor(emgine);
                    setLineColor(line3);
                }else {
                    clearLineColor(line3);
                    clearTextViewColor(tv3);
                    clearTextViewColor(emgine);
                }
                break;
            case R.id.edu_add_car_et4:
                if(hasFocus){
                    setTextViewColor(tv4);
                    setTextViewColor(frame);
                    setLineColor(line4);
                }else {
                    clearLineColor(line4);
                    clearTextViewColor(tv4);
                    clearTextViewColor(frame);
                }
                break;
            case R.id.edu_add_car_et5:
                if(hasFocus){
                    setTextViewColor(tv5);
                    setTextViewColor(buyTime);
                    setLineColor(line5);
                }else {
                    clearLineColor(line5);
                    clearTextViewColor(tv5);
                    clearTextViewColor(buyTime);
                }
                break;
            case R.id.edu_add_car_et6:
                if(hasFocus){
                    setTextViewColor(tv6);
                    setTextViewColor(teacherName);
                    setLineColor(line6);
                }else {
                    clearLineColor(line6);
                    clearTextViewColor(tv6);
                    clearTextViewColor(teacherName);
                }
                break;
            case R.id.edu_add_car_et7:
                if(hasFocus){
                    setTextViewColor(tv7);
                    setTextViewColor(pratice);
                    setLineColor(line7);
                }else {
                    clearLineColor(line7);
                    clearTextViewColor(tv7);
                    clearTextViewColor(pratice);
                }
                break;
            case R.id.edu_add_car_et9:
                if(hasFocus){
                    setTextViewColor(tv9);
                    setTextViewColor(deviceId);
                    setLineColor(line9);
                }else {
                    clearLineColor(line9);
                    clearTextViewColor(tv9);
                    clearTextViewColor(deviceId);
                }
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.edu_car_manage_choice_t:
                //添加教练
                Intent add=new Intent(this, ChocieTeacherActivity.class);
                add.putExtra("Type",11);
                startActivity(add);
                break;
            case R.id.edu_add_car_et5:
                //购买日期
                com.kangzhan.student.utils.picker.DatePicker picker=new com.kangzhan.student.utils.picker.DatePicker(EduCarManage_Add.this);
                picker.setRangeStart(2016,1,1);
                picker.setRangeEnd(2050,10,14);
                picker.setSelectedItem(mcalender[0],mcalender[1],mcalender[2]);
                picker.setOnDatePickListener(new com.kangzhan.student.utils.picker.DatePicker.OnYearMonthDayPickListener() {
                    @Override
                    public void onDatePicked(String year, String month, String day) {
                        buyTime.setText(year+"年"+month+"月"+day+"日");
                    }
                });
                picker.show();
                break;
            case R.id.edu_add_car_et51:
                //选择车型
                AlertDialog.Builder bulder=new AlertDialog.Builder(this);
                bulder.setTitle("选择车型");
                bulder.setSingleChoiceItems(mCarType, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        carType.setText(mCarType[which]);
                        dialog.dismiss();
                    }
                });
                bulder.setCancelable(true);
                bulder.create().show();
                break;
            case R.id.edu_car_manage_choice_p:
                //添加训练场
                Intent region=new Intent(this, ChoiceRegionActivity.class);
                startActivity(region);
                break;
            case R.id.edu_car_manage_choice_d:
                //计时终端
                Intent device=new Intent(this, ChoiceDeviceActivity.class);
                startActivity(device);
                break;
            case R.id.edu_car_manage_addCar_save:
                //保存
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        handler.sendEmptyMessage(1111);
                        params.clear();
                        values.clear();
                        params.add("key");
                        params.add("licnum");
                        params.add("franum");
                        params.add("engnum");
                        params.add("buydate");
                        params.add("brand");
                        params.add("coach_id");
                        params.add("region_id");
                        params.add("device_id");
                        params.add("perdritype");
                        //
                        values.add(school.schoolKey(getApplicationContext()));
                        values.add(label.getText().toString().trim());
                        values.add(emgine.getText().toString().trim());
                        values.add(frame.getText().toString().trim());
                        values.add(getTime(buyTime));
                        values.add(brand.getText().toString().trim());
                        values.add(getChoiceTeacher()[1].substring(0,getChoiceTeacher()[1].length()-1));
                        values.add(getRegionData()[1]);
                        values.add(getChoiceDevice()[1]);
                        values.add(carType.getText().toString().trim());
                        mLog.e("ChoiceTeacher_Name_Id","-->"+getChoiceTeacher()[0]+"-"+getChoiceTeacher()[1].substring(0,getChoiceTeacher()[1].length()-1));
                        sendRequest("POST",school.EduAddCar(),1,params,values);
                    }
                }).start();
                break;
        }
    }

    private void sendRequest(String Way, String url, int what, ArrayList<String> params, ArrayList<String> values) {
        RequestMethod method=null;
        if(Way.equals("POST")){
            method=RequestMethod.POST;
        }else if(Way.equals("GET")){
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
                    JSONObject object=new JSONObject(response.get().toString());
                    mLog.e("reponse","-->"+response.get().toString());
                    mmsg=object.getString("msg");
                    if(what==1){
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

    private void setTextViewColor(TextView txt){
        txt.setTextColor(ContextCompat.getColor(EduCarManage_Add.this,R.color.text_background_color_aqua));
    }
    private void setLineColor(View view){
        view.setBackgroundColor(ContextCompat.getColor(EduCarManage_Add.this,R.color.text_background_color_aqua));
    }
    private void clearLineColor(View view){
        view.setBackgroundColor(ContextCompat.getColor(EduCarManage_Add.this,R.color.color_line));
    }
    private void clearTextViewColor(TextView txt){
        txt.setTextColor(ContextCompat.getColor(EduCarManage_Add.this,R.color.textColor_gray));
    }

    private String[] getRegionData(){
        String[] regionData=new String[2];
        SharedPreferences sp=getSharedPreferences("ChoiceRegion",MODE_PRIVATE);
        regionData[0]=sp.getString("regionName","");
        regionData[1]=sp.getString("regionId","");
        return regionData;
    }
    private String[] getChoiceTeacher(){
        String[] regionData=new String[2];
        SharedPreferences sp=getSharedPreferences("ChoiceTeacher",MODE_PRIVATE);
        regionData[0]=sp.getString("Name","");
        regionData[1]=sp.getString("Id","");
        return regionData;
    }
    private String[] getChoiceDevice(){
        String[] regionData=new String[2];
        SharedPreferences sp=getSharedPreferences("ChoiceDevice",MODE_PRIVATE);
        regionData[0]=sp.getString("DeviceNum","");
        regionData[1]=sp.getString("DeviceId","");
        return regionData;
    }


    private String getTime(TextView edit){
        String getTime=edit.getText().toString().trim();
        getTime=getTime.replace("年","-");
        getTime=getTime.replace("月","-");
        getTime=getTime.replace("日","");
        return getTime;
    }
}
