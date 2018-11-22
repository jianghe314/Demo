package com.kangzhan.student.CompayManage.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.holenzhou.pullrecyclerview.BaseRecyclerAdapter;
import com.holenzhou.pullrecyclerview.BaseViewHolder;
import com.kangzhan.student.CompayManage.AccountManage.MessageCountDetailActivity;
import com.kangzhan.student.CompayManage.AccountManage.MessageDetailActivity;
import com.kangzhan.student.CompayManage.Bean.SchoolMessageList;
import com.kangzhan.student.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kangzhan011 on 2017/7/31.
 */

public class SchoolMesageAdapter extends BaseRecyclerAdapter<SchoolMessageList> {
    private ArrayList<SchoolMessageList> data;
    private Context context;
    public SchoolMesageAdapter(Context context, int layoutResId, ArrayList<SchoolMessageList> data) {
        super(context, layoutResId, data);
        this.context=context;
        this.data=data;
    }

    @Override
    protected void convert(BaseViewHolder holder, final SchoolMessageList item) {
        RelativeLayout container= (RelativeLayout) holder.getView().findViewById(R.id.compay_msg_detail_container);
        ImageView choice= (ImageView) holder.getView().findViewById(R.id.compay_msg_detail_choice);
        TextView school= (TextView) holder.getView().findViewById(R.id.compay_msg_detail_school);
        TextView num= (TextView) holder.getView().findViewById(R.id.compay_msg_detail_num);
        TextView cost= (TextView) holder.getView().findViewById(R.id.compay_msg_detail_cost);
        TextView status= (TextView) holder.getView().findViewById(R.id.compay_msg_detail_status);
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
        school.setText(item.getInsti_name());
        num.setText(item.getSend_counts());
        cost.setText(item.getAmount());
        status.setText(item.getStatus());
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
                    Intent detail=new Intent(context, MessageCountDetailActivity.class);
                    detail.putExtra("Id",item.getInst_id());
                    context.startActivity(detail);
                }
            }
        });
    }
}
