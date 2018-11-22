package com.kangzhan.student.HomeFragment.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kangzhan.student.HomeFragment.Bean.SchoolDetail_Remark;
import com.kangzhan.student.R;
import com.kangzhan.student.mUI.RemarkStar;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by kangzhan011 on 2017/11/17.
 */

public class SchoolDetailRemarkAdapter extends RecyclerView.Adapter<SchoolDetailRemarkAdapter.schoolDetailRemark> {
    private ArrayList<SchoolDetail_Remark> data;
    private Context context;
    private LayoutInflater inflater;
    public SchoolDetailRemarkAdapter(Context context, ArrayList<SchoolDetail_Remark> data) {
        this.context=context;
        this.data=data;
        inflater=LayoutInflater.from(context);
    }

    @Override
    public schoolDetailRemark onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.home_school_detail_remark_list_layout,parent,false);
        return new schoolDetailRemark(view);
    }

    @Override
    public void onBindViewHolder(schoolDetailRemark holder, int position) {
        SchoolDetail_Remark remark=data.get(position);
        holder.name.setText(remark.getStu_name());
        holder.time.setText(remark.getEvaluatetime());
        holder.star.setRemarkStar(Integer.valueOf(remark.getOverall_id()));
        holder.content.setText(remark.getTeachlevel());

        Glide.with(context).load(remark.getStu_oss_photo()).placeholder(R.mipmap.student_head).error(R.mipmap.student_head).crossFade().into(holder.header);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class schoolDetailRemark extends RecyclerView.ViewHolder{
       CircleImageView header;
       TextView name;
       RemarkStar star;
       TextView time;
       TextView content;
        public schoolDetailRemark(View itemView) {
            super(itemView);
            header= (CircleImageView) itemView.findViewById(R.id.item_home_school_detail_list_head);
            name= (TextView) itemView.findViewById(R.id.item_home_school_detail_list_name);
            time= (TextView) itemView.findViewById(R.id.item_home_school_detail_list_time);
            star= (RemarkStar) itemView.findViewById(R.id.item_home_school_detail_list_star);
            content= (TextView) itemView.findViewById(R.id.item_home_school_detail_list_content);
        }
    }
}
