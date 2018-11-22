package com.kangzhan.student.Teacher.person_data;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;

import com.google.gson.Gson;
import com.holenzhou.pullrecyclerview.PullRecyclerView;
import com.holenzhou.pullrecyclerview.layoutmanager.XLinearLayoutManager;
import com.kangzhan.student.Debug.mLog;
import com.kangzhan.student.R;
import com.kangzhan.student.ShowUI.mToast;
import com.kangzhan.student.ShowUI.showMessage;
import com.kangzhan.student.Student.mutils.DividerItemDecoration;
import com.kangzhan.student.Student.person_data_activity.MyPurseDetail;
import com.kangzhan.student.Teacher.Adapter.PurseDetailAdapter;
import com.kangzhan.student.Teacher.bean.purseDetail;
import com.kangzhan.student.com.BaseActivity;
import com.kangzhan.student.mInterface.TeacherInterface.teacher;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.JsonObjectRequest;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TeacherPurseDetailActivity extends BaseActivity {
    private PullRecyclerView recycler;
    private String mmsg;
    private PurseDetailAdapter adapter;
    private ArrayList<purseDetail> data=new ArrayList<>();
    private Gson gson;
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
                case 9999:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            recycler.stopRefresh();
                            showMessage.showMsg(TeacherPurseDetailActivity.this,"网络连接异常，请检查网络连接");
                        }
                    });
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_purse_detail);
        Toolbar toolbar= (Toolbar) findViewById(R.id.teacher_purse_detail_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar=getSupportActionBar();
        //设置返回箭头
        if(actionbar!=null){
            actionbar.setDisplayHomeAsUpEnabled(true);
        }
        gson=new Gson();
        initView();
    }

    private void initView() {
        adapter=new PurseDetailAdapter(this,R.layout.item_student_mydata_pursedetail,data);
        recycler= (PullRecyclerView) findViewById(R.id.teacher_purse_detail_recycler);
        recycler.setAdapter(adapter);
        recycler.setLayoutManager(new XLinearLayoutManager(TeacherPurseDetailActivity.this, LinearLayoutManager.VERTICAL,false));
        recycler.addItemDecoration(new DividerItemDecoration(TeacherPurseDetailActivity.this,DividerItemDecoration.VERTICAL_LIST));
        recycler.enablePullRefresh(true);       //设置可以下拉
        recycler.enableLoadMore(true);
        recycler.setOnRecyclerRefreshListener(new PullRecyclerView.OnRecyclerRefreshListener() {
            @Override
            public void onPullRefresh() {
                initData();
            }

            @Override
            public void onLoadMore() {
                recycler.stopLoadMore();
            }
        });
        recycler.postRefreshing();
    }

    private void initData() {
        Request<JSONObject> request=new JsonObjectRequest(teacher.teacherPurseDetail(), RequestMethod.GET);
        request.add("key",teacher.teacherKey(getApplicationContext()));
        getRequestQueue().add(1, request, new OnResponseListener<JSONObject>() {
            @Override
            public void onStart(int what) {

            }

            @Override
            public void onSucceed(int what, Response<JSONObject> response) {
                try {
                    JSONObject object=new JSONObject(response.get().toString());
                    mLog.e("reponse","--》"+response.get().toString());
                    mmsg=object.getString("msg");
                    if(object.getString("code").equals("200")){
                        JSONArray array=new JSONArray(object.getString("data"));
                        if(array.length()>0){
                            data.clear();
                            for (int i = 0; i <array.length() ; i++) {
                                purseDetail detail=gson.fromJson(array.getJSONObject(i).toString(),purseDetail.class);
                                data.add(detail);
                            }
                            handler.sendEmptyMessage(1111);
                        }else {
                            handler.sendEmptyMessage(2222);
                        }
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


}
