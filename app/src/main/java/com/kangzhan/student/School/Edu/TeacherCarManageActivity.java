package com.kangzhan.student.School.Edu;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.holenzhou.pullrecyclerview.PullRecyclerView;
import com.holenzhou.pullrecyclerview.layoutmanager.XLinearLayoutManager;
import com.kangzhan.student.Debug.mLog;
import com.kangzhan.student.R;
import com.kangzhan.student.School.Adapter.EduCarManageAdapter;
import com.kangzhan.student.School.Bean.EduCarManage;
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

public class TeacherCarManageActivity extends BaseActivity implements View.OnClickListener{
    private LinearLayout add,dele;
    private TextView search;
    private EditText searchContent;
    private EduCarManageAdapter adapter;
    private ArrayList<EduCarManage> data=new ArrayList<>();
    private ArrayList<String> params=new ArrayList<>();
    private ArrayList<String> values=new ArrayList<>();
    private PullRecyclerView recycler;
    private Gson gson;
    private String mmsg;
    private int mpage=1;
    private boolean isOpen=false;
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
                            showProgress.showProgress(TeacherCarManageActivity.this,"删除中...");
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
                            values.add(school.schoolKey(getApplicationContext()));
                            sendReuqst("GET",1, school.EduCarManage(),params,values);
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
                            showProgress.closeProgress();
                            mToast.showText(getApplicationContext(),mmsg);
                        }
                    });
                    break;
                case 6666:
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
                            showMessage.showMsg(TeacherCarManageActivity.this,"网络连接异常，请检查网络连接");
                        }
                    });
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_car_manage);
        Toolbar toolbar= (Toolbar) findViewById(R.id.teacher_CarM_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        gson=new Gson();
        initView();
    }

    private void initView() {
        search= (TextView) findViewById(R.id.edu_car_manage_Tosearch);
        search.setOnClickListener(this);
        searchContent= (EditText) findViewById(R.id.edu_car_manage_search_content);
        add= (LinearLayout) findViewById(R.id.edu_car_manage_add);
        add.setOnClickListener(this);
        dele= (LinearLayout) findViewById(R.id.edu_car_manage_dele);
        dele.setOnClickListener(this);
        adapter=new EduCarManageAdapter(TeacherCarManageActivity.this,R.layout.item_teacher_car_manage,data);
        recycler= (PullRecyclerView) findViewById(R.id.teacher_car_manage_recycler);
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
                        values.add(school.schoolKey(getApplicationContext()));
                        sendReuqst("GET",1, school.EduCarManage(),params,values);
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
                       values.add(school.schoolKey(getApplicationContext()));
                       values.add(mpage+"");
                       sendReuqst("GET",3, school.EduCarManage(),params,values);
                   }
               }).start();
            }
        });
        recycler.postRefreshing();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.edu_car_manage_Tosearch:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        params.clear();
                        values.clear();
                        params.add("key");
                        params.add("searchCriteria");
                        params.add("export");
                        values.add(school.schoolKey(getApplicationContext()));
                        values.add(searchContent.getText().toString().trim());
                        values.add("1");
                        sendReuqst("GET",1, school.EduCarManage(),params,values);
                    }
                }).start();
                break;
            case R.id.edu_car_manage_add:
                //
                Intent addCar=new Intent(this,EduCarManage_Add.class);
                startActivity(addCar);
                break;
            case R.id.edu_car_manage_dele:
                final StringBuilder builder=new StringBuilder();
                for (int i = 0; i <data.size(); i++) {
                    if(data.get(i).isClick()){
                        builder.append(data.get(i).getId());
                        builder.append(",");
                    }
                }
                final AlertDialog.Builder dialo=new AlertDialog.Builder(TeacherCarManageActivity.this);
                dialo.setMessage("确认删除?");
                dialo.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        handler.sendEmptyMessage(3333);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                params.clear();
                                values.clear();
                                params.add("key");
                                params.add("car_ids");
                                values.add(school.schoolKey(getApplicationContext()));
                                values.add(builder.toString());
                                sendReuqst("POST",2,school.EduCarManage_Dele(),params,values);
                            }
                        }).start();
                    }
                });
                dialo.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialo.create().show();
                break;
        }
    }



    private void sendReuqst(String way, int what, String url, ArrayList<String> params, ArrayList<String> values) {
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
                    JSONObject object = new JSONObject(response.get().toString());
                    mLog.e("reponse","->"+response.get().toString());
                    if(what==1){
                        mmsg=object.getString("msg");
                        if(object.getString("code").equals("200")){
                            JSONArray array=new JSONArray(object.getString("data"));
                            if(array.length()>0){
                                data.clear();
                                for (int i = 0; i <array.length(); i++) {
                                    EduCarManage car=gson.fromJson(array.getJSONObject(i).toString(),EduCarManage.class);
                                    data.add(car);
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
                        mmsg=object.getString("msg");
                        if(object.getString("code").equals("200")){
                            handler.sendEmptyMessage(4444);
                        }else{
                            handler.sendEmptyMessage(5555);
                        }
                    }else if(what==3){
                        if(object.getString("code").equals("200")){
                            JSONArray array=new JSONArray(object.getString("data"));
                            if(array.length()>0) {
                                for (int i = 0; i < array.length(); i++) {
                                    EduCarManage car = gson.fromJson(array.getJSONObject(i).toString(), EduCarManage.class);
                                    data.add(car);
                                }
                            }
                            handler.sendEmptyMessage(1111);
                        }else {
                            handler.sendEmptyMessage(6666);
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
            if(isOpen){
                for (int i = 0; i <data.size() ; i++) {
                    data.get(i).setShow(false);
                }
                isOpen=false;
            }else {
                for (int i = 0; i <data.size() ; i++) {
                    data.get(i).setShow(true);
                }
                isOpen=true;
            }
            adapter.notifyDataSetChanged();
        }
        return super.onOptionsItemSelected(item);
    }

}
