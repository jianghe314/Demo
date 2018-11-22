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
import com.kangzhan.student.ShowUI.showMessage;
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

public class SendNoticeFragment extends Fragment implements View.OnClickListener{
    private EditText noticeTitle,noticeContent;
    private TextView ChoiceTime;
    private Button sendbtn;
    private ImageView add,chocieMsg;
    //还有一个发送对象
    private mLableLayout sendToMan;
    private RequestQueue requestQueue;
    private int isSMS=0;
    private String mmsg;
    private View view;
    private String stuID,stuName;
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
                            Toast.makeText(getActivity().getApplicationContext(),mmsg,Toast.LENGTH_SHORT).show();
                        }
                    });
                    break;
                case 9999:
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.closeProgress();
                            showMessage.showMsg(getContext(),"网络连接异常，请检查网络连接");
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
            view=inflater.inflate(R.layout.teacher_news_sendnoticefragment_layout,container,false);
            intView(view);
        }
        return view;
    }

    private void intView(View v) {
        add= (ImageView) v.findViewById(R.id.teacher_news_sendNotice_addObject);
        add.setOnClickListener(this);
        chocieMsg= (ImageView) v.findViewById(R.id.teacher_news_sendNotice_choice);
        chocieMsg.setOnClickListener(this);
        chocieMsg.setTag(0);
        noticeTitle= (EditText) v.findViewById(R.id.teacher_news_sendNotice_title);
        noticeContent= (EditText) v.findViewById(R.id.teacher_news_sendNotice_content);
        sendToMan= (mLableLayout) v.findViewById(R.id.teacher_news_sendNotice_toMan);
        sendToMan.setOnClickListener(this);
        sendbtn= (Button) v.findViewById(R.id.teacher_news_sendNotice_sendBtn);
        sendbtn.setOnClickListener(this);
        ChoiceTime= (TextView) v.findViewById(R.id.teacher_news_sendNotice_choiceTime);
        ChoiceTime.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.teacher_news_sendNotice_addObject:
                Intent choice=new Intent(getContext(),MystudentListActivity.class);
                startActivityForResult(choice,1);
                break;
            case R.id.teacher_news_sendNotice_choice:
                int mtag= (int) chocieMsg.getTag();
                if(mtag==0){
                    //选中
                    chocieMsg.setImageResource(R.mipmap.teacher_news_sendnotice);
                    chocieMsg.setTag(1);
                    isSMS=1;
                }else {
                    //反选
                    chocieMsg.setImageResource(R.mipmap.teacher_news_choice);
                    chocieMsg.setTag(0);
                    isSMS=0;
                }
                break;
            case R.id.teacher_news_sendNotice_sendBtn:
                //发送通知
                if(isRight()){
                    handler.sendEmptyMessage(0000);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            sendRequest();
                        }
                    }).start();
                }
                break;
            case R.id.teacher_news_sendNotice_toMan:
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
            case R.id.teacher_news_sendNotice_choiceTime:
                DateTimePicker picker = new DateTimePicker(getActivity(), DateTimePicker.HOUR_24);
                picker.setDateRangeStart(2017, 1, 1);
                picker.setDateRangeEnd(2025, 11, 11);
                picker.setTimeRangeStart(00, 00);
                picker.setTimeRangeEnd(23, 59);
                picker.setOnDateTimePickListener(new DateTimePicker.OnYearMonthDayTimePickListener() {
                    @Override
                    public void onDateTimePicked(String year, String month, String day, String hour, String minute) {
                        ChoiceTime.setText(year + "-" + month + "-" + day + " " + hour + ":" + minute);
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
        Request<JSONObject> request=NoHttp.createJsonObjectRequest(teacher.teacherAddNotice(), RequestMethod.POST);
        request.add("key",teacher.teacherKey(getContext().getApplicationContext()));
        request.add("stu_ids",stuID);  //学员ID
        request.add("sender_source",20);
        request.add("send_time",ChoiceTime.getText().toString().trim());
        request.add("summary",noticeTitle.getText().toString().trim());
        request.add("content",noticeContent.getText().toString().trim());
        request.add("is_sms",isSMS);
        requestQueue.add(1, request, new OnResponseListener<JSONObject>() {
            @Override
            public void onStart(int what) {

            }

            @Override
            public void onSucceed(int what, Response<JSONObject> response) {
                mLog.i("response","->"+response.get().toString());
                try {
                    JSONObject object=new JSONObject(response.get().toString().trim());
                    mmsg=object.getString("msg");
                    handler.sendEmptyMessage(1111);
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
        boolean mlist=false,mtitle=false,mcontent=false;
        if(sendToMan.getChildCount()==0){
            Toast.makeText(getActivity().getApplicationContext(),"通知对象人不能为空",Toast.LENGTH_SHORT).show();
        }else {
            mlist=true;
        }
        if(noticeTitle.getText().toString().trim().equals("")){
            Toast.makeText(getActivity().getApplicationContext(),"摘要不能为空",Toast.LENGTH_SHORT).show();
        }else {
            mtitle=true;
        }
        if(noticeContent.getText().toString().trim().equals("")){
            Toast.makeText(getActivity().getApplicationContext(),"正文不能为空",Toast.LENGTH_SHORT).show();
        }else {
            mcontent=true;
        }
        if(mlist&&mtitle&&mcontent){
            return true;
        }else {
            return false;
        }

    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //mLog.e("得到调用1","------>>>>>>>");
        if(requestCode==1&&resultCode==1){
            Bundle bundle=data.getBundleExtra("data");
            stuID=bundle.getString("Id");
            stuName=bundle.getString("Name");
            String[] str=stuName.split(",");
            mLog.e("stuID","---->"+stuID);
            mLog.e("stuName","---->"+stuName);
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
