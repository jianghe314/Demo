package com.kangzhan.student.CompayManage.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.holenzhou.pullrecyclerview.BaseRecyclerAdapter;
import com.holenzhou.pullrecyclerview.BaseViewHolder;
import com.kangzhan.student.CompayManage.Bean.BookingOrder;
import com.kangzhan.student.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kangzhan011 on 2017/7/26.
 */

public class BookingOrderAdapter extends BaseRecyclerAdapter<BookingOrder> {
    private Context context;
    private ArrayList<BookingOrder> data;
    public BookingOrderAdapter(Context context, int layoutResId, ArrayList<BookingOrder> data) {
        super(context, layoutResId, data);
        this.context=context;
        this.data=data;
    }

    @Override
    protected void convert(BaseViewHolder holder, final BookingOrder item) {
        RelativeLayout container= (RelativeLayout) holder.getView().findViewById(R.id.compay_teaching_booking_container);
        ImageView choice= (ImageView) holder.getView().findViewById(R.id.compay_teaching_booking_choice);
        TextView name= (TextView) holder.getView().findViewById(R.id.compay_teaching_booking_name);
        TextView price= (TextView) holder.getView().findViewById(R.id.compay_teaching_booking_price);
        TextView time= (TextView) holder.getView().findViewById(R.id.compay_teaching_booking_time);
        TextView subject= (TextView) holder.getView().findViewById(R.id.compay_teaching_booking_subject);
        TextView teacher= (TextView) holder.getView().findViewById(R.id.compay_teaching_booking_teacher);
        TextView lable= (TextView) holder.getView().findViewById(R.id.compay_teaching_booking_carLable);
        RelativeLayout bg= (RelativeLayout) holder.getView().findViewById(R.id.compay_teaching_booking_bg);
        TextView status= (TextView) holder.getView().findViewById(R.id.compay_teaching_booking_status);
        //
        if(item.isShow()){
            container.setVisibility(View.VISIBLE);
        }else {
            container.setVisibility(View.GONE);
        }
        if(item.isClick()){
            choice.setImageResource(R.mipmap.student_test_choice1);
        }else {
            choice.setImageResource(R.mipmap.student_test_choice);
        }
        name.setText(item.getName());
        price.setText(item.getPrice());
        time.setText("时间："+item.getSdate());
        subject.setText("课程："+item.getStarttime()+"-"+item.getEndtime());
        teacher.setText("教练："+item.getCoach_id());
        lable.setText("车牌："+item.getCar_id());
        status.setText(item.getStatus());
        if(item.getStatus().equals("待培训")){
            bg.setBackgroundColor(ContextCompat.getColor(context,R.color.textColor_orange));
        }else if(item.getStatus().equals("已取消")){
            bg.setBackgroundColor(ContextCompat.getColor(context,R.color.textColor_gray));
        }else {
            bg.setBackgroundColor(ContextCompat.getColor(context,R.color.colorPrimary));
        }
        holder.getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(item.isShow()){
                    if(item.isClick()){
                        item.setClick(false);
                    }else {
                        item.setClick(true);
                    }
                    notifyDataSetChanged();
                }else {
                    BottomSheetDialog dialog=new BottomSheetDialog(context);
                    View view= LayoutInflater.from(context).inflate(R.layout.item_compay_bookingorder_detail,null);
                    TextView name= (TextView) view.findViewById(R.id.item_compay_bookingorder_detail_name);
                    TextView clerk= (TextView) view.findViewById(R.id.item_compay_bookingorder_detail_clerk);
                    TextView school= (TextView) view.findViewById(R.id.item_compay_bookingorder_detail_school);
                    TextView time= (TextView) view.findViewById(R.id.item_compay_bookingorder_detail_sdate);
                    TextView subject= (TextView) view.findViewById(R.id.item_compay_bookingorder_detail_subject);
                    TextView price= (TextView) view.findViewById(R.id.item_compay_bookingorder_detail_price);
                    TextView label= (TextView) view.findViewById(R.id.item_compay_bookingorder_detail_label);
                    name.setText(item.getName());
                    clerk.setText(item.getCoach_id());
                    school.setText(item.getInstname());
                    time.setText(item.getSdate());
                    subject.setText(item.getStage()+" "+item.getStarttime()+"-"+item.getEndtime());
                    price.setText(item.getPrice());
                    label.setText(item.getCar_id());
                    dialog.setContentView(view);
                    dialog.setCanceledOnTouchOutside(true);
                    dialog.setCancelable(true);
                    dialog.show();

                }
            }
        });
    }
}
