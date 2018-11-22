package com.kangzhan.student.CompayManage.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.autonavi.amap.mapcore.interfaces.IText;
import com.holenzhou.pullrecyclerview.BaseRecyclerAdapter;
import com.holenzhou.pullrecyclerview.BaseViewHolder;
import com.kangzhan.student.CompayManage.Bean.InfoTeaManage;
import com.kangzhan.student.R;
import com.kangzhan.student.School.Edu.School_TeacherDetail;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kangzhan011 on 2017/7/27.
 */

public class InfoTeaManageAdapter extends BaseRecyclerAdapter<InfoTeaManage> {
    private Context context;
    private ArrayList<InfoTeaManage> data;
    public InfoTeaManageAdapter(Context context, int layoutResId, ArrayList<InfoTeaManage> data) {
        super(context, layoutResId, data);
        this.context=context;
        this.data=data;
    }

    @Override
    protected void convert(BaseViewHolder holder, final InfoTeaManage item) {
        RelativeLayout container= (RelativeLayout) holder.getView().findViewById(R.id.compay_teaManage_container);
        ImageView choice= (ImageView) holder.getView().findViewById(R.id.compay_teaManage_choice);
        ImageView sex= (ImageView) holder.getView().findViewById(R.id.compay_teaManage_sex);
        TextView name= (TextView) holder.getView().findViewById(R.id.compay_teaManage_name);
        TextView isStaff= (TextView) holder.getView().findViewById(R.id.compay_teaManage_isStaff);
        TextView status= (TextView) holder.getView().findViewById(R.id.compay_teaManage_status);
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
        isStaff.setText("是否挂靠："+item.getAttach_name());
        status.setText(item.getStatus());
        if(item.getStatus().equals("在职在岗")){
            status.setTextColor(ContextCompat.getColor(context,R.color.colorPrimary));
        }else {
            status.setTextColor(ContextCompat.getColor(context,R.color.textColor_orange));
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
                    Intent detail=new Intent(context, School_TeacherDetail.class);
                    detail.putExtra("teacherId",item.getId());
                    detail.putExtra("who","compay");
                    context.startActivity(detail);
                }
            }
        });

    }
}
