package com.kangzhan.student.HomeFragment.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kangzhan.student.HomeFragment.Bean.SchoolDetail_Que;
import com.kangzhan.student.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by kangzhan011 on 2017/11/23.
 */

public class SchoolDetailQueAdapter extends RecyclerView.Adapter<SchoolDetailQueAdapter.schoolDetailQues> {
    private ArrayList<SchoolDetail_Que> data;
    private Context context;
    private LayoutInflater inflater;
    public SchoolDetailQueAdapter(Context context, ArrayList<SchoolDetail_Que> data) {
        this.context=context;
        this.data=data;
        inflater= LayoutInflater.from(context);
    }

    @Override
    public schoolDetailQues onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.home_school_question_list_layout,parent,false);
        return new schoolDetailQues(view);
    }

    @Override
    public void onBindViewHolder(schoolDetailQues holder, int position) {
        SchoolDetail_Que item=data.get(position);
        holder.name.setText(item.getStu_name()+" 问:");
        holder.time.setText(item.getCreate_time());
        holder.ques.setText(item.getStu_content());
        holder.replyContent.setText("驾校回复："+item.getReply_content());
        Glide.with(context).load(item.getStu_oss_photo()).placeholder(R.mipmap.student_head).error(R.mipmap.student_head).crossFade().into(holder.header);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class schoolDetailQues extends RecyclerView.ViewHolder{
        CircleImageView header;
        TextView name;
        TextView time;
        TextView ques;
        TextView replyContent;
        public schoolDetailQues(View itemView) {
            super(itemView);
            header= (CircleImageView) itemView.findViewById(R.id.item_home_school_detail_question_list_head);
            name= (TextView) itemView.findViewById(R.id.item_home_school_detail_question_list_name);
            time= (TextView) itemView.findViewById(R.id.item_home_school_detail_question_list_time);
            ques= (TextView) itemView.findViewById(R.id.item_home_school_detail_question_list_ques);
            replyContent= (TextView) itemView.findViewById(R.id.item_home_school_detail_question_list_content);
        }
    }
}
