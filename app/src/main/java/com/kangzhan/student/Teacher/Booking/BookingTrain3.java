package com.kangzhan.student.Teacher.Booking;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.holenzhou.pullrecyclerview.PullRecyclerView;
import com.holenzhou.pullrecyclerview.layoutmanager.XLinearLayoutManager;
import com.kangzhan.student.Debug.mLog;
import com.kangzhan.student.R;
import com.kangzhan.student.ShowUI.mToast;
import com.kangzhan.student.Student.mutils.DividerItemDecoration;
import com.kangzhan.student.Teacher.Adapter.TeacherBookingTrain1Adapter;
import com.kangzhan.student.Teacher.Adapter.TeacherBookingTrain3Adapter;
import com.kangzhan.student.Teacher.bean.TeacherBookingTrain1;
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
 * Created by kangzhan011 on 2017/5/9.
 */

public class BookingTrain3 extends Fragment {
    private Gson gson;
    private RequestQueue requestQueue;
    private TextView person,hour;
    private PullRecyclerView recycler;
    private View view;
    private String mmsg;
    private TeacherBookingTrain3Adapter adapter;
    private ArrayList<TeacherBookingTrain1> mdata3=new ArrayList<>();
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1111:
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            recycler.stopRefresh();
                            person.setText(mdata3.size()+"");
                            //hour.setText(100+"");
                            adapter.notifyDataSetChanged();
                        }
                    });
                    break;
                case 2222:
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            recycler.stopRefresh();
                            adapter.notifyDataSetChanged();
                            mToast.showText(getActivity().getApplicationContext(),mmsg);
                        }
                    });
                    break;
                case 9999:
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            recycler.stopRefresh();
                            mToast.showText(getActivity().getApplicationContext(),"加载失败，请稍后再试");
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
            view=inflater.inflate(R.layout.teacher_booking_train3,container,false);
            requestQueue=NoHttp.newRequestQueue();
            gson=new Gson();
            initView(view);
        }
        return view;
    }

    private void initView(View v) {
        person= (TextView) v.findViewById(R.id.teacher_booking_train3_sum);
        //hour= (TextView) v.findViewById(R.id.teacher_booking_train3_hours);
        adapter=new TeacherBookingTrain3Adapter(getContext(),R.layout.item_teacher_hastrain,mdata3);
        recycler= (PullRecyclerView) v.findViewById(R.id.teacher_booking_train3_recycler);
        recycler.setAdapter(adapter);
        recycler.setEmptyView(LayoutInflater.from(getContext()).inflate(R.layout.item_no_data,null));
        recycler.setLayoutManager(new XLinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false));
        recycler.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL_LIST));
        recycler.setColorSchemeResources(R.color.swipe_color1,R.color.swipe_color2,R.color.swipe_color3);
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

    private void sendRequest() {
        Request<JSONObject> request=NoHttp.createJsonObjectRequest(teacher.teacherHasTrain(), RequestMethod.GET);
        request.add("key",teacher.teacherKey(getContext().getApplicationContext()));
        request.add("page",1);
        requestQueue.add(1, request, new OnResponseListener<JSONObject>() {
            @Override
            public void onStart(int what) {

            }

            @Override
            public void onSucceed(int what, Response<JSONObject> response) {
                mLog.i("response3","->"+response.get().toString());
                try {
                    JSONObject object=new JSONObject(response.get().toString());
                    mmsg=object.getString("msg");
                    if(object.getString("code").equals("200")){
                        String data=object.getString("data");
                        JSONArray array=new JSONArray(data);
                        if(array.length()>0){
                            mdata3.clear();
                            for (int i = 0; i <array.length(); i++) {
                                JSONObject obj=array.getJSONObject(i);
                                TeacherBookingTrain1 train=gson.fromJson(obj.toString(),TeacherBookingTrain1.class);
                                mdata3.add(train);
                            }
                            handler.sendEmptyMessage(1111);
                        }
                    }else {
                        mdata3.clear();
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
        super.onDestroy();
        if(requestQueue!=null){
            requestQueue.cancelAll();
            requestQueue.stop();
        }

    }
}
