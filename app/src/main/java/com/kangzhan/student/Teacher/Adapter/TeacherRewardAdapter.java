package com.kangzhan.student.Teacher.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.BaseAdapter;
import android.widget.TextView;

import com.holenzhou.pullrecyclerview.BaseRecyclerAdapter;
import com.holenzhou.pullrecyclerview.BaseViewHolder;
import com.kangzhan.student.R;
import com.kangzhan.student.Teacher.bean.TeacherReward;
import com.kangzhan.student.Teacher.person_data.RewardDetail;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kangzhan011 on 2017/4/10.
 */

public class TeacherRewardAdapter extends BaseRecyclerAdapter<TeacherReward> {
    private Context context;
    private ArrayList<TeacherReward> data;
    public TeacherRewardAdapter(Context context, int layoutResId, ArrayList<TeacherReward> data) {
        super(context, layoutResId, data);
        this.data=data;
        this.context=context;
    }

    @Override
    protected void convert(BaseViewHolder holder, final TeacherReward item) {
        TextView year= (TextView) holder.getView().findViewById(R.id.teacher_reward_year_month);
        TextView keer= (TextView) holder.getView().findViewById(R.id.teacher_reward_ke2);
        TextView kesan= (TextView) holder.getView().findViewById(R.id.teacher_reward_ke3);
        TextView sum= (TextView) holder.getView().findViewById(R.id.teacher_reward_sum);
        //
        year.setText(item.getBonus_month());
        keer.setText(item.getKeer());
        kesan.setText(item.getKesan());
        sum.setText(item.getAmount());
        //点击查看详情
        holder.getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent detail=new Intent(context, RewardDetail.class);
                detail.putExtra("id",item.getId());
                context.startActivity(detail);
            }
        });
    }
}
