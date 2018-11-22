package com.kangzhan.student.School.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.holenzhou.pullrecyclerview.BaseRecyclerAdapter;
import com.holenzhou.pullrecyclerview.BaseViewHolder;
import com.kangzhan.student.R;
import com.kangzhan.student.School.Bean.EduRemark;
import com.kangzhan.student.School.Edu.School_StudentDetail;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kangzhan011 on 2017/7/4.
 */

public class EduReamrkAdapter extends BaseRecyclerAdapter<EduRemark> {
    private ArrayList<EduRemark> data;
    private Context context;
    public EduReamrkAdapter(Context context, int layoutResId, ArrayList<EduRemark> data) {
        super(context, layoutResId, data);
        this.context=context;
        this.data=data;
    }

    @Override
    protected void convert(BaseViewHolder holder, final EduRemark item) {
        ImageView choice= (ImageView) holder.getView().findViewById(R.id.item_school_edu_remark_choice);
        TextView tName= (TextView) holder.getView().findViewById(R.id.item_school_edu_remark_tName);
        TextView sName= (TextView) holder.getView().findViewById(R.id.item_school_edu_remark_sName);
        TextView time= (TextView) holder.getView().findViewById(R.id.item_school_edu_remark_time);
        TextView subject= (TextView) holder.getView().findViewById(R.id.item_school_edu_remark_subject);
        TextView content= (TextView) holder.getView().findViewById(R.id.item_school_edu_remark_content);
        //
        if(item.isClick()){
            choice.setImageResource(R.mipmap.student_test_choice1);
        }else {
            choice.setImageResource(R.mipmap.student_test_choice);
        }
        tName.setText("教练姓名："+item.getCoaname());
        sName.setText("学员姓名："+item.getStuname());
        time.setText("课程："+item.getCreate_time());
        subject.setText(item.getSubject_content());
        content.setText(item.getContent());
        holder.getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent detail=new Intent(context,School_StudentDetail.class);
                detail.putExtra("who","school");
                detail.putExtra("Id",item.getStu_id());
                context.startActivity(detail);
            }
        });
    }
}
