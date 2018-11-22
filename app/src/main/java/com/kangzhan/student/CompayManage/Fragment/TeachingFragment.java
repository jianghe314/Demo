package com.kangzhan.student.CompayManage.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.IInterface;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.kangzhan.student.CompayManage.TeachingManage.BookingOrderActivity;
import com.kangzhan.student.CompayManage.TeachingManage.EvaluActivity;
import com.kangzhan.student.CompayManage.TeachingManage.GradeInqueryActivity;
import com.kangzhan.student.CompayManage.TeachingManage.LearnHourRecordActivity;
import com.kangzhan.student.CompayManage.TeachingManage.RemarkActivity;
import com.kangzhan.student.CompayManage.TeachingManage.TestGrade;
import com.kangzhan.student.CompayManage.TeachingManage.TrainingOrderActivity;
import com.kangzhan.student.CompayManage.TeachingManage.TrainingRecordActivity;
import com.kangzhan.student.R;
import com.uuzuche.lib_zxing.decoding.InactivityTimer;

/**
 * Created by kangzhan011 on 2017/7/14.
 */

public class TeachingFragment extends Fragment implements View.OnClickListener{
    private View view;
    private LinearLayout part11,part12,part13,part14,part21,part22,part23,part24;
    private ImageView titleIv;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(view==null){
            System.gc();
            view=inflater.inflate(R.layout.compay_fragment_teaching,container,false);
            initView(view);
        }
        return view;
    }

    private void initView(View v) {
        titleIv= (ImageView) v.findViewById(R.id.fragment_teaching_tiv);
        part11= (LinearLayout) v.findViewById(R.id.compay_fragment_teaching11);
        part12= (LinearLayout) v.findViewById(R.id.compay_fragment_teaching12);
        part13= (LinearLayout) v.findViewById(R.id.compay_fragment_teaching13);
        part14= (LinearLayout) v.findViewById(R.id.compay_fragment_teaching14);
        part21= (LinearLayout) v.findViewById(R.id.compay_fragment_teaching21);
        part22= (LinearLayout) v.findViewById(R.id.compay_fragment_teaching22);
        part23= (LinearLayout) v.findViewById(R.id.compay_fragment_teaching23);
        part24= (LinearLayout) v.findViewById(R.id.compay_fragment_teaching24);
        part11.setOnClickListener(this);
        part12.setOnClickListener(this);
        part13.setOnClickListener(this);
        part14.setOnClickListener(this);
        part21.setOnClickListener(this);
        part22.setOnClickListener(this);
        part23.setOnClickListener(this);
        part24.setOnClickListener(this);
        Glide.with(getActivity().getApplicationContext()).load(R.drawable.compay_teaching_bg).into(titleIv);
    /*
        iv11= (ImageView) v.findViewById(R.id.compay_fragment_teaching_iv11);
        iv12= (ImageView) v.findViewById(R.id.compay_fragment_teaching_iv12);
        iv13= (ImageView) v.findViewById(R.id.compay_fragment_teaching_iv13);
        iv14= (ImageView) v.findViewById(R.id.compay_fragment_teaching_iv14);
        iv21= (ImageView) v.findViewById(R.id.compay_fragment_teaching_iv21);
        iv22= (ImageView) v.findViewById(R.id.compay_fragment_teaching_iv22);
        iv23= (ImageView) v.findViewById(R.id.compay_fragment_teaching_iv23);
        iv24= (ImageView) v.findViewById(R.id.compay_fragment_teaching_iv24);

        Glide.with(getActivity()).load(R.mipmap.compay_teaching_test).into(iv11);

        Glide.with(getActivity()).load(R.mipmap.compay_teachig_search).into(iv12);
        Glide.with(getActivity()).load(R.mipmap.compay_teach_booking).into(iv13);
        Glide.with(getActivity()).load(R.mipmap.compay_teaching_train).into(iv14);

        Glide.with(getActivity()).load(R.mipmap.compay_teach_train_record).into(iv21);
        Glide.with(getActivity()).load(R.mipmap.compay_learn_record).into(iv22);
        Glide.with(getActivity()).load(R.mipmap.compay_teaching_remark).into(iv23);
        Glide.with(getActivity()).load(R.mipmap.comapy_teaching_evalu).into(iv24);
        */

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.compay_fragment_teaching11:
                Intent test=new Intent(getContext(), TestGrade.class);
                startActivity(test);
                break;
            case R.id.compay_fragment_teaching12:
                Intent grade=new Intent(getContext(), GradeInqueryActivity.class);
                startActivity(grade);
                break;
            case R.id.compay_fragment_teaching13:
                Intent booking=new Intent(getContext(), BookingOrderActivity.class);
                startActivity(booking);
                break;
            case R.id.compay_fragment_teaching14:
                Intent training=new Intent(getContext(), TrainingOrderActivity.class);
                startActivity(training);
                break;
            case R.id.compay_fragment_teaching21:
                Intent record=new Intent(getContext(), TrainingRecordActivity.class);
                startActivity(record);
                break;
            case R.id.compay_fragment_teaching22:
                Intent hour=new Intent(getContext(), LearnHourRecordActivity.class);
                startActivity(hour);
                break;
            case R.id.compay_fragment_teaching23:
                //评价
                Intent evalu=new Intent(getContext(), EvaluActivity.class);
                startActivity(evalu);
                break;
            case R.id.compay_fragment_teaching24:
                //点评
                Intent remark=new Intent(getContext(), RemarkActivity.class);
                startActivity(remark);
                break;


        }
    }
}
