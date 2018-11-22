package com.kangzhan.student.School.Edu;

import android.content.Context;
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
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.holenzhou.pullrecyclerview.PullRecyclerView;
import com.holenzhou.pullrecyclerview.layoutmanager.XLinearLayoutManager;
import com.kangzhan.student.Debug.mLog;
import com.kangzhan.student.R;
import com.kangzhan.student.School.Adapter.EduTeacherRestListAdapter;
import com.kangzhan.student.School.Bean.EduTeacherRestList;
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

import static com.kangzhan.student.utils.framework.popup.BasicPopup.MATCH_PARENT;
import static com.kangzhan.student.utils.framework.popup.BasicPopup.WRAP_CONTENT;

public class EduTeacherRestListActivity extends BaseActivity implements View.OnClickListener{
    //搜索没有反应
    private LinearLayout menu1,menu2,menu3;
    private PopupWindow popupMenu;
    private PullRecyclerView recycler;
    private EditText searchContent;
    private TextView search;
    private boolean isShow=false;
    private EduTeacherRestListAdapter adapter;
    private ArrayList<EduTeacherRestList> data=new ArrayList<>();
    private Gson gson;
    private String mmsg;
    private int mpage=1;
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
                            showProgress.showProgress(EduTeacherRestListActivity.this,"删除中...");
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
                            recycler.stopLoadMore();
                            showMessage.showMsg(EduTeacherRestListActivity.this,"网络连接异常，请检查网络连接");
                        }
                    });
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edu_teacher_rest_list);
        Toolbar toolbar= (Toolbar) findViewById(R.id.teacher_restlist_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        gson=new Gson();
        initView();
    }

    private void initView() {
        searchContent= (EditText) findViewById(R.id.school_teacher_rest_search_content);
        search= (TextView) findViewById(R.id.school_teacher_rest_Tosearch);
        search.setOnClickListener(this);
        adapter=new EduTeacherRestListAdapter(this,R.layout.item_school_edu_teacher_restlist,data);
        recycler= (PullRecyclerView) findViewById(R.id.school_edu_teacherRestList_recycler);
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
                        values.add(searchContent.getText().toString().trim());
                        sendRequest("GET",1, school.EduTeaherRestList(),params,values);
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
                        values.add(searchContent.getText().toString().trim());
                        values.add(mpage+"");
                        sendRequest("GET",3, school.EduTeaherRestList(),params,values);
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
                    mmsg=objcet.getString("msg");
                    mLog.e("reponse","-->"+response.get().toString());
                    if(what==1){
                        if(objcet.getString("code").equals("200")){
                            JSONArray array=new JSONArray(objcet.getString("data"));
                            if(array.length()>0){
                                data.clear();
                                for (int i = 0; i < array.length(); i++) {
                                    EduTeacherRestList list=gson.fromJson(array.getJSONObject(i).toString(),EduTeacherRestList.class);
                                    data.add(list);
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
                                    EduTeacherRestList list = gson.fromJson(array.getJSONObject(i).toString(), EduTeacherRestList.class);
                                    data.add(list);
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
            case R.id.item_school_teacher_rest_menu1:
                Intent add=new Intent(this,AddTeacherRestListActivity.class);
                startActivity(add);
                popupMenu.dismiss();
                isShow=false;
                break;
           /* case R.id.item_school_teacher_rest_menu2:
                //发送消息
                popupMenu.dismiss();
                isShow=false;
                break;*/
            case R.id.item_school_teacher_rest_menu3:
                //删除
                final StringBuilder builder=new StringBuilder();
                for (int i = 0; i < data.size(); i++) {
                    if(data.get(i).isClick()){
                        builder.append(data.get(i).getId());
                        builder.append(",");
                    }
                }
                handler.sendEmptyMessage(3333);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        params.clear();
                        values.clear();
                        params.add("key");
                        params.add("vacation_ids");
                        values.add(school.schoolKey(getApplicationContext()));
                        values.add(builder.toString());
                        sendRequest("POST",2,school.EduCancelRest(),params,values);
                    }
                }).start();
                popupMenu.dismiss();
                isShow=false;
                break;
            case R.id.school_teacher_rest_Tosearch:
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
                        values.add(searchContent.getText().toString().trim());
                        sendRequest("GET",1, school.EduTeaherRestList(),params,values);
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
        View root= LayoutInflater.from(this).inflate(R.layout.item_school_edu_teacherrest_menu,null);
        menu1= (LinearLayout) root.findViewById(R.id.item_school_teacher_rest_menu1);
        //menu2= (LinearLayout) root.findViewById(R.id.item_school_teacher_rest_menu2);
        menu3= (LinearLayout) root.findViewById(R.id.item_school_teacher_rest_menu3);
        menu1.setOnClickListener(this);
        //menu2.setOnClickListener(this);
        menu3.setOnClickListener(this);
        popupMenu=new PopupWindow(root,MATCH_PARENT,WRAP_CONTENT);
        popupMenu.setAnimationStyle(R.style.BottomMenu);
        popupMenu.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this,R.color.textColor_whit)));
        popupMenu.showAtLocation(getLayoutInflater().inflate(R.layout.activity_student_manage,null), Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL,0,0);
        popupMenu.isShowing();
    }

}
