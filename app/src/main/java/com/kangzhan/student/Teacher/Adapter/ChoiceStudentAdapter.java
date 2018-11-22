package com.kangzhan.student.Teacher.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.holenzhou.pullrecyclerview.BaseRecyclerAdapter;
import com.holenzhou.pullrecyclerview.BaseViewHolder;
import com.kangzhan.student.R;
import com.kangzhan.student.Teacher.bean.ChoiceStudentList;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by kangzhan011 on 2017/4/27.
 */

public class ChoiceStudentAdapter extends BaseRecyclerAdapter<ChoiceStudentList>{
    private Context context;
    private ArrayList<ChoiceStudentList> data;
    private BaseViewHolder view;
    public ChoiceStudentAdapter(Context context, int layoutResId, ArrayList<ChoiceStudentList> data) {
        super(context, layoutResId, data);
        this.context=context;
        this.data=data;
    }

    @Override
    protected void convert(BaseViewHolder holder, final ChoiceStudentList item) {
        view=holder;
        ImageView choice= (ImageView) holder.getView().findViewById(R.id.item_teacher_news_choice);
        ImageView sex= (ImageView) holder.getView().findViewById(R.id.item_teacher_news_sex);
        TextView name= (TextView) holder.getView().findViewById(R.id.item_teacher_news_name);
        TextView train= (TextView) holder.getView().findViewById(R.id.item_teacher_news_train);
        TextView phone= (TextView) holder.getView().findViewById(R.id.item_teacher_news_phone);
        if(item.isClick()){
            choice.setImageResource(R.mipmap.student_test_choice1);
        }else {
            choice.setImageResource(R.mipmap.student_test_choice);
        }

        //
        if(item.getSex().equals("男")||item.getSex().equals("1")){
            sex.setImageResource(R.mipmap.boy);
        }else {
            sex.setImageResource(R.mipmap.girl);
        }
        name.setText(item.getName());
        train.setText(item.getTraintype());
        phone.setText(item.getPhone());
        //
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
