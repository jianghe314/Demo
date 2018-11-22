package com.kangzhan.student.Student.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.navi.AmapRouteActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.holenzhou.pullrecyclerview.BaseRecyclerAdapter;
import com.holenzhou.pullrecyclerview.BaseViewHolder;
import com.kangzhan.student.R;
import com.kangzhan.student.ShowUI.mToast;
import com.kangzhan.student.Student.Booking.AmapNaviActivity;
import com.kangzhan.student.Student.Booking.Teacher_DetailActivity;
import com.kangzhan.student.Student.Interface.ItemCallPhone;
import com.kangzhan.student.Student.Interface.ItemOnNavi;
import com.kangzhan.student.Student.bean.BookingTeacher;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by kangzhan011 on 2017/3/21.
 */

public class BookingRecordAdapter extends BaseRecyclerAdapter<BookingTeacher> {
    private Context context;
    private ArrayList<BookingTeacher> data;
    private ItemCallPhone callPhone;
    private ItemOnNavi onNavi;


    public BookingRecordAdapter(Context context, int layoutResId, ArrayList<BookingTeacher> data) {
        super(context, layoutResId, data);
        this.context=context;
        this.data=data;
    }

    @Override
    protected void convert(BaseViewHolder holder, final BookingTeacher item) {
        View colorline=holder.getView().findViewById(R.id.booking_colorLine);
        ImageView location= (ImageView) holder.getView().findViewById(R.id.booking_rt);
        ImageView mcallPhone= (ImageView) holder.getView().findViewById(R.id.booking_phone); //打电话
        CircleImageView header= (CircleImageView) holder.getView().findViewById(R.id.booking_iv);
        TextView school= (TextView) holder.getView().findViewById(R.id.booking_school);
        ImageView sex= (ImageView) holder.getView().findViewById(R.id.booking_sex);
        TextView name= (TextView) holder.getView().findViewById(R.id.booking_name);
        TextView evalu= (TextView) holder.getView().findViewById(R.id.booking_evalu);
        TextView car_type= (TextView) holder.getView().findViewById(R.id.booking_car);
        TextView car_label= (TextView) holder.getView().findViewById(R.id.booking_label);
        TextView address= (TextView) holder.getView().findViewById(R.id.booking_address);    //导航
        TextView teacher_year= (TextView) holder.getView().findViewById(R.id.booking_year);
        TextView phone_Num= (TextView) holder.getView().findViewById(R.id.booking_phone_Num);
        //赋值数据
        if(holder.getAdapterPosition()%3==0){
            colorline.setBackgroundResource(R.color.text_background_color_aqua);
        }else if(holder.getAdapterPosition()%3==1){
            colorline.setBackgroundResource(R.color.textColor_red);
        }else {
            colorline.setBackgroundResource(R.color.colorPrimary);
        }
        if (item.getSex()==1){              //1:男   0：女
            sex.setImageResource(R.mipmap.boy);
        }else {
            sex.setImageResource(R.mipmap.girl);
        }
        Glide.with(context).load(item.getOss_photo()).diskCacheStrategy(DiskCacheStrategy.NONE).error(R.drawable.header).crossFade().into(header);
        school.setText(item.getInstitutionname());
        name.setText(item.getName());
        evalu.setText("好评分"+item.getScore());
        car_type.setText(item.getBrand());
        car_label.setText(item.getLicnum());
        address.setText(item.getAddress());
        teacher_year.setText(item.getExperience_years()+"年教龄");
        phone_Num.setText(item.getMobile());
        //
        holder.getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent teacher_detail=new Intent(context,Teacher_DetailActivity.class);
                teacher_detail.putExtra("id",item.getId());
                context.startActivity(teacher_detail);
            }
        });
        address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onNavi!=null){
                    onNavi.itemOnNavi(item.getLatitude(),item.getLongitude());
                }
            }
        });
        mcallPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //写一个接口回调，提供给Fragment打电话
                if(callPhone!=null){
                    callPhone.itemCallPhone(item.getMobile());
                }
            }
        });

    }

    public void CallPhone(ItemCallPhone callPhone){
        this.callPhone=callPhone;
    }
    public void OnItemNavi(ItemOnNavi onNavi){
        this.onNavi=onNavi;
    }



}
