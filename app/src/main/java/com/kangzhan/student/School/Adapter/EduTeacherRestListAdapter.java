package com.kangzhan.student.School.Adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.holenzhou.pullrecyclerview.BaseRecyclerAdapter;
import com.holenzhou.pullrecyclerview.BaseViewHolder;
import com.kangzhan.student.R;
import com.kangzhan.student.School.Bean.EduTeacherRestList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kangzhan011 on 2017/6/30.
 */

public class EduTeacherRestListAdapter extends BaseRecyclerAdapter<EduTeacherRestList> {
    private ArrayList<EduTeacherRestList> data;
    private Context context;
    public EduTeacherRestListAdapter(Context context, int layoutResId, ArrayList<EduTeacherRestList> data) {
        super(context, layoutResId, data);
        this.data=data;
        this.context=context;
    }

    @Override
    protected void convert(BaseViewHolder holder, final EduTeacherRestList item) {
        RelativeLayout container= (RelativeLayout) holder.getView().findViewById(R.id.school_edu_teacher_restlist_container);
        ImageView choice= (ImageView) holder.getView().findViewById(R.id.school_edu_teacher_restlist_choice);
        TextView name= (TextView) holder.getView().findViewById(R.id.school_edu_teacher_restlist_name);
        TextView starT= (TextView) holder.getView().findViewById(R.id.school_edu_teacher_restlist_starT);
        TextView endT= (TextView) holder.getView().findViewById(R.id.school_edu_teacher_restlist_endT);
        TextView reason= (TextView) holder.getView().findViewById(R.id.school_edu_teacher_restlist_reason);

        if(item.isShow()){
            container.setVisibility(View.VISIBLE);
        }else {
            container.setVisibility(View.GONE);
        }
        if(item.isClick()){
            choice.setImageResource(R.mipmap.student_test_choice1);
        }else {
            choice.setImageResource(R.mipmap.student_test_choice);
        }
        name.setText("姓名："+item.getCoach_name()+"  "+"联系电话："+item.getCoach_phone());
        starT.setText("开始时间："+item.getStart_time());
        endT.setText("结束时间："+item.getEnd_time());
        reason.setText("缘由："+item.getReason());
        holder.getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(item.isShow()){
                    if(item.isClick()){
                        item.setClick(false);
                    }else {
                        item.setClick(true);
                    }
                }
                notifyDataSetChanged();
            }
        });
    }
}
