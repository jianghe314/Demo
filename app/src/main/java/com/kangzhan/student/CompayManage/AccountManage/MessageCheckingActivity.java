package com.kangzhan.student.CompayManage.AccountManage;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.holenzhou.pullrecyclerview.PullRecyclerView;
import com.holenzhou.pullrecyclerview.layoutmanager.XLinearLayoutManager;
import com.kangzhan.student.CompayManage.Adapter.AccountSchoolListAdapter;
import com.kangzhan.student.CompayManage.Bean.AccountSchoolList;
import com.kangzhan.student.Debug.mLog;
import com.kangzhan.student.R;
import com.kangzhan.student.School.Bean.AccountAffirm;
import com.kangzhan.student.ShowUI.mToast;
import com.kangzhan.student.ShowUI.showMessage;
import com.kangzhan.student.ShowUI.showProgress;
import com.kangzhan.student.com.BaseActivity;
import com.kangzhan.student.entity.City;
import com.kangzhan.student.entity.County;
import com.kangzhan.student.entity.Province;
import com.kangzhan.student.mInterface.CompayInterface.CompayInterface;
import com.kangzhan.student.utils.AddressPickTask;
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

public class MessageCheckingActivity extends BaseActivity implements View.OnClickListener {
    private TextView content,doSearch;
    private PullRecyclerView recycler;
    private Button btn;
    private String mmsg,mprovince,mcity,mcounty;
    private Gson gson;
    private AccountSchoolListAdapter adapter;
    private ArrayList<String> params=new ArrayList<>();
    private ArrayList<String> values=new ArrayList<>();
    private ArrayList<AccountSchoolList> mdata=new ArrayList<>();
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
                            showProgress.showProgress(MessageCheckingActivity.this,"删除中...");
                        }
                    });
                    break;
                case 4444:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.closeProgress();
                            initData();
                            mToast.showText(getApplicationContext(),mmsg);
                        }
                    });
                    break;
                case 9999:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            recycler.stopRefresh();
                            showMessage.showMsg(MessageCheckingActivity.this,"网络连接异常，请检查网络连接");
                        }
                    });
                    break;
            }
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_checking);
        Toolbar toolbar= (Toolbar) findViewById(R.id.compay_account_msg_checking_toobar);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        gson=new Gson();
        initView();
    }

    private void initView() {
        content= (TextView) findViewById(R.id.compay_account_msg_checking_address);
        doSearch= (TextView) findViewById(R.id.compay_account_msg_checking_choice);
        recycler= (PullRecyclerView) findViewById(R.id.compay_account_msg_checking_recycler);
        btn= (Button) findViewById(R.id.compay_account_msg_checking_btn);
        doSearch.setOnClickListener(this);
        btn.setOnClickListener(this);
        content.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.compay_account_msg_checking_address:
                AddressPickTask task=new AddressPickTask(this);
                task.setHideCounty(false);
                task.setHideProvince(false);
                task.setCallback(new AddressPickTask.Callback() {
                    @Override
                    public void onAddressInitFailed() {
                        mToast.showText(getApplicationContext(),"数据初始化失败");
                    }

                    @Override
                    public void onAddressPicked(Province province, City city, County county) {
                        if (county == null) {
                            //看这里可以改不，，，，，
                            // showToast(province.getAreaName() + city.getAreaName());
                            String str=(province.getAreaName()+(province.getAreaName().equals(city.getAreaName())?"":city.getAreaName()));
                            content.setText(str);

                        } else {
                            //showToast(province.getAreaName() + city.getAreaName() + county.getAreaName());
                            String str=(province.getAreaName()+(province.getAreaName().equals(city.getAreaName())?"":city.getAreaName()));
                            content.setText(str);
                            mprovince=province.getAreaId();
                            mcity=city.getAreaId();
                            mcounty=county.getAreaId();

                            mLog.e("PCC","p->"+mprovince+":city->"+city.getAreaId()+":mcounty->"+mcounty);
                        }
                    }
                });
                task.execute("北京市", "北京市", "东城区");
                break;
            case R.id.compay_account_msg_checking_choice:
                initData();
                break;
            case R.id.compay_account_msg_checking_btn:
                String id="";
                for (int i = 0; i <mdata.size() ; i++) {
                    if(mdata.get(i).isClick()){
                        id=mdata.get(i).getId();
                    }
                }
                if(id.length()>0){
                    Intent detail=new Intent(this,MessageDetailActivity.class);
                    detail.putExtra("Id",id);
                    startActivity(detail);
                }else {
                    mToast.showText(getApplicationContext(),"请选择对象！");
                }
                break;

        }
    }


    private void initData() {
        adapter=new AccountSchoolListAdapter(MessageCheckingActivity.this,R.layout.item_compay_account_school_recycler,mdata);
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
                        params.add("type");
                        params.add("province_id");
                        params.add("city_id");
                        values.add(CompayInterface.CompayKey(MessageCheckingActivity.this));
                        values.add("2");
                        values.add(mprovince);
                        values.add(mcity);
                        sendRequest("GET",CompayInterface.AccountMsgCheck(),1,params,values);
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



    private void sendRequest(String Way,String url,int what,ArrayList<String> params,ArrayList<String> values) {
        RequestMethod method=null;
        if(Way.equals("POST")){
            method=RequestMethod.POST;
        }else if(Way.equals("GET")){
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
                    JSONObject object=new JSONObject(response.get().toString());
                    Log.e("reponse","---->"+response.get().toString());
                    mmsg=object.getString("msg");
                    if(what==1){
                        if(object.getString("code").equals("200")){
                            JSONArray array=new JSONArray(object.getString("data"));
                            if(array.length()>0){
                                mdata.clear();
                                for (int i = 0; i < array.length(); i++) {
                                    AccountSchoolList info=gson.fromJson(array.getJSONObject(i).toString(),AccountSchoolList.class);
                                    mdata.add(info);
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
