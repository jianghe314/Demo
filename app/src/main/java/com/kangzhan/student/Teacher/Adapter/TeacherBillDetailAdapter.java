package com.kangzhan.student.Teacher.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.holenzhou.pullrecyclerview.BaseRecyclerAdapter;
import com.holenzhou.pullrecyclerview.BaseViewHolder;
import com.kangzhan.student.R;
import com.kangzhan.student.Teacher.bean.TeacherBillDetail;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kangzhan011 on 2017/4/11.
 */

public class TeacherBillDetailAdapter extends BaseRecyclerAdapter<TeacherBillDetail> {

    private Context context;
    private ArrayList<TeacherBillDetail> data;
    public TeacherBillDetailAdapter(Context context, int layoutResId, ArrayList<TeacherBillDetail> data) {
        super(context, layoutResId, data);
        this.context=context;
        this.data=data;
    }

    @Override
    protected void convert(BaseViewHolder holder, TeacherBillDetail item) {
        TextView time= (TextView) holder.getView().findViewById(R.id.bill_detail_date);
        TextView carLabel= (TextView) holder.getView().findViewById(R.id.bill_detail_car_label);
        TextView content= (TextView) holder.getView().findViewById(R.id.bill_detail_content);
        TextView name= (TextView) holder.getView().findViewById(R.id.bill_detail_name);
        TextView hour= (TextView) holder.getView().findViewById(R.id.bill_detail_hour);
        //
        time.setText(item.getTime());
        carLabel.setText(item.getLicnum());
        content.setText(item.getStage());
        name.setText(item.getStudent_name());
        hour.setText(item.getAmount());
    }
}
