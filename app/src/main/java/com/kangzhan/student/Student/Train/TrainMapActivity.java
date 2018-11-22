package com.kangzhan.student.Student.Train;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Location;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.google.gson.Gson;
import com.kangzhan.student.Debug.mLog;
import com.kangzhan.student.ShowUI.mToast;
import com.kangzhan.student.ShowUI.showProgress;
import com.kangzhan.student.R;
import com.kangzhan.student.Student.bean.TrainMapTeacherList;
import com.kangzhan.student.com.BaseActivity;
import com.kangzhan.student.mInterface.StudentInterface.student;
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


public class TrainMapActivity extends BaseActivity implements View.OnClickListener,AMap.InfoWindowAdapter{
    private Button btn;
    private ImageView setLocation;

    //
    private  final int STROKE_COLOR = Color.argb(180, 3, 145, 255);
    private  final int FILL_COLOR = Color.argb(10, 0, 0, 180);
    //
    private MapView mapView;
    private AMap aMap;
    //
    private View infoWindow=null;
    private ArrayList<TrainMapTeacherList> mdata=new ArrayList<>();
    private Gson gson;
    //先设置界面属性，在设置定位属性

    //标识，用于判断是否只显示一次定位信息和用户重新定位
    private boolean isFirstLoc = true;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0000:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.showProgress(TrainMapActivity.this,"加载中...");
                        }
                    });
                    break;
                case 1111:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.closeProgress();
                            showMap();
                        }
                    });
                    break;
                case 2222:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.closeProgress();
                            showMap();
                        }
                    });
                    break;
                case 9999:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgress.closeProgress();
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
        setContentView(R.layout.activity_train_map);
        gson=new Gson();
        //修改默认显示在北京
        //mLog.e("小米测试","->Lat-long"+getLatLong()[0]+"-"+getLatLong()[1]);
        LatLng centerMePoint=new LatLng(Double.valueOf(getLatLong()[0]),Double.valueOf(getLatLong()[1]));
        AMapOptions mapOptions=new AMapOptions();
        mapOptions.camera(new CameraPosition(centerMePoint,10f,0,0));
        mapView=new MapView(TrainMapActivity.this,mapOptions);
        //
        mapView= (MapView) findViewById(R.id.train_map);
        mapView.onCreate(savedInstanceState);
        handler.sendEmptyMessage(0000);
        new Thread(new Runnable() {
            @Override
            public void run() {
                sendRequest("1","latitude","longitude",getLatLong()[0],getLatLong()[1]);
            }
        }).start();

    }

    private void showMap() {
        if(aMap==null){
            aMap=mapView.getMap();
            //设置地图的默认缩放比例
            aMap.moveCamera(CameraUpdateFactory.zoomTo(15));
            aMap.setInfoWindowAdapter(this);
            // 如果不设置标志位，此时再拖动地图时，它会不断将地图移动到当前的位置,展示

            setUpMap();
            //添加Marker
            addMarkersToMap();// 往地图上添加marker
        }
        btn= (Button) findViewById(R.id.train_map_btn);
        btn.setOnClickListener(this);
        setLocation= (ImageView) findViewById(R.id.train_map_setLocation);
        setLocation.setOnClickListener(this);
    }


    private void addMarkersToMap() {
        //设置图钉点样式
        for (int i = 0; i <mdata.size(); i++) {
            TrainMapTeacherList item=mdata.get(i);
            MarkerOptions option=new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.location_icon));
            LatLng lag=new LatLng(item.getLatitude(),item.getLongitude());
            option.position(lag);
            option.title("");
            option.draggable(false);
            Marker marker=aMap.addMarker(option);
            marker.setObject(item);
        }
    }

    private void setUpMap() {
        UiSettings setting=aMap.getUiSettings();
        setting.setZoomControlsEnabled(false);
        setting.setMyLocationButtonEnabled(false);// 设置默认定位按钮是否显示
        //3.0 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        aMap.setMyLocationEnabled(true);
        setupLocationStyle();

    }
    //设置自定义定位蓝点
    private void setupLocationStyle() {
        MyLocationStyle myLocationStyle=new MyLocationStyle();
        myLocationStyle.interval(2000);
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE);
//        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE);
        //myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW_NO_CENTER);
        //自定义定位蓝点
