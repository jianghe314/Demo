package com.kangzhan.student.CompayManage.SlideMenu;

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
import android.widget.TextView;

import com.google.gson.Gson;
import com.holenzhou.pullrecyclerview.PullRecyclerView;
import com.holenzhou.pullrecyclerview.layoutmanager.XLinearLayoutManager;
import com.kangzhan.student.CompayManage.Adapter.AccountInfoAdapter;
import com.kangzhan.student.CompayManage.Adapter.InfoTeaManageAdapter;
import com.kangzhan.student.CompayManage.Bean.InfoTeaManage;
import com.kangzhan.student.CompayManage.Bean.SchoolAccontInfo;
import com.kangzhan.student.CompayManage.InfoManage.CompayTeacManage;
import com.kangzhan.student.Debug.mLog;
import com.kangzhan.student.R;
import com.kangzhan.student.ShowUI.mToast;
import com.kangzhan.student.ShowUI.showMessage;
import com.kangzhan.student.ShowUI.showProgress;
import com.kangzhan.student.com.BaseActivity;
import com.kangzhan.student.entity.City;
import com.kangzhan.student.entity.County;
import com.kangzhan.student.entity.Province;
import com.kangzhan.student.mInterface.CompayInterface.CompayInterface;
import com.kangzhan.student.utils.AddressPickTask;
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

public class Compay_AccountInfo extends BaseActivity implements View.OnClickListener{
    private PopupWindow popupMenu;
    private LinearLayout menu1,menu2;
    private boolean isShow=false;
    private TextView choice,address;
    private PullRecyclerView recycler;
    private String mprovince,mcity,mcounty,mmsg;
    private Gson gson;
    private int mpage=1;
    private AccountInfoAdapter adapter;
    private ArrayList<String> params=new ArrayList<>();
    private ArrayList<String> values=new ArrayList<>();
    private ArrayList<SchoolAccontInfo> mdata=new ArrayList<>();
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
                            showProgress.showProgress(Compay_AccountInfo.this,"更改中...");
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
                            showMessage.showMsg(Compay_AccountInfo.this,"网络连接异常，请检查网络连接");
                        }
                    });
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compay__account_info);
        Toolbar toolbar= (Toolbar) findViewById(R.id.compay_slide_info_toolbar);
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
        recycler= (PullRecyclerView) findViewById(R.id.compay_slide_info_recycler);
       /* choice= (TextView) findViewById(R.id.compay_slide_info_choice);
        choice.setOnClickListener(this);
        address= (TextView) findViewById(R.id.compay_slide_info_address);*/
    }
    private void initData() {
        adapter=new AccountInfoAdapter(Compay_AccountInfo.this,R.layout.item_compay_acount_info,mdata);
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
                        sendRequest("GET",CompayInterface.SchoolAccountInfo(),1,params,values);
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
                        sendRequest("GET",CompayInterface.SchoolAccountInfo(),3,params,values);
                    }
                }).start();
            }
        });
        recycler.postRefreshing();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            /*case R.id.compay_slide_info_choice:
                AddressPickTask task=new AddressPickTask(this);
                task.setHideCounty(false);
                task.setHideProvince(false);
                task.setCallback(new AddressPickTask.Callback() {
                    @Override
                    public void onAddressInitFailed() {
                        mToast.showText(getApplicationContext(),"初始化数据失败");
                    }

                    @Override
                    public void onAddressPicked(Province province, City city, County county) {
                        if (county == null) {
                            address.setText(province.getAreaName()+city.getAreaName());

                        } else {
                            String str=(province.getAreaName().equals(city.getAreaName())?(province.getAreaName()+county.getAreaName()):(province.getAreaName()+city.getAreaName()+county.getAreaName()));
                            address.setText(str);
                            mprovince=province.getAreaId();
                            mcity=city.getAreaId();
                            mcounty=county.getAreaId();

                        }
                    }
                });
                task.execute("北京市", "北京市", "东城区");
                //地区选择完了加载数据

                break;*/
            case R.id.item_compay_account_info_menu1:
                //type  1：禁用   2：启用
                popupMenu.dismiss();
                isShow=false;
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
                            params.add("ids");
                            params.add("type");
                            values.add(CompayInterface.CompayKey(getApplicationContext()));
                            values.add(builder.toString().substring(0,builder.toString().length()-1));
                            values.add("1");
                            sendRequest("GET",CompayInterface.SchoolChangeStatus(),2,params,values);
                        }
                    }).start();
                }else {
                    mToast.showText(getApplicationContext(),"请选择对象");
                }
                break;
            case R.id.item_compay_account_info_menu2:
                popupMenu.dismiss();
                isShow=false;
                final StringBuilder builder2=new StringBuilder();
                for (int i = 0; i < mdata.size(); i++) {
                    if(mdata.get(i).isClick()){
                        builder2.append(mdata.get(i).getId());
                        builder2.append(",");
                    }
                }
                if(builder2.toString().length()>0){
                    handler.sendEmptyMessage(3333);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            params.clear();
                            values.clear();
                            params.add("key");
                            params.add("ids");
                            params.add("type");
                            values.add(CompayInterface.CompayKey(getApplicationContext()));
                            values.add(builder2.toString().substring(0,builder2.toString().length()-1));
                            values.add("2");
                            sendRequest("GET",CompayInterface.SchoolChangeStatus(),2,params,values);
                        }
                    }).start();
                }else {
                    mToast.showText(getApplicationContext(),"请选择对象");
                }
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
                                    SchoolAccontInfo order=gson.fromJson(array.getJSONObject(i).toString(),SchoolAccontInfo.class);
                                    mdata.add(order);
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
                                    SchoolAccontInfo order = gson.fromJson(array.getJSONObject(i).toString(), SchoolAccontInfo.class);
                                    mdata.add(order);
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
        View menu= LayoutInflater.from(this).inflate(R.layout.item_compay_slide_info_menu,null);
        menu1= (LinearLayout) menu.findViewById(R.id.item_compay_account_info_menu1);
        menu2= (LinearLayout) menu.findViewById(R.id.item_compay_account_info_menu2);
        menu1.setOnClickListener(this);
        menu2.setOnClickListener(this);
        popupMenu=new PopupWindow(menu,ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        popupMenu.setAnimationStyle(R.style.BottomMenu);
        popupMenu.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this,R.color.textColor_whit)));
        popupMenu.showAtLocation(getLayoutInflater().inflate(R.layout.activity_compay__account_info,null), Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL,0,0);
    }

}
