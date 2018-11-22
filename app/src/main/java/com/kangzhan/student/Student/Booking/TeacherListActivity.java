package com.kangzhan.student.Student.Booking;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.gson.Gson;
import com.holenzhou.pullrecyclerview.PullRecyclerView;
import com.holenzhou.pullrecyclerview.layoutmanager.XLinearLayoutManager;
import com.kangzhan.student.Debug.mLog;
import com.kangzhan.student.ShowUI.mToast;
import com.kangzhan.student.ShowUI.showProgress;
import com.kangzhan.student.Student.Adapter.BookingRecordAdapter;
import com.kangzhan.student.Student.Adapter.LearnHoursAdapter;
import com.kangzhan.student.Student.Adapter.TeacherListAdapter;
import com.kangzhan.student.R;
import com.kangzhan.student.Student.bean.BookingTeacher;
import com.kangzhan.student.Student.bean.MoreTeacher;
import com.kangzhan.student.Student.mutils.DividerItemDecoration;
import com.kangzhan.student.Student.person_data_activity.LearnHourActivity;
import com.kangzhan.student.com.BaseActivity;
import com.kangzhan.student.mInterface.StudentInterface.student;
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

public class TeacherListActivity extends BaseActivity {
    private TeacherListAdapter adapter;
    private PullRecyclerView recyclerView;
    private ArrayList<MoreTeacher> mdata=new ArrayList<>();
    private Gson gson;
    private String mmsg;
    private String[] location=new String[3];
    private int sum_Count,page;
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
                            recyclerView.stopRefresh();
                            adapter.notifyDataSetChanged();
                        }
                    });
                    break;
                case 2222:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            recyclerView.stopRefresh();
                            adapter.notifyDataSetChanged();
                            mToast.showText(getApplicationContext(),mmsg);
                        }
                    });
                    break;
                case 9999:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.closeProgress();
                            mToast.showText(getApplicationContext(),"加载失败，请稍后再试");
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
        setContentView(R.layout.activity_teacher_list);
        Toolbar toolbar= (Toolbar) findViewById(R.id.student_teacherList_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        Intent getData=getIntent();
        location=getData.getStringArrayExtra("Location");
        gson=new Gson();
        initView();
        initData();
    }
    private void initView() {
        for (int i = 0; i <location.length ; i++) {
            mLog.e("Location","->>"+location[0]+"--"+location[1]+"---"+location[2]);
        }
        recyclerView= (PullRecyclerView) findViewById(R.id.student_teacher_list_recycler);
        adapter=new TeacherListAdapter(TeacherListActivity.this,R.layout.item_list_booking_adapte,mdata);
        recyclerView.setAdapter(adapter);
        recyclerView.setEmptyView(LayoutInflater.from(this).inflate(R.layout.item_no_data,null));
    }

    private void initData() {
        recyclerView.setColorSchemeResources(R.color.swipe_color1,R.color.swipe_color2,R.color.swipe_color3);
        recyclerView.setLayoutManager(new XLinearLayoutManager(TeacherListActivity.this, LinearLayoutManager.VERTICAL,false));
        recyclerView.addItemDecoration(new DividerItemDecoration(TeacherListActivity.this,DividerItemDecoration.VERTICAL_LIST));
        recyclerView.enablePullRefresh(true);       //设置可以下拉
        recyclerView.enableLoadMore(true);
        recyclerView.setOnRecyclerRefreshListener(new PullRecyclerView.OnRecyclerRefreshListener() {
            @Override
            public void onPullRefresh() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        params.clear();
                        values.clear();
                        params.add("key");
                        params.add("province_id");
                        params.add("city_id");
                        params.add("county_id");
                        values.add(student.studentKey(getApplicationContext()));
                        if(location[0]!=null&&location[1]!=null){
                            values.add(location[0]);
                            values.add((location[0]).equals(location[1])?location[2]:location[1]);
                            values.add((location[0]).equals(location[1])?"":location[2]);
                        }
                        sendRequset("GET",1,student.studentMoreTeacher(),params,values);
                    }
                }).start();
                recyclerView.stopRefresh();
            }

            @Override
            public void onLoadMore() {
                recyclerView.stopLoadMore();
            }
        });
        recyclerView.postRefreshing();
    }

    private void sendRequset(String way, int what, String url, ArrayList<String> params, ArrayList<String> values) {
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
                if(what==1){
                    try {
                        JSONObject object=new JSONObject(response.get().toString());
                        mLog.e("reponse","---->"+response.get().toString());
                        mmsg=object.getString("msg");
                        if(object.getString("code").equals("200")){
                            JSONArray array=new JSONArray(object.getString("data"));
                            if(array.length()>0){
                                mdata.clear();
                                for (int i = 0; i < array.length(); i++) {
                                    MoreTeacher teacher=gson.fromJson(array.getJSONObject(i).toString(),MoreTeacher.class);
                                    mdata.add(teacher);
                                }
                                handler.sendEmptyMessage(1111);
                            }else {
                                mdata.clear();
                                handler.sendEmptyMessage(2222);
                            }
                        }else {
                            handler.sendEmptyMessage(2222);
                        }
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

}
