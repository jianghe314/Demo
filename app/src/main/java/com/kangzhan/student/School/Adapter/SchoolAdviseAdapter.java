package com.kangzhan.student.School.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.holenzhou.pullrecyclerview.BaseRecyclerAdapter;
import com.holenzhou.pullrecyclerview.BaseViewHolder;
import com.kangzhan.student.R;
import com.kangzhan.student.School.Bean.SchoolAdvise;
import com.kangzhan.student.School.Notice.School_Advise_detail;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kangzhan011 on 2017/7/11.
 */

public class SchoolAdviseAdapter extends BaseRecyclerAdapter<SchoolAdvise> {
    private Context context;
    private ArrayList<SchoolAdvise> data;
    public SchoolAdviseAdapter(Context context, int layoutResId, ArrayList<SchoolAdvise> data) {
        super(context, layoutResId, data);
        this.context=context;
        this.data=data;
    }

    @Override
    protected void convert(BaseViewHolder holder, final SchoolAdvise item) {
        RelativeLayout container= (RelativeLayout) holder.getView().findViewById(R.id.school_advise_container);
        RelativeLayout bg= (RelativeLayout) holder.getView().findViewById(R.id.school_advise_bg);
        ImageView choice= (ImageView) holder.getView().findViewById(R.id.school_advise_choice);
        TextView name= (TextView) holder.getView().findViewById(R.id.school_advise_name);
        TextView phone= (TextView) holder.getView().findViewById(R.id.school_advise_phone);
        TextView time= (TextView) holder.getView().findViewById(R.id.school_advise_time);
        TextView content= (TextView) holder.getView().findViewById(R.id.school_advise_content);
        TextView status= (TextView) holder.getView().findViewById(R.id.school_advise_status);
        //
        if(item.isClick()){
            choice.setImageResource(R.mipmap.student_test_choice1);
        }else {
            choice.setImageResource(R.mipmap.student_test_choice);
        }
        if(item.isShow()){
            container.setVisibility(View.VISIBLE);
        }else {
            container.setVisibility(View.GONE);
        }
        if(item.getStatus_name().equals("已处理")){
            bg.setBackgroundColor(ContextCompat.getColor(context,R.color.colorPrimary));
        }else {
            bg.setBackgroundColor(ContextCompat.getColor(context,R.color.swipe_color1));
        }
        status.setText(item.getStatus_name());
        name.setText("客服："+item.getClerk_name());
        phone.setText("客服电话："+item.getClerk_phone());
        time.setText("登记时间："+item.getCreate_time());
        content.setText(item.getPhone_summary());
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
                   Intent detail=new Intent(context, School_Advise_detail.class);
                   detail.putExtra("Id",item.getId());
                   context.startActivity(detail);
               }
            }
        });

    }
}
