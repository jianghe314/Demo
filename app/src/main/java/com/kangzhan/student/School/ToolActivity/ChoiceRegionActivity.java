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
import android.widget.ListView;

import com.google.gson.Gson;
import com.holenzhou.pullrecyclerview.PullRecyclerView;
import com.holenzhou.pullrecyclerview.layoutmanager.XLinearLayoutManager;
import com.kangzhan.student.R;
import com.kangzhan.student.School.Adapter.ChoiceRegionAdapter;
import com.kangzhan.student.School.Bean.ChoiceRegion;
import com.kangzhan.student.School.Edu.AddTeacherRestListActivity;
import com.kangzhan.student.School.Edu.EduCarManage_Add;
import com.kangzhan.student.ShowUI.mToast;
import com.kangzhan.student.ShowUI.showMessage;
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
import java.util.List;
import java.util.Set;

public class ChoiceRegionActivity extends BaseActivity {
    private ChoiceRegionAdapter adapter;
    private ArrayList<ChoiceRegion> data=new ArrayList<>();
    private PullRecyclerView recycler;
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
                            showMessage.showMsg(ChoiceRegionActivity.this,"网络连接异常，请检查网络连接");
                        }
                    });
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice_region);
        Toolbar toolbar= (Toolbar) findViewById(R.id.ChoiceRegion_toolbar);
        setSupportActionBar(toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        gson=new Gson();
        initView();
    }

    private void initView() {
        adapter=new ChoiceRegionAdapter(ChoiceRegionActivity.this,R.layout.item_choice_region,data);
        recycler= (PullRecyclerView) findViewById(R.id.choice_region_list);
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
                        sendRequest();
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

    private void sendRequest() {
        Request<JSONObject> request=NoHttp.createJsonObjectRequest(school.EduChoiceRegion(), RequestMethod.GET);
        request.add("key",school.schoolKey(getApplicationContext()));
        request.add("inst_id",1);
        getRequestQueue().add(1, request, new OnResponseListener<JSONObject>() {
            @Override
            public void onStart(int what) {

            }

            @Override
            public void onSucceed(int what, Response<JSONObject> response) {
                try {
                    JSONObject objcet=new JSONObject(response.get().toString());
                    mmsg=objcet.getString("msg");
                    if(objcet.getString("code").equals("200")){
                        JSONArray array=new JSONArray(objcet.getString("data"));
                        if(array.length()>0){
                            for (int i = 0; i < array.length(); i++) {
                                ChoiceRegion region=gson.fromJson(array.getJSONObject(i).toString(),ChoiceRegion.class);
                                data.add(region);
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
        getMenuInflater().inflate(R.menu.school_sure,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            case R.id.sure:
                String regionId = "";
                String regionName="";
                for (int i = 0; i <data.size(); i++) {
                    if(data.get(i).isClick()){
                        regionId=data.get(i).getId();
                        regionName=data.get(i).getName();
                    }
                }
                saveChoiceRegion(regionName,regionId);
                finish();
                break;
            default:
                break;
        }
        return true;
    }
    private void saveChoiceRegion(String regionName,String regionId){
        SharedPreferences sp=getSharedPreferences("ChoiceRegion",MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putString("regionName",regionName);
        editor.putString("regionId",regionId);
        editor.apply();
    }

}
