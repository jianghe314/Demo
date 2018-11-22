package com.kangzhan.student.Teacher.Train;

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

import com.google.gson.Gson;
import com.holenzhou.pullrecyclerview.PullRecyclerView;
import com.holenzhou.pullrecyclerview.layoutmanager.XLinearLayoutManager;
import com.kangzhan.student.Debug.mLog;
import com.kangzhan.student.R;
import com.kangzhan.student.ShowUI.mToast;
import com.kangzhan.student.ShowUI.showMessage;
import com.kangzhan.student.Teacher.Adapter.TeacherTrainGetOrderAdapter;
import com.kangzhan.student.Teacher.Adapter.TeacherTrainOrderAdapter;
import com.kangzhan.student.Teacher.bean.TeacherTrainOrder;
import com.kangzhan.student.com.BaseActivity;
import com.kangzhan.student.mInterface.TeacherInterface.teacher;
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

import static com.yanzhenjie.nohttp.NoHttp.getContext;

public class Teacher_TrainOrderActivity extends BaseActivity {
    private PullRecyclerView recyclerView;
    private TeacherTrainOrderAdapter adapter;
    private ArrayList<TeacherTrainOrder> mdata=new ArrayList<>();
    private String mmsg;
    private Gson gson;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0000:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                        }
                    });
                    break;
                case 1111:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            recyclerView.stopRefresh();
                            adapter.notifyDataSetChanged();
                        }
                    });
                    break;
                case 2222:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            recyclerView.stopRefresh();
                            adapter.notifyDataSetChanged();
                            mToast.showText(getApplicationContext(),mmsg);
                        }
                    });
                    break;
                case 3333:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            recyclerView.stopRefresh();
                            adapter.notifyDataSetChanged();
                            mToast.showText(getApplicationContext(),mmsg);
                        }
                    });
                    break;
                case 9999:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            recyclerView.stopRefresh();
                            adapter.notifyDataSetChanged();
                            showMessage.showMsg(Teacher_TrainOrderActivity.this,"网络连接异常，请检测网络");
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
        setContentView(R.layout.activity_teacher__train_order);
        Toolbar toolbar= (Toolbar) findViewById(R.id.teacher_order_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        gson=new Gson();
        iniView();
    }

    private void iniView() {
        adapter=new TeacherTrainOrderAdapter(Teacher_TrainOrderActivity.this,R.layout.item_teacher_train_order,mdata,Teacher_TrainOrderActivity.this);
        recyclerView= (PullRecyclerView) findViewById(R.id.teacher_train_order_recycler);
        recyclerView.setAdapter(adapter);
        recyclerView.setEmptyView(LayoutInflater.from(this).inflate(R.layout.item_no_data,null));
        recyclerView.setLayoutManager(new XLinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false));
        recyclerView.setColorSchemeResources(R.color.swipe_color1,R.color.swipe_color2,R.color.swipe_color3);
        recyclerView.enablePullRefresh(true);       //设置可以下拉
        recyclerView.enableLoadMore(true);
        recyclerView.setOnRecyclerRefreshListener(new PullRecyclerView.OnRecyclerRefreshListener() {
            @Override
            public void onPullRefresh() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        sendRequest();
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

    private void sendRequest() {
        Request<JSONObject> request=NoHttp.createJsonObjectRequest(teacher.teacherOrder(), RequestMethod.GET);
        request.add("key",teacher.teacherKey(getApplicationContext()));
        getRequestQueue().add(1, request, new OnResponseListener<JSONObject>() {
            @Override
            public void onStart(int what) {

            }

            @Override
            public void onSucceed(int what, Response<JSONObject> response) {
                mLog.i("response","->"+response.get().toString());
                //备注：当只剩最后一条记录时，点击开始培训完成后刷新列表，返回为：I/response: ->{"code":401,"msg":"系统繁忙，请稍候再试"}
                try {
                    JSONObject obejct=new JSONObject(response.get().toString());
                    mLog.e("reponse","-->"+response.get().toString());
                    mmsg=obejct.getString("msg");
                    if(obejct.getString("code").equals("200")){
                        JSONArray array=new JSONArray(obejct.getString("data"));
                        if(array.length()>0){
                            mdata.clear();
                            for (int i = 0; i <array.length(); i++) {
                                TeacherTrainOrder item=gson.fromJson(array.getJSONObject(i).toString(),TeacherTrainOrder.class);
                                mdata.add(item);
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
