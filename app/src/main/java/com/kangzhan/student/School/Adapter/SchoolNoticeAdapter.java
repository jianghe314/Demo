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
import com.kangzhan.student.School.Bean.SchoolNotice;
import com.kangzhan.student.School.Notice.School_NoticeDetail;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kangzhan011 on 2017/7/10.
 */

public class SchoolNoticeAdapter extends BaseRecyclerAdapter<SchoolNotice> {
    private ArrayList<SchoolNotice> data;
    private Context context;
    public SchoolNoticeAdapter(Context context, int layoutResId, ArrayList<SchoolNotice> data) {
        super(context, layoutResId, data);
        this.data=data;
        this.context=context;
    }

    @Override
    protected void convert(BaseViewHolder holder, final SchoolNotice item) {
        RelativeLayout container= (RelativeLayout) holder.getView().findViewById(R.id.school_notice_notice_container);
        ImageView choice= (ImageView) holder.getView().findViewById(R.id.school_notice_notice_choice);
        TextView name= (TextView) holder.getView().findViewById(R.id.school_notice_notice_name);
        TextView time= (TextView) holder.getView().findViewById(R.id.school_notice_notice_time);
        TextView object= (TextView) holder.getView().findViewById(R.id.school_notice_notice_accept);
        TextView content= (TextView) holder.getView().findViewById(R.id.school_notice_notice_content);
        RelativeLayout bg= (RelativeLayout) holder.getView().findViewById(R.id.school_notice_notice_bg);
        TextView status= (TextView) holder.getView().findViewById(R.id.school_notice_notice_status);
        //
        name.setText(item.getSender_id());
        time.setText("发送时间："+item.getSend_time());
        object.setText("接收对象："+item.getReceiver_name());
        content.setText(item.getPhone_summary());
        if(item.getStatus().equals("发送失败")){
            bg.setBackgroundColor(ContextCompat.getColor(context,R.color.Not_read_color));
        }else if(item.getStatus().equals("未读")){
            bg.setBackgroundColor(ContextCompat.getColor(context,R.color.swipe_color2));
        }else if(item.getStatus().equals("部分已读")){
            bg.setBackgroundColor(ContextCompat.getColor(context,R.color.text_background_color_aqua));
        }else {
            bg.setBackgroundColor(ContextCompat.getColor(context,R.color.colorPrimary));
        }
        if(item.isShow()){
            container.setVisibility(View.VISIBLE);
        }else {
            container.setVisibility(View.GONE);
        }
        status.setText(item.getStatus());
        if(item.isClick()){
            choice.setImageResource(R.mipmap.student_test_choice1);
        }else {
            choice.setImageResource(R.mipmap.student_test_choice);
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
                }else {
                    Intent detail=new Intent(context, School_NoticeDetail.class);
                    detail.putExtra("Id",item.getId());
                    detail.putExtra("who","School");
                    context.startActivity(detail);
                }
                notifyDataSetChanged();
            }
        });


    }
}
