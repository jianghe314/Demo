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
import com.kangzhan.student.RecommendBean.RecommendNews;

import java.util.ArrayList;

/**
 * Created by kangzhan011 on 2017/5/23.
 */

public class RecommendNewsAdapter extends BaseRecyclerAdapter<RecommendNews> {
    private Context context;
    private ArrayList<RecommendNews> data;
    private String cid;
    public RecommendNewsAdapter(Context context, int layoutResId, ArrayList<RecommendNews> data,String cid) {
        super(context, layoutResId, data);
        this.context=context;
        this.data=data;
        this.cid=cid;
    }

    @Override
    protected void convert(BaseViewHolder holder, final RecommendNews item) {
        ImageView iv= (ImageView) holder.getView().findViewById(R.id.item_recommend_news_iv);
        TextView title= (TextView) holder.getView().findViewById(R.id.item_recommend_news_title);
        TextView time= (TextView) holder.getView().findViewById(R.id.item_recommend_news_time);
        TextView content= (TextView) holder.getView().findViewById(R.id.item_recommend_news_content);
        //
        Glide.with(context).load("http://app.kzxueche.com/"+item.getInfo_image()).into(iv);
        title.setText(item.getInfo_title());
        time.setText(item.getPublish_time());
        content.setText(item.getInfo_intro());
        holder.getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent detail=new Intent(context, RecommendDetailActivity.class);
                detail.putExtra("url","student_consultDec.html?"+"id="+item.getConsult_id()+"&"+"cid="+cid);
                context.startActivity(detail);
            }
        });

    }
}
