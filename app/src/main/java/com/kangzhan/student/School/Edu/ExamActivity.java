package com.kangzhan.student.School.Edu;

import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.BottomSheetDialog;
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
import com.kangzhan.student.Debug.mLog;
import com.kangzhan.student.R;
import com.kangzhan.student.School.Adapter.EduTestAdapter;
import com.kangzhan.student.School.Bean.EduTest;
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

public class ExamActivity extends BaseActivity implements View.OnClickListener {
    private TextView startT,endT,seacher;
    private EditText searchContent;
    private PullRecyclerView recycler;
    private int[] mcalender= mCalender.getCalender();
    private EduTestAdapter adapter;
    private ArrayList<EduTest> data=new ArrayList<>();
    private Gson gson;
    private String mmsg;
    private int mpage=1;
    private PopupWindow popupMenu;
    private boolean isShow=false;
    private ArrayList<String> params=new ArrayList<>();
    private ArrayList<String> values=new ArrayList<>();
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1111:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            recycler.stopRefresh();
                            recycler.stopLoadMore();
                            adapter.notifyDataSetChanged();
                        }
                    });
                    break;
                case 2222:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            recycler.stopRefresh();
                            adapter.notifyDataSetChanged();
                            mToast.showText(getApplicationContext(),mmsg);
                        }
                    });
                    break;
                case 3333:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.showProgress(ExamActivity.this,"删除中...");
                        }
                    });
                    break;
                case 4444:
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            params.clear();
                            values.clear();
                            params.add("key");
                            params.add("scoretype");
                            values.add(school.schoolKey(getApplicationContext()));
                            values.add("2");
                            sendRequest("GET",1,school.EduTestList(),params,values);
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
                            recycler.stopLoadMore();
                            mToast.showText(getApplicationContext(),"没有更多了！");
                        }
                    });
                    break;
                case 9999:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showMessage.showMsg(ExamActivity.this,"网络连接异常，请检查网络连接");
                        }
                    });
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam2);
        Toolbar toolbar= (Toolbar) findViewById(R.id.school_edu_exam_toolbar);
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
        startT= (TextView) findViewById(R.id.school_exam_StartT);
        endT= (TextView) findViewById(R.id.school_exam_endT);
        seacher= (TextView) findViewById(R.id.school_exam__Tosearch);
        seacher.setOnClickListener(this);
        startT.setOnClickListener(this);
        endT.setOnClickListener(this);
        searchContent= (EditText) findViewById(R.id.school_edu_exam_search_content);
        recycler= (PullRecyclerView) findViewById(R.id.school_edu_exam_recycler);
        startT.setText(mcalender[0]+"年"+mcalender[1]+"月"+mcalender[2]+"日");
        endT.setText(mcalender[0]+"年"+mcalender[1]+"月"+mcalender[2]+"日");
    }

    private void initData() {
        adapter=new EduTestAdapter(ExamActivity.this,R.layout.item_school_edu_test,data);
        recycler.setAdapter(adapter);
        recycler.setEmptyView(LayoutInflater.from(this).inflate(R.layout.item_no_data,null));
        recycler.setLayoutManager(new XLinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
        recycler.setColorSchemeResources(R.color.swipe_color1,R.color.swipe_color2,R.color.swipe_color3);
        recycler.enablePullRefresh(true);       //设置可以下拉
        recycler.enableLoadMore(true);
        recycler.setOnRecyclerRefreshListener(new PullRecyclerView.OnRecyclerRefreshListener() {
            @Override
            public void onPullRefresh() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        params.clear();
                        values.clear();
                        params.add("key");
                        params.add("start_date");
                        params.add("end_date");
                        params.add("searchCriteria");
                        params.add("scoretype");
                        params.add("export");
                        //
                        values.add(school.schoolKey(getApplicationContext()));
                        values.add(getTime(startT));
                        values.add(getTime(endT));
                        values.add("");
                        values.add("2");
                        values.add("1");
                        sendRequest("GET",1,school.EduTestList(),params,values);
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
                        params.add("start_date");
                        params.add("end_date");
                        params.add("searchCriteria");
                        params.add("scoretype");
                        params.add("export");
                        params.add("page");
                        //
                        values.add(school.schoolKey(getApplicationContext()));
                        values.add(getTime(startT));
                        values.add(getTime(endT));
                        values.add("");
                        values.add("2");
                        values.add("1");
                        values.add(mpage+"");
                        sendRequest("GET",3,school.EduTestList(),params,values);
                    }
                }).start();
            }
        });
        recycler.postRefreshing();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.school_exam_StartT:
                com.kangzhan.student.utils.picker.DatePicker picker=new com.kangzhan.student.utils.picker.DatePicker(ExamActivity.this);
                picker.setRangeStart(2016,1,1);
                picker.setRangeEnd(2050,10,14);
                picker.setSelectedItem(mcalender[0],mcalender[1],mcalender[2]);
                picker.setOnDatePickListener(new com.kangzhan.student.utils.picker.DatePicker.OnYearMonthDayPickListener() {
                    @Override
                    public void onDatePicked(String year, String month, String day) {
                        startT.setText(year+"年"+month+"月"+day+"日");
                    }
                });
                picker.show();
                break;
            case R.id.school_exam_endT:
                com.kangzhan.student.utils.picker.DatePicker picker1=new com.kangzhan.student.utils.picker.DatePicker(ExamActivity.this);
                picker1.setRangeStart(2016,1,1);
                picker1.setRangeEnd(2050,10,14);
                picker1.setSelectedItem(mcalender[0],mcalender[1],mcalender[2]);
                picker1.setOnDatePickListener(new com.kangzhan.student.utils.picker.DatePicker.OnYearMonthDayPickListener() {
                    @Override
                    public void onDatePicked(String year, String month, String day) {
                        endT.setText(year+"年"+month+"月"+day+"日");
                    }
                });
                picker1.show();
                break;
            case R.id.school_exam__Tosearch:
                //搜索
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        params.clear();
                        values.clear();
                        params.add("key");
                        params.add("start_date");
                        params.add("end_date");
                        params.add("searchCriteria");
                        params.add("scoretype");
                        params.add("export");
                        //
                        values.add(school.schoolKey(getApplicationContext()));
                        values.add(getTime(startT));
                        values.add(getTime(endT));
                        values.add(searchContent.getText().toString().trim());
                        values.add("2");
                        values.add("1");
                        sendRequest("GET",1,school.EduTestList(),params,values);
                    }
                }).start();
                break;
            /*case R.id.item_school_edu_test_menu1:
                //发送消息

                break;*/
            case R.id.item_school_edu_test_menu2:
                //删除
                final StringBuilder builder=new StringBuilder();
                for (int i = 0; i <data.size(); i++) {
                    builder.append(data.get(i).getStu_id());
                    builder.append(",");
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        handler.sendEmptyMessage(3333);
                        params.clear();
                        values.clear();
                        params.add("key");
                        params.add("scoretype");
                        params.add("score_ids");
                        values.add(school.schoolKey(getApplicationContext()));
                        values.add("1");
                        values.add(builder.toString());
                        sendRequest("GET",2,school.EduTestDele(),params,values);
                    }
                }).start();
                popupMenu.dismiss();
                isShow=false;
                break;
            default:
                break;
        }
    }


    private void sendRequest(final String way, int what, String url, ArrayList<String> params, ArrayList<String> values) {
        RequestMethod method=null;
        if(way.equals("POST")){
            method=RequestMethod.POST;
        }else if(way.equals("GET")){
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
                    JSONObject objcet = new JSONObject(response.get().toString());
                    mLog.e("reponse","->"+response.toString());
                    mmsg=objcet.getString("msg");
                    if(what==1){
                        if(objcet.getString("code").equals("200")){
                            JSONArray array=new JSONArray(objcet.getString("data"));
                            if(array.length()>0){
                                data.clear();
                                for (int i = 0; i < array.length(); i++) {
                                    EduTest test=gson.fromJson(array.getJSONObject(i).toString(),EduTest.class);
                                    data.add(test);
                                }
                                handler.sendEmptyMessage(1111);
                            }else {
                                data.clear();
                                handler.sendEmptyMessage(2222);
                            }
                        }else {
                            data.clear();
                            handler.sendEmptyMessage(2222);
                        }
                    }else if(what==2){
                        handler.sendEmptyMessage(4444);
                    }else if(what==3){
                        if(objcet.getString("code").equals("200")){
                            JSONArray array=new JSONArray(objcet.getString("data"));
                            if(array.length()>0) {
                                for (int i = 0; i < array.length(); i++) {
                                    EduTest test = gson.fromJson(array.getJSONObject(i).toString(), EduTest.class);
                                    data.add(test);
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


    //点击学员成绩列表弹出一个BottomMenufragemnt查看学员成绩详情  item为：fragment_school_edu_test_detail

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.student_mydata_edit,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            case R.id.student_mydata_detail:
                if(isShow){
                    popupMenu.dismiss();
                    for (int i = 0; i <data.size() ; i++) {
                        data.get(i).setShow(false);
                    }
                    adapter.notifyDataSetChanged();
                    isShow=false;
                }else {
                    addMenu();
                    for (int i = 0; i <data.size() ; i++) {
                        data.get(i).setShow(true);
                    }
                    adapter.notifyDataSetChanged();
                    isShow=true;
                }
                break;
            default:
                break;
        }
        return true;
    }

    private void addMenu() {
        View view= getLayoutInflater().inflate(R.layout.item_school_edu_test_menu,null);
       // LinearLayout menu1= (LinearLayout) view.findViewById(R.id.item_school_edu_test_menu1);
        LinearLayout menu2= (LinearLayout) view.findViewById(R.id.item_school_edu_test_menu2);
        //menu1.setOnClickListener(this);
        menu2.setOnClickListener(this);
        popupMenu=new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        popupMenu.setAnimationStyle(R.style.BottomMenu);
        popupMenu.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this,R.color.textColor_whit)));
        popupMenu.showAtLocation(getLayoutInflater().inflate(R.layout.activity_student_manage,null), Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL,0,0);
        popupMenu.isShowing();
    }



    private String getTime(TextView edit){
        String getTime=edit.getText().toString().trim();
        getTime=getTime.replace("年","-");
        getTime=getTime.replace("月","-");
        getTime=getTime.replace("日","");
        return getTime;
    }

}
