package com.kangzhan.student.CompayManage.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.holenzhou.pullrecyclerview.BaseRecyclerAdapter;
import com.holenzhou.pullrecyclerview.BaseViewHolder;
import com.kangzhan.student.CompayManage.Bean.SelfRegSchoM;
import com.kangzhan.student.CompayManage.SelfRegistManage.ShowSchoolDetailActivity;
import com.kangzhan.student.CompayManage.SelfRegistManage.ShowStuDetailActivity;
import com.kangzhan.student.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kangzhan011 on 2017/7/25.
 */

public class SelfRegSchoAdapter extends BaseRecyclerAdapter<SelfRegSchoM> {
    private Context context;
    private ArrayList<SelfRegSchoM> data;
    public SelfRegSchoAdapter(Context context, int layoutResId, ArrayList<SelfRegSchoM> data) {
        super(context, layoutResId, data);
        this.context=context;
        this.data=data;
    }

    @Override
    protected void convert(BaseViewHolder holder, final SelfRegSchoM item) {
        RelativeLayout bg= (RelativeLayout) holder.getView().findViewById(R.id.compay_selfReg_stu_bg);
        ImageView choice= (ImageView) holder.getView().findViewById(R.id.compay_selfReg_stu_choice);
        TextView name= (TextView) holder.getView().findViewById(R.id.compay_selfReg_stu_name);
        TextView status= (TextView) holder.getView().findViewById(R.id.compay_self_regist_status);
        TextView clerk= (TextView) holder.getView().findViewById(R.id.compay_selfReg_stu_clerk);
        TextView phone= (TextView) holder.getView().findViewById(R.id.compay_selfReg_stu_phone);
        //
        if(item.isShow()){
            bg.setVisibility(View.VISIBLE);
        }else {
            bg.setVisibility(View.GONE);
        }
        if(item.isClick()){
            choice.setImageResource(R.mipmap.student_test_choice1);
        }else {
            choice.setImageResource(R.mipmap.student_test_choice);
        }
        name.setText(item.getName());
        status.setText(item.getProcess_flag());
        clerk.setText("业务员："+item.getClerk_id());
        phone.setText("联系电话："+item.getMobile());
        holder.getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(item.isShow()){
                    //
                    if(item.isClick()){
                        item.setClick(false);
                    }else {
                        item.setClick(true);
                    }
                    notifyDataSetChanged();
                }else {
                    //查看详情
                    Intent detail=new Intent(context, ShowSchoolDetailActivity.class);
                    detail.putExtra("Id",item.getId());
                    context.startActivity(detail);
                }
            }
        });
    }
}
