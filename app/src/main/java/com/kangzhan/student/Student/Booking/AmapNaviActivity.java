package com.kangzhan.student.Student.Booking;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.AMapNaviListener;
import com.amap.api.navi.AMapNaviView;
import com.amap.api.navi.AMapNaviViewListener;
import com.amap.api.navi.AMapNaviViewOptions;
import com.amap.api.navi.enums.NaviType;
import com.amap.api.navi.model.AMapLaneInfo;
import com.amap.api.navi.model.AMapNaviCameraInfo;
import com.amap.api.navi.model.AMapNaviCross;
import com.amap.api.navi.model.AMapNaviInfo;
import com.amap.api.navi.model.AMapNaviLocation;
import com.amap.api.navi.model.AMapNaviPath;
import com.amap.api.navi.model.AMapNaviTrafficFacilityInfo;
import com.amap.api.navi.model.AMapServiceAreaInfo;
import com.amap.api.navi.model.AimLessModeCongestionInfo;
import com.amap.api.navi.model.AimLessModeStat;
import com.amap.api.navi.model.NaviInfo;
import com.autonavi.tbt.TrafficFacilityInfo;
import com.kangzhan.student.R;
import com.kangzhan.student.com.BaseActivity;

public class AmapNaviActivity extends BaseActivity implements AMapNaviViewListener,AMapNaviListener{
    private AMapNaviView naviView;
    private AMapNavi navi;
    private String[] aim=new String[2];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amap_navi);
        Toolbar toolbar= (Toolbar) findViewById(R.id.student_navi_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        Intent getData=getIntent();
        aim=getData.getStringArrayExtra("Aim");//获取经纬度
        navi=AMapNavi.getInstance(this);
        navi.addAMapNaviListener(this);
        navi.setEmulatorNaviSpeed(60);
        naviView= (AMapNaviView) findViewById(R.id.navi_view);
        naviView.setAMapNaviViewListener(this);
        setAmapNaviViewOption();
    }

    private void setAmapNaviViewOption() {
        if(naviView==null){
            return;
        }
        AMapNaviViewOptions viewOptions=new AMapNaviViewOptions();
        viewOptions.setSettingMenuEnabled(false);//设置菜单按钮是否在导航界面显示
        viewOptions.setNaviNight(false);//设置导航界面是否显示黑夜模式
        viewOptions.setReCalculateRouteForYaw(true);//设置偏航时是否重新计算路径
        viewOptions.setReCalculateRouteForTrafficJam(true);//前方拥堵时是否重新计算路径
        viewOptions.setTrafficInfoUpdateEnabled(true);//设置交通播报是否打开
        viewOptions.setCameraInfoUpdateEnabled(true);//设置摄像头播报是否打开
        viewOptions.setScreenAlwaysBright(true);//设置导航状态下屏幕是否一直开启。
        viewOptions.setCrossDisplayShow(false); //设置导航时 路口放大功能是否显示
        viewOptions.setTrafficBarEnabled(false);  //设置 返回路况光柱条是否显示（只适用于驾车导航，需要联网）
        // viewOptions.setLayoutVisible(false);  //设置导航界面UI是否显示
        //viewOptions.setNaviViewTopic(mThemeStle);//设置导航界面的主题
        //viewOptions.setZoom(16);
        viewOptions.setTilt(0);  //2D显示
        naviView.setViewOptions(viewOptions);
    }


    @Override
    public void onNaviSetting() {

    }

    @Override
    public void onNaviCancel() {

    }

    @Override
    public boolean onNaviBackClick() {
        return false;
    }

    @Override
    public void onNaviMapMode(int i) {

    }

    @Override
    public void onNaviTurnClick() {

    }

    @Override
    public void onNextRoadClick() {

    }

    @Override
    public void onScanViewButtonClick() {

    }

    @Override
    public void onLockMap(boolean b) {

    }

    @Override
    public void onNaviViewLoaded() {

    }


    @Override
    protected void onResume() {
        super.onResume();
        naviView.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
        naviView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        naviView.onDestroy();
        navi.stopNavi();
        navi.destroy();
    }
    //
    //导航失败回调函数
    @Override
    public void onInitNaviFailure() {

    }
    //导航成功回调函数
    @Override
    public void onInitNaviSuccess() {
        int strategy=0;
        try{
            strategy=navi.strategyConvert(true,false,false,false,false);
        }catch (Exception e){
            e.printStackTrace();
        }
        //navi.calculateDriveRoute()
    }
    //
    @Override
    public void onStartNavi(int i) {

    }

    @Override
    public void onTrafficStatusUpdate() {

    }

    @Override
    public void onLocationChange(AMapNaviLocation aMapNaviLocation) {

    }

    @Override
    public void onGetNavigationText(int i, String s) {

    }

    @Override
    public void onGetNavigationText(String s) {

    }

    @Override
    public void onEndEmulatorNavi() {

    }

    @Override
    public void onArriveDestination() {

    }

    @Override
    public void onCalculateRouteFailure(int i) {

    }

    @Override
    public void onReCalculateRouteForYaw() {

    }

    @Override
    public void onReCalculateRouteForTrafficJam() {

    }

    @Override
    public void onArrivedWayPoint(int i) {

    }

    @Override
    public void onGpsOpenStatus(boolean b) {

    }

    @Override
    public void onNaviInfoUpdate(NaviInfo naviInfo) {

    }

    @Override
    public void onNaviInfoUpdated(AMapNaviInfo aMapNaviInfo) {

    }

    @Override
    public void updateCameraInfo(AMapNaviCameraInfo[] aMapNaviCameraInfos) {

    }

    @Override
    public void onServiceAreaUpdate(AMapServiceAreaInfo[] aMapServiceAreaInfos) {

    }

    @Override
    public void showCross(AMapNaviCross aMapNaviCross) {

    }

    @Override
    public void hideCross() {

    }

    @Override
    public void showLaneInfo(AMapLaneInfo[] aMapLaneInfos, byte[] bytes, byte[] bytes1) {

    }

    @Override
    public void hideLaneInfo() {

    }
    //步行或者驾车路径规划成功后的回调函数
    @Override
    public void onCalculateRouteSuccess(int[] ints) {
        AMapNaviPath path=navi.getNaviPath();
        if(path==null){
            return;
        }
        navi.startNavi(NaviType.GPS);
    }

    @Override
    public void notifyParallelRoad(int i) {

    }

    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo aMapNaviTrafficFacilityInfo) {

    }

    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo[] aMapNaviTrafficFacilityInfos) {

    }

    @Override
    public void OnUpdateTrafficFacility(TrafficFacilityInfo trafficFacilityInfo) {

    }

    @Override
    public void updateAimlessModeStatistics(AimLessModeStat aimLessModeStat) {

    }

    @Override
    public void updateAimlessModeCongestionInfo(AimLessModeCongestionInfo aimLessModeCongestionInfo) {

    }

    @Override
    public void onPlayRing(int i) {

    }
}
