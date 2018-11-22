package com.kangzhan.student.Teacher.person_data;

import android.app.ExpandableListActivity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.kangzhan.student.Debug.mLog;
import com.kangzhan.student.R;
import com.kangzhan.student.ShowUI.mToast;
import com.kangzhan.student.ShowUI.showProgress;
import com.kangzhan.student.Teacher.Adapter.RewardDetailAdapter;
import com.kangzhan.student.Teacher.Adapter.TeacherRewardAdapter;
import com.kangzhan.student.Teacher.bean.ExpandChild;
import com.kangzhan.student.Teacher.bean.ExpandReward;
import com.kangzhan.student.Teacher.bean.TeacherReward;
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
import java.util.List;
//查看详情有问题
public class RewardDetail extends BaseActivity {
    private TextView year_month,ke2,ke3,reward_sum;
    private String Id;
    private ExpandableListView expand;
    private ArrayList<ExpandReward> mparent=new ArrayList<>();        //组列表项，每个组都有一个子List
    private ArrayList<ArrayList<ExpandChild>> child=new ArrayList<>();     //子列表项
    private RewardDetailAdapter adapter;
    private TeacherReward reward;
    private Gson gson;
    private String mmsg;
    //
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0000:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.showProgress(RewardDetail.this,"加载中...");
                        }
                    });
                    break;
                case 1111:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.closeProgress();
                            year_month.setText(reward.getBonus_month());
                            ke2.setText(reward.getKeer());
                            ke3.setText(reward.getKesan());
                            reward_sum.setText(reward.getAmount());
                            adapter.notifyDataSetChanged();
                        }
                    });
                    break;
                case 2222:
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
                            mToast.showText(RewardDetail.this,"网络异常，请稍后再试");
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
        setContentView(R.layout.activity_reward_detail);
        Intent getIntent=getIntent();
        Id=getIntent.getStringExtra("id");
        Toolbar toolbar= (Toolbar) findViewById(R.id.teacher_RewardDetail_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        gson=new Gson();
        initView();
        initData();
    }
    private void initView() {
        year_month= (TextView) findViewById(R.id.teacher_reward_year_month);
        ke2= (TextView) findViewById(R.id.teacher_reward_ke2);
        ke3= (TextView) findViewById(R.id.teacher_reward_ke3);
        reward_sum= (TextView) findViewById(R.id.teacher_reward_sum);
        expand= (ExpandableListView) findViewById(R.id.teacher_reward_detail);
        adapter=new RewardDetailAdapter(this,mparent,child);
        expand.setAdapter(adapter);
    }

    private void initData() {
       handler.sendEmptyMessage(0000);
        new Thread(new Runnable() {
            @Override
            public void run() {
                sendRequst();
            }
        }).start();
    }

    private void sendRequst() {
        Request<JSONObject> request=NoHttp.createJsonObjectRequest(teacher.teacherRewardDetail(), RequestMethod.GET);
        request.add("key",teacher.teacherKey(getApplicationContext()));
        request.add("id",Id);
        getRequestQueue().add(1, request, new OnResponseListener<JSONObject>() {
            @Override
            public void onStart(int what) {

            }

            @Override
            public void onSucceed(int what, Response<JSONObject> response) {
                mLog.i("response","->"+response.get().toString());
                try {
                    JSONObject object=new JSONObject(response.get().toString());
                    mmsg=object.getString("msg");
                    if(object.getString("code").equals("200")){
                        String data=object.getString("Bonusdetail");
                        JSONObject obj=new JSONObject(data);
                        String data0=obj.getString("0");
                        reward=gson.fromJson(data0,TeacherReward.class);
                        //
                        String data1=obj.getString("student_score");
                        JSONArray array=new JSONArray(data1);
                        if(array.length()>0){
                            mparent.clear();
                            child.clear();
                            for (int i = 0; i < array.length(); i++) {
                                mLog.e("array.length","->"+i);
                                JSONObject obj1=array.getJSONObject(i);
                                ExpandReward parent=new ExpandReward();
                                //
                                parent.setId(obj1.getString("id"));
                                parent.setExam_date(obj1.getString("exam_date"));
                                parent.setStudent_name(obj1.getString("student_name"));
                                parent.setExam_name(obj1.getString("exam_name"));
                                parent.setAmount(obj1.getString("amount"));
                                parent.setLength(obj1.getString("length"));
                                String data2=obj1.getString("record");
                                //取record里面的数据
                                JSONArray array2=new JSONArray(data2);
                                ArrayList<ExpandChild> childdata=new ArrayList<ExpandChild>();
                                if(array2.length()>0){
                                    for (int j = 0; j <array2.length(); j++) {
                                        JSONObject obj2=array2.getJSONObject(j);
                                        ExpandChild child=gson.fromJson(obj2.toString(),ExpandChild.class);
                                        childdata.add(child);
                                    }
                                    child.add(i,childdata);
                                }else {
                                    child.add(i,childdata);
                                }
                                mparent.add(parent);
                            }
                            handler.sendEmptyMessage(1111);
                        }else {
                            handler.sendEmptyMessage(1111);
                        }
                    }else {
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
