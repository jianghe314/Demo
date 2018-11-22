package com.kangzhan.student.Teacher.person_data;

import android.os.Bundle;
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
import com.kangzhan.student.Student.mutils.DividerItemDecoration;
import com.kangzhan.student.Teacher.Adapter.MyStudentAdapter1;
import com.kangzhan.student.Teacher.bean.TeacherMyStudent;
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
 * Created by kangzhan011 on 2017/5/2.
 */

public class MyStudentFragment1 extends Fragment {
    private View view;
    private RequestQueue requestQueue;
    private Gson gson;
    private TextView sum;
    private PullRecyclerView recycler;
    private MyStudentAdapter1 adapter;
    private ArrayList<TeacherMyStudent> mdata=new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(view==null){
            view=inflater.inflate(R.layout.item_teacher_mystudent1,container,false);
            requestQueue=NoHttp.newRequestQueue();
            gson=new Gson();
            initView(view);
        }
        return view;
    }

    private void initView(View  v) {
        sum= (TextView) v.findViewById(R.id.teacher_myStudent_sum);
        recycler= (PullRecyclerView) v.findViewById(R.id.teacher_myStudent_l1);
        adapter=new MyStudentAdapter1(getContext(),R.layout.item_teacher_mystudent,mdata);
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
                sendRequest();
                recycler.stopRefresh();
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
        requestQueue=NoHttp.newRequestQueue();
        Request<JSONObject> request=NoHttp.createJsonObjectRequest(teacher.teacherMyStudent(), RequestMethod.GET);
        request.add("key",teacher.teacherKey(getContext().getApplicationContext()));
        request.add("order_type",1);
        requestQueue.add(1, request, new OnResponseListener<JSONObject>() {
            @Override
            public void onStart(int what) {

            }

            @Override
            public void onSucceed(int what, Response<JSONObject> response) {
                mLog.i("response","->"+response.get().toString());
                try {
                    JSONObject object=new JSONObject(response.get().toString());
                    String code=object.getString("code");
                    if(code.equals("200")){
                        String data=object.getString("student");
                        JSONArray array=new JSONArray(data);
                        if(array.length()>0){
                            mdata.clear();
                            for (int i = 0; i <array.length(); i++) {
                                JSONObject obj=array.getJSONObject(i);
                                TeacherMyStudent stu=gson.fromJson(obj.toString(),TeacherMyStudent.class);
                                mdata.add(stu);
                            }
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    sum.setText(mdata.size()+"");
                                }
                            });
                            adapter.notifyDataSetChanged();
                        }
                    }else {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(int what, Response<JSONObject> response) {

            }

            @Override
            public void onFinish(int what) {

            }
        });
    }
}
