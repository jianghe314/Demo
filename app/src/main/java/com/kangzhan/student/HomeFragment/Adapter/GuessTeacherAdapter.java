package com.kangzhan.student.HomeFragment.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kangzhan.student.HomeFragment.Bean.TeacherList;
import com.kangzhan.student.HomeFragment.activities.HomeTeacherDetailActivity;
import com.kangzhan.student.R;
import com.kangzhan.student.mUI.RemarkStar;

import java.text.DecimalFormat;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by kangzhan011 on 2017/11/23.
 */

public class GuessTeacherAdapter extends RecyclerView.Adapter<GuessTeacherAdapter.guessTeacher>{

    private Context context;
    private ArrayList<TeacherList> data;
    private LayoutInflater inflater;
    public GuessTeacherAdapter(Context context, ArrayList<TeacherList> data) {
        this.context=context;
        this.data=data;
        inflater=LayoutInflater.from(context);
    }

    @Override
    public guessTeacher onCreateViewHolder(ViewGroup parent, int viewType) {
        View view =inflater.inflate(R.layout.item_home_findt_layout,parent,false);
        return new guessTeacher(view);
    }

    @Override
    public void onBindViewHolder(guessTeacher holder, int position) {
        final TeacherList item=data.get(position);
        holder.name.setText(item.getName());
        holder.star.setRemarkStar(Integer.valueOf(item.getScore_eval()));
        DecimalFormat format=new DecimalFormat("0.00");
        float dis=Float.valueOf(item.getDistance());
        holder.distance.setText("距离"+format.format(dis)+"KM");
        holder.school.setText(item.getShortname());
        Glide.with(context).load(item.getOss_photo()).error(R.drawable.home_head).placeholder(R.drawable.home_head).crossFade().into(holder.head);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent detail=new Intent(context, HomeTeacherDetailActivity.class);
                detail.putExtra("Id",item.getId());
                context.startActivity(detail);
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class guessTeacher extends RecyclerView.ViewHolder{
        CircleImageView head;
        TextView name;
        RemarkStar star;
        TextView distance;
        TextView school;
        public guessTeacher(View itemView) {
            super(itemView);
            head= (CircleImageView) itemView.findViewById(R.id.item_home_findt_head);
            name= (TextView) itemView.findViewById(R.id.item_home_findt_name);
            star= (RemarkStar) itemView.findViewById(R.id.item_home_findt_remarkStar);
            distance= (TextView) itemView.findViewById(R.id.item_home_findt_distance);
            school= (TextView) itemView.findViewById(R.id.item_home_findt_from_school);
        }
    }
}
