package com.kangzhan.student.Teacher.News;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kangzhan.student.Debug.mLog;
import com.kangzhan.student.R;
import com.kangzhan.student.ShowUI.showProgress;
import com.kangzhan.student.Teacher.bean.ChoiceStudentList;
import com.kangzhan.student.com.BaseFragment;
import com.kangzhan.student.com.LazyFragment;
import com.kangzhan.student.mInterface.TeacherInterface.teacher;
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

/**
 * Created by kangzhan011 on 2017/4/14.
 */

public class SendMsgFragment extends Fragment implements View.OnClickListener{
    private ImageView add;
    private View view;
    private TextView time;
    private mLableLayout sendToMan;
    private EditText MsgContent;
    private String Msg,StuPhone,StuName;
    private Button sendbtn;
    private RequestQueue requestQueue;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0000:
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.showProgress(getContext(),"发送中...");
                        }
                    });
                    break;
                case 1111:
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.closeProgress();
                            Toast.makeText(getActivity().getApplicationContext(),Msg,Toast.LENGTH_SHORT).show();
                        }
                    });
                    break;
                default:
                    break;
            }
        }
    };
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(view==null){
            view=inflater.inflate(R.layout.teacher_news_sendmsgfragment_layout,container,false);
            iniView(view);
        }
        return view;
    }

    private void iniView(View v) {
        NoHttp.initialize(getContext());
        sendbtn= (Button) v.findViewById(R.id.teacher_news_sendMsg_btn);
        sendbtn.setOnClickListener(this);
        add= (ImageView) v.findViewById(R.id.teacher_news_sendMsg_addObject);
        add.setOnClickListener(this);
        sendToMan= (mLableLayout) v.findViewById(R.id.teacher_news_sendMsg_toMan);
        sendToMan.setOnClickListener(this);
        MsgContent= (EditText) v.findViewById(R.id.teacher_news_sendMsg_content);
        time= (TextView) v.findViewById(R.id.teacher_news_sendMsg_choiceTime);
        time.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //添加数据
            case R.id.teacher_news_sendMsg_addObject:
                Intent intent=new Intent(getContext(),MystudentListActivity.class);
                startActivityForResult(intent,2);
                break;
            //发送按钮
            case R.id.teacher_news_sendMsg_btn:
                handler.sendEmptyMessage(0000);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        sendRequest();
                    }
                }).start();
                break;
            case R.id.teacher_news_sendMsg_toMan:
                AlertDialog.Builder dialog=new AlertDialog.Builder(getContext());
                dialog.setMessage("确定删除发送对象吗？");
                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sendToMan.removeAllViews();
                    }
                });
                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dialog.create().show();
                break;
            case R.id.teacher_news_sendMsg_choiceTime:
                DateTimePicker picker = new DateTimePicker(getActivity(), DateTimePicker.HOUR_24);
                picker.setDateRangeStart(2017, 1, 1);
                picker.setDateRangeEnd(2025, 11, 11);
                picker.setTimeRangeStart(00,00);
                picker.setTimeRangeEnd(23,59);
                picker.setOnDateTimePickListener(new DateTimePicker.OnYearMonthDayTimePickListener() {
                    @Override
                    public void onDateTimePicked(String year, String month, String day, String hour, String minute) {
                        time.setText(year + "-" + month + "-" + day + " " + hour + ":" + minute);
                    }
                });
                picker.show();
                break;
            default:
                break;
        }
    }


    private void sendRequest() {
        requestQueue=NoHttp.newRequestQueue();
        Request<JSONObject> request=NoHttp.createJsonObjectRequest(teacher.teacherAddMsg(), RequestMethod.POST);
        request.add("key",teacher.teacherKey(getContext().getApplicationContext()));
        request.add("sender_source",20);
        request.add("send_time",time.getText().toString().trim());
        request.add("content",MsgContent.getText().toString().trim());
        request.add("receiver_phones",StuPhone);
        requestQueue.add(1, request, new OnResponseListener<JSONObject>() {
            @Override
            public void onStart(int what) {

            }

            @Override
            public void onSucceed(int what, Response<JSONObject> response) {
                Log.i("response","->"+response.get().toString().trim());
                try {
                    JSONObject object=new JSONObject(response.get().toString().trim());
                    Msg=object.getString("msg");
                    Message msg=new Message();
                    msg.what=1111;
                    handler.sendMessage(msg);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(int what, Response<JSONObject> response) {

            }

            @Override
            public void onFinish(int what) {

            }
        });

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //mLog.e("得到调用2","------>>>>>>>");
        if(requestCode==2&&resultCode==1){
            Bundle bundle=data.getBundleExtra("data");
            StuName=bundle.getString("Name");
            StuPhone=bundle.getString("Phone");
            String[] str=StuPhone.split(",");
            LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.setMargins(5,5,5,5);
            for(int i = 0; i <str.length; i ++){
                TextView view = new TextView(getContext());
                view.setText(str[i]);
                view.setTextColor(ContextCompat.getColor(getContext(),R.color.textColor_black));
                view.setBackgroundResource(R.drawable.sendmsgbackground);
                sendToMan.addView(view,lp);
            }
        }
    }


}
