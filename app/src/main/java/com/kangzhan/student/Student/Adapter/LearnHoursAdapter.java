package com.kangzhan.student.Student.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.holenzhou.pullrecyclerview.BaseRecyclerAdapter;
import com.holenzhou.pullrecyclerview.BaseViewHolder;
import com.kangzhan.student.R;
import com.kangzhan.student.Student.Train.EvalutionActivity;
import com.kangzhan.student.Student.Train.PayActivity;
import com.kangzhan.student.Student.bean.LearnHours;
import com.kangzhan.student.Student.person_data_activity.BookingPayActivity;
import com.kangzhan.student.Student.person_data_activity.HourEvaluActivity;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by kangzhan011 on 2017/4/20.
 */

public class LearnHoursAdapter extends BaseRecyclerAdapter<LearnHours> {
    private Context context;
    private ArrayList<LearnHours> data;
    public LearnHoursAdapter(Context context, int layoutResId, ArrayList<LearnHours> data) {
        super(context, layoutResId, data);
        this.context=context;
        this.data=data;
    }

    @Override
    protected void convert(BaseViewHolder holder, final LearnHours item) {
        CircleImageView header= (CircleImageView) holder.getView().findViewById(R.id.learnHour_header);
        View line=holder.getView().findViewById(R.id.learnHour_colorLine);
        ImageView sex= (ImageView) holder.getView().findViewById(R.id.learnHour_sex);
        TextView name= (TextView) holder.getView().findViewById(R.id.learnHour_name);
        TextView number= (TextView) holder.getView().findViewById(R.id.learnHour_number);
        TextView date= (TextView) holder.getView().findViewById(R.id.learnHour_date);
        TextView time= (TextView) holder.getView().findViewById(R.id.learnHour_time);
        TextView lesson= (TextView) holder.getView().findViewById(R.id.learnHour_lesson);
        TextView price= (TextView) holder.getView().findViewById(R.id.learnHour_price);
        final TextView click= (TextView) holder.getView().findViewById(R.id.learnHour_click);
        //
        if(holder.getAdapterPosition()%3==0){
            line.setBackgroundColor(context.getResources().getColor(R.color.swipe_color1));
        }else if(holder.getAdapterPosition()%3==1){
            line.setBackgroundColor(context.getResources().getColor(R.color.swipe_color2));
        }else{
            line.setBackgroundColor(context.getResources().getColor(R.color.swipe_color3));
        }

        if(item.getSex().equals("1")){
            sex.setImageResource(R.mipmap.boy);
        }else {
            sex.setImageResource(R.mipmap.girl);
        }
        Glide.with(context).load(item.getOss_photo()).diskCacheStrategy(DiskCacheStrategy.NONE).error(R.drawable.header).crossFade().into(header);
        name.setText(item.getCoach_name());
        number.setText(item.getRecnum());
        date.setText(item.getSdate());
        time.setText(item.getStarttime()+"-"+item.getEndtime());
        lesson.setText(item.getCar_id()+item.getStage());
        price.setText(item.getAmount());
        if(data.size()>0){
            if(item.getPay_status().equals("10")){
                click.setText("待付款");
                click.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                click.setBackgroundResource(R.drawable.text_background2);
            }else if(item.getEval_status().equals("10")){
                click.setText("待评价");
                click.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                click.setBackgroundResource(R.drawable.text_background2);
            }else if(item.getEval_status().equals("20")){
                click.setText("已评价");
            }
            click.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(click.getText().equals("待付款")){
                        Intent evalu=new Intent(context, BookingPayActivity.class);
                        evalu.putExtra("Id",item.getRid());
                        evalu.putExtra("Type","20");
                        evalu.putExtra("Sex",item.getSex());
                        context.startActivity(evalu);
                    }else if(click.getText().equals("待评价")){
                        Intent evalu=new Intent(context,HourEvaluActivity.class);
                        evalu.putExtra("Id",item.getRid());
                        context.startActivity(evalu);
                    }
                }
            });
        }
    }
}
