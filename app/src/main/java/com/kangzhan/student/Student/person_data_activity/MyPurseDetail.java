package com.kangzhan.student.Student.person_data_activity;

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
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.holenzhou.pullrecyclerview.PullRecyclerView;
import com.holenzhou.pullrecyclerview.layoutmanager.XLinearLayoutManager;
import com.kangzhan.student.R;
import com.kangzhan.student.ShowUI.showProgress;
import com.kangzhan.student.Student.Adapter.MypurseDetailAdapter;
import com.kangzhan.student.Student.bean.PurseDetail;
import com.kangzhan.student.Student.mutils.DividerItemDecoration;
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

public class MyPurseDetail extends BaseActivity {
    private Toolbar toolbar;
    private PullRecyclerView recycler;
    private MypurseDetailAdapter adapter;
    private ArrayList<PurseDetail> mdata=new ArrayList<>();
    private Gson gson;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0000:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.showProgress(MyPurseDetail.this,"加载中...");
                        }
                    });
                    sendRequest();
                    break;
                case 1111:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.closeProgress();
                            recycler.setAdapter(adapter);
                        }
                    });
                    break;
                case 9999:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.closeProgress();
                            Toast.makeText(MyPurseDetail.this,"加载失败，请稍后再试",Toast.LENGTH_SHORT).show();
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
        setContentView(R.layout.activity_my_purse_detail);
        Toolbar toolbar= (Toolbar) findViewById(R.id.mypurse_detail);
        setSupportActionBar(toolbar);
        ActionBar actionbar=getSupportActionBar();
        //设置返回箭头
        if(actionbar!=null){
            actionbar.setDisplayHomeAsUpEnabled(true);
        }
        gson=new Gson();
        initView();
        refresh();
    }



    private void initView() {
        adapter=new MypurseDetailAdapter(MyPurseDetail.this,R.layout.item_student_mydata_pursedetail,mdata);
        recycler= (PullRecyclerView) findViewById(R.id.mypurse_detail_recycler);
        recycler.setAdapter(adapter);
        recycler.setEmptyView(LayoutInflater.from(this).inflate(R.layout.item_no_data,null));
        Message msg=new Message();
        msg.what=0000;
        handler.sendMessage(msg);
    }

    private void refresh() {
        recycler.setColorSchemeResources(R.color.swipe_color1,R.color.swipe_color2,R.color.swipe_color3);
        recycler.setLayoutManager(new XLinearLayoutManager(MyPurseDetail.this, LinearLayoutManager.VERTICAL,false));
        recycler.addItemDecoration(new DividerItemDecoration(MyPurseDetail.this,DividerItemDecoration.VERTICAL_LIST));
        recycler.enablePullRefresh(true);       //设置可以下拉
        recycler.enableLoadMore(true);
        recycler.setOnRecyclerRefreshListener(new PullRecyclerView.OnRecyclerRefreshListener() {
            @Override
            public void onPullRefresh() {
                //加载网络数据
                sendRequest();
                recycler.stopRefresh();
            }

            @Override
            public void onLoadMore() {
                recycler.stopLoadMore();
            }
        });
        recycler.postRefreshing();
    }

    private void sendRequest() {
        Request<JSONObject> request=NoHttp.createJsonObjectRequest(student.studentMyPurseDetail(), RequestMethod.GET);
        request.add("key",student.studentKey(MyPurseDetail.this));
        getRequestQueue().add(1, request, new OnResponseListener<JSONObject>() {
            @Override
            public void onStart(int what) {

            }

            @Override
            public void onSucceed(int what, Response<JSONObject> response) {
                try {
                    JSONObject object=new JSONObject(response.get().toString());
                    String code=object.getString("code");
                    String data=object.getString("data");
                    if(code.equals("200")){
                        JSONArray array=new JSONArray(data);
                        if(array.length()>0){
                            mdata.clear();
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject obj=array.getJSONObject(i);
                                PurseDetail detail=gson.fromJson(obj.toString(),PurseDetail.class);
                                mdata.add(detail);
                            }
                            adapter.notifyDataSetChanged();
                            Message msg=new Message();
                            msg.what=1111;
                            handler.sendMessage(msg);
                        }
                    }else {
                        Message msg=new Message();
                        msg.what=9999;
                        handler.sendMessage(msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(int what, Response<JSONObject> response) {
                Message msg=new Message();
                msg.what=9999;
                handler.sendMessage(msg);
            }

            @Override
            public void onFinish(int what) {

            }
        });
    }

}
