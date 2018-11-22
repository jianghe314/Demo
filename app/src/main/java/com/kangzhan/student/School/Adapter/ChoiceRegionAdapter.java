package com.kangzhan.student.School.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.holenzhou.pullrecyclerview.BaseRecyclerAdapter;
import com.holenzhou.pullrecyclerview.BaseViewHolder;
import com.kangzhan.student.R;
import com.kangzhan.student.School.Bean.ChoiceRegion;
import com.kangzhan.student.School.Edu.EduCarManage_Add;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kangzhan011 on 2017/7/3.
 */

public class ChoiceRegionAdapter extends BaseRecyclerAdapter<ChoiceRegion> {
    private ArrayList<ChoiceRegion> data;
    private Context context;
    public ChoiceRegionAdapter(Context context, int layoutResId, ArrayList<ChoiceRegion> data) {
        super(context, layoutResId, data);
        this.context=context;
        this.data=data;
    }

    @Override
    protected void convert(BaseViewHolder holder, final ChoiceRegion item) {
        ImageView choice= (ImageView) holder.getView().findViewById(R.id.item_choice_region_choice);
        TextView name= (TextView) holder.getView().findViewById(R.id.item_choice_region_name);
        TextView address= (TextView) holder.getView().findViewById(R.id.item_choice_region_address);
        //
        name.setText("训练场名称："+item.getName());
        address.setText("训练场地址："+item.getAddress());
        //
        if(item.isClick()){
            choice.setImageResource(R.mipmap.student_test_choice1);
        }else {
            choice.setImageResource(R.mipmap.student_test_choice);
        }
        holder.getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < data.size(); i++) {
                    data.get(i).setClick(false);
                }
                item.setClick(true);
                notifyDataSetChanged();
            }
        });


    }
}
