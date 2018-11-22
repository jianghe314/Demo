package com.kangzhan.student.HomeFragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;

import com.google.gson.Gson;
import com.holenzhou.pullrecyclerview.PullRecyclerView;
import com.holenzhou.pullrecyclerview.layoutmanager.XLinearLayoutManager;
import com.kangzhan.student.Debug.mLog;
import com.kangzhan.student.HomeFragment.Adapter.HomeSchoolFragmentAdapter;
import com.kangzhan.student.HomeFragment.Bean.EventMessage;
import com.kangzhan.student.HomeFragment.Bean.SchoolList;
import com.kangzhan.student.HomeFragment.HomeInterface.HomeInterface;
import com.kangzhan.student.HomeFragment.mDialog.ErrorDialog;
import com.kangzhan.student.HomeFragment.mDialog.WaitDialog;
import com.kangzhan.student.R;
import com.kangzhan.student.ShowUI.mToast;
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
import java.util.List;

/**
 * Created by kangzhan011 on 2017/11/29.
 */

public class SchoolFragment2 extends HomeBaseFragment {

    private PullRecyclerView recyclerView;
    private HomeSchoolFragmentAdapter adapter;
    private List<SchoolList> data=new ArrayList<>();
    private ArrayList<String> params=new ArrayList<>();
    private ArrayList<String> values=new ArrayList<>();
    private Gson gson=new Gson();
    private WaitDialog waitDialog;
    private ErrorDialog errorDialog;
    private String Msg;
    private int page=1;

    @Override
    protected int setContentView() {
        return R.layout.home_school_fragment_layout;
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EventMessage msg){
        waitDialog.dismiss();
        if(msg.getMsg().equals("schoollist")){
            adapter.notifyDataSetChanged();
        } else if(msg.getMsg().equals("set_city")){
            refreshData("");
        }else if(msg.getMsg().equals("has_located")){
            refreshData("");
        }else if(msg.getMsg().equals("schoollist_tip")){
            errorDialog.show();
            errorDialog.setTextMsg(Msg);
        }else if(msg.getMsg().equals("schoollist2_more")) {
            mToast.showText(getContext().getApplicationContext(), Msg);
        }else if(msg.getMsg().equals("schoollist_error")){
            errorDialog.show();
            errorDialog.setTextMsg("网络连接不上，请检查网络连接");
        }
    }

    private  String getSearchContent(Context context){
        SharedPreferences sp=context.getSharedPreferences("Home_Search",Context.MODE_PRIVATE);
        String city=sp.getString("content","搜索内容为空");
        return city;
    }

    @Override
    protected void initView(View view) {
        EventBus.getDefault().register(this);
        recyclerView= (PullRecyclerView) view.findViewById(R.id.home_school_fragment_list_recycler);
        adapter=new HomeSchoolFragmentAdapter(getContext(),R.layout.item_home_finds_layout,data);
        recyclerView.setAdapter(adapter);
        recyclerView.setEmptyView(LayoutInflater.from(getContext()).inflate(R.layout.item_no_data,null));
    }

    @Override
    protected void initData() {
        waitDialog=new WaitDialog(getContext());
        errorDialog=new ErrorDialog(getContext());
        recyclerView.setColorSchemeResources(R.color.swipe_color1,R.color.swipe_color2,R.color.swipe_color3);
        recyclerView.setLayoutManager(new XLinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false));
        recyclerView.enablePullRefresh(true);       //设置可以下拉
        recyclerView.enableLoadMore(true);
        recyclerView.setOnRecyclerRefreshListener(new PullRecyclerView.OnRecyclerRefreshListener() {
            @Override
            public void onPullRefresh() {
                refreshData("");
                recyclerView.stopRefresh();
            }

            @Override
            public void onLoadMore() {
                page++;
                loadMore(String.valueOf(page),2);
                recyclerView.stopLoadMore();
            }
        });
        refreshData("");
    }


    private void loadMore(final String page, int more){
        waitDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                params.clear();
                values.clear();
                params.add("city");
                params.add("latitude");
                params.add("longitude");
                params.add("order");
                params.add("page");
                values.add(HomeInterface.getLocation(getContext()));
                values.add(HomeInterface.getHomeLocation(getContext())[2]);
                values.add(HomeInterface.getHomeLocation(getContext())[1]);
                values.add("2");
                values.add(page);
                sendRequest(2,params,values, HomeInterface.schoolList(),"GET");
            }
        }).start();

    }


    private void refreshData(final String search){
        waitDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                params.clear();
                values.clear();
                params.add("city");
                params.add("latitude");
                params.add("longitude");
                params.add("order");
                params.add("page");
                params.add("searchCriteria");
                values.add(HomeInterface.getLocation(getContext()));
                values.add(HomeInterface.getHomeLocation(getContext())[2]);
                values.add(HomeInterface.getHomeLocation(getContext())[1]);
                values.add("2");
                values.add("1");
                values.add(search);
                sendRequest(1,params,values, HomeInterface.schoolList(),"GET");
            }
        }).start();

    }



    private void sendRequest(int what, ArrayList<String> params, ArrayList<String> values,String url,String way) {
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
                    mLog.e("reponse","-->"+response.get().toString());
                    JSONObject object=new JSONObject(response.get().toString());
                    switch (what){
                        case 1:
                            if(object.getString("code").equals("200")){
                                data.clear();
                                JSONArray array=new JSONArray(object.getString("data"));
                                for (int i = 0; i <array.length() ; i++) {
                                    SchoolList list=gson.fromJson(array.getJSONObject(i).toString(),SchoolList.class);
                                    data.add(list);
                                }
                                EventBus.getDefault().post(new EventMessage("schoollist"));
                            }else {
                                Msg=object.getString("msg");
                                EventBus.getDefault().post(new EventMessage("schoollist_tip"));
                            }
                            break;
                        case 2:
                            if(object.getString("code").equals("200")){
                                JSONArray array=new JSONArray(object.getString("data"));
                                for (int i = 0; i <array.length() ; i++) {
                                    SchoolList list=gson.fromJson(array.getJSONObject(i).toString(),SchoolList.class);
                                    data.add(list);
                                }
                                EventBus.getDefault().post(new EventMessage("schoollist"));
                            }else {
                                Msg="没有更多了";
                                EventBus.getDefault().post(new EventMessage("schoollist2_more"));
                            }
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(int what, Response<JSONObject> response) {
                EventBus.getDefault().post(new EventMessage("schoollist_error"));
            }

            @Override
            public void onFinish(int what) {

            }
        });

    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
