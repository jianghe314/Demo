package com.kangzhan.student.Student.person_data_activity;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.holenzhou.pullrecyclerview.PullRecyclerView;
import com.holenzhou.pullrecyclerview.layoutmanager.XLinearLayoutManager;
import com.kangzhan.student.Debug.mLog;
import com.kangzhan.student.ShowUI.showMessage;
import com.kangzhan.student.ShowUI.showProgress;
import com.kangzhan.student.Student.Adapter.LearnHoursAdapter;
import com.kangzhan.student.Student.Adapter.TrainRecordAdapter;
import com.kangzhan.student.R;
import com.kangzhan.student.Student.bean.LearnHours;
import com.kangzhan.student.Student.bean.TrainRecord;
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

public class LearnHourActivity extends BaseActivity {
    private TextView learn_Num,learn_spend;
    private PullRecyclerView recycler;
    private LearnHoursAdapter adapter;
    private ArrayList<LearnHours> mdata=new ArrayList<>();
    private Gson gson;
    private String count,sum_cout,mmsg;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0000:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.showProgress(LearnHourActivity.this,"加载中...");
                        }
                    });
                    break;
                case 1111:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.closeProgress();
                            learn_Num.setText(count);
                            learn_spend.setText(sum_cout);
                            adapter.notifyDataSetChanged();
                        }
                    });
                    break;
                case 2222:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.closeProgress();
                            adapter.notifyDataSetChanged();
                            Toast.makeText(getApplicationContext(),mmsg,Toast.LENGTH_SHORT).show();
                        }
                    });
                    break;
                case 9999:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.closeProgress();
                            showMessage.showMsg(LearnHourActivity.this,"网络连接异常，请检查网络连接");
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
        setContentView(R.layout.activity_learn_hour);
        Toolbar toolbar= (Toolbar) findViewById(R.id.learn_hour_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        gson=new Gson();
        initView();
    }

    private void initView() {
        learn_Num= (TextView) findViewById(R.id.learn_hour_record_number);
        learn_spend= (TextView) findViewById(R.id.learn_hour_record_spend);
        recycler= (PullRecyclerView) findViewById(R.id.learn_hour_recyclerView);
        adapter=new LearnHoursAdapter(LearnHourActivity.this,R.layout.item_list_learnhour_adapter,mdata);
        recycler.setAdapter(adapter);
        recycler.setEmptyView(LayoutInflater.from(this).inflate(R.layout.item_no_data,null));
        recycler.setColorSchemeResources(R.color.swipe_color1,R.color.swipe_color2,R.color.swipe_color3);
        recycler.setLayoutManager(new XLinearLayoutManager(LearnHourActivity.this, LinearLayoutManager.VERTICAL,false));
        recycler.enablePullRefresh(true);       //设置可以下拉
        recycler.enableLoadMore(true);
        recycler.setOnRecyclerRefreshListener(new PullRecyclerView.OnRecyclerRefreshListener() {
            @Override
            public void onPullRefresh() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        sendRequest();
                    }
                }).start();
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
        Request<JSONObject> request=NoHttp.createJsonObjectRequest(student.studentLearnHour(), RequestMethod.GET);
        request.add("key",student.studentKey(getApplicationContext()));
        getRequestQueue().add(1, request, new OnResponseListener<JSONObject>() {
            @Override
            public void onStart(int what) {

            }

            @Override
            public void onSucceed(int what, Response<JSONObject> response) {
                mLog.e("response","->"+response.get().toString());
                try {
                    JSONObject objet=new JSONObject(response.get().toString());
                    String code=objet.getString("code");
                    mmsg=objet.getString("msg");
                    if(code.equals("200")){
                        count=objet.getString("count");
                        sum_cout=objet.getString("totalAmout");
                        String data=objet.getString("data");
                        JSONArray array=new JSONArray(data);
                        if(array.length()>0){
                            mdata.clear();
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject obj=array.getJSONObject(i);
                                LearnHours lh=gson.fromJson(obj.toString(),LearnHours.class);
                                mdata.add(lh);
                            }
                            adapter.notifyDataSetChanged();
                        }else {
                            mdata.clear();
                        }
                        handler.sendEmptyMessage(1111);
                    }else {
                        mdata.clear();
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
