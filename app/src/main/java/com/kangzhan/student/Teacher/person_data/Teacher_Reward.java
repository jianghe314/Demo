package com.kangzhan.student.Teacher.person_data;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.holenzhou.pullrecyclerview.PullRecyclerView;
import com.holenzhou.pullrecyclerview.layoutmanager.XLinearLayoutManager;
import com.kangzhan.student.Debug.mLog;
import com.kangzhan.student.R;
import com.kangzhan.student.ShowUI.mToast;
import com.kangzhan.student.ShowUI.showMessage;
import com.kangzhan.student.ShowUI.showProgress;
import com.kangzhan.student.Student.Booking.TeacherListActivity;
import com.kangzhan.student.Student.News.AddAdviseActivity;
import com.kangzhan.student.Student.mutils.DividerItemDecoration;
import com.kangzhan.student.Teacher.Adapter.TeacherRewardAdapter;
import com.kangzhan.student.Teacher.bean.TeacherReward;
import com.kangzhan.student.com.BaseActivity;
import com.kangzhan.student.mInterface.TeacherInterface.teacher;
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

public class Teacher_Reward extends BaseActivity implements View.OnClickListener{
    //年份选择器
    private TextView year;
    private ImageView sele_year;
    private TeacherRewardAdapter adapter;
    private ArrayList<TeacherReward> mdata=new ArrayList<>();
    private PullRecyclerView recycler;
    private int[] choiceYear= mCalender.getCalender();
    private String mmsg;
    private Gson gson;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0000:
                    break;
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
                            mToast.showText(getApplicationContext(),mmsg);
                        }
                    });
                    break;
                case 9999:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            recycler.stopRefresh();
                            showMessage.showMsg(Teacher_Reward.this,"网络连接异常，请检查网络连接");
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
        setContentView(R.layout.activity_teacher__reward);
        Toolbar toolbar= (Toolbar) findViewById(R.id.teacher_reward_toolbar);
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
        year= (TextView) findViewById(R.id.teacher_reward_year);
        year.setText(choiceYear[0]+"年");
        sele_year= (ImageView) findViewById(R.id.teacher_reward_year_sele);
        sele_year.setOnClickListener(this);
        recycler= (PullRecyclerView) findViewById(R.id.teacher_reward_list);
    }

    private void initData() {
        adapter=new TeacherRewardAdapter(Teacher_Reward.this,R.layout.item_teacher_reward,mdata);
        recycler.setAdapter(adapter);
        recycler.setEmptyView(LayoutInflater.from(this).inflate(R.layout.item_no_data,null));
        recycler.setColorSchemeResources(R.color.swipe_color1,R.color.swipe_color2,R.color.swipe_color3);
        recycler.setLayoutManager(new XLinearLayoutManager(Teacher_Reward.this, LinearLayoutManager.VERTICAL,false));
        recycler.enablePullRefresh(true);       //设置可以下拉
        recycler.enableLoadMore(true);
        recycler.setOnRecyclerRefreshListener(new PullRecyclerView.OnRecyclerRefreshListener() {
            @Override
            public void onPullRefresh() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        sendRequest(choiceYear[0]);
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

    private void sendRequest(int year) {
        Request<JSONObject> request=NoHttp.createJsonObjectRequest(teacher.teacherReward(), RequestMethod.GET);
        request.add("key",teacher.teacherKey(getApplicationContext()));
        request.add("year",year);
        getRequestQueue().add(1, request, new OnResponseListener<JSONObject>() {
            @Override
            public void onStart(int what) {

            }

            @Override
            public void onSucceed(int what, Response<JSONObject> response) {
                mLog.i("response","->"+response);
                try {
                    JSONObject object=new JSONObject(response.get().toString());
                    mmsg=object.getString("msg");
                    if(object.getString("code").equals("200")){
                        String data=object.getString("bonus");
                        JSONArray array=new JSONArray(data);
                        mLog.e("array->",array.length()+"");
                        if(array.length()>0){
                            mdata.clear();
                            for (int i = 0; i <array.length(); i++) {
                                JSONObject obj=array.getJSONObject(i);
                                TeacherReward reward=gson.fromJson(obj.toString(),TeacherReward.class);
                                mdata.add(reward);
                            }
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



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //年份选择器
            case R.id.teacher_reward_year_sele:
                addChoiceYears(2017,2025);
                break;
        }
    }

    private void addChoiceYears(int starYear,int endYear) {
        ArrayList<String> myear=new ArrayList<>();
        for (int i = starYear; i <=endYear; i++) {
            myear.add(i+"年");
        }
        final Dialog dialog=new Dialog(Teacher_Reward.this);
        dialog.setTitle("选择查询年份");
        View view= LayoutInflater.from(Teacher_Reward.this).inflate(R.layout.item_simple_listview,null);
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
                        sendRequest(Integer.valueOf(year.getText().toString().substring(0,4)));
                        mLog.e("选择年份","->"+Integer.valueOf(year.getText().toString().substring(0,4)));
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
}
