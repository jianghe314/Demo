package com.kangzhan.student.Teacher.Train;

import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
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
import com.kangzhan.student.ShowUI.showMessage;
import com.kangzhan.student.Teacher.Adapter.TeacherTrainingAdapter;
import com.kangzhan.student.Teacher.bean.TeacherTrainRecord;
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

public class Teacher_trainTrainingActivity extends BaseActivity {
    private TeacherTrainingAdapter adapter;
    private Gson gson;
    private String mmg;
    private ArrayList<TeacherTrainRecord> mdata=new ArrayList<>();
    private PullRecyclerView recyclerView;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
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
                            AlertDialog.Builder builder=new AlertDialog.Builder(Teacher_trainTrainingActivity.this);
                            builder.setMessage(mmg);
                            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            builder.create().show();
                        }
                    });
                    break;
                case 9999:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            recyclerView.stopRefresh();
                            adapter.notifyDataSetChanged();
                            showMessage.showMsg(Teacher_trainTrainingActivity.this,"网络连接异常，请检测网络");
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
        setContentView(R.layout.activity_teacher_train_training);
        Toolbar toolbar= (Toolbar) findViewById(R.id.teacher_training_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        gson=new Gson();
        initView();
    }

    private void initView() {
        recyclerView= (PullRecyclerView) findViewById(R.id.teacher_training_recycler);
        adapter=new TeacherTrainingAdapter(Teacher_trainTrainingActivity.this,R.layout.item_teacher_training,mdata);
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
        Request<JSONObject> request=NoHttp.createJsonObjectRequest(teacher.teacherTrainRecord(), RequestMethod.GET);
        request.add("key",teacher.teacherKey(getApplicationContext()));
        request.add("type",1);
        getRequestQueue().add(1, request, new OnResponseListener<JSONObject>() {
            @Override
            public void onStart(int what) {

            }

            @Override
            public void onSucceed(int what, Response<JSONObject> response) {
                mLog.i("response","->"+response.get().toString());
                //response: ->{"code":402,"msg":"您暂时还没有陪练记录哦"}
                try {
                    JSONObject object=new JSONObject(response.get().toString());
                    String code=object.getString("code");
                    mmg=object.getString("msg");
                    if(code.equals("200")){
                        JSONArray array=new JSONArray(object.getString("data"));
                        if(array.length()>0){
                            mdata.clear();
                            for (int i = 0; i < array.length(); i++) {
                                TeacherTrainRecord item=gson.fromJson(array.getJSONObject(i).toString(),TeacherTrainRecord.class);
                                mdata.add(item);
                            }
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return true;
    }
}
