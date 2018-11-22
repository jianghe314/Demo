package com.kangzhan.student.CompayManage.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.holenzhou.pullrecyclerview.BaseRecyclerAdapter;
import com.holenzhou.pullrecyclerview.BaseViewHolder;
import com.kangzhan.student.CompayManage.Bean.GradeInquery;
import com.kangzhan.student.R;
import com.kangzhan.student.School.Edu.School_StudentDetail;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kangzhan011 on 2017/8/2.
 */

public class GradeInqueryAdapter extends BaseRecyclerAdapter<GradeInquery> {
    private Context context;
    private ArrayList<GradeInquery> data;
    public GradeInqueryAdapter(Context context, int layoutResId, ArrayList<GradeInquery> data) {
        super(context, layoutResId, data);
        this.context=context;
        this.data=data;
    }

    @Override
    protected void convert(BaseViewHolder holder, final GradeInquery item) {
        //ImageView choice= (ImageView) holder.getView().findViewById(R.id.item_compay_grade_inquery_choice);
        TextView name= (TextView) holder.getView().findViewById(R.id.item_compay_grade_inquery_name);
        TextView sub= (TextView) holder.getView().findViewById(R.id.item_compay_grade_inquery_subject);
        TextView score= (TextView) holder.getView().findViewById(R.id.item_compay_grade_inquery_score);
        TextView school= (TextView) holder.getView().findViewById(R.id.item_compay_grade_inquery_school);
        /*
        if(item.isClick()){
            choice.setImageResource(R.mipmap.student_test_choice1);
        }else {
            choice.setImageResource(R.mipmap.student_test_choice);
        }*/
        name.setText(item.getName());
        sub.setText(item.getQualified());
        score.setText(item.getQualified());
        school.setText(item.getInstname());
        holder.getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                if(item.isClick()){
                    item.setClick(false);
                }else {
                    item.setClick(true);
                }
                notifyDataSetChanged();
                */
                Intent detail=new Intent(context, School_StudentDetail.class);
                detail.putExtra("Id",item.getStu_id());
                detail.putExtra("who","compay");
                context.startActivity(detail);
            }
        });
    }
}
