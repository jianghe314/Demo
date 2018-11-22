package com.kangzhan.student.Student.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
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
import com.kangzhan.student.ShowUI.mToast;
import com.kangzhan.student.Student.Interface.ItemCallPhone;
import com.kangzhan.student.Student.bean.BookingOrder;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by kangzhan011 on 2017/3/30.
 */

public class BookingOrderAdapter extends BaseRecyclerAdapter<BookingOrder> {
    private Context context;
    private ArrayList<BookingOrder> data;
    private msetOnLongClickListener onLongClickListener;
    private ItemCallPhone callPhone;

    public BookingOrderAdapter(Context context, int layoutResId, ArrayList<BookingOrder> data) {
        super(context, layoutResId, data);
        this.context=context;
        this.data=data;
    }
    public void SetOnLongClick(msetOnLongClickListener onLongClickListener){
        this.onLongClickListener=onLongClickListener;
    }

    @Override
    protected void convert(final BaseViewHolder holder, final BookingOrder item) {
        CircleImageView haeder= (CircleImageView) holder.getView().findViewById(R.id.circleImageView);
        TextView lesson= (TextView) holder.getView().findViewById(R.id.booking_order_lesson);
        ImageView sex= (ImageView) holder.getView().findViewById(R.id.booking_record_iv1);
        ImageView mcallphone= (ImageView) holder.getView().findViewById(R.id.booking_record_call_phone);
        TextView name= (TextView) holder.getView().findViewById(R.id.booking_order_name);
        TextView brand= (TextView) holder.getView().findViewById(R.id.booking_record_car);
        TextView car_label= (TextView) holder.getView().findViewById(R.id.student_booking_order_carLable);
        TextView phone= (TextView) holder.getView().findViewById(R.id.booking_record_phone_Num);
        TextView date= (TextView) holder.getView().findViewById(R.id.student_booking_order_date);
        TextView time= (TextView) holder.getView().findViewById(R.id.booking_order_time);
        TextView price= (TextView) holder.getView().findViewById(R.id.student_booking_order_price);
        //
        lesson.setText(item.getPerdritype());
        if(item.getSex().equals("1")){
            sex.setImageResource(R.mipmap.boy);
        }else {
            sex.setImageResource(R.mipmap.girl);
        }
        Glide.with(context).load(item.getOss_photo()).diskCacheStrategy(DiskCacheStrategy.NONE).error(R.drawable.header).crossFade().into(haeder);
        name.setText(item.getName());
        brand.setText(item.getBrand());
        car_label.setText(item.getCar_id());
        phone.setText(item.getMobile());
        date.setText(item.getSdate());
        time.setText(item.getStarttime()+"-"+item.getEndtime());
        price.setText("￥"+item.getPrice());
        //
        holder.getView().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(onLongClickListener!=null){
                    onLongClickListener.OnLongClick(holder.getAdapterPosition(),item);
                }
                return true;
            }
        });
        mcallphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(callPhone!=null){
                    callPhone.itemCallPhone(item.getMobile());
                }
            }
        });

    }

    public void CallPhone(ItemCallPhone callPhone){
        this.callPhone=callPhone;
    }

    public interface msetOnLongClickListener{
        void OnLongClick(int position,BookingOrder item);
    }
}
