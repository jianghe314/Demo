package com.kangzhan.student.School.Adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.holenzhou.pullrecyclerview.BaseRecyclerAdapter;
import com.holenzhou.pullrecyclerview.BaseViewHolder;
import com.kangzhan.student.R;
import com.kangzhan.student.School.Bean.EduSetClass;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kangzhan011 on 2017/6/26.
 */

public class EduSetClassAdapter extends BaseRecyclerAdapter<EduSetClass> {
    private Context context;
    private ArrayList<EduSetClass> data;
    public EduSetClassAdapter(Context context, int layoutResId, ArrayList<EduSetClass> data) {
        super(context, layoutResId, data);
        this.context=context;
        this.data=data;
    }

    @Override
    protected void convert(BaseViewHolder holder, final EduSetClass item) {
        ImageView choice= (ImageView) holder.getView().findViewById(R.id.item_edu_setclass_choice);
        TextView className= (TextView) holder.getView().findViewById(R.id.item_edu_setclass_class);
        TextView crossScholl= (TextView) holder.getView().findViewById(R.id.item_edu_setclass_crossSc);
        TextView crossTea= (TextView) holder.getView().findViewById(R.id.item_edu_setclass_crossTe);
        TextView bookingNum= (TextView) holder.getView().findViewById(R.id.item_edu_setclass_bookingNum);
        TextView payNum= (TextView) holder.getView().findViewById(R.id.item_edu_setclass_NoPay);
        TextView showPay= (TextView) holder.getView().findViewById(R.id.item_edu_setclass_showPay);
        RelativeLayout bg= (RelativeLayout) holder.getView().findViewById(R.id.item_edu_setclass_sta);
        TextView stastu= (TextView) holder.getView().findViewById(R.id.item_edu_setclass_stastu);
        //
        className.setText(item.getName());
        crossScholl.setText(item.getCross_inst());
        crossTea.setText(item.getCross_coach());
        bookingNum.setText(item.getDay_max_times()+"次/天"+" "+item.getWeek_max_times()+"次/周");
        payNum.setText(item.getArrearage_times()+"次");
        showPay.setText(item.getArrearage_amount()+"元");
        if(TextUtils.equals(item.getStatus(),"有效")){
            stastu.setText("有效");
            bg.setBackgroundColor(ContextCompat.getColor(context,R.color.colorPrimary));
        }else {
            stastu.setText("无效");
            bg.setBackgroundColor(ContextCompat.getColor(context,R.color.swipe_color1));
        }
        if(item.isClick()){
            choice.setImageResource(R.mipmap.student_test_choice1);
        }else {
            choice.setImageResource(R.mipmap.student_test_choice);
        }
        //实现单选功能
        choice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(item.isClick()){
                    for (int i = 0; i <data.size(); i++) {
                        data.get(i).setClick(false);
                    }
                }else {
                    for (int i = 0; i <data.size(); i++) {
                        data.get(i).setClick(false);
                    }
                    item.setClick(true);
                }
                notifyDataSetChanged();
            }
        });

    }
}
