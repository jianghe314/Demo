package com.kangzhan.student.Teacher.Train;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.holenzhou.pullrecyclerview.PullRecyclerView;
import com.holenzhou.pullrecyclerview.layoutmanager.XLinearLayoutManager;
import com.kangzhan.student.Debug.mLog;
import com.kangzhan.student.R;
import com.kangzhan.student.ShowUI.mToast;
import com.kangzhan.student.ShowUI.showMessage;
import com.kangzhan.student.ShowUI.showProgress;
import com.kangzhan.student.Teacher.Adapter.TeacherTrainRecordAdapter;
import com.kangzhan.student.Teacher.bean.TeacherTrainRecord;
import com.kangzhan.student.com.BaseActivity;
import com.kangzhan.student.mInterface.TeacherInterface.teacher;
import com.kangzhan.student.utils.mCalendar.mCalender;
import com.kangzhan.student.utils.picker.DatePicker;
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

import static com.yanzhenjie.nohttp.NoHttp.getContext;

public class Teacher_TrainRecordActivity extends BaseActivity implements View.OnClickListener{
    private TextView Num,Price,search,setStartT,setEndT;
    private int[] mm= mCalender.getCalender();
    private PullRecyclerView recycler;
    private TeacherTrainRecordAdapter adapter;
    private ArrayList<TeacherTrainRecord> mdata=new ArrayList<>();
    private EditText searchContent;
    private String mmsg;
    private String Count,Prices;
    private Gson gson;
    private int mpage=1;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0000:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.showProgress(Teacher_TrainRecordActivity.this,"搜索中...");
                        }
                    });
                    break;
                case 1111:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.closeProgress();
                            recycler.stopRefresh();
                            recycler.stopLoadMore();
                            searchContent.setText("");
                            Num.setText(Count);
                            Price.setText(Prices);
                            adapter.notifyDataSetChanged();
                        }
                    });
                    break;
                case 2222:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.closeProgress();
                            recycler.stopRefresh();
                            adapter.notifyDataSetChanged();
                            AlertDialog.Builder builder=new AlertDialog.Builder(Teacher_TrainRecordActivity.this);
                            builder.setMessage(mmsg);
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
                case 5555:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            recycler.stopLoadMore();
                            mToast.showText(getApplicationContext(),"没有更多了！");
                        }
                    });
                    break;
                case 9999:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.closeProgress();
                            recycler.stopRefresh();
                            adapter.notifyDataSetChanged();
                            showMessage.showMsg(Teacher_TrainRecordActivity.this,"网络连接异常，请检测网络");
                        }
                    });
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher__train_record);
        Toolbar toolbar= (Toolbar) findViewById(R.id.teacher_train_record_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        gson=new Gson();
        iniView();
    }

    private void iniView() {
        Num= (TextView) findViewById(R.id.teacher_trainRecord_num);
        Price= (TextView) findViewById(R.id.teacher_trainRecord_hours);
        searchContent= (EditText) findViewById(R.id.teacher_train_search_content);
        search= (TextView) findViewById(R.id.teacher_train_Tosearch);
        search.setOnClickListener(this);
        setStartT= (TextView) findViewById(R.id.teacher_train_setStartT);
        setEndT= (TextView) findViewById(R.id.teacher_train_setEndT);
        setEndT.setOnClickListener(this);
        setStartT.setText(mm[0]+"年"+mm[1]+"月"+mm[2]+"日");
        setEndT.setText(mm[0]+"年"+mm[1]+"月"+mm[2]+"日");
        setStartT.setOnClickListener(this);
        adapter=new TeacherTrainRecordAdapter(Teacher_TrainRecordActivity.this,R.layout.item_teacher_train_record,mdata);
        recycler= (PullRecyclerView) findViewById(R.id.teacher_train_order_recycler);
        recycler.setAdapter(adapter);
        recycler.setEmptyView(LayoutInflater.from(this).inflate(R.layout.item_no_data,null));
        recycler.setLayoutManager(new XLinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false));
        recycler.setColorSchemeResources(R.color.swipe_color1,R.color.swipe_color2,R.color.swipe_color3);
        recycler.enablePullRefresh(true);       //设置可以下拉
        recycler.enableLoadMore(true);
        recycler.setOnRecyclerRefreshListener(new PullRecyclerView.OnRecyclerRefreshListener() {
            @Override
            public void onPullRefresh() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        sendRequest(1,"","","","");
                    }
                }).start();
            }

            @Override
            public void onLoadMore() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        mpage=mpage+1;
                        sendRequest(3,"","","",mpage+"");
                    }
                }).start();
            }
        });
        recycler.postRefreshing();
    }

    private void sendRequest(final int way, String signStartTime, String signEndtTime, String searchCriteria,String page) {
        //1:普通请求    2：搜索请求
        Request<JSONObject> request=NoHttp.createJsonObjectRequest(teacher.teacherTrainRecord(), RequestMethod.GET);
        request.add("key",teacher.teacherKey(getApplicationContext()));
        request.add("signStartTime",signStartTime);
        request.add("signEndtTime",signEndtTime);
        request.add("searchCriteria",searchCriteria);
        request.add("page",page);
        getRequestQueue().add(way, request, new OnResponseListener<JSONObject>() {
            @Override
            public void onStart(int what) {

            }
            @Override
            public void onSucceed(int what, Response<JSONObject> response) {
                mLog.i("response","->"+response);
                try {
                    JSONObject object=new JSONObject(response.get().toString());
                    mmsg=object.getString("msg");
                    if(way==1||way==2){
                        if(object.getString("code").equals("200")){
                            Count=object.getString("totalCount");
                            Prices=object.getString("totalPrice");
                            JSONArray array=new JSONArray(object.getString("data"));
                            if(array.length()>0){
                                mdata.clear();
                                for (int i = 0; i <array.length(); i++) {
                                    TeacherTrainRecord item=gson.fromJson(array.getJSONObject(i).toString(),TeacherTrainRecord.class);
                                    mdata.add(item);
                                }
                            }else {
                                mdata.clear();
                            }
                            handler.sendEmptyMessage(1111);
                        }else {
                            handler.sendEmptyMessage(2222);
                        }
                    }else if(way==3){
                        if(object.getString("code").equals("200")){
                            Count=object.getString("totalCount");
                            Prices=object.getString("totalPrice");
                            JSONArray array=new JSONArray(object.getString("data"));
                            if(array.length()>0){
                                for (int i = 0; i <array.length(); i++) {
                                    TeacherTrainRecord item=gson.fromJson(array.getJSONObject(i).toString(),TeacherTrainRecord.class);
                                    mdata.add(item);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.teacher_train_Tosearch:
                handler.sendEmptyMessage(0000);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        mLog.e("request","->1");
                        String startT=setStartT.getText().toString().trim().replace("年","-");
                        startT=startT.replace("月","-");
                        startT=startT.replace("日","");
                        String endT=setEndT.getText().toString().trim().replace("年","-");
                        endT=endT.replace("月","-");
                        endT=endT.replace("日","");
                        mLog.e("request","->2");
                        sendRequest(2,startT,endT,searchContent.getText().toString().trim(),"");
                    }
                }).start();
                //搜索
                break;
            case R.id.teacher_train_setStartT:
                //设置开始时间
                DatePicker picker = new DatePicker(this);
                picker.setRangeStart(2016, 8, 29);
                picker.setRangeEnd(2050, 1, 11);
                picker.setSelectedItem(mm[0], mm[1], mm[2]);
                picker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
                    @Override
                    public void onDatePicked(String year, String month, String day) {
                        setStartT.setText(year+"年"+month+"月"+day+"日");
                    }
                });
                picker.show();
                break;
            case R.id.teacher_train_setEndT:
                //设置结束时间
                DatePicker picker2 = new DatePicker(this);
                picker2.setRangeStart(2016, 8, 29);
                picker2.setRangeEnd(2050, 1, 11);
                picker2.setSelectedItem(mm[0], mm[1], mm[2]);
                picker2.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
                    @Override
                    public void onDatePicked(String year, String month, String day) {
                        setEndT.setText(year+"年"+month+"月"+day+"日");
                    }
                });
                picker2.show();
                break;
            default:
                break;
        }
    }

}
