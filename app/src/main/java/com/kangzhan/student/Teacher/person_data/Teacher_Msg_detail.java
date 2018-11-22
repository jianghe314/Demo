package com.kangzhan.student.Teacher.person_data;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.holenzhou.pullrecyclerview.PullRecyclerView;
import com.holenzhou.pullrecyclerview.layoutmanager.XLinearLayoutManager;
import com.kangzhan.student.R;
import com.kangzhan.student.ShowUI.mToast;
import com.kangzhan.student.ShowUI.showMessage;
import com.kangzhan.student.ShowUI.showProgress;
import com.kangzhan.student.Student.mutils.DividerItemDecoration;
import com.kangzhan.student.Teacher.Adapter.MsgDetailAdapter;
import com.kangzhan.student.Teacher.bean.MsgDetail;
import com.kangzhan.student.Teacher.bean.TeacherMsg;
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

public class Teacher_Msg_detail extends BaseActivity implements View.OnClickListener{

    private TextView Date,Num,Price;
    private PullRecyclerView detail_list;
    private MsgDetailAdapter adapter;
    private ArrayList<MsgDetail> mdata=new ArrayList<>();
    private Button sure;
    private String Id;
    private String mmsg;
    private TeacherMsg Msg;
    private Gson gson;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0000:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Date.setText(Msg.getYear()+"-"+Msg.getMonth());
                            Num.setText(Msg.getSend_counts());
                            Price.setText(Msg.getAmount());
                            detail_list.stopRefresh();
                            adapter.notifyDataSetChanged();
                        }
                    });
                    break;
                case 1111:
                    runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        detail_list.stopRefresh();
                        mToast.showText(Teacher_Msg_detail.this,"服务器打盹，请稍后再试");
                    }
                });
                    break;
                case 2222:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.showProgress(Teacher_Msg_detail.this,"确认中...");
                        }
                    });
                    break;
                case 3333:
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
                            detail_list.stopRefresh();
                            showMessage.showMsg(Teacher_Msg_detail.this,"网络连接异常，请检查网络连接");
                        }
                    });
                    break;
                default:break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher__msg_detail);
        Toolbar toolbar= (Toolbar) findViewById(R.id.teacher_msg_deatil_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        Intent getIntent=getIntent();
        Id=getIntent.getStringExtra("id");
        gson=new Gson();
        initView();

    }

    private void initView() {
        Date= (TextView) findViewById(R.id.teacher_msg_detail_year_month);
        Num= (TextView) findViewById(R.id.teacher_msg_detail_Num);
        Price= (TextView) findViewById(R.id.teacher_msg_detail_total);
        sure= (Button) findViewById(R.id.teacher_msg_detail_btn);
        sure.setOnClickListener(this);
        detail_list= (PullRecyclerView) findViewById(R.id.teacher_msg_detail_listView);
        adapter=new MsgDetailAdapter(Teacher_Msg_detail.this,R.layout.item_teacher_msg_detail,mdata);
        detail_list.setAdapter(adapter);
        detail_list.setEmptyView(LayoutInflater.from(this).inflate(R.layout.item_no_data,null));
        detail_list.setLayoutManager(new XLinearLayoutManager(Teacher_Msg_detail.this, LinearLayoutManager.VERTICAL,false));
        detail_list.addItemDecoration(new DividerItemDecoration(Teacher_Msg_detail.this,DividerItemDecoration.VERTICAL_LIST));
        detail_list.setColorSchemeResources(R.color.swipe_color1,R.color.swipe_color2,R.color.swipe_color3);
        detail_list.enablePullRefresh(true);       //设置可以下拉
        detail_list.enableLoadMore(true);
        detail_list.setOnRecyclerRefreshListener(new PullRecyclerView.OnRecyclerRefreshListener() {
            // 模拟下拉刷新网络请求
            @Override
            public void onPullRefresh() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        sendRequest(teacher.teacherMsgBillDetail(),1,"id","GET");
                    }
                }).start();

            }
            // 模拟上拉加载更多网络请求
            @Override
            public void onLoadMore() {
                detail_list.stopLoadMore();
            }
        });
        detail_list.postRefreshing();
    }

    private void sendRequest(String url,int what,String params,String way) {
        RequestMethod mway=null;
        if(way.equals("POST")){
            mway=RequestMethod.POST;
        }else if(way.equals("GET")){
            mway=RequestMethod.GET;
        }
        Request<JSONObject> request=NoHttp.createJsonObjectRequest(url, mway);
        request.add("key",teacher.teacherKey(getApplicationContext()));
        request.add(params,Id);
        getRequestQueue().add(what, request, new OnResponseListener<JSONObject>() {
            @Override
            public void onStart(int what) {

            }

            @Override
            public void onSucceed(int what, Response<JSONObject> response) {
                if(what==1){
                    try {
                        JSONObject object=new JSONObject(response.get().toString());
                        if(object.getString("code").equals("200")){
                            String data=object.getString("bill");
                            JSONArray array=new JSONArray(data);
                            JSONObject obj=array.getJSONObject(0);
                            Msg=new TeacherMsg();
                            Msg.setId(obj.getString("id"));
                            Msg.setYear(obj.getString("year"));
                            Msg.setMonth(obj.getString("month"));
                            Msg.setAmount(obj.getString("amount"));
                            Msg.setSend_counts(obj.getString("send_counts"));
                            String sms=obj.getString("sms_detail");
                            JSONArray arra=new JSONArray(sms);
                            if(arra.length()>0){
                                mdata.clear();
                                for (int i = 0; i < arra.length(); i++) {
                                    MsgDetail detail=gson.fromJson(arra.getString(i).toString(),MsgDetail.class);
                                    mdata.add(detail);
                                }
                            }
                            handler.sendEmptyMessage(0000);
                        }else {
                            handler.sendEmptyMessage(1111);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else if(what==2){
                    try {
                        JSONObject object=new JSONObject(response.get().toString());
                        mmsg=object.getString("msg");
                        handler.sendEmptyMessage(3333);

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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.teacher_msg_detail_btn:
                handler.sendEmptyMessage(2222);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        sendRequest(teacher.teacherMsgConfirmBill(),2,"bill_id","POST");
                    }
                }).start();
                break;
            default:
                break;
        }
    }
}
