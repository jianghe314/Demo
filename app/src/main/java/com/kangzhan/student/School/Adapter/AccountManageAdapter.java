package com.kangzhan.student.School.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.holenzhou.pullrecyclerview.BaseRecyclerAdapter;
import com.holenzhou.pullrecyclerview.BaseViewHolder;
import com.kangzhan.student.R;
import com.kangzhan.student.School.Bean.AccountManage;
import com.kangzhan.student.School.SlideMenu.AccountDetail;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kangzhan011 on 2017/6/27.
 */

public class AccountManageAdapter extends BaseRecyclerAdapter<AccountManage> {
    private Context context;
    private ArrayList<AccountManage> data;
    public AccountManageAdapter(Context context, int layoutResId, ArrayList<AccountManage> data) {
        super(context, layoutResId, data);
        this.context=context;
        this.data=data;
    }

    @Override
    protected void convert(BaseViewHolder holder, final AccountManage item) {
        ImageView choice= (ImageView) holder.getView().findViewById(R.id.school_account_manage_item_choice);
        TextView name= (TextView) holder.getView().findViewById(R.id.school_account_manage_item_name);
        TextView phone= (TextView) holder.getView().findViewById(R.id.school_account_manage_item_phone);
        TextView position= (TextView) holder.getView().findViewById(R.id.school_account_manage_item_work);
        //
        if(item.isVisible()){
            choice.setVisibility(View.VISIBLE);
        }else {
            choice.setVisibility(View.GONE);
        }
        if(item.isClick()){
            choice.setImageResource(R.mipmap.student_test_choice1);
        }else {
            choice.setImageResource(R.mipmap.student_test_choice);
        }
        name.setText(item.getReal_name());
        phone.setText(item.getPhone());
        position.setText(item.getStatus());
        holder.getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(item.isVisible()){
                    //可见时，点击选择按钮
                    if(item.isClick()){
                        item.setClick(false);
                    }else {
                        item.setClick(true);
                    }
                    notifyDataSetChanged();
                }else {
                    Intent detail=new Intent(context, AccountDetail.class);
                    detail.putExtra("who",item);
                    context.startActivity(detail);
                }

            }
        });
    }
}
