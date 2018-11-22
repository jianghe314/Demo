package com.kangzhan.student.HomeFragment.Adapter;

import android.content.Context;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.holenzhou.pullrecyclerview.BaseRecyclerAdapter;
import com.holenzhou.pullrecyclerview.BaseViewHolder;
import com.kangzhan.student.HomeFragment.Bean.SchoolDetail_Remark;
import com.kangzhan.student.R;
import com.kangzhan.student.mUI.RemarkStar;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by kangzhan011 on 2017/11/29.
 */

public class MoreRemarkAdapter extends BaseRecyclerAdapter<SchoolDetail_Remark> {
    private Context context;
    private ArrayList<SchoolDetail_Remark> data;
    public MoreRemarkAdapter(Context context, int layoutResId, ArrayList<SchoolDetail_Remark> data) {
        super(context, layoutResId, data);
        this.context=context;
        this.data=data;
    }

    @Override
    protected void convert(BaseViewHolder holder, SchoolDetail_Remark item) {
        CircleImageView header= (CircleImageView) holder.getView().findViewById(R.id.item_home_school_detail_list_head);
        TextView name= (TextView) holder.getView().findViewById(R.id.item_home_school_detail_list_name);
        TextView time= (TextView) holder.getView().findViewById(R.id.item_home_school_detail_list_time);
        RemarkStar star= (RemarkStar) holder.getView().findViewById(R.id.item_home_school_detail_list_star);
        TextView content= (TextView) holder.getView().findViewById(R.id.item_home_school_detail_list_content);

        name.setText(item.getStu_name());
        time.setText(item.getEvaluatetime());
        star.setRemarkStar(Integer.valueOf(item.getOverall_id()));
        content.setText(item.getTeachlevel());

        Glide.with(context).load(item.getStu_oss_photo()).placeholder(R.mipmap.student_head).error(R.mipmap.student_head).crossFade().into(header);
    }
}
