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
import com.kangzhan.student.CompayManage.Bean.InfoCarManage;
import com.kangzhan.student.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kangzhan011 on 2017/7/28.
 */

public class InfoCarManageAdapter extends BaseRecyclerAdapter<InfoCarManage> {
    private Context context;
    private ArrayList<InfoCarManage> data;
    public InfoCarManageAdapter(Context context, int layoutResId, ArrayList<InfoCarManage> data) {
        super(context, layoutResId, data);
        this.context=context;
        this.data=data;
    }

    @Override
    protected void convert( BaseViewHolder holder, final InfoCarManage item) {
        RelativeLayout container= (RelativeLayout) holder.getView().findViewById(R.id.item_compay_info_carM_container);
        ImageView choice= (ImageView) holder.getView().findViewById(R.id.item_compay_info_carM_choice);
        final TextView label= (TextView) holder.getView().findViewById(R.id.item_compay_info_carM_label);
        TextView type= (TextView) holder.getView().findViewById(R.id.item_compay_info_carM_carType);
        TextView school= (TextView) holder.getView().findViewById(R.id.item_compay_info_carM_school);
        TextView name= (TextView) holder.getView().findViewById(R.id.item_compay_info_carM_name);
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
        label.setText(item.getLicnum());
        type.setText(item.getPerdritype());
        school.setText(item.getRegion_name());
        name.setText(item.getCoach_name());
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
                    View view= LayoutInflater.from(context).inflate(R.layout.item_compay_info_car_detail,null);
                    TextView mlable= (TextView) view.findViewById(R.id.item_compay_info_car_detail_label);
                    TextView brand= (TextView) view.findViewById(R.id.item_compay_info_car_detail_brand);
                    TextView time= (TextView) view.findViewById(R.id.item_compay_info_car_detail_time);
                    TextView em= (TextView) view.findViewById(R.id.item_compay_info_car_detail_em);
                    TextView frame= (TextView) view.findViewById(R.id.item_compay_info_car_detail_frame);
                    TextView teacher= (TextView) view.findViewById(R.id.item_compay_info_car_detail_teacher);
                    TextView area= (TextView) view.findViewById(R.id.item_compay_info_car_detail_area);
                    TextView status= (TextView) view.findViewById(R.id.item_compay_info_car_detail_status);
                    mlable.setText(item.getLicnum());
                    brand.setText(item.getBrand());
                    time.setText(item.getBuydate());
                    em.setText(item.getEngnum());
                    frame.setText(item.getFranum());
                    teacher.setText(item.getCoach_name());
                    area.setText(item.getRegion_name());
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
