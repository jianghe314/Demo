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
import com.kangzhan.student.Student.mutils.DividerItemDecoration;
import com.kangzhan.student.Teacher.Adapter.TeacherMsgAdapter;
import com.kangzhan.student.Teacher.bean.TeacherMsg;
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

public class Teacher_Msg_bill extends BaseActivity implements View.OnClickListener{

    private TextView year;
    private ImageView sele_year;
    private PullRecyclerView listView;
    private Context context;
    private String mmsg;
    private int[] choiceYear= mCalender.getCalender();
    private TeacherMsgAdapter adapter;
    private ArrayList<TeacherMsg> mdata=new ArrayList<>();
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
                            listView.stopRefresh();
                            adapter.notifyDataSetChanged();
                        }
                    });
                    break;
                case 2222:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            listView.stopRefresh();
                            adapter.notifyDataSetChanged();
                            mToast.showText(getApplicationContext(),mmsg);
                        }
                    });
                    break;
                case 9999:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            listView.stopRefresh();
                            showMessage.showMsg(Teacher_Msg_bill.this,"网络连接异常，请检查网络连接");
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
        setContentView(R.layout.activity_teacher__msg_bill);
        context=this;
        Toolbar toolbar= (Toolbar) findViewById(R.id.teacher_msg_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        gson=new Gson();
        initView();

    }



    private void initView() {
        year= (TextView) findViewById(R.id.teacher_msg_year);
        year.setText(choiceYear[0]+"年");
        sele_year= (ImageView) findViewById(R.id.teacher_msg_year_sele);
        sele_year.setOnClickListener(this);
        listView= (PullRecyclerView) findViewById(R.id.teacher_msg_listView);
        adapter=new TeacherMsgAdapter(Teacher_Msg_bill.this,R.layout.item_teacher_msg,mdata);
        listView.setAdapter(adapter);
        listView.setEmptyView(LayoutInflater.from(this).inflate(R.layout.item_no_data,null));
        listView.setLayoutManager(new XLinearLayoutManager(Teacher_Msg_bill.this, LinearLayoutManager.VERTICAL,false));
        listView.addItemDecoration(new DividerItemDecoration(Teacher_Msg_bill.this,DividerItemDecoration.VERTICAL_LIST));
        listView.setColorSchemeResources(R.color.swipe_color1,R.color.swipe_color2,R.color.swipe_color3);
        listView.enablePullRefresh(true);       //设置可以下拉
        listView.enableLoadMore(true);
        listView.setOnRecyclerRefreshListener(new PullRecyclerView.OnRecyclerRefreshListener() {
            // 模拟下拉刷新网络请求
            @Override
            public void onPullRefresh() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        sendRequest(choiceYear[0]);
                    }
                }).start();

            }
            // 模拟上拉加载更多网络请求
            @Override
            public void onLoadMore() {
                listView.stopLoadMore();
            }
        });
        listView.postRefreshing();
    }

    private void sendRequest(int year) {
        Request<JSONObject> request=NoHttp.createJsonObjectRequest(teacher.teacherMsgBill(), RequestMethod.GET);
        request.add("key",teacher.teacherKey(getApplicationContext()));
        request.add("year",year);
        getRequestQueue().add(1, request, new OnResponseListener<JSONObject>() {
            @Override
            public void onStart(int what) {

            }

            @Override
            public void onSucceed(int what, Response<JSONObject> response) {
                try {
                    JSONObject object=new JSONObject(response.get().toString());
                    mLog.e("reponse","-->"+response.get().toString());
                    mmsg=object.getString("msg");
                    if(object.getString("code").equals("200")){
                        String data=object.getString("bill");
                        JSONArray array=new JSONArray(data);
                        if(array.length()>0){
                            mdata.clear();
                            for (int i = 0; i <array.length(); i++) {
                                JSONObject obj=array.getJSONObject(i);
                                TeacherMsg Msg=gson.fromJson(obj.toString(),TeacherMsg.class);
                                mdata.add(Msg);
                            }
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
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.teacher_msg_year_sele:
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
        final Dialog dialog=new Dialog(Teacher_Msg_bill.this);
        dialog.setTitle("选择查询年份");
        View view= LayoutInflater.from(Teacher_Msg_bill.this).inflate(R.layout.item_simple_listview,null);
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
