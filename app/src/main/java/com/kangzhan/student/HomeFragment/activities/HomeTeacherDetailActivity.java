package com.kangzhan.student.HomeFragment.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.kangzhan.student.Debug.mLog;
import com.kangzhan.student.HomeFragment.Adapter.SchoolDetailClassAdapter;
import com.kangzhan.student.HomeFragment.Adapter.SchoolDetailRemarkAdapter;
import com.kangzhan.student.HomeFragment.Bean.EventMessage;
import com.kangzhan.student.HomeFragment.Bean.SchoolDetail_Remark;
import com.kangzhan.student.HomeFragment.Bean.SchoolList;
import com.kangzhan.student.HomeFragment.Bean.TeacherDetail;
import com.kangzhan.student.HomeFragment.HomeInterface.HomeInterface;
import com.kangzhan.student.HomeFragment.mDialog.JoinDialog;
import com.kangzhan.student.HomeFragment.mDialog.ShareDialog;
import com.kangzhan.student.HomeFragment.mDialog.WaitDialog;
import com.kangzhan.student.R;
import com.kangzhan.student.ShowUI.mToast;
import com.kangzhan.student.Student.mutils.DividerItemDecoration;
import com.kangzhan.student.com.BaseActivity;
import com.kangzhan.student.com.RegisterActivity;
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

public class HomeTeacherDetailActivity extends BaseActivity implements View.OnClickListener,NestedScrollView.OnScrollChangeListener{
    private ImageView teacher_iv;
    private TextView name,phone,qqNum,teacherIntroduce,belongSchool,trainPlace,ReCount,remarkMore;
    private RecyclerView classList,remarkList;
    private RelativeLayout callPhone,consult,free_learn,join,trainPlaceContainer ;


