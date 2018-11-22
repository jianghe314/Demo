package com.kangzhan.student.Teacher.News;

import android.content.Context;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.holenzhou.pullrecyclerview.PullRecyclerView;
import com.holenzhou.pullrecyclerview.layoutmanager.XLinearLayoutManager;
import com.kangzhan.student.Debug.mLog;
import com.kangzhan.student.R;
import com.kangzhan.student.ShowUI.showProgress;
import com.kangzhan.student.Student.mutils.DividerItemDecoration;
import com.kangzhan.student.Teacher.Adapter.ChoiceStudentAdapter;
import com.kangzhan.student.Teacher.bean.ChoiceStudentList;
import com.kangzhan.student.com.BaseActivity;
import com.kangzhan.student.mInterface.TeacherInterface.teacher;
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

public class MystudentListActivity extends BaseActivity implements View.OnClickListener{
    //全选先放一边
      //<!--列表的item为：item_teacher_news_mystudent_list-->
    //学员列表
    private EditText searchContent;
    private TextView searchTv,choice;
    private ImageView choiceIv;
    private PullRecyclerView recycler;
    private ChoiceStudentAdapter adapter;
    private ArrayList<ChoiceStudentList> mdata=new ArrayList<>();
    private Gson gson;
    private String mmsg;
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
                case 1122:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            recycler.stopRefresh();
                            Toast.makeText(getApplicationContext(),mmsg,Toast.LENGTH_SHORT).show();
                        }
                    });
                    break;
                case 2222:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.closeProgress();
                            Toast.makeText(getApplicationContext(),"搜索内容不能为空",Toast.LENGTH_SHORT).show();
                        }
                    });
                    break;
                case 9999:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            recycler.stopRefresh();
                            showProgress.closeProgress();
                            AlertDialog.Builder builder=new AlertDialog.Builder(MystudentListActivity.this);
                            builder.setMessage("网络连接异常，请检查网络连接");
                            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            builder.create().show();
                        }
                    });
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mystudent_list);
        Toolbar toolbar= (Toolbar) findViewById(R.id.teacher_news_studentlist_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        gson=new Gson();
        initView();
    }

    private void initView() {
        searchContent= (EditText) findViewById(R.id.mystudentList_search);
        searchTv= (TextView) findViewById(R.id.mystudentList_search_tv);
        searchTv.setOnClickListener(this);
        choice= (TextView) findViewById(R.id.mystudent_list_choiceAll_tv);
        choiceIv= (ImageView) findViewById(R.id.mystudent_list_choiceAll_iv);
        choiceIv.setOnClickListener(this);
        choiceIv.setTag(0);
        recycler= (PullRecyclerView) findViewById(R.id.teacher_news_myStudent_list);
        adapter=new ChoiceStudentAdapter(MystudentListActivity.this,R.layout.item_teacher_news_mystudent_list,mdata);
        recycler.setAdapter(adapter);
        recycler.setLayoutManager(new XLinearLayoutManager(MystudentListActivity.this, LinearLayoutManager.VERTICAL,false));
        recycler.setColorSchemeResources(R.color.swipe_color1,R.color.swipe_color2,R.color.swipe_color3);
        recycler.addItemDecoration(new DividerItemDecoration(MystudentListActivity.this,DividerItemDecoration.VERTICAL_LIST));
        recycler.enablePullRefresh(true);       //设置可以下拉
        recycler.enableLoadMore(true);
        recycler.setOnRecyclerRefreshListener(new PullRecyclerView.OnRecyclerRefreshListener() {
            // 模拟下拉刷新网络请求
            @Override
            public void onPullRefresh() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        sendRequest();
                    }
                }).start();
            }
            // 模拟上拉加载更多网络请求
            @Override
            public void onLoadMore() {
                recycler.stopLoadMore();
            }
        });
        recycler.postRefreshing();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //搜索
            case R.id.mystudentList_search_tv:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if(!searchContent.getText().toString().trim().equals("")){
                            Search(searchContent.getText().toString().trim());
                        }else {
                            handler.sendEmptyMessage(2222);
                        }

                    }
                }).start();
                break;
            //全选
            case R.id.mystudent_list_choiceAll_iv:
                int mtag= (int) choiceIv.getTag();
                if(mtag==0){
                    //选中
                    choiceIv.setImageResource(R.mipmap.choiceall1);
                    choiceIv.setTag(1);
                    choice.setText("取消");
                    if (mdata.size()>0) {
                        for (int i = 0; i <mdata.size(); i++) {
                            mdata.get(i).setClick(true);
                        }
                        adapter.notifyDataSetChanged();
                    }

                }else {
                    //反选
                    choiceIv.setImageResource(R.mipmap.choiceall0);
                    choiceIv.setTag(0);
                    choice.setText("全选");
                    if(mdata.size()>0){
                        for (int i = 0; i <mdata.size(); i++) {
                            mdata.get(i).setClick(false);
                        }
                        adapter.notifyDataSetChanged();
                    }
                }
                break;
            default:
                break;
        }
    }

    private void Search(String searchContent) {
        Request<JSONObject> request=NoHttp.createJsonObjectRequest(teacher.teacherChoiceStu(),RequestMethod.GET);
        request.add("key",teacher.teacherKey(getApplicationContext()));
        request.add("type",1);
        request.add("searchCriteria",searchContent);
        getRequestQueue().add(2, request, new OnResponseListener<JSONObject>() {
            @Override
            public void onStart(int what) {

            }

            @Override
            public void onSucceed(int what, Response<JSONObject> response) {
                try {
                    JSONObject object=new JSONObject(response.get().toString());
                    mmsg=object.getString("msg");
                    if(object.getString("code").equals("200")){
                        String data=object.getString("data");
                        JSONArray array=new JSONArray(data);
                        if(array.length()>0){
                            mdata.clear();
                            for (int i = 0; i <array.length(); i++) {
                                JSONObject obj=array.getJSONObject(i);
                                ChoiceStudentList list=gson.fromJson(obj.toString(),ChoiceStudentList.class);
                                mdata.add(list);
                            }
                            handler.sendEmptyMessage(1111);
                        }
                    }else {
                        handler.sendEmptyMessage(1122);
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


    private void sendRequest() {
        Request<JSONObject> request=NoHttp.createJsonObjectRequest(teacher.teacherChoiceStu(), RequestMethod.GET);
        request.add("key",teacher.teacherKey(MystudentListActivity.this));
        request.add("type",1);
        getRequestQueue().add(1, request, new OnResponseListener<JSONObject>() {
            @Override
            public void onStart(int what) {

            }

            @Override
            public void onSucceed(int what, Response<JSONObject> response) {
                try {
                    JSONObject object=new JSONObject(response.get().toString());
                    String code=object.getString("code");
                    mmsg=object.getString("msg");
                    if(code.equals("200")){
                        String data=object.getString("data");
                        JSONArray array=new JSONArray(data);
                        if(array.length()>0){
                            mdata.clear();
                            for (int i = 0; i <array.length(); i++) {
                                JSONObject obj=array.getJSONObject(i);
                                ChoiceStudentList list=gson.fromJson(obj.toString(),ChoiceStudentList.class);
                                mdata.add(list);
                            }
                        }
                        handler.sendEmptyMessage(1111);
                    }else {
                        handler.sendEmptyMessage(1122);
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


    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            case R.id.teacher_choice_student_menu:
                StringBuilder stuId=new StringBuilder();
                StringBuilder stuName=new StringBuilder();
                StringBuilder stuPhone=new StringBuilder();
                for (int i = 0; i <mdata.size(); i++) {
                    if(mdata.get(i).isClick()){
                        stuId.append(mdata.get(i).getId());
                        stuName.append(mdata.get(i).getName());
                        stuPhone.append(mdata.get(i).getPhone());
                        stuId.append(",");
                        stuName.append(",");
                        stuPhone.append(",");
                    }
                }
                String Id=stuId.toString().substring(0,stuId.toString().length()-1);
                String Name=stuName.toString().substring(0,stuName.toString().length()-1);
                String Phone=stuPhone.toString().substring(0,stuPhone.toString().length()-1);
                Bundle data=new Bundle();
                data.putString("Id",Id);
                data.putString("Name",Name);
                data.putString("Phone",Phone);
                mLog.e("Id","-->"+Id);
                mLog.e("Name","-->"+Name);
                mLog.e("Phone","-->"+Phone);
                Intent intent=new Intent();
                intent.putExtra("data",data);
                setResult(1,intent);
                finish();
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.teacher_choice_menu,menu);
        return true;
    }

}
