package com.kangzhan.student.School.Adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.holenzhou.pullrecyclerview.BaseRecyclerAdapter;
import com.holenzhou.pullrecyclerview.BaseViewHolder;
import com.kangzhan.student.R;
import com.kangzhan.student.School.Bean.BookingOrder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kangzhan011 on 2017/7/5.
 */

public class BookingOrderAdapter extends BaseRecyclerAdapter<BookingOrder> {
    private ArrayList<BookingOrder> data;
    private Context context;
    public BookingOrderAdapter(Context context, int layoutResId, ArrayList<BookingOrder> data) {
        super(context, layoutResId, data);
        this.data=data;
        this.context=context;
    }

    @Override
    protected void convert(BaseViewHolder holder, BookingOrder item) {
        //ImageView choice=holder.getView().findViewById(R.id.item_booking_order_choice)
        ImageView sex= (ImageView) holder.getView().findViewById(R.id.item_school_booking_order_sex);
        TextView name= (TextView) holder.getView().findViewById(R.id.item_booking_order_name);
        TextView time= (TextView) holder.getView().findViewById(R.id.item_school_booking_order_time);
        TextView carLabel= (TextView) holder.getView().findViewById(R.id.item_school_booking_order_carLabel);
        TextView subjcet= (TextView) holder.getView().findViewById(R.id.item_school_booking_order_subject);
        RelativeLayout bg= (RelativeLayout) holder.getView().findViewById(R.id.item_booking_order_bg);
        TextView status= (TextView) holder.getView().findViewById(R.id.item_booking_order_status);
        //
        if(item.getStudent_sex().equals("男")){
            sex.setImageResource(R.mipmap.boy);
        }else {
            sex.setImageResource(R.mipmap.girl);
        }
        name.setText(item.getStudent_name());
        time.setText("时间："+item.getCreate_time());
        carLabel.setText("车牌："+item.getCar_name());
        subjcet.setText("课程："+item.getTime_name()+" "+item.getCoach_name()+" "+item.getStage_name());
        if(item.getStatus_name().equals("已取消")){
            bg.setBackgroundColor(ContextCompat.getColor(context,R.color.login_picbgc));
        }else if(item.getStatus_name().equals("已完成")){
            bg.setBackgroundColor(ContextCompat.getColor(context,R.color.colorPrimary));
        }else if(item.getStatus_name().equals("培训中")){
            bg.setBackgroundColor(ContextCompat.getColor(context,R.color.text_background_color_aqua));
        }else if(item.getStatus_name().equals("待培训")){
            bg.setBackgroundColor(ContextCompat.getColor(context,R.color.swipe_color1));
        }
        status.setText(item.getStatus_name());
    }
}
