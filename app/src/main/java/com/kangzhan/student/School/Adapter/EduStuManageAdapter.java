package com.kangzhan.student.School.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.holenzhou.pullrecyclerview.BaseRecyclerAdapter;
import com.holenzhou.pullrecyclerview.BaseViewHolder;
import com.kangzhan.student.R;
import com.kangzhan.student.School.Bean.EduStuMange;
import com.kangzhan.student.School.Edu.School_StudentDetail;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kangzhan011 on 2017/6/21.
 */

public class EduStuManageAdapter extends BaseRecyclerAdapter<EduStuMange> {
    private Context context;
    private ArrayList<EduStuMange> data;

    public EduStuManageAdapter(Context context, int layoutResId, ArrayList<EduStuMange> data) {
        super(context, layoutResId, data);
        this.context=context;
        this.data=data;
    }

    @Override
    protected void convert(BaseViewHolder holder, final EduStuMange item) {
        RelativeLayout container= (RelativeLayout) holder.getView().findViewById(R.id.item_school_edu_stuM_rel);
        ImageView choice= (ImageView) holder.getView().findViewById(R.id.item_school_edu_stuM_choice);
        ImageView sex= (ImageView) holder.getView().findViewById(R.id.item_school_edu_stuM_sex);
        TextView name= (TextView) holder.getView().findViewById(R.id.item_school_edu_stuM_name);
        TextView time= (TextView) holder.getView().findViewById(R.id.item_school_edu_stuM_startT);
        TextView id= (TextView) holder.getView().findViewById(R.id.item_school_edu_stuM_id);
        //
        if(item.isShow()){
            container.setVisibility(View.VISIBLE);
        }else {
            container.setVisibility(View.GONE);
        }
        if(item.getSex().equals("女")){
            sex.setImageResource(R.mipmap.girl);
        }else {
            sex.setImageResource(R.mipmap.boy);
        }
        if(item.isClick()){
            //如果是true表示已经选取，则取消
            choice.setBackgroundResource(R.mipmap.student_test_choice1);
        }else {
            //则选择
            choice.setBackgroundResource(R.mipmap.student_test_choice);
        }
        name.setText(item.getName());
        time.setText(item.getApplydate());
        id.setText(item.getIdcard());
        //
        //查看学员详情
        holder.getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(item.isShow()){
                    if(item.isClick()){
                        //如果是true表示已经选取，则取消
                        item.setClick(false);
                    }else {
                        //则选择
                        item.setClick(true);
                    }
                    notifyDataSetChanged();
                }else {
                    Intent detail=new Intent(context, School_StudentDetail.class);
                    detail.putExtra("Id",item.getId());
                    detail.putExtra("who","school");
                    context.startActivity(detail);
                }
            }
        });


    }
}
