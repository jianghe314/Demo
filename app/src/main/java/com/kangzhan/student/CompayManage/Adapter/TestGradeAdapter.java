package com.kangzhan.student.CompayManage.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.holenzhou.pullrecyclerview.BaseRecyclerAdapter;
import com.holenzhou.pullrecyclerview.BaseViewHolder;
import com.kangzhan.student.CompayManage.Bean.Testgrade;
import com.kangzhan.student.CompayManage.TeachingManage.GradeInqueryActivity;
import com.kangzhan.student.R;
import com.kangzhan.student.School.Edu.School_StudentDetail;
import com.kangzhan.student.Student.bean.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kangzhan011 on 2017/7/26.
 */

public class TestGradeAdapter extends BaseRecyclerAdapter<Testgrade> {
    private Context context;
    private ArrayList<Testgrade> data;
    public TestGradeAdapter(Context context, int layoutResId, ArrayList<Testgrade> data) {
        super(context, layoutResId, data);
        this.context=context;
        this.data=data;
    }

    @Override
    protected void convert(BaseViewHolder holder, final Testgrade item) {
        RelativeLayout container= (RelativeLayout) holder.getView().findViewById(R.id.item_compay_teaching_test_container);
        ImageView choice= (ImageView) holder.getView().findViewById(R.id.item_compay_teaching_test_choice);
        ImageView sex= (ImageView) holder.getView().findViewById(R.id.compay_teaching_test_sex);
        TextView name= (TextView) holder.getView().findViewById(R.id.compay_teaching_test_name);
        TextView subject= (TextView) holder.getView().findViewById(R.id.compay_teaching_test_subject);
        TextView school= (TextView) holder.getView().findViewById(R.id.compay_teaching_test_school);
        TextView time= (TextView) holder.getView().findViewById(R.id.compay_teaching_test_time);
        TextView score= (TextView) holder.getView().findViewById(R.id.compay_teaching_test_grade);
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
        if(item.getSex().equals("女")){
            sex.setImageResource(R.mipmap.girl);
        }else {
            sex.setImageResource(R.mipmap.boy);
        }
        name.setText(item.getName());
        subject.setText("科目："+item.getStage());
        school.setText(item.getInstname());
        time.setText("时间："+item.getEnd_time()+" "+item.getStart_time());
        score.setText(item.getQualified());
        holder.getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (item.isShow()) {
                    if(item.isClick()){
                        item.setClick(false);
                    }else {
                        item.setClick(true);
                    }
                    notifyDataSetChanged();
                }else {
                    Intent detail=new Intent(context, School_StudentDetail.class);
                    detail.putExtra("Id",item.getStu_id());
                    detail.putExtra("who","compay");
                    context.startActivity(detail);
                }
            }
        });

    }
}
