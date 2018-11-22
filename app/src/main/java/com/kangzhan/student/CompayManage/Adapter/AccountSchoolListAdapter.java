package com.kangzhan.student.CompayManage.Adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.holenzhou.pullrecyclerview.BaseRecyclerAdapter;
import com.holenzhou.pullrecyclerview.BaseViewHolder;
import com.kangzhan.student.CompayManage.Bean.AccountSchoolList;
import com.kangzhan.student.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kangzhan011 on 2017/7/28.
 */

public class AccountSchoolListAdapter extends BaseRecyclerAdapter<AccountSchoolList> {
    private ArrayList<AccountSchoolList> data;
    private Context context;
    public AccountSchoolListAdapter(Context context, int layoutResId, ArrayList<AccountSchoolList> data) {
        super(context, layoutResId, data);
        this.context=context;
        this.data=data;
    }

    @Override
    protected void convert(BaseViewHolder holder, final AccountSchoolList item) {
        ImageView choice= (ImageView) holder.getView().findViewById(R.id.item_compay_account_choice);
        TextView school= (TextView) holder.getView().findViewById(R.id.item_compay_account_school);
        if(item.isClick()){
            choice.setImageResource(R.mipmap.student_test_choice1);
        }else {
            choice.setImageResource(R.mipmap.student_test_choice);
        }
        school.setText(item.getName());
        //查看账单
        holder.getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i <data.size() ; i++) {
                    data.get(i).setClick(false);
                }
                item.setClick(true);
                notifyDataSetChanged();
            }
        });
    }
}
