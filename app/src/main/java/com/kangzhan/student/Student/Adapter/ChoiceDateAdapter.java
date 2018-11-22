package com.kangzhan.student.Student.Adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kangzhan.student.R;
import com.kangzhan.student.Student.bean.ChoiceDate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kangzhan011 on 2017/5/5.
 */

public class ChoiceDateAdapter extends RecyclerView.Adapter<ChoiceDateAdapter.mViewHolder>{
    private Context context;
    private ArrayList<ChoiceDate> data;
    private OnItemClick onItemClick;
    private ArrayList<Boolean> isClick;
    private Activity activity;
    public ChoiceDateAdapter(Context context, ArrayList<ChoiceDate> data, Activity activity) {
        isClick=new ArrayList<>();
        this.activity=activity;
        for (int i = 0; i <data.size(); i++) {
            isClick.add(false);
        }
        this.context=context;
        this.data=data;

    }

    @Override
    public mViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(context).inflate(R.layout.item_list_choice_date_adapter,parent,false);
        DisplayMetrics display=new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(display);
        int width=(display.widthPixels)/7;
        RecyclerView.LayoutParams lp= (RecyclerView.LayoutParams) view.getLayoutParams();
        lp.width=width;
        view.setLayoutParams(lp);
        mViewHolder holder=new mViewHolder(view);
        return holder;
    }

    public void setOnItemClick(OnItemClick onItemClick){
        this.onItemClick=onItemClick;
    }
    @Override
    public void onBindViewHolder(final mViewHolder holder, final int position) {
        holder.week.setText(data.get(position).getWeek());
        holder.month.setText(data.get(position).getMonth()+"月");
        holder.day.setText(data.get(position).getDay());
        if(isClick.get(position)){
            //点击设置颜色
            holder.item.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
            holder.day.setTextColor(context.getResources().getColor(R.color.textColor_whit));
            holder.lines.setBackgroundColor(context.getResources().getColor(R.color.textColor_whit));
            holder.month.setTextColor(context.getResources().getColor(R.color.textColor_whit));
            holder.week.setTextColor(context.getResources().getColor(R.color.textColor_whit));
        }else {
            holder.item.setBackgroundColor(context.getResources().getColor(R.color.textColor_whit));
            holder.lines.setBackgroundColor(context.getResources().getColor(R.color.light_gray));
            holder.day.setTextColor(context.getResources().getColor(R.color.texColor_Text));
            holder.week.setTextColor(context.getResources().getColor(R.color.texColor_Text));
            holder.month.setTextColor(context.getResources().getColor(R.color.texColor_Text));
        }

        if(onItemClick!=null){
            holder.item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int mpostion=holder.getLayoutPosition();
                    onItemClick.onItemClick(data.get(position),mpostion);
                    for (int i = 0; i <data.size(); i++) {
                        isClick.set(i,false);
                    }
                    isClick.set(position,true);
                    notifyDataSetChanged();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    class mViewHolder extends RecyclerView.ViewHolder{
        TextView week;
        TextView month;
        TextView day;
        View lines;
        View item;
        public mViewHolder(View view) {
            super(view);
            item=view;
            week= (TextView) view.findViewById(R.id.chocie_week);
            month= (TextView) view.findViewById(R.id.choice_month);
            day= (TextView) view.findViewById(R.id.choice_day);
            lines=view.findViewById(R.id.choice_lines);
        }

    }
    public interface OnItemClick{
        void onItemClick(ChoiceDate date,int postion);
    }

}
