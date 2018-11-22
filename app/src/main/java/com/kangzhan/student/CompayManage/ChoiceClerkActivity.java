package com.kangzhan.student.CompayManage;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.holenzhou.pullrecyclerview.PullRecyclerView;
import com.holenzhou.pullrecyclerview.layoutmanager.XLinearLayoutManager;
import com.kangzhan.student.CompayManage.Adapter.ChoiceClerkAdapter;
import com.kangzhan.student.CompayManage.Bean.ChoiceClerk;
import com.kangzhan.student.CompayManage.Bean.SelfRegStuM;
import com.kangzhan.student.Debug.mLog;
import com.kangzhan.student.R;
import com.kangzhan.student.ShowUI.mToast;
import com.kangzhan.student.ShowUI.showMessage;
import com.kangzhan.student.ShowUI.showProgress;
import com.kangzhan.student.com.BaseActivity;
import com.kangzhan.student.mInterface.CompayInterface.CompayInterface;
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

public class ChoiceClerkActivity extends BaseActivity implements View.OnClickListener {
    private PullRecyclerView recycler;
    private EditText searchContent;
    private TextView doSearch;
    private ChoiceClerkAdapter adapter;
    private String mmsg,Id="",clerkId="",who="";
    private Gson gson;
    private Button btn;
    private ArrayList<ChoiceClerk> mdata=new ArrayList<>();
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
                            recycler.stopLoadMore();
                            adapter.notifyDataSetChanged();
                            mToast.showText(getApplicationContext(),mmsg);

                        }
                    });
                    break;
                case 3333:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.showProgress(ChoiceClerkActivity.this,"分配中...");
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
                            showProgress.closeProgress();
                            recycler.stopRefresh();
                            showMessage.showMsg(ChoiceClerkActivity.this,"网络连接异常，请检查网络连接");
                        }
                    });
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice_clerk);
        Toolbar toolbar= (Toolbar) findViewById(R.id.compay_choice_clerk_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        Intent getData=getIntent();
        Id=getData.getStringExtra("Id");
        who=getData.getStringExtra("who");
        gson=new Gson();
        initView();
        initData();
    }



    private void initView() {
        btn= (Button) findViewById(R.id.choice_clerk_btn);
        btn.setOnClickListener(this);
        searchContent= (EditText) findViewById(R.id.choice_clerk_search_content);
        doSearch= (TextView) findViewById(R.id.choice_clerk_Tosearch);
        recycler= (PullRecyclerView) findViewById(R.id.choice_clerk_recycler);
        doSearch.setOnClickListener(this);
    }

    private void initData() {
        adapter=new ChoiceClerkAdapter(ChoiceClerkActivity.this,R.layout.item_compay_choice_clerk_recycler,mdata);
        recycler.setAdapter(adapter);
        recycler.setEmptyView(LayoutInflater.from(this).inflate(R.layout.item_no_data,null));
        recycler.setLayoutManager(new XLinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
        recycler.setColorSchemeResources(R.color.swipe_color1,R.color.swipe_color2,R.color.swipe_color3);
        recycler.enablePullRefresh(true);
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
                        params.add("searchCriteria");
                        values.add(CompayInterface.CompayKey(getApplicationContext()));
                        values.add(searchContent.getText().toString().trim());
                        sendRequest("GET",CompayInterface.ChoiceClerk(),1,params,values);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.choice_clerk_Tosearch:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        params.clear();
                        values.clear();
                        params.add("key");
                        params.add("searchCriteria");
                        values.add(CompayInterface.CompayKey(getApplicationContext()));
                        values.add(searchContent.getText().toString().trim());
                        sendRequest("GET",CompayInterface.ChoiceClerk(),1,params,values);
                    }
                }).start();
                break;
            case R.id.choice_clerk_btn:
                for (int i = 0; i < mdata.size(); i++) {
                    if(mdata.get(i).isClick()){
                        clerkId=mdata.get(i).getId();
                    }
                }
                if(clerkId.equals("")){
                    mToast.showText(getApplicationContext(),"没有选择业务员");
                }else {
                    handler.sendEmptyMessage(3333);
                    if(who.equals("student")||who.equals("teacher")){
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                params.clear();
                                values.clear();
                                params.add("key");
                                params.add("type");
                                params.add("clerk_id");
                                params.add("ids");
                                values.add(CompayInterface.CompayKey(getApplicationContext()));
                                values.add(who.equals("student")?"1":"2");
                                values.add(clerkId);
                                values.add(Id);
                                sendRequest("GET",CompayInterface.AllocateClerk(),2,params,values);
                            }
                        }).start();
                    }else if(who.equals("school")){
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                params.clear();
                                values.clear();
                                params.add("key");
                                params.add("inst_id");
                                params.add("clerk_id");
                                values.add(CompayInterface.CompayKey(getApplicationContext()));
                                values.add(clerkId);
                                values.add(Id);
                                sendRequest("GET",CompayInterface.InfoSchoolAlloca(),2,params,values);
                            }
                        }).start();
                    }

                }
                break;
        }
    }



    private void sendRequest(String Way,String url,int what,ArrayList<String> params,ArrayList<String> values) {
        RequestMethod method=null;
        if(Way.equals("POST")){
            method=RequestMethod.POST;
        }else if(Way.equals("GET")){
            method=RequestMethod.GET;
        }
        Request<JSONObject> request=NoHttp.createJsonObjectRequest(url,method);
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
                    JSONObject object=new JSONObject(response.get().toString());
                    mLog.e("reponse","-->"+response.get().toString());
                    mmsg=object.getString("msg");
                    if(what==1){
                        if(object.getString("code").equals("200")){
                            JSONArray array=new JSONArray(object.getString("data"));
                            if(array.length()>0){
                                mdata.clear();
                                for (int i = 0; i < array.length(); i++) {
                                    ChoiceClerk clerk=gson.fromJson(array.getJSONObject(i).toString(),ChoiceClerk.class);
                                    mdata.add(clerk);
                                }
                                handler.sendEmptyMessage(1111);
                            }else {
                                mdata.clear();
                                handler.sendEmptyMessage(2222);
                            }

                        }else {
                            mdata.clear();
                            handler.sendEmptyMessage(2222);
                        }
                    }else if(what==2){
                        handler.sendEmptyMessage(4444);
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
