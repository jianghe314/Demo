package com.kangzhan.student.Student.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.holenzhou.pullrecyclerview.BaseRecyclerAdapter;
import com.holenzhou.pullrecyclerview.BaseViewHolder;
import com.kangzhan.student.R;
import com.kangzhan.student.Student.News.AdviseDetailActivity;
import com.kangzhan.student.Student.bean.Advise;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kangzhan011 on 2017/3/28.
 */

public class AdviseAdapter extends BaseRecyclerAdapter<Advise> {
    private Context context;
    private ArrayList<Advise> data;
    public AdviseAdapter(Context context, int layoutResId, ArrayList<Advise> data) {
        super(context, layoutResId, data);
        this.context=context;
        this.data=data;
    }

    @Override
    protected void convert(BaseViewHolder holder, final Advise item) {
        TextView date= (TextView) holder.getView().findViewById(R.id.advise_date);
        TextView problem= (TextView) holder.getView().findViewById(R.id.advise_problem);
        RelativeLayout r1= (RelativeLayout) holder.getView().findViewById(R.id.advise_list_r1);
        final TextView deal= (TextView) holder.getView().findViewById(R.id.advise_deal);
        //赋值数据
        date.setText(item.getCreate_time());
        problem.setText(item.getPhone_summary());
        deal.setText(item.getState());
        if(item.getState().equals("未处理")){
            r1.setBackgroundResource(R.color.Not_read_color);
        }else if(item.getState().equals("已处理")){
            r1.setBackgroundResource(R.color.colorPrimary);
        }
        deal.setText(item.getState());
        holder.getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent detail=new Intent(context, AdviseDetailActivity.class);
                detail.putExtra("id",item.getId());
                context.startActivity(detail);
            }
        });
    }
}
