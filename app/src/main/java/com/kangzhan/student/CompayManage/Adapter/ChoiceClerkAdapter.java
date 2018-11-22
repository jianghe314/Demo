package com.kangzhan.student.CompayManage.Adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.holenzhou.pullrecyclerview.BaseRecyclerAdapter;
import com.holenzhou.pullrecyclerview.BaseViewHolder;
import com.kangzhan.student.CompayManage.Bean.ChoiceClerk;
import com.kangzhan.student.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kangzhan011 on 2017/7/24.
 */

public class ChoiceClerkAdapter extends BaseRecyclerAdapter<ChoiceClerk> {
    private Context context;
    private ArrayList<ChoiceClerk> data;
    public ChoiceClerkAdapter(Context context, int layoutResId, ArrayList<ChoiceClerk> data) {
        super(context, layoutResId, data);
        this.context=context;
        this.data=data;
    }

    @Override
    protected void convert(BaseViewHolder holder, final ChoiceClerk item) {
        ImageView choice= (ImageView) holder.getView().findViewById(R.id.compay_choice_clerk_choice);
        TextView name= (TextView) holder.getView().findViewById(R.id.compay_choice_clerk_name);
        if(item.isClick()){
            choice.setImageResource(R.mipmap.student_test_choice1);
        }else {
            choice.setImageResource(R.mipmap.student_test_choice);
        }
        name.setText("业务员："+item.getReal_name());
        holder.getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //单选
                for (int i = 0; i < data.size(); i++) {
                    data.get(i).setClick(false);
                }
                item.setClick(true);
                notifyDataSetChanged();
            }
        });

    }
}
