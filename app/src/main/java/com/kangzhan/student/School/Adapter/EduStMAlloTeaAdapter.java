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
 * Created by kangzhan011 on 2017/6/26.
 */

public class EduStMAlloTeaAdapter extends BaseRecyclerAdapter<AllocateTeacher> {
    private Context context;
    private ArrayList<AllocateTeacher> data;
    public EduStMAlloTeaAdapter(Context context, int layoutResId, ArrayList<AllocateTeacher> data) {
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
        if(item.isClick()){
            choice.setImageResource(R.mipmap.student_test_choice1);
        }else {
            choice.setImageResource(R.mipmap.student_test_choice);
        }
        //分配教练这里只能是单选  注意这里少两个参数星级，学员数
        if(item.getSex()!=null){
            if(item.getSex().equals("男")){
                sex.setImageResource(R.mipmap.boy);
            }else {
                sex.setImageResource(R.mipmap.girl);
            }
        }
        name.setText(item.getName());
        StMnum.setText("学员数："+item.getStudent_count());
        teacherStar.setText("星级："+item.getStarLevel());
        carType.setText("准教车型："+item.getTeachpermitted());
        phone.setText("电话："+item.getMobile());
        //单选
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
