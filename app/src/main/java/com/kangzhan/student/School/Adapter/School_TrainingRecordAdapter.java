package com.kangzhan.student.School.Adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.holenzhou.pullrecyclerview.BaseRecyclerAdapter;
import com.holenzhou.pullrecyclerview.BaseViewHolder;
import com.kangzhan.student.R;
import com.kangzhan.student.School.Bean.SchoolTrainingRecord;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kangzhan011 on 2017/7/5.
 */

public class School_TrainingRecordAdapter extends BaseRecyclerAdapter<SchoolTrainingRecord> {
    private ArrayList<SchoolTrainingRecord> data;
    private Context context;
    public School_TrainingRecordAdapter(Context context, int layoutResId, ArrayList<SchoolTrainingRecord> data) {
        super(context, layoutResId, data);
        this.context=context;
        this.data=data;
    }

    @Override
    protected void convert(BaseViewHolder holder, final SchoolTrainingRecord item) {
        ImageView choice= (ImageView) holder.getView().findViewById(R.id.item_school_training_record_choice);
        ImageView sex= (ImageView) holder.getView().findViewById(R.id.item_school_training_record_sex);
        TextView name= (TextView) holder.getView().findViewById(R.id.item_school_training_record_name);
        TextView pickUp= (TextView) holder.getView().findViewById(R.id.item_school_training_record_pickUp);
        TextView time= (TextView) holder.getView().findViewById(R.id.item_school_training_record_time);
        TextView price= (TextView) holder.getView().findViewById(R.id.item_school_training_record_price);
        //
        if(item.isClick()){
            choice.setImageResource(R.mipmap.student_test_choice1);
        }else {
            choice.setImageResource(R.mipmap.student_test_choice);
        }
        if(item.getSex().equals("男")){
            sex.setImageResource(R.mipmap.boy);
        }else {
            sex.setImageResource(R.mipmap.girl);
        }
        name.setText(item.getStuname());
        pickUp.setText(item.getShuttle_name());
        time.setText(item.getApplydate()+" "+item.getStime()+"-"+item.getEtime()+" "+item.getCoachname()+" "+Math.round(Float.valueOf(item.getTimefiff()))+"课时");
        price.setText("￥"+item.getPrice());
        //item的点击事件
        holder.getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(item.isClick()){
                    item.setClick(false);
                }else {
                    item.setClick(true);
                }
                notifyDataSetChanged();
            }
        });
    }
}
