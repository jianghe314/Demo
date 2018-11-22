package com.kangzhan.student.School.Adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.holenzhou.pullrecyclerview.BaseRecyclerAdapter;
import com.holenzhou.pullrecyclerview.BaseViewHolder;
import com.kangzhan.student.R;
import com.kangzhan.student.School.Bean.SchoolTrainingOrder;
import com.kangzhan.student.School.Training.School_training_order;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kangzhan011 on 2017/7/5.
 */

public class SchoolTrainingOrderAdapter extends BaseRecyclerAdapter<SchoolTrainingOrder> {
    private ArrayList<SchoolTrainingOrder> data;
    private Context context;
    public SchoolTrainingOrderAdapter(Context context, int layoutResId, ArrayList<SchoolTrainingOrder> data) {
        super(context, layoutResId, data);
        this.data=data;
        this.context=context;
    }

    @Override
    protected void convert(BaseViewHolder holder, final SchoolTrainingOrder item) {
        ImageView choice= (ImageView) holder.getView().findViewById(R.id.item_school_training_order_choice);
        ImageView sex= (ImageView) holder.getView().findViewById(R.id.item_school_training_order_sex);
        TextView name= (TextView) holder.getView().findViewById(R.id.item_school_training_order_name);
        TextView pickUp= (TextView) holder.getView().findViewById(R.id.item_school_training_order_pickUp);
        TextView time= (TextView) holder.getView().findViewById(R.id.item_school_training_order_time);
        TextView lesson= (TextView) holder.getView().findViewById(R.id.item_school_training_order_lesson);
        TextView price= (TextView) holder.getView().findViewById(R.id.item_school_training_order_price);
        //
        if(item.getSex().equals("男")){
            sex.setImageResource(R.mipmap.boy);
        }else {
            sex.setImageResource(R.mipmap.girl);
        }
        name.setText(item.getStuname());
        pickUp.setText(item.getShuttle_name());
        time.setText(item.getTakingdate());
        lesson.setText(item.getApplydate()+" "+item.getStime()+"-"+item.getEtime()+item.getCoachname()+" "+Math.round(Float.valueOf(item.getTimefiff()))+"课时");
        price.setText("￥"+item.getPrice());
        if(item.isClick()){
            choice.setImageResource(R.mipmap.student_test_choice1);
        }else {
            choice.setImageResource(R.mipmap.student_test_choice);
        }
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
