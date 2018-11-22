package com.kangzhan.student.School.Adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.holenzhou.pullrecyclerview.BaseRecyclerAdapter;
import com.holenzhou.pullrecyclerview.BaseViewHolder;
import com.kangzhan.student.R;
import com.kangzhan.student.School.Bean.EduTeaMSetLesson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kangzhan011 on 2017/6/29.
 */

public class EduTeaMSetLessonAdapter extends BaseRecyclerAdapter<EduTeaMSetLesson> {
    private ArrayList<EduTeaMSetLesson> data;
    private Context context;
    public EduTeaMSetLessonAdapter(Context context, int layoutResId, ArrayList<EduTeaMSetLesson> data) {
        super(context, layoutResId, data);
        this.data=data;
        this.context=context;
    }

    @Override
    protected void convert(BaseViewHolder holder, final EduTeaMSetLesson item) {
        ImageView choice= (ImageView) holder.getView().findViewById(R.id.item_school_edu_setlessson_choice);
        TextView name= (TextView) holder.getView().findViewById(R.id.item_school_edu_setLesson_name);
        TextView hours= (TextView) holder.getView().findViewById(R.id.item_school_edu_setLesson_hours);
        TextView type= (TextView) holder.getView().findViewById(R.id.item_school_edu_setLesson_type);
        TextView price= (TextView) holder.getView().findViewById(R.id.item_school_edu_setLesson_price);
        TextView time= (TextView) holder.getView().findViewById(R.id.item_school_edu_setLesson_time);
        //
        if(item.isClick()){
            choice.setImageResource(R.mipmap.student_test_choice1);
        }else {
            choice.setImageResource(R.mipmap.student_test_choice);
        }
        name.setText("名称："+item.getName());
        hours.setText("课时人数"+item.getMax_stu());
        type.setText("类型："+item.getTraintype()+item.getStage());
        price.setText("价格："+item.getPrice());
        time.setText("时间："+item.getStart_time()+"至"+item.getEnd_time());
        holder.getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < data.size(); i++) {
                    data.get(i).setClick(false);
                }
                item.setClick(true);
                notifyDataSetChanged();
            }
        });


    }
}
