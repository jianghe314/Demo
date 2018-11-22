package com.kangzhan.student.Adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.holenzhou.pullrecyclerview.BaseRecyclerAdapter;
import com.holenzhou.pullrecyclerview.BaseViewHolder;
import com.kangzhan.student.R;
import com.kangzhan.student.RecommendBean.RecommendTeacher;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kangzhan011 on 2017/5/23.
 */

public class RecommendTeacherAdapter extends BaseRecyclerAdapter<RecommendTeacher> {
    private Context context;
    private ArrayList<RecommendTeacher> data;
    public RecommendTeacherAdapter(Context context, int layoutResId, ArrayList<RecommendTeacher> data) {
        super(context, layoutResId, data);
        this.context=context;
        this.data=data;
    }

    @Override
    protected void convert(BaseViewHolder holder, RecommendTeacher item) {
        ImageView iv= (ImageView) holder.getView().findViewById(R.id.item_recommend_teacher_iv);
        TextView name= (TextView) holder.getView().findViewById(R.id.item_recommend_teacher_name);
        TextView years= (TextView) holder.getView().findViewById(R.id.item_recommend_teacher_years);
        TextView address= (TextView) holder.getView().findViewById(R.id.item_recommend_teacher_address);
        TextView content= (TextView) holder.getView().findViewById(R.id.item_recommend_teacher_content);
        //

        Glide.with(context).load("http://app.kzxueche.com/"+item.getPhoto()).error(R.drawable.error).crossFade().into(iv);
        name.setText(item.getName());
        years.setText(item.getDriving_year()+"年教龄");
        address.setText(item.getDriving_school());
        content.setText(item.getIntro());
    }
}
