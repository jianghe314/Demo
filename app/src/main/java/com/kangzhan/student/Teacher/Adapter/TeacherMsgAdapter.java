package com.kangzhan.student.Teacher.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.holenzhou.pullrecyclerview.BaseRecyclerAdapter;
import com.holenzhou.pullrecyclerview.BaseViewHolder;
import com.kangzhan.student.R;
import com.kangzhan.student.Teacher.bean.TeacherMsg;
import com.kangzhan.student.Teacher.person_data.Teacher_Msg_detail;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kangzhan011 on 2017/4/11.
 */

public class TeacherMsgAdapter extends BaseRecyclerAdapter<TeacherMsg> {
    private Context context;
    private ArrayList<TeacherMsg> data;
    public TeacherMsgAdapter(Context context, int layoutResId, ArrayList<TeacherMsg> data) {
        super(context, layoutResId, data);
        this.context=context;
        this.data=data;
    }

    @Override
    protected void convert(BaseViewHolder holder, final TeacherMsg item) {
        TextView time= (TextView) holder.getView().findViewById(R.id.teacher_msg_year_month);
        TextView Msg_Num= (TextView) holder.getView().findViewById(R.id.teacher_msg_Num);
        TextView cost_Sum= (TextView) holder.getView().findViewById(R.id.teacher_msg_total);
        //
        time.setText(item.getYear()+"-"+item.getMonth());
        Msg_Num.setText(item.getSend_counts());
        cost_Sum.setText(item.getAmount());
        //
        holder.getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent detail=new Intent(context, Teacher_Msg_detail.class);
                detail.putExtra("id",item.getId());
                context.startActivity(detail);
            }
        });
    }
}
