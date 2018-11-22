package com.kangzhan.student.HomeFragment.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.kangzhan.student.Debug.mLog;
import com.kangzhan.student.HomeFragment.Adapter.SchoolDetailClassAdapter;
import com.kangzhan.student.HomeFragment.Adapter.SchoolDetailQueAdapter;
import com.kangzhan.student.HomeFragment.Adapter.SchoolDetailRemarkAdapter;
import com.kangzhan.student.HomeFragment.Bean.EventMessage;
import com.kangzhan.student.HomeFragment.Bean.SchoolDetail;
import com.kangzhan.student.HomeFragment.Bean.SchoolDetail_Que;
import com.kangzhan.student.HomeFragment.Bean.SchoolDetail_Remark;
import com.kangzhan.student.HomeFragment.Bean.SchoolList;
import com.kangzhan.student.HomeFragment.HomeInterface.HomeInterface;
import com.kangzhan.student.HomeFragment.mDialog.ErrorDialog;
import com.kangzhan.student.HomeFragment.mDialog.IknowDialog;
import com.kangzhan.student.HomeFragment.mDialog.JoinDialog;
import com.kangzhan.student.HomeFragment.mDialog.ShareDialog;
import com.kangzhan.student.HomeFragment.mDialog.WaitDialog;
import com.kangzhan.student.R;
import com.kangzhan.student.ShowUI.mToast;
import com.kangzhan.student.ShowUI.showMessage;
import com.kangzhan.student.Student.mutils.DividerItemDecoration;
import com.kangzhan.student.com.BaseActivity;
import com.kangzhan.student.com.RegisterActivity;
import com.kangzhan.student.mUI.AutoVerticalScrollTextView;
import com.kangzhan.student.utils.ImageCompress;
import com.qiyukf.unicorn.api.ConsultSource;
import com.qiyukf.unicorn.api.Unicorn;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
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

public class HomeSchoolDetailActivity extends BaseActivity implements View.OnClickListener,AutoVerticalScrollTextView.TextItemOnClick,NestedScrollView.OnScrollChangeListener{
    private RelativeLayout jini,freeLearn,callPhone,consult,trainPlaceContainer,newActivitesContainer;
    private Button sendMsg;
    private ImageView school_iv;
    private EditText ques_edit;
    private TextView schoolName,schoolPhone,schoolQQ,indtorduce,more,activities,trainPlace,ReCount,QueCount,remarkMore,quesMore;
    private AutoVerticalScrollTextView autoTextView;
    private RecyclerView classList,remarkList,quesList;
    private LinearLayout zixun;



