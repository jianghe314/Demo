package com.kangzhan.student.School.Notice;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.holenzhou.pullrecyclerview.PullRecyclerView;
import com.holenzhou.pullrecyclerview.layoutmanager.XLinearLayoutManager;
import com.kangzhan.student.Debug.mLog;
import com.kangzhan.student.R;
import com.kangzhan.student.School.Adapter.SchoolAdviseAdapter;
import com.kangzhan.student.School.Adapter.SchoolNoticeAdapter;
import com.kangzhan.student.School.Bean.SchoolAdvise;
import com.kangzhan.student.School.Bean.SchoolNotice;
import com.kangzhan.student.ShowUI.mToast;
import com.kangzhan.student.ShowUI.showMessage;
import com.kangzhan.student.ShowUI.showProgress;
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

public class School_Notice_AdviseActivity extends BaseActivity implements View.OnClickListener{
    private LinearLayout menu1,menu2;
    private String mmsg;
    private PopupWindow popupMenu;
    private PullRecyclerView recyclerView;
    private SchoolAdviseAdapter adapter;
    private ArrayList<SchoolAdvise> data=new ArrayList<>();
    //网络请求参数
    private ArrayList<String> params=new ArrayList<>();
    private ArrayList<String> values=new ArrayList<>();
    private boolean isVisible=false;
    private Gson gson;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1000:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            recyclerView.stopRefresh();
                            adapter.notifyDataSetChanged();
                        }
                    });
                    break;
                case 1111:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            recyclerView.stopRefresh();
                            mToast.showText(getApplicationContext(),mmsg);
                        }
                    });
                    break;
                case 2222:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.showProgress(School_Notice_AdviseActivity.this,"删除中...");
                        }
                    });
                    break;
                case 3333:
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            params.clear();
                            values.clear();
                            //参数
                            params.add("key");
                            params.add("source");
                            //值
                            values.add(school.schoolKey(getApplicationContext()));
                            values.add("30");
                            sendRequest("GET",school.SchoolAdviseList(),1,params,values);
                        }
                    }).start();
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
                            showMessage.showMsg(School_Notice_AdviseActivity.this,"网络连接异常，请检查网络连接");
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
        setContentView(R.layout.activity_school__notice__advise);
        Toolbar toolbar= (Toolbar) findViewById(R.id.school_notice_advise_toolbar);
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
        recyclerView= (PullRecyclerView) findViewById(R.id.school_notice_advise_recycler);
        addMenu();
    }

    private void initData() {
        adapter=new SchoolAdviseAdapter(School_Notice_AdviseActivity.this,R.layout.item_fragment_school_notice_t2,data);
        recyclerView.setAdapter(adapter);
        recyclerView.setEmptyView(LayoutInflater.from(this).inflate(R.layout.item_no_data,null));
        recyclerView.setLayoutManager(new XLinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
        recyclerView.setColorSchemeResources(R.color.swipe_color1,R.color.swipe_color2,R.color.swipe_color3);
        recyclerView.enablePullRefresh(true);       //设置可以下拉
        recyclerView.enableLoadMore(true);
        recyclerView.setOnRecyclerRefreshListener(new PullRecyclerView.OnRecyclerRefreshListener() {
            // 模拟下拉刷新网络请求
            @Override
            public void onPullRefresh() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        params.clear();
                        values.clear();
                        //参数
                        params.add("key");
                        params.add("source");
                        //值
                        values.add(school.schoolKey(getApplicationContext()));
                        values.add("30");
                        sendRequest("GET",school.SchoolAdviseList(),1,params,values);
                    }
                }).start();

            }
            // 模拟上拉加载更多网络请求
            @Override
            public void onLoadMore() {
                recyclerView.stopLoadMore();
            }
        });
        recyclerView.postRefreshing();
    }


    private void sendRequest(String Way,String url,int what,ArrayList<String> params,ArrayList<String> values) {
        RequestMethod method=null;
        if(Way.equals("POST")){
            method=RequestMethod.POST;
        }else if(Way.equals("GET")){
            method=RequestMethod.GET;
        }
        Request<JSONObject> request=NoHttp.createJsonObjectRequest(url,method);
        for (int i = 0; i <params.size(); i++) {
            request.add(params.get(i),values.get(i));
        }
        getRequestQueue().add(what, request, new OnResponseListener<JSONObject>() {
            @Override
            public void onStart(int what) {

            }

            @Override
            public void onSucceed(int what, Response<JSONObject> response) {
                try {
                    JSONObject object = new JSONObject(response.get().toString());
                    mLog.e("reponse","-->"+response.get().toString());
                    mmsg=object.getString("msg");
                    if(what==1){
                        if(object.getString("code").equals("200")){
                            JSONArray array=new JSONArray(object.getString("data"));
                            if(array.length()>0){
                                data.clear();
                                for (int i = 0; i < array.length(); i++) {
                                    SchoolAdvise mange=gson.fromJson(array.getJSONObject(i).toString(),SchoolAdvise.class);
                                    data.add(mange);
                                }
                                handler.sendEmptyMessage(1000);
                            }else {
                                data.clear();
                                handler.sendEmptyMessage(1111);
                            }
                        }else {
                            data.clear();
                            handler.sendEmptyMessage(1111);
                        }
                    }else if(what==2){
                        handler.sendEmptyMessage(3333);
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
            case R.id.item_school_notice_edit_add:
                popupMenu.dismiss();
                //添加工单
                Intent add=new Intent(this,School_add_advise.class);
                startActivity(add);
                break;
            case R.id.item_school_notice_edit_dele:
                //工单删除
                popupMenu.dismiss();
                final StringBuilder builder=new StringBuilder();
                for (int i = 0; i <data.size(); i++) {
                    if(data.get(i).isClick()){
                        builder.append(data.get(i).getId());
                        builder.append(",");
                    }
                }
                handler.sendEmptyMessage(2222);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        params.clear();
                        values.clear();
                        params.add("key");
                        params.add("ticketids");
                        params.add("close_type");
                        values.add(school.schoolKey(getApplicationContext()));
                        values.add(builder.toString());
                        values.add("30");
                        sendRequest("POST",school.SchoolAdviseListDele(),2,params,values);
                    }
                }).start();

                break;
            default:
                break;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.teacher_mydata_edit,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            case R.id.teacher_mydata_edit:
                //点击编辑底部菜单弹出
                if(isVisible){
                    for (int i = 0; i <data.size(); i++) {
                        data.get(i).setShow(false);
                    }
                    isVisible=false;
                    popupMenu.dismiss();
                }else {
                    for (int i = 0; i <data.size(); i++) {
                        data.get(i).setShow(true);
                    }
                    isVisible=true;
                    popupMenu.showAtLocation(getLayoutInflater().inflate(R.layout.activity_student_manage,null), Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL,0,0);
                }
                adapter.notifyDataSetChanged();
                break;
            default:
                break;
        }
        return true;
    }


    private void addMenu() {
        //实现 PopupWindow
        View root= LayoutInflater.from(this).inflate(R.layout.item_school_notice_edit_menu,null);
        menu1= (LinearLayout) root.findViewById(R.id.item_school_notice_edit_add);
        menu2= (LinearLayout) root.findViewById(R.id.item_school_notice_edit_dele);
        menu1.setOnClickListener(this);
        menu2.setOnClickListener(this);
        popupMenu=new PopupWindow(root, ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        popupMenu.setAnimationStyle(R.style.BottomMenu);
        popupMenu.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this,R.color.textColor_whit)));
    }

}
