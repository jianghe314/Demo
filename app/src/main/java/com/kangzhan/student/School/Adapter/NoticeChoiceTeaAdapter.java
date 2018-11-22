package com.kangzhan.student.School.Adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.holenzhou.pullrecyclerview.BaseRecyclerAdapter;
import com.holenzhou.pullrecyclerview.BaseViewHolder;
import com.kangzhan.student.R;
import com.kangzhan.student.School.Bean.NoticeChoiceTea;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kangzhan011 on 2017/7/11.
 */

public class NoticeChoiceTeaAdapter extends BaseRecyclerAdapter<NoticeChoiceTea> {
    private Context context;
    private ArrayList<NoticeChoiceTea> data;
    public NoticeChoiceTeaAdapter(Context context, int layoutResId, ArrayList<NoticeChoiceTea> data) {
        super(context, layoutResId, data);
        this.context=context;
        this.data=data;
    }

    @Override
    protected void convert(BaseViewHolder holder, final NoticeChoiceTea item) {
        ImageView choice= (ImageView) holder.getView().findViewById(R.id.item_school_notice_choice_tea_choice);
        ImageView sex= (ImageView) holder.getView().findViewById(R.id.item_school_notice_choice_tea_sex);
        TextView name= (TextView) holder.getView().findViewById(R.id.item_school_notice_choice_tea_name);
        TextView phone= (TextView) holder.getView().findViewById(R.id.item_school_notice_choice_tea_phone);
        TextView type= (TextView) holder.getView().findViewById(R.id.item_school_notice_choice_tea_carType);
        //
        if(item.isClick()){
            choice.setImageResource(R.mipmap.student_test_choice1);
        }else {
            choice.setImageResource(R.mipmap.student_test_choice);
        }
        if(item.getSex()!=null){
            if(item.getSex().equals("男")){
                sex.setImageResource(R.mipmap.boy);
            }else {
                sex.setImageResource(R.mipmap.girl);
            }
        }
        name.setText(item.getName());
        phone.setText(item.getMobile());
        type.setText("准教车型："+item.getTeachpermitted());
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
