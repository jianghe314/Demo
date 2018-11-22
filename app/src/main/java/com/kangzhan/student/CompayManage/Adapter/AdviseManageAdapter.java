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
import com.kangzhan.student.CompayManage.Bean.AdviseManage;
import com.kangzhan.student.CompayManage.Bean.TeaFollowUp;
import com.kangzhan.student.CompayManage.SelfRegistManage.AdviseDetailActivity;
import com.kangzhan.student.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kangzhan011 on 2017/7/25.
 */

public class AdviseManageAdapter extends BaseRecyclerAdapter<AdviseManage> {
    private ArrayList<AdviseManage> data;
    private Context context;
    public AdviseManageAdapter(Context context, int layoutResId, ArrayList<AdviseManage> data) {
        super(context, layoutResId, data);
        this.context=context;
        this.data=data;
    }

    @Override
    protected void convert(BaseViewHolder holder, final AdviseManage item) {
        RelativeLayout container= (RelativeLayout) holder.getView().findViewById(R.id.compay_self_regist_advise_container);
        ImageView choice= (ImageView) holder.getView().findViewById(R.id.compay_self_regist_advise_choice);
        TextView school= (TextView) holder.getView().findViewById(R.id.compay_self_regist_advise_school);
        TextView name= (TextView) holder.getView().findViewById(R.id.compay_self_regist_advise_name);
        TextView time= (TextView) holder.getView().findViewById(R.id.compay_self_regist_advise_time);
        TextView content= (TextView) holder.getView().findViewById(R.id.compay_self_regist_advise_content);
        RelativeLayout bg= (RelativeLayout) holder.getView().findViewById(R.id.compay_self_regist_advise_bg);
        TextView status= (TextView) holder.getView().findViewById(R.id.compay_self_regist_advise_status);
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
        school.setText(item.getInst_name());
        name.setText(item.getSender_name());
        time.setText("时间："+item.getCreate_time());
        content.setText(item.getDescription());
        status.setText(item.getStatus_name());
        if(item.getStatus_name()!=null){
            if(item.getStatus_name().equals("已处理")){
                bg.setBackgroundColor(ContextCompat.getColor(context,R.color.colorPrimary));
            }else {
                bg.setBackgroundColor(ContextCompat.getColor(context,R.color.textColor_orange));
            }
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
                    Intent detail=new Intent(context, AdviseDetailActivity.class);
                    detail.putExtra("status",item.getStatus_name());
                    detail.putExtra("Id",item.getId());
                    context.startActivity(detail);
                }
            }
        });
    }
}
