package com.kangzhan.student.School.Adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.holenzhou.pullrecyclerview.BaseRecyclerAdapter;
import com.holenzhou.pullrecyclerview.BaseViewHolder;
import com.kangzhan.student.R;
import com.kangzhan.student.School.Bean.EduLearnHours;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kangzhan011 on 2017/7/4.
 */

public class EduLearnHourAdapter extends BaseRecyclerAdapter<EduLearnHours> {
    private ArrayList<EduLearnHours> data;
    private Context context;
    public EduLearnHourAdapter(Context context, int layoutResId, ArrayList<EduLearnHours> data) {
        super(context, layoutResId, data);
        this.context=context;
        this.data=data;
    }

    @Override
    protected void convert(BaseViewHolder holder, final EduLearnHours item) {
        ImageView choice= (ImageView) holder.getView().findViewById(R.id.item_school_edu_lh_choice);
        ImageView sex= (ImageView) holder.getView().findViewById(R.id.item_school_edu_lh_sex);
        TextView name= (TextView) holder.getView().findViewById(R.id.item_school_edu_lh_name);
        TextView teacher= (TextView) holder.getView().findViewById(R.id.item_school_edu_lh_teacher);
        TextView subject= (TextView) holder.getView().findViewById(R.id.item_school_edu_lh_subject);
        TextView time= (TextView) holder.getView().findViewById(R.id.item_school_edu_lh_time);
        TextView syn_flag= (TextView) holder.getView().findViewById(R.id.item_school_edu_lh_synFlag);
        RelativeLayout bg= (RelativeLayout) holder.getView().findViewById(R.id.item_school_edu_lh_bg);
        //
        if(item.isClick()){
            choice.setImageResource(R.mipmap.student_test_choice1);
        }else {
            choice.setImageResource(R.mipmap.student_test_choice);
        }
        if(item.getStudent_sex().equals("男")){
            sex.setImageResource(R.mipmap.boy);
        }else {
            sex.setImageResource(R.mipmap.girl);
        }
        name.setText(item.getStudent_name());
        teacher.setText("教练："+item.getCoach_name());
        subject.setText("科目："+item.getStage_name());
        time.setText("课程："+item.getCreate_time());
        if(item.getSynchro_flag_name().equals("暂存")){
            bg.setBackgroundColor(ContextCompat.getColor(context,R.color.Not_read_color));
        }else {
            bg.setBackgroundColor(ContextCompat.getColor(context,R.color.colorPrimary));
        }
        syn_flag.setText(item.getSynchro_flag_name());
        //
       /* holder.getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(item.isClick()){
                    item.setClick(false);
                }else {
                    item.setClick(true);
                }
                notifyDataSetChanged();
            }
        });*/

    }
}
