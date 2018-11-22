package com.kangzhan.student.HomeFragment.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.holenzhou.pullrecyclerview.BaseRecyclerAdapter;
import com.holenzhou.pullrecyclerview.BaseViewHolder;
import com.kangzhan.student.HomeFragment.Bean.TrainPlace;
import com.kangzhan.student.R;
import com.kangzhan.student.ShowUI.mToast;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kangzhan011 on 2017/11/24.
 */

public class SchoolTrainPlaceAdapter extends BaseRecyclerAdapter<TrainPlace> {
    private ArrayList<TrainPlace> data;
    private Context context;
    public SchoolTrainPlaceAdapter(Context context, int layoutResId, ArrayList<TrainPlace> data) {
        super(context, layoutResId, data);
        this.context=context;
        this.data=data;
    }

    @Override
    protected void convert(BaseViewHolder holder, final TrainPlace item) {
        ImageView place_iv= (ImageView) holder.getView().findViewById(R.id.item_home_school_train_place_iv);
        TextView place_name= (TextView) holder.getView().findViewById(R.id.item_home_school_train_place_name);
        TextView address= (TextView) holder.getView().findViewById(R.id.item_home_school_train_place_address);
        TextView distance= (TextView) holder.getView().findViewById(R.id.item_home_school_train_place_distance);
        ImageView go= (ImageView) holder.getView().findViewById(R.id.item_home_school_train_place_go);
        //
        place_name.setText(item.getName());
        address.setText(item.getAddress());
        DecimalFormat format=new DecimalFormat("0.00");
        float f=Float.valueOf(item.getDistance());
        distance.setText("距您"+format.format(f)+"KM");
        //没有练车场地头像
        Glide.with(context).load("").placeholder(R.drawable.home_place).error(R.drawable.home_place).into(place_iv);
        //导航，调用外部导航
        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_VIEW);
                if(item.getLatitude().equals("")||item.getLongitude().equals("")){
                    mToast.showText(context.getApplicationContext(),"导航错误，没有经纬度");
                }else {
                    if(isAvilible("com.baidu.BaiduMap")){
                        intent.setData(Uri.parse("baidumap://map/marker?location="+item.getLatitude()+","+item.getLongitude()+"&title=目的地"));
                        context.startActivity(intent);
                    }else if(isAvilible("com.autonavi.minimap")){
                        intent.setData(Uri.parse("http://uri.amap.com/marker?position="+item.getLongitude()+","+item.getLatitude()+"&name=目的地&src=康展学车&coordinate=gaode&callnative=1"));
                        context.startActivity(intent);
                    }else {
                        mToast.showText(context.getApplicationContext(),"您未安装百度或高德地图");
                    }
                }
            }
        });
    }


    public boolean isAvilible(String packageName){
        PackageManager packageManager=context.getPackageManager();
        List<PackageInfo> packageInfos=packageManager.getInstalledPackages(0);
        List<String> packageNames=new ArrayList<>();
        if(packageInfos!=null){
            for (int i = 0; i <packageInfos.size() ; i++) {
                packageNames.add(packageInfos.get(i).packageName);
            }
        }
        return packageNames.contains(packageName);
    }
}
