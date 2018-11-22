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
import com.kangzhan.student.School.Adapter.EduStuManageAdapter;
import com.kangzhan.student.School.Adapter.SchoolNoticeAdapter;
import com.kangzhan.student.School.Bean.EduStuMange;
import com.kangzhan.student.School.Bean.SchoolNotice;
import com.kangzhan.student.School.Edu.StudentManageActivity;
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

import static com.kangzhan.student.utils.framework.popup.BasicPopup.MATCH_PARENT;
import static com.kangzhan.student.utils.framework.popup.BasicPopup.WRAP_CONTENT;

public class School_Notice_NoticeActivity extends BaseActivity implements View.OnClickListener{
    private LinearLayout menu1,menu2;
    private TextView timeS,timeE,doSearch;
    private EditText searchContent;
    private String mmsg;
    private PopupWindow popupMenu;
    private PullRecyclerView recyclerView;
    private SchoolNoticeAdapter adapter;
    private ArrayList<SchoolNotice> data=new ArrayList<>();
    //网络请求参数
    private ArrayList<String> params=new ArrayList<>();
    private ArrayList<String> values=new ArrayList<>();
    private int[] mcalender= mCalender.getCalender();
    private boolean isVisible=false;
    private Gson gson;
    private int mpage=1;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0000:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            recyclerView.stopRefresh();
                            recyclerView.stopLoadMore();
                            adapter.notifyDataSetChanged();
                        }
                    });
                    break;
                case 1111:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            recyclerView.stopRefresh();
                            adapter.notifyDataSetChanged();
                            mToast.showText(getApplicationContext(),mmsg);
                        }
                    });
                    break;
                case 2222:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.showProgress(School_Notice_NoticeActivity.this,"删除中...");
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
                            params.add("export");
                            params.add("start_date");
                            params.add("end_date");
                            params.add("searchCriteria");
                            //值
                            values.add(school.schoolKey(getApplicationContext()));
                            values.add("1");
                            values.add(getTime(timeS));
                            values.add(getTime(timeE));
                            values.add(searchContent.getText().toString().trim());
                            sendRequest("GET",school.SchoolNotice(),1,params,values);
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
                            showProgress.closeProgress();
                            showMessage.showMsg(School_Notice_NoticeActivity.this,"网络连接异常，请检查网络连接");
                        }
                    });
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school__notice__notice);
        Toolbar toolbar= (Toolbar) findViewById(R.id.school_notice_notice_toolbar);
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
        timeS= (TextView) findViewById(R.id.school_notice_notice_setStartT);
        timeE= (TextView) findViewById(R.id.school_notice_notice_setEndT);
        doSearch= (TextView) findViewById(R.id.school_notice_notice_Tosearch);
        searchContent= (EditText) findViewById(R.id.school_notice_notice_content);
        recyclerView= (PullRecyclerView) findViewById(R.id.school_notice_notice_recycler);
        timeS.setText(mcalender[0]+"年"+mcalender[1]+"月"+mcalender[2]+"日");
        timeE.setText(mcalender[0]+"年"+mcalender[1]+"月"+mcalender[2]+"日");
        addMenu();
        doSearch.setOnClickListener(this);
        timeS.setOnClickListener(this);
        timeE.setOnClickListener(this);
    }
    private void initData() {
        adapter=new SchoolNoticeAdapter(School_Notice_NoticeActivity.this,R.layout.item_fragment_school_notice_t1,data);
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
                        params.add("export");
                        params.add("searchCriteria");
                        params.add("start_date");
                        params.add("end_date");
                        //值
                        values.add(school.schoolKey(getApplicationContext()));
                        values.add("1");
                        values.add(searchContent.getText().toString().trim());
                        values.add(getTime(timeS));
                        values.add(getTime(timeE));
                        sendRequest("GET",school.SchoolNotice(),1,params,values);
                    }
                }).start();

            }
            // 模拟上拉加载更多网络请求
            @Override
            public void onLoadMore() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        mpage=mpage+1;
                        params.clear();
                        values.clear();
                        //参数
                        params.add("key");
                        params.add("export");
                        params.add("searchCriteria");
                        params.add("start_date");
                        params.add("end_date");
                        params.add("page");
                        //值
                        values.add(school.schoolKey(getApplicationContext()));
                        values.add("1");
                        values.add(searchContent.getText().toString().trim());
                        values.add(getTime(timeS));
                        values.add(getTime(timeE));
                        values.add(mpage+"");
                        sendRequest("GET",school.SchoolNotice(),3,params,values);
                    }
                }).start();
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
                                    SchoolNotice mange=gson.fromJson(array.getJSONObject(i).toString(),SchoolNotice.class);
                                    data.add(mange);
                                }
                                handler.sendEmptyMessage(0000);
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
                    }else if(what==3){
                        if(object.getString("code").equals("200")){
                            JSONArray array=new JSONArray(object.getString("data"));
                            if(array.length()>0) {
                                for (int i = 0; i < array.length(); i++) {
                                    SchoolNotice mange = gson.fromJson(array.getJSONObject(i).toString(), SchoolNotice.class);
                                    data.add(mange);
                                }
                            }
                            handler.sendEmptyMessage(0000);
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



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.school_notice_notice_setStartT:
                //时间选择器
                com.kangzhan.student.utils.picker.DatePicker picker=new com.kangzhan.student.utils.picker.DatePicker(School_Notice_NoticeActivity.this);
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
            case R.id.school_notice_notice_setEndT:
                com.kangzhan.student.utils.picker.DatePicker picker1=new com.kangzhan.student.utils.picker.DatePicker(School_Notice_NoticeActivity.this);
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
            case R.id.school_notice_notice_Tosearch:
                //搜索按钮
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        params.clear();
                        values.clear();
                        //参数
                        params.add("key");
                        params.add("export");
                        params.add("searchCriteria");
                        params.add("start_date");
                        params.add("end_date");
                        //值
                        values.add(school.schoolKey(getApplicationContext()));
                        values.add("1");
                        values.add(searchContent.getText().toString().trim());
                        values.add(getTime(timeS));
                        values.add(getTime(timeE));
                        sendRequest("GET",school.SchoolNotice(),1,params,values);
                    }
                }).start();
                break;
            case R.id.item_school_notice_edit_add:
                popupMenu.dismiss();
                Intent add=new Intent(this,School_add_notice.class);
                startActivity(add);
                break;
            case R.id.item_school_notice_edit_dele:
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
                        params.add("batch_ids");
                        values.add(school.schoolKey(getApplicationContext()));
                        values.add(builder.toString());
                        sendRequest("POST",school.SchoolNoticeDele(),2,params,values);
                    }
                }).start();

                break;
            default:
                break;
        }
    }



    private String getTime(TextView edit){
        String getTime=edit.getText().toString().trim();
        getTime=getTime.replace("年","-");
        getTime=getTime.replace("月","-");
        getTime=getTime.replace("日","");
        return getTime;
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
