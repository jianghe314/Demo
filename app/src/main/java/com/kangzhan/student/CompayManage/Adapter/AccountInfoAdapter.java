package com.kangzhan.student.CompayManage.Adapter;

import android.content.Context;
import android.support.design.widget.BottomSheetDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.holenzhou.pullrecyclerview.BaseRecyclerAdapter;
import com.holenzhou.pullrecyclerview.BaseViewHolder;
import com.kangzhan.student.CompayManage.Bean.SchoolAccontInfo;
import com.kangzhan.student.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kangzhan011 on 2017/7/28.
 */

public class AccountInfoAdapter extends BaseRecyclerAdapter<SchoolAccontInfo> {
    private Context context;
    private ArrayList<SchoolAccontInfo> data;
    public AccountInfoAdapter(Context context, int layoutResId, ArrayList<SchoolAccontInfo> data) {
        super(context, layoutResId, data);
        this.context=context;
        this.data=data;
    }

    @Override
    protected void convert(BaseViewHolder holder, final SchoolAccontInfo item) {
        RelativeLayout container= (RelativeLayout) holder.getView().findViewById(R.id.item_compay_account_info_container);
        ImageView choice= (ImageView) holder.getView().findViewById(R.id.item_compay_account_info_choice);
        TextView name= (TextView) holder.getView().findViewById(R.id.item_compay_account_info_name);
        TextView school= (TextView) holder.getView().findViewById(R.id.item_compay_account_info_school);
        TextView status= (TextView) holder.getView().findViewById(R.id.item_compay_account_info_status);
        //
        if(item.isShow()){
            container.setVisibility(View.VISIBLE);
        }else {
            container.setVisibility(View.GONE);
        }
        if(item.isClick()){
            choice.setImageResource(R.mipmap.student_test_choice1);
        }else {
            choice.setImageResource(R.mipmap.student_test_choice);
        }
        name.setText(item.getReal_name());
        school.setText(item.getInst_id());
        status.setText(item.getStatus());
        holder.getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(item.isShow()){
                    if(item.isClick()){
                        item.setClick(false);
                    }else {
                        item.setClick(true);
                    }
                    notifyDataSetChanged();
                }else {
                    BottomSheetDialog dialog=new BottomSheetDialog(context);
                    View view= LayoutInflater.from(context).inflate(R.layout.item_compay_account_info_detail,null);
                    TextView name= (TextView) view.findViewById(R.id.item_compay_school_detail_name);
                    TextView Id= (TextView) view.findViewById(R.id.item_compay_school_ID);
                    TextView phone= (TextView) view.findViewById(R.id.item_compay_school_phone);
                    TextView record= (TextView) view.findViewById(R.id.item_compay_school_record);
                    TextView school= (TextView) view.findViewById(R.id.item_compay_school_school);
                    TextView status= (TextView) view.findViewById(R.id.item_compay_school_status);
                    name.setText(item.getReal_name());
                    Id.setText(item.getIdcard());
                    phone.setText(item.getPhone());
                    record.setText(item.getRecorder_id());
                    school.setText(item.getInst_id());
                    status.setText(item.getStatus());
                    dialog.setContentView(view);
                    dialog.setCancelable(true);
                    dialog.setCanceledOnTouchOutside(true);
                    dialog.show();
                }
            }
        });
    }
}
