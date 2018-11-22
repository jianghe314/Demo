package com.kangzhan.student.CompayManage.Adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.holenzhou.pullrecyclerview.BaseRecyclerAdapter;
import com.holenzhou.pullrecyclerview.BaseViewHolder;
import com.kangzhan.student.CompayManage.Bean.CompayChoiceSchool;
import com.kangzhan.student.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kangzhan011 on 2017/8/1.
 */

public class CompayChoiceSchoolAdapter extends BaseRecyclerAdapter<CompayChoiceSchool> {
    private Context context;
    private ArrayList<CompayChoiceSchool> data;
    public CompayChoiceSchoolAdapter(Context context, int layoutResId, ArrayList<CompayChoiceSchool> data) {
        super(context, layoutResId, data);
        this.context=context;
        this.data=data;
    }

    @Override
    protected void convert(BaseViewHolder holder, final CompayChoiceSchool item) {
        ImageView choice= (ImageView) holder.getView().findViewById(R.id.item_school_notice_choice_tea_choice);
        TextView school= (TextView) holder.getView().findViewById(R.id.item_compay_notice_choice_school_school);
        TextView phone= (TextView) holder.getView().findViewById(R.id.item_compay_notice_choice_school_phone);
        TextView name= (TextView) holder.getView().findViewById(R.id.item_compay_notice_choice_school_name);
        if(item.isClick()){
            choice.setImageResource(R.mipmap.student_test_choice1);
        }else {
            choice.setImageResource(R.mipmap.student_test_choice);
        }
        school.setText(item.getName());
        phone.setText(item.getPhone());
        name.setText("联系人："+item.getContact());
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