//        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.mipmap.gps_point));
//        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW_NO_CENTER);
//        myLocationStyle.strokeColor(STROKE_COLOR);
//        myLocationStyle.strokeWidth(5);
//        myLocationStyle.radiusFillColor(FILL_COLOR);
        aMap.setMyLocationStyle(myLocationStyle);
        aMap.setMyLocationEnabled(true);
        aMap.setOnMyLocationChangeListener(new AMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                Bundle bundle=location.getExtras();
                int errorCode=bundle.getInt((MyLocationStyle.ERROR_CODE));
                //mToast.showText(getApplicationContext(),"错误代码"+errorCode);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.train_map_btn:
                Intent apply=new Intent(this,Train_applyActivity.class);
                StringBuilder builder=new StringBuilder();
                for (int i = 0; i <mdata.size(); i++) {
                    builder.append(mdata.get(i).getId());
                    if(i<mdata.size()-1){
                        builder.append(",");
                    }
                }
                apply.putExtra("Id",builder.toString());
                startActivity(apply);
                break;
            case R.id.train_map_setLocation:
                //跳转到地区设置
                Intent setLocation=new Intent(this,Train_SetLocationActivity.class);
                startActivity(setLocation);
                break;
        }

    }

    private void sendRequest(String type,String params1,String params2,String p1,String p2) {
        Request<JSONObject> request=NoHttp.createJsonObjectRequest(student.studentTrainGetTeacher(), RequestMethod.GET);
        request.add("key",student.studentKey(getApplicationContext()));
        request.add("type",1);
        request.add(params1,p1);
        request.add(params2,p2);
        getRequestQueue().add(1, request, new OnResponseListener<JSONObject>() {
            @Override
            public void onStart(int what) {

            }

            @Override
            public void onSucceed(int what, Response<JSONObject> response) {
                try {
                    mLog.e("reponse","-->"+response.get().toString());
                    JSONObject object=new JSONObject(response.get().toString());
                    String code=object.getString("code");
                    if(code.equals("200")){
                        String data=object.getString("data");
                        JSONArray array=new JSONArray(data);
                        if(array.length()>0){
                            mdata.clear();
                            for (int i = 0; i <array.length(); i++) {
                                TrainMapTeacherList teacher=gson.fromJson(array.getJSONObject(i).toString(),TrainMapTeacherList.class);
                                mdata.add(teacher);
                            }
                        }else {
                            mdata.clear();
                        }
                        handler.sendEmptyMessage(1111);
                    }else {
                        handler.sendEmptyMessage(2222);
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
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }
    @Override
    public View getInfoContents(Marker marker) {
        if(infoWindow==null){
            infoWindow= LayoutInflater.from(TrainMapActivity.this).inflate(R.layout.item_train_map_infowindow,null);
        }
        render(marker,infoWindow);
        return infoWindow;
    }

    private void render(Marker marker, View infoWindow) {
        TrainMapTeacherList item = (TrainMapTeacherList) marker.getObject();
        TextView name= (TextView) infoWindow.findViewById(R.id.item_train_map_info_name);
        name.setText(item.getName());
        TextView distence= (TextView) infoWindow.findViewById(R.id.item_train_map_info_distence);
        distence.setText(String.format("%.2f",item.getDistance())+"km");
    }

    private  String[] getLatLong(){
        String[] mm=new String[2];
        SharedPreferences sp=getSharedPreferences("studentLocation",MODE_PRIVATE);
        //默认显示在北京
        mm[0]=sp.getString("lat","39.904989");
        mm[1]=sp.getString("long","116.405285");
        return mm;
    }




}
