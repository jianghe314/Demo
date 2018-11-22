package com.kangzhan.student.Teacher.Fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.holenzhou.pullrecyclerview.PullRecyclerView;
import com.kangzhan.student.Debug.mLog;
import com.kangzhan.student.R;
import com.kangzhan.student.ShowUI.mToast;
import com.kangzhan.student.ShowUI.showMessage;
import com.kangzhan.student.ShowUI.showProgress;
import com.kangzhan.student.Student.mutils.DividerItemDecoration;
import com.kangzhan.student.Teacher.Adapter.TeacherClassAdapter;
import com.kangzhan.student.Teacher.bean.TeacherClass;
import com.kangzhan.student.com.BaseFragment;
import com.kangzhan.student.com.LazyFragment;
import com.kangzhan.student.mInterface.TeacherInterface.teacher;
import com.kangzhan.student.utils.mCalendar.mCalender;
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
 * Created by kangzhan011 on 2017/4/7.
 */

public class ClassFragment extends Fragment implements View.OnClickListener{
    private View view;
    private  int[] mm=mCalender.getCalender();
    private TextView time;
    private Button btn1,btn2,btn3;
    private TeacherClassAdapter adapter;
    private ArrayList<TeacherClass> mdata=new ArrayList<>();
    private ArrayList<TeacherClass> dataA=new ArrayList<>();
    private ArrayList<TeacherClass> dataB=new ArrayList<>();
    private ArrayList<TeacherClass> dataC=new ArrayList<>();
    private SwipeRefreshLayout refresh;
    private RecyclerView recycler;
    private RelativeLayout topMenu;
    private LinearLayout bottomMenu;
    private RequestQueue requestQueue;
    private Gson gson;
    private String mmsg;
    private ArrayList<String> params=new ArrayList<>();
    private ArrayList<String> values=new ArrayList<>();
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0000:
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            refresh.setRefreshing(false);
                            showProgress.closeProgress();
                            adapter.notifyDataSetChanged();
                        }
                    });
                    break;
                case 1111:
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //暂停刷新
                            refresh.setRefreshing(false);
                            showProgress.closeProgress();
                            adapter.notifyDataSetChanged();
                            mToast.showText(getActivity().getApplicationContext(),mmsg);
                        }
                    });
                    break;
                case 2222:
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //暂停刷新
                            refresh.setRefreshing(false);
                            showProgress.closeProgress();
                            mToast.showText(getActivity().getApplicationContext(),"服务器打盹，请稍后再试");
                        }
                    });
                    break;
                case 3333:
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.showProgress(getContext(),"同步中...");
                        }
                    });
                    break;
                case 4444:
                    //修改数据后，要加载一次刷新
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.closeProgress();
                            AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
                            builder.setMessage(mmsg);
                            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            params.clear();
                                            values.clear();
                                            params.add("key");
                                            params.add("date");
                                            values.add(teacher.teacherKey(getContext().getApplicationContext()));
                                            values.add(getTime());
                                            sendRequest("GET",teacher.teacherMyLesson(),1,params,values);
                                        }
                                    }).start();
                                }
                            });
                            builder.create().show();
                        }
                    });

                    break;
                case 9999:
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            refresh.setRefreshing(false);
                            showProgress.closeProgress();
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
            view=inflater.inflate(R.layout.teacher_class_layout,container,false);
            requestQueue=NoHttp.newRequestQueue();
            gson=new Gson();
            initView(view);
        }
        return view;
    }
    private void initView(View v) {
        time= (TextView) v.findViewById(R.id.teacher_class_date_time);
        time.setOnClickListener(this);
        time.setText(mm[0]+"年"+mm[1]+"月"+mm[2]+"日");
        btn1= (Button) v.findViewById(R.id.teacher_class_btn1);
        btn1.setOnClickListener(this);
        btn2= (Button) v.findViewById(R.id.teacher_class_btn2);
        btn2.setOnClickListener(this);
        btn3= (Button) v.findViewById(R.id.teacher_class_btn3);
        btn3.setOnClickListener(this);
        adapter=new TeacherClassAdapter(getContext(),mdata);
        refresh= (SwipeRefreshLayout) v.findViewById(R.id.teacher_class_refrsh);
        refresh.setOnClickListener(this);
        recycler= (RecyclerView) v.findViewById(R.id.teacher_class_list);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        recycler.setAdapter(adapter);
        recycler.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL_LIST));
        refresh.setColorSchemeResources(R.color.swipe_color1,R.color.swipe_color2,R.color.swipe_color3);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //刷新操作
                        params.clear();
                        values.clear();
                        params.add("key");
                        params.add("date");
                        values.add(teacher.teacherKey(getContext().getApplicationContext()));
                        values.add(getTime());
                        sendRequest("GET",teacher.teacherMyLesson(),1,params,values);
                    }
                }).start();
            }
        });
        refresh.setRefreshing(true);
        new Thread(new Runnable() {
            @Override
            public void run() {
                params.clear();
                values.clear();
                params.add("key");
                params.add("date");
                values.add(teacher.teacherKey(getContext().getApplicationContext()));
                values.add(getTime());
                sendRequest("GET",teacher.teacherMyLesson(),1,params,values);
            }
        }).start();

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
                mLog.e("reponse","--->"+response.get().toString());
                try {
                    JSONObject object=new JSONObject(response.get().toString());
                    mmsg=object.getString("msg");
                    if(what==1){
                        if(object.getString("code").equals("200")){
                            mdata.clear();
                            String data1=object.getString("subdata");
                            JSONObject obj11=new JSONObject(data1);
                            JSONArray arraya=new JSONArray(obj11.getString("subjecta"));
                            JSONArray arrayb=new JSONArray(obj11.getString("subjectb"));
                            JSONArray arrayc=new JSONArray(obj11.getString("subjectc"));
                            //
                            if(arraya.length()>0){
                                dataA.clear();
                                for (int i = 0; i < arraya.length(); i++) {
                                    TeacherClass class1=gson.fromJson(arraya.getJSONObject(i).toString(),TeacherClass.class);
                                    class1.setType("a");
                                    dataA.add(class1);
                                }
                            }else {
                                dataA.clear();
                            }
                            //
                            if(arrayb.length()>0){
                                dataB.clear();
                                for (int i = 0; i <arrayb.length(); i++) {
                                    TeacherClass class2=gson.fromJson(arrayb.getJSONObject(i).toString(),TeacherClass.class);
                                    class2.setType("b");
                                    dataB.add(class2);
                                }
                            }else {
                                dataB.clear();
                            }
                            //
                            if(arrayc.length()>0){
                                dataC.clear();
                                for (int i = 0; i < arrayc.length(); i++) {
                                    TeacherClass class3=gson.fromJson(arrayc.getJSONObject(i).toString(),TeacherClass.class);
                                    class3.setType("c");
                                    dataC.add(class3);
                                }
                            }else {
                                dataC.clear();
                            }
                            //数据汇总
                            if(dataA.size()>0){
                                for (int i = 0; i <dataA.size(); i++) {
                                    mdata.add(dataA.get(i));
                                }
                            }
                            if(dataB.size()>0){
                                for (int i = 0; i < dataB.size(); i++) {
                                    mdata.add(dataB.get(i));
                                }
                            }
                            if(dataC.size()>0){
                                for (int i = 0; i < dataC.size(); i++) {
                                    mdata.add(dataC.get(i));
                                }
                            }
                            mLog.e("mdata","->"+mdata.size());
                            handler.sendEmptyMessage(0000);
                        }else {
                            handler.sendEmptyMessage(1111);
                        }
                    }else if(what==2){
                        handler.sendEmptyMessage(4444);
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
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.teacher_class_date_time:
                com.kangzhan.student.utils.picker.DatePicker picker=new com.kangzhan.student.utils.picker.DatePicker(getActivity());
                picker.setRangeStart(2016,1,1);
                picker.setRangeEnd(2050,10,14);
                picker.setSelectedItem(mm[0],mm[1],mm[2]);
                picker.setOnDatePickListener(new com.kangzhan.student.utils.picker.DatePicker.OnYearMonthDayPickListener() {
                    @Override
                    public void onDatePicked(String year, String month, String day) {
                        time.setText(year+"年"+month+"月"+day+"日");
                        handler.sendEmptyMessage(3333);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                params.clear();
                                values.clear();
                                params.add("key");
                                params.add("date");
                                values.add(teacher.teacherKey(getContext().getApplicationContext()));
                                values.add(getTime());
                                sendRequest("GET",teacher.teacherMyLesson(),1,params,values);
                            }
                        }).start();
                    }
                });
                picker.show();
                break;
            case R.id.teacher_class_btn1:
                //休息
                ArrayList<TeacherClass> isclick1=new ArrayList<>();
                for (int i = 0; i <mdata.size(); i++) {
                    if( mdata.get(i).isclick()){
                        isclick1.add(mdata.get(i));
                        mLog.e("Tag","->"+i);
                    }
                }
                //打包JSon数据
                adapter.notifyDataSetChanged();
                //备注：修改了的信息要将他设置到实体类中
                if(isclick1.size()>0){
                    final StringBuilder builder=new StringBuilder();
                    for (int i = 0; i <isclick1.size(); i++) {
                        builder.append(isclick1.get(i).getSubj_id());
                        builder.append(",");
                    }
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            handler.sendEmptyMessage(3333);
                            params.clear();
                            values.clear();
                            params.add("key");
                            params.add("subject_ids");
                            values.add(teacher.teacherKey(getContext().getApplicationContext()));
                            values.add(builder.toString().substring(0,builder.toString().length()-1));
                            sendRequest("POST",teacher.teacherHaveRest(),2,params,values);
                        }
                    }).start();
                }else {
                    mToast.showText(getActivity().getApplicationContext(),"没有选中修改项");
                }
                break;
            case R.id.teacher_class_btn3:
                //取消休息
                ArrayList<TeacherClass> isclick2=new ArrayList<>();
                for (int i = 0; i <mdata.size(); i++) {
                    if( mdata.get(i).isclick()){
                        isclick2.add(mdata.get(i));
                        mLog.e("Tag","->"+i);
                    }
                }
                //打包JSon数据
                adapter.notifyDataSetChanged();
                //备注：修改了的信息要将他设置到实体类中
                if(isclick2.size()>0){
                    final StringBuilder builder=new StringBuilder();
                    for (int i = 0; i <isclick2.size(); i++) {
                        builder.append(isclick2.get(i).getSubj_id());
                        builder.append(",");
                    }
                    mLog.e("选中-》","-"+builder.toString());
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            handler.sendEmptyMessage(3333);
                            params.clear();
                            values.clear();
                            params.add("key");
                            params.add("subject_ids");
                            values.add(teacher.teacherKey(getContext().getApplicationContext()));
                            values.add(builder.toString().substring(0,builder.toString().length()-1));
                            sendRequest("POST",teacher.teacherCancelRest(),2,params,values);
                        }
                    }).start();
                }else {
                    mToast.showText(getActivity().getApplicationContext(),"没有选中修改项");
                }
                break;
            case R.id.teacher_class_btn2:
                //保存修改
                ArrayList<TeacherClass> isclick=new ArrayList<>();
                for (int i = 0; i <mdata.size(); i++) {
                    if( mdata.get(i).isclick()){
                        isclick.add(mdata.get(i));
                        mLog.e("Tag","->"+i);
                    }
                }
                //打包JSon数据
                adapter.notifyDataSetChanged();
                //备注：修改了的信息要将他设置到实体类中
                if(isclick.size()>0){
                    final JSONArray array=new JSONArray();
                    for (int i = 0; i < isclick.size(); i++) {
                        TeacherClass mclass=isclick.get(i);
                        JSONObject object=new JSONObject();
                        try {
                            object.put("id",mclass.getSubj_id());
                            object.put("stage",mclass.getStage());
                            object.put("licnum",mclass.getLicnum());
                            object.put("price",mclass.getPrice());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        array.put(object);
                    }
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            handler.sendEmptyMessage(3333);
                            sendInfo(teacher.teacherKey(getContext().getApplicationContext()),array.toString());
                        }
                    }).start();
                }else {
                    mToast.showText(getActivity().getApplicationContext(),"没有选中修改项");
                }
                break;
        }
    }

    private void sendInfo(String key,String obj){
        Request<JSONObject> request=NoHttp.createJsonObjectRequest(teacher.teacherChangeLesson(),RequestMethod.GET);
        request.add("key",key);
        request.add("obj",obj);
        mLog.e("key","-->"+key);
        mLog.e("obj","-->"+obj);
        requestQueue.add(3, request, new OnResponseListener<JSONObject>() {
            @Override
            public void onStart(int what) {

            }

            @Override
            public void onSucceed(int what, Response<JSONObject> response) {
                try {
                    mLog.e("reponse","-->"+response.get().toString());
                    JSONObject object=new JSONObject(response.get().toString());
                    mmsg=object.getString("msg");
                    handler.sendEmptyMessage(4444);
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


    private String getTime(){
        String getTime=time.getText().toString().trim();
        getTime=getTime.replace("年","-");
        getTime=getTime.replace("月","-");
        getTime=getTime.replace("日","");
        return getTime;
    }
}
