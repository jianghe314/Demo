package com.kangzhan.student.Student.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.holenzhou.pullrecyclerview.BaseRecyclerAdapter;
import com.holenzhou.pullrecyclerview.BaseViewHolder;
import com.kangzhan.student.R;
import com.kangzhan.student.Student.bean.Exam;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kangzhan011 on 2017/3/30.
 */

public class ExamAdapter extends BaseRecyclerAdapter<Exam> {
    private Context context;
    private ArrayList<Exam> data;

    public ExamAdapter(Context context, int layoutResId, ArrayList<Exam> data) {
        super(context, layoutResId, data);
        this.context=context;
        this.data=data;
    }

    @Override
    protected void convert(BaseViewHolder holder, Exam item) {
        ImageView circle= (ImageView) holder.getView().findViewById(R.id.exam_iv);
        TextView lesson= (TextView) holder.getView().findViewById(R.id.exam_lessson);
        TextView grade= (TextView) holder.getView().findViewById(R.id.exam_grade);
        TextView time= (TextView) holder.getView().findViewById(R.id.exam_time);
        //
        lesson.setText(item.getStage());
        grade.setText(item.getQualified());
        time.setText(item.getExam_date());
    }
}
