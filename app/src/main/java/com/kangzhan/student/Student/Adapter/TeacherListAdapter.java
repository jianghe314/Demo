package com.kangzhan.student.Student.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.holenzhou.pullrecyclerview.BaseRecyclerAdapter;
import com.holenzhou.pullrecyclerview.BaseViewHolder;
import com.kangzhan.student.Student.Booking.Teacher_DetailActivity;
import com.kangzhan.student.R;
import com.kangzhan.student.Student.bean.BookingTeacher;
import com.kangzhan.student.Student.bean.MoreTeacher;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by kangzhan011 on 2017/3/28.
 */

public class TeacherListAdapter extends BaseRecyclerAdapter<MoreTeacher>{
    private Context context;
    private ArrayList<MoreTeacher> data;
    public TeacherListAdapter(Context context, int layoutResId, ArrayList<MoreTeacher> data) {
        super(context, layoutResId, data);
        this.context=context;
        this.data=data;
    }

    @Override
    protected void convert(BaseViewHolder holder, final MoreTeacher item) {
        View colorline=holder.getView().findViewById(R.id.booking_colorLine);
        CircleImageView header= (CircleImageView) holder.getView().findViewById(R.id.booking_iv);
        TextView school= (TextView) holder.getView().findViewById(R.id.booking_school);
        ImageView sex= (ImageView) holder.getView().findViewById(R.id.booking_sex);
        TextView name= (TextView) holder.getView().findViewById(R.id.booking_name);
        TextView score= (TextView) holder.getView().findViewById(R.id.booking_evalu);
        TextView carType= (TextView) holder.getView().findViewById(R.id.booking_car);
        TextView car_label= (TextView) holder.getView().findViewById(R.id.booking_label);
        TextView year= (TextView) holder.getView().findViewById(R.id.booking_year);
        TextView address= (TextView) holder.getView().findViewById(R.id.booking_address);
        TextView phone= (TextView) holder.getView().findViewById(R.id.booking_phone_Num);
       //
        if(holder.getAdapterPosition()%3==0){
            colorline.setBackgroundResource(R.color.text_background_color_aqua);
        }else if(holder.getAdapterPosition()%3==1){
            colorline.setBackgroundResource(R.color.textColor_red);
        }else {
            colorline.setBackgroundResource(R.color.colorPrimary);
        }
        school.setText(item.getInstitutionname());
        if(item.getSex()==1){
            sex.setImageResource(R.mipmap.boy);
        }else {
            sex.setImageResource(R.mipmap.girl);
        }
        Glide.with(context).load(item.getOss_photo()).diskCacheStrategy(DiskCacheStrategy.NONE).error(R.drawable.header).crossFade().into(header);
        name.setText(item.getName());
        score.setText("好评分"+item.getScore());
        carType.setText(item.getBrand());
        car_label.setText(item.getLicnum());
        year.setText(item.getExperience_years()+"年教练");
        address.setText(item.getAddress());
        phone.setText(item.getMobile());
        holder.getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent teacher_detail=new Intent(context,Teacher_DetailActivity.class);
                teacher_detail.putExtra("id",item.getId());
                context.startActivity(teacher_detail);
            }
        });


    }
}
