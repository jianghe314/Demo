package com.kangzhan.student.School.Adapter;

import android.content.Context;
import android.support.design.widget.BottomSheetDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.holenzhou.pullrecyclerview.BaseRecyclerAdapter;
import com.holenzhou.pullrecyclerview.BaseViewHolder;
import com.kangzhan.student.R;
import com.kangzhan.student.School.Bean.EduCarManage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kangzhan011 on 2017/7/3.
 */

public class EduCarManageAdapter extends BaseRecyclerAdapter<EduCarManage> {
    private ArrayList<EduCarManage> data;
    private Context context;
    public EduCarManageAdapter(Context context, int layoutResId, ArrayList<EduCarManage> data) {
        super(context, layoutResId, data);
        this.context=context;
        this.data=data;
    }

    @Override
    protected void convert(BaseViewHolder holder, final EduCarManage item) {
        ImageView choice= (ImageView) holder.getView().findViewById(R.id.edu_car_manage_choice);
        TextView carLable= (TextView) holder.getView().findViewById(R.id.edu_car_manage_carLabel);
        TextView carType= (TextView) holder.getView().findViewById(R.id.edu_car_manage_carType);
        TextView school= (TextView) holder.getView().findViewById(R.id.edu_car_manage_school);
        TextView teacher= (TextView) holder.getView().findViewById(R.id.edu_car_manage_name);
        if(item.isShow()){
            choice.setVisibility(View.VISIBLE);
        }else {
            choice.setVisibility(View.GONE);
        }

        if(item.isClick()){
            choice.setImageResource(R.mipmap.student_test_choice1);
        }else {
            choice.setImageResource(R.mipmap.student_test_choice);
        }
        //
        carLable.setText(item.getLicnum());
        carType.setText(item.getPerdritype());
        school.setText(item.getRegion_name());
        teacher.setText(item.getCoach_name());
       //
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
                    //查看详情
                    BottomSheetDialog bottomDialog=new BottomSheetDialog(context);
                    View view= LayoutInflater.from(context).inflate(R.layout.item_school_edu_car_detail,null);
                    TextView label= (TextView) view.findViewById(R.id.school_edu_car_detail_label);
                    TextView brand= (TextView) view.findViewById(R.id.school_edu_car_detail_brand);
                    TextView buyT= (TextView) view.findViewById(R.id.school_edu_car_detail_buyT);
                    TextView carEm= (TextView) view.findViewById(R.id.school_edu_car_detail_num);
                    TextView carFrameN= (TextView) view.findViewById(R.id.school_edu_car_detail_frameN);
                    TextView carType= (TextView) view.findViewById(R.id.school_edu_car_detail_type);
                    TextView teacher= (TextView) view.findViewById(R.id.school_edu_car_detail_teacher);
                    TextView timeN= (TextView) view.findViewById(R.id.school_edu_car_detail_timeN);
                    TextView area= (TextView) view.findViewById(R.id.school_edu_car_detail_area);
                    try{
                        label.setText(item.getLicnum());
                        brand.setText(item.getBrand());
                        buyT.setText(item.getBuydate());
                        carEm.setText(item.getEngnum());
                        carFrameN.setText(item.getFranum());
                        carType.setText(item.getPerdritype());
                        teacher.setText(item.getCoach_name());
                        timeN.setText(item.getDevnum());
                        area.setText(item.getRegion_name());
                    }catch (NullPointerException e){
                        e.printStackTrace();
                    }
                    bottomDialog.setContentView(view);
                    bottomDialog.setCanceledOnTouchOutside(true);
                    bottomDialog.setCancelable(true);
                    bottomDialog.show();

                }


            }
        });


    }
}
