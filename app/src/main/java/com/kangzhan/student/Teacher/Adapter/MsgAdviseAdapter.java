package com.kangzhan.student.Teacher.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.holenzhou.pullrecyclerview.BaseRecyclerAdapter;
import com.holenzhou.pullrecyclerview.BaseViewHolder;
import com.kangzhan.student.R;
import com.kangzhan.student.Teacher.News.AdviseDetailActivity;
import com.kangzhan.student.Teacher.bean.TeacherNewsAdvise;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kangzhan011 on 2017/4/27.
 */

public class MsgAdviseAdapter extends BaseRecyclerAdapter<TeacherNewsAdvise> {
    private Context context;
    private ArrayList<TeacherNewsAdvise> data;
    public MsgAdviseAdapter(Context context, int layoutResId, ArrayList<TeacherNewsAdvise> data) {
        super(context, layoutResId, data);
        this.context=context;
        this.data=data;
    }

    @Override
    protected void convert(BaseViewHolder holder, final TeacherNewsAdvise item) {
        TextView time= (TextView) holder.getView().findViewById(R.id.teacher_news_advise_date);
        final TextView content= (TextView) holder.getView().findViewById(R.id.teacher_news_advise_content);
        TextView stastu= (TextView) holder.getView().findViewById(R.id.teacher_news_advise_stastu);
        RelativeLayout r1= (RelativeLayout) holder.getView().findViewById(R.id.teacher_news_advise_r1);
        //
        time.setText(item.getCreate_time());
        content.setText(item.getPhone_summary());
        stastu.setText(item.getStatus_name());
        if(item.getStatus_name().equals("待处理")){
            r1.setBackgroundColor(content.getResources().getColor(R.color.Not_read_color));
        }else if(item.getStatus_name().equals("已处理")){
            r1.setBackgroundColor(content.getResources().getColor(R.color.colorPrimary));
        }else if(item.getStatus_name().equals("处理中")){
            r1.setBackgroundColor(content.getResources().getColor(R.color.text_background_color_aqua));
        }
        //
        holder.getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent detail=new Intent(context, AdviseDetailActivity.class);
                detail.putExtra("id",item.getId());
                context.startActivity(detail);
            }
        });

    }
}
