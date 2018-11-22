package com.kangzhan.student.Teacher.Adapter;

import android.content.Context;
import android.os.CountDownTimer;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.TimeUtils;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.holenzhou.pullrecyclerview.BaseRecyclerAdapter;
import com.holenzhou.pullrecyclerview.BaseViewHolder;
import com.kangzhan.student.R;
import com.kangzhan.student.Teacher.bean.TeacherGetOrder;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by kangzhan011 on 2017/4/13.
 */

public class TeacherTrainGetOrderAdapter extends BaseRecyclerAdapter<TeacherGetOrder> {
    private Context context;
    private ArrayList<TeacherGetOrder> data;
    private GetOrder order;
    private CallPhone callPhone;
    public TeacherTrainGetOrderAdapter(Context context, int layoutResId, ArrayList<TeacherGetOrder> data) {
        super(context, layoutResId, data);
        this.context=context;
        this.data=data;
    }
    public void getOrder(GetOrder order){
        this.order=order;
    }
    public void callPhone(CallPhone callPhonephone){
        this.callPhone=callPhonephone;
    }
    @Override
    protected void convert(final BaseViewHolder holder, final TeacherGetOrder item) {
        CircleImageView header= (CircleImageView) holder.getView().findViewById(R.id.teacher_train_getOrder_item_header);
        View colorLine=holder.getView().findViewById(R.id.teacher_train_getOrder_item_colorLine);
        TextView isId= (TextView) holder.getView().findViewById(R.id.item_getOrder_isId);
        ImageView sex= (ImageView) holder.getView().findViewById(R.id.item_getOrder_sex);
        TextView name= (TextView) holder.getView().findViewById(R.id.item_getOrder_name);
        TextView isPickUp= (TextView) holder.getView().findViewById(R.id.item_getOrder_pickUp);
        TextView date= (TextView) holder.getView().findViewById(R.id.item_getOrder_date);
        TextView phone= (TextView) holder.getView().findViewById(R.id.item_getOrder_phone_Num);
        TextView address= (TextView) holder.getView().findViewById(R.id.item_getOrder_address);
        TextView price= (TextView) holder.getView().findViewById(R.id.item_getOrder_price);
        TextView hours= (TextView) holder.getView().findViewById(R.id.item_getOrder_remainHour);
        TextView minte= (TextView) holder.getView().findViewById(R.id.item_getOrder_remainMinute);
        TextView linkTa= (TextView) holder.getView().findViewById(R.id.item_getOrder_linkTa);
        TextView getOrder= (TextView) holder.getView().findViewById(R.id.item_getOrder_getOrder);
        //传数据
        if(holder.getAdapterPosition()%2==0){
            colorLine.setBackgroundColor(ContextCompat.getColor(context,R.color.textColor_orange));
        }else if(holder.getAdapterPosition()%2==1){
            colorLine.setBackgroundColor(ContextCompat.getColor(context,R.color.colorPrimary));
        }else {
            colorLine.setBackgroundColor(ContextCompat.getColor(context,R.color.text_background_color_aqua));
        }
        Glide.with(context).load(item.getOss_photo()).error(R.drawable.header).crossFade().into(header);
        isId.setText(item.getDrilic());
        if(item.getSex().equals("女")){
            sex.setImageResource(R.mipmap.girl);
        }else {
            sex.setImageResource(R.mipmap.boy);
        }
        name.setText(item.getStuname());
        isPickUp.setText(item.getShuttle());
        date.setText(item.getStart_time()+"-"+item.getEnd_time());
        phone.setText(item.getPhone());
        address.setText(item.getAddress());
        price.setText("￥"+item.getPrice());
        //设置时间 秒
        long time=item.getStarttimes()-item.getNow();
        long result=time/(60*60);
        long mint=(time/60)%60;
        hours.setText(result+"");
        minte.setText(mint+"");

        //点击事件
        linkTa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //打电话
                if(callPhone!=null){
                    callPhone.getCallPhone(item,holder.getAdapterPosition());
                }
            }
        });
        getOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //接单
                if(order!=null){
                    order.getOrder(item,holder.getAdapterPosition());
                }
            }
        });

    }

    public interface GetOrder{
        void getOrder(TeacherGetOrder item,int position);
    }
    public interface CallPhone{
        void getCallPhone(TeacherGetOrder item,int position);
    }

}
