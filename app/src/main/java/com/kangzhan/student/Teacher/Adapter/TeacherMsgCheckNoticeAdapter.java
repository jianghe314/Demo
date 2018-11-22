package com.kangzhan.student.Teacher.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.holenzhou.pullrecyclerview.BaseRecyclerAdapter;
import com.holenzhou.pullrecyclerview.BaseViewHolder;
import com.kangzhan.student.R;
import com.kangzhan.student.Teacher.News.NoticeDetailActivity;
import com.kangzhan.student.Teacher.bean.TeacherMsgCheckNotice;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kangzhan011 on 2017/4/26.
 */

public class TeacherMsgCheckNoticeAdapter extends BaseRecyclerAdapter<TeacherMsgCheckNotice> {
    private Context context;
    private ArrayList<TeacherMsgCheckNotice> data;
    public TeacherMsgCheckNoticeAdapter(Context context, int layoutResId, ArrayList<TeacherMsgCheckNotice> data) {
        super(context, layoutResId, data);
        this.context=context;
        this.data=data;
    }

    @Override
    protected void convert(BaseViewHolder holder, final TeacherMsgCheckNotice item) {
        TextView title= (TextView) holder.getView().findViewById(R.id.teacher_news_checkNotice_school);
        TextView tag= (TextView) holder.getView().findViewById(R.id.teacher_news_checkNotice_read);
        TextView time= (TextView) holder.getView().findViewById(R.id.teacher_news_checkNotice_time);
        TextView tip= (TextView) holder.getView().findViewById(R.id.teacher_news_checkNotice_title);
        final TextView content= (TextView) holder.getView().findViewById(R.id.teacher_news_checkNotice_content);
        //
        title.setText(item.getSender_id());
        if(item.getMsg().equals("已读")){
            tag.setText("已读");
            tag.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            tag.setBackgroundResource(R.drawable.text_background2);
        }else if(item.getMsg().equals("未读")){
            tag.setText("未读");
            tag.setTextColor(content.getResources().getColor(R.color.textColor_orange));
            tag.setBackgroundResource(R.drawable.text_background_orange);
        }
        time.setText(item.getSend_time());
        tip.setText(item.getPhone_summary());
        content.setText(item.getPhone_content());
        holder.getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent detail=new Intent(context, NoticeDetailActivity.class);
                detail.putExtra("id",item.getId());
                item.setMsg("已读");
                notifyDataSetChanged();
                context.startActivity(detail);
            }
        });
    }
}
