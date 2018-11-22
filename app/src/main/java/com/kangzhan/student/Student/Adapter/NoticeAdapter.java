package com.kangzhan.student.Student.Adapter;

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
import com.kangzhan.student.Student.News.NewsDetailActivity;
import com.kangzhan.student.Student.bean.Notice;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kangzhan011 on 2017/3/28.
 */

public class NoticeAdapter extends BaseRecyclerAdapter<Notice> {
    private Context context;
    private ArrayList<Notice> data;
    public NoticeAdapter(Context context, int layoutResId, ArrayList<Notice> data) {
        super(context, layoutResId, data);
        this.context=context;
        this.data=data;
    }

    @Override
    protected void convert(final BaseViewHolder holder, final Notice item) {
        TextView school= (TextView) holder.getView().findViewById(R.id.notice_school);
        TextView statu= (TextView) holder.getView().findViewById(R.id.notice_state);
        TextView time= (TextView) holder.getView().findViewById(R.id.notice_time);
        TextView title= (TextView) holder.getView().findViewById(R.id.notice_title);
        TextView content= (TextView) holder.getView().findViewById(R.id.notice_content);
        //数据赋值
        school.setText(item.getSender_id());
        time.setText(item.getSend_time());
        title.setText(item.getPhone_summary());
        content.setText(item.getPhone_content());
        if(item.getStatus().equals("20")){
            statu.setText("未读");
            statu.setTextColor(content.getResources().getColor(R.color.textColor_orange));
            statu.setBackgroundResource(R.drawable.text_background_orange);
        }else if(item.getStatus().equals("40")){
            statu.setText("已读");
            statu.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            statu.setBackgroundResource(R.drawable.text_background2);
        }else {
            statu.setText("");
        }
        //查看通知详情
        holder.getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item.setStatus("40");
                notifyItemChanged(holder.getAdapterPosition());
                Intent noticedetail=new Intent(context, NewsDetailActivity.class);
                noticedetail.putExtra("id",item.getId());
                context.startActivity(noticedetail);
            }
        });

    }
}
