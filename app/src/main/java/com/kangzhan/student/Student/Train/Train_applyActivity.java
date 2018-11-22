package com.kangzhan.student.Student.Train;

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
import android.widget.Toast;

import com.kangzhan.student.Debug.mLog;
import com.kangzhan.student.R;
import com.kangzhan.student.ShowUI.mToast;
import com.kangzhan.student.ShowUI.showMessage;
import com.kangzhan.student.ShowUI.showProgress;
import com.kangzhan.student.com.BaseActivity;
import com.kangzhan.student.mInterface.StudentInterface.student;
import com.kangzhan.student.utils.mCalendar.mCalender;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.Response;

import org.json.JSONException;
import org.json.JSONObject;

public class Train_applyActivity extends BaseActivity implements View.OnClickListener{
    private Button Apply_sure,Apply_record;
    private ImageView choiceDate,isPickUp;
    private  int[] mm= mCalender.getCalender();
    private TextView time,ispickUp;
    private EditText startHour,startMin,endHour,endMin,address,price,stage,other;
    private String mmsg;
    private String id;
    private String getTime;
    private int pickUp;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0000:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.showProgress(Train_applyActivity.this,"申请中...");
                        }
                    });
                    break;
                case 1111:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.closeProgress();
                            AlertDialog.Builder builder=new AlertDialog.Builder(Train_applyActivity.this);
                            builder.setMessage(mmsg);
                            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    finish();
                                }
                            });
                            builder.create().show();
                        }
                    });
                    break;
                case 2222:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.closeProgress();
                            AlertDialog.Builder builder=new AlertDialog.Builder(Train_applyActivity.this);
                            builder.setMessage(mmsg);
                            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            builder.create().show();
                        }
                    });
                    break;
                case 9999:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.closeProgress();
                            showMessage.showMsg(Train_applyActivity.this,"网络连接异常，请检测网络连接");
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
        setContentView(R.layout.activity_train_apply);
        Toolbar toolbar= (Toolbar) findViewById(R.id.student_tain_apply_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        Intent data=getIntent();
        id=data.getStringExtra("Id");
        initView();
    }

    private void initView() {
        Apply_sure= (Button) findViewById(R.id.train_apply_sure);
        Apply_sure.setOnClickListener(this);
        Apply_record= (Button) findViewById(R.id.train_apply_record);
        Apply_record.setOnClickListener(this);
        choiceDate= (ImageView) findViewById(R.id.trian_apply_choice_date_btn);
        choiceDate.setOnClickListener(this);
        isPickUp= (ImageView) findViewById(R.id.choice_isPickUp);
        isPickUp.setOnClickListener(this);
        time= (TextView) findViewById(R.id.train_apply_date1);
        ispickUp= (TextView) findViewById(R.id.train_apply_pickUp_tv);
        //
        startHour= (EditText) findViewById(R.id.trian_apply_startHour);
        startMin= (EditText) findViewById(R.id.trian_apply_startMinute);
        endHour= (EditText) findViewById(R.id.trian_apply_endHour);
        endMin= (EditText) findViewById(R.id.trian_apply_endMinute);
        address= (EditText) findViewById(R.id.trian_apply_address);
        price= (EditText) findViewById(R.id.train_apply_price_et);
        stage= (EditText) findViewById(R.id.train_apply_trainPhases_et);
        other= (EditText) findViewById(R.id.train_apply_other_et);
    }
    private String gettime(){
        getTime=time.getText().toString().trim();
        getTime=getTime.replace("年","-");
        getTime=getTime.replace("月","-");
        getTime=getTime.replace("日","");
       return getTime;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //选择时间
            case R.id.trian_apply_choice_date_btn:
                com.kangzhan.student.utils.picker.DatePicker picker=new com.kangzhan.student.utils.picker.DatePicker(Train_applyActivity.this);
                picker.setRangeStart(2016,1,1);
                picker.setRangeEnd(2050,10,14);
                picker.setSelectedItem(mm[0],mm[1],mm[2]);
                picker.setOnDatePickListener(new com.kangzhan.student.utils.picker.DatePicker.OnYearMonthDayPickListener() {
                    @Override
                    public void onDatePicked(String year, String month, String day) {
                        time.setText(year+"年"+month+"月"+day+"日");
                    }
                });
                picker.show();
                break;
            //是否接送
            case R.id.choice_isPickUp:
                final String[] items={"不接送", "要接送"};
                AlertDialog.Builder builder=new AlertDialog.Builder(Train_applyActivity.this);
                builder.setSingleChoiceItems(items,2, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ispickUp.setText(items[which]);
                        if(which==0){
                            pickUp=1;
                        }else if(which==1){
                            pickUp=2;
                        }
                        dialog.dismiss();
                        //1:接送  2;不接受
                    }
                });
                builder.create().show();
                break;
            //确定
            case R.id.train_apply_sure:
                if(isRight()){
                    Message msg=new Message();
                    msg.what=0000;
                    handler.sendMessage(msg);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            sendRequest(id,gettime()+" "+startHour.getText().toString().trim()+":"+startMin.getText().toString().trim(),gettime()+" "+endHour.getText().toString().trim()+":"+endMin.getText().toString().trim(),
                                    pickUp,address.getText().toString().trim(),price.getText().toString().trim(),stage.getText().toString().trim()+other.getText().toString().trim());
                        }
                    }).start();
                }
                break;
            //我的陪练申请--申请记录
            case R.id.train_apply_record:
                Intent record=new Intent(this,Train_BookingActivity.class);
                startActivity(record);
                finish();
                break;
            default:
                break;
        }
    }

    private void sendRequest(String teacher_id,String startT,String endT,int shuttle,String address,String price,String detail) {
        mLog.e("startT-endT","->"+startT+"-"+endT);
        Request<JSONObject> request=NoHttp.createJsonObjectRequest(student.studentTrainApply(), RequestMethod.POST);
        request.add("key",student.studentKey(Train_applyActivity.this));
        request.add("coach_ids",teacher_id);
        request.add("start_time",startT);
        request.add("end_time",endT);
        request.add("shuttle",shuttle);
        request.add("address",address);
        request.add("price",price);
        request.add("detail",detail);
        getRequestQueue().add(1, request, new OnResponseListener<JSONObject>() {
            @Override
            public void onStart(int what) {

            }

            @Override
            public void onSucceed(int what, Response<JSONObject> response) {
                mLog.i("response","->"+response.get().toString());
                try {
                    JSONObject object=new JSONObject(response.get().toString());
                    String code=object.getString("code");
                    mmsg=object.getString("msg");
                    if(code.equals("200")){
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

    private boolean isRight() {
        boolean mtime=false,mstartH=false,mstartM=false,mendH=false,mendM=false,maddress=false,mprice=false,ispick=false;
        boolean p1=false,p2=false,p3=false,p4=false;
        if(time.getText().toString().trim().equals("")){
            mToast.showText(getApplicationContext(),"日期不能为空");
        }else {
            mtime=true;
        }
        if(startHour.getText().toString().trim().equals("")){
            mToast.showText(getApplicationContext(),"开始时间不能为空");
        }else {
            mstartH=true;
        }
        if(mtime&&mstartH){
            p1=true;
        }else {
            p1=false;
        }
        if(startMin.getText().toString().trim().equals("")){
            mToast.showText(getApplicationContext(),"开始时间不能为空");
        }else {
            mstartM=true;
        }
        if(endHour.getText().toString().trim().equals("")){
            mToast.showText(getApplicationContext(),"结束时间不能为空");
        }else {
            mendH=true;
        }
        if(mstartM&&mendH){
            p2=true;
        }else {
            p2=false;
        }
        if(endMin.getText().toString().trim().equals("")){
            mToast.showText(getApplicationContext(),"结束时间不能为空");
        }else {
            mendH=true;
        }
        if(address.getText().toString().trim().equals("")){
            mToast.showText(getApplicationContext(),"地址不能为空");
        }else {
            maddress=true;
        }
        if(mendH&&maddress){
            p3=true;
        }else {
            p3=false;
        }
        if(price.getText().toString().trim().equals("")){
            mToast.showText(getApplicationContext(),"价格不能为空");
        }else {
            mprice=true;
        }
        if(ispickUp.getText().toString().trim().equals("")){
            mToast.showText(getApplicationContext(),"接送不能为空");
        }else {
            ispick=true;
        }
        if(mprice&&ispick){
            p4=true;
        }else {
            p4=false;
        }

        if(p1&&p2&&p3&&p4){
            return true;
        }else {
            return false;
        }

    }

}
