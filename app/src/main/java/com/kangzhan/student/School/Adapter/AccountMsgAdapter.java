package com.kangzhan.student.School.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.holenzhou.pullrecyclerview.BaseRecyclerAdapter;
import com.holenzhou.pullrecyclerview.BaseViewHolder;
import com.kangzhan.student.R;
import com.kangzhan.student.School.Account.School_Msg_detail;
import com.kangzhan.student.School.Bean.AccountMsg;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kangzhan011 on 2017/7/6.
 */

public class AccountMsgAdapter extends BaseRecyclerAdapter<AccountMsg> {
    private ArrayList<AccountMsg> data;
    private Context context;
    public AccountMsgAdapter(Context context, int layoutResId, ArrayList<AccountMsg> data) {
        super(context, layoutResId, data);
        this.data=data;
        this.context=context;
    }

    @Override
    protected void convert(BaseViewHolder holder, final AccountMsg item) {
        TextView time= (TextView) holder.getView().findViewById(R.id.school_account_msg_time);
        TextView num= (TextView) holder.getView().findViewById(R.id.school_account_msg_num);
        TextView price= (TextView) holder.getView().findViewById(R.id.school_account_msg_price);
        RelativeLayout bg= (RelativeLayout) holder.getView().findViewById(R.id.school_account_msg_bg);
        TextView status= (TextView) holder.getView().findViewById(R.id.school_account_msg_status);
        //
        if(item.getStatus()!=null){
            if(item.getStatus().equals("已确认")){
                bg.setBackgroundColor(ContextCompat.getColor(context,R.color.colorPrimary));
            }else {
                bg.setBackgroundColor(ContextCompat.getColor(context,R.color.swipe_color1));
            }
        }
        time.setText(item.getMonth()+"月");
        num.setText(item.getSend_counts());
        price.setText(item.getAmount());
        status.setText(item.getStatus());
        holder.getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent detail=new Intent(context, School_Msg_detail.class);
                detail.putExtra("Id",item.getId());
                context.startActivity(detail);
            }
        });

    }
}
