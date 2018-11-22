package com.kangzhan.student.CompayManage.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.holenzhou.pullrecyclerview.BaseRecyclerAdapter;
import com.holenzhou.pullrecyclerview.BaseViewHolder;
import com.kangzhan.student.CompayManage.Bean.InfoSchoolManage;
import com.kangzhan.student.CompayManage.InfoManage.SchoolDetailActivity;
import com.kangzhan.student.R;
import com.kangzhan.student.School.Edu.School_StudentDetail;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kangzhan011 on 2017/7/27.
 */

public class InfoSchoolManageAdapter extends BaseRecyclerAdapter<InfoSchoolManage> {
    private Context context;
    private ArrayList<InfoSchoolManage> data;
    public InfoSchoolManageAdapter(Context context, int layoutResId, ArrayList<InfoSchoolManage> data) {
        super(context, layoutResId, data);
        this.context=context;
        this.data=data;
    }

    @Override
    protected void convert(BaseViewHolder holder, final InfoSchoolManage item) {
        RelativeLayout container= (RelativeLayout) holder.getView().findViewById(R.id.compay_school_manage_container);
        ImageView choice= (ImageView) holder.getView().findViewById(R.id.compay_school_manage_choice);
        TextView school= (TextView) holder.getView().findViewById(R.id.compay_school_manage_school);
        TextView phone= (TextView) holder.getView().findViewById(R.id.compay_school_manage_phone);
        TextView time= (TextView) holder.getView().findViewById(R.id.compay_school_manage_time);
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
        school.setText(item.getInsti_name());
        phone.setText("电话："+item.getPhone());
        time.setText("加盟时间："+item.getCreate_time());
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
                    Intent detail=new Intent(context, SchoolDetailActivity.class);
                    detail.putExtra("Id",item.getInst_id());
                    context.startActivity(detail);
                }
            }
        });
    }
}
