package com.kangzhan.student.CompayManage.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.holenzhou.pullrecyclerview.BaseRecyclerAdapter;
import com.holenzhou.pullrecyclerview.BaseViewHolder;
import com.kangzhan.student.CompayManage.Bean.SelfRegStuM;
import com.kangzhan.student.CompayManage.SelfRegistManage.ShowStuDetailActivity;
import com.kangzhan.student.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kangzhan011 on 2017/7/24.
 */

public class SelfRegStuAdapter extends BaseRecyclerAdapter<SelfRegStuM>{
    private ArrayList<SelfRegStuM> data;
    private Context context;
    public SelfRegStuAdapter(Context context, int layoutResId, ArrayList<SelfRegStuM> data) {
        super(context, layoutResId, data);
        this.data=data;
        this.context=context;
    }

    @Override
    protected void convert(BaseViewHolder holder, final SelfRegStuM item) {
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
        name.setText(item.getReal_name());
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
                    Intent detail=new Intent(context, ShowStuDetailActivity.class);
                    detail.putExtra("stuId",item.getId());
                    context.startActivity(detail);
                }
            }
        });
    }
}
