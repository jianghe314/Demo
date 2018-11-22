package com.kangzhan.student.Teacher.Adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;
import android.widget.TextView;

import com.holenzhou.pullrecyclerview.BaseRecyclerAdapter;
import com.holenzhou.pullrecyclerview.BaseViewHolder;
import com.kangzhan.student.R;
import com.kangzhan.student.Teacher.bean.purseDetail;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kangzhan011 on 2017/8/22.
 */

public class PurseDetailAdapter extends BaseRecyclerAdapter<purseDetail> {
    private Context context;
    private ArrayList<purseDetail> data;
    public PurseDetailAdapter(Context context, int layoutResId, ArrayList<purseDetail> data) {
        super(context, layoutResId, data);
        this.context=context;
        this.data=data;
    }

    @Override
    protected void convert(BaseViewHolder holder, purseDetail item) {
        ImageView iv= (ImageView) holder.getView().findViewById(R.id.student_purseDetail_type);
        TextView title= (TextView) holder.getView().findViewById(R.id.student_purse_detail_type);
        TextView amount= (TextView) holder.getView().findViewById(R.id.student_purse_detail_Num);
        TextView time= (TextView) holder.getView().findViewById(R.id.student_purse_detail_time);
        if(item.getDescription().equals("充值")){
            iv.setImageResource(R.mipmap.purse_detail_spend);
            amount.setTextColor(ContextCompat.getColor(context,R.color.textColor_yellow));
        }else {
            iv.setImageResource(R.mipmap.teacher_excharge);
            amount.setTextColor(ContextCompat.getColor(context,R.color.textColor_black));
        }
        amount.setText(item.getAmount());
        title.setText(item.getDescription());
        time.setText(item.getBusi_date());
    }
}
