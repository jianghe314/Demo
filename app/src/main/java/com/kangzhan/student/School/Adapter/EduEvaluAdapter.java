package com.kangzhan.student.School.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.holenzhou.pullrecyclerview.BaseRecyclerAdapter;
import com.holenzhou.pullrecyclerview.BaseViewHolder;
import com.kangzhan.student.R;
import com.kangzhan.student.School.Bean.EduEvalu;
import com.kangzhan.student.School.Edu.School_TeacherDetail;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kangzhan011 on 2017/7/4.
 */

public class EduEvaluAdapter extends BaseRecyclerAdapter<EduEvalu> {
    private ArrayList<EduEvalu> data;
    private Context context;
    public EduEvaluAdapter(Context context, int layoutResId, ArrayList<EduEvalu> data) {
        super(context, layoutResId, data);
        this.data=data;
        this.context=context;
    }

    @Override
    protected void convert(BaseViewHolder holder, final EduEvalu item) {
        ImageView choice= (ImageView) holder.getView().findViewById(R.id.item_school_edu_evalu_choice);
        TextView tName= (TextView) holder.getView().findViewById(R.id.item_school_edu_evalu_tName);
        TextView sName= (TextView) holder.getView().findViewById(R.id.item_school_edu_evalu_sName);
        TextView time= (TextView) holder.getView().findViewById(R.id.item_school_edu_evalu_time);
        TextView subject= (TextView) holder.getView().findViewById(R.id.item_school_edu_evalu_subject);
        LinearLayout liner= (LinearLayout) holder.getView().findViewById(R.id.item_school_edu_evalu_score);
        TextView starNum= (TextView) holder.getView().findViewById(R.id.item_school_edu_evalu_score_Num);
        //
        if(item.isClick()){
            choice.setImageResource(R.mipmap.student_test_choice1);
        }else {
            choice.setImageResource(R.mipmap.student_test_choice);
        }
        tName.setText("教练姓名："+item.getCoaname());
        sName.setText("学员姓名："+item.getStuname());
        time.setText("课程："+item.getEvalsubjcontent());
        if(item.getOverall()!=null){
            starNum.setText(item.getOverall()+"分");
            liner.removeAllViews();
            for (int i = 0; i <Integer.valueOf(item.getOverall()) ; i++) {
                ImageView star=new ImageView(context);
                star.setPadding(10,10,10,10);
                star.setImageResource(R.mipmap.star);
                liner.addView(star);
            }
        }
        holder.getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent detail=new Intent(context, School_TeacherDetail.class);
                detail.putExtra("who","school");
                detail.putExtra("teacherId",item.getCoach_id());
                context.startActivity(detail);
            }
        });

    }
}
