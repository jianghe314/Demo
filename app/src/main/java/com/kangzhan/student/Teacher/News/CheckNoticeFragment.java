package com.kangzhan.student.Teacher.News;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.holenzhou.pullrecyclerview.PullRecyclerView;
import com.holenzhou.pullrecyclerview.layoutmanager.XLinearLayoutManager;
import com.kangzhan.student.Debug.mLog;
import com.kangzhan.student.R;
import com.kangzhan.student.ShowUI.mToast;
import com.kangzhan.student.ShowUI.showMessage;
import com.kangzhan.student.ShowUI.showProgress;
import com.kangzhan.student.Teacher.Adapter.TeacherMsgCheckNoticeAdapter;
import com.kangzhan.student.Teacher.bean.TeacherMsgCheckNotice;
import com.kangzhan.student.com.BaseFragment;
import com.kangzhan.student.com.LazyFragment;
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

/**
 * Created by kangzhan011 on 2017/4/14.
 */

public class CheckNoticeFragment extends Fragment {
    private PullRecyclerView recyclerView;
    private View view;
    private RequestQueue requestQueue;
    private Gson gson;
    private String mmsg;
    private ArrayList<TeacherMsgCheckNotice> mdata=new ArrayList<>();
    private TeacherMsgCheckNoticeAdapter adapter;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1111:
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            recyclerView.stopRefresh();
                            adapter.notifyDataSetChanged();
                        }
                    });
                    break;
                case 2222:
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            recyclerView.stopRefresh();
                            adapter.notifyDataSetChanged();
                            mToast.showText(getActivity().getApplicationContext(),mmsg);
                        }
                    });
                    break;
                case 9999:
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            recyclerView.stopRefresh();
                            showMessage.showMsg(getContext(),"网络连接异常，请检测网络连接");
                        }
                    });
                    break;
                default:
                    break;
            }
        }
    };
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(view==null){
            view=inflater.inflate(R.layout.teacher_news_checknoticefragment_layout,container,false);
            requestQueue=NoHttp.newRequestQueue();
            gson=new Gson();
            initView(view);
        }
        return view;
    }

    private void initView(View v) {
        recyclerView= (PullRecyclerView) v.findViewById(R.id.teacher_news_checknotice_recycler);
        adapter=new TeacherMsgCheckNoticeAdapter(getContext(),R.layout.item_teacher_news_checknotice,mdata);
        recyclerView.setAdapter(adapter);
        recyclerView.setEmptyView(LayoutInflater.from(getContext()).inflate(R.layout.item_no_data,null));
        recyclerView.setLayoutManager(new XLinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false));
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
                        sendRequest();
                    }
                }).start();

            }
            // 模拟上拉加载更多网络请求
            @Override
            public void onLoadMore() {
                recyclerView.stopLoadMore();
            }
        });
        recyclerView.postRefreshing();
    }

    private void sendRequest() {
        Request<JSONObject> request=NoHttp.createJsonObjectRequest(teacher.teacherNewsCheckNotice(), RequestMethod.GET);
        request.add("key",teacher.teacherKey(getContext().getApplicationContext()));
        request.add("receiver_type",20);
        request.add("receiver_id",1);
        requestQueue.add(1, request, new OnResponseListener<JSONObject>() {
            @Override
            public void onStart(int what) {

            }

            @Override
            public void onSucceed(int what, Response<JSONObject> response) {
                mLog.e("response","->"+response.get().toString().trim());
                try {
                    JSONObject object=new JSONObject(response.get().toString());
                    mmsg=object.getString("msg");
                    if(object.getString("code").equals("200")){
                        String data=object.getString("data");
                        JSONArray array=new JSONArray(data);
                        if(array.length()>0){
                            mdata.clear();
                            for (int i = 0; i <array.length() ; i++) {
                                JSONObject obje=array.getJSONObject(i);
                                TeacherMsgCheckNotice notice=gson.fromJson(obje.toString(),TeacherMsgCheckNotice.class);
                                mdata.add(notice);
                            }
                            handler.sendEmptyMessage(1111);
                        }
                    }else {
                        mdata.clear();
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
    public void onDestroy() {
        requestQueue.cancelAll();
        super.onDestroy();
    }
}
