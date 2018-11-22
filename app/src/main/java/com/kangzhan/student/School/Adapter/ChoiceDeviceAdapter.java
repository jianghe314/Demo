package com.kangzhan.student.School.Adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.holenzhou.pullrecyclerview.BaseRecyclerAdapter;
import com.holenzhou.pullrecyclerview.BaseViewHolder;
import com.kangzhan.student.R;
import com.kangzhan.student.School.Bean.ChoiceDevice;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kangzhan011 on 2017/8/8.
 */

public class ChoiceDeviceAdapter extends BaseRecyclerAdapter<ChoiceDevice>{
    private ArrayList<ChoiceDevice> data;
    private Context context;
    public ChoiceDeviceAdapter(Context context, int layoutResId, ArrayList<ChoiceDevice> data) {
        super(context, layoutResId, data);
        this.data=data;
        this.context=context;
    }

    @Override
    protected void convert(BaseViewHolder holder, final ChoiceDevice item) {
        ImageView choice= (ImageView) holder.getView().findViewById(R.id.item_school_choice_device_choice);
        TextView name= (TextView) holder.getView().findViewById(R.id.item_school_choice_device_name);
        TextView code= (TextView) holder.getView().findViewById(R.id.item_school_choice_device_code);
        if(item.isClick()){
            choice.setImageResource(R.mipmap.student_test_choice1);
        }else {
            choice.setImageResource(R.mipmap.student_test_choice);
        }
        name.setText("名称："+item.getTermtype());
        code.setText("编号："+item.getDevnum());
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
