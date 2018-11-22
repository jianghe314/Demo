package com.kangzhan.student.HomeFragment.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kangzhan.student.HomeFragment.Bean.SchoolList;
import com.kangzhan.student.HomeFragment.activities.HomeSchoolDetailActivity;
import com.kangzhan.student.R;
import com.kangzhan.student.mUI.RemarkStar;

import java.util.ArrayList;

/**
 * Created by kangzhan011 on 2017/11/23.
 */

public class GuessSchoolAdapter extends RecyclerView.Adapter<GuessSchoolAdapter.guessSchool> {

    private Context context;
    private ArrayList<SchoolList> data;
    private LayoutInflater inflater;
    public GuessSchoolAdapter(Context context, ArrayList<SchoolList> data) {
        this.context=context;
        this.data=data;
        inflater=LayoutInflater.from(context);
    }

    @Override
    public guessSchool onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.item_home_finds_layout,parent,false);
        return new guessSchool(view);
    }

    @Override
    public void onBindViewHolder(guessSchool holder, int position) {
        final SchoolList item=data.get(position);
        holder.name.setText(item.getInsti_name());
        holder.star.setRemarkStar(Integer.valueOf(item.getScore_eval()));
        holder.place.setText("共有"+item.getRegion_count()+"训练场");
        Glide.with(context).load(item.getLogo()).error(R.drawable.home_school).placeholder(R.drawable.home_school).crossFade().into(holder.iv);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent detail=new Intent(context, HomeSchoolDetailActivity.class);
                detail.putExtra("Id",item.getInst_id());
                context.startActivity(detail);
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class guessSchool extends RecyclerView.ViewHolder{
        ImageView iv;
        TextView name;
        RemarkStar star;
        TextView place;
        public guessSchool(View itemView) {
            super(itemView);
            iv= (ImageView) itemView.findViewById(R.id.item_home_finds_iv);
            name= (TextView) itemView.findViewById(R.id.item_home_finds_name_tv);
            star= (RemarkStar) itemView.findViewById(R.id.item_home_finds_remarkStar);
            place= (TextView) itemView.findViewById(R.id.item_home_finds_address_tv);
        }
    }
}
