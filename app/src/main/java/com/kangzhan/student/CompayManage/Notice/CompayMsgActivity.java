package com.kangzhan.student.CompayManage.Notice;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
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
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.holenzhou.pullrecyclerview.PullRecyclerView;
import com.holenzhou.pullrecyclerview.layoutmanager.XLinearLayoutManager;
import com.kangzhan.student.CompayManage.Adapter.CompayMsgAdapter;
import com.kangzhan.student.CompayManage.Adapter.CompayNoticeAdapter;
import com.kangzhan.student.CompayManage.Bean.CompayMsg;
import com.kangzhan.student.CompayManage.Bean.CompayNotice;
import com.kangzhan.student.Debug.mLog;
import com.kangzhan.student.R;
import com.kangzhan.student.ShowUI.mToast;
import com.kangzhan.student.ShowUI.showMessage;
import com.kangzhan.student.ShowUI.showProgress;
import com.kangzhan.student.com.BaseActivity;
import com.kangzhan.student.mInterface.CompayInterface.CompayInterface;
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

public class CompayMsgActivity extends BaseActivity implements View.OnClickListener{
    private TextView timeS,timeE,doSearch;
    private EditText searchContent;
    private String mmsg;
    private CompayMsgAdapter adapter;
    private boolean isShow=false;
    private PopupWindow popupMenu;
    private LinearLayout menu1,menu2;
    private PullRecyclerView recyclerView;
    private ArrayList<String> params=new ArrayList<>();
    private ArrayList<String> values=new ArrayList<>();
    private ArrayList<CompayMsg> mdata=new ArrayList<>();
    private int[] mcalender= mCalender.getCalender();
    private boolean isVisible=false;
    private Gson gson;
    private int mpage=1;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1111:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            recyclerView.stopRefresh();
                            recyclerView.stopLoadMore();
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
                            showProgress.showProgress(CompayMsgActivity.this,"搜索中...");
                        }
                    });
                    break;
                case 4444:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.closeProgress();
                            initData();
                            mToast.showText(getApplicationContext(),mmsg);
                        }
                    });
                    break;
                case 5555:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            recyclerView.stopLoadMore();
                            mToast.showText(getApplicationContext(),"没有更多了！");
                        }
                    });
                    break;
                case 9999:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            recyclerView.stopRefresh();
                            showMessage.showMsg(CompayMsgActivity.this,"网络连接异常，请检查网络连接");
                        }
                    });
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compay_msg);
        Toolbar toolbar= (Toolbar) findViewById(R.id.compay_msg_toolbar);
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
        recyclerView= (PullRecyclerView) findViewById(R.id.compay_msg_recycler);
        timeS= (TextView) findViewById(R.id.compay_msg_msg_setStartT);
        timeE= (TextView) findViewById(R.id.compay_msg_msg_setEndT);
        doSearch= (TextView) findViewById(R.id.compay_msg_msg_Tosearch);
        searchContent= (EditText) findViewById(R.id.compay_msg_msg_content);
        timeS.setText(mcalender[0]+"年"+mcalender[1]+"月"+mcalender[2]+"日");
        timeE.setText(mcalender[0]+"年"+mcalender[1]+"月"+mcalender[2]+"日");
        doSearch.setOnClickListener(this);
        timeS.setOnClickListener(this);
        timeE.setOnClickListener(this);
    }
    private void initData() {
        adapter=new CompayMsgAdapter(CompayMsgActivity.this,R.layout.item_fragment_school_notice_t3,mdata);
        recyclerView.setAdapter(adapter);
        recyclerView.setEmptyView(LayoutInflater.from(this).inflate(R.layout.item_no_data,null));
        recyclerView.setLayoutManager(new XLinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
        recyclerView.setColorSchemeResources(R.color.swipe_color1,R.color.swipe_color2,R.color.swipe_color3);
        recyclerView.enablePullRefresh(true);       //设置可以下拉
        recyclerView.enableLoadMore(true);
        recyclerView.setOnRecyclerRefreshListener(new PullRecyclerView.OnRecyclerRefreshListener() {
            @Override
            public void onPullRefresh() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        params.clear();
                        values.clear();
                        params.add("key");
                        params.add("is_export");
                        values.add(CompayInterface.CompayKey(getApplicationContext()));
                        values.add("0");
                        sendRequest("GET",CompayInterface.CompayMsgList(),1,params,values);
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
                        params.add("is_export");
                        params.add("page");
                        values.add(CompayInterface.CompayKey(getApplicationContext()));
                        values.add("0");
                        values.add(mpage+"");
                        sendRequest("GET",CompayInterface.CompayMsgList(),3,params,values);
                    }
                }).start();
            }
        });
        recyclerView.postRefreshing();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.compay_msg_msg_setStartT:
                com.kangzhan.student.utils.picker.DatePicker picker=new com.kangzhan.student.utils.picker.DatePicker(CompayMsgActivity.this);
                picker.setRangeStart(2016,1,1);
                picker.setRangeEnd(2050,10,14);
                picker.setSelectedItem(mcalender[0],mcalender[1],mcalender[2]);
                picker.setOnDatePickListener(new com.kangzhan.student.utils.picker.DatePicker.OnYearMonthDayPickListener() {
                    @Override
                    public void onDatePicked(String year, String month, String day) {
                        timeS.setText(year+"年"+month+"月"+day+"日");
                    }
                });
                picker.show();
                break;
            case R.id.compay_msg_msg_setEndT:
                com.kangzhan.student.utils.picker.DatePicker picker1=new com.kangzhan.student.utils.picker.DatePicker(CompayMsgActivity.this);
                picker1.setRangeStart(2016,1,1);
                picker1.setRangeEnd(2050,10,14);
                picker1.setSelectedItem(mcalender[0],mcalender[1],mcalender[2]);
                picker1.setOnDatePickListener(new com.kangzhan.student.utils.picker.DatePicker.OnYearMonthDayPickListener() {
                    @Override
                    public void onDatePicked(String year, String month, String day) {
                        timeE.setText(year+"年"+month+"月"+day+"日");
                    }
                });
                picker1.show();
                break;
            case R.id.compay_msg_msg_Tosearch:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        params.clear();
                        values.clear();
                        params.add("key");
                        params.add("is_export");
                        params.add("searchCriteria");
                        params.add("signStartTime");
                        params.add("signEndtTime");
                        values.add(CompayInterface.CompayKey(getApplicationContext()));
                        values.add("0");
                        values.add(searchContent.getText().toString().trim());
                        values.add(getTime(timeS));
                        values.add(getTime(timeE));
                        sendRequest("GET",CompayInterface.CompayMsgList(),1,params,values);
                    }
                }).start();
                break;
            case R.id.item_school_notice_edit_add:
                popupMenu.dismiss();
                Intent add=new Intent(this,CompayAddMsgActivity.class);
                startActivity(add);
                break;
            case R.id.item_school_notice_edit_dele:
                final StringBuilder builder=new StringBuilder();
                for (int i = 0; i < mdata.size(); i++) {
                    if(mdata.get(i).isClick()){
                        builder.append(mdata.get(i).getId());
                        builder.append(",");
                    }
                }
                if(builder.toString().length()>0){
                    handler.sendEmptyMessage(3333);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            params.clear();
                            values.clear();
                            params.add("key");
                            params.add("batch_ids");
                            values.add(CompayInterface.CompayKey(getApplicationContext()));
                            values.add(builder.toString().substring(0,builder.toString().length()-1));
                            sendRequest("POST",CompayInterface.CompayMsgDele(),2,params,values);
                        }
                    }).start();
                    popupMenu.dismiss();
                }else {
                    mToast.showText(getApplicationContext(),"请选择对象！");
                }
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
        if(item.getItemId()==R.id.teacher_mydata_edit){
            //添加菜单
            if(isShow){
                popupMenu.dismiss();
                for (int i = 0; i <mdata.size() ; i++) {
                    mdata.get(i).setShow(false);
                }
                adapter.notifyDataSetChanged();
                isShow=false;
            }else {
                addMenu();
                for (int i = 0; i <mdata.size() ; i++) {
                    mdata.get(i).setShow(true);
                }
                adapter.notifyDataSetChanged();
                isShow=true;
            }
        }
        return super.onOptionsItemSelected(item);
    }


    private void addMenu() {
        View root= LayoutInflater.from(this).inflate(R.layout.item_school_notice_edit_menu,null);
        menu1= (LinearLayout) root.findViewById(R.id.item_school_notice_edit_add);
        menu2= (LinearLayout) root.findViewById(R.id.item_school_notice_edit_dele);
        menu1.setOnClickListener(this);
        menu2.setOnClickListener(this);
        popupMenu=new PopupWindow(root, ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        popupMenu.setAnimationStyle(R.style.BottomMenu);
        popupMenu.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this,R.color.textColor_whit)));
        popupMenu.showAtLocation(getLayoutInflater().inflate(R.layout.activity_student_manage,null), Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL,0,0);
    }

    private void sendRequest(String Way,String url,int what,ArrayList<String> params,ArrayList<String> values) {
        RequestMethod method=null;
        if(Way.equals("POST")){
            method=RequestMethod.POST;
        }else if(Way.equals("GET")){
            method=RequestMethod.GET;
        }
        Request<JSONObject> request= NoHttp.createJsonObjectRequest(url,method);
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
                    JSONObject object=new JSONObject(response.get().toString());
                    mLog.e("reponse","-->"+response.get().toString());
                    mmsg=object.getString("msg");
                    if(what==1){
                        if(object.getString("code").equals("200")){
                            JSONArray array=new JSONArray(object.getString("data"));
                            if(array.length()>0){
                                mdata.clear();
                                for (int i = 0; i < array.length(); i++) {
                                    CompayMsg info=gson.fromJson(array.getJSONObject(i).toString(),CompayMsg.class);
                                    mdata.add(info);
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
                    }else if(what==2){
                        handler.sendEmptyMessage(4444);
                    }else if(what==3){
                        if(object.getString("code").equals("200")){
                            JSONArray array=new JSONArray(object.getString("data"));
                            if(array.length()>0) {
                                for (int i = 0; i < array.length(); i++) {
                                    CompayMsg info = gson.fromJson(array.getJSONObject(i).toString(), CompayMsg.class);
                                    mdata.add(info);
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


    private String getTime(TextView edit){
        String getTime=edit.getText().toString().trim();
        getTime=getTime.replace("年","-");
        getTime=getTime.replace("月","-");
        getTime=getTime.replace("日","");
        return getTime;
    }
}
