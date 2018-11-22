package com.kangzhan.student.School.Account;

import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.holenzhou.pullrecyclerview.PullRecyclerView;
import com.holenzhou.pullrecyclerview.layoutmanager.XLinearLayoutManager;
import com.kangzhan.student.Debug.mLog;
import com.kangzhan.student.R;
import com.kangzhan.student.School.Adapter.AccountMsgAdapter;
import com.kangzhan.student.School.Adapter.AccountRewardAdapter;
import com.kangzhan.student.School.Bean.AccountMsg;
import com.kangzhan.student.School.Bean.AccountRewardRecord;
import com.kangzhan.student.ShowUI.mToast;
import com.kangzhan.student.ShowUI.showMessage;
import com.kangzhan.student.com.BaseActivity;
import com.kangzhan.student.mInterface.SchoolInterface.school;
import com.kangzhan.student.utils.mCalendar.mCalender;
import com.kangzhan.student.utils.picker.DatePicker;
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

import static com.kangzhan.student.utils.framework.popup.BasicPopup.MATCH_PARENT;
import static com.kangzhan.student.utils.framework.popup.BasicPopup.WRAP_CONTENT;

public class School_reward_record extends BaseActivity implements View.OnClickListener{
    private TextView myear;
    private ImageView choiceYear;
    private PullRecyclerView recycler;
    private RequestQueue requestQueue;
    private Gson gson;
    private String mmsg;
    private int mpage=1;
    private int[] mcalender= mCalender.getCalender();
    private AccountRewardAdapter adapter;
    private ArrayList<AccountRewardRecord> data=new ArrayList<>();
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
                            showMessage.showMsg(School_reward_record.this,"网络连接异常，请检查网络连接");
                        }
                    });
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_reward_record);
        Toolbar toolbar= (Toolbar) findViewById(R.id.school_account_reward_toolbar);
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
        myear= (TextView) findViewById(R.id.school_account_reward_year);
        choiceYear= (ImageView) findViewById(R.id.school_account_reward_sele_year);
        choiceYear.setOnClickListener(this);
        recycler= (PullRecyclerView) findViewById(R.id.school_account_reward_recycler);
        adapter=new AccountRewardAdapter(School_reward_record.this,R.layout.item_school_account_reward,data);
        myear.setText(mcalender[0]+"年"+mcalender[1]+"月");
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
                        params.add("bonus_mouth");
                        values.add(school.schoolKey(School_reward_record.this));
                        values.add("1");
                        values.add(mcalender[0]+"-"+mcalender[1]);
                        sendRequest("GET",1,school.AccountRewardManage(),params,values);
                    }
                }).start();
            }

            @Override
            public void onLoadMore() {
               new Thread(new Runnable() {
                   @Override
                   public void run() {
                       mpage=mpage+1;
                       params.clear();
                       values.clear();
                       params.add("key");
                       params.add("export");
                       params.add("bonus_mouth");
                       params.add("page");
                       values.add(school.schoolKey(School_reward_record.this));
                       values.add("1");
                       values.add(mcalender[0]+"-"+mcalender[1]);
                       values.add(mpage+"");
                       sendRequest("GET",3,school.AccountRewardManage(),params,values);
                   }
               }).start();
            }
        });
        recycler.postRefreshing();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.school_account_reward_sele_year:
                DatePicker picker = new DatePicker(this, DatePicker.YEAR_MONTH);
                picker.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL);
                picker.setWidth((int) (picker.getScreenWidthPixels() * 0.6));
                picker.setRangeStart(2016, 1, 01);
                picker.setRangeEnd(2025, 12, 31);
                picker.setSelectedItem(mcalender[0],mcalender[1]);
                picker.setOnDatePickListener(new DatePicker.OnYearMonthPickListener() {
                    @Override
                    public void onDatePicked(final String year, final String month) {
                        myear.setText(year+"年"+month+"月");
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                params.clear();
                                values.clear();
                                params.add("key");
                                params.add("export");
                                params.add("bonus_mouth");
                                values.add(school.schoolKey(School_reward_record.this));
                                values.add("1");
                                values.add(year+"-"+month);
                                sendRequest("GET",1,school.AccountRewardManage(),params,values);
                            }
                        }).start();
                    }
                });
                picker.show();
                break;
            default:
                break;
        }
    }



    private void sendRequest( String way, int what, String url, ArrayList<String> params, ArrayList<String> values) {
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
                                    AccountRewardRecord test=gson.fromJson(array.getJSONObject(i).toString(),AccountRewardRecord.class);
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
                    }else if(what==3){
                        if(objcet.getString("code").equals("200")){
                            JSONArray array=new JSONArray(objcet.getString("data"));
                            if(array.length()>0) {
                                for (int i = 0; i < array.length(); i++) {
                                    AccountRewardRecord test = gson.fromJson(array.getJSONObject(i).toString(), AccountRewardRecord.class);
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
    protected void onDestroy() {
        requestQueue.cancelAll();
        super.onDestroy();
    }
}
