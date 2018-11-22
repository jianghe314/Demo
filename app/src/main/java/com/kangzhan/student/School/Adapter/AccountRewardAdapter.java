package com.kangzhan.student.School.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.holenzhou.pullrecyclerview.BaseRecyclerAdapter;
import com.holenzhou.pullrecyclerview.BaseViewHolder;
import com.kangzhan.student.R;
import com.kangzhan.student.School.Account.School_reward_detail;
import com.kangzhan.student.School.Bean.AccountRewardRecord;

import java.util.ArrayList;


/**
 * Created by kangzhan011 on 2017/7/7.
 */

public class AccountRewardAdapter extends BaseRecyclerAdapter<AccountRewardRecord> {
    private ArrayList<AccountRewardRecord> data;
    private Context context;
    public AccountRewardAdapter(Context context, int layoutResId, ArrayList<AccountRewardRecord> data) {
        super(context, layoutResId, data);
        this.data=data;
        this.context=context;
    }

    @Override
    protected void convert(BaseViewHolder holder, final AccountRewardRecord item) {
        RelativeLayout bg= (RelativeLayout) holder.getView().findViewById(R.id.item_school_account_reward_bg);
        ImageView choice= (ImageView) holder.getView().findViewById(R.id.item_school_account_reward_choice);
        TextView name= (TextView) holder.getView().findViewById(R.id.item_school_account_reward_name);
        TextView score= (TextView) holder.getView().findViewById(R.id.item_school_account_reward_score);
        TextView price= (TextView) holder.getView().findViewById(R.id.item_school_account_reward_get);
        TextView time= (TextView) holder.getView().findViewById(R.id.item_school_account_pp);
        //

        name.setText(item.getCoach_name());
        score.setText(item.getScore());
        price.setText(item.getAmount());
        time.setText("加盟日期："+item.getHiredate());
        holder.getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent detail=new Intent(context, School_reward_detail.class);
                detail.putExtra("Id",item.getCoach_id());
                context.startActivity(detail);
            }
        });
    }
}
