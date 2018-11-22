package com.kangzhan.student.Student.Train;

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

import com.google.gson.Gson;
import com.holenzhou.pullrecyclerview.PullRecyclerView;
import com.holenzhou.pullrecyclerview.layoutmanager.XLinearLayoutManager;
import com.kangzhan.student.ShowUI.mToast;
import com.kangzhan.student.ShowUI.showProgress;
import com.kangzhan.student.Student.Adapter.TrainRecordAdapter;
import com.kangzhan.student.R;
import com.kangzhan.student.Student.bean.BookingOrder;
import com.kangzhan.student.Student.bean.TrainRecord;
import com.kangzhan.student.Student.mutils.DividerItemDecoration;
import com.kangzhan.student.Student.person_data_activity.LearnHourActivity;
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

import static com.kangzhan.student.R.id.cancel_action;
import static com.kangzhan.student.R.id.default_activity_button;
import static com.kangzhan.student.R.id.train_record_price_Sum;

public class Train_recordActivity extends BaseActivity {
    private TextView record_Num,record_price_Sum;
    private PullRecyclerView recyclerView;
    private String num,sum,mmg;
    private ArrayList<TrainRecord> mdata=new ArrayList<>();
    private TrainRecordAdapter adapter;
    private Gson gson;
    private Handler handelr=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0000:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            recyclerView.postRefreshing();
                        }
                    });
                    break;
                case 1111:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            recyclerView.stopRefresh();
                            record_Num.setText(num);
                            record_price_Sum.setText(sum);
                            adapter.notifyDataSetChanged();
                        }
                    });
                    break;
                case 2222:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            recyclerView.stopRefresh();
                            mToast.showText(getApplicationContext(),mmg);
                        }
                    });
                    break;
                case 9999:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            recyclerView.stopRefresh();
                            mToast.showText(getApplicationContext(),"网络连接异常，请检查网络连接");
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
        setContentView(R.layout.activity_train_record);
        Toolbar toolbar= (Toolbar) findViewById(R.id.student_train_Record_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        gson=new Gson();
        initView();
        iniData();
    }

    private void iniData() {
        Message msg=new Message();
        msg.what=0000;
        handelr.sendMessage(msg);
        new Thread(new Runnable() {
            @Override
            public void run() {
                sendRequest();
            }
        }).start();
    }

    private void sendRequest() {
        Request<JSONObject> request= NoHttp.createJsonObjectRequest(student.studentTrainRecord(), RequestMethod.GET);
        request.add("key",student.studentKey(getApplicationContext()));
        getRequestQueue().add(1, request, new OnResponseListener<JSONObject>() {
            @Override
            public void onStart(int what) {

            }

            @Override
            public void onSucceed(int what, Response<JSONObject> response) {
                try {
                    JSONObject object=new JSONObject(response.get().toString());
                    String code=object.getString("code");
                    mmg=object.getString("msg");
                    if(code.equals("200")){
                        String data=object.getString("data");
                        sum=object.getString("totalPrice");
                        num=object.getString("totalCount");
                        JSONArray array=new JSONArray(data);
                        if(array.length()>0){
                            mdata.clear();
                            for (int i = 0; i <array.length(); i++) {
                                TrainRecord item=gson.fromJson(array.getJSONObject(i).toString(),TrainRecord.class);
                                mdata.add(item);
                            }
                        }else {
                            mdata.clear();
                        }
                        Message msg=new Message();
                        msg.what=1111;
                        handelr.sendMessage(msg);
                    }else {
                        Message msg=new Message();
                        msg.what=2222;
                        handelr.sendMessage(msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(int what, Response<JSONObject> response) {
                Message msg=new Message();
                msg.what=9999;
                handelr.sendMessage(msg);
            }

            @Override
            public void onFinish(int what) {

            }
        });
    }


    private void initView() {
        record_Num= (TextView) findViewById(R.id.trian_record_Num);
        record_price_Sum= (TextView) findViewById(train_record_price_Sum);
        recyclerView= (PullRecyclerView) findViewById(R.id.train_record_recyclerView);
        adapter=new TrainRecordAdapter(Train_recordActivity.this,R.layout.item_list_train_record_adapter,mdata);
        recyclerView.setAdapter(adapter);
        recyclerView.setEmptyView(LayoutInflater.from(this).inflate(R.layout.item_no_data,null));
        recyclerView.setColorSchemeResources(R.color.swipe_color1,R.color.swipe_color2,R.color.swipe_color3);
        recyclerView.setLayoutManager(new XLinearLayoutManager(Train_recordActivity.this, LinearLayoutManager.VERTICAL,false));
        recyclerView.enablePullRefresh(true);       //设置可以下拉
        recyclerView.enableLoadMore(true);
        recyclerView.setOnRecyclerRefreshListener(new PullRecyclerView.OnRecyclerRefreshListener() {
            @Override
            public void onPullRefresh() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        sendRequest();
                        recyclerView.enableLoadMore(true); // 当剩余还有大于0页的数据时，开启上拉加载更多
                    }
                }).start();
            }

            @Override
            public void onLoadMore() {

                recyclerView.stopLoadMore();
            }
        });
        recyclerView.postRefreshing();
    }

}
