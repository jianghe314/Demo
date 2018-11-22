package com.kangzhan.student.School.Account;

import android.app.Dialog;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.holenzhou.pullrecyclerview.PullRecyclerView;
import com.holenzhou.pullrecyclerview.layoutmanager.XLinearLayoutManager;
import com.kangzhan.student.Debug.mLog;
import com.kangzhan.student.R;
import com.kangzhan.student.School.Adapter.AccountAffirmAdapter;
import com.kangzhan.student.School.Adapter.AccountMsgAdapter;
import com.kangzhan.student.School.Bean.AccountAffirm;
import com.kangzhan.student.School.Bean.AccountMsg;
import com.kangzhan.student.ShowUI.mToast;
import com.kangzhan.student.ShowUI.showMessage;
import com.kangzhan.student.com.BaseActivity;
import com.kangzhan.student.mInterface.SchoolInterface.school;
import com.kangzhan.student.utils.mCalendar.mCalender;
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

public class School_Msg_Manage extends BaseActivity implements View.OnClickListener {
    private TextView year;
    private ImageView choiceYear;
    private PullRecyclerView recycler;
    private RequestQueue requestQueue;
    private Gson gson;
    private String mmsg;
    private int mpage=1;
    private int[] mcalender= mCalender.getCalender();
    private AccountMsgAdapter adapter;
    private ArrayList<AccountMsg> data=new ArrayList<>();
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
                            recycler.stopLoadMore();
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
                case 5555:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            recycler.stopLoadMore();
                            mToast.showText(getApplicationContext(),"没有更多了！");
                        }
                    });
                    break;
                case 9999:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showMessage.showMsg(School_Msg_Manage.this,"网络连接异常，请检查网络连接");
                        }
                    });
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school__msg__manage);
        Toolbar toolbar= (Toolbar) findViewById(R.id.school_account_manage_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        requestQueue=NoHttp.newRequestQueue();
        gson=new Gson();
        initView();
        initData();
    }

    private void initView() {
        year= (TextView) findViewById(R.id.school_account_msg_year);
        choiceYear= (ImageView) findViewById(R.id.school_account_msg_sele_year);
        choiceYear.setOnClickListener(this);
        adapter=new AccountMsgAdapter(School_Msg_Manage.this,R.layout.item_school_account_msg,data);
        recycler= (PullRecyclerView) findViewById(R.id.school_accout_msg_recycler);
        year.setText(mcalender[0]+"年");
    }
    private void initData() {
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
                        params.add("export");
                        params.add("bill_year");
                        values.add(school.schoolKey(School_Msg_Manage.this));
                        values.add("1");
                        values.add(mcalender[0]+"");
                        sendRequest("GET",1,school.AccountMsg(),params,values);
                    }
                }).start();
            }

            @Override
            public void onLoadMore() {
                recycler.stopLoadMore();
                /*
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        mpage=mpage+1;
                        params.clear();
                        values.clear();
                        params.add("key");
                        params.add("export");
                        params.add("bill_year");
                        params.add("page");
                        values.add(school.schoolKey(School_Msg_Manage.this));
                        values.add("1");
                        values.add(mcalender[0]+"");
                        values.add(mpage+"");
                        sendRequest("GET",3,school.AccountMsg(),params,values);
                    }
                }).start();
                */
            }
        });
        recycler.postRefreshing();
    }

    private void sendRequest(String way, int what, String url, ArrayList<String> params, ArrayList<String> values) {
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
        requestQueue.add(what, request, new OnResponseListener<JSONObject>() {
            @Override
            public void onStart(int what) {

            }

            @Override
            public void onSucceed(int what, Response<JSONObject> response) {
                try {
                    JSONObject objcet=new JSONObject(response.get().toString());
                    mLog.e("reponse","->"+response.toString());
                    mmsg=objcet.getString("msg");
                    if(what==1){
                        if(objcet.getString("code").equals("200")){
                            JSONArray array=new JSONArray(objcet.getString("data"));
                            if(array.length()>0){
                                data.clear();
                                for (int i = 0; i < array.length(); i++) {
                                    AccountMsg test=gson.fromJson(array.getJSONObject(i).toString(),AccountMsg.class);
                                    data.add(test);
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
                    }else if (what==3){
                        if(objcet.getString("code").equals("200")){
                            JSONArray array=new JSONArray(objcet.getString("data"));
                            if(array.length()>0) {
                                for (int i = 0; i < array.length(); i++) {
                                    AccountMsg test = gson.fromJson(array.getJSONObject(i).toString(), AccountMsg.class);
                                    data.add(test);
                                }
                            }
                            handler.sendEmptyMessage(1111);
                        }else {
                            handler.sendEmptyMessage(5555);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.school_account_msg_sele_year:
                addChoiceYears(2017,2025);
                break;
            default:
                break;
        }
    }


    private void addChoiceYears(int starYear,int endYear) {
        ArrayList<String> myear=new ArrayList<>();
        for (int i = starYear; i <=endYear; i++) {
            myear.add(i+"年");
        }
        final Dialog dialog=new Dialog(School_Msg_Manage.this);
        dialog.setTitle("选择查询年份");
        View view= LayoutInflater.from(School_Msg_Manage.this).inflate(R.layout.item_simple_listview,null);
        ListView listView= (ListView) view.findViewById(R.id.item_simple_list);
        final ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,myear);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                year.setText(adapter.getItem(position));
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        params.clear();
                        values.clear();
                        params.add("key");
                        params.add("export");
                        params.add("bill_year");
                        values.add(school.schoolKey(School_Msg_Manage.this));
                        values.add("1");
                        values.add(""+Integer.valueOf(year.getText().toString().substring(0,4)));
                        sendRequest("GET",1,school.AccountMsg(),params,values);
                    }
                }).start();
                dialog.dismiss();
            }
        });
        dialog.setContentView(view);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    @Override
    protected void onDestroy() {
        requestQueue.cancelAll();
        super.onDestroy();
    }
}
