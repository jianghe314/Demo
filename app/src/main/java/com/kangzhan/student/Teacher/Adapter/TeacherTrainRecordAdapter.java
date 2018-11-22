package com.kangzhan.student.Teacher.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.holenzhou.pullrecyclerview.BaseRecyclerAdapter;
import com.holenzhou.pullrecyclerview.BaseViewHolder;
import com.kangzhan.student.R;
import com.kangzhan.student.Teacher.Booking.Teacher_student_remark;
import com.kangzhan.student.Teacher.Train.Teacher_train_RemarkActivity;
import com.kangzhan.student.Teacher.bean.TeacherTrainRecord;

import java.util.ArrayList;


import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by kangzhan011 on 2017/4/14.
 */

public class TeacherTrainRecordAdapter extends BaseRecyclerAdapter<TeacherTrainRecord> {
    private Context context;
    private ArrayList<TeacherTrainRecord> data;
    public TeacherTrainRecordAdapter(Context context, int layoutResId, ArrayList<TeacherTrainRecord> data) {
        super(context, layoutResId, data);
        this.context=context;
        this.data=data;
    }

    @Override
    protected void convert(BaseViewHolder holder, final TeacherTrainRecord item) {
        View colorLine=holder.getView().findViewById(R.id.teacher_train_record_item_colorLine);
        CircleImageView header= (CircleImageView) holder.getView().findViewById(R.id.teacher_train_record_item_header);
        TextView lesson= (TextView) holder.getView().findViewById(R.id.item_record_isId);
        ImageView sex= (ImageView) holder.getView().findViewById(R.id.item_record_sex);
        TextView name= (TextView) holder.getView().findViewById(R.id.item_record_name);
        TextView pickup= (TextView) holder.getView().findViewById(R.id.item_record_pickUp);
        TextView price= (TextView) holder.getView().findViewById(R.id.item_record_price);
        TextView date= (TextView) holder.getView().findViewById(R.id.item_record_date);
        TextView address= (TextView) holder.getView().findViewById(R.id.item_record_address);
        TextView has_remark= (TextView) holder.getView().findViewById(R.id.item_teacher_trainRecord_eval1);
        TextView remark= (TextView) holder.getView().findViewById(R.id.item_teacher_trainRecord_eval0);
        //赋值数据
        if(holder.getAdapterPosition()%2==0){
            colorLine.setBackgroundColor(ContextCompat.getColor(context,R.color.textColor_orange));
        }else if(holder.getAdapterPosition()%2==1){
            colorLine.setBackgroundColor(ContextCompat.getColor(context,R.color.colorPrimary));
        }else {
            colorLine.setBackgroundColor(ContextCompat.getColor(context,R.color.text_background_color_aqua));
        }
        Glide.with(context).load(item.getOss_photo()).diskCacheStrategy(DiskCacheStrategy.NONE).error(R.drawable.header).crossFade().into(header);
        lesson.setText(item.getDetail());
        if(item.getSex().equals("2")){
            sex.setImageResource(R.mipmap.girl);
        }else {
            sex.setImageResource(R.mipmap.boy);
        }
        name.setText(item.getStuname());
        pickup.setText(item.getShuttle_name());
        price.setText("￥"+item.getPrice());
        date.setText(item.getStart_time()+"-"+item.getEnd_time());
        address.setText(item.getAddress());
        has_remark.setVisibility(View.GONE);
        remark.setText(item.getSuggest_status_name());
        remark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mremark=new Intent(context, Teacher_train_RemarkActivity.class);
                mremark.putExtra("Id",item.getId());
                context.startActivity(mremark);
            }
        });
    }
}
