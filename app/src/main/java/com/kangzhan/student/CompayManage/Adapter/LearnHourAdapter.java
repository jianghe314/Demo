package com.kangzhan.student.CompayManage.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.holenzhou.pullrecyclerview.BaseRecyclerAdapter;
import com.holenzhou.pullrecyclerview.BaseViewHolder;
import com.kangzhan.student.CompayManage.Bean.CompayLearnHour;
import com.kangzhan.student.CompayManage.TeachingManage.LearnHourDetail;
import com.kangzhan.student.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kangzhan011 on 2017/7/26.
 */

public class LearnHourAdapter extends BaseRecyclerAdapter<CompayLearnHour> {
    private Context context;
    private ArrayList<CompayLearnHour> data;
    public LearnHourAdapter(Context context, int layoutResId, ArrayList<CompayLearnHour> data) {
        super(context, layoutResId, data);
        this.context=context;
        this.data=data;
    }

    @Override
    protected void convert(BaseViewHolder holder, final CompayLearnHour item) {
        RelativeLayout container= (RelativeLayout) holder.getView().findViewById(R.id.compay_teaching_hour_container);
        ImageView choice= (ImageView) holder.getView().findViewById(R.id.compay_teaching_hour_choice);
        TextView name= (TextView) holder.getView().findViewById(R.id.compay_teaching_hour_name);
        TextView code= (TextView) holder.getView().findViewById(R.id.compay_teaching_hour_code);
        TextView time= (TextView) holder.getView().findViewById(R.id.compay_teaching_hour_time);
        RelativeLayout bg= (RelativeLayout) holder.getView().findViewById(R.id.compay_teaching_hour_bg);
        TextView status= (TextView) holder.getView().findViewById(R.id.compay_teaching_hour_status);
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
        name.setText(item.getName());
        code.setText("计时仪编号："+item.getDevice_id());
        time.setText("时间："+item.getSdate()+" "+item.getStarttime()+" "+item.getEndtime());
        status.setText(item.getSynchro_flag());
        if(item.getSynchro_flag().equals("已同步")){
            bg.setBackgroundColor(ContextCompat.getColor(context,R.color.colorPrimary));
        }else {
            bg.setBackgroundColor(ContextCompat.getColor(context,R.color.textColor_orange));
        }
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
                }else {
                    Intent detail=new Intent(context, LearnHourDetail.class);
                    detail.putExtra("Id",item.getId());
                    context.startActivity(detail);
                }
            }
        });
    }
}
