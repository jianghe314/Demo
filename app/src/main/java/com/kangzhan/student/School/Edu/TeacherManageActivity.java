package com.kangzhan.student.School.Edu;

import android.content.Intent;
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
import android.view.WindowManager;
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
import com.kangzhan.student.School.Adapter.EduTeaMangeAdapter;
import com.kangzhan.student.School.Bean.EduTeaManage;
import com.kangzhan.student.ShowUI.mToast;
import com.kangzhan.student.ShowUI.showMessage;
import com.kangzhan.student.ShowUI.showProgress;
import com.kangzhan.student.com.BaseActivity;
import com.kangzhan.student.mInterface.SchoolInterface.school;
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

public class TeacherManageActivity extends BaseActivity implements View.OnClickListener {
    private ArrayList<EduTeaManage> data=new ArrayList<>();
    private EduTeaMangeAdapter adapter;
    private Gson gson;
    private PullRecyclerView recycler;
    private EditText searcherContent;
    private TextView searcher;
    private String mmsg;
    private int mpage=1;
    private boolean isShow=false;
    private PopupWindow popupWindow;
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
                            showProgress.showProgress(TeacherManageActivity.this,"删除中...");
                        }
                    });
                    break;
                case 4444:
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
                            recycler.stopRefresh();
                            showMessage.showMsg(TeacherManageActivity.this,"网络连接异常，请检查网络连接");
                        }
                    });
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_manage);
        Toolbar toolbar= (Toolbar) findViewById(R.id.school_edu_stuManage_alloc_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        gson=new Gson();
        initView();

    }


    private void initView() {
        searcherContent= (EditText) findViewById(R.id.school_edu_TeaM_search_content);
        searcher= (TextView) findViewById(R.id.school_edu_teaM_Tosearch);
        searcher.setOnClickListener(this);
        adapter=new EduTeaMangeAdapter(TeacherManageActivity.this,R.layout.item_school_edu_tea_m,data);
        recycler= (PullRecyclerView) findViewById(R.id.school_edu_teacherM_recycler);
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
                        params.add("export");
                        params.add("searchCriteria");
                        values.add(school.schoolKey(getApplicationContext()));
                        values.add("1");
                        values.add("");
                        sendRequest("GET",1, school.EduTeaManage(),params,values);
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
                        params.add("export");
                        params.add("searchCriteria");
                        params.add("page");
                        values.add(school.schoolKey(getApplicationContext()));
                        values.add("1");
                        values.add("");
                        values.add(mpage+"");
                        sendRequest("GET",3, school.EduTeaManage(),params,values);
                    }
                }).start();
            }
        });
        recycler.postRefreshing();

    }

    private void sendRequest(String way,int what,String url,ArrayList<String> params,ArrayList<String> values) {
        RequestMethod method=null;
        if(way.equals("POST")){
            method=RequestMethod.POST;
        }else if(way.equals("GET")){
            method=RequestMethod.GET;
        }
        Request<JSONObject> request=NoHttp.createJsonObjectRequest(url,method);
        for (int i = 0; i < params.size(); i++) {
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
                                for (int i = 0; i <array.length(); i++) {
                                    EduTeaManage manage=gson.fromJson(array.getJSONObject(i).toString(),EduTeaManage.class);
                                    data.add(manage);
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
                        if(object.getString("code").equals("200")){
                            JSONArray array=new JSONArray(object.getString("data"));
                            if(array.length()>0) {
                                for (int i = 0; i < array.length(); i++) {
                                    EduTeaManage manage = gson.fromJson(array.getJSONObject(i).toString(), EduTeaManage.class);
                                    data.add(manage);
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


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.school_edu_teaM_Tosearch:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        params.clear();
                        values.clear();
                        params.add("key");
                        params.add("export");
                        params.add("searchCriteria");
                        values.add(school.schoolKey(getApplicationContext()));
                        values.add("1");
                        values.add(searcherContent.getText().toString().trim());
                        sendRequest("GET",1,school.EduTeaManage(),params,values);
                    }
                }).start();
                break;
            case R.id.item_school_edu_teac_m1:
                StringBuilder builder1=new StringBuilder();
                for (int i = 0; i < data.size(); i++) {
                    if(data.get(i).isClick()){
                        builder1.append(data.get(i).getId());
                        builder1.append(",");
                    }
                }
                Intent setLesson=new Intent(this,TeacherM_SetLessonActivity.class);
                setLesson.putExtra("teacherId",builder1.toString());
                startActivity(setLesson);
                popupWindow.dismiss();
                isShow=false;
                break;
            /*case R.id.item_school_edu_teac_m2:
                //发信息，没有搞
                popupWindow.dismiss();
                isShow=false;
                break;*/
            case R.id.item_school_edu_teac_m3:
                final StringBuilder builder3=new StringBuilder();
                for (int i = 0; i <data.size(); i++) {
                    if(data.get(i).isClick()){
                        builder3.append(data.get(i).getId());
                        builder3.append(",");
                    }
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        handler.sendEmptyMessage(3333);
                        params.clear();
                        values.clear();
                        params.add("key");
                        params.add("coach_ids");
                        values.add(school.schoolKey(getApplicationContext()));
                        values.add(builder3.toString());
                        sendRequest("POST",2,school.EduTeaManageDele(),params,values);
                    }
                }).start();
                popupWindow.dismiss();
                isShow=false;
                break;
        }
    }


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
                    popupWindow.dismiss();
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
        }
        return true;
    }

    private void addMenu() {
        View view= LayoutInflater.from(TeacherManageActivity.this).inflate(R.layout.item_school_edu_teac_m_menu,null);
        LinearLayout m1= (LinearLayout) view.findViewById(R.id.item_school_edu_teac_m1);
        //LinearLayout m2= (LinearLayout) view.findViewById(R.id.item_school_edu_teac_m2);
        LinearLayout m3= (LinearLayout) view.findViewById(R.id.item_school_edu_teac_m3);
        m1.setOnClickListener(this);
        //m2.setOnClickListener(this);
        m3.setOnClickListener(this);
        popupWindow=new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setAnimationStyle(R.style.BottomMenu);
        popupWindow.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this,R.color.textColor_whit)));
        popupWindow.showAtLocation(getLayoutInflater().inflate(R.layout.activity_student_manage,null), Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL,0,0);
    }

}