    private String Id,remarkCount,Msg;
    private ArrayList<String> params=new ArrayList<>();
    private ArrayList<String> values=new ArrayList<>();
    private ArrayList<SchoolDetail_Remark> remarkData=new ArrayList<>();
    private TeacherDetail detail;
    private Gson gson=new Gson();
    private String App_Id="wx69f0305db6ab47e4";
    private NestedScrollView scrollView;
    private LinearLayout zixun;
    private WaitDialog waitDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_teacher_detail);
        EventBus.getDefault().register(this);
        Toolbar toolbar= (Toolbar) findViewById(R.id.home_teacher_detail_back);
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
        if(msg.getMsg().equals("teacher_detail")){
            name.setText(detail.getName());
            phone.setText(detail.getMobile());
            qqNum.setText(detail.getQqnum());
            teacherIntroduce.setText(detail.getSelf_description());
            belongSchool.setText(detail.getInst_name());
            trainPlace.setText("共"+detail.getRegion_count()+"个");
            Glide.with(this).load(detail.getOss_photo()).placeholder(R.drawable.kzplaceholder).error(R.drawable.kzplaceholder).into(teacher_iv);
            SchoolDetailClassAdapter adapter=new SchoolDetailClassAdapter(this,detail.getClasses());
            classList.setAdapter(adapter);
            classList.requestLayout();

        }else if(msg.getMsg().equals("teacher_detail_remark")){
            ReCount.setText("学员点评("+remarkCount+")");
            SchoolDetailRemarkAdapter adapter=new SchoolDetailRemarkAdapter(this,remarkData);
            remarkList.setAdapter(adapter);
        }else if(msg.getMsg().equals("send_msg")){
            mToast.showText(getApplicationContext(),Msg);
        }
    }

    private void initView() {
        scrollView= (NestedScrollView) findViewById(R.id.home_teacher_detail_scroll);
        scrollView.setOnScrollChangeListener(this);
        teacher_iv= (ImageView) findViewById(R.id.home_teacher_detail_iv);
        name= (TextView) findViewById(R.id.home_teacher_detail_name);
        phone= (TextView) findViewById(R.id.home_teacher_detail_phone);
        phone.setOnClickListener(this);
        qqNum= (TextView) findViewById(R.id.home_teacher_detail_qq);
        teacherIntroduce= (TextView) findViewById(R.id.home_teacher_detail_indua_tv);
        belongSchool= (TextView) findViewById(R.id.home_teacher_detail_belong);
        trainPlace= (TextView) findViewById(R.id.home_teacher_detail_place);
        trainPlaceContainer= (RelativeLayout) findViewById(R.id.home_teacher_detail_place_container);
        trainPlaceContainer.setOnClickListener(this);
        classList= (RecyclerView) findViewById(R.id.home_teacher_detail_class_list);
        classList.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        remarkList= (RecyclerView) findViewById(R.id.home_teacher_detail_remark_list);
        remarkList.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        ReCount= (TextView) findViewById(R.id.home_teacher_detail_remark_count);
        remarkMore= (TextView) findViewById(R.id.home_teacher_detail_remark_more);
        remarkMore.setOnClickListener(this);
        remarkMore.setTag("1");
        callPhone= (RelativeLayout) findViewById(R.id.home_teacher_linke1);
        consult= (RelativeLayout) findViewById(R.id.home_teacher_linke2);
        free_learn= (RelativeLayout) findViewById(R.id.home_teacher_linke3);
        join= (RelativeLayout) findViewById(R.id.home_teacher_linke4);
        callPhone.setOnClickListener(this);
        consult.setOnClickListener(this);
        free_learn.setOnClickListener(this);
        join.setOnClickListener(this);
        zixun= (LinearLayout) findViewById(R.id.home_teacher_zixun_container);
        waitDialog=new WaitDialog(this);

        waitDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                //获取教练详情数据
                params.clear();
                values.clear();
                params.add("coach_id");
                values.add(Id);
                sendRequest(1,params,values, HomeInterface.TeacherDetail(),"GET");
                //获取教练点评数据
                params.clear();
                values.clear();
                params.add("type");
                params.add("object_id");
                params.add("pagesize");
                values.add("1");            //type:1 为教练  2为驾校   object_id为相应的ID
                values.add(Id);
                values.add("3");
                sendRequest(2,params,values,HomeInterface.schoolDetail_Remark(),"GET");

                
            }
        }).start();

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //给教练打电话
            case R.id.home_teacher_detail_phone:
                callPhone(detail.getMobile());
                break;
            //打电话
            case R.id.home_teacher_linke1:
                callPhone("0731-85575773");
                break;
            //咨询
            case R.id.home_teacher_linke2:
                startChat();
                break;
            //我要试学
            case R.id.home_teacher_linke3:
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
                                            values.add(Id);        //88
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
            case R.id.home_teacher_linke4:
                Intent student=new Intent(this, RegisterActivity.class);
                student.putExtra("who","student");
                startActivity(student);
                break;
                //查看场地
            case R.id.home_teacher_detail_place_container:
                Intent place=new Intent(this,SchoolTrainPlaceActivity.class);
                place.putExtra("Id",detail.getInst_id());
                place.putExtra("longit","0.0");
                place.putExtra("latit","0.0");
                startActivity(place);
                break;
                //查看全部点评
            case R.id.home_teacher_detail_remark_more:
                if(remarkMore.getTag().equals("0")){
                    mToast.showText(getApplicationContext(),"暂无数据");
                }else {
                    Intent more=new Intent(this,HomeRemarkMoreActivity.class);
                    more.putExtra("Id",Id);     //  Id 为对应的驾校ID
                    more.putExtra("type","1");      //type  1为教练  2为驾校
                    startActivity(more);
                }
                break;
        }
    }

    private void startChat() {
        String title="咨询客服";
        ConsultSource source=new ConsultSource("教练咨询",title,"咨询");
        Unicorn.openServiceActivity(this,title,source);
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
                                detail=gson.fromJson(object.getString("data"),TeacherDetail.class);
                                EventBus.getDefault().post(new EventMessage("teacher_detail"));
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
                                EventBus.getDefault().post(new EventMessage("teacher_detail_remark"));
                            }else {
                                remarkMore.setTag("0");
                            }
                            break;
                        case 5:
                            Msg=object.getString("msg");
                            EventBus.getDefault().post(new EventMessage("send_msg"));
                            break;
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_school_detail_share,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.home_school_detail_item_share){
            ShareDialog share=new ShareDialog(HomeTeacherDetailActivity.this);
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
        webpage.webpageUrl="http://www.kzxueche.com/wap/kangzhan_web/modules/sample/views/index_trainerDec.html?coach_id="+Id;

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
