package com.kangzhan.student.HomeFragment.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.holenzhou.pullrecyclerview.BaseRecyclerAdapter;
import com.holenzhou.pullrecyclerview.BaseViewHolder;
import com.kangzhan.student.HomeFragment.Bean.TeacherList;
import com.kangzhan.student.HomeFragment.activities.HomeTeacherDetailActivity;
import com.kangzhan.student.R;
import com.kangzhan.student.mUI.RemarkStar;

import java.text.DecimalFormat;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by kangzhan011 on 2017/11/22.
 */

public class HomeTeacherFragmentAdapter extends BaseRecyclerAdapter<TeacherList> {
    private Context context;
    private List<TeacherList> data;
    public HomeTeacherFragmentAdapter(Context context, int layoutResId, List<TeacherList> data) {
        super(context, layoutResId, data);
        this.context=context;
        this.data=data;
    }

    @Override
    protected void convert(BaseViewHolder holder, final TeacherList item) {
        CircleImageView header= (CircleImageView) holder.getView().findViewById(R.id.item_home_findt_head);
        TextView name= (TextView) holder.getView().findViewById(R.id.item_home_findt_name);
        RemarkStar star= (RemarkStar) holder.getView().findViewById(R.id.item_home_findt_remarkStar);
        TextView distance= (TextView) holder.getView().findViewById(R.id.item_home_findt_distance);
        TextView school= (TextView) holder.getView().findViewById(R.id.item_home_findt_from_school);

        name.setText(item.getName());
        star.setRemarkStar(Integer.valueOf(item.getScore_eval()));
        DecimalFormat format=new DecimalFormat("0.00");
        float dis=Float.valueOf(item.getDistance());
        distance.setText("距离"+format.format(dis)+"KM");
        school.setText(item.getShortname());
        Glide.with(context).load(item.getOss_photo()).error(R.drawable.home_head).placeholder(R.drawable.home_head).crossFade().into(header);

        holder.getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent detail=new Intent(context, HomeTeacherDetailActivity.class);
                detail.putExtra("Id",item.getId());
                context.startActivity(detail);
            }
        });
    }
}
