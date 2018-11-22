package com.kangzhan.student.Advisetment.newsFragment;

import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;

import com.google.gson.Gson;
import com.holenzhou.pullrecyclerview.PullRecyclerView;
import com.holenzhou.pullrecyclerview.layoutmanager.XLinearLayoutManager;
import com.kangzhan.student.Adapter.RecommendNewsAdapter;
import com.kangzhan.student.Debug.mLog;
import com.kangzhan.student.R;
import com.kangzhan.student.RecommendBean.RecommendNews;
import com.kangzhan.student.ShowUI.mToast;
import com.kangzhan.student.ShowUI.showProgress;
import com.kangzhan.student.Student.mutils.DividerItemDecoration;
import com.kangzhan.student.com.LazyFragment;
import com.kangzhan.student.mInterface.AdvisetInterface.Adviset;
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

/**
 * Created by kangzhan011 on 2017/5/23.
 */

public class NewsOurs extends LazyFragment{
    private PullRecyclerView recyclerView;
    private RecommendNewsAdapter adapter;
    private ArrayList<RecommendNews> mdata=new ArrayList<>();
    private int total,lastPage,current_page,i=1;
    private Gson gson;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0000:
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            recyclerView.stopRefresh();
                            recyclerView.stopLoadMore();
                            adapter.notifyDataSetChanged();
                        }
                    });
                    break;
                case 1111:
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            recyclerView.stopRefresh();
                            recyclerView.stopLoadMore();
                            adapter.notifyDataSetChanged();
                            mToast.showText(getActivity().getApplicationContext(),"当前没有数据");
                        }
                    });
                    break;
                case 2222:
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            recyclerView.stopRefresh();
                            recyclerView.stopLoadMore();
                            adapter.notifyDataSetChanged();
                            mToast.showText(getActivity().getApplicationContext(),"没有更多了");
                        }
                    });
                    break;
                case 9999:
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            recyclerView.stopRefresh();
                            recyclerView.stopLoadMore();
                            showProgress.showProgress(getContext(),"网络连接异常，请检查网络连接");
                        }
                    });
                    break;
            }
        }
    };
    @Override
    protected int setContentView() {
        return R.layout.recommend_news_2;
    }

    @Override
    protected void lazyLoad() {
        gson=new Gson();
        recyclerView=findViewById(R.id.recommend_news_Ours);
        adapter=new RecommendNewsAdapter(getContext(),R.layout.item_recommend_news,mdata,"31");
        recyclerView.setAdapter(adapter);
        recyclerView.setEmptyView(LayoutInflater.from(getContext()).inflate(R.layout.item_no_data,null));
        recyclerView.setLayoutManager(new XLinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL_LIST));
        recyclerView.setColorSchemeResources(R.color.swipe_color1,R.color.swipe_color2,R.color.swipe_color3);
        recyclerView.enablePullRefresh(true);       //设置可以下拉
        recyclerView.enableLoadMore(true);
        recyclerView.setOnRecyclerRefreshListener(new PullRecyclerView.OnRecyclerRefreshListener() {
            // 模拟下拉刷新网络请求
            @Override
            public void onPullRefresh() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        sendRequest(1,1);
                    }
                }).start();

            }
            // 模拟上拉加载更多网络请求
            @Override
            public void onLoadMore() {
                if(total>=1){
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            if(i<=total){
                                i++;
                                sendRequest(2,i);
                            }
                        }
                    }).start();
                }else {
                    recyclerView.stopLoadMore();
                }
            }
        });
        recyclerView.postRefreshing();
    }


    private void sendRequest(int what,int i) {
        Request<JSONObject> request=NoHttp.createJsonObjectRequest(Adviset.SchoolRecommend(), RequestMethod.GET);
        request.add("id",31);
        request.add("page",i);
        getRequestQueue().add(what, request, new OnResponseListener<JSONObject>() {
            @Override
            public void onStart(int what) {

            }

            @Override
            public void onSucceed(int what, Response<JSONObject> response) {
                try {
                    JSONObject object=new JSONObject(response.get().toString());
                    mLog.e("reponse","-->"+response.get().toString());
                    /*
                    total=object.getInt("total");
                    lastPage=object.getInt("lastPage");
                    current_page=object.getInt("current_page");
                    */
                    String data=object.getString("data");
                    if(what==1){
                        JSONArray array=new JSONArray(data);
                        if(array.length()>0){
                            mdata.clear();
                            for (int j = 0; j <array.length(); j++) {
                                RecommendNews item=gson.fromJson(array.getJSONObject(j).toString(),RecommendNews.class);
                                mdata.add(item);
                            }
                            handler.sendEmptyMessage(0000);
                        }else {
                            handler.sendEmptyMessage(1111);
                        }
                    }else if(what==2){
                        JSONArray array=new JSONArray(data);
                        if(array.length()>0){
                            for (int j = 0; j <array.length(); j++) {
                                RecommendNews item=gson.fromJson(array.getJSONObject(j).toString(),RecommendNews.class);
                                mdata.add(item);
                            }
                            handler.sendEmptyMessage(0000);
                        }else {
                            handler.sendEmptyMessage(2222);
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
    protected void stopLoad() {

    }

}
