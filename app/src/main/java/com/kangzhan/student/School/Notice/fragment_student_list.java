package com.kangzhan.student.School.Notice;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.holenzhou.pullrecyclerview.PullRecyclerView;
import com.holenzhou.pullrecyclerview.layoutmanager.XLinearLayoutManager;
import com.kangzhan.student.R;
import com.kangzhan.student.School.Adapter.NoticeChoiceStuAdapter;
import com.kangzhan.student.School.Bean.EduStuMange;
import com.kangzhan.student.School.Bean.NoticeChoiceStu;
import com.kangzhan.student.School.Edu.StudentManageActivity;
import com.kangzhan.student.ShowUI.mToast;
import com.kangzhan.student.ShowUI.showMessage;
import com.kangzhan.student.com.LazyFragment;
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

/**
 * Created by kangzhan011 on 2017/6/13.
 */

public class fragment_student_list extends Fragment implements View.OnClickListener {
    private View view;
    private PullRecyclerView recycler;
    private LinearLayout choice,add;
    private ImageView choiceIv;
    private TextView choiceHint,doSearch;
    private EditText searchContent;
    private NoticeChoiceStuAdapter adapter;
    private ArrayList<NoticeChoiceStu> data=new ArrayList<>();
    private RequestQueue requestQueue;
    private Gson gson;
    private String mmsg;
    private int mpage=1;
    private ChoiceStudent choiceStudent;
    private ArrayList<String> params=new ArrayList<>();
    private ArrayList<String> values=new ArrayList<>();
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1111:
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            recycler.stopRefresh();
                            recycler.stopLoadMore();
                            adapter.notifyDataSetChanged();
                        }
                    });
                    break;
                case 2222:
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            recycler.stopRefresh();
                            mToast.showText(getActivity().getApplicationContext(),mmsg);
                        }
                    });
                    break;
                case 5555:
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            recycler.stopLoadMore();
                            mToast.showText(getActivity().getApplicationContext(),"没有更多了！");
                        }
                    });
                    break;
                case 9999:
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            recycler.stopRefresh();
                            showMessage.showMsg(getContext(),"网络连接异常，请检查网络连接");
                        }
                    });
                    break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(view==null){
            view=inflater.inflate(R.layout.fragment_school_notice_stu_list,container,false);
            initView(view);
            requestQueue=NoHttp.newRequestQueue();
            gson=new Gson();
            initData();
        }
        return view;
    }


    private void initView(View v) {
        recycler= (PullRecyclerView) v.findViewById(R.id.school_notice_choiceStu_list_recycler);
        choice= (LinearLayout) v.findViewById(R.id.school_notice_choiceStu_list_choice);
        add= (LinearLayout) v.findViewById(R.id.school_notice_choiceStu_list_sure);
        choiceIv= (ImageView) v.findViewById(R.id.school_notice_choiceStu_list_iv);
        choiceIv.setTag(0);
        choiceHint= (TextView) v.findViewById(R.id.school_notice_choiceStu_list_txt);
        doSearch= (TextView) v.findViewById(R.id.school_notice_choiceStu_list_search);
        searchContent= (EditText) v.findViewById(R.id.school_notice_choiceStu_list_content);
        adapter=new NoticeChoiceStuAdapter(getContext(),R.layout.item_school_notice_choice_student,data);
        choice.setOnClickListener(this);
        doSearch.setOnClickListener(this);
        add.setOnClickListener(this);
    }
    private void initData() {
        recycler.setAdapter(adapter);
        recycler.setEmptyView(LayoutInflater.from(getContext()).inflate(R.layout.item_no_data,null));
        recycler.setLayoutManager(new XLinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false));
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
                        params.clear();
                        values.clear();
                        //参数
                        params.add("key");
                        params.add("type");
                        //值
                        values.add(school.schoolKey(getActivity().getApplicationContext()));
                        values.add("2");
                        sendRequest("GET",school.SchoolNoticeChoiceStu(),1,params,values);
                    }
                }).start();

            }
            // 模拟上拉加载更多网络请求
            @Override
            public void onLoadMore() {
                recycler.stopLoadMore();
                /*
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        mpage=mpage+1;
                        params.clear();
                        values.clear();
                        //参数
                        params.add("key");
                        params.add("type");
                        params.add("page");
                        //值
                        values.add(school.schoolKey(getActivity()));
                        values.add("2");
                        values.add(mpage+"");
                        sendRequest("GET",school.SchoolNoticeChoiceStu(),3,params,values);
                    }
                }).start();
                */
            }
        });
        recycler.postRefreshing();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.school_notice_choiceStu_list_choice:
                if(data.size()>0){
                    int flags= (int) choiceIv.getTag();
                    if(flags==0){
                        choiceIv.setImageResource(R.mipmap.choiceall1);
                        choiceIv.setTag(1);
                        choiceHint.setText("取消");
                        for (int i = 0; i < data.size(); i++) {
                            data.get(i).setClick(true);
                        }
                    }else {
                        choiceIv.setImageResource(R.mipmap.choiceall0);
                        choiceIv.setTag(0);
                        choiceHint.setText("全选");
                        for (int i = 0; i < data.size(); i++) {
                            data.get(i).setClick(false);
                        }
                    }
                    adapter.notifyDataSetChanged();
                }
                break;
            case R.id.school_notice_choiceStu_list_search:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        params.clear();
                        values.clear();
                        //参数
                        params.add("key");
                        params.add("type");
                        params.add("searchCriteria");
                        //值
                        values.add(school.schoolKey(getActivity().getApplicationContext()));
                        values.add("2");
                        values.add(searchContent.getText().toString().trim());
                        sendRequest("GET",school.SchoolNoticeChoiceStu(),1,params,values);
                    }
                }).start();
                break;
            case R.id.school_notice_choiceStu_list_sure:
                //添加
                mToast.showText(getActivity().getApplicationContext(),"添加成功");
                if(choiceStudent!=null){
                    choiceStudent.choiceStudent(data);
                }
                break;
            default:
                break;
        }
    }

    private void sendRequest(String Way,String url,int what,ArrayList<String> params,ArrayList<String> values) {
        RequestMethod method=null;
        if(Way.equals("POST")){
            method=RequestMethod.POST;
        }else if(Way.equals("GET")){
            method=RequestMethod.GET;
        }
        Request<JSONObject> request=NoHttp.createJsonObjectRequest(url,method);
        for (int i = 0; i <params.size(); i++) {
            request.add(params.get(i),values.get(i));
        }
        requestQueue.add(what, request, new OnResponseListener<JSONObject>() {
            @Override
            public void onStart(int what) {

            }

            @Override
            public void onSucceed(int what, Response<JSONObject> response) {
                try {
                    JSONObject object = new JSONObject(response.get().toString());
                    mmsg=object.getString("msg");
                    if(what==1){
                        if(object.getString("code").equals("200")){
                            JSONArray array=new JSONArray(object.getString("data"));
                            if(array.length()>0){
                                data.clear();
                                for (int i = 0; i < array.length(); i++) {
                                    NoticeChoiceStu mange=gson.fromJson(array.getJSONObject(i).toString(),NoticeChoiceStu.class);
                                    data.add(mange);
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
                    }else if(what==3){
                        if(object.getString("code").equals("200")){
                            JSONArray array=new JSONArray(object.getString("data"));
                            if(array.length()>0) {
                                for (int i = 0; i < array.length(); i++) {
                                    NoticeChoiceStu mange = gson.fromJson(array.getJSONObject(i).toString(), NoticeChoiceStu.class);
                                    data.add(mange);
                                }
                            }
                            handler.sendEmptyMessage(1111);
                        }else {
                            handler.sendEmptyMessage(5555);
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
    public interface ChoiceStudent{
        void choiceStudent(ArrayList<NoticeChoiceStu> data);
    }
    public void FragmentChoiceStudent(ChoiceStudent choiceStudent){
        this.choiceStudent=choiceStudent;
    }



}
