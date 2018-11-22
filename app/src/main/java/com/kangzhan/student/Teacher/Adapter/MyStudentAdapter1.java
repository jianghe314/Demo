package com.kangzhan.student.Teacher.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.holenzhou.pullrecyclerview.BaseRecyclerAdapter;
import com.holenzhou.pullrecyclerview.BaseViewHolder;
import com.kangzhan.student.R;
import com.kangzhan.student.Teacher.Booking.Teacher_student_detail;
import com.kangzhan.student.Teacher.bean.TeacherMyStudent;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by kangzhan011 on 2017/5/2.
 */

public class MyStudentAdapter1 extends BaseRecyclerAdapter<TeacherMyStudent> {
    private Context context;
    private ArrayList<TeacherMyStudent> data;
    public MyStudentAdapter1(Context context, int layoutResId, ArrayList<TeacherMyStudent> data) {
        super(context, layoutResId, data);
        this.context=context;
        this.data=data;
    }

    @Override
    protected void convert(BaseViewHolder holder, final TeacherMyStudent item) {
        CircleImageView header= (CircleImageView) holder.getView().findViewById(R.id.teacher_mystudent_header);
        ImageView sex= (ImageView) holder.getView().findViewById(R.id.teacher_mystudent_iv2);
        TextView name= (TextView) holder.getView().findViewById(R.id.teacher_mystudent_name);
        TextView stastu= (TextView) holder.getView().findViewById(R.id.teacher_mystudent_class);
        TextView phone= (TextView) holder.getView().findViewById(R.id.teacher_mystudent_phone);
        //
        if(item.getSex().equals("男")){
            sex.setImageResource(R.mipmap.boy);
        }else {
            sex.setImageResource(R.mipmap.girl);
        }
        name.setText(item.getName());
        stastu.setText(item.getTraintype()+"-"+item.getStatus_str());
        phone.setText(item.getPhone());
        holder.getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent detail=new Intent(context, Teacher_student_detail.class);
                detail.putExtra("Id",item.getId());
                context.startActivity(detail);
            }
        });
    }
}
