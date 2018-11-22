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
import com.kangzhan.student.CompayManage.Bean.CompayMsg;
import com.kangzhan.student.CompayManage.Notice.CompayMsgActivity;
import com.kangzhan.student.R;
import com.kangzhan.student.School.Notice.School_Notice_MsgDetail;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kangzhan011 on 2017/8/1.
 */

public class CompayMsgAdapter extends BaseRecyclerAdapter<CompayMsg>{
    private Context context;
    private ArrayList<CompayMsg> data;
    public CompayMsgAdapter(Context context, int layoutResId, ArrayList<CompayMsg> data) {
        super(context, layoutResId, data);
        this.context=context;
        this.data=data;
    }

    @Override
    protected void convert(BaseViewHolder holder, final CompayMsg item) {
        RelativeLayout container= (RelativeLayout) holder.getView().findViewById(R.id.school_msg_container);
        ImageView choice= (ImageView) holder.getView().findViewById(R.id.school_msg_choice);
        TextView name= (TextView) holder.getView().findViewById(R.id.school_msg_name);
        TextView time= (TextView) holder.getView().findViewById(R.id.school_msg_time);
        TextView phone= (TextView) holder.getView().findViewById(R.id.school_msg_phone);
        TextView content= (TextView) holder.getView().findViewById(R.id.school_msg_content);
        RelativeLayout bg= (RelativeLayout) holder.getView().findViewById(R.id.school_msg_bg);
        TextView status= (TextView) holder.getView().findViewById(R.id.school_msg_status);
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
        name.setText(item.getSender_name());
        time.setText("发送时间："+item.getSend_time());
        phone.setText("接收电话："+item.getReceiver_phone());
        content.setText(item.getContent());
        status.setText(item.getSms_status());
        if(item.getSms_status().equals("已发送")){
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
                    //查看详情
                    Intent detail=new Intent(context, School_Notice_MsgDetail.class);
                    detail.putExtra("Id",item.getId());
                    detail.putExtra("who","compay");
                    context.startActivity(detail);
                }
            }
        });

    }
}
