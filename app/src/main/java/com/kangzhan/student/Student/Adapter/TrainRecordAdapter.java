package com.kangzhan.student.Student.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
import com.kangzhan.student.Student.bean.TrainRecord;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by kangzhan011 on 2017/3/29.
 */

public class TrainRecordAdapter extends BaseRecyclerAdapter<TrainRecord>{
    private Context context;
    private ArrayList<TrainRecord> data;
    public TrainRecordAdapter(Context context, int layoutResId, ArrayList<TrainRecord> data) {
        super(context, layoutResId, data);
        this.context=context;
        this.data=data;

    }

    @Override
    protected void convert(BaseViewHolder holder, final TrainRecord item) {
        View colorline=holder.getView().findViewById(R.id.train_line);
        CircleImageView header= (CircleImageView) holder.getView().findViewById(R.id.train_record_header);
        ImageView sex= (ImageView) holder.getView().findViewById(R.id.train_record_sex);
        TextView name= (TextView) holder.getView().findViewById(R.id.train_record_name);
        TextView time= (TextView) holder.getView().findViewById(R.id.train_record_time);
        TextView lesson= (TextView) holder.getView().findViewById(R.id.train_record_class);
        TextView price= (TextView) holder.getView().findViewById(R.id.train_record_price);
        final TextView remark= (TextView) holder.getView().findViewById(R.id.train_record_evalu);
        //
        if(holder.getAdapterPosition()%3==0){
           colorline.setBackgroundColor(ContextCompat.getColor(context,R.color.textColor_orange));
        }else if(holder.getAdapterPosition()%3==1){
            colorline.setBackgroundColor(ContextCompat.getColor(context,R.color.text_background_color_aqua));
        }else {
            colorline.setBackgroundColor(ContextCompat.getColor(context,R.color.colorPrimary));
        }
        if(item.getSex().equals("女")){
            sex.setImageResource(R.mipmap.girl);
        }else {
            sex.setImageResource(R.mipmap.boy);
        }
        Glide.with(context).load(item.getOss_photo()).diskCacheStrategy(DiskCacheStrategy.NONE).placeholder(R.drawable.header).error(R.drawable.header).crossFade().into(header);
        name.setText(item.getCoachname());
        time.setText(item.getStart_time()+" "+item.getEnd_time());
        lesson.setText(item.getDetail());
        price.setText(item.getPrice());
        if(item.getPay_status().equals("10")||item.getPay_status().equals("20")||item.getPay_status().equals("40")){
            remark.setText("待付款");
            remark.setBackgroundResource(R.drawable.text_background2);
            remark.setTextColor(ContextCompat.getColor(context,R.color.colorPrimary));
        }else {
            if(item.getEval_status().equals("10")){
                remark.setText("待评价");
                remark.setBackgroundResource(R.drawable.text_background2);
                remark.setTextColor(ContextCompat.getColor(context,R.color.colorPrimary));
            }else if(item.getEval_status().equals("20")){
                remark.setText("已评价");
            }
        }
        remark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(remark.getText().equals("待付款")){
                    Intent pay=new Intent(context,PayActivity.class);
                    pay.putExtra("Id",String.valueOf(item.getId()));
                    pay.putExtra("Type","30");
                    context.startActivity(pay);
                }else if(remark.getText().equals("待评价")){
                    Intent re=new Intent(context,EvalutionActivity.class);
                    re.putExtra("Id",String.valueOf(item.getId()));
                    context.startActivity(re);
                }
            }
        });

    }
}
