package com.kangzhan.student.Student.Booking;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.kangzhan.student.Debug.mLog;
import com.kangzhan.student.R;
import com.kangzhan.student.Student_Login;
import com.kangzhan.student.com.BaseActivity;
import com.kangzhan.student.entity.City;
import com.kangzhan.student.entity.County;
import com.kangzhan.student.entity.Province;
import com.kangzhan.student.utils.AddressPickTask;

public class SetLocationActivity extends BaseActivity implements View.OnClickListener,AMapLocationListener{
    private TextView setlocation,setCurrentLocation;
    private Button sure;
    private String[] strLocation=new String[3];
    private String mprovince,mcity,mcounty;
    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;
    //初始化AMapLocationClientOption对象
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_location);
        Toolbar toolbar= (Toolbar) findViewById(R.id.student_setLocation_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        initView();
    }
    private void showToast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }
    private void initView() {
        setlocation= (TextView) findViewById(R.id.set_location);
        setlocation.setOnClickListener(this);
        sure= (Button) findViewById(R.id.set_location_sure);
        sure.setOnClickListener(this);
        setCurrentLocation= (TextView) findViewById(R.id.getCurrent_location);
        setCurrentLocation.setOnClickListener(this);
        //
        mLocationClient=new AMapLocationClient(SetLocationActivity.this);
        mLocationOption=new AMapLocationClientOption();
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        mLocationOption.setOnceLocation(true);
        mLocationOption.setOnceLocationLatest(true);
        mLocationOption.setMockEnable(true);        //容许开启模拟器定位
        mLocationOption.setHttpTimeOut(10000);
        mLocationClient.setLocationOption(mLocationOption);
        mLocationClient.startLocation();
        mLocationClient.setLocationListener(this);
        //
        if(mprovince!=null&&mcity!=null&&mcounty!=null){
            setlocation.setText(mprovince+mcity+mcounty);
        }else {
            setlocation.setText("请选择地区");
            strLocation[0]="北京市";
            strLocation[1]="北京市";
            strLocation[2]="东城区";
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.set_location:
                //选择地区
                AddressPickTask task=new AddressPickTask(this);
                task.setHideCounty(false);
                task.setHideProvince(false);
                task.setCallback(new AddressPickTask.Callback() {
                    @Override
                    public void onAddressInitFailed() {
                        showToast("数据初始化失败");
                    }

                    @Override
                    public void onAddressPicked(Province province, City city, County county) {
                        if (county == null) {
                            setlocation.setText(province.getAreaName()+city.getAreaName());
                        } else {
                            setlocation.setText(province.getAreaName()+(province.getAreaName().equals(city.getAreaName())?"":city.getAreaName())+county.getAreaName());
                        }
                        strLocation[0]=province.getAreaId();
                        strLocation[1]=city.getAreaId();
                        strLocation[2]=county.getAreaId();
                    }
                });
                task.execute("北京市", "北京市", "东城区");
                break;
            //使用当前位置
            case R.id.getCurrent_location:
                //掉GPS定位当前位置
                if(mprovince==null||mcity==null||mcounty==null){
                    setlocation.setText("定位失败，请再试");
                }else {
                    setlocation.setText(mprovince+mcity+mcounty);
                    strLocation[0]=mprovince;
                    strLocation[1]=mcity;
                    strLocation[2]=mcounty;
                }
                break;
            //确定
            case R.id.set_location_sure:
                //将位置传递给教练列表
                Intent getMore=new Intent(this,TeacherListActivity.class);
                getMore.putExtra("Location",strLocation);
                startActivity(getMore);
                break;
            default:
                break;
        }
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if(aMapLocation!=null){
            if(aMapLocation.getErrorCode()==0){
                mprovince=aMapLocation.getProvince();
                mcity=aMapLocation.getCity();
                mcounty=aMapLocation.getDistrict();
                //Log.i("pcd","->"+mprovince+"-"+mcity+"-"+mcounty);
            }else {
                mLog.e("AmapError","location Error, ErrCode:"
                        + aMapLocation.getErrorCode() + ", errInfo:"
                        + aMapLocation.getErrorInfo());
            }
        }
    }


    @Override
    protected void onStop() {
        mLocationClient.stopLocation();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mLocationClient.onDestroy();
        super.onDestroy();
    }


}
