package com.kangzhan.student.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.holenzhou.pullrecyclerview.BaseRecyclerAdapter;
import com.holenzhou.pullrecyclerview.BaseViewHolder;
import com.kangzhan.student.Advisetment.RecommendDetailActivity;
import com.kangzhan.student.R;
import com.kangzhan.student.RecommendBean.RecommendSchool;

import java.util.ArrayList;

/**
 * Created by kangzhan011 on 2017/5/22.
 */

public class RecommendSchoolAdapter extends BaseRecyclerAdapter<RecommendSchool> {
    private Context context;
    private ArrayList<RecommendSchool> data;
    private String cid;
    public RecommendSchoolAdapter(Context context, int layoutResId, ArrayList<RecommendSchool> data,String cid) {
        super(context, layoutResId, data);
        this.context=context;
        this.data=data;
        this.cid=cid;
    }

    @Override
    protected void convert(BaseViewHolder holder, final RecommendSchool item) {
        ImageView banner= (ImageView) holder.getView().findViewById(R.id.item_recomend_school_iv);
        TextView name= (TextView) holder.getView().findViewById(R.id.item_recomend_school_name);
        TextView address= (TextView) holder.getView().findViewById(R.id.item_recomend_school_location);
        TextView phone= (TextView) holder.getView().findViewById(R.id.item_recomend_school_phoneNum);
        //
        Glide.with(context).load("http://app.kzxueche.com/"+item.getPhoto()).error(R.drawable.error).crossFade().into(banner);
        name.setText(item.getName());
        address.setText(item.getAddress());
        phone.setText(item.getTelephone());
        holder.getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent detail=new Intent(context, RecommendDetailActivity.class);
                detail.putExtra("url","schoolDec.html?"+"id="+item.getSchoolrecommend_id()+"&"+"cid="+cid);
                context.startActivity(detail);
            }
        });
    }
}
