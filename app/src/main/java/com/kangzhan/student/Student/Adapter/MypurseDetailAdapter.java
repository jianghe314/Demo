package com.kangzhan.student.Student.Adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;
import android.widget.TextView;

import com.holenzhou.pullrecyclerview.BaseRecyclerAdapter;
import com.holenzhou.pullrecyclerview.BaseViewHolder;
import com.kangzhan.student.R;
import com.kangzhan.student.Student.bean.PurseDetail;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kangzhan011 on 2017/4/20.
 */

public class MypurseDetailAdapter extends BaseRecyclerAdapter<PurseDetail>{
    private Context context;
    private ArrayList<PurseDetail> data;
    public MypurseDetailAdapter(Context context, int layoutResId, ArrayList<PurseDetail> data) {
        super(context, layoutResId, data);
        this.context=context;
        this.data=data;
    }

    @Override
    protected void convert(BaseViewHolder holder, PurseDetail item) {
        ImageView type_iv= (ImageView) holder.getView().findViewById(R.id.student_purseDetail_type);
        TextView cost_Type= (TextView) holder.getView().findViewById(R.id.student_purse_detail_type);
        TextView cost_Num= (TextView) holder.getView().findViewById(R.id.student_purse_detail_Num);
        TextView time= (TextView) holder.getView().findViewById(R.id.student_purse_detail_time);
        //
        if(item.getBusi_type().equals("充值")){
            type_iv.setImageResource(R.mipmap.purse_detail_spend);
            cost_Num.setTextColor(ContextCompat.getColor(context,R.color.textColor_yellow));
        }else {
            type_iv.setImageResource(R.mipmap.pursedetail_cost);
            cost_Num.setTextColor(ContextCompat.getColor(context,R.color.textColor_black));
        }
        cost_Type.setText(item.getDescription());
        cost_Num.setText(item.getAmount());
        time.setText(item.getBusi_date());
    }
}
