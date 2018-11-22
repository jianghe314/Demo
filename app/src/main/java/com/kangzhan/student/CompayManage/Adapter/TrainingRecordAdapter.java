package com.kangzhan.student.CompayManage.Adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.holenzhou.pullrecyclerview.BaseRecyclerAdapter;
import com.holenzhou.pullrecyclerview.BaseViewHolder;
import com.kangzhan.student.CompayManage.Bean.CompayTrainingRecord;
import com.kangzhan.student.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kangzhan011 on 2017/7/26.
 */

public class TrainingRecordAdapter extends BaseRecyclerAdapter<CompayTrainingRecord>{
    private ArrayList<CompayTrainingRecord> data;
    private Context context;
    public TrainingRecordAdapter(Context context, int layoutResId, ArrayList<CompayTrainingRecord> data) {
        super(context, layoutResId, data);
        this.context=context;
        this.data=data;
    }

    @Override
    protected void convert(BaseViewHolder holder, final CompayTrainingRecord item) {
        //ImageView choice= (ImageView) holder.getView().findViewById(R.id.compay_training_record_choice);
        TextView name= (TextView) holder.getView().findViewById(R.id.compay_training_record_name);
        TextView pickUp= (TextView) holder.getView().findViewById(R.id.compay_training_record_pickUp);
        TextView price= (TextView) holder.getView().findViewById(R.id.compay_training_record_price);
        TextView subject= (TextView) holder.getView().findViewById(R.id.compay_training_record_time);
        /*
        if(item.isClick()){
            choice.setImageResource(R.mipmap.student_test_choice1);
        }else {
            choice.setImageResource(R.mipmap.student_test_choice);
        }*/
        name.setText(item.getStuname());
        pickUp.setText(item.getShuttle_name());
        price.setText("￥"+item.getPrice());
        subject.setText("课程："+item.getApplydate()+" "+item.getStime()+"-"+item.getStime()+" "+item.getDetail()+" "+item.getCoachname()+" "+item.getLicnum());
    }
}
