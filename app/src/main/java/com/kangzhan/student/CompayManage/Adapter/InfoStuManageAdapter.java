package com.kangzhan.student.CompayManage.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.holenzhou.pullrecyclerview.BaseRecyclerAdapter;
import com.holenzhou.pullrecyclerview.BaseViewHolder;
import com.kangzhan.student.CompayManage.Bean.InfoStuManage;
import com.kangzhan.student.R;
import com.kangzhan.student.School.Edu.School_StudentDetail;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kangzhan011 on 2017/7/27.
 */

public class InfoStuManageAdapter extends BaseRecyclerAdapter<InfoStuManage> {
    private Context context;
    private ArrayList<InfoStuManage> data;
    public InfoStuManageAdapter(Context context, int layoutResId, ArrayList<InfoStuManage> data) {
        super(context, layoutResId, data);
        this.context=context;
        this.data=data;
    }

    @Override
    protected void convert(BaseViewHolder holder, final InfoStuManage item) {
        RelativeLayout container= (RelativeLayout) holder.getView().findViewById(R.id.compay_info_stuM_container);
        ImageView choice= (ImageView) holder.getView().findViewById(R.id.compay_info_stuM_choice);
        TextView name= (TextView) holder.getView().findViewById(R.id.compay_info_stuM_name);
        ImageView sex= (ImageView) holder.getView().findViewById(R.id.compay_info_stuM_sex);
        TextView subject= (TextView) holder.getView().findViewById(R.id.compay_info_stuM_subject);
        TextView time= (TextView) holder.getView().findViewById(R.id.compay_info_stuM_time);
        TextView carType= (TextView) holder.getView().findViewById(R.id.compay_info_stuM_carType);
        TextView phone= (TextView) holder.getView().findViewById(R.id.compay_info_stuM_phone);
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
        if(item.getSex().equals("男")){
            sex.setImageResource(R.mipmap.boy);
        }else {
            sex.setImageResource(R.mipmap.girl);
        }
        name.setText(item.getName());
        subject.setText("状态："+item.getStatus_str());
        time.setText("添加时间："+item.getApplydate());
        carType.setText("车型："+item.getTraintype());
        phone.setText("电话："+item.getPhone());
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
                    Intent detail=new Intent(context, School_StudentDetail.class);
                    detail.putExtra("Id",item.getId());
                    detail.putExtra("who","compay");
                    context.startActivity(detail);
                }
            }
        });
    }
}
