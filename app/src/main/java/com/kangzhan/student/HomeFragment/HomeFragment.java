package com.kangzhan.student.HomeFragment;


import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.bumptech.glide.util.Util;
import com.google.gson.Gson;
import com.kangzhan.student.Advisetment.RecommendNewsActivity;
import com.kangzhan.student.Debug.mLog;
import com.kangzhan.student.HomeFragment.Adapter.GuessSchoolAdapter;
import com.kangzhan.student.HomeFragment.Adapter.GuessTeacherAdapter;
import com.kangzhan.student.HomeFragment.Adapter.HomeTeacherFragmentAdapter;
import com.kangzhan.student.HomeFragment.Bean.Banner;
import com.kangzhan.student.HomeFragment.Bean.EventMessage;
import com.kangzhan.student.HomeFragment.Bean.NewPublish;
import com.kangzhan.student.HomeFragment.Bean.SchoolList;
import com.kangzhan.student.HomeFragment.Bean.TeacherList;
import com.kangzhan.student.HomeFragment.HomeInterface.HomeInterface;
import com.kangzhan.student.HomeFragment.activities.Home_Detail_URl;
import com.kangzhan.student.HomeFragment.activities.SelectCityActivity;
import com.kangzhan.student.HomeFragment.mDialog.ErrorDialog;
import com.kangzhan.student.HomeFragment.mDialog.ShareDialog;
import com.kangzhan.student.HomeFragment.mDialog.WaitDialog;
import com.kangzhan.student.R;
import com.kangzhan.student.ShowUI.mToast;
import com.kangzhan.student.ShowUI.showMessage;
import com.kangzhan.student.Student.mutils.DividerItemDecoration;
import com.kangzhan.student.mInterface.AdvisetInterface.Adviset;
import com.kangzhan.student.mUI.AutoVerticalScrollTextView;
import com.kangzhan.student.mUI.mBanner;
import com.kangzhan.student.utils.ImageCompress;
import com.tencent.mm.opensdk.modelbase.BaseResp;
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
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by kangzhan011 on 2017/11/16.
 */

public class HomeFragment extends HomeBaseFragment implements View.OnClickListener,mBanner.OnItemClickListener,AMapLocationListener,AutoVerticalScrollTextView.TextItemOnClick {
    private TextView address,guessShool_tv,guessTeacher_tv;
    private ImageView share;
    private mBanner banner;
    private AutoVerticalScrollTextView mautoText;
    private List<String> imageUrl=new ArrayList<>();
    private RelativeLayout findschool,findteacher,newActivities,kzNews,guide,learnPro,guess_school,guess_teacher;
    private WaitDialog waitDialog;
    private ErrorDialog errorDialog;
    private RecyclerView recyclerView;
    private GuessSchoolAdapter schooladapter;
    private GuessTeacherAdapter teacheradapter;
    private String App_Id="wx69f0305db6ab47e4";



    private boolean isRunning=true;
    private int number=1;
    private ArrayList<String> params=new ArrayList<>();
    private ArrayList<String> values=new ArrayList<>();
    private ArrayList<SchoolList> schoolData=new ArrayList<>();
    private ArrayList<TeacherList> teacherData=new ArrayList<>();
    private ArrayList<NewPublish> newsData=new ArrayList<>();
    private ArrayList<Banner> bannerDatas=new ArrayList<>();
    private Gson gson;

