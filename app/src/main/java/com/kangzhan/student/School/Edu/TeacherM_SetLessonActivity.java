package com.kangzhan.student.School.Edu;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;

import com.google.gson.Gson;
import com.holenzhou.pullrecyclerview.PullRecyclerView;
import com.holenzhou.pullrecyclerview.layoutmanager.XLinearLayoutManager;
import com.kangzhan.student.R;
import com.kangzhan.student.School.Adapter.EduTeaMSetLessonAdapter;
import com.kangzhan.student.School.Bean.EduTeaMSetLesson;
import com.kangzhan.student.ShowUI.mToast;
import com.kangzhan.student.ShowUI.showMessage;
import com.kangzhan.student.ShowUI.showProgress;
import com.kangzhan.student.com.BaseActivity;
import com.kangzhan.student.mInterface.SchoolInterface.school;
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

public class TeacherM_SetLessonActivity extends BaseActivity {
    private String teacherId;
    private ArrayList<EduTeaMSetLesson> data=new ArrayList<>();
    private EduTeaMSetLessonAdapter adapter;
    private Gson gson;
    private String mmsg,lessonId;
    private PullRecyclerView recycler;
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
                            recycler.stopRefresh();
                            adapter.notifyDataSetChanged();
                        }
                    });
                    break;
                case 2222:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            recycler.stopRefresh();
                            adapter.notifyDataSetChanged();
                            mToast.showText(getApplicationContext(),mmsg);
                        }
                    });
                    break;
                case 3333:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.showProgress(TeacherM_SetLessonActivity.this,"设置中...");
                        }
                    });
                    break;
                case 4444:
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
                            showMessage.showMsg(TeacherM_SetLessonActivity.this,"网络连接异常，请检查网络连接");
                        }
                    });
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_m__set_lesson);
        Toolbar toolbar= (Toolbar) findViewById(R.id.teacher_m_setLessson_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        Intent getID=getIntent();
        teacherId=getID.getStringExtra("teacherId");
        gson=new Gson();
        initView();
    }

    private void initView() {
        adapter=new EduTeaMSetLessonAdapter(TeacherM_SetLessonActivity.this,R.layout.item_school_edu_setlesson,data);
        recycler= (PullRecyclerView) findViewById(R.id.school_Edu_teaM_setLesson_recycler);
        recycler.setAdapter(adapter);
        recycler.setEmptyView(LayoutInflater.from(this).inflate(R.layout.item_no_data,null));
        recycler.setLayoutManager(new XLinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
        recycler.setColorSchemeResources(R.color.swipe_color1,R.color.swipe_color2,R.color.swipe_color3);
        recycler.enablePullRefresh(true);       //设置可以下拉
        recycler.enableLoadMore(true);
        recycler.setOnRecyclerRefreshListener(new PullRecyclerView.OnRecyclerRefreshListener() {
            @Override
            public void onPullRefresh() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        params.clear();
                        values.clear();
                        params.add("key");
                        values.add(school.schoolKey(getApplicationContext()));
                        sendRequest("GET",1, school.EduTeaMShowLesson(),params,values);
                    }
                }).start();
            }

            @Override
            public void onLoadMore() {
                recycler.stopLoadMore();
            }
        });
        recycler.postRefreshing();

    }

    private void sendRequest(String way,int what,String url,ArrayList<String> params,ArrayList<String> values) {
        RequestMethod method=null;
        if(way.equals("POST")){
            method=RequestMethod.POST;
        }else if(way.equals("GET")){
            method=RequestMethod.GET;
        }
        Request<JSONObject> request=NoHttp.createJsonObjectRequest(url,method);
        for (int i = 0; i < params.size(); i++) {
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
                        mmsg=object.getString("msg");
                        if(object.getString("code").equals("200")){
                            JSONArray array=new JSONArray(object.getString("data"));
                            if(array.length()>0){
                                data.clear();
                                for (int i = 0; i < array.length(); i++) {
                                    EduTeaMSetLesson lesson=gson.fromJson(array.getJSONObject(i).toString(),EduTeaMSetLesson.class);
                                    data.add(lesson);
                                }
                                handler.sendEmptyMessage(1111);
                            }else {
                                data.clear();
                                handler.sendEmptyMessage(2222);
                            }
                        }else {
                            data.clear();
                            handler.sendEmptyMessage(2222);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else if(what==2){
                    try {
                        JSONObject object=new JSONObject(response.get().toString());
                        mmsg=object.getString("msg");
                        handler.sendEmptyMessage(4444);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.school_sure,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            case R.id.sure:
                for (int i = 0; i <data.size(); i++) {
                    if(data.get(i).isClick()){
                        lessonId=data.get(i).getId();
                    }
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        handler.sendEmptyMessage(3333);
                        params.clear();
                        values.clear();
                        params.add("key");
                        params.add("coach_ids");
                        params.add("subject_id");
                        values.add(school.schoolKey(getApplicationContext()));
                        values.add(teacherId);
                        values.add(lessonId);
                        sendRequest("POST",2,school.EduTeaMSetLesson(),params,values);
                    }
                }).start();
                break;
            default:
                break;
        }
        return true;
    }
}
