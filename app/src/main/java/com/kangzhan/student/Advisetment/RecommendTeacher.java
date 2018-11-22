package com.kangzhan.student.Advisetment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;

import com.google.gson.Gson;
import com.holenzhou.pullrecyclerview.PullRecyclerView;
import com.holenzhou.pullrecyclerview.layoutmanager.XLinearLayoutManager;
import com.kangzhan.student.Adapter.RecommendTeacherAdapter;
import com.kangzhan.student.Debug.mLog;
import com.kangzhan.student.R;
import com.kangzhan.student.ShowUI.mToast;
import com.kangzhan.student.ShowUI.showMessage;
import com.kangzhan.student.Student.mutils.DividerItemDecoration;
import com.kangzhan.student.com.BaseActivity;
import com.kangzhan.student.mInterface.AdvisetInterface.Adviset;
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

public class RecommendTeacher extends BaseActivity {
    private PullRecyclerView recyclerView;
    private String Id;
    private ArrayList<com.kangzhan.student.RecommendBean.RecommendTeacher> mdata=new ArrayList<>();
    private RecommendTeacherAdapter adapter;
    private int total,lastPage,current_page,i=1;
    private Gson gson;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1111:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            recyclerView.stopRefresh();
                            recyclerView.stopLoadMore();
                            adapter.notifyDataSetChanged();
                        }
                    });
                    break;
                case 2222:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            recyclerView.stopRefresh();
                            recyclerView.stopLoadMore();
                            adapter.notifyDataSetChanged();
                            mToast.showText(getApplicationContext(),"没有更多了");
                        }
                    });
                    break;
                case 3333:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            recyclerView.stopRefresh();
                            recyclerView.stopLoadMore();
                            adapter.notifyDataSetChanged();
                            mToast.showText(getApplicationContext(),"当前数据没有数据");
                        }
                    });
                    break;
                case 9999:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            recyclerView.stopRefresh();
                            recyclerView.stopLoadMore();
                            showMessage.showMsg(RecommendTeacher.this,"网络连接异常，请检测网络连接");
                        }
                    });
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend_teacher);
        Toolbar toolbar= (Toolbar) findViewById(R.id.recommend_school_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        gson=new Gson();
        Intent teacher=getIntent();
        Id=teacher.getStringExtra("id");
        initView();
    }

    private void initView() {
        recyclerView= (PullRecyclerView) findViewById(R.id.recommend_teacher);
        adapter=new RecommendTeacherAdapter(RecommendTeacher.this,R.layout.item_recommend_teacher,mdata);
        recyclerView.setAdapter(adapter);
        recyclerView.setEmptyView(LayoutInflater.from(this).inflate(R.layout.item_no_data,null));
        recyclerView.setLayoutManager(new XLinearLayoutManager(RecommendTeacher.this,LinearLayoutManager.VERTICAL,false));
        recyclerView.addItemDecoration(new DividerItemDecoration(RecommendTeacher.this,DividerItemDecoration.VERTICAL_LIST));
        recyclerView.setColorSchemeResources(R.color.swipe_color1,R.color.swipe_color2,R.color.swipe_color3);
        recyclerView.enablePullRefresh(true);       //设置可以下拉
        recyclerView.enableLoadMore(true);
        recyclerView.setOnRecyclerRefreshListener(new PullRecyclerView.OnRecyclerRefreshListener() {
            // 模拟下拉刷新网络请求
            @Override
            public void onPullRefresh() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        sendRequest(1,1);
                    }
                }).start();

            }
            // 模拟上拉加载更多网络请求
            @Override
            public void onLoadMore() {
                if(total>=1){
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            if(i<=total){
                                i++;
                                sendRequest(2,i);
                            }
                        }
                    }).start();
                }else {
                    recyclerView.stopLoadMore();
                }

            }
        });
        recyclerView.postRefreshing();
    }

    private void sendRequest(int what,int page) {
        Request<JSONObject> request=NoHttp.createJsonObjectRequest(Adviset.SchoolRecommend(), RequestMethod.GET);
        request.add("id",Id);
        request.add("page",page);
        getRequestQueue().add(what, request, new OnResponseListener<JSONObject>() {
            @Override
            public void onStart(int what) {

            }

            @Override
            public void onSucceed(int what, Response<JSONObject> response) {
                try {
                    JSONObject object=new JSONObject(response.get().toString());
                    mLog.e("reponse","-->"+response.get().toString());
                    total=object.getInt("total");
                    lastPage=object.getInt("lastPage");
                    current_page=object.getInt("current_page");
                    String data=object.getString("data");
                    if(what==1){
                        JSONArray array=new JSONArray(data);
                        if(array.length()>0){
                            mdata.clear();
                            for (int i = 0; i <array.length(); i++) {
                                com.kangzhan.student.RecommendBean.RecommendTeacher item=gson.fromJson(array.getJSONObject(i).toString(), com.kangzhan.student.RecommendBean.RecommendTeacher.class);
                                mdata.add(item);
                            }
                            handler.sendEmptyMessage(1111);
                        }else {
                            handler.sendEmptyMessage(3333);
                        }
                    }else if(what==2){
                        JSONArray array=new JSONArray(data);
                        if(array.length()>0){
                            for (int i = 0; i <array.length(); i++) {
                                com.kangzhan.student.RecommendBean.RecommendTeacher item=gson.fromJson(array.getJSONObject(i).toString(), com.kangzhan.student.RecommendBean.RecommendTeacher.class);
                                mdata.add(item);
                            }
                            handler.sendEmptyMessage(1111);
                        }else {
                            handler.sendEmptyMessage(2222);
                        }
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
