package com.kangzhan.student.CompayManage.SelfRegistManage;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.holenzhou.pullrecyclerview.PullRecyclerView;
import com.holenzhou.pullrecyclerview.layoutmanager.XLinearLayoutManager;
import com.kangzhan.student.CompayManage.Adapter.SelfRegStuAdapter;
import com.kangzhan.student.CompayManage.Adapter.SelfRegTeaAdapter;
import com.kangzhan.student.CompayManage.Bean.SelfRegStuM;
import com.kangzhan.student.CompayManage.Bean.SelfRegTeaM;
import com.kangzhan.student.CompayManage.ChoiceClerkActivity;
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

import static com.kangzhan.student.utils.framework.popup.BasicPopup.MATCH_PARENT;
import static com.kangzhan.student.utils.framework.popup.BasicPopup.WRAP_CONTENT;

public class TeacherManageActivity extends BaseActivity implements View.OnClickListener{
    private TextView startT,endT,doSearch;
    private EditText searchContent;
    private int[] mcalender= mCalender.getCalender();
    private PullRecyclerView recycler;
    private boolean isShow=false;
    private PopupWindow popupMenu;
    private LinearLayout menu1,menu2,menu3;
    private Gson gson;
    private String mmsg;
    private int mpage=1;
    private SelfRegTeaAdapter adapter;
    private ArrayList<String> params=new ArrayList<>();
    private ArrayList<String> values=new ArrayList<>();
    private ArrayList<SelfRegTeaM> mdata=new ArrayList<>();
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
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            params.clear();
                            values.clear();
                            params.add("key");
                            params.add("type");
                            values.add(CompayInterface.CompayKey(getApplicationContext()));
                            values.add("2");
                            sendRequest("GET",CompayInterface.SelfRegStu(),1,params,values);
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
        setContentView(R.layout.activity_compay_teacher_manage);
        Toolbar toolbar= (Toolbar) findViewById(R.id.self_regist_teacher_list_toolbar);
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
        startT= (TextView) findViewById(R.id.self_regist_teacher_setStartT);
        endT= (TextView) findViewById(R.id.self_regist_teacher_setEndT);
        doSearch= (TextView) findViewById(R.id.self_regist_teacher_Tosearch);
        searchContent= (EditText) findViewById(R.id.self_regist_teacher_search_content);
        recycler= (PullRecyclerView) findViewById(R.id.self_regist_teacher_recycler);
        startT.setText(mcalender[0]+"年"+mcalender[1]+"月"+mcalender[2]+"日");
        endT.setText(mcalender[0]+"年"+mcalender[1]+"月"+mcalender[2]+"日");
        startT.setOnClickListener(this);
        endT.setOnClickListener(this);
        doSearch.setOnClickListener(this);
    }

    private void initData() {
        adapter=new SelfRegTeaAdapter(this,R.layout.item_compay_self_regist,mdata);
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
                        params.add("type");
                        values.add(CompayInterface.CompayKey(getApplicationContext()));
                        values.add("2");
                        sendRequest("GET",CompayInterface.SelfRegStu(),1,params,values);
                    }
                }).start();
            }

            @Override
            public void onLoadMore() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        mpage=mpage+1;
                        mLog.e("Mpage","-->"+mpage);
                        params.clear();
                        values.clear();
                        params.add("key");
                        params.add("page");
                        values.add(CompayInterface.CompayKey(getApplicationContext()));
                        values.add(mpage+"");
                        sendRequest("GET",CompayInterface.AdviseManage(),3,params,values);
                    }
                }).start();
            }
        });
        recycler.postRefreshing();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.self_regist_teacher_setStartT:
                com.kangzhan.student.utils.picker.DatePicker picker=new com.kangzhan.student.utils.picker.DatePicker(TeacherManageActivity.this);
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
            case R.id.self_regist_teacher_setEndT:
                com.kangzhan.student.utils.picker.DatePicker picker1=new com.kangzhan.student.utils.picker.DatePicker(TeacherManageActivity.this);
                picker1.setRangeStart(2016,1,1);
                picker1.setRangeEnd(2050,10,14);
                picker1.setSelectedItem(mcalender[0],mcalender[1],mcalender[2]);
                picker1.setOnDatePickListener(new com.kangzhan.student.utils.picker.DatePicker.OnYearMonthDayPickListener() {
                    @Override
                    public void onDatePicked(String year, String month, String day) {
                        endT.setText(year+"年"+month+"月"+day+"日");
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                params.clear();
                                values.clear();
                                params.add("key");
                                params.add("type");
                                params.add("signStartTime");
                                params.add("signEndtTime");
                                params.add("searchCriteria");
                                values.add(CompayInterface.CompayKey(getApplicationContext()));
                                values.add("2");
                                values.add(getTime(startT));
                                values.add(getTime(endT));
                                values.add(searchContent.getText().toString().trim());
                                sendRequest("GET",CompayInterface.SelfRegStu(),1,params,values);
                            }
                        }).start();
                    }
                });
                picker1.show();
                break;
            case R.id.self_regist_teacher_Tosearch:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                params.clear();
                                values.clear();
                                params.add("key");
                                params.add("type");
                                params.add("signStartTime");
                                params.add("signEndtTime");
                                params.add("searchCriteria");
                                values.add(CompayInterface.CompayKey(getApplicationContext()));
                                values.add("2");
                                values.add(getTime(startT));
                                values.add(getTime(endT));
                                values.add(searchContent.getText().toString().trim());
                                sendRequest("GET",CompayInterface.SelfRegStu(),1,params,values);
                            }
                        }).start();
                    }
                }).start();
                break;
            case R.id.item_compay_self_regist_menu1:
                //分配教练
                popupMenu.dismiss();
                StringBuilder builder=new StringBuilder();
                for (int i = 0; i < mdata.size(); i++) {
                    if(mdata.get(i).isClick()){
                        builder.append(mdata.get(i).getId());
                        builder.append(",");
                    }
                }
                if(builder.toString().length()>0){
                    String str=builder.toString().substring(0,builder.toString().length()-1);
                    Intent alloc=new Intent(this, ChoiceClerkActivity.class);
                    alloc.putExtra("Id",str);
                    alloc.putExtra("who","teacher");
                    startActivity(alloc);
                }else {
                    mToast.showText(getApplicationContext(),"请选择对象");
                }

                break;
            case R.id.item_compay_self_regist_menu2:
                popupMenu.dismiss();
                for (int i = 0; i <mdata.size() ; i++) {
                    mdata.get(i).setClick(true);
                }
                adapter.notifyDataSetChanged();
                break;
            case R.id.item_compay_self_regist_menu3:
                popupMenu.dismiss();
                final Dialog dialog=new Dialog(TeacherManageActivity.this);
                View view=LayoutInflater.from(this).inflate(R.layout.item_compay_dele_reason,null);
                final EditText reason= (EditText) view.findViewById(R.id.compay_dele_edt);
                Button btn= (Button) view.findViewById(R.id.compay_dele_btn);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(reason.getText().toString().trim().equals("")){
                            mToast.showText(getApplicationContext(),"请输入删除理由");
                        }else {
                            final StringBuilder buidler=new StringBuilder();
                            for (int i = 0; i < mdata.size(); i++) {
                                if(mdata.get(i).isClick()){
                                    buidler.append(mdata.get(i).getId());
                                    buidler.append(",");
                                }
                            }
                            handler.sendEmptyMessage(3333);
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    params.clear();
                                    values.clear();
                                    params.add("key");
                                    params.add("type");
                                    params.add("ids");
                                    params.add("del_reason");
                                    values.add(CompayInterface.CompayKey(getApplicationContext()));
                                    values.add("2");
                                    values.add(buidler.toString());
                                    values.add(reason.getText().toString().trim());
                                    sendRequest("POST",CompayInterface.SelfRegStuDele(),2,params,values);
                                }
                            }).start();
                            dialog.dismiss();
                        }
                    }
                });
                dialog.setCanceledOnTouchOutside(true);
                dialog.setCancelable(true);
                dialog.setContentView(view);
                dialog.show();
                break;
            default:
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
                    JSONObject object=new JSONObject(response.get().toString());
                    mLog.e("reponse","-->"+response.get().toString());
                    mmsg=object.getString("msg");
                    if(what==1){
                        if(object.getString("code").equals("200")){
                            JSONArray array=new JSONArray(object.getString("data"));
                            if(array.length()>0){
                                mdata.clear();
                                for (int i = 0; i < array.length(); i++) {
                                    SelfRegTeaM m=gson.fromJson(array.getJSONObject(i).toString(),SelfRegTeaM.class);
                                    mdata.add(m);
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
                        //上拉刷新加载
                        if(object.getString("code").equals("200")){
                            JSONArray array=new JSONArray(object.getString("data"));
                            if(array.length()>0){
                                for (int i = 0; i < array.length(); i++) {
                                    SelfRegTeaM m=gson.fromJson(array.getJSONObject(i).toString(),SelfRegTeaM.class);
                                    mdata.add(m);
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
            //添加菜单
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
        View root= LayoutInflater.from(this).inflate(R.layout.item_compay_self_regist_menu,null);
        menu1= (LinearLayout) root.findViewById(R.id.item_compay_self_regist_menu1);
        menu2= (LinearLayout) root.findViewById(R.id.item_compay_self_regist_menu2);
        menu3= (LinearLayout) root.findViewById(R.id.item_compay_self_regist_menu3);
        menu1.setOnClickListener(this);
        menu2.setOnClickListener(this);
        menu3.setOnClickListener(this);
        popupMenu=new PopupWindow(root,MATCH_PARENT,WRAP_CONTENT);
        popupMenu.setAnimationStyle(R.style.BottomMenu);
        popupMenu.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this,R.color.textColor_whit)));
        popupMenu.showAtLocation(getLayoutInflater().inflate(R.layout.activity_student_mange,null), Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL,0,0);
    }


    private String getTime(TextView edit){
        String getTime=edit.getText().toString().trim();
        getTime=getTime.replace("年","-");
        getTime=getTime.replace("月","-");
        getTime=getTime.replace("日","");
        return getTime;
    }

}
