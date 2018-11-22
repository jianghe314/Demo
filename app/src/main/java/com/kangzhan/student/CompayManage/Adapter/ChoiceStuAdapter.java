package com.kangzhan.student.CompayManage.Adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.holenzhou.pullrecyclerview.BaseRecyclerAdapter;
import com.holenzhou.pullrecyclerview.BaseViewHolder;
import com.kangzhan.student.CompayManage.Bean.CompayChoiceStu;
import com.kangzhan.student.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kangzhan011 on 2017/8/1.
 */

public class ChoiceStuAdapter extends BaseRecyclerAdapter<CompayChoiceStu> {
    private ArrayList<CompayChoiceStu> data;
    private Context context;
    public ChoiceStuAdapter(Context context, int layoutResId, ArrayList<CompayChoiceStu> data) {
        super(context, layoutResId, data);
        this.data=data;
        this.context=context;
    }

    @Override
    protected void convert(BaseViewHolder holder, final CompayChoiceStu item) {
        ImageView choice= (ImageView) holder.getView().findViewById(R.id.item_school_notice_choice_stu_choice);
        ImageView sex= (ImageView) holder.getView().findViewById(R.id.item_school_notice_choice_stu_sex);
        TextView name= (TextView) holder.getView().findViewById(R.id.item_school_notice_choice_stu_name);
        TextView phone= (TextView) holder.getView().findViewById(R.id.item_school_notice_choice_stu_phone);
        TextView type= (TextView) holder.getView().findViewById(R.id.item_school_notice_choice_stu_type);
        //
        if(item.isClick()){
            choice.setImageResource(R.mipmap.student_test_choice1);
        }else {
            choice.setImageResource(R.mipmap.student_test_choice);
        }
        if(item.getSex().equals("男")){
            sex.setImageResource(R.mipmap.boy);
        }else {
            sex.setImageResource(R.mipmap.girl);
        }
        name.setText(item.getName());
        phone.setText(item.getPhone());
        type.setText(item.getTraintype());
        holder.getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(item.isClick()){
                    item.setClick(false);
                }else {
                    item.setClick(true);
                }
                notifyDataSetChanged();
            }
        });
    }
}
