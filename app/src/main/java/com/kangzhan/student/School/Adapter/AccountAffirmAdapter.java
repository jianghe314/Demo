package com.kangzhan.student.School.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.holenzhou.pullrecyclerview.BaseRecyclerAdapter;
import com.holenzhou.pullrecyclerview.BaseViewHolder;
import com.kangzhan.student.R;
import com.kangzhan.student.School.Account.School_Account_Manage_Detail;
import com.kangzhan.student.School.Bean.AccountAffirm;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kangzhan011 on 2017/7/6.
 */

public class AccountAffirmAdapter extends BaseRecyclerAdapter<AccountAffirm> {
    private ArrayList<AccountAffirm> data;
    private Context context;
    public AccountAffirmAdapter(Context context, int layoutResId, ArrayList<AccountAffirm> data) {
        super(context, layoutResId, data);
        this.data=data;
        this.context=context;
    }

    @Override
    protected void convert(BaseViewHolder holder, final AccountAffirm item) {
        TextView time= (TextView) holder.getView().findViewById(R.id.item_school_account_affirm_time);
        TextView hour= (TextView) holder.getView().findViewById(R.id.item_school_account_affirm_hour);
        TextView draw= (TextView) holder.getView().findViewById(R.id.item_school_account_affirm_draw);
        TextView price= (TextView) holder.getView().findViewById(R.id.item_school_account_affirm_price);
        RelativeLayout bg= (RelativeLayout) holder.getView().findViewById(R.id.item_school_account_affirm_bg);
        TextView status= (TextView) holder.getView().findViewById(R.id.item_school_account_affirm_status);
        //
        time.setText(item.getTime());
        hour.setText(item.getTrain_length());
        draw.setText(item.getDraw());
        price.setText(item.getAmount());
        if(item.getStatus().equals("已确认")){
            bg.setBackgroundColor(ContextCompat.getColor(context,R.color.colorPrimary));
        }else {
            bg.setBackgroundColor(ContextCompat.getColor(context,R.color.swipe_color1));
        }
        status.setText(item.getStatus());
        holder.getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent detail=new Intent(context, School_Account_Manage_Detail.class);
                detail.putExtra("Id",item.getId());
                context.startActivity(detail);
            }
        });
    }
}
