package com.kangzhan.student.HomeFragment.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.holenzhou.pullrecyclerview.BaseRecyclerAdapter;
import com.holenzhou.pullrecyclerview.BaseViewHolder;
import com.kangzhan.student.HomeFragment.Bean.SchoolList;
import com.kangzhan.student.HomeFragment.activities.HomeSchoolDetailActivity;
import com.kangzhan.student.R;
import com.kangzhan.student.mUI.RemarkStar;

import java.util.List;

/**
 * Created by kangzhan011 on 2017/11/22.
 */

public class HomeSchoolFragmentAdapter extends BaseRecyclerAdapter<SchoolList> {
    private List<SchoolList> data;
    private Context context;
    public HomeSchoolFragmentAdapter(Context context, int layoutResId, List<SchoolList> data) {
        super(context, layoutResId, data);
        this.data=data;
        this.context=context;
    }

    @Override
    protected void convert(BaseViewHolder holder, final SchoolList item) {
        ImageView place= (ImageView) holder.getView().findViewById(R.id.item_home_finds_iv);
        TextView name= (TextView) holder.getView().findViewById(R.id.item_home_finds_name_tv);
        RemarkStar star= (RemarkStar) holder.getView().findViewById(R.id.item_home_finds_remarkStar);
        TextView trainPlace= (TextView) holder.getView().findViewById(R.id.item_home_finds_address_tv);
        name.setText(item.getInsti_name());
        star.setRemarkStar(Integer.valueOf(item.getScore_eval()));
        trainPlace.setText("共有"+item.getRegion_count()+"训练场");
        Glide.with(context).load(item.getLogo()).error(R.drawable.home_school).placeholder(R.drawable.home_school).crossFade().into(place);
        holder.getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent detail=new Intent(context, HomeSchoolDetailActivity.class);
                detail.putExtra("Id",item.getInst_id());
                context.startActivity(detail);
            }
        });

    }

}
