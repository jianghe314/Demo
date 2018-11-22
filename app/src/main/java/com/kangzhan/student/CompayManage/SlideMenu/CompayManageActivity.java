package com.kangzhan.student.CompayManage.SlideMenu;

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
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.google.gson.Gson;
import com.holenzhou.pullrecyclerview.PullRecyclerView;
import com.holenzhou.pullrecyclerview.layoutmanager.XLinearLayoutManager;
import com.kangzhan.student.CompayManage.Adapter.InfoStuManageAdapter;
import com.kangzhan.student.CompayManage.Adapter.StaffAdapter;
import com.kangzhan.student.CompayManage.Bean.InfoStuManage;
import com.kangzhan.student.CompayManage.Bean.StaffManage;
import com.kangzhan.student.CompayManage.InfoManage.CompayStuManageActivity;
import com.kangzhan.student.Debug.mLog;
import com.kangzhan.student.R;
import com.kangzhan.student.ShowUI.mToast;
import com.kangzhan.student.ShowUI.showMessage;
import com.kangzhan.student.ShowUI.showProgress;
import com.kangzhan.student.com.BaseActivity;
import com.kangzhan.student.mInterface.CompayInterface.CompayInterface;
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

public class CompayManageActivity extends BaseActivity implements View.OnClickListener {
    private PopupWindow popupMenu;
    private LinearLayout menu1,menu2;
    private boolean isShow;
    private Gson gson;
    private PullRecyclerView recycler;
    private StaffAdapter adapter;
    private ArrayList<String> params=new ArrayList<>();
    private ArrayList<String> values=new ArrayList<>();
    private ArrayList<StaffManage> mdata=new ArrayList<>();
    private String mmsg;
    private int mpage=1;
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
                            showProgress.showProgress(CompayManageActivity.this,"删除中...");
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
                            showMessage.showMsg(CompayManageActivity.this,"网络连接异常，请检查网络连接");
                        }
                    });
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compay_manage);
        Toolbar toolbar= (Toolbar) findViewById(R.id.compay_slide_manage_toolbar);
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
        recycler= (PullRecyclerView) findViewById(R.id.compay_slide_manage_recycler);
    }
    private void initData() {
        adapter=new StaffAdapter(CompayManageActivity.this,R.layout.item_compay_slide_manage,mdata);
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
                        values.add(CompayInterface.CompayKey(getApplicationContext()));
                        sendRequest("GET",CompayInterface.StaffManage(),1,params,values);
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
                        params.add("page");
                        values.add(CompayInterface.CompayKey(getApplicationContext()));
                        values.add(mpage+"");
                        sendRequest("GET",CompayInterface.StaffManage(),3,params,values);
                    }
                }).start();
            }
        });
        recycler.postRefreshing();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.item_compay_manage_info_menu1:
                popupMenu.dismiss();
                isShow=false;
                StringBuilder builder=new StringBuilder();
                for (int i = 0; i < mdata.size(); i++) {
                    if (mdata.get(i).isClick()){
                        builder.append(mdata.get(i).getId());
                    }
                }
                if(builder.toString().length()>0){
                    Intent psd=new Intent(this,StaffChangePsdActivity.class);
                    psd.putExtra("Id",builder.toString());
                    startActivity(psd);
                }else {
                    mToast.showText(getApplicationContext(),"请选择对象！");
                }
                break;
            /*
            case R.id.item_compay_manage_info_menu2:
                popupMenu.dismiss();
                isShow=false;
                break;
                */
        }
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
                                    StaffManage info=gson.fromJson(array.getJSONObject(i).toString(),StaffManage.class);
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
                                    StaffManage info = gson.fromJson(array.getJSONObject(i).toString(), StaffManage.class);
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



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.teacher_mydata_edit,menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.teacher_mydata_edit){
            //创建菜单
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
        View menu= LayoutInflater.from(this).inflate(R.layout.item_compay_manage_menu,null);
        menu1= (LinearLayout) menu.findViewById(R.id.item_compay_manage_info_menu1);
        //menu2= (LinearLayout) menu.findViewById(R.id.item_compay_manage_info_menu2);
        menu1.setOnClickListener(this);
        //menu2.setOnClickListener(this);
        popupMenu=new PopupWindow(menu, ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        popupMenu.setAnimationStyle(R.style.BottomMenu);
        popupMenu.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this,R.color.textColor_whit)));
        popupMenu.showAtLocation(getLayoutInflater().inflate(R.layout.activity_compay__account_info,null), Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL,0,0);
    }

}
