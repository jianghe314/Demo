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
import com.kangzhan.student.CompayManage.Bean.StaffManage;
import com.kangzhan.student.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kangzhan011 on 2017/7/28.
 */

public class StaffAdapter extends BaseRecyclerAdapter<StaffManage> {
    private Context context;
    private ArrayList<StaffManage> data;
    public StaffAdapter(Context context, int layoutResId, ArrayList<StaffManage> data) {
        super(context, layoutResId, data);
        this.context=context;
        this.data=data;
    }

    @Override
    protected void convert(BaseViewHolder holder, final StaffManage item) {
        RelativeLayout container= (RelativeLayout) holder.getView().findViewById(R.id.compay_slide_manage_container);
        ImageView choice= (ImageView) holder.getView().findViewById(R.id.compay_slide_manage_choice);
        TextView name= (TextView) holder.getView().findViewById(R.id.compay_slide_manage_name);
        TextView posi= (TextView) holder.getView().findViewById(R.id.compay_slide_manage_pos);
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
        posi.setText(item.getRole_id());
        holder.getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(item.isShow()){
                    for (int i = 0; i < mData.size(); i++) {
                        mData.get(i).setClick(false);
                    }
                    item.setClick(true);
                    notifyDataSetChanged();
                }else {
                    BottomSheetDialog dialog=new BottomSheetDialog(context);
                    View view= LayoutInflater.from(context).inflate(R.layout.item_compay_staff_menu,null);
                    TextView name= (TextView) view.findViewById(R.id.item_compay_staff_name);
                    TextView phone= (TextView) view.findViewById(R.id.item_compay_staff_phone);
                    TextView Id= (TextView) view.findViewById(R.id.item_compay_staff_id);
                    TextView qq= (TextView) view.findViewById(R.id.item_compay_staff_qq);
                    TextView wechat= (TextView) view.findViewById(R.id.item_compay_staff_wechat);
                    TextView role= (TextView) view.findViewById(R.id.item_compay_staff_role);
                    TextView status= (TextView) view.findViewById(R.id.item_compay_staff_status);
                    name.setText(item.getReal_name());
                    phone.setText(item.getPhone());
                    Id.setText(item.getIdcard());
                    qq.setText(item.getQqnum());
                    wechat.setText(item.getWechatnum());
                    role.setText(item.getRole_id());
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
