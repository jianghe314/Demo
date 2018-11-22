package com.kangzhan.student.School.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.holenzhou.pullrecyclerview.BaseRecyclerAdapter;
import com.holenzhou.pullrecyclerview.BaseViewHolder;
import com.kangzhan.student.R;
import com.kangzhan.student.School.Bean.LessonManage;
import com.kangzhan.student.School.Lesson.LessonManageDetailAcitivity;

import java.util.ArrayList;

/**
 * Created by kangzhan011 on 2017/7/4.
 */

public class LessonManageAdapter extends BaseRecyclerAdapter<LessonManage> {
    private ArrayList<LessonManage> data;
    private Context context;
    public LessonManageAdapter(Context context, int layoutResId, ArrayList<LessonManage> data) {
        super(context, layoutResId, data);
        this.data=data;
        this.context=context;
    }

    @Override
    protected void convert(BaseViewHolder holder, final LessonManage item) {
        //阶段确参数
        TextView name= (TextView) holder.getView().findViewById(R.id.item_school_lesson_name);
        TextView stage= (TextView) holder.getView().findViewById(R.id.item_school_lesson_stage);
        TextView status= (TextView) holder.getView().findViewById(R.id.item_school_lesson_stastu);
        TextView num= (TextView) holder.getView().findViewById(R.id.item_school_lesson_num);
        //
        name.setText(item.getName());
        if(item.getStatus().equals("有效")){
            status.setTextColor(ContextCompat.getColor(context,R.color.colorPrimary));
        }else {
            status.setTextColor(ContextCompat.getColor(context,R.color.swipe_color1));
        }
        stage.setText(item.getTraintype()+item.getStage());
        num.setText(item.getMax_stu());
        status.setText(item.getStatus());
        //
        holder.getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent detail=new Intent(context, LessonManageDetailAcitivity.class);
                detail.putExtra("LessonId",item.getId());
                context.startActivity(detail);
            }
        });

    }
}
