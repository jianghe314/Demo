package com.kangzhan.student.School.Adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.holenzhou.pullrecyclerview.BaseRecyclerAdapter;
import com.holenzhou.pullrecyclerview.BaseViewHolder;
import com.kangzhan.student.R;
import com.kangzhan.student.School.Bean.ChoiceTeacher;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kangzhan011 on 2017/7/3.
 */

public class ChoiceTeahcerAdapter extends BaseRecyclerAdapter<ChoiceTeacher> {
    private ArrayList<ChoiceTeacher> data;
    private Context context;
    public ChoiceTeahcerAdapter(Context context, int layoutResId, ArrayList<ChoiceTeacher> data) {
        super(context, layoutResId, data);
        this.data=data;
        this.context=context;
    }

    @Override
    protected void convert(BaseViewHolder holder, final ChoiceTeacher item) {
        ImageView choice= (ImageView) holder.getView().findViewById(R.id.item_stum_allo_choice);
        ImageView sex= (ImageView) holder.getView().findViewById(R.id.item_stum_allo_sex);
        TextView name= (TextView) holder.getView().findViewById(R.id.item_stum_allo_name);
        TextView carType= (TextView) holder.getView().findViewById(R.id.item_stum_allo_carType);
        TextView phone= (TextView) holder.getView().findViewById(R.id.item_stum_allo_phone);
        TextView StMnum= (TextView) holder.getView().findViewById(R.id.item_stum_allo_hasStu);
        TextView teacherStar= (TextView) holder.getView().findViewById(R.id.item_stum_allo_star);

        //分配教练这里只能是单选  注意这里少两个参数星级，学员数
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
        name.setText(item.getName());
        StMnum.setText("学员数："+item.getStudent_count());
        teacherStar.setText("星级："+item.getStarLevel());
        carType.setText("准教车型："+item.getTeachpermitted());
        phone.setText("电话："+item.getMobile());
        //多选
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
