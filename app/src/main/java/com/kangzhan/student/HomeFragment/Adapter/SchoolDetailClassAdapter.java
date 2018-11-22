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
import com.kangzhan.student.HomeFragment.Bean.SchoolDetail;
import com.kangzhan.student.HomeFragment.Bean.SchoolList;
import com.kangzhan.student.HomeFragment.Bean.mClass;
import com.kangzhan.student.HomeFragment.activities.HomeSchoolDetailActivity;
import com.kangzhan.student.HomeFragment.activities.Home_Detail_URl;
import com.kangzhan.student.R;
import com.kangzhan.student.mUI.RemarkStar;

import java.util.ArrayList;

/**
 * Created by kangzhan011 on 2017/11/17.
 */

public class SchoolDetailClassAdapter extends RecyclerView.Adapter<SchoolDetailClassAdapter.schoolClass> {

    private Context context;
    private ArrayList<mClass> data=new ArrayList<>();
    private LayoutInflater inflater;
    public SchoolDetailClassAdapter(Context context, ArrayList<mClass> data) {
        this.context=context;
        this.data=data;
        inflater=LayoutInflater.from(context);
    }

    @Override
    public schoolClass onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.home_item_school_detail_class_layout,parent,false);
        return new schoolClass(view);

    }

    @Override
    public void onBindViewHolder(schoolClass holder, int position) {
        final mClass item=data.get(position);
        holder.className.setText("★"+item.getName());
        holder.classIntroduce.setText(item.getSummary());
        holder.classPrice.setText(item.getPrice());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent detail=new Intent(context, Home_Detail_URl.class);
                detail.putExtra("url","http://www.kzxueche.com/wap/kangzhan_web/modules/sample/views/banbie_dec.html?id="+item.getId());
                context.startActivity(detail);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class schoolClass extends RecyclerView.ViewHolder{
        TextView className;
        TextView classIntroduce;
        TextView classPrice;
        public schoolClass(View itemView) {
            super(itemView);
            className= (TextView) itemView.findViewById(R.id.home_school_detail_className);
            classIntroduce= (TextView) itemView.findViewById(R.id.home_school_detail_classIntroduce);
            classPrice= (TextView) itemView.findViewById(R.id.home_school_detail_classPrice);
        }
    }
}
