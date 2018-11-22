package com.kangzhan.student.School.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.holenzhou.pullrecyclerview.BaseRecyclerAdapter;
import com.holenzhou.pullrecyclerview.BaseViewHolder;
import com.kangzhan.student.R;
import com.kangzhan.student.School.Bean.EduTeaManage;
import com.kangzhan.student.School.Edu.School_TeacherDetail;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kangzhan011 on 2017/6/29.
 */

public class EduTeaMangeAdapter extends BaseRecyclerAdapter<EduTeaManage> {
    private ArrayList<EduTeaManage> data;
    private Context context;
    public EduTeaMangeAdapter(Context context, int layoutResId, ArrayList<EduTeaManage> data) {
        super(context, layoutResId, data);
        this.data=data;
        this.context=context;
    }

    @Override
    protected void convert(BaseViewHolder holder, final EduTeaManage item) {
        RelativeLayout container= (RelativeLayout) holder.getView().findViewById(R.id.school_edu_teaM_container);
        ImageView choice= (ImageView) holder.getView().findViewById(R.id.school_edu_teaM_choice);
        ImageView sex= (ImageView) holder.getView().findViewById(R.id.school_edu_teaM_sex);
        TextView name= (TextView) holder.getView().findViewById(R.id.school_edu_teaM_name);
        TextView isSure= (TextView) holder.getView().findViewById(R.id.school_edu_teaM_carType);
        TextView joinTime= (TextView) holder.getView().findViewById(R.id.school_edu_teaM_joinT);
        TextView carType= (TextView) holder.getView().findViewById(R.id.school_edu_teaM_car);
        TextView carLable= (TextView) holder.getView().findViewById(R.id.school_edu_teaM_carLabel);
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
        isSure.setText("挂靠："+item.getAttach_name());
        joinTime.setText("加盟时间"+item.getHiredate());
        carType.setText("车型："+item.getTeachpermitted());
        carLable.setText("教练车："+item.getLicnum());
        holder.getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //查看教练详情
                if(item.isShow()){
                    if(item.isClick()){
                        item.setClick(false);
                    }else {
                        item.setClick(true);
                    }
                    notifyDataSetChanged();
                }else {
                    Intent detail=new Intent(context, School_TeacherDetail.class);
                    detail.putExtra("teacherId",item.getId());
                    detail.putExtra("who","school");
                    context.startActivity(detail);
                }
            }
        });

    }
}
