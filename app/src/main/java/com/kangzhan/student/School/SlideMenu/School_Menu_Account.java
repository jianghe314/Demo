package com.kangzhan.student.School.SlideMenu;

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
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.google.gson.Gson;
import com.holenzhou.pullrecyclerview.PullRecyclerView;
import com.holenzhou.pullrecyclerview.layoutmanager.XLinearLayoutManager;
import com.kangzhan.student.Debug.mLog;
import com.kangzhan.student.R;
import com.kangzhan.student.School.Adapter.AccountManageAdapter;
import com.kangzhan.student.School.Bean.AccountManage;
import com.kangzhan.student.School_Login;
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

public class School_Menu_Account extends BaseActivity implements View.OnClickListener{
    private PullRecyclerView recyclerView;
    private AccountManageAdapter adapter;
    private ArrayList<AccountManage> data=new ArrayList<>();
    private String mmsg;
    private Gson gson;
    private boolean isShow=false;
    private PopupWindow popupMenu;
    private ArrayList<String> params=new ArrayList<>();
    private ArrayList<String> values=new ArrayList<>();
    private LinearLayout addAccount;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1111:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            recyclerView.stopRefresh();
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
                            showProgress.showProgress(School_Menu_Account.this,"删除中...");
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
                case 9999:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            recyclerView.stopRefresh();
                            showProgress.closeProgress();
                            showMessage.showMsg(School_Menu_Account.this,"网络连接异常，请检查网络连接");
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
        setContentView(R.layout.activity_school__menu__account);
        Toolbar toolbar= (Toolbar) findViewById(R.id.school_menu_account_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        gson=new Gson();
        initView();
    }

    private void initView() {
        addAccount= (LinearLayout) findViewById(R.id.school_menu_account_add);
        addAccount.setOnClickListener(this);
        adapter=new AccountManageAdapter(School_Menu_Account.this,R.layout.item_school_account_manage,data);
        recyclerView= (PullRecyclerView) findViewById(R.id.school_account_manage_recycler);
        recyclerView.setAdapter(adapter);
        recyclerView.setEmptyView(LayoutInflater.from(this).inflate(R.layout.item_no_data,null));
        recyclerView.setLayoutManager(new XLinearLayoutManager(School_Menu_Account.this, LinearLayoutManager.VERTICAL,false));
        recyclerView.setColorSchemeResources(R.color.swipe_color1,R.color.swipe_color2,R.color.swipe_color3);
        recyclerView.setOnRecyclerRefreshListener(new PullRecyclerView.OnRecyclerRefreshListener() {
            @Override
            public void onPullRefresh() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        params.clear();
                        values.clear();
                        params.add("key");
                        values.add(school.schoolKey(getApplicationContext()));
                        sendRequest("GET",1,school.AccountMange(),params,values);
                    }
                }).start();
            }

            @Override
            public void onLoadMore() {
                recyclerView.stopLoadMore();
            }
        });
        recyclerView.postRefreshing();
    }

    private void sendRequest(String way, int what, String url, ArrayList<String> params, ArrayList<String> values) {
        RequestMethod method=null;
        if(way.equals("POST")){
            method=RequestMethod.POST;
        }else if(way.equals("GET")){
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
                                    AccountManage manage=gson.fromJson(array.getJSONObject(i).toString(),AccountManage.class);
                                    data.add(manage);
                                }
                                handler.sendEmptyMessage(1111);
                            }else {
                                data.clear();
                                handler.sendEmptyMessage(2222);
                            }
                        }else {
                            handler.sendEmptyMessage(2222);
                        }
                    }else if(what==2){
                        handler.sendEmptyMessage(4444);
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
            case R.id.item_school_prohibit_menu:
                //禁用
                handler.sendEmptyMessage(3333);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        StringBuilder builder=new StringBuilder();
                        for (int i = 0; i <data.size(); i++) {
                            if(data.get(i).isClick()){
                                builder.append(data.get(i).getId());
                                builder.append(",");
                            }
                        }
                        params.clear();
                        values.clear();
                        params.add("key");
                        params.add("mgr_ids");
                        values.add(school.schoolKey(getApplicationContext()));
                        values.add(builder.toString().substring(0,builder.toString().length()-1));
                        sendRequest("POST",2,school.AccountProhibit(),params,values);
                    }
                }).start();
                break;
            case R.id.school_menu_account_add:
                Intent add=new Intent(School_Menu_Account.this,Add_Account.class);
                startActivity(add);
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
                        data.get(i).setVisible(false);
                    }
                    adapter.notifyDataSetChanged();
                    isShow=false;
                }else {
                    addMenu();
                    for (int i = 0; i <data.size() ; i++) {
                        data.get(i).setVisible(true);
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
        View menu= LayoutInflater.from(School_Menu_Account.this).inflate(R.layout.item_school_prohibit_menu,null);
        menu.findViewById(R.id.item_school_prohibit_menu).setOnClickListener(this);
        popupMenu=new PopupWindow(menu, ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        popupMenu.setAnimationStyle(R.style.BottomMenu);
        popupMenu.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this,R.color.textColor_whit)));
        popupMenu.showAtLocation(getLayoutInflater().inflate(R.layout.activity_student_manage,null), Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL,0,0);
    }

}
