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
import com.kangzhan.student.School.Notice.School_add_notice;
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

public class CompayAddNoticeActivity extends BaseActivity implements View.OnClickListener {
    private ImageView addObjcet,isNow,choiceDate;
    private EditText title,content;
    private Button send;
    private TextView time;
    private  int flag;
    private String mmsg;
    private String[] phones,Names,Ids;
    private mLableLayout container;
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
                            showProgress.showProgress(CompayAddNoticeActivity.this,"发送中...");
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
                            showMessage.showMsg(CompayAddNoticeActivity.this,"网络连接异常，请检查网络连接");
                        }
                    });
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compay_add_notice);
        Toolbar toolbar= (Toolbar) findViewById(R.id.compay_add_notice_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        initView();
    }

    private void initView() {
        addObjcet= (ImageView) findViewById(R.id.compay_notice_addNotice_addObject);
        isNow= (ImageView) findViewById(R.id.compay_notice_addNotice_isNow);
        isNow.setTag(0);
        choiceDate= (ImageView) findViewById(R.id.compay_notice_addNotice_chocieDate);
        send= (Button) findViewById(R.id.compay_notice_addNotice_send);
        addObjcet.setOnClickListener(this);
        isNow.setOnClickListener(this);
        choiceDate.setOnClickListener(this);
        send.setOnClickListener(this);
        //
        title= (EditText) findViewById(R.id.compay_notice_addNotice_title);
        content= (EditText) findViewById(R.id.compay_notice_addNotice_content);
        //
        time= (TextView) findViewById(R.id.compay_notice_addNotice_time);
        container= (mLableLayout) findViewById(R.id.compay_notice_addNotice_container);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.compay_notice_addNotice_addObject:
                //添加发送对象
                Intent choice=new Intent(this,CompayChoiceObject.class);
                startActivityForResult(choice,1);
                break;
            case R.id.compay_notice_addNotice_isNow:
                flag= (int) isNow.getTag();
                if(flag==0){
                    isNow.setImageResource(R.mipmap.teacher_news_sendnotice);
                    isNow.setTag(1);
                }else {
                    isNow.setImageResource(R.mipmap.teacher_news_choice);
                    isNow.setTag(0);
                }
                break;
            case R.id.compay_notice_addNotice_chocieDate:
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
            case R.id.compay_notice_addNotice_send:
                handler.sendEmptyMessage(1111);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        params.clear();
                        values.clear();
                        params.add("key");
                        params.add("sender_source");
                        params.add("send_id");
                        params.add("send_time");
                        params.add("summary");
                        params.add("content");
                        params.add("is_sms");
                        //
                        params.add("stu_ids");
                        params.add("coach_ids");
                        params.add("insti_ids");
                       /* params.add("clerk_ids");
                        params.add("trsndti_ids");*/
                        values.add(CompayInterface.CompayKey(getApplicationContext()));
                        values.add("40");
                        values.add(CompayInterface.CompayId(getApplicationContext()));
                        values.add(time.getText().toString().trim());
                        values.add(title.getText().toString());
                        values.add(content.getText().toString());
                        values.add((int)isNow.getTag()+"");
                        values.add(Ids[0]);
                        values.add(Ids[1]);
                        values.add(Ids[2]);
                        sendRequest("POST", CompayInterface.CompayNoticeAdd(),1,params,values);
                    }
                }).start();
                break;
            default:
                break;
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        container.removeAllViews();
        if(requestCode==1&&resultCode==1){
            Bundle mdata=data.getBundleExtra("data");
            Names=mdata.getStringArray("Name");
            Ids=mdata.getStringArray("Id");
            phones=mdata.getStringArray("Phone");
            LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.setMargins(5,5,5,5);
            String[]  names=(Names[0]+Names[1]+Names[2]).split(",");
            for (int i = 0; i < names.length; i++) {
                TextView txt=new TextView(this);
                txt.setText(names[i]+"");
                txt.setTextColor(ContextCompat.getColor(this,R.color.textColor_black));
                txt.setBackgroundResource(R.drawable.sendmsgbackground);
                container.addView(txt,lp);
            }
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
                    mLog.e("reponse","--->"+response.get().toString());
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

}
