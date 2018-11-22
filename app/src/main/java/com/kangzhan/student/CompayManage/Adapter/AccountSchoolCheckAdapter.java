package com.kangzhan.student.CompayManage.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.holenzhou.pullrecyclerview.BaseRecyclerAdapter;
import com.holenzhou.pullrecyclerview.BaseViewHolder;
import com.kangzhan.student.CompayManage.AccountManage.RemittanceRecordActivity;
import com.kangzhan.student.CompayManage.Bean.AccountSchoolChecking;
import com.kangzhan.student.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kangzhan011 on 2017/7/31.
 */

public class AccountSchoolCheckAdapter extends BaseRecyclerAdapter<AccountSchoolChecking> {
    private Context context;
    private ArrayList<AccountSchoolChecking> data;
    public AccountSchoolCheckAdapter(Context context, int layoutResId, ArrayList<AccountSchoolChecking> data) {
        super(context, layoutResId, data);
        this.context=context;
        this.data=data;
    }

    @Override
    protected void convert(BaseViewHolder holder, final AccountSchoolChecking item) {
        RelativeLayout container= (RelativeLayout) holder.getView().findViewById(R.id.compay_account_inquery_container);
        ImageView choice= (ImageView) holder.getView().findViewById(R.id.compay_account_inquery_choice);
        TextView school= (TextView) holder.getView().findViewById(R.id.compay_account_inquery_school);
        TextView clerk= (TextView) holder.getView().findViewById(R.id.compay_account_inquery_clerk);
        TextView code= (TextView) holder.getView().findViewById(R.id.compay_account_inquery_code);
        TextView cost= (TextView) holder.getView().findViewById(R.id.compay_account_inquery_cost);
        TextView draw= (TextView) holder.getView().findViewById(R.id.compay_account_inquery_draw);
        RelativeLayout bg= (RelativeLayout) holder.getView().findViewById(R.id.compay_account_inquery_bg);
        TextView status= (TextView) holder.getView().findViewById(R.id.compay_account_inquery_status);
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
        school.setText(item.getInsti_name());
        clerk.setText("业务员："+item.getClerk_name());
        code.setText("汇款单据："+item.getRemittance_number());
        cost.setText("培训费用："+item.getAmount());
        draw.setText("平台提成："+item.getDraw());
        status.setText(item.getStatus());
        switch (item.getStatus()){
            case "待发送":
                bg.setBackgroundColor(ContextCompat.getColor(context,R.color.light_gray));
                break;
            case "待确定":
                bg.setBackgroundColor(ContextCompat.getColor(context,R.color.swipe_color2));
                break;
            case "汇款失败":
                bg.setBackgroundColor(ContextCompat.getColor(context,R.color.swipe_color1));
                break;
            case "已汇款":
                bg.setBackgroundColor(ContextCompat.getColor(context,R.color.colorPrimary));
                break;
            case "已确认":
                bg.setBackgroundColor(ContextCompat.getColor(context,R.color.text_background_color_aqua));
                break;
            default:
                break;
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
                    //查看账单详情
                    Intent detail=new Intent(context, RemittanceRecordActivity.class);
                    detail.putExtra("Id",item.getInst_id());
                    context.startActivity(detail);
                }
            }
        });

    }
}
