package com.kangzhan.student.HomeFragment.Adapter;

import android.content.Context;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.holenzhou.pullrecyclerview.BaseRecyclerAdapter;
import com.holenzhou.pullrecyclerview.BaseViewHolder;
import com.kangzhan.student.HomeFragment.Bean.SchoolDetail_Que;
import com.kangzhan.student.R;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by kangzhan011 on 2017/11/29.
 */

public class MoreQuesAdapter extends BaseRecyclerAdapter<SchoolDetail_Que> {

    private Context context;
    private ArrayList<SchoolDetail_Que> data;
    public MoreQuesAdapter(Context context, int layoutResId, ArrayList<SchoolDetail_Que> data) {
        super(context, layoutResId, data);
        this.context=context;
        this.data=data;
    }

    @Override
    protected void convert(BaseViewHolder holder, SchoolDetail_Que item) {
        CircleImageView header= (CircleImageView) holder.getView().findViewById(R.id.item_home_school_detail_question_list_head);
        TextView name= (TextView) holder.getView().findViewById(R.id.item_home_school_detail_question_list_name);
        TextView time= (TextView) holder.getView().findViewById(R.id.item_home_school_detail_question_list_time);
        TextView ques= (TextView) holder.getView().findViewById(R.id.item_home_school_detail_question_list_ques);
        TextView replyContent= (TextView) holder.getView().findViewById(R.id.item_home_school_detail_question_list_content);

        name.setText(item.getStu_name()+" 问:");
        time.setText(item.getCreate_time());
        ques.setText(item.getStu_content());
        replyContent.setText("驾校回复："+item.getReply_content());
        Glide.with(context).load(item.getStu_oss_photo()).placeholder(R.mipmap.student_head).error(R.mipmap.student_head).crossFade().into(header);
    }
}
