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
import com.kangzhan.student.CompayManage.Bean.CompayNotice;
import com.kangzhan.student.R;
import com.kangzhan.student.School.Notice.School_NoticeDetail;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kangzhan011 on 2017/7/31.
 */

public class CompayNoticeAdapter extends BaseRecyclerAdapter<CompayNotice> {
    private Context context;
    private ArrayList<CompayNotice> data;
    public CompayNoticeAdapter(Context context, int layoutResId, ArrayList<CompayNotice> data) {
        super(context, layoutResId, data);
        this.context=context;
        this.data=data;
    }

    @Override
    protected void convert(BaseViewHolder holder, final CompayNotice item) {
        RelativeLayout container= (RelativeLayout) holder.getView().findViewById(R.id.school_notice_notice_container);
        ImageView choice= (ImageView) holder.getView().findViewById(R.id.school_notice_notice_choice);
        TextView name= (TextView) holder.getView().findViewById(R.id.school_notice_notice_name);
        TextView time= (TextView) holder.getView().findViewById(R.id.school_notice_notice_time);
        TextView object= (TextView) holder.getView().findViewById(R.id.school_notice_notice_accept);
        TextView content= (TextView) holder.getView().findViewById(R.id.school_notice_notice_content);
        RelativeLayout bg= (RelativeLayout) holder.getView().findViewById(R.id.school_notice_notice_bg);
        TextView status= (TextView) holder.getView().findViewById(R.id.school_notice_notice_status);
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
        name.setText(item.getSender_name());
        time.setText("发送时间："+item.getSend_time());
        object.setText("接收对象："+item.getReceiver_name());
        content.setText(item.getContent());
        status.setText(item.getPush_status());
        if(item.getPush_status().equals("发送失败")){
            bg.setBackgroundColor(ContextCompat.getColor(context,R.color.textColor_orange));
        }else {
            bg.setBackgroundColor(ContextCompat.getColor(context,R.color.colorPrimary));
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
                    //查看详情
                    Intent detail=new Intent(context, School_NoticeDetail.class);
                    detail.putExtra("Id",item.getId());
                    detail.putExtra("who","Compay");
                    context.startActivity(detail);
                }
            }
        });
    }
}
