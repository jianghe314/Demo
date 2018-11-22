package com.kangzhan.student.Student.Booking;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.kangzhan.student.Debug.mLog;
import com.kangzhan.student.R;
import com.kangzhan.student.ShowUI.mToast;
import com.kangzhan.student.ShowUI.showMessage;
import com.kangzhan.student.ShowUI.showProgress;
import com.kangzhan.student.Student.Adapter.ChoiceDateAdapter;
import com.kangzhan.student.Student.Adapter.TeacherLessonAdapter;
import com.kangzhan.student.Student.bean.ChoiceLesson;
import com.kangzhan.student.Student.bean.TeacherLesson;
import com.kangzhan.student.Student.bean.ChoiceDate;
import com.kangzhan.student.Student.bean.TeacherDetail;
import com.kangzhan.student.com.BaseActivity;
import com.kangzhan.student.mInterface.StudentInterface.student;
import com.kangzhan.student.utils.mCalendar.mCalender;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.Response;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;


public class Teacher_DetailActivity extends BaseActivity implements View.OnClickListener{
    //时间选择器布局为item_list_choice_date_adapter
    private CircleImageView header;
    private TextView name,address,school,myself;
    private RecyclerView recycler,recyclerLesson;
    private Button sendBtn;
    private String Id;
    private String Msg;
    private String MMsg;
    private Gson gson;
    private TeacherDetail teacherDetail;
    private ArrayList<ChoiceDate> mdate=new ArrayList<>();
    private ArrayList<TeacherLesson> lessondata=new ArrayList<>();
    private ArrayList<ChoiceLesson> choiceLesson=new ArrayList<>();
    private ChoiceDateAdapter adapter;
    private TeacherLessonAdapter lessonAdapter;
    private ArrayList<Boolean> isClick=new ArrayList<>();
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0000:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.showProgress(Teacher_DetailActivity.this,"加载中...");
                        }
                    });
                    break;
                case 1111:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.closeProgress();
                            Glide.with(Teacher_DetailActivity.this).load(teacherDetail.getOss_photo()).diskCacheStrategy(DiskCacheStrategy.NONE).error(R.drawable.header).crossFade().into(header);
                            name.setText(teacherDetail.getCoaname());
                            address.setText(teacherDetail.getReginname());
                            school.setText(teacherDetail.getInst_id());
                            myself.setText(teacherDetail.getSelf_description());
                        }
                    });
                    break;
                case 2222:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.closeProgress();
                            mToast.showText(getApplicationContext(),Msg);
                        }
                    });
                    break;
                case 3333:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //通知更改教练课程
                            showProgress.closeProgress();
                            lessonAdapter.notifyDataSetChanged();
                        }
                    });
                    break;
                case 4444:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.showProgress(Teacher_DetailActivity.this,"预约中...");
                        }
                    });
                    break;
                case 5555:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.closeProgress();
                            mToast.showText(getApplicationContext(),MMsg);
                        }
                    });
                    break;
                case 6666:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.closeProgress();
                            lessonAdapter.notifyDataSetChanged();
                            mToast.showText(getApplicationContext(),"当前没有课程");
                        }
                    });
                    break;
                case 9999:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.closeProgress();
                            showMessage.showMsg(getApplicationContext(),"网络连接异常，请检查网络连接");
                        }
                    });
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher__detail);
        Toolbar toolbar= (Toolbar) findViewById(R.id.student_teacherdetail_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        initData();
        initView();
        gson=new Gson();
        Intent intent=getIntent();
        Id=intent.getStringExtra("id");
        handler.sendEmptyMessage(0000);
        new Thread(new Runnable() {
            @Override
            public void run() {
                sendRequest(false,Id,mdate.get(0).getYear()+mdate.get(0).getMonth()+mdate.get(0).getDay(),1);
            }
        }).start();
    }

    private void initData() {
        int[] calendar= mCalender.getCalender();
        DateTime dateTime=new DateTime(calendar[0]+"-"+calendar[1]+"-"+calendar[2]);
        for (int i = 0; i <7; i++) {
            String month=String.valueOf(dateTime.plusDays(i).getMonthOfYear());
            String day=String.valueOf(dateTime.plusDays(i).getDayOfMonth());
            String week=getWeek(String.valueOf(dateTime.plusDays(i).getDayOfWeek()));
            ChoiceDate date=new ChoiceDate();
            date.setYear(String.valueOf(calendar[0]));
            date.setWeek(week);
            date.setDay(day);
            date.setMonth(month);
            mdate.add(date);
        }
    }

    private void initView() {
        header= (CircleImageView) findViewById(R.id.teacher_detail_header);
        sendBtn= (Button) findViewById(R.id.teacher_choiceLesson_send);
        sendBtn.setOnClickListener(this);
        school= (TextView) findViewById(R.id.teacher_detail_school);
        name= (TextView) findViewById(R.id.teacher_detail_name);
        myself= (TextView) findViewById(R.id.teacher_detail_myself);
        address= (TextView) findViewById(R.id.teacher_detail_address);
        recycler= (RecyclerView) findViewById(R.id.teacher_detail_date);
        recyclerLesson= (RecyclerView) findViewById(R.id.teacher_detail_lesson);
        recyclerLesson.setLayoutManager(new GridLayoutManager(Teacher_DetailActivity.this,3));
        lessonAdapter=new TeacherLessonAdapter(Teacher_DetailActivity.this,lessondata,Teacher_DetailActivity.this);
        recyclerLesson.setAdapter(lessonAdapter);
        adapter=new ChoiceDateAdapter(Teacher_DetailActivity.this,mdate,Teacher_DetailActivity.this);
        RecyclerView.LayoutManager manger=new LinearLayoutManager(Teacher_DetailActivity.this,LinearLayoutManager.HORIZONTAL,false);
        recycler.setLayoutManager(manger);
        recycler.setAdapter(adapter);
        adapter.setOnItemClick(new ChoiceDateAdapter.OnItemClick() {
            @Override
            public void onItemClick(final ChoiceDate date, int postion) {
                //获取点击日期
                //获取当期日期下的课程
                handler.sendEmptyMessage(0000);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        mLog.e("request","->"+date.getYear()+"-"+date.getMonth()+"-"+date.getDay());
                        sendRequest(false,Id,date.getYear()+"-"+date.getMonth()+"-"+date.getDay(),2);
                    }
                }).start();
            }
        });

    }

    private void sendChoiceLesson(ArrayList<ChoiceLesson> data) {
        JSONArray array=new JSONArray();
        for (int i = 0; i <data.size(); i++) {
            JSONObject object=new JSONObject();
            try {
                object.put("inst_id",data.get(i).getInst_id());
                object.put("subj_id",data.get(i).getSubj_id());
                object.put("coach_id",data.get(i).getCoach_id());
                object.put("car_id",data.get(i).getCar_id());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            array.put(object);
        }

        Request<JSONObject> request=NoHttp.createJsonObjectRequest(student.studentBookingLesson(),RequestMethod.GET);
        request.add("key",student.studentKey(getApplicationContext()));
        request.add("obj",array.toString().trim());
        mLog.i("最终发送","-->"+array.toString().trim());
        getRequestQueue().add(3, request, new OnResponseListener<JSONObject>() {
            @Override
            public void onStart(int what) {

            }

            @Override
            public void onSucceed(int what, Response<JSONObject> response) {
                mLog.i("response3","->"+response.get().toString());
                if (what==3) {
                    JSONObject object = null;
                    try {
                        object = new JSONObject(response.get().toString().trim());
                        MMsg = object.getString("msg");
                        handler.sendEmptyMessage(5555);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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

    private void sendRequest(final boolean ways, String id, String date, int what) {
        RequestMethod WAY;
        if(ways){        //true:POST   false:GET
            WAY=RequestMethod.POST;
        }else{
            WAY=RequestMethod.GET;
        }
        Request<JSONObject> request=NoHttp.createJsonObjectRequest(student.studentGetLesson(),WAY);
        request.add("key",student.studentKey(getApplicationContext()));
        request.add("id",id);
        request.add("date",date);
        getRequestQueue().add(what, request, new OnResponseListener<JSONObject>() {
            @Override
            public void onStart(int what) {

            }

            @Override
            public void onSucceed(int what, Response<JSONObject> response) {
                switch (what){
                    case 1:
                        try {
                            JSONObject object=new JSONObject(response.get().toString());
                            mLog.i("response1","->"+object.toString());
                            String code=object.getString("code");
                            Msg=object.getString("msg");
                            if(code.equals("200")||code.equals("400")){
                                teacherDetail=gson.fromJson(object.getString("coachdata"),TeacherDetail.class);
                                JSONArray array=new JSONArray(object.getString("subdata"));
                                if(array.length()>0){
                                    lessondata.clear();
                                    for (int i = 0; i <array.length(); i++) {
                                        JSONObject obj=array.getJSONObject(i);
                                        TeacherLesson lesson=gson.fromJson(obj.toString(),TeacherLesson.class);
                                        lessondata.add(lesson);
                                    }
                                }
                                handler.sendEmptyMessage(1111);
                            }else {
                                handler.sendEmptyMessage(2222);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                    case 2:
                        try {
                            JSONObject object=new JSONObject(response.get().toString());
                            mLog.i("response2","->"+object.toString());
                            String code=object.getString("code");
                            Msg=object.getString("msg");
                            if(code.equals("200")||code.equals("400")){
                                String data=object.getString("subdata");
                                JSONArray array=new JSONArray(data);
                                if(array.length()>0){
                                    lessondata.clear();
                                    for (int i = 0; i <array.length(); i++) {
                                        JSONObject obj=array.getJSONObject(i);
                                        TeacherLesson lesson=gson.fromJson(obj.toString().trim(),TeacherLesson.class);
                                        lessondata.add(lesson);
                                    }
                                   handler.sendEmptyMessage(3333);
                                }else {
                                    lessondata.clear();
                                    handler.sendEmptyMessage(6666);
                                }
                            }else {
                                lessondata.clear();
                                handler.sendEmptyMessage(6666);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
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
            case R.id.teacher_choiceLesson_send:
                //上传选中课程
                choiceLesson.clear();
                for (int i = 0; i <lessondata.size(); i++) {
                    if(lessondata.get(i).isClick()){
                        mLog.e("发送选中了","->"+lessondata.get(i).getSubj_id());
                        ChoiceLesson mlesson=new ChoiceLesson();
                        mlesson.setInst_id(lessondata.get(i).getInst_id());
                        mlesson.setSubj_id(lessondata.get(i).getSubj_id());
                        mlesson.setCoach_id(Id);
                        mlesson.setCar_id(lessondata.get(i).getCar_id());
                        choiceLesson.add(mlesson);
                    }
                }
                if(choiceLesson.size()>0){
                    handler.sendEmptyMessage(4444);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            sendChoiceLesson(choiceLesson);
                        }
                    }).start();
                }else {
                    mToast.showText(getApplicationContext(),"没有选择对象");
                }

                break;
        }
    }

    private String getWeek(String week){
        String mweek="";
        switch (week){
            case "1":
                mweek="周一";
                break;
            case "2":
                mweek="周二";
                break;
            case "3":
                mweek="周三";
                break;
            case "4":
                mweek="周四";
                break;
            case "5":
                mweek="周五";
                break;
            case "6":
                mweek="周六";
                break;
            case "7":
                mweek="周日";
                break;
            default:
                break;
        }
        return mweek;
    }

}
