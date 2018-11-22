package com.kangzhan.student.HomeFragment.activities;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;

import com.google.gson.Gson;
import com.holenzhou.pullrecyclerview.PullRecyclerView;
import com.holenzhou.pullrecyclerview.layoutmanager.XLinearLayoutManager;
import com.kangzhan.student.Debug.mLog;
import com.kangzhan.student.HomeFragment.Adapter.MoreQuesAdapter;
import com.kangzhan.student.HomeFragment.Adapter.MoreRemarkAdapter;
import com.kangzhan.student.HomeFragment.Bean.EventMessage;
import com.kangzhan.student.HomeFragment.Bean.SchoolDetail_Que;
import com.kangzhan.student.HomeFragment.Bean.SchoolDetail_Remark;
import com.kangzhan.student.HomeFragment.HomeInterface.HomeInterface;
import com.kangzhan.student.HomeFragment.mDialog.ErrorDialog;
import com.kangzhan.student.HomeFragment.mDialog.WaitDialog;
import com.kangzhan.student.R;
import com.kangzhan.student.ShowUI.mToast;
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

public class HomeQuesMoreActivity extends BaseActivity {

    private String Id,Msg;
    private PullRecyclerView recyclerView;
    private int page=1;
    private ArrayList<String> params=new ArrayList<>();
    private ArrayList<String> values=new ArrayList<>();
    private ArrayList<SchoolDetail_Que> quesData=new ArrayList<>();
    private Gson gson=new Gson();
    private MoreQuesAdapter adapter;
    private WaitDialog waitDialog;
    private ErrorDialog errorDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_ques_more);
        EventBus.getDefault().register(this);
        Toolbar toolbar= (Toolbar) findViewById(R.id.home_ques_more_back);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        Id=getIntent().getStringExtra("Id");
        initView();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EventMessage msg){
        waitDialog.dismiss();
        if(msg.getMsg().equals("ques_more")){
            adapter.notifyDataSetChanged();
        }else if(msg.getMsg().equals("ques_more_no")){
            mToast.showText(getApplicationContext(),Msg);
        }else if(msg.getMsg().equals("ques_more_error")){
            errorDialog.show();
            errorDialog.setTextMsg("网络连接不上，请检查网络连接");
        }
    }



    private void initView() {
        waitDialog=new WaitDialog(this);
        errorDialog=new ErrorDialog(this);
        adapter=new MoreQuesAdapter(this,R.layout.home_school_question_list_layout,quesData);
        recyclerView= (PullRecyclerView) findViewById(R.id.home_ques_more_recycler);
        recyclerView.setAdapter(adapter);
        recyclerView.setColorSchemeResources(R.color.swipe_color1,R.color.swipe_color2,R.color.swipe_color3);
        recyclerView.setLayoutManager(new XLinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST));
        recyclerView.enablePullRefresh(true);       //设置可以下拉
        recyclerView.enableLoadMore(true);
        recyclerView.setOnRecyclerRefreshListener(new PullRecyclerView.OnRecyclerRefreshListener() {
            @Override
            public void onPullRefresh() {
                loadData("",1);
                recyclerView.stopRefresh();
            }

            @Override
            public void onLoadMore() {
                page++;
                loadData(String.valueOf(page),2);
                recyclerView.stopLoadMore();
            }
        });
        loadData("",1);
    }

    private void loadData(final String page, final int what) {
        waitDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                params.clear();
                values.clear();
                params.add("inst_id");
                params.add("page");
                values.add(Id);
                values.add(page);
                sendRequest(what,params,values, HomeInterface.schoolDetail_Ques(),"GET");
            }
        }).start();
    }

    private void sendRequest(int what, ArrayList<String> params, ArrayList<String> values, String url, String way) {
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
                                    quesData.clear();
                                    for (int i = 0; i <array.length() ; i++) {
                                        SchoolDetail_Que data=gson.fromJson(array.getString(i),SchoolDetail_Que.class);
                                        quesData.add(data);
                                    }
                                    EventBus.getDefault().post(new EventMessage("ques_more"));
                                }
                            }else {
                                Msg=object.getString("msg");
                                EventBus.getDefault().post(new EventMessage("ques_more_no"));
                            }
                            break;
                        case 2:
                            if(object.getString("code").equals("200")){
                                JSONArray array=new JSONArray(object.getString("data"));
                                if(array.length()>0){
                                    for (int i = 0; i <array.length() ; i++) {
                                        SchoolDetail_Que data=gson.fromJson(array.getString(i),SchoolDetail_Que.class);
                                        quesData.add(data);
                                    }
                                    EventBus.getDefault().post(new EventMessage("ques_more"));
                                }
                            }else {
                                Msg="没有更多了";
                                EventBus.getDefault().post(new EventMessage("ques_more_no"));
                            }
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(int what, Response<JSONObject> response) {
                EventBus.getDefault().post(new EventMessage("ques_more_error"));
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
