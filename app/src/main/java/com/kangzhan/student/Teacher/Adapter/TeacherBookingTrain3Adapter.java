package com.kangzhan.student.Teacher.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.holenzhou.pullrecyclerview.BaseRecyclerAdapter;
import com.holenzhou.pullrecyclerview.BaseViewHolder;
import com.kangzhan.student.R;
import com.kangzhan.student.Teacher.Booking.Teacher_student_detail;
import com.kangzhan.student.Teacher.Booking.Teacher_student_remark;
import com.kangzhan.student.Teacher.bean.TeacherBookingTrain1;

import java.util.ArrayList;


import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by kangzhan011 on 2017/4/12.
 */

public class TeacherBookingTrain3Adapter extends BaseRecyclerAdapter<TeacherBookingTrain1> {
    private Context context;
    private ArrayList<TeacherBookingTrain1> data;
    public TeacherBookingTrain3Adapter(Context context, int layoutResId, ArrayList<TeacherBookingTrain1> data) {
        super(context, layoutResId, data);
        this.context=context;
        this.data=data;
    }

    @Override
    protected void convert(BaseViewHolder holder, final TeacherBookingTrain1 item) {
        View colorLine=holder.getView().findViewById(R.id.item_teacher_booking3_colorLine);
        CircleImageView header=holder.getView((R.id.item_teacher_booking3_header));
        TextView lesson=holder.getView(R.id.item_teacher_booking3_lesson);
        ImageView sex=holder.getView(R.id.item_teacher_booking3_sex);
        TextView name=holder.getView(R.id.item_teacher_booking3_name);
        TextView price=holder.getView(R.id.item_teacher_booking3_price);
        TextView time=holder.getView(R.id.item_teacher_booking3_time);
        TextView car_label=holder.getView(R.id.item_teacher_booking3_carLabel);
        TextView remark=holder.getView(R.id.item_teacher_booking3_startTrain);
        //
        if(holder.getAdapterPosition()%3==0){
            colorLine.setBackgroundResource(R.color.text_background_color_aqua);
        }else if(holder.getAdapterPosition()%3==1){
            colorLine.setBackgroundResource(R.color.textColor_orange);
        }else {
            colorLine.setBackgroundResource(R.color.colorPrimary);
        }
        if(data.size()>0){
            lesson.setText(item.getStage_name());
            if(item.getStudent_sex().equals("男")){
                sex.setImageResource(R.mipmap.boy);
            }else {
                sex.setImageResource(R.mipmap.girl);
            }
            Glide.with(context).load(item.getOss_photo()).diskCacheStrategy(DiskCacheStrategy.NONE).error(R.drawable.header).crossFade().into(header);
            name.setText(item.getStudent_name());
            time.setText(item.getTime_name());
            car_label.setText(item.getStudent_traintype()+" "+item.getCar_name());
            price.setText(item.getAmount());
            remark.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //点评
                    Intent remark=new Intent(context, Teacher_student_remark.class);
                    remark.putExtra("id",item.getId());
                    remark.putExtra("source","1");
                    context.startActivity(remark);
                }
            });
            holder.getView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent detail=new Intent(context, Teacher_student_detail.class);
                    detail.putExtra("Id",item.getStu_id());
                    context.startActivity(detail);
                }
            });
        }
        }

}
