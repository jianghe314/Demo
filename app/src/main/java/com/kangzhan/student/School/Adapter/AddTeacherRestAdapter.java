package com.kangzhan.student.School.Adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.holenzhou.pullrecyclerview.BaseRecyclerAdapter;
import com.holenzhou.pullrecyclerview.BaseViewHolder;
import com.kangzhan.student.R;
import com.kangzhan.student.School.Bean.AllocateTeacher;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kangzhan011 on 2017/6/30.
 */

public class AddTeacherRestAdapter extends BaseRecyclerAdapter<AllocateTeacher> {
    private Context context;
    private ArrayList<AllocateTeacher> data;
    public AddTeacherRestAdapter(Context context, int layoutResId, ArrayList<AllocateTeacher> data) {
        super(context, layoutResId, data);
        this.context=context;
        this.data=data;
    }

    @Override
    protected void convert(BaseViewHolder holder, final AllocateTeacher item) {
        ImageView choice= (ImageView) holder.getView().findViewById(R.id.item_stum_allo_choice);
        ImageView sex= (ImageView) holder.getView().findViewById(R.id.item_stum_allo_sex);
        TextView name= (TextView) holder.getView().findViewById(R.id.item_stum_allo_name);
        TextView carType= (TextView) holder.getView().findViewById(R.id.item_stum_allo_carType);
        TextView phone= (TextView) holder.getView().findViewById(R.id.item_stum_allo_phone);
        TextView StMnum= (TextView) holder.getView().findViewById(R.id.item_stum_allo_hasStu);
        TextView teacherStar= (TextView) holder.getView().findViewById(R.id.item_stum_allo_star);
        if(item.getSex()!=null){
            if(item.getSex().equals("男")){
                sex.setImageResource(R.mipmap.boy);
            }else {
                sex.setImageResource(R.mipmap.girl);
            }
        }
        if(item.isClick()){
            choice.setImageResource(R.mipmap.student_test_choice1);
        }else {
            choice.setImageResource(R.mipmap.student_test_choice);
        }
        StMnum.setText("学员数："+item.getStudent_count());
        teacherStar.setText("星级："+item.getStarLevel());
        name.setText(item.getName());
        carType.setText("准教车型："+item.getTeachpermitted());
        phone.setText("电话："+item.getMobile());
        //单选
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
