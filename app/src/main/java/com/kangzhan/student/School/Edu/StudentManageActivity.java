package com.kangzhan.student.School.Edu;

import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.holenzhou.pullrecyclerview.PullRecyclerView;
import com.holenzhou.pullrecyclerview.layoutmanager.XLinearLayoutManager;
import com.kangzhan.student.Debug.mLog;
import com.kangzhan.student.R;
import com.kangzhan.student.School.Adapter.EduStuManageAdapter;
import com.kangzhan.student.School.Bean.EduStuMange;
import com.kangzhan.student.ShowUI.mToast;
import com.kangzhan.student.ShowUI.showMessage;
import com.kangzhan.student.ShowUI.showProgress;
import com.kangzhan.student.Student.mutils.DividerItemDecoration;
import com.kangzhan.student.com.BaseActivity;
import com.kangzhan.student.mInterface.SchoolInterface.school;
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

import static com.kangzhan.student.utils.framework.popup.BasicPopup.MATCH_PARENT;
import static com.kangzhan.student.utils.framework.popup.BasicPopup.WRAP_CONTENT;

public class StudentManageActivity extends BaseActivity implements View.OnClickListener{
    private LinearLayout menu1,menu2,menu3,menu4;
    private TextView timeS,timeE,doSearch;
    private EditText searchContent;
    private String mmsg;
    private boolean isShow=false;
    private PopupWindow popupMenu;
    private PullRecyclerView recyclerView;
    private EduStuManageAdapter adapter;
    private ArrayList<EduStuMange> data=new ArrayList<>();
    //网络请求参数
    private ArrayList<String> params=new ArrayList<>();
    private ArrayList<String> values=new ArrayList<>();
    private int[] mcalender= mCalender.getCalender();
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
                            mToast.showText(getApplicationContext(),mmsg);
                        }
                    });
                    break;
                case 2222:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.showProgress(StudentManageActivity.this,"删除中...");
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
                            recyclerView.stopLoadMore();
                            showMessage.showMsg(StudentManageActivity.this,"网络连接异常，请检查网络连接");
                        }
                    });
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_manage);
        Toolbar toolbar= (Toolbar) findViewById(R.id.school_edu_stuManage_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        gson=new Gson();
        initView();
        iniData();
    }

    private void initView() {
        recyclerView= (PullRecyclerView) findViewById(R.id.school_edu_stuM_recycler);
        timeS= (TextView) findViewById(R.id.school_edu_stuM_setStartT);
        timeS.setText(mcalender[0]+"年"+mcalender[1]+"月"+mcalender[2]+"日");
        timeE= (TextView) findViewById(R.id.school_edu_stuM_setEndT);
        timeE.setText(mcalender[0]+"年"+mcalender[1]+"月"+mcalender[2]+"日");
        doSearch= (TextView) findViewById(R.id.teacher_train_Tosearch);
        searchContent= (EditText) findViewById(R.id.teacher_train_search_content);
        doSearch.setOnClickListener(this);
        timeE.setOnClickListener(this);
        timeS.setOnClickListener(this);
    }
    private void iniData() {
        adapter=new EduStuManageAdapter(StudentManageActivity.this,R.layout.item_school_edu_stu_manage,data);
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
                        values.add("");
                        values.add(getTime(timeS));
                        values.add(getTime(timeE));
                        sendRequest("GET",school.EduStuManage(),1,params,values);
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
                        values.add("");
                        values.add(getTime(timeS));
                        values.add(getTime(timeE));
                        values.add(mpage+"");
                        sendRequest("GET",school.EduStuManage(),3,params,values);
                    }
                }).start();
            }
        });
        recyclerView.postRefreshing();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.school_edu_stuM_setStartT:
                //设置开始时间
                //时间选择器
                com.kangzhan.student.utils.picker.DatePicker picker=new com.kangzhan.student.utils.picker.DatePicker(StudentManageActivity.this);
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
            case R.id.school_edu_stuM_setEndT:
                //设置结束时间
                //时间选择器
                com.kangzhan.student.utils.picker.DatePicker picker1=new com.kangzhan.student.utils.picker.DatePicker(StudentManageActivity.this);
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
            case R.id.teacher_train_Tosearch:
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
                        sendRequest("GET",school.EduStuManage(),1,params,values);
                    }
                }).start();
                break;
            case R.id.item_school_stu_manage_menu1:
                //分配教练
                StringBuilder builder=new StringBuilder();
                for (int i = 0; i <data.size(); i++) {
                    if(data.get(i).isClick()){
                        builder.append(data.get(i).getId());
                        builder.append(",");
                    }
                }
                Intent allocT=new Intent(StudentManageActivity.this,AllocationTeacherActivity.class);
                allocT.putExtra("stuId",builder.toString());
                startActivity(allocT);
                popupMenu.dismiss();
                isShow=false;
                break;
           /* case R.id.item_school_stu_manage_menu2:
                //没有界面
                popupMenu.dismiss();
                isShow=false;
                break;*/
            case R.id.item_school_stu_manage_menu3:
                StringBuilder builder3=new StringBuilder();
                for (int i = 0; i <data.size(); i++) {
                    if(data.get(i).isClick()){
                        builder3.append(data.get(i).getId());
                        builder3.append(",");
                    }
                }
                Intent setClass=new Intent(StudentManageActivity.this,EduSetClassActivity.class);
                setClass.putExtra("stuId",builder3.toString());
                startActivity(setClass);
                popupMenu.dismiss();
                isShow=false;
                break;
            case R.id.item_school_stu_manage_menu4:
                //删除学员
                final StringBuilder builder4=new StringBuilder();
                for (int i = 0; i <data.size(); i++) {
                    if(data.get(i).isClick()){
                        builder4.append(data.get(i).getId());
                        builder4.append(",");
                    }
                }
                handler.sendEmptyMessage(2222);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        params.clear();
                        values.clear();
                        //
                        params.add("key");
                        params.add("student_ids");
                        values.add(school.schoolKey(getApplicationContext()));
                        values.add(builder4.toString());
                        sendRequest("POST",school.EduStMdele(),2,params,values);
                    }
                }).start();
                popupMenu.dismiss();
                isShow=false;
                break;
        }
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
                    mLog.e("reponse","->"+response.toString());
                    mmsg=object.getString("msg");
                    if(what==1){
                        if(object.getString("code").equals("200")){
                            JSONArray array=new JSONArray(object.getString("data"));
                            if(array.length()>0){
                                data.clear();
                                for (int i = 0; i < array.length(); i++) {
                                    EduStuMange mange=gson.fromJson(array.getJSONObject(i).toString(),EduStuMange.class);
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
                                    EduStuMange mange = gson.fromJson(array.getJSONObject(i).toString(), EduStuMange.class);
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
        //实现 PopupWindow
        View root= LayoutInflater.from(this).inflate(R.layout.item_school_edu_stu_manage_menu,null);
        menu1= (LinearLayout) root.findViewById(R.id.item_school_stu_manage_menu1);
        //menu2= (LinearLayout) root.findViewById(R.id.item_school_stu_manage_menu2);
        menu3= (LinearLayout) root.findViewById(R.id.item_school_stu_manage_menu3);
        menu4= (LinearLayout) root.findViewById(R.id.item_school_stu_manage_menu4);
        menu1.setOnClickListener(this);
        //menu2.setOnClickListener(this);
        menu3.setOnClickListener(this);
        menu4.setOnClickListener(this);
        popupMenu=new PopupWindow(root,MATCH_PARENT,WRAP_CONTENT);
        popupMenu.setAnimationStyle(R.style.BottomMenu);
        popupMenu.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this,R.color.textColor_whit)));
        popupMenu.showAtLocation(getLayoutInflater().inflate(R.layout.activity_student_manage,null),Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL,0,0);
    }

    private String getTime(TextView edit){
        String getTime=edit.getText().toString().trim();
        getTime=getTime.replace("年","-");
        getTime=getTime.replace("月","-");
        getTime=getTime.replace("日","");
        return getTime;
    }


}
