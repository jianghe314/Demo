package com.kangzhan.student.HomeFragment.activities;

import android.content.SharedPreferences;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.kangzhan.student.Debug.mLog;
import com.kangzhan.student.HomeFragment.Bean.EventMessage;
import com.kangzhan.student.HomeFragment.Bean.OpenedCity;
import com.kangzhan.student.HomeFragment.Bean.SchoolList;
import com.kangzhan.student.HomeFragment.HomeInterface.HomeInterface;
import com.kangzhan.student.HomeFragment.mDialog.ErrorDialog;
import com.kangzhan.student.HomeFragment.mDialog.WaitDialog;
import com.kangzhan.student.R;
import com.kangzhan.student.ShowUI.mToast;
import com.kangzhan.student.ShowUI.showMessage;
import com.kangzhan.student.com.BaseActivity;
import com.kangzhan.student.mUI.mLableLayout;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.Response;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SelectCityActivity extends BaseActivity {
    private mLableLayout city_layout;
    private SwipeRefreshLayout refreshLayout;
    private ArrayList<OpenedCity> data=new ArrayList<>();
    private Gson gson=new Gson();
    private TextView CurrentCity;
    private WaitDialog waitDialog;
    private ErrorDialog errorDialog;

    private String Msg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_city);
        EventBus.getDefault().register(this);
        Toolbar toolbar= (Toolbar) findViewById(R.id.home_sele_city_detail_back);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        initView();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EventMessage msg){
        waitDialog.dismiss();
        refreshLayout.setRefreshing(false);
        if(msg.getMsg().equals("city_list")){
            //添加数据
            LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.setMargins(20,10,20,10);
            city_layout.removeAllViews();
            for (int i = 0; i <data.size() ; i++) {
                final TextView tv=new TextView(this);
                tv.setTextColor(getResources().getColor(R.color.textColor_black));
                tv.setTextSize(16);
                tv.setGravity(Gravity.CENTER);
                tv.setBackgroundResource(R.drawable.edit_white_bg_no_line);
                tv.setPadding(20,10,20,20);
                tv.setText(data.get(i).getName());
                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //城市的点击事件 将首页的城市设置为选择城市
                        CurrentCity.setText(tv.getText().toString().trim());
                        setCurrentCity(tv.getText().toString().trim());
                        EventBus.getDefault().post(new EventMessage("set_city"));
                        finish();
                    }
                });
                city_layout.addView(tv,lp);
            }
            city_layout.requestLayout();

        }else if(msg.getMsg().equals("city_no_data")){
            mToast.showText(getApplicationContext(),Msg);
        }else if(msg.getMsg().equals("city_error")){
            errorDialog.show();
        }
    }

    private void initView() {
        CurrentCity= (TextView) findViewById(R.id.select_city_name);
        city_layout= (mLableLayout) findViewById(R.id.select_city_list_container);
        refreshLayout= (SwipeRefreshLayout) findViewById(R.id.select_city_refresh);
        refreshLayout.setColorSchemeResources(R.color.swipe_color1,R.color.swipe_color2,R.color.swipe_color3);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //刷新
                refresh();
            }
        });
        CurrentCity.setText(HomeInterface.getHomeLocation(this)[0]);
        errorDialog=new ErrorDialog(this);
        waitDialog=new WaitDialog(this);
        waitDialog.show();
        refresh();
    }



    private void refresh(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                sendRequest(1, HomeInterface.OpenedCity());
            }
        }).start();
    }

    private void setCurrentCity(String city){
        SharedPreferences sp=getSharedPreferences("CurrentCity",MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putString("city",city);
        editor.apply();
    }



    private void sendRequest(int what,String url) {
        Request<JSONObject> request= NoHttp.createJsonObjectRequest(url,RequestMethod.GET);
        getRequestQueue().add(what, request, new OnResponseListener<JSONObject>() {
            @Override
            public void onStart(int what) {

            }

            @Override
            public void onSucceed(int what, Response<JSONObject> response) {
                try {
                    mLog.e("reponse","-->"+response.get().toString());
                    JSONObject object=new JSONObject(response.get().toString());
                    switch (what){
                        case 1:
                            if(object.getString("code").equals("200")){
                                JSONArray array=new JSONArray(object.getString("data"));
                                if(array.length()>0){
                                    data.clear();
                                    for (int i = 0; i <array.length() ; i++) {
                                        OpenedCity city=gson.fromJson(array.getString(i),OpenedCity.class);
                                        data.add(city);
                                    }
                                    EventBus.getDefault().post(new EventMessage("city_list"));
                                }
                            }else {
                                Msg=object.getString("msg");
                                EventBus.getDefault().post(new EventMessage("city_no_data"));
                            }
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(int what, Response<JSONObject> response) {
                EventBus.getDefault().post(new EventMessage("city_error"));
            }

            @Override
            public void onFinish(int what) {

            }
        });

    }


    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
