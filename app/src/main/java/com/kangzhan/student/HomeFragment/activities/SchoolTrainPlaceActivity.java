package com.kangzhan.student.HomeFragment.activities;

import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;

import com.google.gson.Gson;
import com.holenzhou.pullrecyclerview.PullRecyclerView;
import com.holenzhou.pullrecyclerview.layoutmanager.XLinearLayoutManager;
import com.kangzhan.student.Debug.mLog;
import com.kangzhan.student.HomeFragment.Adapter.SchoolTrainPlaceAdapter;
import com.kangzhan.student.HomeFragment.Bean.EventMessage;
import com.kangzhan.student.HomeFragment.Bean.TrainPlace;
import com.kangzhan.student.HomeFragment.HomeInterface.HomeInterface;
import com.kangzhan.student.R;
import com.kangzhan.student.ShowUI.showMessage;
import com.kangzhan.student.Student.mutils.DividerItemDecoration;
import com.kangzhan.student.com.BaseActivity;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SchoolTrainPlaceActivity extends BaseActivity {
    private PullRecyclerView recyclerView;
    private ArrayList<String> params=new ArrayList<>();
    private ArrayList<String> values=new ArrayList<>();
    private ArrayList<TrainPlace> data=new ArrayList<>();
    private Gson gson=new Gson();
    private SchoolTrainPlaceAdapter adapter;

    //驾校ID 经纬度
    private String Id ,latitude,longitude,Msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_train_place);
        EventBus.getDefault().register(this);
        Toolbar toolbar= (Toolbar) findViewById(R.id.home_school_train_place_back);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        Id=getIntent().getStringExtra("Id");
        longitude=getIntent().getStringExtra("longit");
        latitude=getIntent().getStringExtra("latit");
        initView();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EventMessage msg){
        if(msg.getMsg().equals("train_place")){
            adapter.notifyDataSetChanged();
        }else if(msg.getMsg().equals("train_no_data")){
            showMessage.showMsg(getApplicationContext(),Msg);
        }else if(msg.getMsg().equals("train_place_error")){
            showMessage.showMsg(getApplicationContext(),"网络连接错误，请检测网络连接");
        }
    }

    private void initView() {
        recyclerView= (PullRecyclerView) findViewById(R.id.home_school_train_place_recycler);
        adapter=new SchoolTrainPlaceAdapter(this,R.layout.home_item_school_train_place_layout,data);
        recyclerView.setAdapter(adapter);
        recyclerView.setColorSchemeResources(R.color.swipe_color1,R.color.swipe_color2,R.color.swipe_color3);
        recyclerView.setLayoutManager(new XLinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST));
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
                        params.add("inst_id");
                        params.add("latitude");
                        params.add("longitude");
                        values.add(Id);
                        values.add(latitude);
                        values.add(longitude);
                        sendRequest(1,params,values, HomeInterface.TrainPlace(),"GET");
                    }
                }).start();
                recyclerView.stopRefresh();
            }

            @Override
            public void onLoadMore() {
                recyclerView.stopLoadMore();
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                params.clear();
                values.clear();
                params.add("inst_id");
                params.add("latitude");
                params.add("longitude");
                values.add(Id);
                values.add(latitude);
                values.add(longitude);
                sendRequest(1,params,values, HomeInterface.TrainPlace(),"GET");
            }
        }).start();
    }


    private void sendRequest(int what, ArrayList<String> params, ArrayList<String> values,String url,String way) {
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
                try {
                    mLog.e("reponse","-->"+response.get().toString());
                    JSONObject object=new JSONObject(response.get().toString());
                    switch (what){
                        case 1:
                            if(object.getString("code").equals("200")){
                                JSONArray array=new JSONArray(object.getString("data"));
                                if(array.length()>0){
                                    data.clear();
                                    for (int i = 0; i <array.length() ; i++) {
                                        TrainPlace place=gson.fromJson(array.getString(i),TrainPlace.class);
                                        data.add(place);
                                    }
                                    EventBus.getDefault().post(new EventMessage("train_place"));
                                }
                            }else {
                                Msg=object.getString("msg");
                                EventBus.getDefault().post(new EventMessage("train_no_data"));
                            }
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(int what, Response<JSONObject> response) {
                EventBus.getDefault().post(new EventMessage("train_place_error"));
            }

            @Override
            public void onFinish(int what) {

            }
        });

    }


    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
