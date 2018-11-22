package com.kangzhan.student.CompayManage.Adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.holenzhou.pullrecyclerview.BaseRecyclerAdapter;
import com.holenzhou.pullrecyclerview.BaseViewHolder;
import com.kangzhan.student.CompayManage.Bean.CompayTrainingOrder;
import com.kangzhan.student.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kangzhan011 on 2017/7/26.
 */

public class TrainingOrderAdapter extends BaseRecyclerAdapter<CompayTrainingOrder> {
    private Context context;
    private ArrayList<CompayTrainingOrder> data;
    public TrainingOrderAdapter(Context context, int layoutResId, ArrayList<CompayTrainingOrder> data) {
        super(context, layoutResId, data);
        this.context=context;
        this.data=data;
    }

    @Override
    protected void convert(BaseViewHolder holder, final CompayTrainingOrder item) {
        RelativeLayout container= (RelativeLayout) holder.getView().findViewById(R.id.compay_teaching_training_container);
        ImageView choice= (ImageView) holder.getView().findViewById(R.id.compay_teaching_training_choice);
        TextView name= (TextView) holder.getView().findViewById(R.id.compay_teaching_trainig_name);
        TextView pickUp= (TextView) holder.getView().findViewById(R.id.compay_teaching_training_pickUp);
        TextView price= (TextView) holder.getView().findViewById(R.id.compay_teaching_training_price);
        TextView time= (TextView) holder.getView().findViewById(R.id.compay_teaching_training_time);
        RelativeLayout bg= (RelativeLayout) holder.getView().findViewById(R.id.compay_teaching_training_bg);
        TextView status= (TextView) holder.getView().findViewById(R.id.compay_teaching_training_status);
        //
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
        name.setText(item.getStuname());
        pickUp.setText(item.getShuttle_name());
        price.setText("￥"+item.getPrice());
        time.setText("课程："+item.getTakingdate()+" "+item.getStime()+"-"+item.getEtime()+" "+item.getDetail()+" "+item.getLicnum());
        holder.getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(item.isShow()){
                    if(item.isClick()){
                        item.setClick(false);
                    }else {
                        item.setClick(true);
                    }
                    notifyDataSetChanged();
                }
            }
        });
    }
}