    private String Id,remarkCount,quesCount,Msg;
    private ArrayList<String> params=new ArrayList<>();
    private ArrayList<String> values=new ArrayList<>();
    private ArrayList<SchoolDetail_Remark> remarkData=new ArrayList<>();
    private ArrayList<SchoolDetail_Que> quesData=new ArrayList<>();
    private SchoolDetail detail;
    private Gson gson=new Gson();
    private boolean isRunning=true;
    private WaitDialog waitDialog;
    private ErrorDialog errorDialog;
    private int number=1;
    private String App_Id="wx69f0305db6ab47e4";
    private NestedScrollView scrollView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_school_detail);
        EventBus.getDefault().register(this);
        Toolbar toolbar= (Toolbar) findViewById(R.id.home_school_detail_back);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        Id=getIntent().getStringExtra("Id");
        initView();

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EventMessage msg){
        waitDialog.dismiss();
        if(msg.getMsg().equals("school_detail")){
            Glide.with(this).load(detail.getPic()).placeholder(R.drawable.kzplaceholder).error(R.drawable.kzplaceholder).into(school_iv);
            schoolName.setText(detail.getInsti_name());
            schoolPhone.setText(detail.getPhone());
            schoolQQ.setText(detail.getQq());
            indtorduce.setText(detail.getSummary());
            activities.setText(detail.getActivity().getTitle());
            trainPlace.setText("共"+detail.getRegion_count()+"个");

            SchoolDetailClassAdapter adapter1=new SchoolDetailClassAdapter(this,detail.getMclass());
            classList.setAdapter(adapter1);
            adapter1.notifyDataSetChanged();
            mLog.e("ListHeight","-->"+classList.computeVerticalScrollRange());
            classList.invalidate();
            classList.requestLayout();

            scrollView.requestLayout();
           // RecyclerView.LayoutParams lp=new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,classList.computeVerticalScrollRange());
           // classList.setLayoutParams(lp);
        }else if(msg.getMsg().equals("schoollunbo")){
            if(detail.getNotice()!=null&&detail.getNotice().size()>0){
                autoTextView.next();
                number++;
                autoTextView.setText(detail.getNotice().get(number%detail.getNotice().size()).getTitle());
            }
            autoTextView.setText("暂无公告");
        }else if(msg.getMsg().equals("school_detail_remark")){
            ReCount.setText("学员点评("+remarkCount+")");
            SchoolDetailRemarkAdapter adapter=new SchoolDetailRemarkAdapter(this,remarkData);
            remarkList.setAdapter(adapter);
            remarkList.requestLayout();
            //scrollView.invalidate();
        }else if(msg.getMsg().equals("school_detail_ques")){
            QueCount.setText("学员问答("+quesCount+")");
            SchoolDetailQueAdapter adapter=new SchoolDetailQueAdapter(this,quesData);
            quesList.setAdapter(adapter);
            quesList.requestLayout();
            //scrollView.invalidate();
        }else if(msg.getMsg().equals("send_msg")){
            IknowDialog dialog=new IknowDialog(this);
            dialog.show();
            dialog.setMsg(Msg);
        }else if(msg.getMsg().equals("send_msg_1")){
            mToast.showText(getApplicationContext(),Msg);
        }else if(msg.getMsg().equals("school_detail_error")){
            //接口发生错误
            errorDialog.show();
            errorDialog.setTextMsg("网络连接不上，请检测网络连接");
        }
    }


    private void initView() {
        //我要报名
        scrollView= (NestedScrollView) findViewById(R.id.home_school_detail_scrollview);
        scrollView.setOnScrollChangeListener(this);
        jini= (RelativeLayout) findViewById(R.id.home_school_linke4);
        jini.setOnClickListener(this);
        sendMsg= (Button) findViewById(R.id.home_school_detail_send_btn);
        sendMsg.setOnClickListener(this);
        school_iv= (ImageView) findViewById(R.id.home_school_detail_iv);
        schoolName= (TextView) findViewById(R.id.home_school_detail_school_name);
        schoolPhone= (TextView) findViewById(R.id.home_school_detail_phone);
        schoolPhone.setOnClickListener(this);
        schoolQQ= (TextView) findViewById(R.id.home_school_detail_qq);
        indtorduce= (TextView) findViewById(R.id.home_school_indua_tv);
        more= (TextView) findViewById(R.id.home_school_more_tv);
        more.setOnClickListener(this);
        autoTextView= (AutoVerticalScrollTextView) findViewById(R.id.home_school_auto_tv);
        autoTextView.TextViewItemOnClick(this);
        activities= (TextView) findViewById(R.id.home_school_detail_new_activities);
        trainPlaceContainer= (RelativeLayout) findViewById(R.id.home_school_detail_place_container);
        trainPlaceContainer.setOnClickListener(this);
        newActivitesContainer= (RelativeLayout) findViewById(R.id.home_school_detail_new_activities_container);
        newActivitesContainer.setOnClickListener(this);
        trainPlace= (TextView) findViewById(R.id.home_school_detail_train_place);
        classList= (RecyclerView) findViewById(R.id.home_school_detail_class_list);
        classList.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        remarkList= (RecyclerView) findViewById(R.id.home_school_detail_remark_list);
        remarkList.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        remarkList.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST));
        remarkMore= (TextView) findViewById(R.id.home_school_detail_remark_more);
        remarkMore.setOnClickListener(this);
        remarkMore.setTag("1");
        ReCount= (TextView) findViewById(R.id.home_school_detail_remark_count);
        quesList= (RecyclerView) findViewById(R.id.home_school_detail_question_list);
        quesList.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        quesList.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST));
        quesMore= (TextView) findViewById(R.id.home_school_detail_ques_more);
        quesMore.setOnClickListener(this);
        quesMore.setTag("1");
        QueCount= (TextView) findViewById(R.id.home_school_detail_ques_count);
        ques_edit= (EditText) findViewById(R.id.home_school_detail_send_edit);
        freeLearn= (RelativeLayout) findViewById(R.id.home_school_linke3);
        freeLearn.setOnClickListener(this);
        callPhone= (RelativeLayout) findViewById(R.id.home_school_linke1);
        callPhone.setOnClickListener(this);
        consult= (RelativeLayout) findViewById(R.id.home_school_linke2);
        consult.setOnClickListener(this);
        zixun= (LinearLayout) findViewById(R.id.home_school_help_container);

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (isRunning){
                    SystemClock.sleep(3000);
                    EventBus.getDefault().post(new EventMessage("schoollunbo"));
                }
            }
        }).start();
        errorDialog=new ErrorDialog(this);
        waitDialog=new WaitDialog(this);
        waitDialog.show();


        new Thread(new Runnable() {
            @Override
            public void run() {
                //请求驾校详情数据
                params.clear();
                values.clear();
                params.add("inst_id");
                values.add(Id);
                sendRequest(1,params,values, HomeInterface.schoolDetail(),"GET");
                //请求学员点评数据
                params.clear();
                values.clear();
                params.add("type");
                params.add("object_id");
                params.add("pagesize");
                values.add("2");            //type:1 为教练  2为驾校   object_id为相应的ID
                values.add("1");
                values.add("3");
                sendRequest(2,params,values,HomeInterface.schoolDetail_Remark(),"GET");
                //请求学员问答数据
                params.clear();
                values.clear();
                params.add("inst_id");
                params.add("pagesize");
                values.add(Id);
                values.add("3");
                sendRequest(3,params,values,HomeInterface.schoolDetail_Ques(),"GET");

            }
        }).start();
    }


    // 打电话：0731-85575773
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //最近活动
            case R.id.home_school_detail_new_activities_container:
                if(detail.getActivity().getTitle().equals("暂无活动")){
                    mToast.showText(getApplicationContext(),"暂无活动");
                }else {
                    Intent mdetail=new Intent(this,Home_Detail_URl.class);
                    mdetail.putExtra("url","http://www.kzxueche.com/wap/kangzhan_web/modules/sample/views/sch_huodong.html?id="+detail.getActivity().getId());
                    startActivity(mdetail);
                }
                break;
            //我要试学
            case R.id.home_school_linke3:
                JoinDialog dialog1=new JoinDialog(this);
                dialog1.show();
                dialog1.sendOnClick(new JoinDialog.SendOnClick() {
                    @Override
                    public void sendOnClick(final String[] info) {
                        final String[] minfo=info;
                        if(minfo[0].equals("")){
                            mToast.showText(getApplicationContext(),"姓名不能为空");
                        }else {
                            if(minfo[1].equals("")){
                                mToast.showText(getApplicationContext(),"电话不能为空");
                            }else {
                                if(minfo[2].equals("")){
                                    mToast.showText(getApplicationContext(),"车型不能为空");
                                }else {
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            params.clear();
                                            values.clear();
                                            params.add("type");
                                            params.add("real_name");
                                            params.add("mobile");
                                            params.add("inst_id");
                                            params.add("traintype");
                                            params.add("coach_id");
                                            values.add("1");
                                            values.add(minfo[0]);
                                            values.add(minfo[1]);
                                            values.add(Id);
                                            values.add(minfo[2]);
                                            values.add("");
                                            sendRequest(5,params,values,HomeInterface.schoolDetail_learn(),"POST");
                                        }
                                    }).start();
                                }
                            }
                        }
                    }
                });

                break;
            //我要报名
            case R.id.home_school_linke4:
                Intent student=new Intent(this, RegisterActivity.class);
                student.putExtra("who","student");
                startActivity(student);
                break;
            case R.id.home_school_more_tv:
                Intent mdetail=new Intent(this,Home_Detail_URl.class);
                mdetail.putExtra("url","http://www.kzxueche.com/wap/kangzhan_web/modules/sample/views/school_jieshao.html?id="+detail.getInst_id());
                startActivity(mdetail);
                break;
            case R.id.home_school_detail_send_btn:
                //提交问题按钮
                if(ques_edit.getText().toString().equals("")){
                    mToast.showText(getApplicationContext(),"问题不能为空哦");
                }else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            params.clear();
                            values.clear();
                            params.add("inst_id");
                            params.add("content");
                            values.add("88");
                            values.add(ques_edit.getText().toString().trim());
                            sendRequest(4,params,values,HomeInterface.schoolDetail_Answer(),"POST");
                        }
                    }).start();
                }

                break;
                //电话
            case R.id.home_school_linke1:
                callPhone("0731-85575773");
                break;
            case R.id.home_school_detail_phone:
                callPhone(detail.getPhone());
                break;
                //咨询
            case R.id.home_school_linke2:
                startChat();
                break;
                //查看训练场地
            case R.id.home_school_detail_place_container:
                Intent place=new Intent(this,SchoolTrainPlaceActivity.class);
                place.putExtra("Id",detail.getInst_id());
                place.putExtra("longit","0.0");
                place.putExtra("latit","0.0");
                startActivity(place);
                break;
                //全部评价
            case R.id.home_school_detail_remark_more:
                if(remarkMore.getTag().equals("0")){
                    mToast.showText(getApplicationContext(),"暂无数据");
                }else {
                    Intent more=new Intent(this,HomeRemarkMoreActivity.class);
                    more.putExtra("Id",Id);     //  Id 为对应的驾校ID
                    more.putExtra("type","2");      //type  1为教练  2为驾校
                    startActivity(more);
                }
                break;
                //全部问答
            case R.id.home_school_detail_ques_more:
                if(quesMore.getTag().equals("0")){
                    mToast.showText(getApplicationContext(),"暂无数据");
                }else {
                    Intent more=new Intent(this,HomeQuesMoreActivity.class);
                    more.putExtra("Id",Id);
                    startActivity(more);
                }
                break;
        }
    }

    private void startChat() {
        String title="咨询客服";
        ConsultSource source=new ConsultSource("驾校咨询",title,"咨询");
        Unicorn.openServiceActivity(this,title,source);
    }


    @Override
    public void textItemOnClick() {
        if(detail.getNotice()!=null&&detail.getNotice().size()>0){
            Intent item=new Intent(this,Home_Detail_URl.class);
            item.putExtra("url","http://www.kzxueche.com/wap/kangzhan_web/modules/sample/views/gonggao_dec.html?id="+detail.getNotice().get(number%detail.getNotice().size()).getId());
            startActivity(item);
        }else {
            mToast.showText(getApplicationContext(),"暂无活动");
        }

    }



    private void callPhone(String phoneNum) {
        //>=23
        if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.M){
            int checkCallPhone= ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);
            if(checkCallPhone!= PackageManager.PERMISSION_GRANTED){
                //没有授权,请求授权
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CALL_PHONE},1);
            }else {
                //授权
                mcallPhone(phoneNum);
            }
        }else {
            mcallPhone(phoneNum);
        }
    }


    private void mcallPhone(String phoneNum) {
        Intent call=new Intent();
        call.setAction(Intent.ACTION_CALL);
        call.setData(Uri.parse("tel:"+phoneNum));
        startActivity(call);
    }

    private void sendRequest(int what, ArrayList<String> params, ArrayList<String> values, String url, String way) {
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
                                detail=gson.fromJson(object.getString("data"),SchoolDetail.class);
                                EventBus.getDefault().post(new EventMessage("school_detail"));
                            }else {

                            }
                            break;
                        case 2:
                            if(object.getString("code").equals("200")){
                                remarkCount=object.getString("count");
                                JSONArray array=new JSONArray(object.getString("data"));
                                if(array.length()>0){
                                    remarkData.clear();
                                    for (int i = 0; i <array.length() ; i++) {
                                        SchoolDetail_Remark data=gson.fromJson(array.getString(i),SchoolDetail_Remark.class);
                                        remarkData.add(data);
                                    }
                                }
                                EventBus.getDefault().post(new EventMessage("school_detail_remark"));
                            }else {
                                remarkMore.setTag("0");
                            }
                            break;
                        case 3:
                            if(object.getString("code").equals("200")){
                                quesCount=object.getString("count");
                                JSONArray array=new JSONArray(object.getString("data"));
                                if(array.length()>0){
                                    quesData.clear();
                                    for (int i = 0; i <array.length() ; i++) {
                                        SchoolDetail_Que data=gson.fromJson(array.getString(i),SchoolDetail_Que.class);
                                        quesData.add(data);
                                    }
                                }
                                EventBus.getDefault().post(new EventMessage("school_detail_ques"));
                            }else {
                                quesMore.setTag("0");
                            }
                            break;
                        case 4:
                            if(object.getString("code").equals("200")){
                                Msg=object.getString("msg");
                            }else {
                                Msg="提交失败，请重试";
                            }
                            EventBus.getDefault().post(new EventMessage("send_msg"));
                            break;
                        case 5:
                            Msg=object.getString("msg");
                            EventBus.getDefault().post(new EventMessage("send_msg_1"));
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(int what, Response<JSONObject> response) {
                EventBus.getDefault().post(new EventMessage("school_detail_error"));
            }

            @Override
            public void onFinish(int what) {

            }
        });

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_school_detail_share,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.home_school_detail_item_share){
            ShareDialog share=new ShareDialog(HomeSchoolDetailActivity.this);
            share.show();
            share.getShareWay(new ShareDialog.ShareWay() {
                @Override
                public void shareWay(boolean way) {
                    share(way);
                }
            });
        }
        return super.onOptionsItemSelected(item);
    }


    private void share(boolean way){
        IWXAPI api= WXAPIFactory.createWXAPI(this,App_Id,true);
        if(!api.isWXAppInstalled()){
            mToast.showText(getApplicationContext(),"您好没有安装微信");
            return;
        }
        WXWebpageObject webpage=new WXWebpageObject();
        webpage.webpageUrl="http://www.kzxueche.com/wap/kangzhan_web/modules/sample/views/index_schoolDec.html?inst_id="+Id;

        WXMediaMessage wxmsg=new WXMediaMessage(webpage);
        wxmsg.title="康展学车";
        wxmsg.description="康展学车是面向学员、教练、驾校、培训主管部门的一站式云上解决方案。";
        Bitmap thumb= BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher);
        wxmsg.thumbData= ImageCompress.bitmapToByteArray(thumb);

        SendMessageToWX.Req req=new SendMessageToWX.Req();
        req.transaction="cb187abb466501296db2b047b8c2aa41";    //签名：
        req.message=wxmsg;
        req.scene=way?SendMessageToWX.Req.WXSceneSession:SendMessageToWX.Req.WXSceneTimeline;
        api.sendReq(req);
    }

    @Override
    protected void onDestroy() {
        isRunning=false;
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }


    @Override
    public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        if(scrollY-oldScrollY>20){
           zixun.setVisibility(View.GONE);
        }
        if(oldScrollY-scrollY>20){
            zixun.setVisibility(View.VISIBLE);
        }
    }
}
