package com.kangzhan.student.Student.Adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.holenzhou.pullrecyclerview.BaseRecyclerAdapter;
import com.holenzhou.pullrecyclerview.BaseViewHolder;
import com.kangzhan.student.R;
import com.kangzhan.student.Student.bean.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kangzhan011 on 2017/4/21.
 */

public class TestAdapter extends BaseRecyclerAdapter<Test> {
    private Context context;
    private ArrayList<Test> data;

    public TestAdapter(Context context, int layoutResId, ArrayList<Test> data) {
        super(context, layoutResId, data);
        this.context=context;
        this.data=data;
    }

    @Override
    protected void convert(BaseViewHolder holder, Test item) {
        RelativeLayout r= (RelativeLayout) holder.getView().findViewById(R.id.student_test_choice_r);
        ImageView choice= (ImageView) holder.getView().findViewById(R.id.exam_iv);
        TextView lesson= (TextView) holder.getView().findViewById(R.id.student_test_lesson);
        TextView time= (TextView) holder.getView().findViewById(R.id.student_test_time);
        TextView grade= (TextView) holder.getView().findViewById(R.id.student_test_grade);
        //
        if(holder.getAdapterPosition()%3==0){
            choice.setBackgroundResource(R.drawable.circle1);
        }else if(holder.getAdapterPosition()%3==1){
            choice.setBackgroundResource(R.drawable.circle2);
        }else {
            choice.setBackgroundResource(R.drawable.circle3);
        }
        lesson.setText(item.getStage());
        time.setText(item.getStart_time());
        if(Integer.valueOf(item.getScore())>89){
            grade.setText("合格"+"("+item.getScore()+")");
        }else {
            grade.setText("不合格"+"("+item.getScore()+")");
        }



    }
}
