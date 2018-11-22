package com.kangzhan.student.Student.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kangzhan.student.R;
import com.kangzhan.student.Student.bean.TrainBooking;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.kangzhan.student.R.id.order_date;
import static com.kangzhan.student.R.id.ordered_time;

/**
 * Created by kangzhan011 on 2017/3/29.
 */

public class TrainBookingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private ArrayList<TrainBooking> data;
    private final int TYPE1=1;
    private final int TYPE2=2;
    private setCancelOrder setCancelOrder;
    private LayoutInflater inflater;
    public TrainBookingAdapter(Context context,ArrayList<TrainBooking> data) {
        this.context=context;
        this.data=data;
        inflater=LayoutInflater.from(context);
    }

    @Override
    public int getItemViewType(int position) {
        //错的：：：：状态(10-未接单; 20-已接单; 30-流单， 40-已取消, 50-已开始，60-已完成)
        if(data.get(position).getStatus().equals("10")){
            //未结单
            return TYPE1;
        }else {
            //已接单
            return TYPE2;
        }
    }
    public void setCancelOrder(setCancelOrder setCancelOrder){
        this.setCancelOrder=setCancelOrder;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        RecyclerView.ViewHolder holder;
        if(viewType==TYPE1){
            view=inflater.inflate(R.layout.item_list_ordered_adapter,parent,false);
            holder=new ViewHolder1(view);
        }else {
            view=inflater.inflate(R.layout.item_list_order_adapter,parent,false);
            holder=new ViewHolder2(view);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final TrainBooking item=data.get(position);
        if(getItemViewType(position)==TYPE1){
            ViewHolder1 holder1= (ViewHolder1) holder;
            holder1.time_tv1.setText(item.getStart_time()+"-"+item.getEnd_time());
            holder1.price_tv1.setText("￥"+item.getPrice());
            holder1.lesson_tv1.setText(item.getDetail());
            holder1.pickUp_tv1.setText(item.getShuttle());
            holder1.v.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if(setCancelOrder!=null){
                        setCancelOrder.SetCancelOrder(item,position);
                    }
                    return true;
                }
            });
        }else if(getItemViewType(position)==TYPE2){
            ViewHolder2 holder2= (ViewHolder2) holder;
            holder2.lesson_tv2.setText(item.getDetail());
            if(item.getSex().equals("女")){
                holder2.sex.setImageResource(R.mipmap.girl);
            }else {
                holder2.sex.setImageResource(R.mipmap.boy);
            }
            holder2.name.setText(item.getCoachname());
            holder2.pickUp.setText(item.getShuttle());
            holder2.date_tv2.setText(item.getStart_time()+item.getEnd_time());
            holder2.phone_Num_tv2.setText(item.getCoachmobile());
            holder2.price_tv2.setText("￥"+item.getPrice());
            holder2.v.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if(setCancelOrder!=null){
                        setCancelOrder.SetCancelOrder(item,position);
                    }
                    return true;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder1 extends RecyclerView.ViewHolder{
        //未接单
        TextView time_tv1;
        TextView pickUp_tv1;
        TextView lesson_tv1;
        TextView price_tv1;
        View v;

        public ViewHolder1(View v1) {
            super(v1);
            v=v1;
            time_tv1= (TextView) v1.findViewById(R.id.ordered_time);
            pickUp_tv1= (TextView) v1.findViewById(R.id.ordered_to_pickUp);
            lesson_tv1= (TextView) v1.findViewById(R.id.ordered_class);
            price_tv1= (TextView) v1.findViewById(R.id.ordered_price);
        }
    }

    class ViewHolder2 extends RecyclerView.ViewHolder{
        //yijiedan
        CircleImageView header;
        TextView lesson_tv2;
        ImageView sex;
        TextView name;
        TextView pickUp;
        TextView date_tv2;
        TextView phone_Num_tv2;
        TextView price_tv2;
        View v;
        public ViewHolder2(View v2) {
            super(v2);
            v=v2;
            header= (CircleImageView) v2.findViewById(R.id.order_header);
            lesson_tv2= (TextView) v2.findViewById(R.id.order_lesson);
            sex= (ImageView) v2.findViewById(R.id.order_sex);
            name= (TextView) v2.findViewById(R.id.order_name);
            pickUp= (TextView) v2.findViewById(R.id.order_pickUp);
            date_tv2= (TextView) v2.findViewById(R.id.order_date);
            phone_Num_tv2= (TextView) v2.findViewById(R.id.order_phoneNum);
            price_tv2= (TextView) v2.findViewById(R.id.order_price);
        }
    }
    public interface setCancelOrder{
        void SetCancelOrder(TrainBooking item,int postion);
    }
}
