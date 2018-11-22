package com.kangzhan.student.School.ToolActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.holenzhou.pullrecyclerview.PullRecyclerView;
import com.holenzhou.pullrecyclerview.layoutmanager.XLinearLayoutManager;
import com.kangzhan.student.R;
import com.kangzhan.student.School.Adapter.ChoiceTeahcerAdapter;
import com.kangzhan.student.School.Bean.AllocateTeacher;
import com.kangzhan.student.School.Bean.ChoiceTeacher;
import com.kangzhan.student.School.Edu.AllocationTeacherActivity;
import com.kangzhan.student.School.Edu.EduCarManage_Add;
import com.kangzhan.student.ShowUI.mToast;
import com.kangzhan.student.ShowUI.showMessage;
import com.kangzhan.student.com.BaseActivity;
import com.kangzhan.student.mInterface.SchoolInterface.school;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.db.BaseDao;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ChocieTeacherActivity extends BaseActivity implements View.OnClickListener{
    private ArrayList<ChoiceTeacher> data=new ArrayList<>();
    private ArrayList<String> params=new ArrayList<>();
    private ArrayList<String> values=new ArrayList<>();
    private PullRecyclerView recycler;
    private ChoiceTeahcerAdapter adapter;
    private String mmsg;
    private int Type;
    private TextView searcher;
    private EditText searcherContent;
    private Gson gson;

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1111:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            recycler.stopRefresh();
                            adapter.notifyDataSetChanged();
                        }
                    });
                    break;
                case 2222:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            recycler.stopRefresh();
                            mToast.showText(getApplicationContext(),mmsg);
                        }
                    });
                    break;
                case 9999:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showMessage.showMsg(ChocieTeacherActivity.this,"网络连接异常，请检查网络连接");
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
        setContentView(R.layout.activity_chocie_teacher);
        Toolbar toolbar= (Toolbar) findViewById(R.id.school_choice_teacher_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        Intent getType=getIntent();
        Type=getType.getIntExtra("Type",11);
        gson=new Gson();
        initView();
    }

    private void initView() {
        searcher= (TextView) findViewById(R.id.Chocie_teacher_train_Tosearch);
        searcher.setOnClickListener(this);
        searcherContent= (EditText) findViewById(R.id.Choice_teacher_search_content);
        adapter=new ChoiceTeahcerAdapter(ChocieTeacherActivity.this,R.layout.item_school_edu_stum_alloc,data);
        recycler= (PullRecyclerView) findViewById(R.id.choice_teacher_recycler);
        recycler.setAdapter(adapter);
        recycler.setLayoutManager(new XLinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
        recycler.setColorSchemeResources(R.color.swipe_color1,R.color.swipe_color2,R.color.swipe_color3);
        recycler.enablePullRefresh(true);       //设置可以下拉
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
                       values.add(school.schoolKey(getApplicationContext()));
                       values.add("1");
                       sendRequset("GET",1,school.EduStmChocieTeac(),params,values);
                   }
               }).start();
           }

           @Override
           public void onLoadMore() {
                recycler.stopLoadMore();
           }
       });
        recycler.postRefreshing();


    }


    private void sendRequset(String way, int what, String url, ArrayList<String> params, ArrayList<String> values) {
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
                if(what==1){
                    try {
                        JSONObject object=new JSONObject(response.get().toString());
                        mmsg=object.getString("msg");
                        if(object.getString("code").equals("200")){
                            JSONArray array=new JSONArray(object.getString("data"));
                            if(array.length()>0){
                                data.clear();
                                for (int i = 0; i <array.length(); i++) {
                                    ChoiceTeacher allo=gson.fromJson(array.getJSONObject(i).toString(),ChoiceTeacher.class);
                                    data.add(allo);
                                }
                                handler.sendEmptyMessage(1111);

                            }
                        }else {
                            handler.sendEmptyMessage(2222);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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
            case R.id.Chocie_teacher_train_Tosearch:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        params.clear();
                        values.clear();
                        params.add("key");
                        params.add("type");
                        params.add("searchCriteria");
                        values.add(school.schoolKey(getApplicationContext()));
                        values.add("1");
                        values.add(searcherContent.getText().toString().trim());
                        sendRequset("GET",1,school.EduStmChocieTeac(),params,values);
                    }
                }).start();
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            case R.id.sure:
                StringBuilder teacherID=new StringBuilder();
                StringBuilder teacherName=new StringBuilder();
                for (int i = 0; i <data.size(); i++) {
                    if(data.get(i).isClick()){
                        teacherID.append(data.get(i).getId());
                        teacherID.append(",");
                        teacherName.append(data.get(i).getName());
                        teacherName.append(",");
                    }
                }
                if(Type==11){
                    saveChoiceTeahcer(teacherName.toString(),teacherID.toString());
                    finish();
                }
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.school_sure,menu);
        return true;
    }


    private void saveChoiceTeahcer(String teacherName,String teacherId){
        SharedPreferences sp=getSharedPreferences("ChoiceTeacher",MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putString("Name",teacherName);
        editor.putString("Id",teacherId);
        editor.apply();
    }

}
