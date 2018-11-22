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
import com.kangzhan.student.School.Bean.EduTest;

import java.util.ArrayList;
import java.util.List;

import static com.kangzhan.student.R.id.school_test_detail_phone;

/**
 * Created by kangzhan011 on 2017/7/4.
 */

public class EduTestAdapter extends BaseRecyclerAdapter<EduTest> {
    private ArrayList<EduTest> data;
    private Context context;
    public EduTestAdapter(Context context, int layoutResId, ArrayList<EduTest> data) {
        super(context, layoutResId, data);
        this.data=data;
        this.context=context;
    }

    @Override
    protected void convert(BaseViewHolder holder, final EduTest item) {
        RelativeLayout container= (RelativeLayout) holder.getView().findViewById(R.id.school_edu_test_container);
        ImageView choice= (ImageView) holder.getView().findViewById(R.id.school_edu_test_choice);
        ImageView sex= (ImageView) holder.getView().findViewById(R.id.item_school_edu_test_sex);
        TextView name= (TextView) holder.getView().findViewById(R.id.item_school_edu_test_name);
        TextView time= (TextView) holder.getView().findViewById(R.id.item_school_edu_test_time);
        TextView carType= (TextView) holder.getView().findViewById(R.id.item_school_edu_test_carType);
        TextView grade= (TextView) holder.getView().findViewById(R.id.item_school_edu_test_subject);
        //
        if(item.isShow()){
            container.setVisibility(View.VISIBLE);
        }else {
            container.setVisibility(View.GONE);
        }
        if(item.getStudent_sex().equals("男")){
            sex.setImageResource(R.mipmap.boy);
        }else {
            sex.setImageResource(R.mipmap.girl);
        }
        if(item.isClick()){
            choice.setImageResource(R.mipmap.student_test_choice1);
        }else {
            choice.setImageResource(R.mipmap.student_test_choice);
        }
        name.setText(item.getStudent_name());
        time.setText("考试日期："+item.getExam_date());
        carType.setText("车型："+item.getTraintype());
        grade.setText(item.getStage_name()+" "+item.getQualified());
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
                    BottomSheetDialog dialog=new BottomSheetDialog(context);
                    View view= LayoutInflater.from(context).inflate(R.layout.item_school_test_detail,null);
                    TextView name= (TextView) view.findViewById(R.id.school_test_detail_name);
                    TextView sex= (TextView) view.findViewById(R.id.school_test_detail_sex);
                    TextView subjcet= (TextView) view.findViewById(R.id.school_test_detail_subject);
                    TextView time= (TextView) view.findViewById(R.id.school_test_detail_time);
                    TextView grade= (TextView) view.findViewById(R.id.school_test_detail_grade);
                    TextView ID= (TextView) view.findViewById(R.id.school_test_detail_ID);
                    TextView carType= (TextView) view.findViewById(R.id.school_test_detail_carType);
                    TextView phone= (TextView) view.findViewById(school_test_detail_phone);
                    TextView teahcher= (TextView) view.findViewById(R.id.school_test_detail_teacher);
                    TextView mclass= (TextView) view.findViewById(R.id.school_test_detail_class);
                    TextView p= (TextView) view.findViewById(R.id.school_test_detail_p);
                    name.setText(item.getStudent_name());
                    sex.setText(item.getStudent_sex());
                    subjcet.setText(item.getStage_name());
                    time.setText(item.getExam_date());
                    grade.setText(item.getQualified());
                    ID.setText(item.getStudent_idcard());
                    carType.setText(item.getTraintype());
                    phone.setText(item.getStudent_phone());
                    teahcher.setText(item.getStudent_enroller());
                    mclass.setText(item.getClass_name());
                    p.setText(item.getCoach_name());
                    dialog.setContentView(view);
                    dialog.setCancelable(true);
                    dialog.setCanceledOnTouchOutside(true);
                    dialog.show();
                }


            }
        });
    }
}
