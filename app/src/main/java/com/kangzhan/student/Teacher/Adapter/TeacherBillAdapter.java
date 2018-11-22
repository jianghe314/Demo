package com.kangzhan.student.Teacher.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.holenzhou.pullrecyclerview.BaseRecyclerAdapter;
import com.holenzhou.pullrecyclerview.BaseViewHolder;
import com.kangzhan.student.R;
import com.kangzhan.student.Teacher.bean.TeacherBill;
import com.kangzhan.student.Teacher.person_data.Teacher_bill_detail;

import java.util.ArrayList;

/**
 * Created by kangzhan011 on 2017/4/11.
 */

public class TeacherBillAdapter extends BaseRecyclerAdapter<TeacherBill> {

    private Context context;
    private ArrayList<TeacherBill> data;
    public TeacherBillAdapter(Context context, int layoutResId, ArrayList<TeacherBill> data) {
        super(context, layoutResId, data);
        this.context=context;
        this.data=data;
    }

    @Override
    protected void convert(BaseViewHolder holder, final TeacherBill item) {
        TextView time= (TextView) holder.getView().findViewById(R.id.teacher_bill_year_month);
        TextView bill_Num= (TextView) holder.getView().findViewById(R.id.teacher_bill_Num);
        TextView cost= (TextView) holder.getView().findViewById(R.id.teacher_bill_cost);
        TextView bill_Money= (TextView) holder.getView().findViewById(R.id.teacher_bill_price);
        //
        time.setText(item.getTrain_date());
        bill_Num.setText(item.getTrain_length());
        cost.setText(item.getAmount());
        bill_Money.setText(item.getDraw());
        //
        holder.getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent detail=new Intent(context, Teacher_bill_detail.class);
                detail.putExtra("id",item.getId());
                context.startActivity(detail);
            }
        });
    }
}
