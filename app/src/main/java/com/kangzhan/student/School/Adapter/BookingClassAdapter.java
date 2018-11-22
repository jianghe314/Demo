package com.kangzhan.student.School.Adapter;

import android.content.Context;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.holenzhou.pullrecyclerview.BaseRecyclerAdapter;
import com.holenzhou.pullrecyclerview.BaseViewHolder;
import com.kangzhan.student.R;
import com.kangzhan.student.School.Bean.BookingClass;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kangzhan011 on 2017/7/5.
 */

public class BookingClassAdapter extends BaseRecyclerAdapter<BookingClass> {
    private ArrayList<BookingClass> data;
    private Context context;
    private BottomSheetDialog dialog;
    public BookingClassAdapter(Context context, int layoutResId, ArrayList<BookingClass> data) {
        super(context, layoutResId, data);
        this.data=data;
        this.context=context;
    }

    @Override
    protected void convert(BaseViewHolder holder, final BookingClass item) {
        TextView className= (TextView) holder.getView().findViewById(R.id.item_booking_class_className);
        TextView name= (TextView) holder.getView().findViewById(R.id.item_booking_class_name);
        TextView num= (TextView) holder.getView().findViewById(R.id.item_booking_class_num);
        TextView discount= (TextView) holder.getView().findViewById(R.id.item_booking_class_discount);
        TextView amount= (TextView) holder.getView().findViewById(R.id.item_booking_class_amount);
        RelativeLayout bg= (RelativeLayout) holder.getView().findViewById(R.id.item_booking_class_bg);
        TextView status= (TextView) holder.getView().findViewById(R.id.item_booking_class_status);
        //
        className.setText(item.getName());
        name.setText("登记人："+item.getInst_mgr_id());
        num.setText(item.getDay_max_times());
        discount.setText(item.getDiscount());
        amount.setText(item.getArrearage_amount());
        if(item.getStatus().equals("有效")){
            bg.setBackgroundColor(ContextCompat.getColor(context,R.color.colorPrimary));
        }else {
            bg.setBackgroundColor(ContextCompat.getColor(context,R.color.swipe_color1));
        }
        status.setText(item.getStatus());
        holder.getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog=new BottomSheetDialog(context);
                View view= LayoutInflater.from(context).inflate(R.layout.item_school_booking_class_detail,null);
                TextView name= (TextView) view.findViewById(R.id.item_school_booking_class_detail_name);
                TextView day= (TextView) view.findViewById(R.id.item_school_booking_class_detail_day);
                TextView week= (TextView) view.findViewById(R.id.item_school_booking_class_detail_week);
                TextView discount= (TextView) view.findViewById(R.id.item_school_booking_class_detail_discount);
                TextView amount= (TextView) view.findViewById(R.id.item_school_booking_class_detail_num);
                TextView school= (TextView) view.findViewById(R.id.item_school_booking_class_detail_school);
                TextView car= (TextView) view.findViewById(R.id.item_school_booking_class_detail_car);
                TextView startT= (TextView) view.findViewById(R.id.item_school_booking_class_detail_startT);
                TextView endT= (TextView) view.findViewById(R.id.item_school_booking_class_detail_endT);
                TextView status= (TextView) view.findViewById(R.id.item_school_booking_class_detail_status);
                name.setText(item.getName());
                day.setText(item.getDay_max_times());
                week.setText(item.getWeek_max_times());
                discount.setText(item.getDiscount());
                amount.setText(item.getArrearage_amount());
                school.setText(item.getCross_inst());
                car.setText(item.getCross_coach());
                startT.setText(item.getStart_time());
                endT.setText(item.getEnd_time());
                status.setText(item.getStatus());
                dialog.setCanceledOnTouchOutside(true);
                dialog.setCancelable(true);
                dialog.setContentView(view);
                dialog.show();
            }
        });
    }
}
