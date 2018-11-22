package com.kangzhan.student.CompayManage.Notice;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kangzhan.student.Debug.mLog;
import com.kangzhan.student.R;
import com.kangzhan.student.School.Notice.School_Notice_Choice_Object;
import com.kangzhan.student.School.Notice.School_add_Msg;
import com.kangzhan.student.ShowUI.mToast;
import com.kangzhan.student.ShowUI.showMessage;
import com.kangzhan.student.ShowUI.showProgress;
import com.kangzhan.student.com.BaseActivity;
import com.kangzhan.student.mInterface.CompayInterface.CompayInterface;
import com.kangzhan.student.mInterface.SchoolInterface.school;
import com.kangzhan.student.mUI.mLableLayout;
import com.kangzhan.student.utils.picker.DateTimePicker;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CompayAddMsgActivity extends BaseActivity implements View.OnClickListener {
    private ImageView addObjcet,choiceTime;
    private mLableLayout container;
    private EditText content;
    private TextView time;
    private String mmsg;
    private Button sure;
    private String[] phones,Names,Ids;
    private ArrayList<String> params=new ArrayList<>();
    private ArrayList<String> values=new ArrayList<>();
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1111:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.showProgress(CompayAddMsgActivity.this,"发送中...");
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
                            showMessage.showMsg(CompayAddMsgActivity.this,"网络连接异常，请检查网络连接");
                        }
                    });
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compay_add_msg);
        Toolbar toolbar= (Toolbar) findViewById(R.id.compay_add_msg_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        initView();
    }

    private void initView() {
        addObjcet= (ImageView) findViewById(R.id.compay_msg_add_Object);
        choiceTime= (ImageView) findViewById(R.id.compay_msg_add_choiceTime);
        container= (mLableLayout) findViewById(R.id.compay_msg_add_toMan);
        content= (EditText) findViewById(R.id.compay_msg_add_content);
        time= (TextView) findViewById(R.id.compay_msg_add_time);
        sure= (Button) findViewById(R.id.compay_msg_add_sure);
        choiceTime.setOnClickListener(this);
        sure.setOnClickListener(this);
        addObjcet.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.compay_msg_add_Object:
                Intent add=new Intent(this,CompayChoiceObject.class);
                startActivityForResult(add,2);
                break;
            case R.id.compay_msg_add_choiceTime:
                DateTimePicker picker = new DateTimePicker(this, DateTimePicker.HOUR_24);
                picker.setDateRangeStart(2017, 1, 1);
                picker.setDateRangeEnd(2025, 11, 11);
                picker.setTimeRangeStart(00,00);
                picker.setTimeRangeEnd(23,59);
                picker.setOnDateTimePickListener(new DateTimePicker.OnYearMonthDayTimePickListener() {
                    @Override
                    public void onDateTimePicked(String year, String month, String day, String hour, String minute) {
                        time.setText(year+"-"+month+"-"+day+" "+hour+":"+minute);
                    }
                });
                picker.show();
                break;
            case R.id.compay_msg_add_sure:
                handler.sendEmptyMessage(1111);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        params.clear();
                        values.clear();
                        params.add("key");
                        params.add("sender_source");
                        params.add("send_time");
                        params.add("content");
                        params.add("stu_ids");
                        params.add("coach_ids");
                        params.add("insti_mgrids");
                        //
                        values.add(CompayInterface.CompayKey(getApplicationContext()));
                        values.add("40");
                        values.add(time.getText().toString().trim());
                        values.add(content.getText().toString().trim());
                        values.add(Ids[0]);
                        values.add(Ids[1]);
                        values.add(Ids[2]);
                        sendRequest("POST", CompayInterface.CompayMsgAdd(),1,params,values);
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
                    if(what==1){
                        mmsg=object.getString("msg");
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


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        container.removeAllViews();
        if(requestCode==2&&resultCode==1){
            Bundle mdata=data.getBundleExtra("data");
            Names=mdata.getStringArray("Name");
            Ids=mdata.getStringArray("Id");
            phones=mdata.getStringArray("Phone");
            LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.setMargins(5,5,5,5);
            String[]  mphone=(phones[0]+phones[1]+phones[2]).split(",");
            for (int i = 0; i < mphone.length; i++) {
                TextView txt=new TextView(this);
                txt.setText(mphone[i]+"");
                txt.setTextColor(ContextCompat.getColor(this,R.color.textColor_black));
                txt.setBackgroundResource(R.drawable.sendmsgbackground);
                container.addView(txt,lp);
            }
        }

    }

}