    //定位
    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient ;
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption ;


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EventMessage event) {
        switch (event.getMsg()){
            case "updata_banner":
                //最后一个网络请求完成，取消加载提示
                waitDialog.dismiss();
                imageUrl.clear();
                banner.clearBannerData();
                //更新轮播图片
                for (int i = 0; i <bannerDatas.size(); i++) {
                    imageUrl.add(bannerDatas.get(i).getPic());
                }
                banner.setBannerURLData(imageUrl);
                banner.startSmoothAuto();
                break;
            case "0000":
                waitDialog.dismiss();
            break;
            case "1111":
                if(newsData.size()>0){
                    mautoText.next();
                    number++;
                    mautoText.setText(newsData.get(number%newsData.size()).getInfo_title());
                }
                break;
            case "2222":
                schooladapter.notifyDataSetChanged();
                recyclerView.setAdapter(schooladapter);
                recyclerView.requestLayout();
                break;
            case "3333":
                teacheradapter.notifyDataSetChanged();
                //recyclerView.setAdapter(teacheradapter);
                break;
            case "newpublish":

                break;
                //定位成功
            case "has_located":
                address.setText(HomeInterface.getHomeLocation(getContext().getApplicationContext())[0]);
                getData(false);
                break;
                //定位失败
            case "location_error":
                address.setText(HomeInterface.getCurrentCity(getContext().getApplicationContext()));
                getData(true);
                break;
                //设置城市
            case "set_city":
                address.setText(HomeInterface.getCurrentCity(getContext().getApplicationContext()));
                //刷新数据
                getData(true);
                break;
            case "home_error":
                //接口出错提示
                waitDialog.dismiss();
                errorDialog.show();
                break;
        }
    }


    @Override
    protected int setContentView() {
        return R.layout.homefragment_layout;
    }


    @Override
    protected void initView(View v) {
        EventBus.getDefault().register(this);
        address= (TextView) v.findViewById(R.id.current_location_tv);
        share= (ImageView) v. findViewById(R.id.share_iv);
        share.setOnClickListener(this);
        banner= (mBanner) v.findViewById(R.id.main_login_banner);
        banner.setOnItemClickListener(this);
        mautoText= (AutoVerticalScrollTextView) v.findViewById(R.id.home_main_atuoText);
        mautoText.TextViewItemOnClick(this);
        findschool= (RelativeLayout) v.findViewById(R.id.homef_findschool_container);
        findteacher= (RelativeLayout) v.findViewById(R.id.homef_findteacher_container);
        newActivities= (RelativeLayout) v.findViewById(R.id.homef_new_activities_container);
        kzNews= (RelativeLayout) v.findViewById(R.id.homef_kznew_container);
        guide= (RelativeLayout) v.findViewById(R.id.homef_guide_container);
        learnPro= (RelativeLayout) v.findViewById(R.id.homef_learn_pro_container);
        newActivities.setOnClickListener(this);
        kzNews.setOnClickListener(this);
        findschool.setOnClickListener(this);
        findteacher.setOnClickListener(this);
        //
        guess_school= (RelativeLayout) v.findViewById(R.id.home_guess_sele_school_container);
        guess_teacher= (RelativeLayout) v.findViewById(R.id.home_guess_sele_teacher_container);
        guess_school.setOnClickListener(this);
        guess_teacher.setOnClickListener(this);
        //
        guessShool_tv= (TextView) v.findViewById(R.id.home_guess_sele_school_tv);
        guessTeacher_tv= (TextView) v.findViewById(R.id.home_guess_sele_teacher_tv);
        address.setOnClickListener(this);

        recyclerView= (RecyclerView) v.findViewById(R.id.home_guess_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));

        errorDialog=new ErrorDialog(getContext());
        waitDialog=new WaitDialog(getContext());

        mcheckPermission();

    }

    private void mcheckPermission() {
        //>=23
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            int checkPermission_location= ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION);
            if(checkPermission_location!= PackageManager.PERMISSION_GRANTED){
                //没有授权,请求授权,
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},1);
            }else {
               getLocation();
            }
        }else {
            getLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    mToast.showText(getContext().getApplicationContext(),"定位授权");
                    getLocation();
                }else {
                    errorDialog.show();
                    errorDialog.setTextMsg("请到应用管理开启定位权限");
                    getLocation();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                break;
        }
    }


    //定位
    private void getLocation(){
        mLocationClient=new AMapLocationClient(getActivity().getApplication());
        mLocationClient.setLocationListener(this);
        mLocationOption=new AMapLocationClientOption();
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        mLocationOption.setOnceLocation(false);
        mLocationOption.setOnceLocationLatest(true);
        mLocationOption.setMockEnable(true);        //容许开启模拟器定位
        mLocationOption.setHttpTimeOut(5000);
        mLocationClient.setLocationOption(mLocationOption);
        mLocationClient.startLocation();
        waitDialog.show();
        waitDialog.setWatiContent("定位中");
    }

    @Override
    protected void initData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (isRunning){
                    SystemClock.sleep(3000);
                    EventBus.getDefault().post(new EventMessage("1111"));
                }
            }
        }).start();
        schooladapter=new GuessSchoolAdapter(getContext(),schoolData);
        teacheradapter=new GuessTeacherAdapter(getContext(),teacherData);
        clearStyle();
        guess_school.setBackgroundResource(R.drawable.home_guess_like_sele_0);
        guessShool_tv.setTextColor(getResources().getColor(R.color.textColor_whit));
        gson=new Gson();
    }

    @Override
    protected void loadData() {
    }

    private void getData(final boolean refreshData){
        //显示定位城市
        waitDialog.show();
        mLog.e("定位","-->城市定位"+refreshData);
        new Thread(new Runnable() {
            @Override
            public void run() {
                //请求轮播图片
                params.clear();
                values.clear();
                params.add("city_name");
                params.add("type");
                values.add(refreshData?HomeInterface.getCurrentCity(getContext()):HomeInterface.getHomeLocation(getContext())[0]);
                values.add("1");
                sendRequest(4,params,values,HomeInterface.Adviset(),"GET");
                //请求驾校数据
                params.clear();
                values.clear();
                params.add("city");
                params.add("latitude");
                params.add("longitude");
                params.add("order");
                params.add("pagesize");
                values.add(refreshData?HomeInterface.getCurrentCity(getContext()):HomeInterface.getHomeLocation(getContext())[0]);
                values.add(HomeInterface.getHomeLocation(getContext())[2]);
                values.add(HomeInterface.getHomeLocation(getContext())[1]);
                values.add("1");
                values.add("10");
                sendRequest(1,params,values,HomeInterface.schoolList(),"GET");

                //请求教练数据
                params.clear();
                values.clear();
                params.add("city");
                params.add("latitude");
                params.add("longitude");
                params.add("order");
                params.add("pagesize");
                values.add(refreshData?HomeInterface.getCurrentCity(getContext()):HomeInterface.getHomeLocation(getContext())[0]);
                values.add(HomeInterface.getHomeLocation(getContext())[2]);
                values.add(HomeInterface.getHomeLocation(getContext())[1]);
                values.add("1");
                values.add("10");
                sendRequest(2,params,values, HomeInterface.teacherList(),"GET");

                //请求轮播广告数据
                params.clear();
                values.clear();
                params.add("id");
                values.add("32");
                sendRequest(3,params,values, Adviset.SchoolRecommend(),"GET");
            }
        }).start();
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
                                schoolData.clear();
                                JSONArray array=new JSONArray(object.getString("data"));
                                for (int i = 0; i <array.length() ; i++) {
                                    SchoolList list=gson.fromJson(array.getJSONObject(i).toString(),SchoolList.class);
                                    schoolData.add(list);
                                }
                                EventBus.getDefault().post(new EventMessage("2222"));
                            }else {

                            }
                            break;
                        case 2:
                            if(object.getString("code").equals("200")){
                                teacherData.clear();
                                JSONArray array=new JSONArray(object.getString("data"));
                                for (int i = 0; i <array.length() ; i++) {
                                    TeacherList list=gson.fromJson(array.getJSONObject(i).toString(),TeacherList.class);
                                    teacherData.add(list);
                                }
                                EventBus.getDefault().post(new EventMessage("3333"));
                            }else {

                            }
                            break;
                        case 3:
                            if(object.getString("code").equals("200")){
                                newsData.clear();
                                JSONArray array=new JSONArray(object.getString("data"));
                                for (int i = 0; i <array.length() ; i++) {
                                    NewPublish newPublish=gson.fromJson(array.getJSONObject(i).toString(),NewPublish.class);
                                    newsData.add(newPublish);
                                }
                                EventBus.getDefault().post(new EventMessage("newpublish"));
                            }else {

                            }
                            break;
                        case 4:
                            if(object.getString("code").equals("200")){
                                JSONArray array=new JSONArray(object.getString("data"));
                                if(array.length()>0){
                                    bannerDatas.clear();
                                    for (int i = 0; i <array.length() ; i++) {
                                        Banner banner=gson.fromJson(array.getJSONObject(i).toString(),Banner.class);
                                        bannerDatas.add(banner);
                                    }
                                    EventBus.getDefault().post(new EventMessage("updata_banner"));
                                }
                            }else {

                            }
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(int what, Response<JSONObject> response) {
                EventBus.getDefault().post(new EventMessage("home_error"));
            }

            @Override
            public void onFinish(int what) {

            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.home_guess_sele_school_container:
                clearStyle();
                guess_school.setBackgroundResource(R.drawable.home_guess_like_sele_0);
                guessShool_tv.setTextColor(getResources().getColor(R.color.textColor_whit));
                recyclerView.setAdapter(schooladapter);
                break;
            case R.id.home_guess_sele_teacher_container:
                clearStyle();
                guess_teacher.setBackgroundResource(R.drawable.home_guess_like_sele_0);
                guessTeacher_tv.setTextColor(getResources().getColor(R.color.textColor_whit));
                recyclerView.setAdapter(teacheradapter);
                break;
            case R.id.current_location_tv:
                Intent loaction=new Intent(getContext(), SelectCityActivity.class);
                getActivity().startActivity(loaction);
                break;
            case R.id.homef_findschool_container:
                EventBus.getDefault().post(new EventMessage("fragmentS"));
                break;
            case R.id.homef_findteacher_container:
                EventBus.getDefault().post(new EventMessage("fragmentT"));
                break;
            case R.id.homef_kznew_container:
                //康展资讯
                Intent mnews=new Intent(getActivity(),RecommendNewsActivity.class);
                mnews.putExtra("item","");
                startActivity(mnews);
                break;
            case R.id.share_iv:
                //微信fenx
                ShareDialog share=new ShareDialog(getActivity());
                share.show();
                share.getShareWay(new ShareDialog.ShareWay() {
                    @Override
                    public void shareWay(boolean way) {
                        share(way);
                    }
                });
                break;
            case R.id.homef_new_activities_container:
                //最新活动
                goDetail("http://www.kzxueche.com/wap/kangzhan_web/modules/sample/views/huodong.html?id=5");
                break;
        }
    }

    //轮播图片的点击事件
    @Override
    public void onItemClick(int position) {
        for (int i = 0; i < bannerDatas.size(); i++) {
            if(i==position){
                goDetail("http://www.kzxueche.com/wap/kangzhan_web/modules/sample/views/huodong.html?"+bannerDatas.get(i).getId());
            }
        }
    }

    //轮播广告的点击事件
    @Override
    public void textItemOnClick() {
        Intent lunbo=new Intent(getActivity(),RecommendNewsActivity.class);
        lunbo.putExtra("item","2");
        startActivity(lunbo);
    }

    //定位监听回调,获得位置之后请求网络数据
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        waitDialog.dismiss();
        if(aMapLocation!=null){
            if(aMapLocation.getErrorCode()==0){
                double mlong=aMapLocation.getLongitude();
                double mlat=aMapLocation.getLatitude();
                String city=aMapLocation.getCity();
                saveHomeLocation(mlat,mlong,city);
                EventBus.getDefault().post(new EventMessage("has_located"));
                mLog.e("location","--->mlong:"+mlong+"-->mlat:"+mlat+"-->city:"+city);
            }else {
                //定位失败
                mToast.showText(getContext().getApplicationContext(),"定位失败");
                EventBus.getDefault().post(new EventMessage("location_error"));
                mLog.e("AmapError","location Error, ErrCode:"
                        + aMapLocation.getErrorCode() + ", errInfo:"
                        + aMapLocation.getErrorInfo());
            }
        }
    }

    private void saveHomeLocation(double mlat,double mlong,String city) {
        SharedPreferences sp=getContext().getSharedPreferences("HomeLocation",MODE_PRIVATE);
        SharedPreferences.Editor edtor=sp.edit();
        edtor.putString("lat",String.valueOf(mlat));
        edtor.putString("long",String.valueOf(mlong));
        edtor.putString("city",city);
        edtor.apply();
    }

    private void goDetail(String url){
        Intent activitie=new Intent(getContext(), Home_Detail_URl.class);
        activitie.putExtra("url",url);
        startActivity(activitie);
    }


    private void share(boolean way){
        IWXAPI api= WXAPIFactory.createWXAPI(getActivity(),App_Id,true);
        if(!api.isWXAppInstalled()){
            mToast.showText(getActivity().getApplicationContext(),"您好没有安装微信");
            return;
        }
        WXWebpageObject webpage=new WXWebpageObject();
        webpage.webpageUrl="http://www.kzxueche.com/wap/kangzhan_web/modules/sample/views/app.html";

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


    private void clearStyle(){
        guess_school.setBackgroundResource(R.drawable.home_guess_like_below);
        guessShool_tv.setTextColor(getResources().getColor(R.color.textColor_black));
        guess_teacher.setBackgroundResource(R.drawable.home_guess_like_below);
        guessTeacher_tv.setTextColor(getResources().getColor(R.color.textColor_black));
    }

    @Override
    public void onResume() {
        banner.onResume();
        super.onResume();
    }

    @Override
    public void onPause() {
        banner.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        isRunning=false;
        if(mLocationClient!=null){
            mLocationClient.onDestroy();
        }
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

}
