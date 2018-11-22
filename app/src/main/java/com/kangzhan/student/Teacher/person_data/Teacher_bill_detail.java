package com.kangzhan.student.Teacher.person_data;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.ParcelUuid;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.holenzhou.pullrecyclerview.PullRecyclerView;
import com.holenzhou.pullrecyclerview.layoutmanager.XLinearLayoutManager;
import com.kangzhan.student.R;
import com.kangzhan.student.ShowUI.mToast;
import com.kangzhan.student.ShowUI.showMessage;
import com.kangzhan.student.ShowUI.showProgress;
import com.kangzhan.student.Student.mutils.DividerItemDecoration;
import com.kangzhan.student.Teacher.Adapter.TeacherBillDetailAdapter;
import com.kangzhan.student.Teacher.bean.TeacherBill;
import com.kangzhan.student.Teacher.bean.TeacherBillDetail;
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

public class Teacher_bill_detail extends BaseActivity implements View.OnClickListener{
    private Toolbar toolbar;
    private TextView data,hours,cost,price;
    private Button sure;
    private String mmsg;
    private String Id;
    private TeacherBill bill;
    private PullRecyclerView listView;
    private ArrayList<TeacherBillDetail> mdata=new ArrayList<>();
    private TeacherBillDetailAdapter adapter;
    private Gson gson;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0000:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            listView.stopRefresh();
                            data.setText(bill.getTrain_date());
                            hours.setText(bill.getTrain_length());
                            price.setText(bill.getDraw());
                            cost.setText(bill.getAmount());
                            adapter.notifyDataSetChanged();
                        }
                    });
                    break;
                case 1111:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            listView.stopRefresh();
                            mToast.showText(getApplicationContext(),"服务器打盹，请稍后再试");
                        }
                    });
                    break;
                case 2222:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.showProgress(Teacher_bill_detail.this,"确认中...");
                        }
                    });
                    break;
                case 3333:
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
                            listView.stopRefresh();
                            showProgress.closeProgress();
                            showMessage.showMsg(Teacher_bill_detail.this,"网络连接异常，请检查网络连接");
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
        setContentView(R.layout.activity_teacher_bill_detail);
        Toolbar toolbar= (Toolbar) findViewById(R.id.teacher_bill_detail_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        Intent getIntent=getIntent();
        Id=getIntent.getStringExtra("id");
        gson=new Gson();
        initView();
    }

    private void initView() {
        data= (TextView) findViewById(R.id.teacher_bill_detail_year_month);
        hours= (TextView) findViewById(R.id.teacher_bill_detail_Num);
        price= (TextView) findViewById(R.id.teacher_bill_detail_price);
        cost= (TextView) findViewById(R.id.teacher_bill_detail_cost);
        sure= (Button) findViewById(R.id.teacher_bill_detail_btn);
        sure.setOnClickListener(this);
        listView= (PullRecyclerView) findViewById(R.id.teacher_bill_detail_list);
        adapter=new TeacherBillDetailAdapter(Teacher_bill_detail.this,R.layout.item_teacher_bill_detail,mdata);
        listView.setAdapter(adapter);
        listView.setEmptyView(LayoutInflater.from(this).inflate(R.layout.item_no_data,null));
        listView.setLayoutManager(new XLinearLayoutManager(Teacher_bill_detail.this, LinearLayoutManager.VERTICAL,false));
        listView.addItemDecoration(new DividerItemDecoration(Teacher_bill_detail.this,DividerItemDecoration.VERTICAL_LIST));
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
                        sendRequest(teacher.teacherBillDetail(),1,"id","GET");
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

    private void sendRequest(String url,int what,String params,String way) {
        RequestMethod mway=null;
        if(way.equals("POST")){
            mway=RequestMethod.POST;
        }else if(way.equals("GET")){
            mway=RequestMethod.GET;
        }
        final Request<JSONObject> request=NoHttp.createJsonObjectRequest(url,mway);
        request.add("key", teacher.teacherKey(getApplicationContext()));
        request.add(params,Id);
        getRequestQueue().add(what, request, new OnResponseListener<JSONObject>() {
            @Override
            public void onStart(int what) {

            }

            @Override
            public void onSucceed(int what, Response<JSONObject> response) {
                if(what==1){
                    try {
                        JSONObject object=new JSONObject(response.get().toString());
                        String code=object.getString("code");
                        if(code.equals("200")){
                            String data=object.getString("state");
                            JSONArray array=new JSONArray(data);
                            if(array.length()>0){
                                JSONObject obj=array.getJSONObject(0);
                                bill=new TeacherBill();
                                bill.setId(obj.getString("id"));
                                bill.setAmount(obj.getString("amount"));
                                bill.setDraw(obj.getString("draw"));
                                bill.setTrain_length(obj.getString("train_length"));
                                bill.setTrain_date(obj.getString("train_date"));
                                String mmdata=obj.getString("train_record");
                                JSONArray arra=new JSONArray(mmdata);
                                if(arra.length()>0){
                                    mdata.clear();
                                    for (int i = 0; i < arra.length(); i++) {
                                        TeacherBillDetail detail=gson.fromJson(arra.getJSONObject(i).toString(),TeacherBillDetail.class);
                                        mdata.add(detail);
                                    }

                                }
                                handler.sendEmptyMessage(0000);
                            }
                        }else {
                           handler.sendEmptyMessage(1111);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else if(what==2){
                    try {
                        JSONObject object=new JSONObject(response.get().toString());
                        mmsg=object.getString("msg");
                        handler.sendEmptyMessage(3333);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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
            case R.id.teacher_bill_detail_btn:
                Message msg=new Message();
                msg.what=2222;
                handler.sendMessage(msg);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        sendRequest(teacher.teacherConfirmBillDetail(),2,"State_id","POST");
                    }
                }).start();
                break;
            default:
                break;
        }
    }


}
