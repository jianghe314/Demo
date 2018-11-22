package com.kangzhan.student.School.Adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.holenzhou.pullrecyclerview.BaseRecyclerAdapter;
import com.holenzhou.pullrecyclerview.BaseViewHolder;
import com.kangzhan.student.R;
import com.kangzhan.student.School.Bean.TrainingStu;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kangzhan011 on 2017/6/30.
 */

public class TrainingAdapter extends BaseRecyclerAdapter<TrainingStu> {
    private ArrayList<TrainingStu> data;
    private Context context;
    public TrainingAdapter(Context context, int layoutResId, ArrayList<TrainingStu> data) {
        super(context, layoutResId, data);
        this.data=data;
        this.context=context;
    }

    @Override
    protected void convert(BaseViewHolder holder, TrainingStu item) {
        ImageView sex= (ImageView) holder.getView().findViewById(R.id.school_edu_training_stu_sex);
        TextView name= (TextView) holder.getView().findViewById(R.id.school_edu_training_stu_name);
        TextView lesson= (TextView) holder.getView().findViewById(R.id.school_edu_training_stu_lesson);
        TextView phone= (TextView) holder.getView().findViewById(R.id.school_edu_training_stu_phone);
        //
        if(item.getSex().equals("男")){
            sex.setImageResource(R.mipmap.boy);
        }else {
            sex.setImageResource(R.mipmap.girl);
        }
        name.setText(item.getName());
        lesson.setText("状态："+item.getStatus_str());
        phone.setText("电话"+item.getPhone());
    }
}
